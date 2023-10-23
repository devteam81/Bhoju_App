package com.bhoju.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object o : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) o);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().replaceAll("\\D", "");

                    //message = message.substring(0, message.length()-1);
                    UiUtils.log("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", message);
                    myIntent.putExtra("number", senderNum);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            UiUtils.log("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}
