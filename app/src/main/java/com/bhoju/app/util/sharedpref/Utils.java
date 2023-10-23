package com.bhoju.app.util.sharedpref;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AlertDialog;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.gcm.MessagingServices;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.ui.activity.SplashActivity;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WIFI_SERVICE;
import static com.bhoju.app.network.APIConstants.APIs.GET_FCM_TOKEN;
import static com.bhoju.app.network.APIConstants.APIs.GET_PUBLIC_IP;
import static com.bhoju.app.network.APIConstants.APIs.GET_PUBLIC_IP_64;
import static com.bhoju.app.network.APIConstants.APIs.GET_USER_LOGGED_STATUS;
import static com.bhoju.app.network.APIConstants.APIs.GET_USER_STATE;
import static com.bhoju.app.network.APIConstants.APIs.SEND_FCM_TOKEN_REF;
import static com.bhoju.app.network.APIConstants.APIs.SEND_SHARE_URL_DETAILS;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.BEBUEXT;
import static com.bhoju.app.network.APIConstants.Params.BOARD;
import static com.bhoju.app.network.APIConstants.Params.BOOTLOADER;
import static com.bhoju.app.network.APIConstants.Params.BRAND;
import static com.bhoju.app.network.APIConstants.Params.CPU_ABI;
import static com.bhoju.app.network.APIConstants.Params.CPU_ABI2;
import static com.bhoju.app.network.APIConstants.Params.DEVICE;
import static com.bhoju.app.network.APIConstants.Params.DEVICE_ID;
import static com.bhoju.app.network.APIConstants.Params.DEVICE_LANGUAGE;
import static com.bhoju.app.network.APIConstants.Params.DISPLAY;
import static com.bhoju.app.network.APIConstants.Params.DISTRIBUTOR;
import static com.bhoju.app.network.APIConstants.Params.FINGERPRINT;
import static com.bhoju.app.network.APIConstants.Params.HARDWARE;
import static com.bhoju.app.network.APIConstants.Params.HOST;
import static com.bhoju.app.network.APIConstants.Params.LOGIN;
import static com.bhoju.app.network.APIConstants.Params.MANUFACTURER;
import static com.bhoju.app.network.APIConstants.Params.MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.MODEL;
import static com.bhoju.app.network.APIConstants.Params.OPEN_GL_VERSION;
import static com.bhoju.app.network.APIConstants.Params.OS_VERSION;
import static com.bhoju.app.network.APIConstants.Params.PLAT;
import static com.bhoju.app.network.APIConstants.Params.PRODUCT;
import static com.bhoju.app.network.APIConstants.Params.RAM;
import static com.bhoju.app.network.APIConstants.Params.SCREEN_DPI;
import static com.bhoju.app.network.APIConstants.Params.SCREEN_SIZE;
import static com.bhoju.app.network.APIConstants.Params.SERIAL;
import static com.bhoju.app.network.APIConstants.Params.VERSION;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_COUNTRY;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_REGION;
import static com.bhoju.app.ui.activity.MainActivity.STATES_PAYMENT;
import static com.bhoju.app.util.UiUtils.log;
import static com.bhoju.app.util.sharedpref.PrefKeys.IP_ADDRESS;
import static com.bhoju.app.util.sharedpref.PrefKeys.REFERENCE_INSTALL;

/**
 * Created by cdi-user on 13/4/17.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    //don't touch this line Important
    public static final String TYPE_LOADMORE = "#TYPE_LOADMORE#";


    public static final String SELECTED_ORDER_REVIEWS = "SELECTED_ORDER_REVIEWS";
    public static final String regExforNames = "^[\\p{L} .'-]+$";
    public static final String regExforAlphaNum = "^[a-zA-Z0-9äöüÄÖÜ]*$";

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatViews(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatViews(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatViews(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static void showConfirmationDialog(Context activity, String title, String msg, String yesBtnTxt, String noBtnTxt, final ConfirmationDialogActions action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = yesBtnTxt;
        builder.setPositiveButton(positiveText,
                (dialog, which) -> action.onPositiveAction());

        String negativeText = noBtnTxt;
        builder.setNegativeButton(negativeText,
                (dialog, which) -> action.onNegativeAction());

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void writeStringToPreferences(String key, String value, Context activity) {
        if (activity == null) {
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.apply();

    }

    public static String getStringFromPreferences(String key, String defaultValue, Context activity) {
        if (activity == null) {
            return defaultValue;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPreferences.getString(key, defaultValue);
    }


    public static String prefixZero(int x) {
        String result;
        if (x < 10)
            result = "0" + x;
        else
            result = String.valueOf(x);

        return result;
    }

    public static String getParsedTime(String serverTime) {
        String time = serverTime;


        int colIndex = time.indexOf(":");
        int hourInt = Integer.parseInt(time.substring(0, colIndex));
        String min = time.substring(colIndex + 1, colIndex + 3);


        String timeSet;
        if (hourInt > 12) {
            hourInt -= 12;
            timeSet = "PM";
        } else if (hourInt == 0) {
            hourInt += 12;
            timeSet = "AM";
        } else if (hourInt == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = prefixZero(Integer.parseInt(min));
        String hoursString = prefixZero(hourInt);
        // Append in a StringBuilder

        return new StringBuilder().append(hoursString).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
    }

    @SuppressLint("NewApi")
    public static String getPathAll(final Context context, final Uri uri) {

        final boolean isKitKat = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }


            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static byte[] fileToBytes(File input) {
        FileInputStream objFileIS;
        try {
            objFileIS = new FileInputStream(input);

            ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
            byte[] byteBufferString = new byte[1024];

            for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1; ) {
                objByteArrayOS.write(byteBufferString, 0, readNum);
            }

            return objByteArrayOS.toByteArray();

        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            return null;
        }

    }

    public static Bitmap decodeBitmap(String string) {
        byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }

    public static String formatDate(String data, String toformat) {
        //2014-10-22T12:25:32
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        //tell the app the server is sending in UTC
        //DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat(toformat, Locale.ENGLISH);
        // OUTPUT_FORMAT.setTimeZone(TimeZone.getDefault());

        try {
            return OUTPUT_FORMAT.format(DATE_FORMAT.parse(data));
        } catch (Exception ex) {
            ex.printStackTrace();
            return data;
        }


    }

    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:

        if (activity == null)
            return;


        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static boolean isMyServiceRunning(Class<?> serviceClass, Context activity) {
        if (activity == null) {
            return true;
        }
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static RoundedBitmapDrawable roundedCornerFactory(Resources res, int drawable, int size) {

        Bitmap src = BitmapFactory.decodeResource(res, drawable);

        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(res, src);

        //  dr.setCircular(true);

        dr.setCornerRadius(size);
        //Math.max(src.getWidth(), src.getHeight()) / 0.002f
        // imageViewNew.setImageDrawable(dr);
        return dr;
    }

    public static void sendLoginSuccessBroadcast(Context context) {
//        /EventBus.getDefault().postSticky(new LoginSuccessEvent("LOGGED IN !"));
    }


    public interface ConfirmationDialogActions {
        void onPositiveAction();

        void onNegativeAction();
    }


    public static boolean isEmailType(String text) {
        Pattern pattern = Pattern.compile("^([a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,10})$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isPhoneType(String text) {
        Pattern pattern = Pattern.compile("^([6789]\\d{9})$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isValidFullname(String text) {
        text = text.trim();
        Pattern pattern = Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /*public static SpannableString getspannableStringForError(Context context,String txt){

        //implementation 'io.github.inflationx:calligraphy3:3.1.1'
        SpannableString spannableString = new SpannableString(txt);
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/Nunito-Regular.ttf"));
        spannableString.setSpan(typefaceSpan, 0, txt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }*/


    public static long getTotalInternalMemorySizeinMB() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();

        try {
            long internalMemSizeinMB = (totalBlocks * blockSize) / 1000000;
            return internalMemSizeinMB;
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return 0;
    }


    public void latencyTest(String ip, String ipInfoUrl, String pushUrl, String count, Context activity) {
        new AsyncTaskRunner(ip, ipInfoUrl, pushUrl, count, activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    private static class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String ip;
        String count;
        String ipInfoUrl;
        String pushUrl;

        Context activity;

        public AsyncTaskRunner(String ip, String ipInfoUrl, String pushUrl, String count, Context activity) {
            this.ip = ip;
            this.count = count;
            this.activity = activity;
            this.ipInfoUrl = ipInfoUrl;
            this.pushUrl = pushUrl;
        }


        @Override
        protected String doInBackground(String... params) {
            JSONObject bodyJson = new JSONObject();

            try {
                String latencyData = getLatency(ip, count);

                //Fallback to this
                if (ipInfoUrl == null || ipInfoUrl.isEmpty()) {
                    ipInfoUrl = "https://ipapi.co/json";
                }

                final OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(ipInfoUrl)
                        .build();
                String body = client.newCall(request).execute().body().string();
                bodyJson = new JSONObject(body);
                bodyJson.put("latencyData", latencyData);

            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
            return bodyJson.toString();
        }


        @Override
        protected void onPostExecute(String result) {

        }

    }

    public static String getLatency(String ipAddress, String NUMBER_OF_PACKTETS) {
        String pingCommand = "/system/bin/ping -c " + NUMBER_OF_PACKTETS + " " + ipAddress;
        String inputLine = "";
        double avgRtt = 0;

        try {
            // execute the command on the environment interface
            Process process = Runtime.getRuntime().exec(pingCommand);
            // gets the input stream to get the output of the executed command
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {  // when we get to the last line of executed ping command
                    break;
                }
                inputLine = bufferedReader.readLine();
            }


            // Extracting the average round trip time from the inputLine string
            String afterEqual = inputLine.substring(inputLine.indexOf("=")).trim();
            String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1).trim();
            String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
            avgRtt = Double.parseDouble(strAvgRtt);
        } catch (Exception e) {
            log("LatencyTest", "getLatency: EXCEPTION");
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return inputLine;
    }


    public static String getNetworkType(Context context) {
        /*try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                if (connManager.isActiveNetworkMetered()) {
                    return "mwifi";
                } else {
                    return "wifi";
                }
            }

            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return  "na";
            }
            int networkType = mTelephonyManager.getNetworkType();
            if (networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType == TelephonyManager.NETWORK_TYPE_EDGE || networkType == TelephonyManager.NETWORK_TYPE_CDMA || networkType == TelephonyManager.NETWORK_TYPE_1xRTT || networkType == TelephonyManager.NETWORK_TYPE_IDEN) {
                return "2g";
            } else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS || networkType == TelephonyManager.NETWORK_TYPE_EVDO_0 || networkType == TelephonyManager.NETWORK_TYPE_EVDO_A || networkType == TelephonyManager.NETWORK_TYPE_HSDPA || networkType == TelephonyManager.NETWORK_TYPE_HSUPA || networkType == TelephonyManager.NETWORK_TYPE_HSPA || networkType == TelephonyManager.NETWORK_TYPE_EVDO_B || networkType == TelephonyManager.NETWORK_TYPE_EHRPD || networkType == TelephonyManager.NETWORK_TYPE_HSPAP) {
                return "3g";
            } else if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {
                return "4g";
            }
            return "na";
        }catch (Exception ex){
            ex.printStackTrace();
            return "UNK";
        }*/
        return "nothing";
    }


    public static void launchCustomChrometabs(Uri uri, Context activity) {
        //Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        // and launch the desired Url with CustomTabsIntent.launchUrl()

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        try {
            customTabsIntent.launchUrl(activity, uri);
        }catch (Exception ex){
            ex.printStackTrace();

            //open in browser if chrometabs not supported !
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            activity.startActivity(intent);
        }
    }

    public static void openPlayStorePage(Activity parentActivity/*, String targetPackageName*/) {

        try {
            parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + parentActivity.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" +  parentActivity.getPackageName())));
        }
    }

    public static boolean isLongScreen(Context context){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int sHeight = displayMetrics.heightPixels;
        int sWidth = displayMetrics.widthPixels;
        try {
            int ratio =  sHeight / sWidth;

            return ratio >= 2;

        }catch (Exception e){
            UiUtils.log(TAG, Log.getStackTraceString(e));

            return false;
        }
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }




    public static String getCountOfDays(String expireDateString) {

        try {


            Date today1 = new Date();


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());

            String createdDateString = dateFormat.format(today1);


            Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
            try {
                createdConvertedDate = dateFormat.parse(createdDateString);
                expireCovertedDate = dateFormat.parse(expireDateString);

                Date today = new Date();

                todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
            } catch (ParseException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }

            int cYear, cMonth, cDay;

            if (createdConvertedDate.after(todayWithZeroTime)) {
                Calendar cCal = Calendar.getInstance();
                cCal.setTime(createdConvertedDate);
                cYear = cCal.get(Calendar.YEAR);
                cMonth = cCal.get(Calendar.MONTH);
                cDay = cCal.get(Calendar.DAY_OF_MONTH);

            } else {
                Calendar cCal = Calendar.getInstance();
                cCal.setTime(todayWithZeroTime);
                cYear = cCal.get(Calendar.YEAR);
                cMonth = cCal.get(Calendar.MONTH);
                cDay = cCal.get(Calendar.DAY_OF_MONTH);
            }


        /*Calendar todayCal = Calendar.getInstance();
        int todayYear = todayCal.get(Calendar.YEAR);
        int today = todayCal.get(Calendar.MONTH);
        int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
        */

            Calendar eCal = Calendar.getInstance();
            eCal.setTime(expireCovertedDate);

            int eYear = eCal.get(Calendar.YEAR);
            int eMonth = eCal.get(Calendar.MONTH);
            int eDay = eCal.get(Calendar.DAY_OF_MONTH);

            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();

            date1.clear();
            date1.set(cYear, cMonth, cDay);
            date2.clear();
            date2.set(eYear, eMonth, eDay);

            long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

            float dayCount = (float) diff / (24 * 60 * 60 * 1000);

            return ("" + (int) dayCount + " Days");
        }catch (Exception e){
            return "";
        }
    }

    public static String getCurrentMonthAndYear() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM_yyyy", Locale.ENGLISH);
            Date today = Calendar.getInstance().getTime();
            return dateFormat.format(today);
        }catch (Exception ignored){}

        return "";
    }

    public static String getDateConversion(long val)
    {
        try {
            Date date=new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.ENGLISH);
            String dateText = df2.format(date);
            UiUtils.log(TAG,"Date: "+ dateText);
            return dateText;
        }catch (Exception ignored){}

        return "";
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return "";
    }

    public static void getPublicIpAddress(Context context, boolean defaultIp)
    {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        String url = defaultIp?GET_PUBLIC_IP:GET_PUBLIC_IP_64;
        Call<String> call = apiInterface.getPublicIpAddress(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject ipAddresssResponse = null;
                try {
                    ipAddresssResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (ipAddresssResponse != null) {
                    try {
                        CURRENT_IP = ipAddresssResponse.getString("ip");
                        UiUtils.log("IPAddress", "--> " + CURRENT_IP);

                        writeStringToPreferences(IP_ADDRESS,CURRENT_IP,context);
                        getStatesFromIP(context,CURRENT_IP);

                    } catch (JSONException e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiErrorToast(context);
                if(defaultIp)
                    getPublicIpAddress(context,false);
            }
        });
    }

    public static void getStatesFromIP(Context context, String ipAddress)
    {
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.IP, ipAddress);

        Call<String> call = apiInterface.getUsersState(GET_USER_STATE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.log("Response", "--> " + response.body());
                JSONObject ipAddresssResponse = null;
                try {
                    ipAddresssResponse = new JSONArray(response.body()).getJSONObject(0);
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (ipAddresssResponse != null) {
                    try {
                        CURRENT_COUNTRY = ipAddresssResponse.getString("countryName");
                        UiUtils.log("CURRENT_COUNTRY", "--> " + CURRENT_COUNTRY);
                        if(CURRENT_COUNTRY.equalsIgnoreCase("India")) {
                            CURRENT_REGION = ipAddresssResponse.getString("regionName");
                            UiUtils.log("CURRENT_REGION", "--> " + CURRENT_REGION);
                            JSONArray statesArray = new JSONArray(response.body()).getJSONObject(1).getJSONArray("states");
                            List<String> exampleList = new ArrayList<String>();
                            for (int i = 0; i < statesArray.length(); i++) {
                                exampleList.add(statesArray.getJSONObject(i).getString("name"));
                            }
                            STATES_PAYMENT = exampleList.toArray(new String[exampleList.size()]);
                            UiUtils.log("STATES_PAYMENT", "--> " + STATES_PAYMENT.toString());
                        }else
                        {
                            CURRENT_REGION = "";
                            STATES_PAYMENT = null;
                            UiUtils.log("OTHER", "CURRENT_REGION --> " + CURRENT_REGION);
                            UiUtils.log("OTHER", "STATES_PAYMENT --> " + STATES_PAYMENT);
                        }
                    } catch (JSONException e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiErrorToast(context);
            }
        });
    }

    public static void sendFCMTokenToServer(Context context, String fcmToken)
    {
        UiUtils.log(TAG,"Version API Called");
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.IP, CURRENT_IP);
        params.put(Params.DEVICE_TOKEN, fcmToken);

        Call<String> call = apiInterface.sendFCMTokenToServer(GET_FCM_TOKEN, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject fcmStatusResponse = null;
                try {
                    fcmStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(context);
            }
        });
    }

    public static void sendFCMTokenToServerForRef(Context context)
    {
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        String secureId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.DEVICE_TOKEN, secureId);
        params.put(Params.FCM_TOKEN, prefUtils.getStringValue(PrefKeys.FCM_TOKEN,""));
        params.put(APIConstants.Params.APPVERSION, BuildConfig.VERSION_NAME);
        params.put(VERSION_CODE, BuildConfig.VERSION_CODE);
        params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);

        Call<String> call = apiInterface.sendFCMTokenToServer(SEND_FCM_TOKEN_REF, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject fcmStatusResponse = null;
                try {
                    fcmStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiErrorToast(context);
            }
        });
    }

    public static String getSecureId(Context context)
    {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void sendShareLinkUrlDetailsToServer(Context context, String urlCode, String urlType, int id)
    {
        String secureId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.SHARE_URL_CODE, urlCode);
        params.put(Params.SHARE_URL_TYPE, urlType);
        params.put(Params.SHARE_USER_ID, id);
        params.put(Params.DEVICE_TOKEN, secureId);
        params.put(DISTRIBUTOR, APIConstants.Constants.DISTRIBUTOR);

        Call<String> call = apiInterface.sendFCMTokenToServer(SEND_SHARE_URL_DETAILS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.log(TAG, "Ref Response: "+ response.body());
                PrefUtils.getInstance(context).setValue(REFERENCE_INSTALL,true);
                /*JSONObject fcmStatusResponse = null;
                try {
                    fcmStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //NetworkUtils.onApiErrorToast(context);
                UiUtils.log(TAG,Log.getStackTraceString(t));
            }
        });
    }

    public static void getUserLoginStatus(Context context) {
        //UiUtils.showLoadingDialog(this);
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        String url = GET_USER_LOGGED_STATUS.replace("ipaddress",CURRENT_IP);
        url = url.replace("userId", String.valueOf(prefUtils.getIntValue(PrefKeys.USER_ID, -1)));
        url = url.replace("accesstoken", prefUtils.getStringValue(PrefKeys.SESSION_NEW_TOKEN, ""));
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<String> call = apiInterface.getUserLoggedInStatus(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //UiUtils.hideLoadingDialog();
                JSONObject videoResponse = null;
                try {
                    videoResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                }
                if (videoResponse != null) {
                    if (videoResponse.optString(LOGIN).equals(APIConstants.Constants.FALSE)) {
                        try {
                            if(videoResponse.optString(MESSAGE).equalsIgnoreCase(""))
                                Toast.makeText(context, context.getResources().getString(R.string.another_device) ,Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(context,videoResponse.optString(MESSAGE),Toast.LENGTH_LONG).show();
                        }catch (Exception e)
                        {
                            Toast.makeText(context, context.getResources().getString(R.string.another_device) ,Toast.LENGTH_LONG).show();
                            UiUtils.log(TAG,Log.getStackTraceString(e));
                        }
                        logOutUserInDevice(context);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //UiUtils.hideLoadingDialog();
                //nestedScrollVideoPage.setVisibility(View.VISIBLE);
                NetworkUtils.onApiError(context);
                //finish();
            }
        });
    }

    public static void logOutUserInDevice(Context context) {
        PrefHelper.setUserLoggedOut(context);
        MainActivity.CURRENT_FRAGMENT ="";
        Intent restartActivity = new Intent(context, SplashActivity.class);
        restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(restartActivity);
        //activity.finish();
    }

    public static HashMap<String,String> getDeviceDetails(Context context) {

        HashMap<String, String> hmDetails = new HashMap<>();

        try {

            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            log("Device",""+getIPAddress(true));
            hmDetails.put("IPADDRESS", getIPAddress(true)+"");
        }catch (Exception e)
        {
            log("Device","Error : "+ e.getMessage());
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

        try {

            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int densityDpi = dm.densityDpi;

            hmDetails.put(MODEL, capitalize(Build.MODEL));
            hmDetails.put(MANUFACTURER, capitalize(Build.MANUFACTURER));
            hmDetails.put(BOARD, capitalize(Build.BOARD));
            hmDetails.put(BOOTLOADER, capitalize(Build.BOOTLOADER));
            hmDetails.put(BRAND, capitalize(Build.BRAND));
            hmDetails.put(CPU_ABI, capitalize(Build.CPU_ABI));
            hmDetails.put(CPU_ABI2, capitalize(Build.CPU_ABI2));
            hmDetails.put(DEVICE, capitalize(Build.DEVICE));
            hmDetails.put(DISPLAY, capitalize(Build.DISPLAY));
            hmDetails.put(FINGERPRINT, capitalize(Build.FINGERPRINT));
            hmDetails.put(HARDWARE, capitalize(Build.HARDWARE));
            hmDetails.put(HOST, capitalize(Build.HOST));
            hmDetails.put(DEVICE_ID, capitalize(Build.ID));
            hmDetails.put(SERIAL, capitalize(Build.SERIAL));
            hmDetails.put(PRODUCT, capitalize(Build.PRODUCT));
            hmDetails.put(OS_VERSION, capitalize(Build.VERSION.RELEASE));
            hmDetails.put(VERSION, android.os.Build.VERSION.SDK_INT + "");
            hmDetails.put(APPVERSION, BuildConfig.VERSION_NAME);
            hmDetails.put(VERSION_CODE, BuildConfig.VERSION_CODE + "");
            hmDetails.put(BEBUEXT, Utils.getTotalInternalMemorySizeinMB() + "");
            hmDetails.put(PLAT, "ANDROID");
            hmDetails.put(SCREEN_SIZE, getScreenWidth(context) + " x " + getScreenWidth(context));
            hmDetails.put(SCREEN_DPI, densityDpi + "");
            hmDetails.put(RAM, getMemorySizeInBytes(context) + "");
            hmDetails.put(DEVICE_LANGUAGE, Locale.getDefault().getDisplayLanguage());
            hmDetails.put(OPEN_GL_VERSION,getVersionFromPackageManager(context)+"");

            return hmDetails;
        }catch (Exception e){
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

        return hmDetails;
    }

    private static int getVersionFromPackageManager(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    } else {
                        return 1; // Lack of property means OpenGL ES version 1
                    }
                }
            }
        }
        return 1;
    }

    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }


    public static long getMemorySizeInBytes(Context activity) {

        try {

            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

            activityManager.getMemoryInfo(memoryInfo);

            return memoryInfo.totalMem / (1024 * 1024);

        }catch (Exception e){
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

        return 0;
    }

    public static long getTime(String time)
    {
        long convertedTime = 0;
        //3 * 60 * 1000L
        String[] splitTime = time.split(":");
        long minute = Integer.parseInt(splitTime[0]) * 60 * 1000L;
        long seconds = Integer.parseInt(splitTime[1]) * 1000L;

        convertedTime = minute + seconds;
        UiUtils.log(TAG,"Time :" +time);
        UiUtils.log(TAG,"Time Converted :" +convertedTime/60/1000L);

        return convertedTime;
    }

    public static int[] getDimensions(Context context)
    {
        int[] dimensions = new int[4];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int heightDp = (int) (height / Resources.getSystem().getDisplayMetrics().density);
        int widthDp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        UiUtils.log(TAG, "Width: "+ width);
        UiUtils.log(TAG, "Width dp: "+ widthDp);

        dimensions[0] = height;
        dimensions[1] = width;
        dimensions[2] = heightDp;
        dimensions[3] = widthDp;

        return dimensions;
    }

}
