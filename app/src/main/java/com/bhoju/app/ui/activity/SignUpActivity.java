package com.bhoju.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bhoju.app.util.sharedpref.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.PrefKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;

public class SignUpActivity extends BaseActivity {

    private final String TAG = SignUpActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_re_password)
    EditText edRePassword;
    @BindView(R.id.termsPrivacy)
    TextView termsPrivacy;
    @BindView(R.id.termsConditionCheck)
    CheckBox termsConditionCheckbox;

    @BindView(R.id.ed_phone)
    EditText edPhone;

    @BindView(R.id.txt_login)
    TextView txt_login;

    APIInterface apiInterface;

    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    EditText referralCode;
    String code;
    Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        img_back.setOnClickListener(v -> {
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));});


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        //Fb setup
        callbackManager = CallbackManager.Factory.create();
        logoutfromFacebook();

        SpannableString terms_Privacy = new SpannableString(getString(R.string.agree_the_terms_and_condition));
        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));

            }
        };
        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.PRIVACY));

            }
        };
        terms_Privacy.setSpan(teremsAndCondition, 31, 36, 0);
        terms_Privacy.setSpan(privacy, 41, 55, 0);
        terms_Privacy.setSpan(new ForegroundColorSpan(getColor(R.color.color_blue)), 31, 36, 0);
        terms_Privacy.setSpan(new ForegroundColorSpan(getColor(R.color.color_blue)), 41, 55, 0);

        termsPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        termsPrivacy.setText(terms_Privacy, TextView.BufferType.SPANNABLE);
        termsPrivacy.setSelected(true);

        //Google setup
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();

    }


    @OnClick({R.id.btn_google_sign, R.id.btn_facebook, R.id.submit_btn,R.id.txt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_google_sign:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.btn_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));
                //LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        /*if (AccessToken.getCurrentAccessToken() != null) {
                            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
                                Timber.d("%s", object);
                                doSocialLoginUser(object.optString("id")
                                        , object.optString("email")
                                        , object.optString("name")
                                        , "https://graph.facebook.com/" + object.optString("id") + "/picture?type=large"
                                        , Constants.FACEBOOK_LOGIN);
                            });
                            Bundle params = new Bundle();
                            params.putString("fields", "id,name,link,email,picture");
                            request.setParameters(params);
                            request.executeAsync();
                            onBackPressed();
                        }*/
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        UiUtils.showShortToast(SignUpActivity.this, getString(R.string.login_cancelled));
                    }
                });
                break;
            case R.id.submit_btn:
                if (validateFields()) {
                    doSignUpUser();
                }
                break;
            case R.id.txt_login:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    /*private void dialogrefferal() {
        dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_refferal_code);
        referralCode = dialog.findViewById(R.id.refrrealCode);
        Button apply = dialog.findViewById(R.id.applyCode);
        Button removeCode = dialog.findViewById(R.id.removeCode);
        ImageView dismissDialog = dialog.findViewById(R.id.dismissDialog);
        referralCode.setText(code);
        apply.setOnClickListener(v ->{
            code = referralCode.getText().toString();
            applyReferralCode(code);

        });

        removeCode.setOnClickListener(v -> {
            refferalCode.setText(getString(R.string.gotAReferralCode));
            code="";
            referralCode.clearAnimation();
           // dialog.dismiss();
            refferalCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            referralCode.setText("");
        });

        dismissDialog.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }*/

   /* protected void applyReferralCode(String referralCode) {
        UiUtils.showLoadingDialog(this);
        PrefUtils preferences = PrefUtils.getInstance(this);
        Call<String> call = apiInterface.applyReferralCode(referralCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject referralResponse = null;
                try {
                    referralResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (referralResponse != null)
                    if (referralResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        Toast.makeText(SignUpActivity.this, referralResponse.optString(Params.MESSAGE), Toast.LENGTH_SHORT).show();
                        if(dialog != null)
                            dialog.dismiss();
                        refferalCode.setText(getString(R.string.referralCodeIsApplied));
                        refferalCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_color));
                    } else {
                        Toast.makeText(SignUpActivity.this, referralResponse.optString(Params.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(SignUpActivity.this);
            }
        });
    }*/


    private void doSignUpUser() {
        UiUtils.showLoadingDialog(this);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(this);
        Map<String, String> params = new HashMap<>();
        params.put(APIConstants.Params.EMAIL, "");
        params.put(APIConstants.Params.PASSWORD, edPassword.getText().toString());
        params.put(APIConstants.Params.NAME, edName.getText().toString());
        params.put(APIConstants.Params.MOBILE, edPhone.getText().toString());
        params.put(APIConstants.Params.LOGIN_BY, APIConstants.Constants.MANUAL_LOGIN);
        params.put(APIConstants.Params.DEVICE_TYPE, APIConstants.Constants.ANDROID);
        params.put(APIConstants.Params.DEVICE_TOKEN, prefUtils.getStringValue(PrefKeys.FCM_TOKEN,""));
        params.put(APIConstants.Params.TIME_ZONE, TimeZone.getDefault().getID());
        params.put(APIConstants.Params.REFERRAL_CODE, "");
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(APIConstants.Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
        params.put(APIConstants.Params.APPVERSION, deviceDetails.get(APPVERSION));
        params.put(VERSION_CODE, deviceDetails.get(VERSION_CODE));
        params.put(APIConstants.Params.DEVICE, deviceDetails.get(DEVICE));
        params.put(APIConstants.Params.BEBUEXT, deviceDetails.get(BEBUEXT));
        params.put(APIConstants.Params.BRAND, deviceDetails.get(BRAND));
        params.put(APIConstants.Params.MODEL, deviceDetails.get(MODEL));
        params.put(APIConstants.Params.VERSION, deviceDetails.get(VERSION));
        params.put(APIConstants.Params.PLAT, deviceDetails.get(PLAT));
        params.put(APIConstants.Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.signUpUser(APIConstants.APIs.REGISTER, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject signUpResponse = null;
                try {
                    signUpResponse = new JSONObject(response.body());

                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (signUpResponse != null) {
                    if (signUpResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(SignUpActivity.this, signUpResponse.optString(Params.MESSAGE));
                        Intent toHome = new Intent(SignUpActivity.this, MainActivity.class);
                        toHome.putExtra(MainActivity.FIRST_TIME,  true);
                        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toHome);
                    } else {
                        UiUtils.showShortToast(SignUpActivity.this, signUpResponse.optString(Params.ERROR_MESSAGE));
                        if (!signUpResponse.optString(Params.ERROR_MESSAGE).contains("taken")) {
                            // clearAllFields();
                            //  finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(SignUpActivity.this);
            }
        });
    }

    private void clearAllFields() {
        edName.setText("");
        edPhone.setText("");
        edEmail.setText("");
        edPassword.setText("");
        edRePassword.setText("");
    }

    private boolean validateFields() {
        /*if (edEmail.getText().toString().trim().length() == 0) {
            UiUtils.showShortToast(this, getString(R.string.email_cant_be_empty));
            return false;
        }
        if (!AppUtils.isValidEmail(edEmail.getText().toString())) {
            UiUtils.showShortToast(this, getString(R.string.enter_valid_email));
            return false;
        }*/
        if (edName.getText().toString().trim().length() == 0) {
            UiUtils.showShortToast(this, getString(R.string.names_cant_be_empty));
            return false;
        }
        if(edPhone.getText().toString().trim().length()==0){
            UiUtils.showShortToast(this,getString(R.string.enter_phone_no));
        }

        //Phone validation
        String phone = edPhone.getText().toString().trim();
        if (phone.length() != 0 && (phone.length() < 6 || phone.length() > 16)) {
            UiUtils.showShortToast(this, getString(R.string.phone_cant_be_stuff));
            return false;
        }

        if (edPassword.getText().toString().trim().length() == 0) {

            UiUtils.showShortToast(this, getString(R.string.password_cant_be_empty));
            return false;
        }
        if (edPassword.getText().toString().length() < 6) {
            UiUtils.showShortToast(this, getString(R.string.minimum_six_characters));
            return false;
        }

        if (edRePassword.getText().toString().trim().length() == 0) {

            UiUtils.showShortToast(this, getString(R.string.password_cant_be_empty));
            return false;
        }

        if (!edPassword.getText().toString().equals(edRePassword.getText().toString())) {
            UiUtils.showShortToast(this, getString(R.string.match_passwords));
            return false;
        }

        if (termsConditionCheckbox.isChecked()){
            return true;
        } else {
            Toast.makeText(SignUpActivity.this, R.string.agree_the_terms_and_condition,Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> account = GoogleSignIn.getSignedInAccountFromIntent(data);
                firebaseAuthWithGoogle(account.getResult().getIdToken());
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        handleSignInResult(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        UiUtils.showShortToast(SignUpActivity.this, getString(R.string.login_cancelled));
                        UiUtils.log(TAG, "onComplete: ");
                    }
                    task.addOnFailureListener(e -> UiUtils.log(TAG, "onFailure: " + e.getMessage()));
                    // ...
                });
    }

    private void handleSignInResult(FirebaseUser firebaseUser) {
        try {
            String photoImg;
            try {
                photoImg = firebaseUser.getPhotoUrl().toString();
            } catch (Exception e) {
                UiUtils.log(TAG, "handleSignInResult:   "+e.getMessage());
                UiUtils.log(TAG, Log.getStackTraceString(e));
                photoImg = "";
            }
            doSocialLoginUser(firebaseUser.getUid()
                    , firebaseUser.getEmail()
                    , firebaseUser.getDisplayName()
                    , photoImg
                    , APIConstants.Constants.GOOGLE_LOGIN);
        } catch (Exception e) {
            UiUtils.log(TAG, "handleSignInResult: "+e.getMessage());
            UiUtils.showShortToast(SignUpActivity.this, getString(R.string.login_cancelled));
        }
    }

    protected void doSocialLoginUser(String socialUniqueId, String email, String name, String picture, String loginBy) {
        UiUtils.showLoadingDialog(this);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(this);
        Map<String, String> params = new HashMap<>();
        params.put(Params.SOCIAL_UNIQUE_ID, socialUniqueId);
        params.put(Params.LOGIN_BY, loginBy);
        params.put(Params.EMAIL, email);
        params.put(Params.NAME, name);
        params.put(Params.MOBILE,"");
        params.put(Params.PICTURE, picture);
        params.put(Params.DEVICE_TYPE, APIConstants.Constants.ANDROID);
        params.put(Params.DEVICE_TOKEN, prefUtils.getStringValue(PrefKeys.FCM_TOKEN,""));
        params.put(Params.TIME_ZONE, TimeZone.getDefault().getID());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
        params.put(Params.APPVERSION, deviceDetails.get(APPVERSION));
        params.put(VERSION_CODE, deviceDetails.get(VERSION_CODE));
        params.put(Params.DEVICE, deviceDetails.get(DEVICE));
        params.put(Params.BEBUEXT, deviceDetails.get(BEBUEXT));
        params.put(Params.BRAND, deviceDetails.get(BRAND));
        params.put(Params.MODEL, deviceDetails.get(MODEL));
        params.put(Params.VERSION, deviceDetails.get(VERSION));
        params.put(Params.PLAT, deviceDetails.get(PLAT));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.socialLoginUser(APIConstants.APIs.SOCIAL_LOGIN, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject socialLoginResponse = null;
                try {
                    socialLoginResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (socialLoginResponse != null) {
                    if (socialLoginResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(SignUpActivity.this, getString(R.string.welcome) + name + "!");
                        loginUserInDevice(socialLoginResponse, loginBy);
                    } else {
                        UiUtils.showShortToast(SignUpActivity.this, socialLoginResponse.optString(Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
            }
        });
    }

    private void loginUserInDevice(JSONObject data, String loginBy) {
        PrefHelper.setUserLoggedIn(this, data.optInt(Params.USER_ID)
                , data.optString(Params.TOKEN)
                , data.optString(Params.LOGIN_TOKEN)
                , loginBy
                , data.optString(Params.EMAIL)
                , data.optString(Params.NAME)
                , data.optString(Params.MOBILE)
                , data.optString(Params.DESCRIPTION)
                , data.optString(Params.PICTURE)
                , data.optString(APIConstants.Params.AGE)
                , data.optString(Params.NOTIF_PUSH_STATUS)
                , data.optString(Params.NOTIF_PUSH_STATUS)
                , data.optString(APIConstants.Params.DEFAULT_EMAIL)
                , data.optString(APIConstants.Params.DEFAULT_MOBILE));
        /*Intent toHome = new Intent(this, MainActivity.class);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toHome);*/
        finish();
    }

    public void logoutfromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> LoginManager.getInstance().logOut()).executeAsync();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        UiUtils.log(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        UiUtils.log(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        handleSignInResult(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        UiUtils.log(TAG, "signInWithCredential:failure: "+ task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }

}