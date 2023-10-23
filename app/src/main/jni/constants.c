//
// Created by home on 11/23/2021.
//
#include <jni.h>

//API KEYS
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getManualLogin(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "manual");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getGoogleLogin(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "google");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getFacebookLogin(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "facebook");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getAndroid(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "android");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getSuccess(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "success");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getError(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "error");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getTrue(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "true");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getFalse(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "false");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCountryCode(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "US");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCurrencyCode(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "USD");
}

JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getPageType(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "pageType");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getCancelledOrCompleted(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "cancelledOrCompleted");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadStatus(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "downloadStatus");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadProgress(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "downloadProgress");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getDownloadUpdate(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "ACTION_DOWNLOAD_UPDATE");
}
JNIEXPORT jstring JNICALL Java_com_bhoju_app_network_APIConstants_getVideoElapsed(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "videoElapsed");
}