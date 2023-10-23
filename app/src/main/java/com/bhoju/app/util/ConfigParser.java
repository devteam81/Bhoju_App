package com.bhoju.app.util;

import android.util.Log;

import com.bhoju.app.ui.fragment.SignupFragment;

import org.json.JSONObject;

//Singleton config provider
public class ConfigParser {

    private static final String TAG = ConfigParser.class.getSimpleName();
    private static ConfigData configData;

    private ConfigParser() {

    }

    synchronized public static ConfigData getConfig(JSONObject configResponse) {
        if (configData == null) {
            // if configData is null, initialize
            if (configResponse == null)
                return new ConfigData(new JSONObject());
            configData = new ConfigData(configResponse);
        }
        return configData;
    }

    @Override
    public String toString() {
        return configData.toString();
    }

    //POJO for config data
    public static class ConfigData {
        PaymentCreds paymentCreds;
        Urls urls;
        PushNotificationCreds pushNotificationCreds;
        private String siteName;
        private String siteLogoUrl;
        private String currencySymbol;
        private boolean redirectGiraffePlayer;
        private String jwPlayerKey;

        public ConfigData(JSONObject configData) {
            //Parse site details first
            try {
                siteName = configData.optString("site_name");
                siteLogoUrl = configData.optString("site_logo");
                currencySymbol = configData.optString("currency");

                JSONObject playerObject = configData.optJSONObject("player");
                redirectGiraffePlayer = playerObject.optInt("is_jwplayer_configured_mobile") != 1;
                jwPlayerKey = playerObject.optString("jwplayer_key_mobile");
                //parse payment details now
                paymentCreds = new PaymentCreds(configData.optJSONObject("payments"));
                //parse url details
                urls = new Urls(configData.optJSONObject("urls"));
                //push notification creds
                pushNotificationCreds = new PushNotificationCreds(configData.optJSONObject("notification"));
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
                siteName = "";
                siteLogoUrl = "";
                currencySymbol = "";
                paymentCreds = new PaymentCreds(null);
                urls = new Urls(null);
                pushNotificationCreds = new PushNotificationCreds(null);
            }
        }

        public PaymentCreds getPaymentCreds() {
            return paymentCreds;
        }

        public Urls getUrls() {
            return urls;
        }

        public PushNotificationCreds getPushNotificationCreds() {
            return pushNotificationCreds;
        }

        public String getSiteName() {
            return siteName;
        }

        public String getSiteLogoUrl() {
            return siteLogoUrl;
        }

        public String getCurrencySymbol() {
            return currencySymbol;
        }

        public boolean isRedirectGiraffePlayer() {
            return redirectGiraffePlayer;
        }

        public void setRedirectGiraffePlayer(boolean redirectGiraffePlayer) {
            this.redirectGiraffePlayer = redirectGiraffePlayer;
        }

        @Override
        public String toString() {
            return "ConfigData{" +
                    "paymentCreds=" + paymentCreds +
                    ", urls=" + urls +
                    ", pushNotificationCreds=" + pushNotificationCreds +
                    ", siteName='" + siteName + '\'' +
                    ", siteLogoUrl='" + siteLogoUrl + '\'' +
                    ", currencySymbol='" + currencySymbol + '\'' +
                    '}';
        }
    }

    public static class PushNotificationCreds {
        private String fcmSenderId;
        private String fcmApiKey;
        private String fcmServerKey;
        private String fcmProtocol;

        public PushNotificationCreds(JSONObject notificationData) {
            try {
                fcmSenderId = notificationData.optString("FCM_SENDER_ID");
                fcmApiKey = notificationData.optString("FCM_API_KEY");
                fcmServerKey = notificationData.optString("FCM_SERVER_KEY");
                fcmProtocol = notificationData.optString("FCM_PROTOCOL");
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
                fcmSenderId = "";
                fcmApiKey = "";
                fcmServerKey = "";
                fcmProtocol = "";
            }
        }

        public String getFcmSenderId() {
            return fcmSenderId;
        }

        public String getFcmApiKey() {
            return fcmApiKey;
        }

        public String getFcmServerKey() {
            return fcmServerKey;
        }

        public String getFcmProtocol() {
            return fcmProtocol;
        }

        @Override
        public String toString() {
            return "PushNotificationCreds{" +
                    "fcmSenderId='" + fcmSenderId + '\'' +
                    ", fcmApiKey='" + fcmApiKey + '\'' +
                    ", fcmServerKey='" + fcmServerKey + '\'' +
                    ", fcmProtocol='" + fcmProtocol + '\'' +
                    '}';
        }
    }

    public static class Urls {
        private String baseUrl;
        private String socketUrl;

        public Urls(JSONObject urlObj) {
            try {
                baseUrl = urlObj.optString("base_url");
                socketUrl = urlObj.optString("socket_url");
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
                baseUrl = "";
                socketUrl = "";
            }
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getSocketUrl() {
            return socketUrl;
        }

        @Override
        public String toString() {
            return "Urls{" +
                    "baseUrl='" + baseUrl + '\'' +
                    ", socketUrl='" + socketUrl + '\'' +
                    '}';
        }
    }

    public static class PaymentCreds {
        private boolean isStripeAvailable;
        private String stripePubKey;
        private String stripePrivKey;
        private boolean isPaypalAvailable;
        private String paypalId;
        private String paypalSecret;
        private String paypalMode;

        public PaymentCreds(JSONObject paymentData) {
            //paypal
            try{
                isPaypalAvailable = paymentData.optInt("is_paypal") == 1;
                paypalId = paymentData.optString("PAYPAL_ID");
                paypalSecret = paymentData.optString("PAYPAL_SECRET");
                paypalMode = paymentData.optString("PAYPAL_MODE");

                //stripe
                isStripeAvailable = paymentData.optInt("is_stripe") == 1;
                stripePubKey = paymentData.optString("stripe_publishable_key");
                stripePrivKey = paymentData.optString("stripe_secret_key");
            }catch (Exception e){
                UiUtils.log(TAG, Log.getStackTraceString(e));
                isPaypalAvailable = false;
                paypalId = "";
                paypalSecret = "";
                paypalMode = "";
                //stripe
                isStripeAvailable = false;
                stripePubKey = "";
                stripePrivKey = "";
            }
        }

        public boolean isStripeAvailable() {
            return isStripeAvailable;
        }

        @Override
        public String toString() {
            return "PaymentCreds{" +
                    "isStripeAvailable=" + isStripeAvailable +
                    ", stripePubKey='" + stripePubKey + '\'' +
                    ", stripePrivKey='" + stripePrivKey + '\'' +
                    ", isPaypalAvailable=" + isPaypalAvailable +
                    ", paypalId='" + paypalId + '\'' +
                    ", paypalSecret='" + paypalSecret + '\'' +
                    ", paypalMode='" + paypalMode + '\'' +
                    '}';
        }

        public String getStripePubKey() {
            return stripePubKey;
        }

        public String getStripePrivKey() {
            return stripePrivKey;
        }

        public boolean isPaypalAvailable() {
            return isPaypalAvailable;
        }

        public String getPaypalId() {
            return paypalId;
        }

        public String getPaypalSecret() {
            return paypalSecret;
        }

        public String getPaypalMode() {
            return paypalMode;
        }
    }

}