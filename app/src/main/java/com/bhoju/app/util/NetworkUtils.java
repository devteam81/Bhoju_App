package com.bhoju.app.util;

import static com.bhoju.app.ui.activity.MainActivity.COOKIEKEYPAIR;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEPOLICY;
import static com.bhoju.app.ui.activity.MainActivity.COOKIESIGNATURE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhoju.app.util.download.NavigateToScreenEvent;
import com.downloader.PRDownloader;
import com.bhoju.app.R;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.download.Downloader;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.elasticviews.ElasticAnimation;
import com.skydoves.elasticviews.ElasticFinishListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;


public class NetworkUtils {
    private NetworkUtils() {

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @SuppressLint("HardwareIds")
    public static String getDeviceToken(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void onApiError(Context context) {
        UiUtils.hideLoadingDialog();
        //UiUtils.showShortToast(context, context.getString(R.string.something_went_wrong));
        UiUtils.showNoInternetConnection(context);
    }

    public static void onApiErrorToast(Context context) {
        UiUtils.hideLoadingDialog();
        UiUtils.showShortToast(context, context.getString(R.string.something_went_wrong));
        //UiUtils.showNoInternetConnection(context);
    }

    private static int getUniqueId(String url, String dirPath, String fileName) {
        String string = url + File.separator + dirPath + File.separator + fileName;

        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().hashCode();
    }

    public static int downloadVideo(Context context, int adminVideoId, String name, String episodeName, String url, int expiryDays) {
        String fileName = MessageFormat.format("{0}.{1}.{2}.{3}.mp4", adminVideoId, expiryDays, name, episodeName);
        UiUtils.log("Download","expiryDays: "+ expiryDays);
        UiUtils.log("Download","Download File name: "+ fileName);
        Downloader downloader = new Downloader(context);
        String savePath = context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context)
                .getIntValue(PrefKeys.USER_ID, 0);

        String cookieValue = "CloudFront-Policy="+COOKIEPOLICY+
                ";CloudFront-Signature="+COOKIESIGNATURE+
                ";CloudFront-Key-Pair-Id="+COOKIEKEYPAIR;

        //get download id ahead of time
        int notificationId = getUniqueId(url, savePath, fileName);

        downloader.setOnDownloadListener(context);
        int downloadId = downloader.downloadVideo(PRDownloader.download(url, savePath, fileName).setHeader("Cookie",cookieValue).build(), notificationId);

        //UiUtils.showCustomToast(context, String.format("Show Download"));
        //UiUtils.showShortToast(context, String.format("Starting download: %s", name));

        View parentLayout = ((Activity) context).findViewById(android.R.id.content);
        createSnackBar(context, parentLayout);

        return downloadId;
    }

    public static void createSnackBar(Context context, View view)
    {
        // create an instance of the snackbar
        final Snackbar snackbar = Snackbar.make(view, ""/*context.getResources().getString(R.string.download_started)*/, Snackbar.LENGTH_LONG);

        // inflate the custom_snackbar_view created previously
        View customSnackView =  ((Activity) context).getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);

        // set the background of the default snackbar as transparent
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        // now change the layout of the snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        // set padding of the all corners as 0
        snackbarLayout.setPadding(0, 0, 0, 0);


        // register the button from the custom_snackbar_view layout file
        TextView txt = customSnackView.findViewById(R.id.txt);
        TextView goToDownloads = customSnackView.findViewById(R.id.btn_downloaded);

        // now handle the same button with onClickListener
        goToDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(200)
                        .setOnFinishListener(new ElasticFinishListener() {
                            @Override
                            public void onFinished() {
                                // Do something after duration time
                                snackbar.dismiss();
                                ((Activity) context).finish();
                                EventBus.getDefault().postSticky(new NavigateToScreenEvent(3));
                            }
                        }).doAction();

            }
        });

        // add the custom snack bar layout to snackbar layout
        snackbarLayout.addView(customSnackView, 0);

        snackbar.show();

        //textSize based on screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);
        if(dp<400)
        {
            txt.setTextSize(12);
            goToDownloads.setTextSize(12);
            goToDownloads.setPadding(0,35,15,0);

        }else if(dp<500) {
            goToDownloads.setPadding(0, 35, 20, 0);
        }else if(dp>800) {
            txt.setTextSize(14);
            goToDownloads.setTextSize(14);
            txt.setPadding(80, 30, 30, 30);
            goToDownloads.setPadding(0, 33, 20, 0);
        }
    }

}
