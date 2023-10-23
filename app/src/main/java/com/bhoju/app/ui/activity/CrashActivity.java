package com.bhoju.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bhoju.app.R;

import butterknife.BindView;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class CrashActivity extends BaseActivity {

    @BindView(R.id.restartApp)
    Button restartApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crash);
        Button restartButton = findViewById(R.id.restartApp);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        try {
            final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());

            if (config == null) {
                //This should never happen - Just finish the activity to avoid a recursive crash.
                finish();
                return;
            }

            if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
                restartButton.setText(getString(R.string.restart_app));
                restartButton.setOnClickListener(v -> CustomActivityOnCrash.restartApplicationWithIntent(CrashActivity.this, i, config));
            } else {
                restartButton.setText(getString(R.string.close_app));
                restartButton.setOnClickListener(v -> CustomActivityOnCrash.closeApplication(CrashActivity.this, config));
            }
        }
        catch (Exception e) {
            restartButton.setText(getString(R.string.try_again));
        }


    }
}
