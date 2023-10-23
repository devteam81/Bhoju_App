package com.bhoju.app.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.bhoju.app.R;
import com.bhoju.app.util.NetworkChangeReceiver;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity implements OnLocaleChangedListener {

    PrefUtils prefUtils;
    int id, subProfileId;
    String token;
    String name;

    //static Dialog dialog;
    //private BroadcastReceiver mNetworkReceiver;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    /*public static void dialog(boolean value) {

        //showpopup if net not available

        try {
            if (value) {
                //alert.dismiss();
                dialog.dismiss();
            } else {
                //alert.show();
                dialog.show();

            }
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* // For block screenshot and screen recroder........
          getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/

        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        prefUtils = PrefUtils.getInstance(this);
        id = prefUtils.getIntValue(PrefKeys.USER_ID, -1);
        token = prefUtils.getStringValue(PrefKeys.SESSION_TOKEN,"");
        subProfileId = prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1);
        //mNetworkReceiver = new NetworkChangeReceiver();
        //registerNetworkBroadcastForNougat();
        /*if(!NetworkUtils.isNetworkConnected(this))
            Toast.makeText(this,getResources().getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show();*/

        /*dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_no_internet);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.

        params.y = 10; // Here is the param to set your dialog position. Same with params.x
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView btnOk = (TextView) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
            }
        });*/
    }

    /*private void registerNetworkBroadcastForNougat() {

        getApplicationContext().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }

    }*/

    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterNetworkChanges();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }

}
