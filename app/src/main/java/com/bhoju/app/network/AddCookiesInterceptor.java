package com.bhoju.app.network;

import com.bhoju.app.util.UiUtils;

import java.io.IOException;
import java.net.CookiePolicy;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.bhoju.app.ui.activity.MainActivity.COOKIEKEYPAIR;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEPOLICY;
import static com.bhoju.app.ui.activity.MainActivity.COOKIESIGNATURE;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        /*HashSet<String> preferences = (HashSet) Preferences.getDefaultPreferences().getStringSet(Preferences.PREF_COOKIES, new HashSet<>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            UiUtils.log("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }*/
        builder.addHeader("Cookie", COOKIEKEYPAIR);
        UiUtils.log("OkHttp", "Adding Header: " + COOKIEKEYPAIR);
        builder.addHeader("Cookie", COOKIEPOLICY);
        UiUtils.log("OkHttp", "Adding Header: " + COOKIEPOLICY);
        builder.addHeader("Cookie", COOKIESIGNATURE);
        UiUtils.log("OkHttp", "Adding Header: " + COOKIESIGNATURE);

        return chain.proceed(builder.build());
    }
}
