package com.bhoju.app.util;

import android.util.Log;

import com.bhoju.app.ui.activity.NotificationsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static com.bhoju.app.network.APIConstants.Constants.COUNTRY_CODE;
import static com.bhoju.app.network.APIConstants.Constants.CURRENCY_CODE;

public class PaymentUtils {

    private static final String TAG = PaymentUtils.class.getSimpleName();
    public static final List<String> SUPPORTED_NETWORKS = Arrays.asList(
            "AMEX", "DISCOVER", "INTERAC", "JCB", "MASTERCARD", "VISA");

    public static final List<String> SUPPORTED_METHODS = Arrays.asList(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS");
    public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList("US", "GB");
    public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";


    public static final BigDecimal CENTS_IN_A_UNIT = new BigDecimal(100d);

    public static JSONObject getBaseRequest() throws    JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {
                {
                    put("gateway", "example");
                    put("gatewayMerchantId", "exampleGatewayMerchantId");
                }
            });
        }};
    }

    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray(SUPPORTED_NETWORKS);
    }

    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray(SUPPORTED_METHODS);
    }

    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
        parameters.put("allowedCardNetworks", getAllowedCardNetworks());
        // Optionally, you can add billing address/phone number associated with a CARD payment method.
        parameters.put("billingAddressRequired", true);

        JSONObject billingAddressParameters = new JSONObject();
        billingAddressParameters.put("format", "FULL");

        parameters.put("billingAddressParameters", billingAddressParameters);

        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

        return cardPaymentMethod;
    }

    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", COUNTRY_CODE);
        transactionInfo.put("currencyCode", CURRENCY_CODE);
        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

        return transactionInfo;
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("merchantName", "CodeGama");
        parameters.put("merchantId", "17606018764966165931");
        return parameters;
    }

    private static JSONObject getPaymentMethodDetails() throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("expirationYear", "2025");
        parameters.put("expirationMonth", "12");
        parameters.put("pan", "4895370012003478");
        parameters.put("authMethod", "CRYPTOGRAM_3DS");
        parameters.put("eciIndicator", "07");
        parameters.put("cryptogram", "AgAAAAAABk4DWZ4C28yUQAAAAAA");
        return parameters;
    }


    public static JSONObject getPaymentDataRequest(long priceCents) {

        final String price = PaymentUtils.centsToString(priceCents);

        try {
            JSONObject paymentDataRequest = PaymentUtils.getBaseRequest();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(PaymentUtils.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", PaymentUtils.getTransactionInfo(price));
            paymentDataRequest.put("merchantInfo", PaymentUtils.getMerchantInfo());

            paymentDataRequest.put("shippingAddressRequired", true);

            JSONObject shippingAddressParameters = new JSONObject();
            shippingAddressParameters.put("phoneNumberRequired", false);

            JSONArray allowedCountryCodes = new JSONArray(SHIPPING_SUPPORTED_COUNTRIES);

            shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);
            paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);
            return paymentDataRequest;

        } catch (JSONException e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public static String centsToString(long cents) {
        return new BigDecimal(cents)
                .divide(CENTS_IN_A_UNIT, RoundingMode.HALF_EVEN)
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString();
    }
}
