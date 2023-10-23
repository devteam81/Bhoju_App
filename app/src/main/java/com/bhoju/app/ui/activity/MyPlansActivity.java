package com.bhoju.app.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.adapter.MyPlanAdapter;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.AUTO_RENEWAL_ENABLE;
import static com.bhoju.app.network.APIConstants.APIs.CANCEL_SUBSCRIPTION;
import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;

public class MyPlansActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        MyPlanAdapter.DisplayAutoRenewal,
        OnLoadMoreVideosListener {

    private final String TAG = MyPlansActivity.class.getSimpleName();

    @BindView(R.id.autoRenewal)
    SwitchCompat autoRenewal;
    @BindView(R.id.autoRenewalLayout)
    LinearLayout autoRenewalLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.myPlansRecycler)
    RecyclerView myPlansRecycler;
    MyPlanAdapter myPlanAdapter;
    ArrayList<SubscriptionPlan> myPlans = new ArrayList<>();
    APIInterface apiInterface;
    PrefUtils prefUtils;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.noResultLayout)
    TextView noResultLayout;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager llmanager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (llmanager.findLastCompletelyVisibleItemPosition() == (myPlanAdapter.getItemCount() - 1)) {
                myPlanAdapter.showLoading();
            }
        }
    };


    private CompoundButton.OnCheckedChangeListener autoRenewCheckChangeListener
            = (buttonView, isChecked) -> {
        if (autoRenewal.isChecked()) {
            new AlertDialog.Builder(MyPlansActivity.this)
                    .setTitle(getString(R.string.autoRenewalMsg))
                    .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> enableAutoRenewal())
                    .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        rollBackAutoRenewToggle();
                    })
                    .create().show();
        } else {
            new AlertDialog.Builder(MyPlansActivity.this)
                    .setTitle(getString(R.string.cancelRenewal))
                    .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> cancelSubscription())
                    .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        rollBackAutoRenewToggle();
                    })
                    .create().show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_my_plans);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);
        autoRenewal = findViewById(R.id.autoRenewal);
        autoRenewalLayout = findViewById(R.id.autoRenewalLayout);
        swipe.setOnRefreshListener(this);
        setUpMyPlans();

        autoRenewal.setOnCheckedChangeListener(autoRenewCheckChangeListener);

    }

    private void setUpMyPlans() {
        myPlanAdapter = new MyPlanAdapter(MyPlansActivity.this, this, myPlans, this);
        myPlansRecycler.setLayoutManager(new LinearLayoutManager(this));
        myPlansRecycler.setAdapter(myPlanAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAvailablePlans(0);
    }

    protected void getAvailablePlans(int skip) {
        if (skip == 0) {
            myPlansRecycler.setOnScrollListener(scrollListener);
            UiUtils.showLoadingDialog(this);
            myPlansRecycler.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.GONE);
            autoRenewalLayout.setVisibility(View.GONE);
        }

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.SKIP, skip);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getMyPlans(APIConstants.APIs.MY_PLANS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (skip == 0) {
                    myPlans.clear();
                    if (swipe.isRefreshing()) swipe.setRefreshing(false);
                    UiUtils.hideLoadingDialog();
                }

                JSONObject myPlansResponse = null;
                try {
                    myPlansResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (myPlansResponse != null) {
                    if (myPlansResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        JSONArray plansArray = myPlansResponse.optJSONArray(Params.DATA);
                        for (int i = 0; i < plansArray.length(); i++) {
                            JSONObject planObj = plansArray.optJSONObject(i);
                            SubscriptionPlan plan = ParserUtils.parsePlan(planObj);
                            myPlans.add(plan);
                        }
                        if (plansArray.length() == 0) {
                            myPlansRecycler.removeOnScrollListener(scrollListener);
                        }
                        onDataChanged();
                    } else {
                        UiUtils.showShortToast(getApplicationContext(), myPlansResponse.optString(Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(MyPlansActivity.this);
            }
        });
    }

    private void onDataChanged() {
        myPlanAdapter.notifyDataSetChanged();
        noResultLayout.setVisibility(myPlans.isEmpty() ? View.VISIBLE : View.GONE);
        myPlansRecycler.setVisibility(myPlans.isEmpty() ? View.GONE : View.VISIBLE);
        if (myPlans.size() == 0
                || (myPlans.size() == 1 && myPlans.get(0).getOriginalAmount() == 0))
            autoRenewalLayout.setVisibility(View.GONE);
        else {
            autoRenewalLayout.setVisibility(View.VISIBLE);
            if (!myPlans.isEmpty())
                onAutoRenewUpdate(myPlans.get(0).isCancelled());
        }
    }

    @Override
    public void onRefresh() {
        onResume();
    }

    protected void enableAutoRenewal() {
        UiUtils.showLoadingDialog(MyPlansActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.autoRenewalEnable(AUTO_RENEWAL_ENABLE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject paymentObject = null;
                try {
                    paymentObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (paymentObject != null) {
                    if (paymentObject.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        Toast.makeText(MyPlansActivity.this, paymentObject.optString(Params.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        rollBackAutoRenewToggle();
                        Toast.makeText(MyPlansActivity.this, paymentObject.optString(Params.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                rollBackAutoRenewToggle();
                NetworkUtils.onApiError(MyPlansActivity.this);
            }
        });
    }

    protected void cancelSubscription() {
        UiUtils.showLoadingDialog(MyPlansActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.cancelSubscription(CANCEL_SUBSCRIPTION, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject cancelAutoRenewalResponse = null;
                try {
                    cancelAutoRenewalResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (cancelAutoRenewalResponse != null) {
                    if (cancelAutoRenewalResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        Toast.makeText(MyPlansActivity.this, cancelAutoRenewalResponse.optString(Params.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        rollBackAutoRenewToggle();
                        Toast.makeText(MyPlansActivity.this, cancelAutoRenewalResponse.optString(Params.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                rollBackAutoRenewToggle();
                NetworkUtils.onApiError(MyPlansActivity.this);
            }
        });
    }

    private void rollBackAutoRenewToggle() {
        autoRenewal.setOnCheckedChangeListener(null);
        autoRenewal.setChecked(!autoRenewal.isChecked());
        autoRenewal.setOnCheckedChangeListener(autoRenewCheckChangeListener);
    }

    public void onAutoRenewUpdate(boolean isCancelled) {
        autoRenewal.setOnCheckedChangeListener(null);
        autoRenewal.setChecked(isCancelled);
        autoRenewal.setOnCheckedChangeListener(autoRenewCheckChangeListener);
    }

    @Override
    public void dataChanged(double orginalAmt) {
        if (orginalAmt == 0) {
            autoRenewalLayout.setVisibility(View.GONE);
        } else {
            autoRenewalLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMore(int skip) {
        getAvailablePlans(skip);
    }
}
