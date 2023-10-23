//
// Created by home on 11/22/2021.
//
#include <jni.h>

//PAYTM DETAILS
//TEST Details
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTestMIDKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "tWwvJq40191917862733");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTestChannelIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WAP");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTestWebsiteKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WEBSTAGING");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTestIndustryTypeIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "Retail");
}

//LIVE Details
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getLiveMIDKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BHOJUP23245076415065");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getLiveChannelIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "WAP");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getLiveWebsiteKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "DEFAULT");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getLiveIndustryTypeIdKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ECommerce");
}

//Paytm UPI Details
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PaytmUPIActivity_getPayeeNameKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "BHOJU");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PaytmUPIActivity_getPayeeVpaKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "paytm-66384265@paytm");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PaytmUPIActivity_getMerchantCodeKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "5815");
}

//Paytm TYPES
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTypePaytmKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "PAYTM");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTypeCardKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "CARD_NET_BANK");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTypeUpiKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "UPI");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTypeAgreePayKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "AGREEPAY");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_ui_activity_PlansActivity_getTypeGoogleInAppKeys(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "GOOGLE_IN_APP");
}
