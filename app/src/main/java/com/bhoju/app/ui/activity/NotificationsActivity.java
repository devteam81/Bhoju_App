package com.bhoju.app.ui.activity;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.NotificationItem;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.ui.adapter.BellNotificationAdapter;
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

import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;

public class NotificationsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreVideosListener {

    private final String TAG = NotificationsActivity.class.getSimpleName();

    @BindView(R.id.notificationRecycler)
    RecyclerView notificationRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    APIInterface apiInterface;
    PrefUtils prefUtils ;
    BellNotificationAdapter notificationAdapter;
    ArrayList<NotificationItem> notifications = new ArrayList<>();
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.noResultLayout)
    TextView noResultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);
        swipe.setOnRefreshListener(this);
        setUpNotifications();
    }

    private void setUpNotifications() {
        notificationAdapter = new BellNotificationAdapter(this, notifications);
        notificationRecycler.setHasFixedSize(true);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationRecycler.setItemAnimator(new DefaultItemAnimator());
        notificationRecycler.setAdapter(notificationAdapter);
        notificationAdapter.setOnLoadMoreVideosListener(this);
        notificationRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llmanager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llmanager.findLastCompletelyVisibleItemPosition() == (notificationAdapter.getItemCount() - 1)) {
                    notificationAdapter.showLoading();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotifications(0);
    }

    protected void getNotifications(int skip) {
        if (skip == 0)
            swipe.setRefreshing(true);

        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, id);
        params.put(APIConstants.Params.TOKEN, token);
        params.put(APIConstants.Params.SUB_PROFILE_ID, subProfileId);
        params.put(APIConstants.Params.SKIP, skip);
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(APIConstants.Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getNotifications(APIConstants.APIs.NOTIFICATIONS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (skip == 0) notifications.clear();
                if (swipe.isRefreshing()) swipe.setRefreshing(false);

                JSONObject categoryListObject = null;
                try {
                    categoryListObject = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (categoryListObject != null) {
                    if (categoryListObject.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONArray categoryArray = categoryListObject.optJSONArray(APIConstants.Params.DATA);
                        if (skip != 0) {
                            notificationAdapter.dismissLoading();
                        }
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject object = categoryArray.optJSONObject(i);
                            NotificationItem notification = new NotificationItem();
                            notification.setAdminVideoId(object.optInt(APIConstants.Params.ADMIN_VIDEO_ID));
                            notification.setTitle(object.optString(APIConstants.Params.TITLE));
                            notification.setImage(object.optString(APIConstants.Params.IMG));
                            notification.setTime(object.optString(APIConstants.Params.TIME));
                            notifications.add(notification);
                        }
                        onDataChanged();
                    } else {
                        UiUtils.showShortToast(NotificationsActivity.this, categoryListObject.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(NotificationsActivity.this);
            }
        });
    }

    private void onDataChanged() {
        notificationAdapter.notifyDataSetChanged();
        noResultLayout.setVisibility(notifications.isEmpty() ? View.VISIBLE : View.GONE);
        notificationRecycler.setVisibility(notifications.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        onResume();
    }

    @Override
    public void onLoadMore(int skip) {
        getNotifications(0);
    }
}
