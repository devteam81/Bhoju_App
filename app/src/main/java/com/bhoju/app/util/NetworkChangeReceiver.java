package com.bhoju.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.bhoju.app.ui.activity.BaseActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private final String TAG = NetworkChangeReceiver.class.getSimpleName();

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            NetworkInfo netInfo;
            netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                //if (netInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                UiUtils.log("Network", "NETWORKNAME: " + netInfo.getTypeName());
                return true;
            }

        } else {
            if (connectivityManager != null) {
                @SuppressWarnings("deprecation")
                NetworkInfo[] netInfoArray = connectivityManager.getAllNetworkInfo();
                if (netInfoArray != null) {
                    for (NetworkInfo netInfo : netInfoArray) {
                        Toast.makeText(context, "NETWORKNAME2: " + netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        if ((netInfo.getTypeName().equalsIgnoreCase("WIFI") || netInfo.getTypeName().equalsIgnoreCase("MOBILE")) && netInfo.getState().name().equalsIgnoreCase("CONNECTED") && netInfo.isAvailable()) {
                            //if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                            UiUtils.log("Network", "NETWORKNAME: " + netInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            int status = NetworkUtil.getConnectivityStatusString(context);

           /* if (isOnline(context)) {
                BaseActivityNew.dialog(true);

            } else {
                BaseActivityNew.dialog(false);

            }*/

            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    //BaseActivity.dialog(false);

                } else {
                    //BaseActivity.dialog(true);
                }
            }

        } catch (NullPointerException e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }
}