package com.bhoju.app.util.sharedpref;

import android.content.Context;


public class PrefHelper {

    public static void setUserLoggedIn(Context context, int id, String token, String loginToken,
                                       String loginType, String email, String name, String mobile,
                                       String about, String picture, String age, String pushNotifStatus,
                                       String emailNotifStatus, String defaultEmail, String defaultMobile) {
        PrefUtils preferences = PrefUtils.getInstance(context);
        preferences.setValue(PrefKeys.IS_LOGGED_IN, true);
        preferences.setValue(PrefKeys.USER_ID, id);
        preferences.setValue(PrefKeys.SESSION_TOKEN, token);
        preferences.setValue(PrefKeys.SESSION_NEW_TOKEN, loginToken);
        preferences.setValue(PrefKeys.LOGIN_TYPE, loginType);
        preferences.setValue(PrefKeys.USER_EMAIL, email);
        preferences.setValue(PrefKeys.USER_NAME, name);
        preferences.setValue(PrefKeys.USER_MOBILE, mobile);
        preferences.setValue(PrefKeys.USER_ABOUT, about);
        preferences.setValue(PrefKeys.USER_PICTURE, picture);
        preferences.setValue(PrefKeys.USER_AGE, age);
        preferences.setValue(PrefKeys.ACTIVE_SUB_PROFILE, 0);
        preferences.setValue(PrefKeys.PUSH_NOTIFICATIONS, pushNotifStatus.equals("1"));
        preferences.setValue(PrefKeys.EMAIL_NOTIFICATIONS, emailNotifStatus.equals("1"));
        preferences.setValue(PrefKeys.TERMS_AND_CONDITION, false);
        preferences.removeKey(PrefKeys.IS_PASS);
        preferences.removeKey(PrefKeys.PASS_CODE);
        preferences.setValue(PrefKeys.DEFAULT_EMAIL, defaultEmail);
        preferences.setValue(PrefKeys.DEFAULT_MOBILE, defaultMobile);
    }

    public static void setUserLoggedOut(Context context) {
        PrefUtils preferences = PrefUtils.getInstance(context);
        preferences.removeKey(PrefKeys.IS_LOGGED_IN);
        preferences.removeKey(PrefKeys.USER_ID);
        preferences.removeKey(PrefKeys.SESSION_TOKEN);
        preferences.removeKey(PrefKeys.SESSION_NEW_TOKEN);
        preferences.removeKey(PrefKeys.LOGIN_TYPE);
        preferences.removeKey(PrefKeys.USER_EMAIL);
        preferences.removeKey(PrefKeys.USER_NAME);
        preferences.removeKey(PrefKeys.USER_MOBILE);
        preferences.removeKey(PrefKeys.USER_ABOUT);
        preferences.removeKey(PrefKeys.USER_PICTURE);
        preferences.removeKey(PrefKeys.USER_AGE);
        preferences.removeKey(PrefKeys.ACTIVE_SUB_PROFILE);
        preferences.removeKey(PrefKeys.PUSH_NOTIFICATIONS);
        preferences.removeKey(PrefKeys.EMAIL_NOTIFICATIONS);
        preferences.removeKey(PrefKeys.TERMS_AND_CONDITION);
        preferences.removeKey(PrefKeys.IS_PASS);
        preferences.removeKey(PrefKeys.PASS_CODE);
        preferences.removeKey(PrefKeys.DEFAULT_EMAIL);
        preferences.removeKey(PrefKeys.DEFAULT_MOBILE);
    }

    public static void setOrderDetailForConfirmation(Context context, int userId, String orderId, int planId)
    {
        PrefUtils preferences = PrefUtils.getInstance(context);
        preferences.setValue(PrefKeys.TEMP_ID, userId);
        preferences.setValue(PrefKeys.ORDER_ID, orderId);
        preferences.setValue(PrefKeys.PLAN_ID, planId);
    }
}
