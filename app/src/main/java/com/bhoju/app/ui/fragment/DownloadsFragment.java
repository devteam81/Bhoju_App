package com.bhoju.app.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.AppSettingsActivity;
import com.bhoju.app.ui.adapter.OfflineVideoAdapter;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.download.DownloadUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.model.Video.DownloadStatus.DOWNLOAD_COMPLETED;
import static com.bhoju.app.model.Video.DownloadStatus.DOWNLOAD_PROGRESS;
import static com.bhoju.app.model.Video.DownloadStatus.SHOW_DOWNLOAD;
import static com.bhoju.app.network.APIConstants.APIs.DOWNLOADED_VIDEOS;
import static com.bhoju.app.network.APIConstants.APIs.UPDATE_DOWNLOAD_EXPIRY;
import static com.bhoju.app.network.APIConstants.Constants.DOWNLOADED_PROGRESS;
import static com.bhoju.app.network.APIConstants.Constants.DOWNLOAD_STATUS;
import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.ui.activity.MainActivity.connectivityReceiver;
import static com.bhoju.app.ui.activity.VideoPageActivity.ACTION_DOWNLOAD_UPDATE;
import static com.bhoju.app.util.NetworkUtils.isNetworkConnected;
import static com.bhoju.app.util.download.DownloadUtils.getEpisodeFileName;
import static com.bhoju.app.util.download.DownloadUtils.getFileExpiry;
import static com.bhoju.app.util.download.DownloadUtils.getFileName;
import static com.bhoju.app.util.download.DownloadUtils.getVideoDuration;
import static com.bhoju.app.util.download.DownloadUtils.getVideoId;
import static com.bhoju.app.util.download.DownloadUtils.isOfflineVideoExisting;
import static com.bhoju.app.util.sharedpref.Utils.getUserLoginStatus;

public class DownloadsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OfflineVideoAdapter.OfflineVideoInterface {

    private final String TAG = DownloadsFragment.class.getSimpleName();

    @BindView(R.id.offlineVideos)
    RecyclerView offlineVideosRecycler;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.noOfflineVideos)
    TextView noOfflineVideos;
    @BindView(R.id.img_loader)
    ImageView img_loader;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.searchView)
    EditText searchView;
    @BindView(R.id.clearSearchText)
    ImageView clearSearchText;

    @BindView(R.id.rl_download_validity)
    RelativeLayout rl_download_validity;

    Context activity;
    Unbinder unbinder;
    APIInterface apiInterface;
    OfflineVideoAdapter offlineVideoAdapter;
    ArrayList<Video> offLineVideos = new ArrayList<>();

    String searchText ="";

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                int downloadStatus = intent.getIntExtra(DOWNLOAD_STATUS, 1);
                switch(downloadStatus)
                {
                    case 1:
                    case 2:
                        for (int i = 0; i < offlineVideoAdapter.getItemCount(); i++) {
                            if (offLineVideos.get(i).getAdminVideoId() == intent.getIntExtra(ADMIN_VIDEO_ID, 0)) {
                                offLineVideos.get(i).setDownloadedStatus(DOWNLOAD_PROGRESS);
                                try {
                                    UiUtils.log(TAG,"Offline: "+intent.getStringExtra(DOWNLOADED_PROGRESS));
                                    offLineVideos.get(i).setDownloadedPercentage(intent.getStringExtra(DOWNLOADED_PROGRESS));
                                }catch (Exception e)
                                {

                                }
                                break;
                            }
                        }
                        break;
                    case 3:
                        for (int i = 0; i < offlineVideoAdapter.getItemCount(); i++) {
                            if (offLineVideos.get(i).getAdminVideoId() == intent.getIntExtra(ADMIN_VIDEO_ID, 0)) {
                                offLineVideos.get(i).setDownloadedStatus(SHOW_DOWNLOAD);
                                break;
                            }
                        }
                        break;
                    case 4:
                        for (int i = 0; i < offlineVideoAdapter.getItemCount(); i++) {
                            if (offLineVideos.get(i).getAdminVideoId() == intent.getIntExtra(ADMIN_VIDEO_ID, 0)) {
                                offLineVideos.get(i).setDownloadedStatus(DOWNLOAD_COMPLETED);
                                offLineVideos.get(i).setVideoUrl(DownloadUtils.getVideoPath(activity,
                                        offLineVideos.get(i).getAdminVideoId(),
                                        offLineVideos.get(i).getVideoUrl()));
                                break;
                            }
                        }
                        break;
                    case 5:
                    case 6:
                        for (int i = 0; i < offlineVideoAdapter.getItemCount(); i++) {
                            if (offLineVideos.get(i).getAdminVideoId() == intent.getIntExtra(ADMIN_VIDEO_ID, 0)) {
                                offLineVideos.remove(i);
                                break;
                            }
                        }
                        break;
                }
                offlineVideoAdapter.notifyDataSetChanged();
            }catch (Exception e)
            {

            }
        }
    };

    private void setVideoAdapter() {
        offlineVideoAdapter = new OfflineVideoAdapter(activity, offLineVideos);
        offlineVideoAdapter.setDownloadVideoListener(this);

        offlineVideosRecycler.setVisibility(offLineVideos.size() > 0 ? View.VISIBLE : View.GONE);
        noOfflineVideos.setVisibility(offLineVideos.size() > 0 ? View.GONE : View.VISIBLE);
        offlineVideosRecycler.setLayoutManager(new LinearLayoutManager(activity));
        offlineVideosRecycler.setAdapter(offlineVideoAdapter);

        swipe.setRefreshing(false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_UPDATE);
        getActivity().registerReceiver(receiver, filter);
        IntentFilter filterInternet = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(connectivityReceiver, filterInternet);
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onStop() {
        try {
            getActivity().unregisterReceiver(connectivityReceiver);
        }catch (Exception e)
        {}
        super.onStop();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_downloads, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        swipe.setOnRefreshListener(this);

        if (PrefUtils.getInstance(activity).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            ll_search.setVisibility(View.VISIBLE);
            rl_download_validity.setVisibility(View.VISIBLE);
        }
        else {
            ll_search.setVisibility(View.GONE);
            rl_download_validity.setVisibility(View.GONE);
        }

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString();
                if (key.length() == 0) {
                    //searchAndUpdateViews("");
                    searchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_white_36dp, 0, 0, 0);
                    clearSearchText.setVisibility(View.GONE);
                    searchText = key;
                    noOfflineVideos.setVisibility(View.VISIBLE);

                } else {
                    noOfflineVideos.setVisibility(View.GONE);
                    searchText = key;
                    //searchAndUpdateViews(key);
                    searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    clearSearchText.setVisibility(View.VISIBLE);
                }
                getAllOfflineVideos();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clearSearchText.setOnClickListener(v -> searchView.setText(""));
        rl_download_validity.setOnClickListener(v -> showDownloadValidityPopup());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img_loader.setBackgroundResource(R.drawable.anim_loader);
        if (PrefUtils.getInstance(activity).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            loadOfflineVideos();
        }else {
            noOfflineVideos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getAllOfflineVideos();
    }

    private void loadOfflineVideos() {
       /* IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_UPDATE);
        getActivity().registerReceiver(receiver, filter);*/
        if(PrefUtils.getInstance(getActivity()).getIntValue(PrefKeys.USER_ID, -1) > 1)
            getUserLoginStatus(getActivity());
        if (isNetworkConnected(activity)) {
            getAllOfflineVideos();
        } else {
            getAllVideos();
        }
    }

    private void getAllOfflineVideos() {
        //loadingVideos.setVisibility(View.VISIBLE);
        showLoader();
        noOfflineVideos.setVisibility(View.GONE);
        offlineVideosRecycler.setVisibility(View.GONE);
        PrefUtils prefUtils = PrefUtils.getInstance(getActivity());
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(APIConstants.Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.downloadedVideos(DOWNLOADED_VIDEOS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    swipe.setRefreshing(false);
                    if (isAdded()) {
                        //loadingVideos.setVisibility(View.GONE);
                        img_loader.setVisibility(View.GONE);
                        JSONObject offlineVideosResponse = null;
                        try {
                            offlineVideosResponse = new JSONObject(response.body());
                        } catch (Exception e) {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                        if (offlineVideosResponse != null) {
                            if (offlineVideosResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                                JSONArray videos = offlineVideosResponse.optJSONArray(APIConstants.Params.DATA);
                                offLineVideos.clear();
                                for (int i = 0; i < videos.length(); i++) {
                                    try {
                                        JSONObject videoItem = videos.getJSONObject(i);
                                        Video videoItemFromWeb = ParserUtils.parseVideoData(videoItem);
                                        //if (videoItemFromWeb.getNumDaysToExpire() > 0) {
                                        //if (isOfflineVideoExisting(activity, videoItemFromWeb.getAdminVideoId(), videoItemFromWeb.getNumDaysToExpire())) {

                                            videoItemFromWeb.setVideoUrl(DownloadUtils.getVideoPath(activity,
                                                    videoItemFromWeb.getAdminVideoId(),
                                                    videoItemFromWeb.getVideoUrl()));
                                            if (!searchText.equalsIgnoreCase("")) {
                                                if (videoItemFromWeb.getTitle().toLowerCase().contains(searchText.toLowerCase())
                                                        || videoItemFromWeb.getEpTitle().toLowerCase().contains(searchText.toLowerCase()))
                                                    offLineVideos.add(videoItemFromWeb);
                                                UiUtils.log("getAllOfflineVideos", "Search Video-->" + searchText);
                                            } else {
                                                offLineVideos.add(videoItemFromWeb);
                                                UiUtils.log("getAllOfflineVideos", "Search-->" + searchText);
                                            }
                                        //}
                                        //}
                                    } catch (JSONException e) {
                                        UiUtils.log(TAG, Log.getStackTraceString(e));
                                    }
                                }

                                setVideoAdapter();
                            } else {
                                UiUtils.showShortToast(activity, offlineVideosResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                            }
                        }
                    }
                }catch (Exception e)
                {
                    UiUtils.log(TAG,"Error: "+ e.getMessage());
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                try {
                    NetworkUtils.onApiError(activity);
                    //loadingVideos.setVisibility(View.GONE);
                    img_loader.setVisibility(View.GONE);
                    swipe.setRefreshing(false);
                }catch (Exception e)
                {
                    UiUtils.log(TAG,"Error: "+ e.getMessage());
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }

            }
        });
    }

    private void getAllVideos() {
        File dir = new File(activity.getExternalFilesDir(null).getPath() + "/" + PrefUtils.getInstance(activity).getIntValue(PrefKeys.USER_ID, 0));
        File[] allFiles = dir.listFiles();
        offLineVideos.clear();
        if (allFiles != null && allFiles.length > 0) {
            for (File file : allFiles) {
                if (!file.isDirectory()) {
                    String filePath = file.getPath();
                    String extension = filePath.substring(filePath.length() - 4);
                    if (extension != null && !extension.equals("") && extension.equals(".mp4")) {
                        Video item = new Video();
                        item.setAdminVideoId(getVideoId(file.getName()));
                        item.setTitle(getFileName(file.getName()));
                        item.setEpTitle(getEpisodeFileName(file.getName()));
                        item.setVideoUrl(file.getAbsolutePath());
                        int daysSinceDownloaded = getFileExpiry(file.getAbsolutePath());
                        UiUtils.log(TAG,"daysSinceDownloaded:" + daysSinceDownloaded);
                        int expiryNumDays;
                        try {
                            UiUtils.log(TAG,"File Name:" + file.getName());
                            UiUtils.log(TAG,"expiryNumDays:" + file.getName().split("\\.")[1]);
                            expiryNumDays = Integer.parseInt(file.getName().split("\\.")[1]);
                        } catch (Exception e) {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                            expiryNumDays = 0;
                        }
                        if (expiryNumDays - daysSinceDownloaded < 0) {
                            continue;
                        } else {
                            item.setExpired(false);
//                            item.setNumDaysToExpire(expiryNumDays - daysSinceDownloaded);
                        }
                        item.setNumDaysToExpire(expiryNumDays - daysSinceDownloaded);

                        String duration;
                        try {
                            duration = getVideoDuration(activity, file.getAbsolutePath());
                        } catch (Exception e) {
                            duration = "--:--:--";
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                        item.setDuration(duration);
                        if(!searchText.equalsIgnoreCase("")) {
                            UiUtils.log(TAG,"Search Found-->"+searchText);
                            if (item.getTitle().toLowerCase().contains(searchText.toLowerCase())
                                    || item.getEpTitle().toLowerCase().contains(searchText.toLowerCase()))
                                offLineVideos.add(item);
                        }else
                        {
                            offLineVideos.add(item);
                            UiUtils.log(TAG,"Search-->"+searchText);
                        }
                        setVideoAdapter();
                    }
                }
            }
        }
    }

    @Override
    public void onVideoDeleted(boolean isEmpty) {
        offlineVideosRecycler.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        noOfflineVideos.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    //Download
    /*private void downloadVideo(String downloadUrl) {
        Toast.makeText(getActivity(), downloadUrl, Toast.LENGTH_SHORT).show();
        NetworkUtils.downloadVideo(this, video.getAdminVideoId(), video.getTitle(), downloadUrl);
        //downloadView.setVisibility(View.GONE);
        //downloadingView.setVisibility(View.VISIBLE);
    }*/


    public void showDownloadValidityPopup()
    {

        Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.dialog_download_validity_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText ed_validity_days = dialog.findViewById(R.id.ed_validity_days);
        TextView txtSave = dialog.findViewById(R.id.txtSave);
        txtSave.setOnClickListener((View v) -> {

            if(ed_validity_days.getText().toString().trim().length()>0 && !ed_validity_days.getText().toString().equals("0")) {
                dialog.dismiss();
                updateDownloadExpiry(ed_validity_days.getText().toString());
            }
            else
                Toast.makeText(activity,getResources().getString(R.string.enter_valid_days),Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setGravity(Gravity.CENTER);
    }

    protected void updateDownloadExpiry(String days) {
        UiUtils.showLoadingDialog(activity);
        PrefUtils prefUtils = PrefUtils.getInstance(activity);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.ID,  prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.DOWNLOAD_EXPIRY, days);

        Call<String> call = apiInterface.updateDownloadExpiry(UPDATE_DOWNLOAD_EXPIRY, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject downloadExpiryUpdateResponse = null;
                try {
                    downloadExpiryUpdateResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (downloadExpiryUpdateResponse != null) {
                    if (downloadExpiryUpdateResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.showShortToast(activity, downloadExpiryUpdateResponse.optString(APIConstants.Params.MESSAGE));
                    } else {
                        UiUtils.showShortToast(activity, downloadExpiryUpdateResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(activity);
            }
        });
    }

    public void showLoader() {
        if (!img_loader.isShown()) {
            img_loader.setVisibility(View.VISIBLE);
            AnimationDrawable rocketAnimation = (AnimationDrawable) img_loader.getBackground();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    rocketAnimation.start();
                }
            }).start();
        }
    }

}
