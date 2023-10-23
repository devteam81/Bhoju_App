package com.bhoju.app.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bhoju.app.gcm.MessagingServices;
import com.bhoju.app.ui.activity.OtpVerificationActivity;
import com.bhoju.app.util.sharedpref.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.ui.activity.NewLoginActivity;
import com.bhoju.app.ui.activity.WebViewActivity;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.USER_SUBSCRIPTION;
import static com.bhoju.app.network.APIConstants.Constants.MANUAL_LOGIN;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.DEVICE_CODE;
import static com.bhoju.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.REFERRER;
import static com.bhoju.app.network.APIConstants.Params.RESPONSE;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;
import static com.bhoju.app.util.Fragments.LOGIN_FRAGMENTS;
import static com.bhoju.app.util.UiUtils.checkString;
import static com.bhoju.app.util.sharedpref.Utils.getSecureId;

public class SignupFragment extends Fragment {

    private final String TAG = SignupFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;
    private static final int MANUAL_SIGN_IN = 101;

    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_re_password)
    EditText edRePassword;
    @BindView(R.id.termsCondition)
    TextView termsCondition;
    @BindView(R.id.termsPrivacy)
    TextView termsPrivacy;
    @BindView(R.id.termsConditionCheck)
    CheckBox termsConditionCheckbox;

    @BindView(R.id.ed_phone)
    EditText edPhone;

    @BindView(R.id.txtGoogle)
    TextView txtGoogle;
    @BindView(R.id.txtFacebook)
    TextView txtFacebook;

    @BindView(R.id.ll_details)
    LinearLayoutCompat ll_details;
    @BindView(R.id.ll_welcome)
    LinearLayoutCompat ll_welcome;


    @BindView(R.id.login_txt)
    TextView loginTxt;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.txt_play)
    TextView txt_play;

    @BindView(R.id.ll_google_sign)
    LinearLayoutCompat ll_google_sign;
    @BindView(R.id.ll_facebook_sign)
    LinearLayoutCompat ll_facebook_sign;

    String Coming;


    private NewLoginActivity activity;
    private Unbinder unbinder;
    private APIInterface apiInterface;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    private PrefUtils prefUtils;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (NewLoginActivity) getActivity();

        Intent intent = activity.getIntent();
        Coming = intent.getStringExtra("Coming");

        prefUtils = PrefUtils.getInstance(activity);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        //Fb setup
        callbackManager = CallbackManager.Factory.create();

        //Google setup
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();
        if(prefUtils.getStringValue(PrefKeys.FCM_TOKEN,"").equalsIgnoreCase(""))
            MessagingServices.getFCMToken(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        String termsAndConditions = getResources().getString(R.string.terms_conditions);
        String privacyPolicy = getResources().getString(R.string.privacy_policy);
        String refundPolicy = getResources().getString(R.string.refund_policy);

        String policy = getResources().getString(R.string.agree_text) + " "+ termsAndConditions +" and " + privacyPolicy +" and " + refundPolicy;
        SpannableString ss = new SpannableString(policy);
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.PRIVACY));
            }
        };

        ClickableSpan span3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.REFUND));
            }
        };

        ss.setSpan(span1, 13, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 36, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span3, 55, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsCondition.setText(ss);
        termsCondition.setMovementMethod(LinkMovementMethod.getInstance());

        String login = getResources().getString(R.string.login);
        SpannableString sslogin = new SpannableString(login);
        ClickableSpan spanLogin = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                activity.replaceFragmentWithAnimation(new LoginFragment(), LOGIN_FRAGMENTS[1], false);
            }
        };
        sslogin.setSpan(spanLogin, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginBtn.setText(sslogin);
        loginBtn.setMovementMethod(LinkMovementMethod.getInstance());

        checkWidth();
        return view;
    }

    @OnClick({R.id.img_password, R.id.img_re_password})
    public void showHidePass(View view){

        if(view.getId()==R.id.img_password){

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

    @OnClick({R.id.ll_google_sign, R.id.ll_facebook_sign, R.id.submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_google_sign:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                UiUtils.log(TAG, "Data--> "+ signInIntent.toString());
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.ll_facebook_sign:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"/*, "user_friends"*/));
                // LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
                                UiUtils.log(TAG,"Object--> "+object);
                                doSocialLoginUser(object.optString("id")
                                        , object.optString("email")
                                        , object.optString("name")
                                        , "https://graph.facebook.com/" + object.optString("id") + "/picture?type=large"
                                        , APIConstants.Constants.FACEBOOK_LOGIN);
                            });
                            Bundle params = new Bundle();
                            params.putString("fields", "id,name,link,email,picture");
                            request.setParameters(params);
                            request.executeAsync();
                        }
                        UiUtils.log(TAG, "onSuccess: ");
                        //handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        UiUtils.log(TAG, "onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        UiUtils.log(TAG, "onError: "+error.getLocalizedMessage());
                    }
                });
                break;
            case R.id.submit_btn:
                if (validateFields()) {
                    doSignUpUser();
                }
                break;
            /*case R.id.login_btn:
                activity.replaceFragmentWithAnimation(new LoginFragment(), LOGIN_FRAGMENTS[1], false);
                break;*/
        }
    }

    private void doSignUpUser() {
        UiUtils.showLoadingDialog(activity);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(activity);
        Map<String, String> params = new HashMap<>();
        params.put(Params.EMAIL, "");
        params.put(Params.PASSWORD, edPassword.getText().toString());
        params.put(Params.NAME, edName.getText().toString());
        params.put(Params.MOBILE, edPhone.getText().toString());
        params.put(Params.LOGIN_BY, MANUAL_LOGIN);
        params.put(Params.DEVICE_TYPE, APIConstants.Constants.ANDROID);
        params.put(Params.DEVICE_TOKEN, prefUtils.getStringValue(PrefKeys.FCM_TOKEN,""));
        params.put(Params.TIME_ZONE, TimeZone.getDefault().getID());
        params.put(Params.REFERRAL_CODE, "");
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
        params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);
        params.put(REFERRER, checkString(prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""))? getResources().getString(R.string.app_name) : prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""));
        params.put(DEVICE_CODE, getSecureId(activity));

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
                    if (signUpResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {

                        Intent i = new Intent(activity, OtpVerificationActivity.class);
                        i.putExtra(Params.ID, signUpResponse.optString(Params.ID));
                        i.putExtra(Params.MOBILE, signUpResponse.optString(Params.MOBILE));
                        UiUtils.log(TAG,"OTP: " + signUpResponse.optString(Params.OTP.toLowerCase()));
                        i.putExtra(Params.OTP, signUpResponse.optString(Params.OTP.toLowerCase()));
                        i.putExtra(RESPONSE, signUpResponse.toString());
                        startActivityForResult(i,MANUAL_SIGN_IN);

                        /*UiUtils.showShortToast(activity, signUpResponse.optString(APIConstants.Params.MESSAGE));
                        Intent toHome = new Intent(activity, MainActivity.class);
                        toHome.putExtra(MainActivity.FIRST_TIME,  true);
                        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(toHome);*/
                    } else {
                        UiUtils.showShortToast(activity, signUpResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                        if (!signUpResponse.optString(APIConstants.Params.ERROR_MESSAGE).contains("taken")) {
                            // clearAllFields();
                            //  finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(activity);
            }
        });
    }

    private boolean validateFields() {
        /*if (edEmail.getText().toString().trim().length() == 0) {
            UiUtils.showShortToast(activity, getString(R.string.email_cant_be_empty));
            return false;
        }
        if (!AppUtils.isValidEmail(edEmail.getText().toString())) {
            UiUtils.showShortToast(activity, getString(R.string.enter_valid_email));
            return false;
        }*/
        if (edName.getText().toString().trim().length() == 0) {
            UiUtils.showShortToast(activity, getString(R.string.names_cant_be_empty));
            return false;
        }
        //Phone validation
        if(edPhone.getText().toString().trim().length()==0){
            UiUtils.showShortToast(activity,getString(R.string.enter_phone_no));
        }
        String phone = edPhone.getText().toString().trim();
        if (phone.length() != 0 && (phone.length() < 6 || phone.length() > 16)) {
            UiUtils.showShortToast(getActivity(), getString(R.string.phone_cant_be_stuff));
            return false;
        }
        if (edPassword.getText().toString().trim().length() == 0) {

            UiUtils.showShortToast(activity, getString(R.string.password_cant_be_empty));
            return false;
        }
        if (edPassword.getText().toString().length() < 6) {
            UiUtils.showShortToast(activity, getString(R.string.minimum_six_characters));
            return false;
        }

        if (!edPassword.getText().toString().equals(edRePassword.getText().toString())) {
            UiUtils.showShortToast(activity, getString(R.string.match_passwords));
            return false;
        }
        if (termsConditionCheckbox.isChecked()){
            return true;
        } else {
            Toast.makeText(activity, R.string.agree_the_terms_and_condition,Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                UiUtils.log(TAG,"After Selection");
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    UiUtils.log(TAG,task.toString());
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    UiUtils.log(TAG,account.toString());
                    UiUtils.log(TAG,account.getDisplayName());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    // ...
                    UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
                }
                break;
            case MANUAL_SIGN_IN:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        JSONObject object = new JSONObject(data.getStringExtra(RESPONSE));
                        loginUserInDevice(object, MANUAL_LOGIN);
                    } catch (JSONException e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                }
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
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
            UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        handleSignInResult(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        UiUtils.showShortToast(activity, getString(R.string.login_cancelled));
                    }
                    task.addOnFailureListener(e -> {
                    });
                    // ...
                });
    }

    protected void doSocialLoginUser(String socialUniqueId, String email, String name, String picture, String loginBy) {
        UiUtils.showLoadingDialog(activity);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(activity);
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
        params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);
        params.put(REFERRER, checkString(prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""))? getResources().getString(R.string.app_name) : prefUtils.getStringValue(PrefKeys.REFERENCE_URL_CODE,""));
        params.put(DEVICE_CODE, getSecureId(activity));

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
                    if (socialLoginResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(activity, getString(R.string.welcome) + name + "!");
                        loginUserInDevice(socialLoginResponse, loginBy);
                    } else {
                        UiUtils.showShortToast(activity, socialLoginResponse.optString(APIConstants.Params.ERROR_MESSAGE));
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
        PrefHelper.setUserLoggedIn(activity, data.optInt(APIConstants.Params.USER_ID)
                , data.optString(APIConstants.Params.TOKEN)
                , data.optString(Params.LOGIN_TOKEN)
                , loginBy
                , data.optString(APIConstants.Params.EMAIL)
                , data.optString(APIConstants.Params.NAME)
                , data.optString(APIConstants.Params.MOBILE)
                , data.optString(APIConstants.Params.DESCRIPTION)
                , data.optString(APIConstants.Params.PICTURE)
                , data.optString(APIConstants.Params.AGE)
                , data.optString(APIConstants.Params.NOTIF_PUSH_STATUS)
                , data.optString(APIConstants.Params.NOTIF_PUSH_STATUS)
                , data.optString(APIConstants.Params.DEFAULT_EMAIL)
                , data.optString(APIConstants.Params.DEFAULT_MOBILE));
        getSubscriptionStatus();
    /*    Intent toHome = new Intent(activity, MainActivity.class);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toHome);*/
        activity.finish();
        if (Coming != null && Coming.equals("Upcoming")){
            Intent toHome = new Intent(activity, MainActivity.class);
            toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(toHome);
        } else {
            activity.finish();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        UiUtils.log(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        UiUtils.log(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        handleSignInResult(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        UiUtils.log(TAG, "signInWithCredential:failure: "+ task.getException());
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }

    private void checkWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        Log.e(TAG, "Width: "+ width);
        Log.e(TAG, "Width dp: "+ dp);

        if(dp<400)
        {
            txtGoogle.setTextSize(16f);
            txtFacebook.setTextSize(16f);
            loginTxt.setTextSize(12f);
            loginBtn.setTextSize(14f);

            txt_play.setPadding(0,35,15,0);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_details.getLayoutParams();
            params.setMargins(0,18,0,0);
            params.setMarginEnd(170);
            ll_details.setLayoutParams(params);

            RelativeLayout.LayoutParams paramsSocialG = (RelativeLayout.LayoutParams) ll_google_sign.getLayoutParams();
            paramsSocialG.width = 370;
            ll_google_sign.setLayoutParams(paramsSocialG);
            ll_facebook_sign.setLayoutParams(paramsSocialG);

        }
        else if(dp<500)
        {
            txt_play.setPadding(0,30,20,0);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_details.getLayoutParams();
            params.setMargins(0,-10,0,0);
            params.setMarginEnd(210);
            ll_details.setLayoutParams(params);
        }
        /*if(dp>500 && dp<600)
        {
            binding.txtGoogle.setTextSize(18f);

        }else if(dp>600 && dp<700)
        {
            binding.txtGoogle.setTextSize(18f);
        }else if(dp>700 && dp<800)
        {
            binding.txtGoogle.setTextSize(18f);
        }*/else if(dp>800)
        {
            txt_play.setPadding(0,30,20,0);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_details.getLayoutParams();
            params.setMargins(0,-40,0,0);
            params.setMarginEnd(550);
            ll_details.setLayoutParams(params);

            RelativeLayout.LayoutParams paramsWelcome = (RelativeLayout.LayoutParams) ll_welcome.getLayoutParams();
            paramsWelcome.setMargins(50,50,0,0);
            //paramsWelcome.setMarginStart(30);
            ll_welcome.setLayoutParams(paramsWelcome);

        }
    }

    private void getSubscriptionStatus()
    {
        UiUtils.log("TAG","API Called");
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getSubscriptionStatus(USER_SUBSCRIPTION, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject subscriptionStatusResponse = null;
                try {
                    subscriptionStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                /*if (subscriptionStatusResponse != null) {
                    supportArrayList = parseSupportData(subscriptionStatusResponse);
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(getActivity());
            }
        });
    }
}
