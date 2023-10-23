package com.bhoju.app.ui.activity;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;

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
import static com.bhoju.app.network.APIConstants.Params.DEVICE_CODE;
import static com.bhoju.app.network.APIConstants.Params.MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.REFERRER;
import static com.bhoju.app.network.APIConstants.Params.RESPONSE;
import static com.bhoju.app.util.UiUtils.checkString;
import static com.bhoju.app.util.sharedpref.Utils.getSecureId;

public class OtpVerificationActivity extends BaseActivity {

    private final String TAG = OtpVerificationActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.ed_otp)
    EditText ed_otp;
    @BindView(R.id.resend_btn)
    Button resend_btn;
    String id,mobile;
    String data;
    APIInterface apiInterface;

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equalsIgnoreCase("otp")) {
//                final String message = intent.getStringExtra(MESSAGE);
//                UiUtils.log(TAG,""+message);
//                // message is the fetching OTP
//                ed_otp.setText(message);
//                //sendConfirmationOTP();
//            }
//        }
//    };

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            resend_btn.setText("Resend OTP in " + millisUntilFinished / 1000);
            //resend_btn.setBackgroundTintList(AppCompatResources.getColorStateList(OtpVerificationActivity.this,R.color.buttonColor));
            resend_btn.setEnabled(false);
            resend_btn.setClickable(false);
            //here you can have your logic to set text to edittext
        }

        public void onFinish() {
            resend_btn.setText(getResources().getString(R.string.send_otp_again));
            //resend_btn.setBackgroundTintList(AppCompatResources.getColorStateList(OtpVerificationActivity.this,R.color.button_color));
            resend_btn.setEnabled(true);
            resend_btn.setClickable(true);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        id = getIntent().getExtras().getString(APIConstants.Params.ID);
        mobile = getIntent().getExtras().getString(APIConstants.Params.MOBILE);

        if(getIntent().getExtras().getString(RESPONSE) != null)
            data = getIntent().getExtras().getString(RESPONSE);

        phone.setText(mobile);
        timer.start();
        //ed_otp.setText(getIntent().getExtras().getString(APIConstants.Params.OTP));
    }

//    @Override
//    public void onResume() {
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
//    }

    @OnClick({R.id.submit_btn, R.id.resend_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if (validateFields()) {
                    sendConfirmationOTP();
                }
                break;
            case R.id.resend_btn:
                sendConfirmationMail();
                timer.start();
                break;
        }
    }

    private boolean validateFields() {
        //Phone validation
        if(ed_otp.getText().toString().trim().length()==0){
            UiUtils.showShortToast(OtpVerificationActivity.this,getString(R.string.enter_otp));
            return false;
        }
        if (ed_otp.getText().toString().length() != 6) {
            UiUtils.showShortToast(OtpVerificationActivity.this, getString(R.string.minimum_six_otp_characters));
            return false;
        }
        return true;
    }

    private void sendConfirmationOTP() {
        UiUtils.showLoadingDialog(this);
        Map<String, String> params = new HashMap<>();
        params.put(Params.MOBILENO, mobile);
        params.put(Params.OTP, ed_otp.getText().toString());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(REFERRER, checkString(prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""))? getResources().getString(R.string.app_name) : prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""));
        params.put(DEVICE_CODE, getSecureId(OtpVerificationActivity.this));

        Call<String> call = apiInterface.verifyOtp(APIConstants.APIs.VERIFY_OTP, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject forgotPasswordResponse = null;
                try {
                    forgotPasswordResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (forgotPasswordResponse != null) {
                    if (forgotPasswordResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        //UiUtils.showShortToast(OtpVerificationActivity.this, forgotPasswordResponse.optString(APIConstants.Params.MESSAGE));
                        if(data==null) {
                            Intent i = new Intent(OtpVerificationActivity.this, ChangePasswordActivity.class);
                            i.putExtra(APIConstants.Params.DATA, TAG);
                            i.putExtra(APIConstants.Params.ID, forgotPasswordResponse.optString(APIConstants.Params.ID));
                            i.putExtra(APIConstants.Params.OTP, forgotPasswordResponse.optString(APIConstants.Params.OTP));
                            startActivity(i);
                            finish();
                        }else
                        {
                            sendStatusBack();
                        }
                    } else {
                        //UiUtils.showShortToast(OtpVerificationActivity.this, "OTP does not match");
                        UiUtils.showShortToast(OtpVerificationActivity.this, forgotPasswordResponse.optString(APIConstants.Params.MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(OtpVerificationActivity.this);
            }
        });
    }

    private void sendConfirmationMail() {
        UiUtils.showLoadingDialog(this);
        Map<String, String> params = new HashMap<>();
        params.put(Params.MOBILE, mobile);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.forgotPassword(APIConstants.APIs.FORGOT_PASSWORD, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject forgotPasswordResponse = null;
                try {
                    forgotPasswordResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (forgotPasswordResponse != null) {
                    if (forgotPasswordResponse.optString(Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(OtpVerificationActivity.this, "OTP has been sent");
                    } else {
                        UiUtils.showShortToast(OtpVerificationActivity.this, forgotPasswordResponse.optString(Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(OtpVerificationActivity.this);
            }
        });
    }

    private void sendStatusBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESPONSE,data);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}