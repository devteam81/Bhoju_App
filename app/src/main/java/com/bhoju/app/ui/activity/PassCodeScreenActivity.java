package com.bhoju.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.download.DownloadUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassCodeScreenActivity extends AppCompatActivity {

    private final String TAG = PassCodeScreenActivity.class.getSimpleName();
    @BindViews({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9})
    List<View> btnNumPads;

    @BindView(R.id.otp_view)
    OtpTextView otp_view;

    @BindView(R.id.txt_forgot_pin)
    TextView txt_forgot_pin;

    @BindView(R.id.txt_info)
    TextView txt_info;

    @BindView(R.id.btn_clear)
    CardView btn_clear;

    /*@BindView(R.id.btn_tick)
    CardView btn_tick;*/

    /*@BindView(R.id.btn_back)
    ImageView btn_back;*/

    private String TRUE_CODE = "";
    private final int MAX_LENGTH = 6;
    private String codeString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code_screen);
        ButterKnife.bind(this);

        TRUE_CODE =  PrefUtils.getInstance(this).getStringValue(PrefKeys.PASS_CODE, "");

        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                //Toast.makeText(PassCodeActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();
                if (codeString.equals(TRUE_CODE)) {
                    if(getIntent().getExtras().getBoolean("screen")) {
                        PrefUtils prefUtils = PrefUtils.getInstance(PassCodeScreenActivity.this);
                        prefUtils.setValue(PrefKeys.IS_PASS,false);
                        prefUtils.setValue(PrefKeys.PASS_CODE,"");
                        Toast.makeText(PassCodeScreenActivity.this, "Pass code disabled", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        intent.putExtra("data", true);
                        setResult(RESULT_OK, intent);
                    }else {
                        Toast.makeText(PassCodeScreenActivity.this, "Code is right", Toast.LENGTH_SHORT).show();
                        Intent splashIntent = new Intent(PassCodeScreenActivity.this, MainActivity.class);
                        startActivity(splashIntent);
                    }

                    finish();

                } else {
                    Toast.makeText(PassCodeScreenActivity.this, "Wrong Pass code", Toast.LENGTH_SHORT).show();
                    //vibrate the dots layout
                    shakeAnimation();
                }
            }
        });

        txt_forgot_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(PassCodeScreenActivity.this, SupportActivity.class));
                finish();*/
                Dialog dialog = new Dialog(PassCodeScreenActivity.this);

                dialog.setContentView(R.layout.dialog_forgot_exit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(true);

                dialog.findViewById(R.id.btnYes).setOnClickListener((View v) -> {
                    dialog.dismiss();
                    doLogOutUser();
                });
                dialog.findViewById(R.id.btnNo).setOnClickListener((View v) -> {
                    dialog.dismiss();
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeString.length() > 0) {
                    //remove last character of code
                    codeString = removeLastChar(codeString);
                    otp_view.setOTP(codeString);
                }
            }
        });

        if(getIntent().getExtras().getBoolean("screen"))
        {
            txt_forgot_pin.setVisibility(View.GONE);
            txt_info.setText(getResources().getString(R.string.disable_lock));
        }


        /*btn_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeString.equals(TRUE_CODE)) {
                    if(getIntent().getExtras().getBoolean("screen")) {
                        Intent intent = getIntent();
                        intent.putExtra("data", true);
                        setResult(RESULT_OK, intent);
                    }else {
                        Toast.makeText(PassCodeScreenActivity.this, "Code is right", Toast.LENGTH_SHORT).show();
                        Intent splashIntent = new Intent(PassCodeScreenActivity.this, MainActivity.class);
                        startActivity(splashIntent);
                    }

                    finish();

                } else {
                    Toast.makeText(PassCodeScreenActivity.this, "Wrong Pass code", Toast.LENGTH_SHORT).show();
                    //vibrate the dots layout
                    shakeAnimation();
                }
            }
        });*/

        /*btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }


    @OnClick({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9})
    public void onClick(CardView button) {
        getStringCode(button.getId());
        otp_view.setOTP(codeString);
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.vibrate_anim);
        findViewById(R.id.otp_view).startAnimation(shake);
        //Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
    }

    private void getStringCode(int buttonId) {
        if(codeString.length()<MAX_LENGTH) {
            //switch (buttonId) {
            if(buttonId == R.id.btn0)
                codeString += "0";
            else if(buttonId == R.id.btn1)
                codeString += "1";
            else if(buttonId == R.id.btn2)
                codeString += "2";
            else if(buttonId == R.id.btn3)
                codeString += "3";
            else if(buttonId == R.id.btn4)
                codeString += "4";
            else if(buttonId == R.id.btn5)
                codeString += "5";
            else if(buttonId == R.id.btn6)
                codeString += "6";
            else if(buttonId == R.id.btn7)
                codeString += "7";
            else if(buttonId == R.id.btn8)
                codeString += "8";
            else if(buttonId == R.id.btn9)
                codeString += "9";
            //}
        }
    }

    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    protected void doLogOutUser() {
        UiUtils.showLoadingDialog(this);
        PrefUtils preferences = PrefUtils.getInstance(this);
        if( preferences.getIntValue(PrefKeys.USER_ID, -1)!=-1) {
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
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
                            UiUtils.showShortToast(PassCodeScreenActivity.this, logoutResponse.optString(APIConstants.Params.MESSAGE));
                            logOutUserInDevice();
                        } else {
                            UiUtils.showShortToast(PassCodeScreenActivity.this, logoutResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                        }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    UiUtils.hideLoadingDialog();
                    NetworkUtils.onApiError(PassCodeScreenActivity.this);
                }
            });
        }else
        {
            logOutUserInDevice();
        }
    }

    private void logOutUserInDevice() {
        PrefHelper.setUserLoggedOut(this);
        Intent restartActivity = new Intent(this, SplashActivity.class);
        restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restartActivity);
        finish();
    }
}