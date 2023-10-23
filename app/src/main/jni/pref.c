//
// Created by home on 11/22/2021.
//
#include <jni.h>
//
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserIsLoggedInKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "isLoggedIn");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getTokenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getLoginTokenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login_token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserEmailKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "email");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserNameKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "userName");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserPictureKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "picture");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserAboutKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "about");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserAgeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "age");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getLoginTypeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "loginType");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getIsPassKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_pass");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getPassCodeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pass_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getDefaultEmailKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "default_email");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getDefaultMobileKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "default_no");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getServerAppVersionKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "version");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUpdateAppCompulsoryKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_compulsory");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getPushNotiKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "push_notifications");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getEmailNotiKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "email_notifications");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getPaymentCurrencyKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_currency");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getUserMobileKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "mobile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getActiveSubProfileKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "activeSubProfile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getDeleteVideoDownloadKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "deleteVideosDownload");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getCancelVideoDownloadKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cancelVideosDownload");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getPauseVideoDownloadKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pauseVideosDownload");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getFCMTokenKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "fcm_token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getAppLanguageStringKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "applanguagestr");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getAppLanguageKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "applanguage");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getPIPModeOnMinimizeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pipmodeOnMinimize");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getFirstTimeLoginKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "first_time_login");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getTermsAndConditionKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "terms_and_condition");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getOrderIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "order_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getPlanIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "plan_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getTempIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "temp_id");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getVideoAgeVerifyKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_age_verification");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getSubscriptionDaysKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscription_days");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getIpAddressKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ip");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getRefUrlKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_url_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_util_sharedpref_PrefKeys_getRefInstallKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_url_install");
}