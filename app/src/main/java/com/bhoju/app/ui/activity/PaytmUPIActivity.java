package com.bhoju.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.model.Invoice;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.fragment.bottomsheet.PaymentBottomSheet;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.bhoju.app.util.sharedpref.Utils;
import com.google.firebase.auth.UserInfo;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.VERIFY_UPI_PAYTM_PAYMENT;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.bhoju.app.network.APIConstants.Params.FAILED;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.PAYMENT_RESP;
import static com.bhoju.app.network.APIConstants.Params.PLAN;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;

public class PaytmUPIActivity extends AppCompatActivity {

    static {
        System.loadLibrary("keys");
    }
    public native String getPayeeNameKeys();
    public native String getPayeeVpaKeys();
    public native String getMerchantCodeKeys();

    private final String TAG = PaytmUPIActivity.class.getSimpleName();

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    APIInterface apiInterface;
    PrefUtils prefUtils;

    SubscriptionPlan plan;
    String orderId;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_upi);

        ButterKnife.bind(this);


        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);

        plan = (SubscriptionPlan) getIntent().getExtras().getSerializable(PLAN);
        UiUtils.log(TAG, String.valueOf(plan.getId()) );
        UiUtils.log(TAG, String.valueOf(plan.getAmount()) );

        startPayment(plan);

    }


    private String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId =  String.format(Locale.ENGLISH,"ORDS_%05d_%d_%d", r.nextInt(100000),plan.getId(), prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        /*String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000) + "_" + plan.getId();*/
        return orderId;
    }

    //UPI
    private void startPayment(SubscriptionPlan plan) {

        double amountReceived = plan.getListedPrice(); //1f;
        String currency = plan.getCurrency();
        try {
            String orderId = initOrderId();
            String payeeVpa = getPayeeVpaKeys();
            String payeeName = getPayeeNameKeys();
            String merchantCode = getMerchantCodeKeys();
            //Other not mandatory details
            /*String pspid = ((TextView) findViewById(R.id.edtPspId)).getText().toString();
            String notes = ((TextView) findViewById(R.id.edtNotes)).getText().toString();
            String minAmount = ((TextView) findViewById(R.id.edtMinAmount)).getText().toString();
            String currency = ((TextView) findViewById(R.id.edtCurrency)).getText().toString();
            String txnUrl = ((TextView) findViewById(R.id.edtRefId)).getText().toString();
            String deepLink = ((TextView) findViewById(R.id.edtDeepLink)).getText().toString();*/


            DecimalFormat df = new DecimalFormat("0.00");
            String amount = df.format(amountReceived);

            /*PaytmIntentUpiSdk paytmIntentUpiSdk = new PaytmIntentUpiSdk.PaytmIntentUpiSdkBuilder(payeeVpa, payeeName, merchantCode, orderId, amount, new SetPaytmUpiSdkListener() {
                @Override
                public void onTransactionComplete() {

                    startPollingForTransStatus();
                    //Toast.makeText(PaytmUPIActivity.this, "Transaction completed", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(PaytmResponseCode paytmResponseCode, String errorMessage) {

                    Toast.makeText(PaytmUPIActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    UiUtils.log(TAG,errorMessage);
                    //sendResultBack(new UserInfo(),"FAILED");
                    sendStatusBack(FAILED);
                    try{
                        if(countDownTimer!=null){
                            countDownTimer.cancel();
                        }
                    }catch (Exception ex){ex.printStackTrace();}
                }
            }).setCurrency(plan.getCode()).build();
            //.setPspGeneratedId(pspid).setTransactionNote(notes).setMinimumAmount(minAmount).setCurrency(currency).setTxnRefUrl(txnUrl).setGenericDeepLink(deepLink).build();

            PrefHelper.setOrderDetailForConfirmation(this, prefUtils.getIntValue(PrefKeys.USER_ID, 0), orderId,plan.getId());
            paytmIntentUpiSdk.startTransaction(this);*/

        }catch (Exception e){
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

    }

    private void startPollingForTransStatus(){

        if(countDownTimer!=null){
            //avoid multiple invokes
            return;
        }

        countDownTimer = new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {


                long timeSecs = millisUntilFinished / 1000;

                if (timeSecs % 5 == 0) {
                    sendResultToServerForValidation();
                }

                //tvMessage.setText("We are verifying your transaction. Kindly wait for " + timeSecs + " seconds.");
            }

            public void onFinish() {
                //Couldn't get any txn success even after continuously polling for 120Secs
                //sendResultBack(new UserInfo(), "FAILED");
                UiUtils.log(TAG,"onFinish");
                sendStatusBack(FAILED);
                try{
                    if(countDownTimer!=null){
                        countDownTimer.cancel();
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }
        }.start();
    }

    private void sendResultToServerForValidation() {
        HashMap<String,String> deviceDetails = Utils.getDeviceDetails(this);
        //Toast.makeText(getApplicationContext(),"OrderId:" + orderId,Toast.LENGTH_LONG).show();

        UiUtils.showLoadingDialog(this);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.ORDER_ID, orderId);
        params.put(APIConstants.Params.SUBSCRIPTION_ID, plan.getId());
        params.put(APIConstants.Params.COUPON_CODE, "");
        params.put(APIConstants.Params.AGE, "0");
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
                    if (paymentObject.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {

                        /*UiUtils.showShortToast(PaytmUPIActivity.this, paymentObject.optString(APIConstants.Params.MESSAGE) +
                                (!invoice.isPayingForPlan() ? "\n" + getString(R.string.play_automatically) : ""));
                        JSONObject payObject = paymentObject.optJSONObject(APIConstants.Params.DATA);*/

                        UiUtils.log(TAG,"SUCCESS");
                        sendStatusBack(SUCCESS);

                        try{
                            if(countDownTimer!=null){
                                countDownTimer.cancel();
                            }
                        }catch (Exception ex){ex.printStackTrace();}

                    } else {
                        UiUtils.hideLoadingDialog();
                        UiUtils.log(TAG,"API FAIL");
                        /*UiUtils.showShortToast(PaytmUPIActivity.this, paymentObject.optString(APIConstants.Params.ERROR_MESSAGE));*/
                        //sendStatusBack("FAILED");
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
                NetworkUtils.onApiError(PaytmUPIActivity.this);
            }
        });
    }

    private void sendStatusBack(String status) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(PAYMENT_RESP,status);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            if(countDownTimer!=null){
                countDownTimer.cancel();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
}