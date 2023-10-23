package com.bhoju.app.util.download;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bhoju.app.util.UiUtils;
import com.downloader.PRDownloader;

import static com.bhoju.app.util.download.Downloader.downloadCanceled;
import static com.bhoju.app.util.download.Downloader.removeFromSharedPrefPauseList;

public class DownloadCancelReceiver extends BroadcastReceiver {

    public static final String EXTRA_ADMIN_ID = "ADMIN_ID";
    public static final String EXTRA_NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String EXTRA_URL_ID = "URL_ID";
    public static final String EXTRA_SAVE_PATH_ID = "SAVE_PATH_ID";
    public static final String EXTRA_FILE_NAME_ID = "FILE_NAME_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        //UiUtils.log("DownloadCancelReceiver","Cancelled");
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        int adminVideoId = intent.getIntExtra(EXTRA_ADMIN_ID, -1);
        String url = intent.getStringExtra(EXTRA_URL_ID);
        String path = intent.getStringExtra(EXTRA_SAVE_PATH_ID);
        String fileName = intent.getStringExtra(EXTRA_FILE_NAME_ID);
        if(notificationId == 500)
        {
            UiUtils.log("DownloadCancelReceiver","adminVideoId"+ adminVideoId);
            UiUtils.log("DownloadCancelReceiver","notificationId"+ notificationId);
            PRDownloader.cancel(adminVideoId);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);

        }else if (adminVideoId != -1) {
            //UiUtils.log("DownloadCancelReceiver","adminVideoId"+ adminVideoId);
            //UiUtils.log("DownloadCancelReceiver","notificationId"+ notificationId);
            //UiUtils.log("DownloadCancelReceiver","url"+ url);
            //UiUtils.log("DownloadCancelReceiver","path"+ path);
            //UiUtils.log("DownloadCancelReceiver","fileName"+ fileName);

            removeFromSharedPrefPauseList(context,adminVideoId,notificationId, url,path,fileName);

            downloadCanceled(context, adminVideoId, notificationId);
            PRDownloader.cancel(notificationId);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
        }
    }
}

