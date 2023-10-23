package com.bhoju.app.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.AppUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.Params.DATA;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.PARENT_ID;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;

public class YouTubePlayerActivity /*extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener*/ {

    /*public static final String VIDEO_URL = "videoUrl";
    public static final String VIDEO_ID = "videoId";
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @BindView(R.id.youtube_view)
    YouTubePlayerView youTubePlayer;
    YouTubePlayer youTubePlayerView;
    private String videoUrl = "";
    private int videoId;
    private MyPlayerStateChangeListener playerStateChangeListener;
    boolean isPlayNextVideo;
    int nextVideoId;
    APIInterface apiInterface;
    PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_player);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            playerStateChangeListener = new MyPlayerStateChangeListener();
            videoUrl = intent.getStringExtra(YouTubePlayerActivity.VIDEO_URL);
            videoId = intent.getIntExtra(YouTubePlayerActivity.VIDEO_ID, 0);
            youTubePlayer.initialize(getString(R.string.youtube_api_key), this);
            isPlayNextVideo = false;
        } else {
            UiUtils.showShortToast(this, getString(R.string.something_went_wrong));
            finish();
        }
        //loadVideoData(videoId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(R.string.youtube_api_key), this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayer;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayerView = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setShowFullscreenButton(false);
        //youTubePlayer.loadVideo(AppUtils.getYouTubeIdFromUrl("https://youtu.be/nwTatevEkT0"));
        youTubePlayer.loadVideo(AppUtils.getYouTubeIdFromUrl(videoUrl));
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, getString(R.string.failed_initialize), Toast.LENGTH_LONG).show();
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addVideoToHistory(int adminVideoId) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        Call<String> call = apiInterface.addToHistory(
                prefUtils.getIntValue(PrefKeys.USER_ID, -1)
                , prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "")
                , prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0)
                , adminVideoId
                , prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject addToHistoryResponse = null;
                try {
                    addToHistoryResponse = new JSONObject(response.body());
                } catch (JSONException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (addToHistoryResponse != null) {
                    if (addToHistoryResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        UiUtils.log("TAG",adminVideoId+ getString(R.string.added_history));
                    } else {
                        UiUtils.log("TAG",adminVideoId+ getString(R.string.couldnot_added));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(YouTubePlayerActivity.this);
            }
        });
    }

    private void onPPVCompleted(int adminVideoId) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefUtils prefUtils = PrefUtils.getInstance(this);
        Call<String> call = apiInterface.ppvEnd(
                prefUtils.getIntValue(PrefKeys.USER_ID, -1)
                , prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "")
                , adminVideoId
                , prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject addToHistoryResponse = null;
                try {
                    addToHistoryResponse = new JSONObject(response.body());
                } catch (JSONException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (addToHistoryResponse != null) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(YouTubePlayerActivity.this);
            }
        });
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {
            addVideoToHistory(videoId);
            onPPVCompleted(videoId);
            isPlayNextVideo = true;
            //loadVideoData(nextVideoId);
        }
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    }

    private void loadVideoData(int adminVideoId) {
        Call<String> call = apiInterface.getVideoData(prefUtils.getIntValue(PrefKeys.USER_ID, 0), prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""),
                prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0), adminVideoId,-1, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject videoResponse = null;
                try {
                    videoResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (videoResponse != null) {
                    if (videoResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        JSONObject data = videoResponse.optJSONObject(DATA);
                        nextVideoId = data.optInt(APIConstants.Params.NEXT_VIDEO_ID);
                        videoUrl = data.optString(APIConstants.Params.VIDEO_URL);
                        videoId = data.optInt(APIConstants.Params.ADMIN_VIDEO_ID);

                        if(isPlayNextVideo)
                            youTubePlayerView.loadVideo(AppUtils.getYouTubeIdFromUrl(videoUrl));
                    } else {
//                        UiUtils.showShortToast(YouTubePlayerActivity.this, videoResponse.optString(ERROR_MESSAGE));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                finish();
                NetworkUtils.onApiError(YouTubePlayerActivity.this);
            }
        });
    }*/

}


