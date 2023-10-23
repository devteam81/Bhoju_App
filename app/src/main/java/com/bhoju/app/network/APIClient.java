package com.bhoju.app.network;

import android.os.StrictMode;
import android.util.Log;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.network.events.APIEvent;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.UnsafeOkHttpClient;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.nio.charset.Charset;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.tls.HandshakeCertificates;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.bhoju.app.network.APIConstants.*;


public class APIClient {

    private static final String TAG = APIClient.class.getSimpleName();

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        /*if(BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //setStrictMode();
        }*/

        HandshakeCertificates certificates = UnsafeOkHttpClient.getUnsafeOkHttpClient("");

        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    //.addInterceptor(new AddCookiesInterceptor())
                    //.sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager())
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        ResponseBody responseBody = response.body();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE); // Buffer the entire body.
                        Buffer buffer = source.buffer();
                        String respData = buffer.clone().readString(Charset.defaultCharset());
                        JSONObject resp = null;
                        try {
                            resp = new JSONObject(respData);
                        } catch (Exception e) {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                        if (resp != null) {
                            switch (resp.optInt(Params.ERROR_CODE)) {
                                case ErrorCodes.TOKEN_EXPIRED:
                                    emitEvent(ErrorCodes.TOKEN_EXPIRED, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.SUB_PROFILE_DOESNT_EXIST:
                                    emitEvent(ErrorCodes.SUB_PROFILE_DOESNT_EXIST, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.USER_DOESNT_EXIST:
                                    emitEvent(ErrorCodes.USER_DOESNT_EXIST, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.USER_RECORD_DELETED_CONTACT_ADMIN:
                                    emitEvent(ErrorCodes.USER_RECORD_DELETED_CONTACT_ADMIN, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.INVALID_TOKEN:
                                    emitEvent(ErrorCodes.INVALID_TOKEN, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.USER_LOGIN_DECLINED:
                                    emitEvent(ErrorCodes.USER_LOGIN_DECLINED, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.EMAIL_NOT_ACTIVATED:
                                    emitEvent(ErrorCodes.EMAIL_NOT_ACTIVATED, resp.optString(Params.ERROR_MSG));
                                    break;
                            }
                        }
                        return response;
                    }).build();
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }


        return new Retrofit.Builder()
                .baseUrl(URLs.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
    }

    //For Cookies and Videos
    public static Retrofit getCookieClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        HandshakeCertificates certificates = UnsafeOkHttpClient.getUnsafeOkHttpClient("media");

        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(new AddCookiesInterceptor())
                    .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager())
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        ResponseBody responseBody = response.body();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE); // Buffer the entire body.
                        Buffer buffer = source.buffer();
                        String respData = buffer.clone().readString(Charset.defaultCharset());
                        JSONObject resp = null;
                        try {
                            resp = new JSONObject(respData);
                        } catch (Exception e) {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                        if (resp != null) {
                            switch (resp.optInt(Params.ERROR_CODE)) {
                                case ErrorCodes.TOKEN_EXPIRED:
                                    emitEvent(ErrorCodes.TOKEN_EXPIRED, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.SUB_PROFILE_DOESNT_EXIST:
                                    emitEvent(ErrorCodes.SUB_PROFILE_DOESNT_EXIST, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.USER_DOESNT_EXIST:
                                    emitEvent(ErrorCodes.USER_DOESNT_EXIST, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.USER_RECORD_DELETED_CONTACT_ADMIN:
                                    emitEvent(ErrorCodes.USER_RECORD_DELETED_CONTACT_ADMIN, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.INVALID_TOKEN:
                                    emitEvent(ErrorCodes.INVALID_TOKEN, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.USER_LOGIN_DECLINED:
                                    emitEvent(ErrorCodes.USER_LOGIN_DECLINED, resp.optString(Params.ERROR_MSG));
                                    break;

                                case ErrorCodes.EMAIL_NOT_ACTIVATED:
                                    emitEvent(ErrorCodes.EMAIL_NOT_ACTIVATED, resp.optString(Params.ERROR_MSG));
                                    break;
                            }
                        }
                        return response;
                    }).build();
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }


        return new Retrofit.Builder()
                .baseUrl(URLs.COOKIES_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
    }


    private static void emitEvent(int code, String message) {
        EventBus.getDefault().post(new APIEvent(message, code));
    }

    private static void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
