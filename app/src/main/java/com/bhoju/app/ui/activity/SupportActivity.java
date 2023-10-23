package com.bhoju.app.ui.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.bhoju.app.util.UnsafeOkHttpClient;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.bhoju.app.R;
import com.bhoju.app.model.Support;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.AppUtils;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.bhoju.app.util.sharedpref.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.GETHELPQUERIES;
import static com.bhoju.app.network.APIConstants.APIs.SUPPORTENQURIY;
import static com.bhoju.app.network.APIConstants.APIs.SUPPORTHELP;
import static com.bhoju.app.network.APIConstants.Constants.MANUAL_LOGIN;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.APP_VERSION;
import static com.bhoju.app.network.APIConstants.Params.AUTHPROVIDER;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;
import static com.bhoju.app.network.APIConstants.Params.USEREMAIL;
import static com.bhoju.app.network.APIConstants.Params.USERNAME;
import static com.bhoju.app.network.APIConstants.Params.USERPHONE;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE_APK;
import static com.bhoju.app.ui.activity.MainActivity.supportArrayList;


public class SupportActivity extends BaseActivity {

    private final String TAG = SupportActivity.class.getSimpleName();

    int queryTypeID=0;
    String querySting="";
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.nestedScrollSupportPage)
    NestedScrollView nestedScrollSupportPage;
    @BindView(R.id.ll_query_type)
    FlexboxLayout ll_query_type;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_subject)
    EditText edSubject;
    @BindView(R.id.ed_comment)
    EditText edComment;
    @BindView(R.id.subscribe_btn)
    TextView subscribe_btn;

    private APIInterface apiInterface;
    private PrefUtils prefUtils;

    HashMap<String,String> deviceDetails;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);
        img_back.setOnClickListener(v -> onBackPressed());

        id = prefUtils.getIntValue(PrefKeys.USER_ID, 1);
        subProfileId = prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0);

        edName.setText(prefUtils.getStringValue(PrefKeys.USER_NAME, ""));
        edEmail.setText(prefUtils.getStringValue(PrefKeys.USER_EMAIL, ""));
        edPhone.setText(prefUtils.getStringValue(PrefKeys.USER_MOBILE, ""));
        if(prefUtils.getStringValue(PrefKeys.LOGIN_TYPE, "").equalsIgnoreCase(""))
        {

        }
        else if(prefUtils.getStringValue(PrefKeys.LOGIN_TYPE, "").equalsIgnoreCase(MANUAL_LOGIN)) {
            edName.setEnabled(false);
            edName.setTextColor(ResourcesCompat.getColor(getResources(),R.color.silver,null));
            edPhone.setEnabled(false);
            edPhone.setTextColor(ResourcesCompat.getColor(getResources(),R.color.silver,null));
            edEmail.setNextFocusDownId(R.id.ed_comment);
        }
        else {
            edName.setEnabled(false);
            edName.setTextColor(ResourcesCompat.getColor(getResources(),R.color.silver,null));
            edEmail.setEnabled(false);
            edEmail.setTextColor(ResourcesCompat.getColor(getResources(),R.color.silver,null));
            edName.setNextFocusDownId(R.id.ed_phone);
        }


        if(supportArrayList!=null && supportArrayList.size()>0)
            setQueryTypeTag();
        getAllQueryTypes();

        deviceDetails =  Utils.getDeviceDetails(this);
        // using for-each loop for iteration over Map.entrySet()
        /*for (Map.Entry<String,String> entry : deviceDetails.entrySet())
            UiUtils.log("TAG","Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

        UiUtils.log("TAG", "Hello " + new JSONObject(deviceDetails).toString());*/
        UiUtils.log(TAG, "Hello " + new JSONObject(deviceDetails).toString());

        subscribe_btn.setOnClickListener(v -> {
            if(validateFields())
//                sendQueryToBackend();
                sendQuery();
        });

        edComment.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (edComment.hasFocus()) {
                    nestedScrollSupportPage.requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            nestedScrollSupportPage.requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);
        if(dp<400) {
            subscribe_btn.setPadding(0, 35, 15, 0);
        } else if(dp<500)
        {
            subscribe_btn.setPadding(0,30,10,0);
        }else if(dp>800) {
            subscribe_btn.setPadding(0, 30, 15, 0);
        }
    }

    private void getAllQueryTypes()
    {
        /*UiUtils.showLoadingDialog(this);
        nestedScrollSupportPage.setVisibility(View.GONE);*/
        Call<String> call = apiInterface.getHelpQueries(GETHELPQUERIES);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                /*UiUtils.hideLoadingDialog();
                nestedScrollSupportPage.setVisibility(View.VISIBLE);*/
                JSONArray helpQueriesResponse = null;
                try {
                    helpQueriesResponse = new JSONArray(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (helpQueriesResponse != null) {
                    supportArrayList = parseSupportData(helpQueriesResponse);

                    if(supportArrayList.size()>0)
                    {
                        setQueryTypeTag();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
               /* UiUtils.hideLoadingDialog();
                nestedScrollSupportPage.setVisibility(View.VISIBLE);
                finish();*/
                NetworkUtils.onApiError(SupportActivity.this);
            }
        });
    }

    private ArrayList<Support> parseSupportData(JSONArray dataArray) {

        ArrayList<Support> parseSupportArrayList  = new ArrayList<>();
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObj = dataArray.getJSONObject(i);
                Support support = new Support(dataObj.optInt(APIConstants.Params.ID),
                        dataObj.optString(APIConstants.Params.NAME),
                        dataObj.optString(APIConstants.Params.STATUS));
                parseSupportArrayList.add(support);
            }
        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return parseSupportArrayList;
    }

    private void setQueryTypeTag() {
        ll_query_type.removeAllViews();
        queryTypeID=0;

        for (int i=0;i<supportArrayList.size();i++) {

            Support obj = supportArrayList.get(i);

            // create a new textview
            RelativeLayout rowTextView = (RelativeLayout) getLayoutInflater().inflate(R.layout.support_item, ll_query_type, false);
            TextView rowText = rowTextView.findViewById(R.id.query_text);
            ImageView rowImgOuter = rowTextView.findViewById(R.id.img_outer);
            ImageView rowImgInner = rowTextView.findViewById(R.id.img_inner);

            rowText.setTag(obj.getId());
            rowText.setText(obj.getName());
            if(obj.isChecked())
            {
                queryTypeID = obj.getId();
                querySting =obj.getName();
                //rowTextView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.oval_shape_button,null));
                //rowTextView.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.pink));
                rowImgOuter.setImageTintList(AppCompatResources.getColorStateList(this,R.color.theme_yellow));
                rowImgInner.setImageTintList(AppCompatResources.getColorStateList(this,R.color.theme_yellow));
                rowImgInner.setVisibility(View.VISIBLE);
                rowText.setTextColor(AppCompatResources.getColorStateList(this,R.color.theme_yellow));
                rowText.setTypeface(null, Typeface.BOLD);
            }else
            {
                //rowTextView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.oval_shape_button,null));
                //rowTextView.setBackgroundTintList(AppCompatResources.getColorStateList(this,R.color.buttonColor));
                rowImgOuter.setImageTintList(AppCompatResources.getColorStateList(this,R.color.button_extra_color));
                rowImgInner.setImageTintList(AppCompatResources.getColorStateList(this,R.color.button_extra_color));
                rowImgInner.setVisibility(View.GONE);
                rowText.setTextColor(AppCompatResources.getColorStateList(this,R.color.button_extra_color));
                rowText.setTypeface(null, Typeface.NORMAL);
            }

            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<supportArrayList.size();i++) {
                        if (obj.getId() == supportArrayList.get(i).getId()) {
                            if(supportArrayList.get(i).isChecked())
                                supportArrayList.get(i).setChecked(false);
                            else
                                supportArrayList.get(i).setChecked(true);
                        }
                        else
                            supportArrayList.get(i).setChecked(false);
                    }
                    setQueryTypeTag();
                }
            });

            // add the textview to the linearlayout
            ll_query_type.addView(rowTextView);
        }
    }

    private boolean validateFields() {

        UiUtils.log(TAG,"Query Type: "+ queryTypeID);
        if(queryTypeID ==0) {
            UiUtils.showShortToast(SupportActivity.this, getString(R.string.select_query));
            return false;
        }
        if(prefUtils.getStringValue(PrefKeys.LOGIN_TYPE, "").equalsIgnoreCase(""))
        {
            if (edName.getText().toString().trim().length() == 0) {
                UiUtils.showShortToast(SupportActivity.this, getString(R.string.names_cant_be_empty));
                return false;
            }

            /*if(edPhone.getText().toString().trim().length()==0){
                UiUtils.showShortToast(SupportActivity.this,getString(R.string.enter_phone_no));
                return false;
            }*/
        }

        if (edEmail.getText().toString().trim().length() == 0) {
            UiUtils.showShortToast(SupportActivity.this, getString(R.string.email_cant_be_empty));
            return false;
        }
        if (!AppUtils.isValidEmail(edEmail.getText().toString())) {
            UiUtils.showShortToast(SupportActivity.this, getString(R.string.enter_valid_email));
            return false;
        }

        //Phone validation
        
        String phone = edPhone.getText().toString().trim();
        if (phone.length() != 0 && (phone.length() < 6 || phone.length() > 16)) {
            UiUtils.showShortToast(SupportActivity.this, getString(R.string.phone_cant_be_stuff));
            return false;
        }
        /*if (edSubject.getText().toString().trim().length() == 0) {

            UiUtils.showShortToast(SupportActivity.this, getString(R.string.subject_cant_be_empty));
            return false;
        }*/

        if (edComment.getText().toString().trim().length() == 0) {

            UiUtils.showShortToast(SupportActivity.this, getString(R.string.comment_cant_be_empty));
            return false;
        }

        return true;
    }

//    private void sendQueryToBackend()
//    {
//        UiUtils.showLoadingDialog(this);
//        //nestedScrollSupportPage.setVisibility(View.GONE);
//        if(prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "").equalsIgnoreCase(""))
//            token = "dummyToken";
//        Map<String, Object> params = new HashMap<>();
//        params.put(Params.ID, id);
//        params.put(Params.TOKEN, token);
//        params.put(Params.SUB_PROFILE_ID, subProfileId);
//        params.put(Params.NAME, edName.getText().toString());
//        params.put(Params.DESCRIPTION, edComment.getText().toString());
//        params.put(Params.EMAIL, edEmail.getText().toString());
//        params.put(Params.CONTACT, edPhone.getText().toString());
//        params.put(Params.HELPTYPE, String.valueOf(queryTypeID));
//        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
//        params.put(Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
//        params.put(Params.APPVERSION, deviceDetails.get(APPVERSION));
//        params.put(Params.DEVICE, deviceDetails.get(DEVICE));
//        params.put(Params.BEBUEXT, deviceDetails.get(BEBUEXT));
//        params.put(Params.BRAND, deviceDetails.get(BRAND));
//        params.put(Params.MODEL, deviceDetails.get(MODEL));
//        params.put(Params.VERSION, deviceDetails.get(VERSION));
//        params.put(Params.PLAT, deviceDetails.get(PLAT));
//
//        Call<String> call = apiInterface.sendQueryToBackend(SUPPORTHELP, params);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                UiUtils.hideLoadingDialog();
//                JSONObject ticketResponse = null;
//                try {
//                    ticketResponse = new JSONObject(response.body());
//                } catch (Exception e) {
//                    UiUtils.log(TAG, Log.getStackTraceString(e));
//                }
//                if (ticketResponse != null) {
//                    if (ticketResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
//                        clearFields();
//                        Toast.makeText(SupportActivity.this,getResources().getString(R.string.support_message),Toast.LENGTH_LONG).show();
//                    }else
//                    {
//                        UiUtils.showShortToast(SupportActivity.this, ticketResponse.optString(ERROR_MESSAGE));
//                        //finish();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                UiUtils.hideLoadingDialog();
//                // nestedScrollSupportPage.setVisibility(View.VISIBLE);
//                //finish();
//                NetworkUtils.onApiError(SupportActivity.this);
//            }
//        });
//    }

    private void sendQuery() {
        UiUtils.showLoadingDialog(this);
        //nestedScrollSupportPage.setVisibility(View.GONE);
        if(prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "").equalsIgnoreCase(""))
            token = "dummyToken";
        Map<String, Object> params = new HashMap<>();
//        params.put(Params.ID, id);
//        params.put(Params.TOKEN, token);
//        params.put(Params.SUB_PROFILE_ID, subProfileId);
//        params.put(Params.NAME, edName.getText().toString());
//        params.put(Params.DESCRIPTION, edComment.getText().toString());
//        params.put(Params.EMAIL, edEmail.getText().toString());
//        params.put(Params.CONTACT, edPhone.getText().toString());
//        params.put(Params.HELPTYPE, String.valueOf(queryTypeID));
//        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
//        params.put(Params.PHONEDETAILS, new JSONObject(deviceDetails).toString());
//        params.put(Params.APPVERSION, deviceDetails.get(APPVERSION));
//        params.put(Params.VERSION_CODE, deviceDetails.get(VERSION_CODE));
//        params.put(Params.DEVICE, deviceDetails.get(DEVICE));
//        params.put(Params.BEBUEXT, deviceDetails.get(BEBUEXT));
//        params.put(Params.BRAND, deviceDetails.get(BRAND));
//        params.put(Params.MODEL, deviceDetails.get(MODEL));
//        params.put(Params.VERSION, deviceDetails.get(VERSION));
//        params.put(Params.PLAT, deviceDetails.get(PLAT));


        params.put(Params.TOKEN, token);
        params.put(Params.ID, id);
        params.put(Params.TYPE,querySting);
        params.put(Params.ENQUIRY,edComment.getText().toString());
        params.put(Params.PLATFORM, "ANDROID");
        params.put(VERSION_CODE_APK,deviceDetails.get(VERSION_CODE));
        params.put(APP_VERSION, deviceDetails.get(APPVERSION));
        params.put(USEREMAIL,edEmail.getText().toString());
        params.put(USERPHONE, edPhone.getText().toString());
        params.put(USERNAME,edName.getText().toString());
        params.put(AUTHPROVIDER, "Bebu");

        Log.d("sendQuery", "sendQuery: 1"+token);//token
        Log.d("sendQuery", "sendQuery: 2"+querySting);//id
        Log.d("sendQuery", "sendQuery: 3"+deviceDetails.get(APPVERSION));
        Log.d("sendQuery", "sendQuery: 4"+deviceDetails.get(VERSION_CODE));

        Call<String> call = apiInterface.sendQuery(SUPPORTENQURIY, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();

                Log.d("responsetr", "onResponse:00 "+response.body());
                JSONObject ticketResponse = null;
                try {
                    ticketResponse = new JSONObject(response.body());
                    Toast.makeText(SupportActivity.this,getResources().getString(R.string.support_message),Toast.LENGTH_LONG).show();
                    Log.d("responsetr", "onResponse:01 "+ticketResponse);
                } catch (Exception e) {
                    Log.d("responsetr", "onResponse:02 "+e.toString());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                // nestedScrollSupportPage.setVisibility(View.VISIBLE);
                //finish();
                NetworkUtils.onApiError(SupportActivity.this);
            }
        });
    }

    private void clearFields()
    {
        for (int i=0;i<supportArrayList.size();i++) {
            supportArrayList.get(i).setChecked(false);
        }
        setQueryTypeTag();
        queryTypeID=0;
        if(prefUtils.getStringValue(PrefKeys.USER_NAME, "").equalsIgnoreCase(""))
            edName.setText("");
        if(prefUtils.getStringValue(PrefKeys.USER_EMAIL, "").equalsIgnoreCase(""))
            edEmail.setText("");
        if(prefUtils.getStringValue(PrefKeys.USER_MOBILE, "").equalsIgnoreCase(""))
            edPhone.setText("");
        edSubject.setText("");
        edComment.setText("");
    }

}