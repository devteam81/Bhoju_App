package com.bhoju.app.ui.activity;

import static com.bhoju.app.network.APIConstants.APIs.SEE_ALL_URL;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.ui.adapter.MoreVideosAdapter;
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

public class MoreVideosActivity extends BaseActivity implements OnLoadMoreVideosListener, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = MoreVideosActivity.class.getSimpleName();

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.moreVideosRecycler)
    RecyclerView moreVideosRecycler;
    @BindView(R.id.noResultLayout)
    TextView noResultLayout;
    MoreVideosAdapter moreVideosAdapter;
    ArrayList<Video> moreVideos = new ArrayList<>();
    APIInterface apiInterface;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    Intent intent;
    String urlType = "";
    String pageType = "";
    String title = "";
    private String categoryId;
    private int subCategoryId;
    private int castNCrewId;
    private int genreId;
    private String pageUrlId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_more_videos2);
        ButterKnife.bind(this);
        swipe.setOnRefreshListener(this);
        img_back.setOnClickListener(v -> onBackPressed());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        intent = getIntent();
        txt_title.setText(intent.getExtras().getString(APIConstants.Params.TITLE));
        setUpAdapter();
    }

    public void setUpAdapter() {
        moreVideosAdapter = new MoreVideosAdapter(MoreVideosActivity.this, moreVideos);
        moreVideosAdapter.setOnLoadMoreVideosListener(this);
        moreVideosRecycler.setHasFixedSize(true);
        moreVideosRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        moreVideosRecycler.setItemAnimator(new DefaultItemAnimator());
        moreVideosRecycler.setAdapter(moreVideosAdapter);
        moreVideosRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llmanager = (LinearLayoutManager) recyclerView.getLayoutManager();
                UiUtils.log(TAG,"SiZE: SKIP: "+ llmanager.findLastCompletelyVisibleItemPosition());
                if (llmanager.findLastCompletelyVisibleItemPosition() == (moreVideosAdapter.getItemCount() - 1)) {
                    UiUtils.log(TAG,"SiZE: INSIDE: "+ moreVideosAdapter.getItemCount());
                    moreVideosAdapter.showLoading();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(intent.getExtras() != null)
        {
            urlType = intent.getStringExtra(APIConstants.Params.URL_TYPE);
            pageType = intent.getStringExtra(APIConstants.Params.PAGE_TYPE);
            pageUrlId = intent.getStringExtra(APIConstants.Params.URL_TYPE_ID);
            categoryId = intent.getStringExtra(APIConstants.Params.CATEGORY_ID);
            subCategoryId = intent.getIntExtra(APIConstants.Params.SUB_CATEGORY_ID, 0);
            castNCrewId = intent.getIntExtra(APIConstants.Params.CAST_CREW_ID, 0);
            genreId = intent.getIntExtra(APIConstants.Params.GENRE_ID, 0);
            title = intent.getStringExtra(APIConstants.Params.TITLE);
            txt_title.setText(title);

            getMoreVideosFromBackend(urlType,pageType,pageUrlId,categoryId,subCategoryId,castNCrewId,genreId,0);
        }
    }

    protected void getMoreVideosFromBackend(String urlType, String pageType,String urlPageId, String categoryId, int subCateId,
                                            int castNCrewId, int genreId, int skip) {
        PrefUtils prefUtils = PrefUtils.getInstance(MoreVideosActivity.this);

        UiUtils.showLoadingDialog(MoreVideosActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(APIConstants.Params.URL_TYPE, urlType);
        params.put(APIConstants.Params.PAGE_TYPE, pageType);
        params.put(APIConstants.Params.URL_TYPE_ID, urlPageId);
        params.put(APIConstants.Params.CATEGORY_ID, categoryId);
        params.put(APIConstants.Params.SUB_CATEGORY_ID, subCateId);
        params.put(APIConstants.Params.CAST_CREW_ID, castNCrewId);
        params.put(APIConstants.Params.GENRE_ID, genreId);
        params.put(APIConstants.Params.SKIP, skip);
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(APIConstants.Params.IP, CURRENT_IP);
        params.put(APIConstants.Params.APPVERSION, BuildConfig.VERSION_NAME);
        params.put(VERSION_CODE, BuildConfig.VERSION_CODE);

        Call<String> call = apiInterface.moreVideosList(SEE_ALL_URL, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (skip == 0) moreVideos.clear();
                if (swipe.isRefreshing()) swipe.setRefreshing(false);
                UiUtils.hideLoadingDialog();
                if (skip != 0) {
                    moreVideosAdapter.dismissLoading();
                }
                JSONObject moreVideosResponse = null;
                try {
                    moreVideosResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (moreVideosResponse != null) {
                    if (moreVideosResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONArray wishListArray = moreVideosResponse.optJSONArray(APIConstants.Params.DATA);
                        for (int i = 0; i < wishListArray.length(); i++) {
                            JSONObject object = wishListArray.optJSONObject(i);
                            Video video = new Video();
                            video.setAdminVideoId(object.optInt(APIConstants.Params.ADMIN_VIDEO_ID));
                            video.setTitle(object.optString(APIConstants.Params.TITLE));
                            video.setCategoryId(object.optInt(APIConstants.Params.CATEGORY_ID));
                            video.setGenreId(object.optInt(APIConstants.Params.GENRE_ID));
                            video.setDefaultImage(object.optString(APIConstants.Params.DEFAULT_IMAGE));
                            video.setThumbNailUrl(object.optString(APIConstants.Params.MOBILE_IMAGE));
                            video.setBrowseImage(object.optString(APIConstants.Params.BROWSE_IMAGE));
                            video.setA_record(object.optInt(APIConstants.Params.A_RECORD));
                            moreVideos.add(video);
                        }

                        onDataChanged();
                    } else {
                        UiUtils.showShortToast(MoreVideosActivity.this, moreVideosResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(MoreVideosActivity.this);
            }
        });
    }

    private void onDataChanged() {
        moreVideosAdapter.notifyDataSetChanged();
        if (moreVideos.isEmpty()) {
            noResultLayout.setVisibility(View.VISIBLE);
            moreVideosRecycler.setVisibility(View.GONE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            moreVideosRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        onResume();
    }

    @Override
    public void onLoadMore(int skip) {
        moreVideosAdapter.showLoading();
        getMoreVideosFromBackend(urlType,pageType,pageUrlId,categoryId,subCategoryId,castNCrewId,genreId,skip);
    }
}
