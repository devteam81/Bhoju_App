package com.bhoju.app.util.download;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bhoju.app.util.ConfigParser;
import com.bhoju.app.util.NetworkUtils;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.request.DownloadRequest;
import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.ui.activity.VideoPageActivity;
import com.bhoju.app.ui.fragment.DownloadsFragment;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.DOWNLOADED_STATUS_UPDATE;
import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Constants.CANCELLED_OR_COMPLETED;
import static com.bhoju.app.network.APIConstants.Constants.DOWNLOADED_PROGRESS;
import static com.bhoju.app.network.APIConstants.Constants.DOWNLOAD_STATUS;
import static com.bhoju.app.network.APIConstants.DownloadStatus;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.ui.activity.VideoPageActivity.ACTION_DOWNLOAD_UPDATE;
import static com.bhoju.app.util.download.DownloadUtils.getFileSize;

public class Downloader {

    private static final String TAG = Downloader.class.getSimpleName();
    private static PrefUtils prefUtils;
    private static APIInterface apiInterface;
    public Context context;
    private CharSequence channelName;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private HashMap<Integer, Long> map;
    private DownloadCompleteListener downloadCompleteListener;
    private List<Integer> downloadingTasks;


    public Downloader(Context context) {
        this.context = context;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(context);
        this.downloadingTasks = new ArrayList<>();
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        map = new HashMap<>();

        channelName = context.getString(R.string.channel_name);
    }


    public static void downloadCompleted(final Context context, final int adminVideoId, final int notificationId) {
        downloadStatusUpdate(context, adminVideoId, notificationId, DownloadStatus.DOWNLOAD_COMPLETE_STATUS,"","","");
    }

    public static void downloadCanceled(final Context context, final int adminVideoId, final int notificationId) {
        downloadStatusUpdate(context, adminVideoId, notificationId, DownloadStatus.DOWNLOAD_CANCEL_STATUS,"","","");
    }

    public static void downloadStart(final Context context, final int adminVideoId, final int notificationId, String url, String path, String fileName) {
        downloadStatusUpdate(context, adminVideoId, notificationId, DownloadStatus.DOWNLOAD_INITIATE_STATUS,url,path,fileName);
    }

    public static void downloadPause(final Context context, final int adminVideoId, final int notificationId, String url, String path, String fileName) {
        downloadStatusUpdate(context, adminVideoId, notificationId, DownloadStatus.DOWNLOAD_PAUSE_STATUS,url,path,fileName);
    }

    public static void downloadProgress(final Context context, final int adminVideoId, final int notificationId, String url, String path, String fileName) {
        downloadStatusUpdate(context, adminVideoId, notificationId, DownloadStatus.DOWNLOAD_PROGRESS_STATUS,url,path,fileName);
    }

    public static void downloadDeleted(final Context context, final int adminVideoId, final int notificationId) {
        downloadStatusUpdate(context, adminVideoId, notificationId, DownloadStatus.DOWNLOAD_DELETED_STATUS,"","","");
    }

    private static void putDownloadForDelete(Context context, int adminVideoId, int notificationId) {
        if (prefUtils == null)
            prefUtils = PrefUtils.getInstance(context);
        String pendingDeletes = prefUtils.getStringValue(PrefKeys.DELETE_VIDEOS_DOWNLOAD, "");
        ArrayList<String> deleteArrayList = new ArrayList<String>();
        if(pendingDeletes.length()>0) {
            String[] strSplit = pendingDeletes.split(",");
            deleteArrayList = new ArrayList<String>(Arrays.asList(strSplit));
        }
        deleteArrayList.add(adminVideoId+"_"+notificationId);

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<deleteArrayList.size();i++)
        {
            sb.append(deleteArrayList.get(i));
            if(i != deleteArrayList.size()-1)
                sb.append(",");
        }
        PrefUtils.getInstance(context).setValue(PrefKeys.DELETE_VIDEOS_DOWNLOAD, String.valueOf(sb));
    }
    private static void removeFromSharedPrefDeleteList(Context context, int adminVideoId, int notificationId) {
        if (prefUtils == null)
            prefUtils = PrefUtils.getInstance(context);
        String pendingDeletes = prefUtils.getStringValue(PrefKeys.DELETE_VIDEOS_DOWNLOAD, "");
        ArrayList<String> deleteArrayList = new ArrayList<String>();
        if(pendingDeletes.length()>0) {
            String[] strSplit = pendingDeletes.split(",");
            deleteArrayList = new ArrayList<String>(Arrays.asList(strSplit));
        }

        for (String s : deleteArrayList)
        {
            if(s.equalsIgnoreCase(adminVideoId+"_"+notificationId)) {
                deleteArrayList.remove(s);
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<deleteArrayList.size();i++)
        {
            sb.append(deleteArrayList.get(i));
            if(i != deleteArrayList.size()-1)
                sb.append(",");
        }
        PrefUtils.getInstance(context).setValue(PrefKeys.DELETE_VIDEOS_DOWNLOAD, String.valueOf(sb));
    }

    private static void putDownloadForCancel(Context context, int adminVideoId,int notificationId) {
        if (prefUtils == null)
            prefUtils = PrefUtils.getInstance(context);
        String pendingCancels = prefUtils.getStringValue(PrefKeys.CANCEL_VIDEOS_DOWNLOAD, "");
        String[] strSplit = pendingCancels.split(",");
        ArrayList<String> cancelArrayList = new ArrayList<String>(Arrays.asList(strSplit));
        cancelArrayList.add(adminVideoId+"_"+notificationId);

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<cancelArrayList.size();i++)
        {
            sb.append(cancelArrayList.get(i));
            if(i != cancelArrayList.size()-1)
                sb.append(",");
        }
        PrefUtils.getInstance(context).setValue(PrefKeys.CANCEL_VIDEOS_DOWNLOAD, String.valueOf(sb));
    }
    private static void removeFromSharedPrefCancelList(Context context, int adminVideoId, int notificationId) {
        if (prefUtils == null)
            prefUtils = PrefUtils.getInstance(context);
        String pendingDeletes = prefUtils.getStringValue(PrefKeys.CANCEL_VIDEOS_DOWNLOAD, "");
        String[] strSplit = pendingDeletes.split(",");
        ArrayList<String> cancelArrayList = new ArrayList<String>(Arrays.asList(strSplit));
        for (String s : cancelArrayList)
        {
            if(s.equalsIgnoreCase(adminVideoId+"_"+notificationId)) {
                cancelArrayList.remove(s);
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<cancelArrayList.size();i++)
        {
            sb.append(cancelArrayList.get(i));
            if(i != cancelArrayList.size()-1)
                sb.append(",");
        }
        PrefUtils.getInstance(context).setValue(PrefKeys.CANCEL_VIDEOS_DOWNLOAD, String.valueOf(sb));
    }

    private static void putDownloadForPause(Context context, int adminVideoId, int notificationId, String url, String path, String fileName) {
        if (prefUtils == null)
            prefUtils = PrefUtils.getInstance(context);
        String pendingPauses = prefUtils.getStringValue(PrefKeys.PAUSE_VIDEOS_DOWNLOAD, "");
        String[] strSplit = pendingPauses.split(",");
        ArrayList<String> pauseArrayList = new ArrayList<String>(Arrays.asList(strSplit));
        pauseArrayList.add(adminVideoId+"_"+notificationId+"_"+url+"_"+path+"_"+fileName);

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<pauseArrayList.size();i++)
        {
            sb.append(pauseArrayList.get(i));
            if(i != pauseArrayList.size()-1)
                sb.append(",");
        }
        PrefUtils.getInstance(context).setValue(PrefKeys.PAUSE_VIDEOS_DOWNLOAD, String.valueOf(sb));
    }
    public static void removeFromSharedPrefPauseList(Context context, int adminVideoId, int notificationId, String url, String path, String fileName ) {
        if(prefUtils==null)
            prefUtils = PrefUtils.getInstance(context);
        String pendingPauses = prefUtils.getStringValue(PrefKeys.PAUSE_VIDEOS_DOWNLOAD, "");
        String[] strSplit = pendingPauses.split(",");
        ArrayList<String> pauseArrayList = new ArrayList<String>(Arrays.asList(strSplit));
        for (String s : pauseArrayList)
        {
            if(s.equalsIgnoreCase(adminVideoId+"_"+notificationId+"_"+url+"_"+path+"_"+fileName)) {
                pauseArrayList.remove(s);
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<pauseArrayList.size();i++)
        {
            sb.append(pauseArrayList.get(i));
            if(i != pauseArrayList.size()-1)
                sb.append(",");
        }
        PrefUtils.getInstance(context).setValue(PrefKeys.PAUSE_VIDEOS_DOWNLOAD, String.valueOf(sb));
    }

    private static void downloadStatusUpdate(final Context context, final int adminVideoId, final int notificationId, final int downloadStatus, String url, String path, String fileName) {
        if (prefUtils == null)
            prefUtils = PrefUtils.getInstance(context);
        if (apiInterface == null)
            apiInterface = APIClient.getClient().create(APIInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(ADMIN_VIDEO_ID, adminVideoId);
        params.put(Params.DOWNLOAD_ID, notificationId);
        params.put(Params.DOWNLOAD_URL, url);
        params.put(Params.DOWNLOAD_SAVE_PATH, path);
        params.put(Params.DOWNLOAD_FILE_NAME, fileName);
        params.put(Params.STATUS, downloadStatus);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.downloadStatusUpdate(DOWNLOADED_STATUS_UPDATE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject downloadStatusUpdateResponse = null;
                try {
                    downloadStatusUpdateResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (downloadStatusUpdateResponse != null) {
                    if (!downloadStatusUpdateResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        handleNetworkFailure(context, adminVideoId, downloadStatus, notificationId);
                    }
                    else
                    {
                        switch (downloadStatus)
                        {
                            case 3:
                                Downloader.removeFromSharedPrefPauseList(context,adminVideoId, notificationId, url, path, fileName);
                                break;
                            case 5:
                                Downloader.removeFromSharedPrefCancelList(context,adminVideoId,notificationId);
                            case 6:
                                Downloader.removeFromSharedPrefDeleteList(context,adminVideoId,notificationId);
                                break;
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                handleNetworkFailure(context, adminVideoId, downloadStatus, notificationId);
            }
        });
    }

    private static void handleNetworkFailure(Context context, int adminVideoId, int downloadStatus, int notificationId) {
        switch (downloadStatus) {
            case DownloadStatus.DOWNLOAD_CANCEL_STATUS:
                putDownloadForCancel(context, adminVideoId, notificationId);
                break;
            case DownloadStatus.DOWNLOAD_COMPLETE_STATUS:
                break;
            case DownloadStatus.DOWNLOAD_INITIATE_STATUS:
                break;
            case DownloadStatus.DOWNLOAD_PAUSE_STATUS:
                UiUtils.log(TAG,"Added to db");
                break;
            case DownloadStatus.DOWNLOAD_DELETED_STATUS:
                putDownloadForDelete(context, adminVideoId, notificationId);
                UiUtils.log(TAG,"Added to db");
                break;
        }
    }

    public int downloadVideo(final DownloadRequest downloadRequest, final int notificationId) {
        final int adminVideoId = Integer.parseInt(downloadRequest.getFileName().split("\\.")[0]);

        if (!map.containsKey(notificationId)) {
            map.put(notificationId, 0L);
            map.put(100, 0L);
        }
        int id = (int) System.currentTimeMillis();
        NotificationCompat.Action cancelAction = getCancelActionForVideoId(notificationId,id,adminVideoId,downloadRequest);
        builder = showDownloadingNotification(context, downloadRequest.getFileName(), "", cancelAction);
        manager.notify(notificationId, builder.build());

        downloadRequest.setOnStartOrResumeListener(() -> {
            UiUtils.log(TAG, "Started");
            String savePath = context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context)
                    .getIntValue(PrefKeys.USER_ID, 0);

            downloadStart(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
            sendBroadCastToSingleVideoPage(context, adminVideoId, false,1, "");
        }).setOnPauseListener(() -> {

            String savePath = context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context)
                    .getIntValue(PrefKeys.USER_ID, 0);
            downloadPause(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
            sendBroadCastToSingleVideoPage(context, adminVideoId, false,3,"");

        }).setOnProgressListener(progress -> {
            boolean once = true;
            if (progress.currentBytes - (map.get(notificationId)) > 300000) {
                map.put(notificationId, progress.currentBytes);
                builder.setContentText(getFileSize(progress.currentBytes) + " / " + getFileSize(progress.totalBytes));
                builder.setProgress(100, (int) ((progress.currentBytes * 100) / progress.totalBytes), false);
                manager.notify(notificationId, builder.build());


                try {

                    int progressMB = (int) ((progress.currentBytes/1024)/1024);
                int progressMBTotal = (int) ((progress.totalBytes/1024)/1024);
                UiUtils.log("Progress" ,"Size: "+ progressMB + "/"+ progressMBTotal);

                int progressPercentage;
                if(progressMB>=1) {
                    progressPercentage = (int) (progressMB * 100) / progressMBTotal;
                }else
                {
                    UiUtils.log("Progress" ,"Size: "+ progress.currentBytes + "/"+ progress.totalBytes);
                    progressPercentage = (int) ((progress.currentBytes*100) / progress.totalBytes);

                }
                UiUtils.log("Progress", "Percentage: " + progressPercentage + "%");
                    String savePath = context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context)
                            .getIntValue(PrefKeys.USER_ID, 0);

                    if (progressMBTotal <= 10 && (progress.currentBytes - (map.get(100)) > 1000000)) {
                        map.put(100, progress.currentBytes);
                        downloadProgress(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                        sendBroadCastToSingleVideoPage(context, adminVideoId, false, 2, String.valueOf(progressPercentage));
                    } else if ((progressMBTotal > 10 && progressMBTotal <= 50) && (progress.currentBytes - (map.get(100)) > 2000000)) {
                        map.put(100, progress.currentBytes);
                        downloadProgress(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                        sendBroadCastToSingleVideoPage(context, adminVideoId, false, 2, String.valueOf(progressPercentage));
                    } else if ((progressMBTotal > 50 && progressMBTotal <= 100) && (progress.currentBytes - (map.get(100)) > 3000000)) {
                        map.put(100, progress.currentBytes);
                        downloadProgress(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                        sendBroadCastToSingleVideoPage(context, adminVideoId, false, 2, String.valueOf(progressPercentage));
                    } else if ((progressMBTotal > 100 && progressMBTotal <= 500) && (progress.currentBytes - (map.get(100)) > 5000000)) {
                        map.put(100, progress.currentBytes);
                        downloadProgress(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                        sendBroadCastToSingleVideoPage(context, adminVideoId, false, 2, String.valueOf(progressPercentage));
                    } else if ((progressMBTotal > 500) && (progress.currentBytes - (map.get(100)) > 10000000)) {
                        map.put(100, progress.currentBytes);
                        downloadProgress(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                        sendBroadCastToSingleVideoPage(context, adminVideoId, false, 2, String.valueOf(progressPercentage));
                    }
                }catch (Exception e)
                {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                    String savePath = context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context)
                            .getIntValue(PrefKeys.USER_ID, 0);
                    downloadProgress(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                    sendBroadCastToSingleVideoPage(context, adminVideoId, false, 2, "");
                }

            }
        }).setOnCancelListener(() -> {
            manager.cancel(notificationId);
            UiUtils.showShortToast(context, context.getString(R.string.download_cancelled));
            downloadCanceled(context, adminVideoId, downloadRequest.getDownloadId());
            downloadCompleteListener.downloadCancelled(adminVideoId);
            sendBroadCastToSingleVideoPage(context, adminVideoId, false, 5, "");
        });

        //start download
        int downloadId = downloadRequest.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                UiUtils.log(TAG, "Done");
                downloadCompleted(context, adminVideoId, downloadRequest.getDownloadId());
                downloadCompleteListener.downloadCompleted(adminVideoId);
                UiUtils.showShortToast(context, downloadRequest.getFileName().split("\\.")[2] + " stored for offline viewing!");
                manager.cancel(notificationId);
                sendBroadCastToSingleVideoPage(context, adminVideoId, true, 4,"");
            }

            @Override
            public void onError(Error error) {
                if (NetworkUtils.isNetworkConnected(context)) {
                    UiUtils.log(TAG,"ERROR: "+ error.getServerErrorMessage());
                    downloadCanceled(context, adminVideoId, downloadRequest.getDownloadId());
                    downloadCompleteListener.downloadCancelled(adminVideoId);
                    UiUtils.showShortToast(context, "There was a problem storing " + downloadRequest.getFileName() + " offline!");
                    manager.cancel(notificationId);
                    //Sending broadcast to single video page
                    sendBroadCastToSingleVideoPage(context, adminVideoId, false, 5, "");
                }else
                {
                    UiUtils.log(TAG,"ERROR: "+ error.getServerErrorMessage());
                    UiUtils.log(TAG,"ERROR: "+ adminVideoId +"-"+notificationId);
                    String savePath = context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context)
                            .getIntValue(PrefKeys.USER_ID, 0);
                    putDownloadForPause(context, adminVideoId, notificationId, downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                    downloadPause(context, adminVideoId, downloadRequest.getDownloadId(), downloadRequest.getUrl(), savePath, downloadRequest.getFileName());
                    downloadCompleteListener.downloadPaused(adminVideoId);
                    sendBroadCastToSingleVideoPage(context, adminVideoId, false, 3, "");

                }
            }
        });

        return downloadId;
    }

    private void sendBroadCastToSingleVideoPage(Context context, int adminVideoId, boolean cancelledOrCompleted, int downloadStatus, String progress) {
        Intent intent = new Intent();
        intent.putExtra(ADMIN_VIDEO_ID, adminVideoId);
        intent.putExtra(DOWNLOAD_STATUS, downloadStatus);
        intent.putExtra(DOWNLOADED_PROGRESS, progress);
        intent.putExtra(CANCELLED_OR_COMPLETED, cancelledOrCompleted);
        intent.setAction(VideoPageActivity.ACTION_DOWNLOAD_UPDATE);
        context.sendBroadcast(intent);
    }

    private void sendBroadCastToDownloadVideoPage(Context context, int adminVideoId, boolean cancelledOrCompleted) {
        Intent intent = new Intent();
        intent.putExtra(ADMIN_VIDEO_ID, adminVideoId);
        intent.putExtra(CANCELLED_OR_COMPLETED, cancelledOrCompleted);
        intent.setAction(ACTION_DOWNLOAD_UPDATE);
        context.sendBroadcast(intent);
    }

    private NotificationCompat.Action getCancelActionForVideoId(int notificationId, int uniqueId, int adminVideoId, DownloadRequest downloadRequest) {
        Intent cancel = new Intent(BuildConfig.APPLICATION_ID + ".CANCEL_DOWNLOAD");
        cancel.putExtra(DownloadCancelReceiver.EXTRA_ADMIN_ID, adminVideoId);
        cancel.putExtra(DownloadCancelReceiver.EXTRA_NOTIFICATION_ID, notificationId);
        cancel.putExtra(DownloadCancelReceiver.EXTRA_URL_ID, downloadRequest.getUrl());
        cancel.putExtra(DownloadCancelReceiver.EXTRA_SAVE_PATH_ID, downloadRequest.getDirPath());
        cancel.putExtra(DownloadCancelReceiver.EXTRA_FILE_NAME_ID, downloadRequest.getFileName());
        cancel.setClass(context, DownloadCancelReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(context.getApplicationContext(), uniqueId, cancel, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_close_clear_cancel, "Cancel download", cancelIntent)
                .build();
    }

    private NotificationCompat.Builder showDownloadingNotification(Context context, String title, String message, NotificationCompat.Action cancelAction) {

        String channelId = "offlineVideos";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            mChannel.enableVibration(false);
            manager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        //cancel action


        return new NotificationCompat.Builder(context, channelId).setContentTitle(title)
                .setSubText("Fetching video offline..")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("")
                .setOngoing(true)
                .setVibrate(new long[]{0L})
                .setContentIntent(contentIntent)
                .addAction(cancelAction)
                .setProgress(100, 0, false);
    }

    private void hideDownloadingNotification(final int id) {
        manager.cancel(id);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for (int i = 0; i < downloadingTasks.size(); i++) {
            hideDownloadingNotification(downloadingTasks.get(i));
        }
    }

    public void setOnDownloadListener(Context context) {
        if(context instanceof VideoPageActivity )
            downloadCompleteListener = (VideoPageActivity) context;
        else
            downloadCompleteListener = (MainActivity) context;
    }
}