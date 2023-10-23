package com.bhoju.app.util.sharedpref;

public class PrefKeys {

    static {
        System.loadLibrary("keys");
    }
    public static native String getUserIsLoggedInKeys();
    public static native String getTokenKeys();
    public static native String getLoginTokenKeys();
    public static native String getUserIdKeys();
    public static native String getUserEmailKeys();
    public static native String getUserNameKeys();
    public static native String getUserPictureKeys();
    public static native String getUserAboutKeys();
    public static native String getUserAgeKeys();
    public static native String getLoginTypeKeys();
    public static native String getIsPassKeys();
    public static native String getPassCodeKeys();
    public static native String getDefaultEmailKeys();
    public static native String getDefaultMobileKeys();
    public static native String getServerAppVersionKeys();
    public static native String getUpdateAppCompulsoryKeys();
    public static native String getPushNotiKeys();
    public static native String getEmailNotiKeys();
    public static native String getPaymentCurrencyKeys();
    public static native String getUserMobileKeys();
    public static native String getActiveSubProfileKeys();
    public static native String getDeleteVideoDownloadKeys();
    public static native String getCancelVideoDownloadKeys();
    public static native String getPauseVideoDownloadKeys();
    public static native String getFCMTokenKeys();
    public static native String getAppLanguageStringKeys();
    public static native String getAppLanguageKeys();
    public static native String getPIPModeOnMinimizeKeys();
    public static native String getFirstTimeLoginKeys();
    public static native String getTermsAndConditionKeys();

    public static native String getOrderIdKeys();
    public static native String getPlanIdKeys();
    public static native String getTempIdKeys();

    public static native String getVideoAgeVerifyKeys();
    public static native String getSubscriptionDaysKeys();
    public static native String getIpAddressKeys();

    public static native String getRefUrlKeys();
    public static native String getRefInstallKeys();

    ////
    public static final String IS_LOGGED_IN = getUserIsLoggedInKeys();
    public static final String SESSION_TOKEN = getTokenKeys();
    public static final String SESSION_NEW_TOKEN = getLoginTokenKeys();
    public static final String USER_ID = getUserIdKeys();
    public static final String USER_EMAIL = getUserEmailKeys();
    public static final String USER_NAME = getUserNameKeys();
    public static final String USER_PICTURE = getUserPictureKeys();
    public static final String USER_ABOUT = getUserAboutKeys();
    public static final String USER_AGE = getUserAgeKeys();
    public static final String LOGIN_TYPE = getLoginTypeKeys();
    public static final String IS_PASS = getIsPassKeys();
    public static final String PASS_CODE = getPassCodeKeys();
    public static final String DEFAULT_EMAIL = getDefaultEmailKeys();
    public static final String DEFAULT_MOBILE = getDefaultMobileKeys();
    public static final String SERVER_APP_VERSION = getServerAppVersionKeys();
    public static final String UPDATE_APP_COMPULSORY = getUpdateAppCompulsoryKeys();
    public static final String PUSH_NOTIFICATIONS = getPushNotiKeys();
    public static final String EMAIL_NOTIFICATIONS = getEmailNotiKeys();
    public static final String PAYMENT_CURRENCY = getPaymentCurrencyKeys();
    public static final String USER_MOBILE = getUserMobileKeys();
    public static final String ACTIVE_SUB_PROFILE = getActiveSubProfileKeys();
    public static final String DELETE_VIDEOS_DOWNLOAD = getDeleteVideoDownloadKeys();
    public static final String CANCEL_VIDEOS_DOWNLOAD = getCancelVideoDownloadKeys();
    public static final String PAUSE_VIDEOS_DOWNLOAD = getPauseVideoDownloadKeys();
    public static final String FCM_TOKEN = getFCMTokenKeys();
    public static final String APP_LANGUAGE_STRING = getAppLanguageStringKeys();
    public static final String APP_LANGUAGE = getAppLanguageKeys();
    public static final String PIP_ENTER_WHEN_MINIMIZED = getPIPModeOnMinimizeKeys();
    public static final String FIRST_TIME_LOGIN = getFirstTimeLoginKeys();
    public static final String TERMS_AND_CONDITION = getTermsAndConditionKeys();

    public static final String ORDER_ID = getOrderIdKeys();
    public static final String PLAN_ID = getPlanIdKeys();
    public static final String TEMP_ID = getTempIdKeys();

    public static final String VIDEO_AGE_VERIFICATION = getVideoAgeVerifyKeys();
    public static final String SUBSCRIPTION_DAYS = getSubscriptionDaysKeys();
    public static final String IP_ADDRESS = getIpAddressKeys();

    public static final String REFERENCE_URL_CODE = getRefUrlKeys();
    public static final String REFERENCE_INSTALL = getRefInstallKeys();

    private PrefKeys() {

    }
}