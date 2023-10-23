package com.bhoju.app.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bhoju.app.gcm.MessagingServices;
import com.bhoju.app.ui.activity.googleInAppPurchase.GoogleInAppPurchaseActivity;
import com.bhoju.app.util.AppUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.Utils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.common.collect.ImmutableList;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.AgeItem;
import com.bhoju.app.model.Invoice;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.adapter.AgeAdapter;
import com.bhoju.app.ui.adapter.PlanAdapter;
import com.bhoju.app.ui.fragment.bottomsheet.InvoiceBottomSheet;
import com.bhoju.app.ui.fragment.bottomsheet.PaymentBottomSheet;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.PaymentUtilsNew;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.GET_PAYMENT_GATEWAY;
import static com.bhoju.app.network.APIConstants.APIs.MAKE_AGREEPAY_PAYMENT;
import static com.bhoju.app.network.APIConstants.APIs.MAKE_GOOGLE_PLAY_PAYMENT;
import static com.bhoju.app.network.APIConstants.APIs.MAKE_PAYTM_PAYMENT;
import static com.bhoju.app.network.APIConstants.APIs.PAYTM_CHECKSUM;
import static com.bhoju.app.network.APIConstants.APIs.UPDATE_AGE;
import static com.bhoju.app.network.APIConstants.APIs.VERIFY_UPI_PAYTM_PAYMENT;
import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Constants.MANUAL_LOGIN;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.AP_API_KEY;
import static com.bhoju.app.network.APIConstants.Params.AP_COUNTRY;
import static com.bhoju.app.network.APIConstants.Params.AP_CURRENCY;
import static com.bhoju.app.network.APIConstants.Params.AP_ERROR_MSG;
import static com.bhoju.app.network.APIConstants.Params.AP_HOST_NAME;
import static com.bhoju.app.network.APIConstants.Params.AP_MODE;
import static com.bhoju.app.network.APIConstants.Params.AP_ORDER_ID;
import static com.bhoju.app.network.APIConstants.Params.AP_RESP_MSG;
import static com.bhoju.app.network.APIConstants.Params.AP_RETURN_URL;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.CHANNEL_ID;
import static com.bhoju.app.network.APIConstants.Params.CHECKSUMHASH;
import static com.bhoju.app.network.APIConstants.Params.CUST_ID;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.DEVICE_CODE;
import static com.bhoju.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.FAILED;
import static com.bhoju.app.network.APIConstants.Params.ID;
import static com.bhoju.app.network.APIConstants.Params.INDUSTRY_TYPE_ID;
import static com.bhoju.app.network.APIConstants.Params.LIVE_CALLBACK_URL;
import static com.bhoju.app.network.APIConstants.Params.MID;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.ORDERID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_CHANNEL_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_CUST_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_INDUSTRY_TYPE_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_ORDER_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_TXN_AMOUNT;
import static com.bhoju.app.network.APIConstants.Params.PAYMENT_GATEWAY_NAME;
import static com.bhoju.app.network.APIConstants.Params.PAYMENT_GATEWAY_TYPE;
import static com.bhoju.app.network.APIConstants.Params.PAYMENT_RESP;
import static com.bhoju.app.network.APIConstants.Params.PAYTM_CALLBACK_URL;
import static com.bhoju.app.network.APIConstants.Params.PLAN;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.REFERRER;
import static com.bhoju.app.network.APIConstants.Params.RESPONSE;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;
import static com.bhoju.app.network.APIConstants.Params.TXN_AMOUNT;
import static com.bhoju.app.network.APIConstants.Params.TXN_TOKEN;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.network.APIConstants.Params.WEBSITE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_COUNTRY;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_REGION;
import static com.bhoju.app.ui.activity.MainActivity.STATES_PAYMENT;
import static com.bhoju.app.ui.fragment.bottomsheet.PaymentBottomSheet.invoice;
import static com.bhoju.app.util.UiUtils.checkString;
import static com.bhoju.app.util.sharedpref.Utils.getDateConversion;
import static com.bhoju.app.util.sharedpref.Utils.getSecureId;
import static com.bhoju.app.util.sharedpref.Utils.getUserLoginStatus;
import static com.google.ads.interactivemedia.v3.internal.aig.skip;

public class PlansActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        PaymentBottomSheet.PaymentsInterface,
        OnLoadMoreVideosListener {

    static {
        System.loadLibrary("keys");
    }
    //Test
    public native String getTestMIDKeys();
    public native String getTestChannelIdKeys();
    public native String getTestWebsiteKeys();
    public native String getTestIndustryTypeIdKeys();
    //Live
    public native String getLiveMIDKeys();
    public native String getLiveChannelIdKeys();
    public native String getLiveWebsiteKeys();
    public native String getLiveIndustryTypeIdKeys();

    //Type
    public native String getTypePaytmKeys();
    public native String getTypeCardKeys();
    public native String getTypeUpiKeys();
    public native String getTypeAgreePayKeys();
    public native String getTypeGoogleInAppKeys();

    private final String TAG = PlansActivity.class.getSimpleName();

    PlanAdapter planAdapter;
    Discounted_priceAdapter discounted_price;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.plansList)
    RecyclerView plansRecycler;
    /*@BindView(R.id.pay)
    Button pay;*/
    ArrayList<SubscriptionPlan> discountplans = new ArrayList<>();
    ArrayList<SubscriptionPlan> plans = new ArrayList<>();
    APIInterface apiInterface;
    public PrefUtils prefUtils;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.noResultLayout)
    TextView noResultLayout;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.ll_current_plan)
    LinearLayout ll_current_plan;
    @BindView(R.id.discount_Price)
    RecyclerView discount_Plans_Recycler;
    @BindView(R.id.discountTimerLayout)
    LinearLayout discountTimerLayout;

    //timercounterbind
    @BindView(R.id.counter_days)
    TextView counter_days;
    @BindView(R.id.counter_hours)
    TextView counter_hours;
    @BindView(R.id.counter_minute)
    TextView counter_minute;
    @BindView(R.id.counter_sec)
    TextView counter_sec;

    @BindView(R.id.current_plan_name)
    TextView current_plan_name;
    @BindView(R.id.current_plan_months)
    TextView current_plan_months;
    @BindView(R.id.current_plan_text_months)
    TextView current_plan_text_months;
    @BindView(R.id.current_plan_original_amount_rupee)
    TextView current_plan_original_amount_rupee;
    @BindView(R.id.current_plan_original_amount)
    TextView current_plan_original_amount;
    @BindView(R.id.current_plan_amount_rupee)
    TextView current_plan_amount_rupee;
    @BindView(R.id.current_plan_amount)
    TextView current_plan_amount;
    @BindView(R.id.current_txt_plan)
    TextView current_txt_plan;
    @BindView(R.id.current_plan_user)
    TextView current_plan_user;
    @BindView(R.id.txt_current_plan_user)
    TextView txt_current_plan_user;
    @BindView(R.id.current_plan_order_id)
    TextView current_plan_order_id;
    @BindView(R.id.txt_current_plan_order_id)
    TextView txt_current_plan_order_id;
    @BindView(R.id.current_plan_trans_id)
    TextView current_plan_trans_id;
    @BindView(R.id.txt_current_plan_trans_id)
    TextView txt_current_plan_trans_id;
    @BindView(R.id.current_plan_expiry_date)
    TextView current_plan_expiry_date;
    @BindView(R.id.txt_current_plan_expiry_date)
    TextView txt_current_plan_expiry_date;

    PaymentBottomSheet.PaymentsInterface paymentsInterface;
    PaymentsClient paymentsClient;
    InvoiceBottomSheet invoiceSheet;
    public static SubscriptionPlan plan;
    private Video video;

    AgeAdapter ageAdapter;
    Dialog dialog,paymentDialog;
    String selectedAge="";

    String selectedState="";
    public static boolean discounted_Listed_price=false;
    String text = "2023-5-11";//default_date

    //Paytm
    private final int PAYTM = 100;
    private final int UPI_INTENT = 50;
    HashMap<String, String> paytmParamMap =null;
    private CountDownTimer countDownTimer;

    //Paypal
    public static final int PAY_PAL_REQUEST_CODE = 200;
    public static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(APIConstants.Payments.PayPal.CLIENT_ID);

    //Google Pay
    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 1000;
    private static final int TEZ_REQUEST_CODE = 123;
    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    //Google play in app purchase
    public static final int IN_APP_GATEWAY = 300;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager llmanager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (llmanager.findLastCompletelyVisibleItemPosition() == (planAdapter.getItemCount() - 1)) {
                planAdapter.showLoading();
            }
        }
    };

    private PurchasesUpdatedListener purchasesUpdatedListener;
    private BillingClient billingClient;
    private BillingClientStateListener billingClientStateListener;
    private Handler handler =new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plans2);
        ButterKnife.bind(this);
        swipe.setOnRefreshListener(this);
        paymentsClient = PaymentUtilsNew.createPaymentsClient(this);
        paymentsInterface = this;
        img_back.setOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        /*pay.setOnClickListener(v -> {
            if(prefUtils.getStringValue(PrefKeys.USER_AGE, "").equalsIgnoreCase(""))
                showAgePicker();
            else {
                setPaymentsInterface(PlansActivity.this, plan, null);
                showPaymentPicker(null);
            }

//            Intent paymentsIntent = new Intent(PlansActivity.this, PaymentBottomSheet.class);
//            startActivity(paymentsIntent);
//            setPaymentsInterface(PlansActivity.this, plan, null);
        });*/
        prefUtils = PrefUtils.getInstance(this);
        if(prefUtils.getStringValue(PrefKeys.FCM_TOKEN,"").equalsIgnoreCase(""))
            MessagingServices.getFCMToken(this);
        selectedState = CURRENT_REGION;
        setUpAvailablePlans();
        setUpDiscountedPlans();
        getDiscountedplans();
        checkWidth();
        setUpGooglePlayBilling();
    }

    private void setUpAvailablePlans() {
        plansRecycler.setLayoutManager(new LinearLayoutManager(this));
        planAdapter = new PlanAdapter(this, this, plans);
        plansRecycler.setAdapter(planAdapter);
    }

    private void setUpDiscountedPlans() {
//        SubscriptionPlan subscriptionPlan=new SubscriptionPlan();
        discount_Plans_Recycler.setLayoutManager(new LinearLayoutManager(this));
        discounted_price = new Discounted_priceAdapter(this, this, discountplans);
        discount_Plans_Recycler.setAdapter(discounted_price);
    }
    protected void getDiscountedplans() {

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.SKIP, skip);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getAvailablePlans(APIConstants.APIs.AVAILABLE_PLANS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                if (skip == 0) {
//                    discountplans.clear();
//                    if (swipe.isRefreshing()) swipe.setRefreshing(false);
//                    UiUtils.hideLoadingDialog();
//                }

                JSONObject myPlansResponse = null;
                try {
                    myPlansResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (myPlansResponse != null) {
                    if (myPlansResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        JSONArray plansArray = myPlansResponse.optJSONArray(Params.DATA);
                        for (int i = 0; i < plansArray.length(); i++) {
                            JSONObject planObj = plansArray.optJSONObject(i);
                            SubscriptionPlan plan = ParserUtils.parsePlan(planObj);
                            discountplans.add(plan);
                        }
//                        if (plansArray.length() == 0) {
//                            myPlansRecycler.removeOnScrollListener(scrollListener);
//                        }
                        onDataChanged();
                    } else {
                        UiUtils.showShortToast(getApplicationContext(), myPlansResponse.optString(Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                String text="2023-6-6";
                Date date = null;
                try {
                    date = formatter.parse(text);
                    if (!discountplans.isEmpty()) {
                        UiUtils.log(TAG,String.valueOf(discountplans.get(0).getDays()));/*error*/
                    } else {
                        // Handle the case when the ArrayList is empty
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList <Integer> countdown = new ArrayList<>();
                countdown = AppUtils.getTimer(date);
                UiUtils.log(TAG, String.valueOf(countdown));
                if(countdown==null) {
                    // Stop Handler
                    handler.removeMessages(0);
                    UiUtils.log(TAG,"Subscription Completed");
                    discountTimerLayout.setVisibility(View.GONE);
                }else{

                    if (!discountplans.isEmpty()) {
                        if(countdown.get(0) < discountplans.get(0).getDays()) {
                            discountTimerLayout.setVisibility(View.VISIBLE);
                            counter_days.setText(String.valueOf(countdown.get(0)));
                            counter_hours.setText(String.valueOf(countdown.get(1)));
                            counter_minute.setText(String.valueOf(countdown.get(2)));
                            counter_sec.setText(String.valueOf(countdown.get(3)));
                        }else {
                            discountTimerLayout.setVisibility(View.GONE);
                        }
                    } else {
                        discountTimerLayout.setVisibility(View.GONE);
                    }


                    //Commit by 3/8/2023

//                    UiUtils.log(TAG,"invalied");
//                    if(countdown.get(0) < discountplans.get(0).getDays()) {
//                        discountTimerLayout.setVisibility(View.VISIBLE);
//                        counter_days.setText(String.valueOf(countdown.get(0)));
//                        counter_hours.setText(String.valueOf(countdown.get(1)));
//                        counter_minute.setText(String.valueOf(countdown.get(2)));
//                        counter_sec.setText(String.valueOf(countdown.get(3)));
//                    }else {
//                        discountTimerLayout.setVisibility(View.GONE);
//                    }

                }
            }},1000);

        if(PrefUtils.getInstance(this).getIntValue(PrefKeys.USER_ID, -1) > 1)
            getUserLoginStatus(this);

        Utils.getPublicIpAddress(this,true);
        String orderId= PrefUtils.getInstance(this).getStringValue(PrefKeys.ORDER_ID, "");
        int planId= PrefUtils.getInstance(this).getIntValue(PrefKeys.PLAN_ID, -1);
        int tempID= PrefUtils.getInstance(this).getIntValue(PrefKeys.TEMP_ID, 0);
        if(!(orderId.equalsIgnoreCase("") || planId == -1))
        {
            if(PrefUtils.getInstance(this).getIntValue(PrefKeys.USER_ID, -1) == tempID)
                sendResultToServerForValidation(orderId,planId);
        }
        id = prefUtils.getIntValue(PrefKeys.USER_ID, 1);
        token = prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "");
        subProfileId = prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0);
        if(plan==null)
            getAvailablePlans(0);
    }

    @Override
    public void onRefresh() {
        plan = null;
        onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlanAdapter.selectedposition = 0;
        plan=null;
    }

    public void showAgePicker()
    {

        //Now we need an AlertDialog.Builder object
        dialog = new Dialog(PlansActivity.this);

        dialog.setContentView(R.layout.dialog_age_selection);
        dialog.setCancelable(false);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout ll_subscription = dialog.findViewById(R.id.ll_subscription);
        ll_subscription.setVisibility(View.GONE);

        TextView txt_state = dialog.findViewById(R.id.txt_state);
        ImageView img_state = dialog.findViewById(R.id.img_state);
        if(CURRENT_COUNTRY.equalsIgnoreCase("India")) {
            txt_state.setText(CURRENT_REGION);

            RelativeLayout ll_state = dialog.findViewById(R.id.ll_state);
            if (STATES_PAYMENT != null && STATES_PAYMENT.length > 1) {
                img_state.setVisibility(View.VISIBLE);
                ll_state.setOnClickListener(v -> {
                    showStatesPopup(txt_state);
                });
            }else
            {
                ll_state.setVisibility(View.GONE);
                UiUtils.log(TAG,"STATES_PAYMENT is NULL");
            }
        }
        else {
            txt_state.setText(CURRENT_COUNTRY);
            img_state.setVisibility(View.GONE);
        }

        CheckBox checkBox =dialog.findViewById(R.id.termsConditionCheck);
        TextView termsCondition = dialog.findViewById(R.id.termsCondition);
        String termsAndConditions = getResources().getString(R.string.terms_conditions);
        String privacyPolicy = getResources().getString(R.string.privacy_policy);
        String refundPolicy = getResources().getString(R.string.refund_policy);

        String policy = getResources().getString(R.string.agree_text) + " "+ termsAndConditions +" and " + privacyPolicy +" and " + refundPolicy;
        SpannableString ss = new SpannableString(policy);
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(PlansActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(PlansActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.PRIVACY));
            }
        };

        ClickableSpan span3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(PlansActivity.this, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.REFUND));
            }
        };

        ss.setSpan(span1, 13, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 36, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span3, 55, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsCondition.setText(ss);
        termsCondition.setMovementMethod(LinkMovementMethod.getInstance());

        RecyclerView rv_age = dialog.findViewById(R.id.rv_age);
        setAgeListAdapter(rv_age,checkBox);

        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener((View v) -> { selectedAge=""; dialog.dismiss(); });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UiUtils.log(TAG,"sgfr: "+ isChecked );
            if(isChecked)
            {
                if(selectedAge.equals(""))
                    Toast.makeText(PlansActivity.this,"Please select age",Toast.LENGTH_SHORT).show();
                else
                    sendAgeToBackend(selectedAge);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);
        Log.e(TAG, "Width: "+ width);
        Log.e(TAG, "Width dp: "+ dp);

        if(dp<400) {
            cancel_btn.setPadding(0,25,15,0);
        }else if(dp<500) {
            cancel_btn.setPadding(0, 20, 20, 0);
        }else if(dp>800) {
            cancel_btn.setPadding(0, 20, 20, 0);
        }

        dialog.show();
    }

    private void setAgeListAdapter(RecyclerView rv_age, CheckBox checkBox) {

        List<String> ageArray = Arrays.asList(getResources().getStringArray(R.array.ageList));
        ArrayList<AgeItem> ageList = new ArrayList<>();
        for(int i=0;i<ageArray.size();i++) {
            //sendAgeToBackend();
            ageList.add(new AgeItem(ageArray.get(i), v -> {
                int itemPosition = rv_age.getChildLayoutPosition(v);
                AgeItem item = ageList.get(itemPosition);
                selectedAge = ((TextView) v.findViewById(R.id.ageName)).getText().toString();
                UiUtils.log(TAG,"sgfr: "+ item.getAgeName());
                if(checkBox.isChecked())
                    sendAgeToBackend(selectedAge);
                else
                    Toast.makeText(PlansActivity.this,"Please accept terms and condition",Toast.LENGTH_SHORT).show();

                clearSelection(ageList);
                item.setSelected(true);
                ageAdapter.notifyDataSetChanged();

                //showPaymentPicker(dialog)/*sendAgeToBackend(((TextView)v.findViewById(R.id.ageName)).getText().toString())*/
            }));
        }

        ageAdapter = new AgeAdapter(PlansActivity.this, ageList);
        rv_age.setLayoutManager(new LinearLayoutManager(PlansActivity.this));
        rv_age.setItemAnimator(new DefaultItemAnimator());
        rv_age.setAdapter(ageAdapter);
    }

    private void clearSelection(List<AgeItem> ageList)
    {
        for (AgeItem age : ageList) {
            // do something with object
            age.setSelected(false);
        }
    }

    private void sendAgeToBackend(String age) {
        UiUtils.showLoadingDialog(PlansActivity.this);

        Map<String, Object> params = new HashMap<>();
        params.put(ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(APIConstants.Params.AGE, age);
        params.put(APIConstants.Params.REGION_NAME, selectedState);
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.updateAge(UPDATE_AGE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();

                JSONObject homeDynamicResponse = null;
                try {
                    homeDynamicResponse = new JSONObject(response.body());
                }  catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (homeDynamicResponse != null) {
                    if (homeDynamicResponse.optString(SUCCESS).equals(Constants.TRUE)) {
                        //JSONArray dynamicVideoArr = homeDynamicResponse.optJSONArray(Params.DATA);
                        //dialog.dismiss();
                        setPaymentsInterface(PlansActivity.this, plan, null);
                        showPaymentPicker(dialog);
                        prefUtils.setValue(PrefKeys.USER_AGE, age);

                    } else {
                        UiUtils.showShortToast(PlansActivity.this, homeDynamicResponse.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    public void showPaymentPicker(Dialog dialogold) {
        if (dialogold != null)
            dialogold.dismiss();
        //Now we need an AlertDialog.Builder object
        paymentDialog = new Dialog(PlansActivity.this);

        paymentDialog.setContentView(R.layout.dialog_payment_selection);
        WindowManager.LayoutParams params = paymentDialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        paymentDialog.getWindow().setAttributes(params);
        paymentDialog.getWindow().setGravity(Gravity.BOTTOM);
        paymentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        paymentDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView txt_currency = paymentDialog.findViewById(R.id.txt_currency);
        TextView txt_price = paymentDialog.findViewById(R.id.txt_price);
        TextView txt_price_currency = paymentDialog.findViewById(R.id.txt_price_currency);
        txt_currency.setText(plan.getSymbol());
        if (discounted_Listed_price == true){
            txt_price.setText(String.valueOf(plan.getPercentage()));
        }else {
            txt_price.setText(String.valueOf((int) plan.getListedPrice()));
        }
        txt_price_currency.setText("*Price in " +plan.getCode());

        if(plan.getCode().equalsIgnoreCase(""))
            txt_price_currency.setText(getResources().getString(R.string.inr));

        if(plan.getSymbol().equalsIgnoreCase(""))
            txt_currency.setText(getResources().getString(R.string.currency));

        TextView txt_months = paymentDialog.findViewById(R.id.txt_months);
        String[] plans = plan.getMonthFormatted().split("\\s");
        txt_months.setText(String.format(" / %s %s", plans[0], plans[1]));

        TextView txt_plan = paymentDialog.findViewById(R.id.txt_plan);
        TextView txt_plan_txt = paymentDialog.findViewById(R.id.txt_plan_txt);
        txt_plan.setText(plan.getMonthFormatted());


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        if(dp<400)
        {
            txt_plan.setTextSize(10);
            txt_plan_txt.setTextSize(10);
        }

        //dialog.findViewById(R.id.btnCancel).setOnClickListener((View v) -> { dialog.dismiss(); });
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getSimCountryIso();

        /*TextView txt_state = paymentDialog.findViewById(R.id.txt_state);
        txt_state.setText(CURRENT_REGION);

        ImageView img_state = paymentDialog.findViewById(R.id.img_state);

        LinearLayout ll_state = paymentDialog.findViewById(R.id.ll_state);
        if (STATES_PAYMENT != null && STATES_PAYMENT.length > 1) {
            img_state.setVisibility(View.VISIBLE);
            ll_state.setOnClickListener(v -> {
                showStatesPopup(txt_state);
            });
        }else
        {
            ll_state.setVisibility(View.GONE);
            UiUtils.log(TAG,"STATES_PAYMENT is NULL");
        }*/

        LinearLayout ll_payment_gateway_available = paymentDialog.findViewById(R.id.ll_payment_gateway_available);
        LinearLayout ll_no_payment_gateway_available = paymentDialog.findViewById(R.id.ll_no_payment_gateway_available);
        ProgressBar loadingPaymentGateway = paymentDialog.findViewById(R.id.loadingPaymentGateway);

        ll_payment_gateway_available.setVisibility(View.VISIBLE);
        ll_no_payment_gateway_available.setVisibility(View.GONE);
        loadingPaymentGateway.setVisibility(View.VISIBLE);

        //api calling
        Call paymentCall = apiInterface.getPaymentGatewayCountryWise(GET_PAYMENT_GATEWAY + CURRENT_IP);
        paymentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONArray plansArray = null;
                try {
                    plansArray = new JSONArray(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }

                try {
                    if (plansArray != null) {

                        ll_payment_gateway_available.setVisibility(View.VISIBLE);
                        loadingPaymentGateway.setVisibility(View.VISIBLE);

                        if (plansArray.getJSONObject(0) == null) {
                            ll_no_payment_gateway_available.setVisibility(View.VISIBLE);
                            ll_payment_gateway_available.setVisibility(View.GONE);
                        } else {
                            UiUtils.log(TAG, "length: "+ plansArray.length());

                            for (int i = 0; i < plansArray.length(); i++) {

                                try {
                                    View singlePaymentType = getLayoutInflater().inflate(R.layout.payment_gateway_row, null);
                                    TextView gatewayName = (TextView) singlePaymentType.findViewById(R.id.payment_gateway_name);
                                    gatewayName.setText(plansArray.getJSONObject(i).getString(PAYMENT_GATEWAY_NAME));

                                    String paymentType = plansArray.getJSONObject(i).getString(PAYMENT_GATEWAY_TYPE);

                                    boolean matchFound = setPaymentTitleAndIcon(paymentType);

                                    //IMPORTANT !!! ADD MORE IF ELSE CONDITIONS TO ADD MORE GATEWAYS...DONT CHANGE LOGIC HERE
                                    singlePaymentType.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (paymentType.equalsIgnoreCase(getTypePaytmKeys())) {

                                                onStartTransaction(paymentType);

                                            } else if (paymentType.equalsIgnoreCase(getTypeCardKeys())) {

                                                onStartTransaction(paymentType);

                                            } else if (paymentType.equalsIgnoreCase(getTypeUpiKeys())) {

                                                onStartTransaction(paymentType);

                                               /* Intent upiIntent = new Intent(PlansActivity.this,PaytmUPIActivity.class);
                                                Bundle b = new Bundle();
                                                b.putSerializable(PLAN,plan);
                                                upiIntent.putExtras(b);
                                                startActivityForResult(upiIntent, UPI_INTENT);*/

                                            } else if (paymentType.equalsIgnoreCase(getTypeAgreePayKeys())) {

                                                onStartAgreePayTransaction();

                                            }else if (paymentType.equalsIgnoreCase(getTypeGoogleInAppKeys())) {

                                                onStartGooglePlayBillingTransaction();
                                                /*Intent intent = new Intent(PlansActivity.this, GoogleInAppPurchaseActivity.class);
                                                intent.putExtra("subscriptionDetails", plan);
                                                startActivityForResult(intent, IN_APP_GATEWAY);*/

                                            }
                                            else if (paymentType.equalsIgnoreCase("RAZORPAY")) {

                                        /*bottomSheetDialog.dismiss();

                                        getOrderId(subscriptionPackage);*/

                                            } else if (paymentType.equalsIgnoreCase("PAYPAL")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaypalActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, PAYPAL_GATEWAY);
*/
                                            } else  if (paymentType.equalsIgnoreCase("GOOGLE_INAPP_V2")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), GoogleInAppPurchaseActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, IN_APP_GETWAY);*/

                                            } else if (paymentType.equalsIgnoreCase("PAYTM_UPI")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaytmUPIActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, IN_APP_GETWAY);*/

                                            } else if (paymentType.equalsIgnoreCase("STRIPE")) {

                                       /* bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), StripePaymentActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, STRIPE_GETWAY);*/

                                            } else if (paymentType.equalsIgnoreCase("PAYPAL_INDIA")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaypalIndiaActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, PAYPAL_INDIA_GETWAY);*/

                                            } else if (paymentType.equalsIgnoreCase("PAYPAL_AUTO_RENEW")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), PaypalAutoRenewActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, PAYPAL_AUTO_RENEW_GETWAY);*/

                                            } else if (paymentType.equalsIgnoreCase("STRIP_RECURRING")) {

                                        /*bottomSheetDialog.dismiss();
                                        Intent intent = new Intent(getActivity(), StripRecurringPaymentActivity.class);
                                        intent.putExtra("subscriptionDetails", new Gson().toJson(subscriptionPackage));
                                        startActivityForResult(intent, STRIP_AUTO_RENUE_GETWAY);*/
                                            }

                                        }
                                    });

                                    if(matchFound)
                                        ll_payment_gateway_available.addView(singlePaymentType);

                                } catch (Exception e) {
                                    UiUtils.log(TAG, Log.getStackTraceString(e));
                                }
                            }

                            ll_payment_gateway_available.setVisibility(View.VISIBLE);
                            loadingPaymentGateway.setVisibility(View.GONE);
                        }

                    } else {
                        ll_no_payment_gateway_available.setVisibility(View.VISIBLE);
                        ll_payment_gateway_available.setVisibility(View.GONE);
                    }
                }catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadingPaymentGateway.setVisibility(View.GONE);
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });

       /* paymentDialog.findViewById(R.id.paytm).setOnClickListener((View v) -> {  onStartTransaction(v); });
        paymentDialog.findViewById(R.id.card).setOnClickListener((View v) -> {  onStartTransaction(v); });
        paymentDialog.findViewById(R.id.upi).setOnClickListener((View v) -> {
            Intent upiIntent = new Intent(this,PaytmUPIActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(PLAN,plan);
            upiIntent.putExtras(b);
            startActivityForResult(upiIntent, UPI_INTENT);
            //startPayment();
        });
        paymentDialog.findViewById(R.id.inApp).setOnClickListener((View v) -> {
            Toast.makeText(PlansActivity.this,"Coming Soon!!",Toast.LENGTH_SHORT).show(); });
        paymentDialog.findViewById(R.id.paypal).setOnClickListener((View v) -> {   getPayPalPayment(); });
        paymentDialog.findViewById(R.id.google_pay).setOnClickListener((View v) -> {

            try {
                if (!countryCodeValue.equalsIgnoreCase("in")) {
                    requestPayment(v);
                } else {
                    Uri uri =
                            new Uri.Builder()
                                    .scheme("upi")
                                    .authority("pay")
                                    .appendQueryParameter("pa", "test@axisbank")
                                    .appendQueryParameter("pn", "Test Merchant")
                                    .appendQueryParameter("mc", "1234")
                                    .appendQueryParameter("tr", "123456789")
                                    .appendQueryParameter("tn", invoice.getTitle())
                                    .appendQueryParameter("am", String.valueOf(invoice.getTotalAmount()))
                                    .appendQueryParameter("cu", "INR")
                                    .appendQueryParameter("url", "https://test.merchIDant.website")
                                    .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                    startActivityForResult(intent, TEZ_REQUEST_CODE);
                }
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        });
        paymentDialog.findViewById(R.id.stripe).setOnClickListener(v -> new AlertDialog.Builder(PlansActivity.this)
                .setTitle(getString(R.string.are_you_sure_you_want_to_pay) + invoice.getCurrency() + invoice.getPaidAmount() + " ?")
                .setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, as) ->
                        sendStripePaymentToBackend(invoice.isPayingForPlan(),
                                invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId()))
                .setNegativeButton(getResources().getString(R.string.no), (dialogInterface, as) -> dialogInterface.cancel()).create().show());*/

        paymentDialog.show();
    }

    private boolean setPaymentTitleAndIcon(String paymentType) {

        if (paymentType.equalsIgnoreCase(getTypePaytmKeys())) {
            return true;
        }else if (paymentType.equalsIgnoreCase(getTypeCardKeys())) {
            return true;
        } else if (paymentType.equalsIgnoreCase(getTypeUpiKeys())) {
            return true;
        } else if (paymentType.equalsIgnoreCase(getTypeAgreePayKeys())) {
            return true;
        }else if (paymentType.equalsIgnoreCase(getTypeGoogleInAppKeys())) {
            if(!checkString(plan.getGooglePlayProductId()))
                return true;
        }/*else if (paymentType.equalsIgnoreCase("RAZORPAY")) {

            tvPaymentTypeText.setText("Cards / UPI / Netbanking");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_razorpay", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);

            matchFound = true;
        } else if (paymentType.equalsIgnoreCase("PAYPAL")) {

            tvPaymentTypeText.setText("PAYPAL");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);


            matchFound = true;
        } else if(paymentType.equalsIgnoreCase("GOOGLE_INAPP_V2")){

            tvPaymentTypeText.setText("Google In-App Purchase");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_google_play", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if(paymentType.equalsIgnoreCase("PAYTM_UPI")){

            tvPaymentTypeText.setText("Google Pay / UPI / WhatsApp UPI");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_upi_gateway", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if(paymentType.equalsIgnoreCase("STRIPE")){

            tvPaymentTypeText.setText("International Cards");
            //Glide.with(getActivity()).load(getResources().getIdentifier("image_stripe", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("PAYTM_CARDS")) {

            tvPaymentTypeText.setText("Cards / UPI / Netbanking");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paytm", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("PAYPAL_INDIA")){

            tvPaymentTypeText.setText("PayPal India / Credit and Debit cards");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("PAYPAL_AUTO_RENEW")){

            tvPaymentTypeText.setText("PayPal Auto renew");
            //Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }else if (paymentType.equalsIgnoreCase("STRIP_RECURRING")){

            tvPaymentTypeText.setText("Strip Auto renew");
            // Glide.with(getActivity()).load(getResources().getIdentifier("img_paypal", "drawable", getActivity().getPackageName())).into(ivPaymentOptionIcon);
            matchFound = true;
        }*/

        return false;

    }

    public void setPadding(TextView tvPaymentTypeText)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);
        Log.e(TAG, "Width: "+ width);
        Log.e(TAG, "Width dp: "+ dp);

        if(dp<400) {
            tvPaymentTypeText.setTextSize(12);
            tvPaymentTypeText.setPadding(0,40,50,0);
        }else if(dp<500) {
            tvPaymentTypeText.setPadding(0, 30, 40, 0);
        }else if(dp>800) {
            tvPaymentTypeText.setPadding(0, 30, 60, 0);
        }
    }

    public void showStatesPopup(TextView txt_state)
    {
        Dialog statesDialog = new Dialog(PlansActivity.this);

        statesDialog.setContentView(R.layout.dialog_states_selection);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

        //params.y = 100; // Here is the param to set your dialog position. Same with params.x
        //statesDialog.getWindow().setAttributes(params);
        statesDialog.getWindow().setGravity(Gravity.CENTER);
        statesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        statesDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ListView simpleListView = statesDialog.findViewById(R.id.simpleListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_state, /*R.id.stateName,*/ STATES_PAYMENT);
        simpleListView.setAdapter(arrayAdapter);

        simpleListView.setOnItemClickListener((parent, view, position, id1) ->
        {
            String value = arrayAdapter.getItem(position);
            UiUtils.log(TAG, "ITEM1: " + value);
            txt_state.setText(value);
            selectedState = value;
            statesDialog.dismiss();
        });

        /*simpleListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UiUtils.log(TAG,"ITEM: TEST");
                UiUtils.log(TAG,"ITEM: "+ arrayAdapter.getItem(position));
                txt_state.setText(arrayAdapter.getItem(position));
                statesDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        statesDialog.show();

    }

    protected void getAvailablePlans(int skip) {
        if (skip == 0) {
            UiUtils.showLoadingDialog(this);
            noResultLayout.setVisibility(View.GONE);
            plansRecycler.setVisibility(View.GONE);
            ll_current_plan.setVisibility(View.GONE);
            //pay.setVisibility(View.GONE);
            plansRecycler.addOnScrollListener(scrollListener);
        }

        Map<String, Object> params = new HashMap<>();
        params.put(ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.SKIP, skip);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getAvailablePlans(APIConstants.APIs.AVAILABLE_PLANS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                if (skip == 0) {
                    plans.clear();
                    if (swipe.isRefreshing()) swipe.setRefreshing(false);
                    UiUtils.hideLoadingDialog();
                }
                if (swipe.isRefreshing()) swipe.setRefreshing(false);

                JSONObject availablePlansObj = null;
                try {
                    availablePlansObj = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (availablePlansObj != null) {
                    if (availablePlansObj.optString(SUCCESS).equals(Constants.TRUE)) {
                        JSONArray currentPlan = availablePlansObj.optJSONArray(Params.ACTIVE_PLAN);
                        if(currentPlan!=null)
                        {
                            discounted_Listed_price = true;
                            try {

                                ll_current_plan.setVisibility(View.VISIBLE);
                                if (currentPlan.getJSONObject(0).optString(Params.LOGIN_BY).equalsIgnoreCase(MANUAL_LOGIN))
                                    current_plan_user.setText(currentPlan.getJSONObject(0).optString(Params.MOBILE));
                                else
                                    current_plan_user.setText(currentPlan.getJSONObject(0).optString(Params.EMAIL));
                                current_plan_order_id.setText(currentPlan.getJSONObject(0).optString(Params.ORDER_ID));
                                current_plan_trans_id.setText("XXXXX"+currentPlan.getJSONObject(0).optString(Params.TRANSACTION_ID));

//                                current_plan_expiry_date.setText(currentPlan.getJSONObject(0).optString(Params.EXPIRIES_ON));
                                text=currentPlan.getJSONObject(0).optString(Params.EXPIRIES_ON);
                                current_plan_expiry_date.setText(AppUtils.dateFormattChange(text));

                                current_plan_name.setText(currentPlan.getJSONObject(0).optString(Params.TITLE));
                                String[] plan =currentPlan.getJSONObject(0).optString(Params.PLAN_FORMATTED).split("\\s");
                                current_plan_months.setText(plan[0]);
                                current_plan_text_months.setText(plan[1]);
                                current_plan_amount.setText(currentPlan.getJSONObject(0).optString(Params.SUBSCRIPTION_AMOUNT));
                                current_txt_plan.setText(currentPlan.getJSONObject(0).optString(Params.PLAN_FORMATTED));

                                if(currentPlan.getJSONObject(0).optInt(Params.AMOUNT)!=0) {
                                    current_plan_original_amount.setVisibility(View.VISIBLE);
                                    current_plan_original_amount_rupee.setText(currentPlan.getJSONObject(0).optString(Params.SYMBOL));
                                    current_plan_amount_rupee.setText(currentPlan.getJSONObject(0).optString(Params.SYMBOL));
                                    if((currentPlan.getJSONObject(0).optString(Params.SYMBOL).equalsIgnoreCase(""))) {
                                        current_plan_original_amount_rupee.setText(getResources().getString(R.string.currency));
                                        current_plan_amount_rupee.setText(getResources().getString(R.string.currency));
                                    }

                                    current_plan_original_amount_rupee.setPaintFlags(current_plan_original_amount_rupee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    current_plan_original_amount.setPaintFlags(current_plan_original_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    current_plan_original_amount.setText(String.valueOf(currentPlan.getJSONObject(0).optInt(Params.AMOUNT)));
                                }else
                                {
                                    current_plan_original_amount.setVisibility(View.GONE);
                                }
                            }catch (Exception e)
                            {
                                UiUtils.log(TAG, Log.getStackTraceString(e));
                            }
                        }else {
                            JSONArray plansArray = availablePlansObj.optJSONArray(Params.DATA);
                            for (int i = 0; i < plansArray.length(); i++) {
                                JSONObject planObj = plansArray.optJSONObject(i);
                                SubscriptionPlan plan = ParserUtils.parsePlan(planObj);
                                plans.add(plan);
                            }
                            if (plansArray.length() == 0) {
                                plansRecycler.removeOnScrollListener(scrollListener);
                            } else {
                                plan = plans.get(0);
                                PlanAdapter.selectedposition = 0;
                            }
                            onDataChanged();
                        }
                    } else {
                        UiUtils.showShortToast(getApplicationContext(), availablePlansObj.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                noResultLayout.setVisibility(View.VISIBLE);
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    private void onDataChanged() {
        planAdapter.notifyDataSetChanged();
        discounted_price.notifyDataSetChanged();
        noResultLayout.setVisibility(plans.isEmpty() ? View.VISIBLE : View.GONE);
        plansRecycler.setVisibility(plans.isEmpty() ? View.GONE : View.VISIBLE);
        noResultLayout.setVisibility(discountplans.isEmpty() ? View.VISIBLE : View.GONE);
        //pay.setVisibility(plans.isEmpty() ? View.GONE : View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String paymentId;
        if (requestCode == PAY_PAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        UiUtils.log(TAG,paymentDetails);
                        JSONObject details = new JSONObject(paymentDetails);
                        paymentId = details.optJSONObject(RESPONSE).optString(ID);
                        sendPayPalPaymentToBackend(paymentId);
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                onPaymentFailed(getString(R.string.something_went_wrong));
            }
        }
        if (requestCode == PGConstants.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                try{
                    String sdkResponse=data.getStringExtra(PGConstants.SDK_PAYMENT_RESPONSE);
                    if(sdkResponse==null){
                        UiUtils.log(TAG,"Transaction Error!");
                        //transactionIdView.setText("Transaction ID: NIL");
                        //transactionStatusView.setText("Transaction Status: Transaction Error!");
                        onPaymentFailed(getString(R.string.invalid_payment));
                    }else{
                        JSONObject response = new JSONObject(sdkResponse);
                        if(response.has("payment_response")&&response.getString("status").equals("success")){
                            JSONObject paymentResponse = new JSONObject(response.getString("payment_response"));
                            UiUtils.log(TAG, "paymentResponse: "+paymentResponse.toString());
                            sendAgreePayPaymentToBackend(invoice.isPayingForPlan(),
                                    invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId(), paymentResponse);
                            //transactionIdView.setText("Transaction ID: "+paymentResponse.getString("transaction_id"));
                            //transactionStatusView.setText("Transaction Status: "+paymentResponse.getString("response_message"));
                        }else{
                            UiUtils.log(TAG, "paymentResponse: Something went wrong");
                            onPaymentFailed(getString(R.string.invalid_payment));
                            //transactionIdView.setText("Transaction Status: "+response.getString("status"));
                            //transactionStatusView.setText("Transaction Message: "+response.getString("error_message"));
                        }
                    }

                }catch (Exception ep) {
                    UiUtils.log(TAG, Log.getStackTraceString(ep));
                }
            }
        }
        else if  ( requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE)
        {
            // value passed in AutoResolveHelper
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    handlePaymentSuccess(paymentData);
                    break;

                case Activity.RESULT_CANCELED:
                    // The user cancelled the payment attempt
                    Toast.makeText(this, getString(R.string.canclePay), Toast.LENGTH_SHORT).show();
                    break;

                case AutoResolveHelper.RESULT_ERROR:
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    handleError(status.getStatusCode());
                    Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == UPI_INTENT)
        {
            if(data!=null) {
                if (data.getStringExtra(PAYMENT_RESP).equalsIgnoreCase(SUCCESS))
                    onPaymentSucceeded(invoice);
                else
                    onPaymentFailed(getString(R.string.invalid_payment));
            }else
            {
                UiUtils.log(TAG,"Back button pressed");
                onPaymentFailed(getString(R.string.invalid_payment));
            }
        }else if (requestCode == IN_APP_GATEWAY)
        {
            if(data!=null) {
                if (data.getStringExtra(PAYMENT_RESP).equalsIgnoreCase(SUCCESS))
                    onPaymentSucceeded(invoice);
                else
                    onPaymentFailed(getString(R.string.invalid_payment));
            }else
            {
                UiUtils.log(TAG,"Back button pressed");
                onPaymentFailed(getString(R.string.invalid_payment));
            }
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            onPaymentFailed(getString(R.string.invalid_payment));
        }
    }

    private void sendPayPalPaymentToBackend(String paymentId) {
        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.SUBSCRIPTION_ID, invoice.getPlan().getId());
        params.put(Params.PAYMENT_ID, paymentId);
        params.put(Params.COUPON_CODE, invoice.getCouponCode());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.makePayPalPlanPayment(APIConstants.APIs.MAKE_PAY_PAL_PAYMENT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    if (paymentObject.optString(SUCCESS).equals(Constants.TRUE)) {
                        invoice.setPaymentId(paymentId);
                        onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        Toast.makeText(PlansActivity.this, paymentObject.optString(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    @Override
    public void onPaymentSucceeded(Invoice invoice) {
        PlansActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    invoice.setStatus(getString(R.string.success));
                    //invoiceSheet = InvoiceBottomSheet.getInstance(invoice);
                    //invoiceSheet.show(getSupportFragmentManager(), invoiceSheet.getTag());
                    if(billingClient.getConnectionState()== BillingClient.ConnectionState.CONNECTED)
                        billingClient.endConnection();
                    if(paymentDialog!=null)
                        paymentDialog.dismiss();
                    Dialog dialogSuccess = new Dialog(PlansActivity.this);

                    dialogSuccess.setContentView(R.layout.dialog_payment_success);
                    WindowManager.LayoutParams params = dialogSuccess.getWindow().getAttributes(); // change this to your dialog.

                    params.y = 100; // Here is the param to set your dialog position. Same with params.x
                    dialogSuccess.getWindow().setAttributes(params);
                    dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialogSuccess.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialogSuccess.getWindow().setGravity(Gravity.CENTER);
                    dialogSuccess.setCancelable(false);

                    TextView payment_message = dialogSuccess.findViewById(R.id.payment_message);
                    payment_message.setText(String.format("Your payment of %s%s was successful.", invoice.getCurrency(), invoice.getPaidAmount()));

                    Button submitBtn = dialogSuccess.findViewById(R.id.submit_btn);
                    submitBtn.setOnClickListener((View v) -> {
                        dialog.dismiss();
                        finish();
                    });

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;
                    int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);
                    Log.e(TAG, "Width: "+ width);
                    Log.e(TAG, "Width dp: "+ dp);

                    if(dp<400) {
                        submitBtn.setPadding(0,35,30,0);
                    }else if(dp<500) {
                        submitBtn.setPadding(0, 30, 30, 0);
                    }else if(dp>800) {
                        submitBtn.setPadding(0, 30, 30, 0);
                    }

                    //finish();
                    dialogSuccess.show();


                } catch (Exception e) {
                    //finish();
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onPaymentFailed(String failureReason) {
        PlansActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(paymentDialog!=null)
                    paymentDialog.dismiss();

                Dialog dialog = new Dialog(PlansActivity.this);

                dialog.setContentView(R.layout.dialog_payment_fail);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

                params.y = 100; // Here is the param to set your dialog position. Same with params.x
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.setCancelable(false);

                TextView payment_message = dialog.findViewById(R.id.payment_message);
                //payment_message.setText();

                dialog.findViewById(R.id.support_btn).setOnClickListener((View v) -> {
                    dialog.dismiss();
                    startActivity(new Intent(PlansActivity.this, SupportActivity.class));
                });

                Button submitBtn = dialog.findViewById(R.id.submit_btn);
                submitBtn.setOnClickListener((View v) -> dialog.dismiss());

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);
                Log.e(TAG, "Width: "+ width);
                Log.e(TAG, "Width dp: "+ dp);

                if(dp<400) {
                    submitBtn.setPadding(0,35,30,0);
                }else if(dp<500) {
                    submitBtn.setPadding(0, 30, 30, 0);
                }else if(dp>800) {
                    submitBtn.setPadding(0, 30, 30, 0);
                }

                dialog.show();
            }
        });
    }

    @Override
    public void onMakePayPalPayment(Invoice invoice) {
        //this.invoice = invoice;

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(invoice.getPaidAmount())), invoice.getCurrencySymbol(), invoice.getTitle(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAY_PAL_REQUEST_CODE);
    }

    @Override
    public void onLoadMore(int skip) {
        getAvailablePlans(skip);
    }

    private boolean setPaymentsInterface(PaymentBottomSheet.PaymentsInterface paymentsListener, SubscriptionPlan planData, Video videoData) {
        if (planData == null && videoData == null)
            return true;

        paymentsInterface = paymentsListener;
        invoice = new Invoice();
        invoice.setPlan(planData);
        invoice.setVideo(videoData);

        plan = invoice.getPlan();
        video = invoice.getVideo();

        invoice.setPayingForPlan(plan != null);
        UiUtils.log("payment", "Plan "+ invoice.isPayingForPlan());
        if (invoice.isPayingForPlan()) {
            UiUtils.log("payment", "Plan");
            if (plan != null) {
                invoice.setTitle(plan.getTitle());
                invoice.setTotalAmount(plan.getAmount());
                if (discounted_Listed_price== true) {
                    invoice.setPaidAmount(plan.getPercentage());
                }else
                {
                    invoice.setPaidAmount(plan.getListedPrice());
                }
                invoice.setCouponAmount(0.0);
                //invoice.setMonths(plan.getMonthFormatted());
                invoice.setMonths(plan.getMonths());
                invoice.setCouponApplied(false);
                invoice.setCouponCode("");
                invoice.setCurrency(plan.getCurrency());
                invoice.setCurrencySymbol("USD");
            } else {
                return true;
            }
        } else {
            if (video != null) {
                UiUtils.log("payment", "Video");
                invoice.setTitle(video.getTitle());
                invoice.setTotalAmount(video.getAmount());
                invoice.setPaidAmount(video.getAmount());
                invoice.setCouponAmount(0.0);
                invoice.setCouponApplied(false);
                invoice.setCouponCode("");
                invoice.setCurrency(video.getCurrency());
                invoice.setCurrencySymbol("USD");
            } else {
                UiUtils.log("payment", "True");
                return true;
            }
        }
        return false;
    }

    //Paypal
    private void getPayPalPayment() {
        /*PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(invoice.getPaidAmount())), invoice.getCurrencySymbol(), invoice.getTitle(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);*/
        if (paymentsInterface != null) {
            //finish();
            paymentsInterface.onMakePayPalPayment(invoice);
        }
    }

    //Stripe
    private void sendStripePaymentToBackend(boolean isPayingForPlan, int id) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        UiUtils.showLoadingDialog(this);

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.COUPON_CODE, invoice.getCouponCode());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call;
        if (!isPayingForPlan) {
            params.put(Params.ADMIN_VIDEO_ID, id);
            call = apiInterface.makeStripePPV(APIConstants.APIs.MAKE_STRIPE_PPV, params);
        }
        else {
            params.put(Params.SUBSCRIPTION_ID, id);
            call = apiInterface.makeStripePayment(APIConstants.APIs.MAKE_STRIPE_PAYMNET, params);
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    if (paymentObject.optString(SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(Params.DATA);
                        String paymentId = payObject.optString(Params.PAYMENT_ID);
                        invoice.setPaymentId(paymentId);
                        finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(ERROR_MESSAGE));
                        switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    //Agreepay
    private void onStartAgreePayTransaction()
    {
        try {
            String id = initOrderId();

            String phone = prefUtils.getStringValue(PrefKeys.USER_MOBILE, "");
           /* UiUtils.log(TAG, "phone: " + prefUtils.getStringValue(PrefKeys.USER_MOBILE,
                    prefUtils.getStringValue(PrefKeys.DEFAULT_MOBILE, "8433900961Extra")));*/
            if(checkString(phone))
                phone = prefUtils.getStringValue(PrefKeys.DEFAULT_MOBILE, "8433900961");//"8433900961";

            String email = prefUtils.getStringValue(PrefKeys.USER_EMAIL, "");
            if(checkString(email))
                email =  prefUtils.getStringValue(PrefKeys.DEFAULT_EMAIL, "connect@bhoju.com");//"connect@bhoju.com";

            UiUtils.log(TAG, "phone: " + phone);
            UiUtils.log(TAG, "email: " + email);

            PaymentParams pgPaymentParams = new PaymentParams();
            pgPaymentParams.setAPiKey(AP_API_KEY);
            pgPaymentParams.setAmount(String.valueOf(invoice.getPaidAmount()));
            pgPaymentParams.setEmail(email);
            pgPaymentParams.setName(prefUtils.getStringValue(PrefKeys.USER_NAME, ""));
            pgPaymentParams.setPhone(phone);
            pgPaymentParams.setOrderId(id);
            pgPaymentParams.setCurrency(AP_CURRENCY);
            pgPaymentParams.setDescription(id);
            if(CURRENT_COUNTRY.equalsIgnoreCase("India"))
                pgPaymentParams.setCity(CURRENT_REGION);
            else
                pgPaymentParams.setCity("Mumbai");
            UiUtils.log(TAG, "CURRENT_REGION:" + CURRENT_REGION);
            pgPaymentParams.setState("Maharashtra");
            pgPaymentParams.setAddressLine1("");
            pgPaymentParams.setAddressLine2("");
            pgPaymentParams.setZipCode("400053");
            pgPaymentParams.setCountry(AP_COUNTRY);
            pgPaymentParams.setReturnUrl(AP_RETURN_URL);
            pgPaymentParams.setMode(AP_MODE);
            pgPaymentParams.setUdf1("1");
            pgPaymentParams.setUdf2("2");
            pgPaymentParams.setUdf3("3");
            pgPaymentParams.setUdf4("4");
            pgPaymentParams.setUdf5("5");
            pgPaymentParams.setEnableAutoRefund("n");
            pgPaymentParams.setOfferCode("");
            pgPaymentParams.setPaymentHostname(AP_HOST_NAME);
            //pgPaymentParams.setSplitInfo("{\"vendors\":[{\"vendor_code\":\"24VEN985\",\"split_amount_percentage\":\"20\"}]}");

            PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, PlansActivity.this);
            pgPaymentInitialzer.initiatePaymentProcess();
        }catch (Exception e)
        {
            e.printStackTrace();
            UiUtils.log(TAG,"ERROR: "+ e.getMessage());
        }
    }

    private void sendAgreePayPaymentToBackend(boolean isPayingForPlan, int id, JSONObject inResponse) {

        try {

            PrefUtils prefUtils = PrefUtils.getInstance(this);
            HashMap<String, String> deviceDetails = Utils.getDeviceDetails(this);
            UiUtils.showLoadingDialog(this);

            Map<String, Object> params = new HashMap<>();
            params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
            params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
            params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
            params.put(Params.ADMIN_VIDEO_ID, id);
            params.put(Params.SUBSCRIPTION_ID, plan.getId());
            params.put(Params.COUPON_CODE, invoice.getCouponCode());
            params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
            params.put(AP_RESP_MSG, inResponse.getString(AP_RESP_MSG));
            params.put(AP_ORDER_ID, inResponse.getString(AP_ORDER_ID));
            params.put(Params.AP_AMOUNT, inResponse.getString(Params.AP_AMOUNT));
            params.put(Params.AP_TRANSACTION_ID, inResponse.getString(Params.AP_TRANSACTION_ID));
            params.put(Params.AP_PAYMENT_METHOD, inResponse.getString(Params.AP_PAYMENT_MODE));
            params.put(Params.CURRENCY, inResponse.getString(Params.CURRENCY));
            params.put(AP_ERROR_MSG, inResponse.getString(AP_ERROR_MSG));
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
            params.put(DEVICE_CODE, getSecureId(PlansActivity.this));

            Call<String> call = apiInterface.makePaytmPayment(MAKE_AGREEPAY_PAYMENT, params);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    UiUtils.hideLoadingDialog();
                    JSONObject paymentObject = null;
                    try {
                        paymentObject = new JSONObject(response.body());
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                    if (paymentObject != null) {
                        if (paymentObject.optString(SUCCESS).equals(Constants.TRUE)) {
                            prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE,"");

                            UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(Params.MESSAGE) +
                                    (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                            JSONObject payObject = paymentObject.optJSONObject(Params.DATA);
                            String paymentId = payObject.optString(Params.PAYMENT_ID);
                            invoice.setPaymentId(paymentId);
                            //finish();
                            paymentsInterface.onPaymentSucceeded(invoice);
                        } else {
                            UiUtils.hideLoadingDialog();
                            UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(ERROR_MESSAGE));
                            paymentsInterface.onPaymentFailed(paymentObject.optString(ERROR_MESSAGE));
                        /*switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }*/
                        }
                    }
                    try {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    NetworkUtils.onApiError(PlansActivity.this);
                }
            });
        }catch (Exception e)
        {
            UiUtils.log(TAG,"ERROR: "+ e.getMessage());
        }
    }

    //Google Play Billing
    private void setUpGooglePlayBilling()
    {
        //skuList.add("android.test.purchased");
        //skuList.add("rokkt_test");
        //skuList.add("gas");

        UiUtils.log(TAG,"PurchasesUpdatedListener");
        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
                // To be implemented in a later section.
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

                    UiUtils.log(TAG, "onPurchasesUpdated");
                    for (Purchase purchase : list) {
                        UiUtils.log(TAG, "onPurchasesUpdated: "+ purchase.toString());
                        handlePurchase(purchase);
                    }
                    UiUtils.log(TAG, "onPurchasesUpdated finished");
                }else {
                    paymentsInterface.onPaymentFailed(FAILED);
                }
            }
        };

        UiUtils.log(TAG,"BillingClient");

        billingClient = BillingClient.newBuilder(PlansActivity.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        //onStartGooglePlayBillingTransaction();

        billingClientStateListener = new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    getProductDetailsAndSetResponseListeners();

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                UiUtils.log(TAG, "DISCONNECTED");
            }
        };

    }

    private void onStartGooglePlayBillingTransaction()
    {
        billingClient.startConnection(billingClientStateListener/*new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    getProductDetailsAndSetResponseListeners();

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        }*/);
    }

    private void getProductDetailsAndSetResponseListeners() {

        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(plan.getGooglePlayProductId()) //"android.test.purchased"
                .setProductType(BillingClient.ProductType.SUBS)
                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();


        billingClient.queryProductDetailsAsync(
                params,
                new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(BillingResult billingResult, List<ProductDetails> productDetailsList) {
                        // Process the result
                        UiUtils.log(TAG, "onProductDetailsResponse: "+ billingResult.toString());
                        UiUtils.log(TAG, "onProductDetailsResponse: "+ productDetailsList.toString());
                        ProductDetails productDetails = productDetailsList.get(0);
                        UiUtils.log(TAG, "onProductDetailsResponse: "+ productDetails.toString());

                        // Retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                        // Get the offerToken of the selected offer
                        String offerToken = productDetails
                                .getSubscriptionOfferDetails().get(0)
                                .getOfferToken();
                        // Set the parameters for the offer that will be presented
                        // in the billing flow creating separate productDetailsParamsList variable
                        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                                ImmutableList.of(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                .setProductDetails(productDetails)
                                                .setOfferToken(offerToken)
                                                .build()
                                );

                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build();

                        // Launch the billing flow
                        BillingResult billingResultFlow = billingClient.launchBillingFlow(PlansActivity.this, billingFlowParams);
                        UiUtils.log(TAG, "Plan: "+ billingResultFlow);

                    }
                }
        );

        //old code
        /*List<String> skuList = new ArrayList<>();
        //skuList.add(String.valueOf(subscriptionPlan.getId()));// product/subscription Id from google play
        //skuList.add("android.test.purchased");
        skuList.add(plan.getGooglePlayProductId());

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);


        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    for (SkuDetails skuDetails : list) {

                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetails)
                                .build();
                        UiUtils.log(TAG, "querySkuDetailsAsync");
                        billingClient.launchBillingFlow(PlansActivity.this,flowParams);
                    }
                }
            }


        });*/


        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) { }
                UiUtils.log(TAG, "queryPurchaseHistoryAsync");
            }

        });
    }

    private void handlePurchase(Purchase purchase) {

        sendGooglePlayPaymentToBackend(purchase);
        UiUtils.log(TAG,"Response: "+purchase.getOriginalJson());
        //paymentsInterface.onPaymentSucceeded(invoice);

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        //.setDeveloperPayload(purchase.getDeveloperPayload())
                        .build();

        //Consume the Purchase
        billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    //  sendResultBack(new UserInfo(),"SUCCESS");

                }
            }

        });

    }

    private void sendGooglePlayPaymentToBackend(Purchase purchase) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(this);
        UiUtils.showLoadingDialog(this);

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.ADMIN_VIDEO_ID, id);
        params.put(Params.SUBSCRIPTION_ID, plan.getId());
        params.put(Params.COUPON_CODE, invoice.getCouponCode());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(ORDERID, initOrderId());
        params.put(Params.GOOGLE_PLAY_ORDERID, purchase.getOrderId());
        params.put(Params.GOOGLE_PLAY_PACKAGE_NAME, purchase.getPackageName());
        params.put(Params.GOOGLE_PLAY_PRODUCTID, plan.getGooglePlayProductId());
        params.put(Params.GOOGLE_PLAY_PURCHASE_TIME, ""+getDateConversion(purchase.getPurchaseTime()));
        params.put(Params.GOOGLE_PLAY_PURCHASE_STATE, purchase.getPurchaseState());
        params.put(Params.GOOGLE_PLAY_PURCHASE_TOKEN, purchase.getPurchaseToken());
        params.put(Params.GOOGLE_PLAY_QUANTITY, purchase.getQuantity());
        params.put(Params.GOOGLE_PLAY_AUTO_RENEWING, purchase.isAutoRenewing());
        params.put(Params.GOOGLE_PLAY_ACKNOWLEDGED, purchase.isAcknowledged());
        params.put(Params.DATA, purchase.getOriginalJson());
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
        params.put(DEVICE_CODE, getSecureId(PlansActivity.this));

        Call<String> call = apiInterface.makePaytmPayment(MAKE_GOOGLE_PLAY_PAYMENT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    if (paymentObject.optString(SUCCESS).equals(Constants.TRUE)) {
                        prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE,"");

                        UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(Params.DATA);
                        String paymentId = payObject.optString(Params.PAYMENT_ID);
                        invoice.setPaymentId(paymentId);
                        //finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(ERROR_MESSAGE));
                        paymentsInterface.onPaymentFailed(paymentObject.optString(ERROR_MESSAGE));
                        /*switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    //Paytm
    private void onStartTransaction(String paymentType) {

        //PaytmPGService Service = PaytmPGService.getStagingService();

        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        paytmParamMap = new HashMap<>();
        String id = initOrderId();

        //Staging
        /*paytmParamMap.put( MID , getTestMIDKeys());
        // Key in your staging and production MID available in your dashboard
        paytmParamMap.put( PARAM_ORDER_ID , id);
        paytmParamMap.put( PARAM_CUST_ID , String.valueOf(prefUtils.getIntValue(PrefKeys.USER_ID, -1)));
        //paytmParamMap.put( "MOBILE_NO" , "7777777777");
        //paytmParamMap.put( "EMAIL" , "username@emailprovider.com");
        paytmParamMap.put( PARAM_CHANNEL_ID , getTestChannelIdKeys());
        paytmParamMap.put( PARAM_TXN_AMOUNT , String.valueOf(invoice.getPaidAmount()));
        paytmParamMap.put( WEBSITE , getTestWebsiteKeys());
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PARAM_INDUSTRY_TYPE_ID , getTestIndustryTypeIdKeys());
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PAYTM_CALLBACK_URL, TEST_CALLBACK_URL+id);*/


        //Production
        paytmParamMap.put( MID , getLiveMIDKeys());
        // Key in your staging and production MID available in your dashboard
        paytmParamMap.put( PARAM_ORDER_ID , id);
        paytmParamMap.put( PARAM_CUST_ID ,String.valueOf(prefUtils.getIntValue(PrefKeys.USER_ID, -1)));
        //paytmParamMap.put( "MOBILE_NO" , "7777777777");
        //paytmParamMap.put( "EMAIL" , "username@emailprovider.com");
        paytmParamMap.put( PARAM_CHANNEL_ID , getLiveChannelIdKeys());
        paytmParamMap.put( PARAM_TXN_AMOUNT , String.valueOf(invoice.getPaidAmount()));
        paytmParamMap.put( WEBSITE , getLiveWebsiteKeys());
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PARAM_INDUSTRY_TYPE_ID , getLiveIndustryTypeIdKeys());
        //paytmParamMap.put( "paymentMode" , "UPI_INTENT");
        // This is the staging value. Production value is available in your dashboard
        paytmParamMap.put( PAYTM_CALLBACK_URL, LIVE_CALLBACK_URL+id);
        paytmParamMap.put(PAYMENT_GATEWAY_TYPE,paymentType);

        getChecksum(id,paytmParamMap);
    }

    private String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId =  String.format(Locale.ENGLISH,"ORDS_%05d_%d_%d", r.nextInt(100000),plan.getId(), id);
        /*String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000) + "_" + plan.getId();*/
        return orderId;
    }

    private void getChecksum(final String orderId , final HashMap<String,String> paramMap) {

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.ORDER_ID, paytmParamMap.get(PARAM_ORDER_ID));
        params.put(CUST_ID, paytmParamMap.get(PARAM_CUST_ID));
        params.put(INDUSTRY_TYPE_ID, paytmParamMap.get( PARAM_INDUSTRY_TYPE_ID));
        params.put(CHANNEL_ID, paytmParamMap.get( PARAM_CHANNEL_ID));
        params.put(TXN_AMOUNT, paytmParamMap.get( PARAM_TXN_AMOUNT));
        params.put(Params.CALLBACK_URL, paytmParamMap.get( PAYTM_CALLBACK_URL));
        params.put(PAYMENT_GATEWAY_TYPE,paytmParamMap.get(PAYMENT_GATEWAY_TYPE));

        Call<String> call = apiInterface.getPaytmChecksum(PAYTM_CHECKSUM, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject checksumObj = null;
                try {
                    checksumObj = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                try {
                    if (checksumObj != null && checksumObj.getBoolean(SUCCESS)) {
                        String checksum = checksumObj.optString(Params.DATA);
                        UiUtils.log("Checksum","Value: "+ checksum);
                        paramMap.put( CHECKSUMHASH , checksum);
                        paramMap.put( TXN_TOKEN , checksum);

                        startPaytmService(paramMap);

                        //UiUtils.showShortToast(getApplicationContext(), availablePlansObj.optString(Params.ERROR_MESSAGE));

                    }
                } catch (JSONException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                //noResultLayout.setVisibility(View.VISIBLE);
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });

    }

    private void startPaytmService(HashMap<String, String> paramMap)
    {
        PaytmOrder paytmOrder = new PaytmOrder(paytmParamMap.get(PARAM_ORDER_ID),paytmParamMap.get(MID),paytmParamMap.get(TXN_TOKEN),
                paytmParamMap.get(TXN_AMOUNT),paytmParamMap.get(PAYTM_CALLBACK_URL));
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable Bundle inResponse) {

                if(inResponse!=null) {
                    UiUtils.log("onTransactionResponse", "Payment Transaction : " + inResponse);
                    //UiUtils.log("onTransactionResponse", "Payment Transaction : " + inResponse.getString(Params.PAYTM_STATUS));
                    if (inResponse.getString(Params.PAYTM_STATUS).equalsIgnoreCase("TXN_SUCCESS")) {
                        sendPaytmPaymentToBackend(invoice.isPayingForPlan(),
                                invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId(), inResponse);
                    } else {
                        paymentsInterface.onPaymentFailed("Something");
                    }
                }else
                {
                    paymentsInterface.onPaymentFailed("Something");
                }
            }

            @Override
            public void networkNotAvailable() {
                UiUtils.log("LOG", "Payment Transaction : Network error");
                //Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorProceed(String inErrorMessage) {
                UiUtils.log("LOG", "onErrorProceed : "+ inErrorMessage);
            }

            @Override
            public void clientAuthenticationFailed(String authFailedMessage) {
                UiUtils.log("clientAuthenticationFailed", "Payment Transaction : " + authFailedMessage);
                //Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + authFailedMessage, Toast.LENGTH_LONG).show();
                paymentsInterface.onPaymentFailed(authFailedMessage);
            }

            @Override
            public void someUIErrorOccurred(String uiErrorMessages) {
                try {
                    UiUtils.log("someUIErrorOccurred", "Payment Transaction : " + uiErrorMessages);
                    //PlansActivity.this.finalize();
                } catch (Throwable throwable) {
                    UiUtils.log(TAG, Log.getStackTraceString(throwable));
                }
                paymentsInterface.onPaymentFailed(uiErrorMessages);
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode,
                                              String inErrorMessage, String inFailingUrl) {
                UiUtils.log("onErrorLoadingWebPage", "Payment Transaction : " + inErrorMessage);
                //Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
                paymentsInterface.onPaymentFailed(inErrorMessage);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                UiUtils.log("onBackPressedCancelTransaction", "Payment Transaction : " + "Transaction cancelled");
                //Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                UiUtils.log("onTransactionCancel", "Payment Transaction Failed " + inErrorMessage);
                //Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                paymentsInterface.onPaymentFailed("Something");
            }
        });

        transactionManager.setAppInvokeEnabled(false);
        transactionManager.setEmiSubventionEnabled(true);
        transactionManager.startTransaction(this, PAYTM);

        //transactionManager.startTransactionAfterCheckingLoginStatus(this, clientId, 100);
    }

    private void sendPaytmPaymentToBackend(boolean isPayingForPlan, int id, Bundle inResponse) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(this);
        UiUtils.showLoadingDialog(this);

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.ADMIN_VIDEO_ID, id);
        params.put(Params.SUBSCRIPTION_ID, plan.getId());
        params.put(Params.COUPON_CODE, invoice.getCouponCode());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.PAYTM_STATUS, inResponse.getString(Params.PAYTM_STATUS));
        params.put(CHECKSUMHASH, inResponse.getString(CHECKSUMHASH));
        params.put(ORDERID, inResponse.getString(ORDERID));
        params.put(Params.TXNAMOUNT, inResponse.getString(Params.TXNAMOUNT));
        params.put(Params.TXNDATE, inResponse.getString(Params.TXNDATE));
        params.put(Params.MID, inResponse.getString(Params.MID));
        params.put(Params.TXNID, inResponse.getString(Params.TXNID));
        params.put(Params.RESPCODE, inResponse.getString(Params.RESPCODE));
        params.put(Params.PAYMENTMODE, inResponse.getString(Params.PAYMENTMODE));
        params.put(Params.BANKTXNID, inResponse.getString(Params.BANKTXNID));
        params.put(Params.CURRENCY, inResponse.getString(Params.CURRENCY.toUpperCase()));
        params.put(Params.GATEWAYNAME, inResponse.getString(Params.GATEWAYNAME));
        params.put(Params.RESPMSG, inResponse.getString(Params.RESPMSG));
        params.put(Params.BANK_NAME, checkString(inResponse.getString(Params.BANK_NAME))?"":inResponse.getString(Params.BANK_NAME));
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
        params.put(DEVICE_CODE, getSecureId(PlansActivity.this));

        Call<String> call = apiInterface.makePaytmPayment(MAKE_PAYTM_PAYMENT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    if (paymentObject.optString(SUCCESS).equals(Constants.TRUE)) {
                        prefUtils.setValue(PrefKeys.REFERENCE_URL_CODE,"");

                        UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(Params.DATA);
                        String paymentId = payObject.optString(Params.PAYMENT_ID);
                        invoice.setPaymentId(paymentId);
                        //finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.showShortToast(PlansActivity.this, paymentObject.optString(ERROR_MESSAGE));
                        paymentsInterface.onPaymentFailed(paymentObject.optString(ERROR_MESSAGE));
                        /*switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PlansActivity.this, PaymentsActivity.class);
                                startActivity(i);
                                break;
                            case APIConstants.ErrorCodes.NO_DEFAULT_CARD_FOUND:
                            default:
                                Intent toAddCard = new Intent(getApplicationContext(), AddCardActivity.class);
                                startActivity(toAddCard);
                                break;
                        }*/
                    }
                }
                try{
                    if(countDownTimer!=null){
                        countDownTimer.cancel();
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(PlansActivity.this);
            }
        });
    }

    //send paytm UPI check
    private void sendResultToServerForValidation(String orderId, int planId) {
        HashMap<String,String> deviceDetails =  Utils.getDeviceDetails(this);

        UiUtils.log(TAG,"UPI Verify Api Called");
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.ORDER_ID, orderId);
        params.put(Params.SUBSCRIPTION_ID, planId);
        params.put(Params.COUPON_CODE, "");
        params.put(Params.AGE, "0");
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

        Call<String> call = apiInterface.verifyUpiPaytmPayment(VERIFY_UPI_PAYTM_PAYMENT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    PrefHelper.setOrderDetailForConfirmation(PlansActivity.this, 0, "", -1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //Google Pay
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(View view) {
        JSONObject paymentDataRequestJson = PaymentUtilsNew.getPaymentDataRequest(10).get();
        if (paymentDataRequestJson == null) {
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        if (request != null) {
            AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String tokenizationType = tokenizationData.getString("type");
            final String token = tokenizationData.getString("token");

            if ("PAYMENT_GATEWAY".equals(tokenizationType) && "pk_test_uDYrTXzzAuGRwDYtu7dkhaF3".equals(token)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage(getString(R.string.stripe))
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }

            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");

            // Logging token string.
            UiUtils.log("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(int statusCode) {
        UiUtils.log("loadPaymentData failed", String.format(Locale.ENGLISH,"Error code: %d", statusCode));
    }

    private void checkWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        UiUtils.log(TAG, "Width: " + width);
        UiUtils.log(TAG, "Width dp: " + dp);

        if(dp<400)
        {
            setTextSize(12f);
        }
        else if(dp>800)
        {
            setTextSize(18f);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_current_plan.getLayoutParams();
            params.setMargins(200, 150, 200, 50);
            ll_current_plan.setLayoutParams(params);

        }
    }

    private void setTextSize(float size) {
        txt_current_plan_user.setTextSize(size);
        current_plan_user.setTextSize(size);
        txt_current_plan_order_id.setTextSize(size);
        current_plan_order_id.setTextSize(size);
        txt_current_plan_trans_id.setTextSize(size);
        current_plan_trans_id.setTextSize(size);
        txt_current_plan_expiry_date.setTextSize(size);
        current_plan_expiry_date.setTextSize(size);
    }
}
