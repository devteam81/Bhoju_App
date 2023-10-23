package com.bhoju.app.ui.fragment.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bhoju.app.util.PaymentUtils;
import com.bhoju.app.util.sharedpref.Utils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.bhoju.app.R;
import com.bhoju.app.model.Invoice;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.AddCardActivity;
import com.bhoju.app.ui.activity.BaseActivity;
import com.bhoju.app.ui.activity.PaymentsActivity;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.PaymentUtilsNew;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.APPLY_COUPON_CODE;
import static com.bhoju.app.network.APIConstants.APIs.APPLY_PPV_CODE;
import static com.bhoju.app.network.APIConstants.APIs.INVOICE_REFERRAL_AMOUNT;
import static com.bhoju.app.network.APIConstants.APIs.MAKE_PAYTM_PAYMENT;
import static com.bhoju.app.network.APIConstants.APIs.PAYTM_CHECKSUM;
import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.CHANNEL_ID;
import static com.bhoju.app.network.APIConstants.Params.CHECKSUMHASH;
import static com.bhoju.app.network.APIConstants.Params.CUST_ID;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.INDUSTRY_TYPE_ID;
import static com.bhoju.app.network.APIConstants.Params.LIVE_CALLBACK_URL;
import static com.bhoju.app.network.APIConstants.Params.MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.MID;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.PARAM_CHANNEL_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_CUST_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_INDUSTRY_TYPE_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_ORDER_ID;
import static com.bhoju.app.network.APIConstants.Params.PARAM_TXN_AMOUNT;
import static com.bhoju.app.network.APIConstants.Params.PAYMENT_GATEWAY_TYPE;
import static com.bhoju.app.network.APIConstants.Params.PAYTM_CALLBACK_URL;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.TXN_AMOUNT;
import static com.bhoju.app.network.APIConstants.Params.TXN_TOKEN;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.network.APIConstants.Params.WEBSITE;
import static com.bhoju.app.network.APIConstants.Payments;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;
import static com.bhoju.app.util.PaymentUtils.getBaseRequest;

public class PaymentBottomSheet extends BaseActivity {

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

    private final String TAG = PaymentBottomSheet.class.getSimpleName();
    public static final int PAY_PAL_REQUEST_CODE = 200;
    public static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Payments.PayPal.CLIENT_ID);
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.planName)
    TextView planName;
    @BindView(R.id.statusText)
    TextView statusText;
    @BindView(R.id.months)
    TextView months;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.amountLayout)
    LinearLayout amountLayout;
    @BindView(R.id.couponCode)
    TextView textCouponCode;
    @BindView(R.id.couponLayout)
    LinearLayout couponLayout;
    @BindView(R.id.couponAmt)
    TextView couponAmt;
    @BindView(R.id.couponAmtLayout)
    LinearLayout couponAmtLayout;
    @BindView(R.id.totalAmt)
    TextView totalAmt;
    @BindView(R.id.totalAmtLayout)
    LinearLayout totalAmtLayout;
    @BindView(R.id.et_couponCode)
    EditText etCouponCode;
    @BindView(R.id.layout_email)
    TextInputLayout layoutEmail;
    @BindView(R.id.applyCode)
    Button applyCode;
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.beforeAppliedLayout)
    LinearLayout beforeAppliedLayout;
    @BindView(R.id.appliedCode)
    TextView appliedCode;
    @BindView(R.id.successMsg)
    TextView successMsg;
    @BindView(R.id.removeCode)
    Button removeCode;
    @BindView(R.id.appliedLayout)
    FrameLayout appliedLayout;
    @BindView(R.id.layoutPayment)
    LinearLayout layoutPayment;
    @BindView(R.id.paypal)
    Button payPal;
    @BindView(R.id.stripe)
    Button stripe;
    Unbinder unbinder;
    @BindView(R.id.gotCouponText)
    TextView gotCouponText;
    @BindView(R.id.deductedAmount)
    TextView deductedAmount;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.paytm)
    Button paytm;
    @BindView(R.id.googlePay)
    Button googlePay;
    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 1000;

    public static SubscriptionPlan plan;
    public static Video video;
    public static Invoice invoice;
    public APIInterface apiInterface;
    public static PaymentsInterface paymentsInterface;
    PaymentsClient paymentsClient;
    private static final int TEZ_REQUEST_CODE = 123;
    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    //Paytm
    private final int PAYTM = 100;
    HashMap<String, String> paytmParamMap =null;
    private PrefUtils prefUtils;
    private int id;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_info);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> finish());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);
        id = prefUtils.getIntValue(PrefKeys.USER_ID, -1);
        token = prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "");

        try {
            getCalculatedDataToPay(String.valueOf(invoice.getTotalAmount()));
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        setUpViewAndShowWhatsBeingPaidFor();

        /*Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        Wallet.getPaymentsClient(getApplicationContext(), walletOptions);*/

        paymentsClient = PaymentUtilsNew.createPaymentsClient(this);
        possiblyShowGooglePayButton();
        //paymentsClient = Wallet.getPaymentsClient(getApplicationContext(), walletOptions);
        findOutIfGoogleIsReadyForPayment();

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getSimCountryIso();

        googlePay.setOnClickListener(v -> {
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
                                    .appendQueryParameter("url", "https://test.merchant.website")
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


    }

    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentUtilsNew.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                task1 -> {
                    if (task1.isSuccessful()) {
                        setGooglePayAvailable(task1.getResult());
                    } else {
                        Log.w("isReadyToPay failed", task1.getException());
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePay.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, /*R.string.googlepay_status_unavailable*/"Google Pay Not Avaliable", Toast.LENGTH_LONG).show();
        }
    }

    public void findOutIfGoogleIsReadyForPayment() {
        try {
            IsReadyToPayRequest readyToPayRequest = IsReadyToPayRequest.fromJson(getBaseRequest().toString());
            Task<Boolean> task = paymentsClient.isReadyToPay(readyToPayRequest);
            task.addOnCompleteListener(PaymentBottomSheet.this, task1 -> {

            });
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
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

        if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            UiUtils.log("result", data.getStringExtra("Status"));
            switch (resultCode) {
                case Activity.RESULT_OK:
                    sendPayPalPaymentToBackend(invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId(), "Google-Test-Payment");
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, getString(R.string.payment_cancle), Toast.LENGTH_SHORT).show();
                    break;
            }
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
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

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

    private void setUpViewAndShowWhatsBeingPaidFor() {
        planName.setText(invoice.getTitle());
//        amount.setText(String.format("%s%s", invoice.getCurrency(), invoice.getPaidAmount()));
        totalAmt.setText(String.format("%s%s", invoice.getCurrency(), invoice.getTotalAmount()));
        if (invoice.isPayingForPlan()) {
            months.setText(String.format("%s", invoice.getMonths()));
            applyCode.setOnClickListener(view -> applyCouponCode(etCouponCode.getText().toString(), plan.getId()));
        } else {
            months.setVisibility(View.GONE);
            statusText.setVisibility(View.GONE);
            applyCode.setOnClickListener(view -> applyPPVCouponCode(etCouponCode.getText().toString(), video.getAdminVideoId()));
        }

        gotCouponText.setVisibility(invoice.getTotalAmount() == 0 ? View.GONE : View.VISIBLE);
        couponAmtLayout.setVisibility(View.GONE);
        couponLayout.setVisibility(View.GONE);
        removeCode.setOnClickListener(view -> {
            //Invoice object changes
            invoice.setCouponCode("");
            invoice.setCouponApplied(false);
            invoice.setPaidAmount(invoice.getTotalAmount());

            //UI changes
            onCouponStuffChanged(invoice.isCouponApplied());
        });

        doPaymentButtonSetups();
    }

    protected void getCalculatedDataToPay(String totalAmount) {
        UiUtils.showLoadingDialog(PaymentBottomSheet.this);
        progressBar.setVisibility(View.VISIBLE);
        PrefUtils preferences = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 0));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.AMOUNT, totalAmount);

        Call<String> call = apiInterface.getInvoiceAmount(INVOICE_REFERRAL_AMOUNT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                progressBar.setVisibility(View.GONE);
                JSONObject referralResponse = null;
                try {
                    referralResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (referralResponse != null)
                    if (referralResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONObject data = referralResponse.optJSONObject(APIConstants.Params.DATA);
                        deductedAmount.setText(data.optString(APIConstants.Params.REFERRAL_AMOUNT_FORMATTED));
                        amount.setText(data.optString(APIConstants.Params.PAY_AMOUNT_FORMATTED));
                        invoice.setPaidAmount(data.optDouble(APIConstants.Params.PAY_AMOUNT));
                        invoice.setCreditApplied(data.optInt(APIConstants.Params.IS_WALLET_CREDIT_APPLIED) == 1);
                        if (data.optInt(APIConstants.Params.PAY_AMOUNT) == 0) {
                            pay.setVisibility(View.VISIBLE);
                            gotCouponText.setVisibility(View.GONE);
                            beforeAppliedLayout.setVisibility(View.GONE);
                            payPal.setVisibility(View.GONE);
                            stripe.setVisibility(View.GONE);
                            googlePay.setVisibility(View.GONE);
                            paytm.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(PaymentBottomSheet.this, referralResponse.optString(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.paypal, R.id.stripe, R.id.pay,R.id.paytm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.paypal:
                getPayPalPayment();
                break;
            case R.id.stripe:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.are_you_sure_you_want_to_pay) + invoice.getCurrency() + invoice.getPaidAmount() + " ?")
                        .setPositiveButton(this.getString(R.string.yes), (dialogInterface, as) ->
                                sendStripePaymentToBackend(invoice.isPayingForPlan(),
                                        invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId()))
                        .setNegativeButton(this.getString(R.string.no), (dialogInterface, as) -> dialogInterface.cancel()).create().show();
                break;
            case R.id.pay:
                String paymetMethod;
                if (invoice.isCouponApplied())
                    paymetMethod = "coupon-discount";
                else if (invoice.isCreditApplied())
                    paymetMethod = "referral-discount";
                else
                    paymetMethod = "free-plan";
                sendPayPalPaymentToBackend(
                        invoice.isPayingForPlan() ? plan.getId() : video.getAdminVideoId(),
                        paymetMethod);
                break;
            case R.id.paytm:
                onStartTransaction(view);
                break;
        }
    }

    private void getPayPalPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(invoice.getPaidAmount())), invoice.getCurrencySymbol(), invoice.getTitle(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        if (paymentsInterface != null) {
            finish();
            paymentsInterface.onMakePayPalPayment(invoice);
        }
    }

    protected void applyCouponCode(String couponCode, int subId) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.SUBSCRIPTION_ID, subId);
        params.put(Params.COUPON_CODE, couponCode);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.applyCouponCode(APPLY_COUPON_CODE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject couponResponse = null;
                try {
                    couponResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (couponResponse != null) {
                    if (couponResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        JSONObject couponObj = couponResponse.optJSONObject(Params.DATA);
                        //Invoice changes
                        invoice.setCouponCode(couponObj.optString(Params.COUPON_CODE));
                        invoice.setCouponApplied(true);
                        invoice.setPaidAmount(couponObj.optDouble(Params.REMAINING_AMT));
                        invoice.setCouponAmount(couponObj.optDouble(Params.COUPON_AMT));

                        onCouponStuffChanged(invoice.isCouponApplied());
                    } else {
                        UiUtils.hideLoadingDialog();
                        Toast.makeText(PaymentBottomSheet.this, couponResponse.optString(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(PaymentBottomSheet.this);
            }
        });
    }

    private void onCouponStuffChanged(boolean couponApplied) {
        //Visibility
        appliedLayout.setVisibility(couponApplied ? View.VISIBLE : View.GONE);
        beforeAppliedLayout.setVisibility(couponApplied ? View.GONE : View.VISIBLE);
        gotCouponText.setVisibility(couponApplied ? View.GONE : View.VISIBLE);
        couponLayout.setVisibility(couponApplied ? View.VISIBLE : View.GONE);
        couponAmtLayout.setVisibility(couponApplied ? View.VISIBLE : View.GONE);
        doPaymentButtonSetups();

        //Display
        couponAmt.setText(String.format("%s%s", invoice.getCurrency(), invoice.getCouponAmount()));
        successMsg.setText(couponApplied ? getString(R.string.coupon_apply_successful) : null);
        textCouponCode.setText(invoice.getCouponCode());
        appliedCode.setText(invoice.getCouponCode());
        etCouponCode.setText(invoice.getCouponCode());
        amount.setText(String.format("%s%s", invoice.getCurrency(), invoice.getPaidAmount()));
        totalAmt.setText(String.format("%s%s", invoice.getCurrency(), invoice.getTotalAmount()));
    }

    private void doPaymentButtonSetups() {
        pay.setVisibility(invoice.getPaidAmount() == 0 ? View.VISIBLE : View.GONE);
        beforeAppliedLayout.setVisibility(invoice.getPaidAmount() == 0 ? View.GONE : View.VISIBLE);
        stripe.setVisibility(invoice.getPaidAmount() == 0 ? View.GONE : View.VISIBLE);
        payPal.setVisibility(invoice.getPaidAmount() == 0 ? View.GONE : View.VISIBLE);
    }


    //    ApplyCouponCode for PPv
    protected void applyPPVCouponCode(String couponCode, int adminVideoId) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.ADMIN_VIDEO_ID, adminVideoId);
        params.put(Params.COUPON_CODE, couponCode);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.applyPPVCode(APPLY_PPV_CODE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject couponObject = null;
                try {
                    couponObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (couponObject != null) {
                    if (couponObject.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        JSONObject couponObj = couponObject.optJSONObject(Params.DATA);

                        //Invoice changes
                        invoice.setCouponCode(couponObj.optString(Params.COUPON_CODE));
                        invoice.setCouponApplied(true);
                        invoice.setPaidAmount(couponObj.optDouble(Params.REMAINING_AMT));
                        invoice.setCouponAmount(couponObj.optDouble(Params.COUPON_AMT));

                        onCouponStuffChanged(invoice.isCouponApplied());
                    } else {
                        UiUtils.hideLoadingDialog();
                        Toast.makeText(PaymentBottomSheet.this, couponObject.optString(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(PaymentBottomSheet.this);
            }
        });
    }


    //Paytm
    public void onStartTransaction(View view) {
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
        paytmParamMap.put(PAYMENT_GATEWAY_TYPE, "upi");

        getChecksum(id,paytmParamMap);
    }

    private String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId =  String.format(Locale.ENGLISH,"ORDS_%05d_%d_%d", r.nextInt(100000),plan.getId(), id);
        /*String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000) + "_" + plan.getId();*/
        return orderId;
    }

    public void getChecksum(final String orderId , final HashMap<String,String> paramMap) {
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
                    if (checksumObj != null && checksumObj.getBoolean(Params.SUCCESS)) {
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
                NetworkUtils.onApiError(PaymentBottomSheet.this);
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
        params.put(Params.CHECKSUMHASH, inResponse.getString(Params.CHECKSUMHASH));
        params.put(Params.BANK_NAME, inResponse.getString(Params.BANK_NAME));
        params.put(Params.ORDERID, inResponse.getString(Params.ORDERID));
        params.put(Params.TXNAMOUNT, inResponse.getString(Params.TXNAMOUNT));
        params.put(Params.TXNDATE, inResponse.getString(Params.TXNDATE));
        params.put(Params.MID, inResponse.getString(Params.MID));
        params.put(Params.TXNID, inResponse.getString(Params.TXNID));
        params.put(Params.RESPCODE, inResponse.getString(Params.RESPCODE));
        params.put(Params.PAYMENTMODE, inResponse.getString(Params.PAYMENTMODE));
        params.put(Params.BANKTXNID, inResponse.getString(Params.BANKTXNID));
        params.put(Params.CURRENCY, inResponse.getString(Params.CURRENCY));
        params.put(Params.GATEWAYNAME, inResponse.getString(Params.GATEWAYNAME));
        params.put(Params.RESPMSG, inResponse.getString(Params.RESPMSG));
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

        Call<String> call = apiInterface.verifyUpiPaytmPayment(MAKE_PAYTM_PAYMENT, params);
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
                    if (paymentObject.optString(Params.SUCCESS).equals(Constants.TRUE)) {

                        UiUtils.showShortToast(PaymentBottomSheet.this, paymentObject.optString(Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(Params.DATA);
                        String paymentId = payObject.optString(Params.PAYMENT_ID);
                        invoice.setPaymentId(paymentId);
                        finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.showShortToast(PaymentBottomSheet.this, paymentObject.optString(ERROR_MESSAGE));
                        switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PaymentBottomSheet.this, PaymentsActivity.class);
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
                NetworkUtils.onApiError(PaymentBottomSheet.this);
            }
        });
    }

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
                    if (paymentObject.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(PaymentBottomSheet.this, paymentObject.optString(Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(Params.DATA);
                        String paymentId = payObject.optString(Params.PAYMENT_ID);
                        invoice.setPaymentId(paymentId);
                        finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.showShortToast(PaymentBottomSheet.this, paymentObject.optString(ERROR_MESSAGE));
                        switch (paymentObject.optInt(Params.ERROR_CODE)) {
                            case APIConstants.ErrorCodes.NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT:
                                Intent i = new Intent(PaymentBottomSheet.this, PaymentsActivity.class);
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
                NetworkUtils.onApiError(PaymentBottomSheet.this);
            }
        });
    }

    private void sendPayPalPaymentToBackend(int id, String paymentId) {
        PrefUtils prefUtils = PrefUtils.getInstance(this);

        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.PAYMENT_ID, paymentId);
        params.put(Params.COUPON_CODE, invoice.getCouponCode());
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call;
        if (invoice.isPayingForPlan()) {
            params.put(Params.SUBSCRIPTION_ID, id);
            call = apiInterface.makePayPalPlanPayment(APIConstants.APIs.MAKE_PAY_PAL_PAYMENT, params);
        }
        else {
            params.put(Params.ADMIN_VIDEO_ID, id);
            call = apiInterface.makePayPalPPV(APIConstants.APIs.MAKE_PAYPAL_PPV, params);
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
                    if (paymentObject.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        Toast.makeText(PaymentBottomSheet.this, paymentObject.optString(MESSAGE), Toast.LENGTH_SHORT).show();
                        invoice.setPaymentId(paymentId);
                        finish();
                        paymentsInterface.onPaymentSucceeded(invoice);
                    } else {
                        UiUtils.hideLoadingDialog();
                        Toast.makeText(PaymentBottomSheet.this, paymentObject.optString(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(PaymentBottomSheet.this);
            }
        });
    }

    public static boolean setPaymentsInterface(PaymentsInterface paymentsListener, SubscriptionPlan planData, Video videoData) {
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
                invoice.setTotalAmount(plan.getListedPrice());
                invoice.setPaidAmount(plan.getAmount());
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

    public interface PaymentsInterface {
        void onPaymentSucceeded(Invoice invoice);

        void onPaymentFailed(String failureReason);

        void onMakePayPalPayment(Invoice invoice);
    }
}
