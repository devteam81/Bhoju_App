//
// Created by home on 11/22/2021.
//
#include <jni.h>

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserApi(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "userApi/");
}

//API KEYS
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLoginUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRegisterUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "register");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getForgotPasswordUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "forgotpassword");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getChangePasswordUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "changePassword");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getResetPasswordUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "reset_password");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeleteAccountUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "deleteAccount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVerifyOtpUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "otp/validate");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLogoutUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "token/logout");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getProfileUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "profile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUpdateProfileUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "updateProfile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationSettingUpdateUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "notification/settings");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAddCardUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_card_add");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymentListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cards_list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeleteCardUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "delete_card");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDefaultCardUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "default_card");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAppConfigUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "project/configurations");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getStaticPagesUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pages/list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWishlistUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wishlists");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSpamListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "spam_videos/list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHistoryListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "history/list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubscriptionPlansUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscription_plans");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubscribedPlansUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscribedPlans");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaidVideosUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ppv_list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCategoriesUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "v4/categories/list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationsUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "notifications");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubProfilesUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "active-profiles");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAddSubProfileUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "add-profile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getEditSubProfileUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "edit-sub-profile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubProfileDeleteUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "sub_profiles/delete");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardDetailsUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "card_details");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHomeSliderUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "home_sliders");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHomeFirstSectionUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "home_first_section");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAllCookiesUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "testVideo");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSingleVideoDataUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "videos/view");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPayPalPaymentUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pay_now");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAddToWishListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wishlists/operations");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getClearWishListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wishlists/operations");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAddToHistoryUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "addHistory");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getClearHistoryUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "deleteHistory");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLikeVideoUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "like_video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUnlikeVideoUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "dis_like_video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSearchVideoUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "search_videos");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAddSpamUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "spam_videos/add");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRemoveSpamUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "spam_videos/remove");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSpamReasonUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "spam-reasons");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getStripePaymentUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "stripe_payment");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCouponCodeUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "apply/coupon/subscription");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPVVCodeUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "apply/coupon/ppv");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getStripePVVUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "stripe_ppv");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaypalPVVUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paypal_ppv");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmPaymentUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paytm_payment");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVerifyPaytmPaymentUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "verify_payment");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayPaymentUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "agreepay_payment");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayPaymentUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "playstore_payment");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCancelSubscriptionUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cancel/subscription");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAutorenewalUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "autorenewal/enable");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getClearSpamListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "remove_spam");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCastVideosUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cast_crews/videos");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSingleVideoDataSecondUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "videos/view/second");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeasonEpisodeUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "seasons_and_episodes");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeeAllUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "see_all");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPPVEndUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ppv_end");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHomeSecondUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "home_second_section");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadedVideosUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "downloaded/videos");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadStatusUpdateUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video/download");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationCountUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "notification/count");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPageListUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pages/list");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSuggestionUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "suggestions");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getContinueWatchingVideoUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "continue_watching_videos/save");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getContinueWatchingVideoEndUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "oncomplete/video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralCodeUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "referral_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralCodeValidateUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "referral_code_validate");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralCodeAmountUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "invoice_referral_amount");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUpdateAgeUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "userAge");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSupportDataUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "help_queries");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAddSupportUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "createTickets");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmChecksumUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "generatePaytmCheck");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserSubscriptionUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "userSubscription");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymentGatewayUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paymentGatewayCountryWise?ip=");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "getDownloadUrls");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUpdateDownloadExpiryUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "updateDownloadExpiry");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPublicIpUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "http://api.ipify.org/?format=json");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPublicIp64Url(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "http://api64.ipify.org/?format=json");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserStateUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "getUsersState");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getServerAppVersionUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "getAppVersion");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getFCMTokenUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "updateFCMToken");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_sendFCMTokenRefUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "app_installation");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserLoggedInOtherDeviceUrl(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "checkUserLoggedIn?ip=ipaddress&id=userId&token=accesstoken");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_sendShareUrlDetails(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "incrementURLHit");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getUpComingVideo(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "upcoming_videos");
}
JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getComingSoonNotification(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "create_master_notification");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAddSupportequryUrl(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "create_enquiry");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAppRatingUrl(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "app_rating_and_review");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAppRatingUpdateUrl(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "user_already_reviewed");
}