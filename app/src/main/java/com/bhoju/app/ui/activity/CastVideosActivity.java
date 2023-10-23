package com.bhoju.app.ui.activity;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.adapter.VideoListAdapter;
import com.bhoju.app.util.NetworkUtils;
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

import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.ui.adapter.VideoListAdapter.*;

public class CastVideosActivity extends BaseActivity implements OnLoadMoreVideosListener, SwipeRefreshLayout.OnRefreshListener, OnDataChangedListener {

    private final String TAG = CastVideosActivity.class.getSimpleName();
    public static final String CAST_CREW_ID = "castCrewId";
    public static final String CAST_CREW_NAME = "castCrewName";

    @BindView(R.id.castRecycler)
    RecyclerView castVideosRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.noResultLayout)
    TextView noResultLayout;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    APIInterface apiInterface;
    PrefUtils prefUtils ;
    VideoListAdapter castVideosAdapter;
    ArrayList<Video> castVideos = new ArrayList<>();
    private int castCrewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cast_videos);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        swipe.setOnRefreshListener(this);

        if (getIntent() != null) {
            castCrewId = getIntent().getIntExtra(CAST_CREW_ID, 0);
            String castCrewName = getIntent().getStringExtra(CAST_CREW_NAME);
            toolbar.setTitle("Videos of: " + castCrewName);
            setUpCastVideos();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCastCrewVideos(0);
    }

    private void setUpCastVideos() {
        castVideosRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager
                .VERTICAL, false));
        castVideosAdapter = new VideoListAdapter(this, -1, castVideos, VideoListType.TYPE_OTHERS, this);
        castVideosRecycler.setAdapter(castVideosAdapter);
        castVideosRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llmanager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llmanager.findLastCompletelyVisibleItemPosition() == (castVideosAdapter.getItemCount() - 1)) {
                    castVideosAdapter.showLoading();

                }
            }
        });
    }

    protected void getCastCrewVideos(int skip) {
        if (skip == 0)
            swipe.setRefreshing(true);

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, id);
        params.put(Params.TOKEN, token);
        params.put(Params.SUB_PROFILE_ID, subProfileId);
        params.put(Params.CAST_CREW_ID, castCrewId);
        params.put(Params.DEVICE_TYPE, APIConstants.Constants.ANDROID);
        params.put(Params.SKIP, skip);
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.getCastVideos(APIConstants.APIs.CAST_VIDEOS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (skip == 0) castVideos.clear();
                if (swipe.isRefreshing()) swipe.setRefreshing(false);

                JSONObject castVideoResponse = null;
                try {
                    castVideoResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (castVideoResponse != null) {
                    if (castVideoResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        if (skip != 0) {
                            castVideosAdapter.dismissLoading();
                        }
                        JSONArray spamListArray = castVideoResponse.optJSONArray(APIConstants.Params.DATA);
                        for (int i = 0; i < spamListArray.length(); i++) {
                            JSONObject object = spamListArray.optJSONObject(i);
                            Video video = new Video();
                            video.setAdminVideoId(object.optInt(APIConstants.Params.ADMIN_VIDEO_ID));
                            video.setTitle(object.optString(APIConstants.Params.TITLE));
                            video.setThumbNailUrl(object.optString(APIConstants.Params.DEFAULT_IMAGE));
                            video.setDescription(object.optString(APIConstants.Params.DESCRIPTION));
                            castVideos.add(video);
                        }
                        onDataChanged();
                    } else {
                        UiUtils.showShortToast(CastVideosActivity.this, castVideoResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (swipe.isRefreshing()) swipe.setRefreshing(false);
                NetworkUtils.onApiError(CastVideosActivity.this);
            }
        });
    }

    @Override
    public void onLoadMore(int skip) {
        getCastCrewVideos(skip);
    }

    @Override
    public void onRefresh() {
        onResume();
    }

    @Override
    public void onDataChanged() {
        castVideosAdapter.notifyDataSetChanged();
        noResultLayout.setVisibility(castVideos.isEmpty() ? View.VISIBLE : View.GONE);
        castVideosRecycler.setVisibility(castVideos.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
