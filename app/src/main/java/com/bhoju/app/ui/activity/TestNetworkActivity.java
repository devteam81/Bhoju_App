package com.bhoju.app.ui.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.*;

public class TestNetworkActivity extends BaseActivity {

    private final String TAG = TestNetworkActivity.class.getSimpleName();

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.checkNetworkImage)
    ImageView checkNetworkImage;
    @BindView(R.id.testStatus)
    TextView testStatus;
    @BindView(R.id.errorDescription)
    TextView errorDescription;
    @BindView(R.id.testingProgress)
    ProgressBar testingProgress;
    @BindView(R.id.testBtn)
    Button testBtn;

    Drawable successDrawable;
    Drawable failedDrawable;
    Drawable emptyDrawable;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_network);
        ButterKnife.bind(this);
        img_back.setOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        successDrawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_tick_mark,null);
        failedDrawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_cross,null);
        emptyDrawable = ResourcesCompat.getDrawable(getResources(),android.R.color.transparent,null);

        //testDisplayArea.setVisibility(View.GONE);
        testingProgress.setVisibility(View.GONE);
        checkNetworkImage.setVisibility(View.VISIBLE);

        testStatus.setVisibility(View.VISIBLE);
        testStatus.setText(getString(R.string.check_your_network_title));
        errorDescription.setVisibility(View.VISIBLE);
        errorDescription.setText(getResources().getString(R.string.test_failed_description));

        checkWidth();
    }


    @OnClick(R.id.testBtn)
    public void onViewClicked() {
        //testDisplayArea.setVisibility(View.VISIBLE);
        testingProgress.setVisibility(View.VISIBLE);
        //checkNetworkImage.setVisibility(View.GONE);
        //ourServer.setCompoundDrawablesRelativeWithIntrinsicBounds(emptyDrawable, null, null, null);
        //internetServer.setCompoundDrawablesRelativeWithIntrinsicBounds(emptyDrawable, null, null, null);

        PrefUtils preferences = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, preferences.getIntValue(PrefKeys.USER_ID, 1));
        params.put(APIConstants.Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));

        Call<String> call = apiInterface.getAppConfigs(APIConstants.APIs.GET_APP_CONFIG, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                testingProgress.setVisibility(View.GONE);
                testBtn.setText(getString(R.string.test_again));
                JSONObject networkCheckResponse = null;
                try {
                    networkCheckResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (networkCheckResponse != null) {
                    if (networkCheckResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        updateViews(false, "Network Check Successful");
                        checkNetworkImage.setImageDrawable(successDrawable);
                        //ourServer.setCompoundDrawablesRelativeWithIntrinsicBounds(successDrawable, null, null, null);
                        //internetServer.setCompoundDrawablesRelativeWithIntrinsicBounds(successDrawable, null, null, null);
                    } else {
                        updateViews(false, "Network Check Successful");
                        checkNetworkImage.setImageDrawable(successDrawable);
                       // ourServer.setCompoundDrawablesRelativeWithIntrinsicBounds(failedDrawable, null, null, null);
                        //internetServer.setCompoundDrawablesRelativeWithIntrinsicBounds(successDrawable, null, null, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                testingProgress.setVisibility(View.GONE);
                updateViews(true, "Network Test Failed");
                checkNetworkImage.setImageDrawable(failedDrawable);
                //ourServer.setCompoundDrawablesRelativeWithIntrinsicBounds(failedDrawable, null, null, null);
                //internetServer.setCompoundDrawablesRelativeWithIntrinsicBounds(failedDrawable, null, null, null);
            }
        });
    }

    private void updateViews(boolean isSuccess, String message) {
        //errorDescription.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
        errorDescription.setTextColor(isSuccess ? getResources().getColor(R.color.cross) :getResources().getColor(R.color.tick));
        //testStatus.setText(isSuccess ? getString(R.string.network_check_success) : getString(R.string.network_check_failed));
        testStatus.setText(getString(R.string.internet_connection));
        //if (!isSuccess)
            errorDescription.setText(message);
    }

    private void checkWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        UiUtils.log(TAG, "Width: "+ width);
        UiUtils.log(TAG, "Width dp: "+ dp);

        if(dp<400)
        {
            testBtn.setPadding(0,30,10,0);
        }
        else if(dp<500)
        {
            testBtn.setPadding(0,27,17,0);
        }
        else if(dp>800)
        {
            testBtn.setPadding(0,27,20,0);
        }
    }
}
