package com.bhoju.app.gcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bhoju.app.ui.activity.SplashActivity;
import com.bhoju.app.util.sharedpref.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.bhoju.app.R;
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.network.APIConstants.Params.APPVERSION;
import static com.bhoju.app.network.APIConstants.Params.IMAGE;
import static com.bhoju.app.network.APIConstants.Params.NOTIFICATION_SOUND;
import static com.bhoju.app.network.APIConstants.Params.NOTIFICATION_TYPE;
import static com.bhoju.app.network.APIConstants.Params.NOTIFICATION_VERSION_CODE;
import static com.bhoju.app.network.APIConstants.Params.NOTIFICATION_VERSION_NAME;
import static com.bhoju.app.network.APIConstants.Params.PARENT_ID;
import static com.bhoju.app.network.APIConstants.Params.SEASON_ID;
import static com.bhoju.app.network.APIConstants.Params.TYPE;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.util.UiUtils.checkString;

public class MessagingServices extends FirebaseMessagingService {

    private static final String TAG = "FCM Message";

    public MessagingServices() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            if(remoteMessage.getNotification()!=null) {
                UiUtils.log("msg", "onMessageReceived: " + remoteMessage.getNotification().getTitle());
                UiUtils.log("msg", "onMessageReceived: " + remoteMessage.getNotification().getBody());
            }

            UiUtils.log(TAG, "onMessageReceived: " + remoteMessage.getData().get("message"));
            UiUtils.log(TAG, "msg" + remoteMessage.getData());
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                UiUtils.log(TAG, "key=> " + key + " value=> " + value);
            }

            String notificationType = remoteMessage.getData().get(TYPE)!=null?remoteMessage.getData().get(TYPE):"Default";

            String channelId = notificationType;//"Default";
            NotificationCompat.Builder builder;

            //Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(TYPE,notificationType);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            builder = new NotificationCompat.Builder(this, channelId)
                    .setContentText(getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);

            Uri soundUri = null;//RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(!checkString(remoteMessage.getData().get(NOTIFICATION_SOUND)))
                soundUri = Uri.parse(remoteMessage.getData().get(NOTIFICATION_SOUND));
            //soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.other_notification);
            String imageUrl = remoteMessage.getData().get(IMAGE);

            //Normal Notification
            if(notificationType.equalsIgnoreCase("all"))
            {
                intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            }
            //Subscription Notification
            else if(notificationType.equalsIgnoreCase("Subscription"))
            {

            }
            //Video Notification
            else if(notificationType.equalsIgnoreCase("Video"))
            {
                UiUtils.log(TAG, "Id: " + remoteMessage.getData().get(ADMIN_VIDEO_ID));
                String video_id = remoteMessage.getData().get(ADMIN_VIDEO_ID);
                if (!checkString(video_id)) {
                    //intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(ADMIN_VIDEO_ID, Integer.parseInt(video_id));
                    intent.putExtra(PARENT_ID, Integer.parseInt(video_id));
                    intent.putExtra(SEASON_ID, -1);
                    intent.putExtra("userName", "");
                }

            }else if(notificationType.equalsIgnoreCase("Update"))
            {
                String versionCode = checkString(remoteMessage.getData().get(NOTIFICATION_VERSION_CODE))? "-1": remoteMessage.getData().get(NOTIFICATION_VERSION_CODE);
                String appVersion = checkString(remoteMessage.getData().get(NOTIFICATION_VERSION_NAME))? "":remoteMessage.getData().get(NOTIFICATION_VERSION_NAME);
                intent.putExtra(VERSION_CODE, Integer.parseInt(versionCode));
                intent.putExtra(APPVERSION, appVersion);

            }else if(notificationType.equalsIgnoreCase("Login"))
            {
            }/*
            else {

                String video_id = remoteMessage.getData().get(ADMIN_VIDEO_ID);
                imageUrl = remoteMessage.getData().get(IMAGE);

                UiUtils.log(TAG, "Id: " + remoteMessage.getData().get(ADMIN_VIDEO_ID));
                UiUtils.log(TAG, "Image: " + remoteMessage.getData().get(IMAGE));

                if (!checkString(video_id)) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(ADMIN_VIDEO_ID, Integer.parseInt(video_id));
                    intent.putExtra(PARENT_ID, Integer.parseInt(video_id));
                    intent.putExtra(SEASON_ID, -1);
                    intent.putExtra("userName", "");
                }

                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (video_id != null && video_id.equalsIgnoreCase("206"))
                    channelId = "Custom";
                builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true);

                if (video_id != null && !video_id.equalsIgnoreCase("206")) {
                    makeNormalLayout(builder, remoteMessage.getData(), imageUrl, pendingIntent);
                    soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://" + getApplicationContext().getPackageName() + "/" + R.raw.normal_notification);
                    UiUtils.log(TAG, "Sound: " + soundUri.toString());

                } else {
                    makeCustomLayout(builder, remoteMessage.getData(), imageUrl, pendingIntent);
                    addActionButtons(builder, video_id);
                    soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.other_notification);
                    UiUtils.log(TAG, "Sound: " + soundUri.toString());
                }


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UiUtils.log(TAG,"Build.VERSION_CODES.O");
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);

                    // Changing Default mode of notification
                    builder.setDefaults(Notification.DEFAULT_VIBRATE);
                    // Creating an Audio Attribute
                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();

                    // Creating Channel
                    channel.enableLights(true);
                    channel.enableVibration(true);
                    channel.setSound(soundUri, attributes); // This is IMPORTANT

                    if (manager != null) {
                   *//* List<NotificationChannel> channelList = manager.getNotificationChannels();

                    for (int i = 0; channelList != null && i < channelList.size(); i++) {
                        manager.deleteNotificationChannel(channelList.get(i).getId());
                    }*//*
                        manager.createNotificationChannel(channel);
                    }
                }else
                {
                    UiUtils.log(TAG,"Build.VERSION_CODES.O NO");

                    //builder.setSound(soundUri);
                }
                //builder.setSound(soundUri);
                if (manager != null) {
                    manager.notify(0, builder.build());
                    //Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
                    //r.play();
                }
            }*/

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            makeCustomLayout(builder, remoteMessage.getData(), imageUrl, pendingIntent);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                UiUtils.log(TAG,"Build.VERSION_CODES.O");
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);

                // Changing Default mode of notification
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                // Creating an Audio Attribute
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

                // Creating Channel
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setSound(null,null/*soundUri, attributes*/); // This is IMPORTANT

                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }
            if (manager != null) {
                manager.notify(0, builder.build());
                if(soundUri==null)
                    soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
                r.play();
            }
        } catch (Exception e) {
            /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);*/
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

    }

    private void makeNormalLayout(NotificationCompat.Builder builder, Map<String, String> data, String imageUrl, PendingIntent pendingIntent) {
        builder.setContentTitle(getString(R.string.app_name) +" - " +data.get("title"))
                .setContentText(data.get("body"))
                .setContentIntent(pendingIntent);

        if (checkString(imageUrl)) {
            Bitmap bitmapFromURL = getBitmapFromURL(imageUrl);
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmapFromURL)/*.setSummaryText(messageBody)*/);
        }
    }

    private void makeCustomLayout(NotificationCompat.Builder builder, Map<String, String> data, String imageUrl, PendingIntent pendingIntent) {

        // Get the layouts to use in the custom notification
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);

        String title = data.get("title");
        String desc = data.get("body");

        notificationLayout.setTextViewText(R.id.notification_title, title);
        notificationLayout.setTextViewText(R.id.notification_description, desc);

        notificationLayoutExpanded.setTextViewText(R.id.notification_title, title);
        notificationLayoutExpanded.setTextViewText(R.id.notification_description, desc);


        {
            int random = new Random().nextInt(5 - 1 + 1) + 1;
            UiUtils.log(TAG,"Number: "+ random);

            int colorTitle = R.color.white;
            int colorBody = R.color.white;

            if(random==1) {
                //Done
                colorTitle = R.color.colorAccent;
                colorBody = R.color.white;
                notificationLayout.setImageViewResource(R.id.notification_img_bg,R.drawable.notification_bg_1_small);
                if(checkString(imageUrl))
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_1_medium);
                else
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_1_big);
            }
            else if(random==2){
                //Done
                colorTitle = R.color.blue_color;
                colorBody = R.color.colorAccent;
                notificationLayout.setImageViewResource(R.id.notification_img_bg,R.drawable.notification_bg_2_small);
                if(checkString(imageUrl))
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_2_medium);
                else
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_2_big);
            }
            else if(random==3){
                //Done
                colorTitle = R.color.white;
                colorBody = R.color.button_color;
                notificationLayout.setImageViewResource(R.id.notification_img_bg,R.drawable.notification_bg_3_small);
                if(checkString(imageUrl))
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_3_medium);
                else
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_3_big);
            }
            else if(random==4){
                colorTitle = R.color.colorPrimaryLight;
                colorBody = R.color.dark_blue;
                notificationLayout.setImageViewResource(R.id.notification_img_bg,R.drawable.notification_bg_4_small);
                if(checkString(imageUrl))
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_4_medium);
                else
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_4_big);
            }else {
                //Done
                colorTitle = R.color.colorPrimaryLight;
                colorBody = R.color.white;
                notificationLayout.setImageViewResource(R.id.notification_img_bg,R.drawable.notification_bg_5_small);
                if(checkString(imageUrl))
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_5_medium);
                else
                    notificationLayoutExpanded.setImageViewResource(R.id.notification_img_bg, R.drawable.notification_bg_5_big);
            }

            notificationLayout.setTextColor(R.id.notification_title,getResources().getColor(colorTitle,null));
            notificationLayoutExpanded.setTextColor(R.id.notification_title,getResources().getColor(colorTitle,null));

            notificationLayout.setTextColor(R.id.notification_description,getResources().getColor(colorBody,null));
            notificationLayoutExpanded.setTextColor(R.id.notification_description,getResources().getColor(colorBody,null));
        }

        if(checkString(desc))
        {
            notificationLayout.setViewVisibility(R.id.notification_description, View.GONE);
            notificationLayoutExpanded.setViewVisibility(R.id.notification_description, View.GONE);
        }

        if (imageUrl != null && imageUrl.length() != 0) {
            Bitmap bitmapFromURL = getBitmapFromURL(imageUrl);

            //Image Rounded Corners
            Bitmap imageRounded=Bitmap.createBitmap(bitmapFromURL.getWidth(), bitmapFromURL.getHeight(), bitmapFromURL.getConfig());
            Canvas canvas=new Canvas(imageRounded);
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(bitmapFromURL, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, bitmapFromURL.getWidth(), bitmapFromURL.getHeight())), 50, 50, paint);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                notificationLayoutExpanded.setImageViewBitmap(R.id.notification_img_new, bitmapFromURL);
            } else {
                notificationLayoutExpanded.setViewVisibility(R.id.notification_img_new, View.GONE);
                //notificationLayout.setImageViewBitmap(R.id.notification_img_new, bitmapFromURL);
                //notificationLayoutExpanded.setImageViewBitmap(R.id.notification_img, bitmapFromURL);
            }

            builder.setStyle(new NotificationCompat.BigPictureStyle());
        }else
        {
            notificationLayout.setViewVisibility(R.id.notification_img_new, View.GONE);
            notificationLayoutExpanded.setViewVisibility(R.id.notification_img_new, View.GONE);
            //notificationLayoutExpanded.setViewVisibility(R.id.notification_img, View.GONE);
            builder.setStyle(new NotificationCompat.BigTextStyle());
        }

        builder.setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(pendingIntent);

        //builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        //builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        //builder.setContent(notificationLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationLayout.setViewLayoutWidth(R.id.img_logo,30, TypedValue.COMPLEX_UNIT_DIP);
            notificationLayout.setViewLayoutHeight(R.id.img_logo,30, TypedValue.COMPLEX_UNIT_DIP);
            //notificationLayout.setTextViewTextSize(R.id.notification_app_name, TypedValue.COMPLEX_UNIT_DIP, 10);
            notificationLayout.setTextViewTextSize(R.id.notification_title, TypedValue.COMPLEX_UNIT_DIP, 10);
            notificationLayout.setTextViewTextSize(R.id.notification_description, TypedValue.COMPLEX_UNIT_DIP, 8);
            notificationLayout.setViewPadding(R.id.rl_content, 20,15, 20,15);
            notificationLayout.setViewLayoutMargin(R.id.ll_notification_details, RemoteViews.MARGIN_TOP,-5,TypedValue.COMPLEX_UNIT_DIP);
            notificationLayout.setViewPadding(R.id.ll_notification_details, 0,0,0,10);
        }

    }

    private void addActionButtons(NotificationCompat.Builder builder, String video_id)
    {
        //send to screen
        //Background task
        /*Intent broadcastUpdateIntent = new Intent(this, NotificationBroadcastReceiver.class);
        broadcastUpdateIntent.putExtra("action", "Update");

        PendingIntent broadcastUpdatePendingIntent = PendingIntent.getBroadcast(this, 0, broadcastUpdateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.app_logo, "Update", broadcastUpdatePendingIntent);*/
        Intent actionUpdateIntent = new Intent(getApplicationContext(), MainActivity.class);
        actionUpdateIntent.putExtra("type", "Update");
        actionUpdateIntent.setAction(Intent.ACTION_MAIN);
        actionUpdateIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        actionUpdateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingUpdateActionIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), actionUpdateIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.app_logo, "Update", pendingUpdateActionIntent);


        Intent actionDownloadIntent = new Intent(getApplicationContext(), MainActivity.class);
        actionDownloadIntent.putExtra("type", "Download");
        actionDownloadIntent.setAction(Intent.ACTION_MAIN);
        actionDownloadIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        actionDownloadIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingDownloadActionIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), actionDownloadIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.app_logo, "Download", pendingDownloadActionIntent);


        //Background task
        /*Intent broadcastSubscribeIntent = new Intent(this, NotificationBroadcastReceiver.class);
        broadcastSubscribeIntent.putExtra("action", "Subscribe");

        PendingIntent broadcastSubscribePendingIntent = PendingIntent.getBroadcast(this, 0, broadcastSubscribeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.app_logo, "Subscribe", broadcastSubscribePendingIntent);*/
        Intent actionSubscribeIntent = new Intent(getApplicationContext(), MainActivity.class);
        if(!checkString(video_id))
        {
            actionSubscribeIntent = new Intent(getApplicationContext(), MainActivity.class);
            actionSubscribeIntent.putExtra(ADMIN_VIDEO_ID, Integer.parseInt(video_id));
            actionSubscribeIntent.putExtra(PARENT_ID, Integer.parseInt(video_id));
            actionSubscribeIntent.putExtra(SEASON_ID, -1);
            //actionSubscribeIntent.putExtra(IMAGE, video.getDefaultImage());
            //actionSubscribeIntent.putExtra(TITLE, video.getTitle());
            actionSubscribeIntent.putExtra("userName", "");
        }
        actionSubscribeIntent.putExtra("type", "Subscribe");
        actionSubscribeIntent.setAction(Intent.ACTION_MAIN);
        actionSubscribeIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        actionSubscribeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingSubscribeActionIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), actionSubscribeIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.app_logo, "Subscribe", pendingSubscribeActionIntent);
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token) {
        PrefUtils.getInstance(getApplicationContext()).setValue(PrefKeys.FCM_TOKEN, token);
        UiUtils.log(TAG,"TOKEN: "+ token);
    }

    public Bitmap getBitmapFromURL(String strURL) {
        InputStream input = null;

        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            return null;
        }finally {
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        }
    }

    public static void getFCMToken(Context context) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            UiUtils.log(TAG, "Fetching FCM registration token failed : " + task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        UiUtils.log("Token","FCM Token: "+ token);
                        PrefUtils.getInstance(context).setValue(PrefKeys.FCM_TOKEN, token);
                        if(PrefUtils.getInstance(context).getBoolanValue(PrefKeys.IS_LOGGED_IN, false))
                            Utils.sendFCMTokenToServer(context, token);
                    }
                });
    }
}


