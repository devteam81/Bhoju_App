package com.bhoju.app.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.REFERRAL_CODE;

public class ReferralActivity extends BaseActivity {

    private final String TAG = ReferralActivity.class.getSimpleName();

    APIInterface apiInterface;
    PrefUtils prefUtils;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.earnings)
    TextView earnings;
    @BindView(R.id.remaingBalance)
    TextView remaingBalance;
    @BindView(R.id.referralCode)
    TextView referralCode;
    @BindView(R.id.copyCode)
    TextView copyCode;
    @BindView(R.id.shareApp)
    Button shareApp;
    String shareMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refer_and_earn);
        ButterKnife.bind(this);
        prefUtils = PrefUtils.getInstance(getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onResume() {
        super.onResume();
        referralAndEarningsFromBackend();
    }

    protected void referralAndEarningsFromBackend() {
        UiUtils.showLoadingDialog(this);

        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));

        Call<String> call = apiInterface.getReferralCode(REFERRAL_CODE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject referralResponse = null;
                try {
                    referralResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (referralResponse != null)
                    if (referralResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONObject data = referralResponse.optJSONObject(APIConstants.Params.DATA);
                        earnings.setText(String.format("%s%s", data.optString(APIConstants.Params.CURRENCY), data.optString(APIConstants.Params.AMOUNT_TOTAL)));
                        remaingBalance.setText(String.format("%s %s%s",getString(R.string.yourRemaningBal), data.optString(APIConstants.Params.CURRENCY), data.optString(APIConstants.Params.REMAINING)));
                        referralCode.setText(data.optString(APIConstants.Params.REFERRAL_CODE));
                        shareMsg = data.optString(APIConstants.Params.SHARE_MESSAGE);
                    } else {
                        Toast.makeText(ReferralActivity.this, referralResponse.optString(APIConstants.Params.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(ReferralActivity.this);
            }
        });
    }

    @OnClick({R.id.copyCode, R.id.shareApp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.copyCode:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", referralCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, getString(R.string.copiedToClipboard), Toast.LENGTH_SHORT).show();
                break;
            case R.id.shareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareMsg + getString(R.string.playstore_link));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
    }
}
