package com.bhoju.app.util.download;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.bhoju.app.util.TrackSelection;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DownloadUtils {

    private static final String TAG = DownloadUtils.class.getSimpleName();

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getFileName(String name) {
        try {
            return name.split("\\.")[2];
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static String getEpisodeFileName(String name) {
        try {
            return name.split("\\.")[3];
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static int getFileExpiry(String absolutePath) {
        try {
            File file = new File(absolutePath);
            Date lastModDate = new Date(file.lastModified());
            return getCountOfDays(lastModDate);
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            return 0;
        }
    }

    public static int getVideoId(String name) {
        try {
            return Integer.parseInt(name.split("\\.")[0]);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getVideoDuration(Context context, String videoAbsolutePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.fromFile(new File(videoAbsolutePath)));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        //retriever.release();
        try {
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getHhMmSs(Long.parseLong(time));
    }

    private static int getCountOfDays(Date lastModDate) {
        Calendar lastMod = Calendar.getInstance();
        lastMod.setTime(lastModDate);
        long msDiff = Calendar.getInstance().getTimeInMillis() - lastMod.getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        return (int) daysDiff;
    }


    public static String getHhMmSs(long millis) {
        return String.format(Locale.ENGLISH,"%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static void deleteVideoFile(Context context, String absolutePath) {
        File deleteFile = new File(absolutePath);
        deleteFile.delete();
    }

    public static boolean isOfflineVideoExisting(Context context, int adminVideoId, int daysLeft) {
        UiUtils.log("TAG","-------");
        try {
            for (File tempFile : new File(context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context).getIntValue(PrefKeys.USER_ID, 0)).listFiles()) {
                try {
                    String id = tempFile.getName().split("\\.")[0];
                    UiUtils.log("TAG","Storage id: "+ id);
                    UiUtils.log("TAG","adminVideoId id: "+ adminVideoId);
                    if (Integer.parseInt(id) == adminVideoId) {
                        UiUtils.log("TAG","Matched");
                        if (daysLeft<1)
                        {
                            UiUtils.log("TAG","DELETED");
                            Downloader.downloadDeleted(context, adminVideoId,0);
                        }
                        return true;
                    }
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        UiUtils.log("TAG","Deleting id: "+ adminVideoId);
        Downloader.downloadDeleted(context, adminVideoId,0);
        return false;
    }


    public static boolean isValidVideoFile(Context context, Uri videoUri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, videoUri);
            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
            return "yes".equals(hasVideo);
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    public static String getVideoPath(Context context, int adminVideoId, String url) {
        try {
            for (File tempFile : new File(context.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(context).getIntValue(PrefKeys.USER_ID, 0)).listFiles()) {
                try {
                    String id = tempFile.getName().split("\\.")[0];
                    if (Integer.parseInt(id) == adminVideoId) {
                        return tempFile.getAbsolutePath();
                    }
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
        return url;
    }
}
