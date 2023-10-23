//
// Created by home on 11/22/2021.
//
#include <jni.h>

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTokenField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLoginTokenField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login_token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLoginField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "name");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getEmailField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "email");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getContactField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "contact");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPasswordField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "password");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLoginByField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login_by");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getFCMTokenField(JNIEnv *env, jobject instance) {
return (*env)->  NewStringUTF(env, "fcm_token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeviceTokenField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "device_token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeviceTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "device_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMessageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "message");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getErrorMessageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "error_messages");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDefaultEmailField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "default_email");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDefaultMobileField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "default_no");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "userName");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getResponseField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "response");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSuccessField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "success");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getFailedField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "FAILED");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPictureField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "picture");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDescriptionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "description");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubjectField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subject");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHelpTypesField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "help_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAppDetailsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "app_details");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAppVersionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "AppV");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeviceField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "DEVICE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPlatformField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "platform");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPlatformOsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "PlatformOsVersion");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getExtField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BebuExt1");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBrandField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BRAND");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getModelField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "MODEL");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVersionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "VERSION");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPlatField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "Plat");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getManufacturerField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "MANUFACTURER");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBoardField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BOARD");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBootLoaderField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BOOTLOADER");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCpuAbiField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CPU_ABI");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCpuAbi2Field(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CPU_ABI2");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDisplayField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "DISPLAY");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getFingerprintField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "FINGERPRINT");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHardwareField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "HARDWARE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHostField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "HOST");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeviceIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSerialField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "SERIAL");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getProductField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "PRODUCT");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOsVersionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "OS_VERSION");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVersionCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "VERSIONCode");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getScreenSizeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "SCREEN_SIZE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getScreenDpiField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "SCREEN_DPI");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRamField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "RAM");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeviceLanguageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "DEVICE_LANGUAGE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOpenGlVersionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "OPEN_GL_VERSION");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOldPasswordField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "old_password");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPasswordConfirmationField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "password_confirmation");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSocialIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "social_unique_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationSoundField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "sound");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationVersionCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "version_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNotificationVersionNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "version_name");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPushStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "push_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getEmailStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "email_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardTokenField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "card_token");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "card_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymentModeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_modes");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cards");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIsDefaultField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_default");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPageTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "page_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDataField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "data");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHistoryField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "history");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getErrorCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "error_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLastFourField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "last_four");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "card_name");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMobileField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "mobile");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMobileNumberField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "mobileNumber");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOTPField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "otpText");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubProfileIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "sub_profile_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSkipField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "skip");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWishlistField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wishlist");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "admin_video_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "notification_download_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadUrlsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "url");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadPathField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "path");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadFileNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "fileName");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTitleField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "title");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getEpisodeTitleField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ep_title");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDefaultImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "default_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBrowseImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "browse_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubscriptionIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscription_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPlanField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "plan");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPlanFormatField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "plan_formatted");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCreatedAtField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "created_at");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCurrencyField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "currency");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCheckCountryField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "chk_country");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSymbolField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "symbol");
}
JNIEXPORT jstring  JNICALL Java_com_bhoju_app_network_APIConstants_getProductIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "product_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTotalAccountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "no_of_account");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPopularStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "popular_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymentIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCouponAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "coupon_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCouponCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "coupon_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCouponField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "coupon");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getActivePlanField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "active_plan");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCancelledStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cancelled_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymenStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getExpiryDateField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "expiry_date");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymenModeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_mode");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTotalAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "total_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPPVIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pay_per_view_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaidDateField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paid_date");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubscriptionTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "type_of_subscription");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTypeUserField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "type_of_user");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUserTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "user_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCategoriesField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "categories");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTimeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "time");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTimezoneField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "timezone");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getImgField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "img");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubProfileUserIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPositionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "position");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardNumberField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "number");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardMonthField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "month");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardYearField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "year");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCardVVField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cvv");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCategoryIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "category_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubCategoryIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "sub_category_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGenreIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "genre_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeasonIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "season_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeasonNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "season_name");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParentIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "parent_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideosIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBannerField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "banner");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOriginalsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "originals");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMobileImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "mobile_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeeAllField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "see_all_url");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getErrorMsgField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "error_message");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAdminIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "admin_unique_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPublishTimeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "publish_time");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubscriptionCountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "plan");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "age");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "url");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRegionNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "regionName");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoLanguageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_language");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAudioLanguageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "audio_language");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoSubtitleField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_subtitle");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoSubtitleLangField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_subtitle_vtt");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoDurationField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "duration");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoMainField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "main");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoTrailerField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoTrailerLanguageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_video_language");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAudioTrailerLanguageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_audio_language");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoTrailerSubtitleField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_subtitle");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoTrailerSubtitleLangField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_subtitle_vtt");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoRatingField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ratings");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoARecordField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "a_record");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSliderTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "slider_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParentMediaField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "parent_media");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoWatchCountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "watch_count");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoIsSingleField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_single");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoIsMainField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_main");
}
JNIEXPORT jstring JNICALL   Java_com_bhoju_app_network_APIConstants_getVideoIsTrailerFreeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_trailer_free");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTrailerVideoField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTrailerDefaultImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_default_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTrailerBrowseImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_browse_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTrailerMobileImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_mobile_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoIsPPVField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_pay_per_view");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRatingTimeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "rating_time");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRatingTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "media_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRatingUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "rating_url");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRatingTrailerTimeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_rating_time");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRatingTrailerTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_media_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRatingTrailerUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_rating_url");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoIsKidsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_kids_video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "download_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDetailsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "details");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWishlistStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wishlist_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHistoryStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "history_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIsLikedField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_liked");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLikesField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "likes");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCastCrewField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cast_crews");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCastCrewsIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cast_crew_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDirectorField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "director");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGenerField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "genres");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeasonField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "seasons");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGenerMainField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "genres_main");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSearchField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "key");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLikeCountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "like_count");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDislikeCountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "dislike_count");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareLinkField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_link");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHistoryidField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "history_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWishListIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wishlist_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getValueField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "value");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReasonField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "reason");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAmountRemainingField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "remaining_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCategoryField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CATEGORY");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubCategoryField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "SUB_CATEGORY");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGenersField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "genres");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoGeneresField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "genre_videos");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIsSelectedField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_selected");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIsSeriresField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_series");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTrailerSectionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "trailer_section");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadResolutionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "download_video_resolutions");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getResolutionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "resolutions");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMainVideoResolutionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "main_video_resolutions");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOriginalField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "original");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUrlTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "url_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUrlPageIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "url_page_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGenreNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "genre_name");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadButtonStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "download_button_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadingViewStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "downloading_video_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadedViewStatusField(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "video_save_status");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "download_urls");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLinkField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "link");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPPVAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ppv_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPPVPageTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ppv_page_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPPVPageTypeContentField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ppv_page_type_content");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSeekTimeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "seek_time_in_seconds");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDeleteSubProfileField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "delete_sub_profile_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getClearStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "clear_all_status");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "count");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIsSpamField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_spam");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBannerImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "banner_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPPVDisplayField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "should_display_ppv");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPrivacyField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "privacy");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRefundField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "refund");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getHelpField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "help");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTermsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "terms");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAboutField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "about");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPageTypeIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "page_type_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getUrlTypeIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "url_type_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralEarningsField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "referral_earnings");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "referral_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareMsgField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "share_message");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAmountTotalField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "amount_total");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getRemainingField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "remaining");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "referral_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPayAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pay_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferralAmountFormatField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "referral_amount_formatted");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPayAmountFormatField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pay_amount_formatted");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWalletAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "wallet_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getListedPriceField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "listedPrice");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSubscriptionAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscription_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWalletAppliedField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "is_wallet_credits_applied");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLanguageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "language");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getNextVideoIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "next_admin_video_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "video");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoResolutionField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "resolution");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMobileBannerImgField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "mobile_banner_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPlayerJsonField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "player_json");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTotalDaysField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "no_of_days");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadedExpiryField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "download_expiry");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransitionImageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "transition_image");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransitionTitleField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "transition_title");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDiscountField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "discount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDaysField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "days");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPercentageField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "percentage");
}


//Paytm
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmRespField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paymentResponse");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOrderIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "order_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCustomerIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cust_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIndustryIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "industry_type_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getWebsiteField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WEBSITE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getChannelIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "channel_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransactionAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "txn_amount");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCallbackUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "callback_url");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmCallbackUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CALLBACK_URL");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmStatusField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "STATUS");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransactionIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "transaction_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getChecksumField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CHECKSUMHASH");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransactionToken(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "TXNTOKEN");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBankNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BANKNAME");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getOrderIDField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ORDERID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransactionAmtField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "TXNAMOUNT");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTransactionDateField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "TXNDATE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getMerchantIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "MID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmTransactionIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "TXNID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getResponseCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "RESPCODE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaytmPaymentModeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "PAYMENTMODE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getBankTransactionIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BANKTXNID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGatewayNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "GATEWAYNAME");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getResponseMessageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "RESPMSG");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTestCallbackUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getLiveCallbackUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParamOrderIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ORDER_ID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParamCustomerIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CUST_ID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParamIndustryIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "INDUSTRY_TYPE_ID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParamChannelIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CHANNEL_ID");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParamTransactionAmtField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "TXN_AMOUNT");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getParamCallbackUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CALLBACK_URL");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymentGatewayName(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_type");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPaymentGatewayType(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_type_key");
}

//Agreepay
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayAPIKeyField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "314d4cab-37b6-4f7d-9ae0-aecd5beb85ec");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayHostNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "https://biz.aggrepaypayments.com");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayReturnUrlField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "https://adminpanelconnect.bhoju.com/userApi/aggreepayCallbackResponse");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayModeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "LIVE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayCurrencyField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "INR");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayCountryField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "IND");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayResponseMessageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "response_message");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayErrorMessageField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "error_desc");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayOrderIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "order_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayTransactionIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "transaction_id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayPaymentMethodField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_method");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayPaymentModeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "payment_mode");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAgreePayAmountField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "amount");
}

//Google Play
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayOrderIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "orderId");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayPackageNameField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "packageName");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayProductIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "productId");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayPurchaseTimeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "purchaseTime");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayPurchaseStateField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "purchaseState");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayPurchaseTokenField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "purchaseToken");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayQuantityField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "quantity");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayAutoRenewingField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "autoRenewing");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGooglePlayAcknowledgedField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "acknowledged");
}

//Cookies
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getKeyPairField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CloudFront-Key-Pair-Id");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPolicyField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CloudFront-Policy");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSignatureField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CloudFront-Signature");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getIPField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ip");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDistributorField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "app_ref");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getReferrerField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "link_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getInstallField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "install_token");
}

//Share
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareUrlCodeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "link_code");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareUrlTypeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "type");//TYPE-> subscribe, login, install
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareUrlUserIdField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "user_id");
}

//TYPE
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareTypeSubscribeField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "subscribe");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareTypeLoginField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "login");//TYPE-> subscribe, login, install
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getShareTypeInstallField(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "install");
}
JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getNotify(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "type");
}
JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getIsNotify(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "isNotify");
}
JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAdminVideoId(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "adminVideoId");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getenquiryField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "enquiry");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getapp_1versionField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "app_version");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getUserEmailField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "user_email");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getUserPhoneField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "user_phone");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getUserNameEnquiryField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "user_name");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAppProviderField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "auth_provider");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAppRating(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "rating");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getAppReview(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "review");
}

JNIEXPORT jstring JNICALL
Java_com_bhoju_app_network_APIConstants_getVersionCodeApkField(JNIEnv *env, jclass clazz) {
    return (*env)->  NewStringUTF(env, "version_code");
}