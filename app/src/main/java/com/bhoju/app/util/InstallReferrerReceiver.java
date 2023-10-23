package com.bhoju.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

public class InstallReferrerReceiver extends BroadcastReceiver {

    private final String TAG = InstallReferrerReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            String referrer = intent.getStringExtra("referrer");
            UiUtils.log(TAG, "referrer: " + referrer);
            //Use the referrer
            //Toast.makeText(context, "Data Received From Broadcast", Toast.LENGTH_LONG).show();
            PrefUtils.getInstance(context).setValue(PrefKeys.REFERENCE_URL_CODE, referrer);
            //Utils.sendShareLinkUrlDetailsToServer(context, referrer, SHARE_TYPE_INSTALL, 1);
        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }
}
