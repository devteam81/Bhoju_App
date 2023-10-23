package com.bhoju.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.Params;

public class ChangePasswordActivity extends AppCompatActivity {

    private final String TAG = ChangePasswordActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rl_old_password)
    RelativeLayout rl_old_password;

    @BindView(R.id.ed_old_password)
    EditText edOldPassword;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_re_password)
    EditText edRePassword;
    String id, from;
    APIInterface apiInterface;
    PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);

        from = getIntent().getExtras().getString(APIConstants.Params.DATA);
        //if(!from.equalsIgnoreCase("OtpVerificationActivity"))
        id = getIntent().getExtras().getString(APIConstants.Params.ID);

        if(prefUtils.getIntValue(PrefKeys.USER_ID, -1) ==-1)
            rl_old_password.setVisibility(View.GONE);
        else
            rl_old_password.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.img_old_password, R.id.img_new_password, R.id.img_re_password})
    public void showHidePass(View view){

        if(view.getId()==R.id.img_old_password){

            if(edOldPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_show);
                //Show Password
                edOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_hide);
                //Hide Password
                edOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }else if(view.getId()==R.id.img_new_password){

            if(edPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_show);
                //Show Password
                edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_hide);
                //Hide Password
                edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }else if(view.getId()==R.id.img_re_password){

            if(edRePassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_show);
                //Show Password
                edRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_eye_hide);
                //Hide Password
                edRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    @OnClick({R.id.submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if (validateFields()) {
                    if(from.equalsIgnoreCase("OtpVerificationActivity"))
                        changePasswordInBackendNew();
                        //Toast.makeText(this,"Changed",Toast.LENGTH_LONG).show();
                    else
                        changePasswordInBackend();
                }
                break;
        }
    }

    private boolean validateFields() {
        if (edPassword.getText().toString().trim().length() == 0) {

            UiUtils.showShortToast(ChangePasswordActivity.this, getString(R.string.password_cant_be_empty));
            return false;
        }
        if (edPassword.getText().toString().length() < 6) {
            UiUtils.showShortToast(ChangePasswordActivity.this, getString(R.string.minimum_six_characters));
            return false;
        }

        if (!edPassword.getText().toString().equals(edRePassword.getText().toString())) {
            UiUtils.showShortToast(ChangePasswordActivity.this, getString(R.string.match_passwords));
            return false;
        }
        return true;
    }

    private void changePasswordInBackendNew() {
        UiUtils.showLoadingDialog(ChangePasswordActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, Integer.parseInt(id));
        params.put(Params.PASSWORD, edPassword.getText().toString());
        params.put(Params.CONFIRM_PASSWORD, edRePassword.getText().toString());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.changePasswordNew(APIConstants.APIs.CHANGE_PASSWORD_NEW, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject changePasswordResponse = null;
                try {
                    changePasswordResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (changePasswordResponse != null) {
                    if (changePasswordResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(ChangePasswordActivity.this, changePasswordResponse.optString(APIConstants.Params.MESSAGE));
                        //doLogOutUser();
                        logOutUserInDevice();
                    } else {
                        UiUtils.showShortToast(ChangePasswordActivity.this, changePasswordResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(ChangePasswordActivity.this);
            }
        });
    }

    private void changePasswordInBackend() {
        UiUtils.showLoadingDialog(ChangePasswordActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.OLD_PASSWORD, edOldPassword.getText().toString());
        params.put(Params.PASSWORD, edPassword.getText().toString());
        params.put(Params.CONFIRM_PASSWORD, edRePassword.getText().toString());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.changePassword(APIConstants.APIs.CHANGE_PASSWORD, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject changePasswordResponse = null;
                try {
                    changePasswordResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (changePasswordResponse != null) {
                    if (changePasswordResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(ChangePasswordActivity.this, changePasswordResponse.optString(APIConstants.Params.MESSAGE));
                        doLogOutUser();
                        //logOutUserInDevice();
                    } else {
                        UiUtils.showShortToast(ChangePasswordActivity.this, changePasswordResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(ChangePasswordActivity.this);
            }
        });
    }

    protected void doLogOutUser() {
        UiUtils.showLoadingDialog(ChangePasswordActivity.this);
        PrefUtils preferences = PrefUtils.getInstance(ChangePasswordActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, preferences.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.LANGUAGE, preferences.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.logOutUser(APIConstants.APIs.LOGOUT,params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject logoutResponse = null;
                try {
                    logoutResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (logoutResponse != null)
                    if (logoutResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showLongToast(ChangePasswordActivity.this, getString(R.string.login_again_with_new_password));
                        logOutUserInDevice();
                    } else {
                        UiUtils.showShortToast(ChangePasswordActivity.this, logoutResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(ChangePasswordActivity.this);
            }
        });
    }

    private void logOutUserInDevice() {
        PrefHelper.setUserLoggedOut(ChangePasswordActivity.this);
        Intent restartActivity = new Intent(ChangePasswordActivity.this, SplashActivity.class);
        restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restartActivity);
        finish();
    }
}