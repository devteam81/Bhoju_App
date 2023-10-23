package com.bhoju.app.ui.activity.googleInAppPurchase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bhoju.app.R;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.bhoju.app.network.APIConstants.Params.FAILED;
import static com.bhoju.app.network.APIConstants.Params.PAYMENT_RESP;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;


public class GoogleInAppPurchaseActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private final String TAG = GoogleInAppPurchaseActivity.class.getSimpleName();

    private BillingClient mBillingClient;
    ConsumeResponseListener consumeResponseListener;

    SubscriptionPlan subscriptionPlan;
    ConsumerOrder consumerOrder;

    private int checkoutRetryCount=0;
    

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_in_app);
        ButterKnife.bind(this);

        if(getIntent()!=null) {

            subscriptionPlan = (SubscriptionPlan) getIntent().getSerializableExtra("subscriptionDetails");

            //prepareOrderForInAppPurchase(subscriptionPlan);
            initPayment();
        }

    }


    /*public void prepareOrderForInAppPurchase(final SubscriptionPlan subscriptionPackage) {



        HashMap<String, String> deviceInfo=new HashMap<>();
        try {
            deviceInfo.put("Manufacturer", Build.MANUFACTURER);
            deviceInfo.put("Model", Build.MODEL);
            deviceInfo.put("Device", Build.DEVICE);
            deviceInfo.put("Platform", "ANDROID");
            deviceInfo.put("PlatformOSVersion", Build.VERSION.SDK_INT + "");
            deviceInfo.put("AppVersion", BuildConfig.VERSION_NAME);
            deviceInfo.put("Age",subscriptionPackage.getAge());
        }catch (Exception ex){ex.printStackTrace();}

        JSONObject parameters = new JSONObject(deviceInfo);


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, Configure.PREPARE_ORDER_FOR_IN_APP + subscriptionPackage.getId(), parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                consumerOrder = new Gson().fromJson(response.toString(),ConsumerOrder.class);

                initPayment();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                sendResultBack(FAILED);

            }
        }) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("authorization", "bearer " + OAuthUtils.getAuthToken(comtest.abethu.app.nui.subscriptionScreen.paymentGateways.googleInAppPurchase.GoogleInAppPurchaseActivity.this).getAccessToken());

                return headers;
            }
        };

        Utils.setVolleyRetryPolicy(postRequest);
        VolleySingleton.getInstance(comtest.abethu.app.nui.subscriptionScreen.paymentGateways.googleInAppPurchase.GoogleInAppPurchaseActivity.this).addToRequestQueue(postRequest, "GET_SUBSCRIPTION_ORDER_ID");
    }*/



    private void initPayment() {

        mBillingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build();

        mBillingClient.startConnection(new BillingClientStateListener() {

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
        });


    }

    private void getProductDetailsAndSetResponseListeners() {


        List<String> skuList = new ArrayList<>();
        //skuList.add(String.valueOf(subscriptionPlan.getId()));// product/subscription Id from google play
        skuList.add("android.test.purchased");

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);


        mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    for (SkuDetails skuDetails : list) {

                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetails)
                                .build();
                        UiUtils.log(TAG, "querySkuDetailsAsync");
                        mBillingClient.launchBillingFlow(GoogleInAppPurchaseActivity.this,flowParams);
                    }
                }
            }


        });




        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) { }
                UiUtils.log(TAG, "queryPurchaseHistoryAsync");
            }

        });
    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

            UiUtils.log(TAG, "onPurchasesUpdated");
            for (Purchase purchase : list) {
                UiUtils.log(TAG, "onPurchasesUpdated: "+ purchase.toString());
                handlePurchase(purchase);
            }
            UiUtils.log(TAG, "onPurchasesUpdated finished");
        }else {

            sendResultBack(FAILED);
        }


    }

    private void handlePurchase(Purchase purchase) {

        inAppSuccessCheckout(purchase);

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        //.setDeveloperPayload(purchase.getDeveloperPayload())
                        .build();

        //Consume the Purchase
        mBillingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    //  sendResultBack(new UserInfo(),"SUCCESS");

                }
            }

        });

    }


    private void inAppSuccessCheckout(Purchase purchase){

        sendResultBack(SUCCESS);

        /*UiUtils.log(TAG,"Purchase Skus: "+ purchase.getSkus().toString());
        HashMap<String, String> deviceInfo=new HashMap<>();
        try {
            deviceInfo.put("consumerOrderId",consumerOrder.getId());
            deviceInfo.put("packageName", purchase.getPackageName());
            deviceInfo.put("productId", purchase.getSkus().toString());
            deviceInfo.put("userPurchaseToken", purchase.getPurchaseToken());
        }catch (Exception ex){ex.printStackTrace();}

        JSONObject parameters = new JSONObject(deviceInfo);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Configure.CHECKOUT_IN_APP_PAYMENT, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                UserInfo userInfo = new Gson().fromJson(response.toString(), UserInfo.class);
                sendResultBack(SUCCESS);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = null;


                    checkoutRetryCount++;
                    if(checkoutRetryCount<3){
                        inAppSuccessCheckout(purchase);
                    }else{

                        sendResultBack(FAILED);

                    }



            }
        }) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                try {
                } catch (Exception ex) {
                }
                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("authorization", "bearer " + OAuthUtils.getAuthToken(comtest.abethu.app.nui.subscriptionScreen.paymentGateways.googleInAppPurchase.GoogleInAppPurchaseActivity.this).getAccessToken());

                return headers;
            }
        };
        Utils.setVolleyRetryPolicy(jsonObjectRequest);
        VolleySingleton.getInstance(comtest.abethu.app.nui.subscriptionScreen.paymentGateways.googleInAppPurchase.GoogleInAppPurchaseActivity.this).addToRequestQueue(jsonObjectRequest, "inAppSuccessCheckout");


*/
    }


    public void sendResultBack(String status){

        Intent returnIntent = new Intent();
        returnIntent.putExtra(PAYMENT_RESP,status);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

}
