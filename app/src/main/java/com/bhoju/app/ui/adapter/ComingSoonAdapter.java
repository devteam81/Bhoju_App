package com.bhoju.app.ui.adapter;

import static com.bhoju.app.ui.activity.MainActivity.COOKIEKEYPAIR;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEPOLICY;
import static com.bhoju.app.ui.activity.MainActivity.COOKIESIGNATURE;
import static com.bhoju.app.util.ParserUtils.getFileExtension;
import static com.bhoju.app.util.UiUtils.animationObject;
import static com.bhoju.app.util.UiUtils.checkString;
import static com.bhoju.app.util.sharedpref.Utils.getTime;
import static com.bhoju.app.util.sharedpref.Utils.getUserLoginStatus;
import static com.google.android.exoplayer2.util.Util.getUserAgent;
import static org.greenrobot.eventbus.EventBus.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.model.comingSoon;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.FullScreenVideoActivity;
import com.bhoju.app.ui.activity.NewLoginActivity;
import com.bhoju.app.ui.fragment.ComingSoonFragment;
import com.bhoju.app.util.ResponsivenessUtils;
import com.bhoju.app.util.TrackSelectionDialog;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.exoPlayer.SoundProgressChangeListner;
import com.bhoju.app.util.exoPlayer.SoundView;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.hls.HlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.collect.ImmutableList;
import com.skydoves.elasticviews.ElasticAnimation;
import com.skydoves.elasticviews.ElasticFinishListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ComingSoonAdapter extends RecyclerView.Adapter<ComingSoonAdapter.ViewHolder> implements DialogInterface.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    boolean abc = false;
    Context context;
    Object user_id;
    Object session_token;
    Activity activity;
    static FragmentManager support_Fragment_Manager;
    View view;
    APIInterface apiInterface;
    ArrayList<comingSoon> comData = new ArrayList<>();

    public ComingSoonAdapter(Context context, ArrayList<comingSoon> comeData, FragmentManager supportFragmentManager, FragmentActivity activity, Object UserID, Object Session_token) {
        this.context = context;
        this.activity = activity;
        this.user_id = UserID;
        this.session_token = Session_token;
        this.comData = comeData;
        this.support_Fragment_Manager = supportFragmentManager;
    }

    //    public void setListener(ComingSoonFragment listener) {
//        this.listener = listener;
//    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate((R.layout.coming_soon_video), parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ComingSoonAdapter.ViewHolder holder, int position) {
        holder.initFullscreenButton();
        comingSoon comeDataSoon = comData.get(position);

        Log.d("onBindViewHolder_testty", "onBindViewHolder: 01"+comeDataSoon.getId());
        Log.d("onBindViewHolder_testty", "onBindViewHolder: 02"+comeDataSoon.getTitle());
        Log.d("onBindViewHolder_testty", "onBindViewHolder: 03Postion"+user_id);

//        String videoUri = String.valueOf(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"));

        String cookieValue = "CloudFront-Policy=" + COOKIEPOLICY +
                ";CloudFront-Signature=" + COOKIESIGNATURE +
                ";CloudFront-Key-Pair-Id=" + COOKIEKEYPAIR;
        GlideUrl glideUrl = new GlideUrl(String.valueOf(comeDataSoon.getMobile_image()), new LazyHeaders.Builder()
                .addHeader("Cookie", cookieValue)
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                .build());
        holder.comeVideoTileImg.setImageResource(comeDataSoon.getComeVideoTileImg());
        holder.comeVideoTileImg.setLayoutParams(ResponsivenessUtils.getLayoutParamsForFullWidthSeasonView(context));
        Glide.with(context).load(glideUrl).thumbnail(0.5f)
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(new RequestOptions().override(holder.comeVideoTileImg.getWidth(), holder.comeVideoTileImg.getHeight()))
                .into(holder.comeVideoTileImg);
        holder.comeVideoTitle.setText(comeDataSoon.getTitle());
        holder.ComingSoonInfo.setText(comeDataSoon.getEp_title());
        holder.comingDesc.setText(comeDataSoon.getDescription());

        holder.comeVideoTileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.readyToPlayer.setVisibility(View.VISIBLE);

                if (!PrefUtils.getInstance(context.getApplicationContext()).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
                    Intent intent = new Intent(context.getApplicationContext(), NewLoginActivity.class);
                    intent.putExtra("Coming", "Upcoming");  // Replace "key" with your desired key and "value" with the actual value you want to send
                    context.startActivity(intent);
//                    context.startActivity(new Intent(context.getApplicationContext(), NewLoginActivity.class));
                    holder.readyToPlayer.setVisibility(View.GONE);
                } else {
                    getUserLoginStatus(context.getApplicationContext());
                    if (comeDataSoon.getTrailer_video() == "null") {
                        holder.readyToPlayer.setVisibility(View.GONE);
                        holder.playVideoBtn.setVisibility(View.GONE);
                        holder.comeVideoTileImg.setImageResource(comeDataSoon.getComeVideoTileImg());
                        holder.comeVideoTileImg.setLayoutParams(ResponsivenessUtils.getLayoutParamsForFullWidthSeasonView(context));
                        Glide.with(context).load(glideUrl).thumbnail(0.5f)
                                .transition(GenericTransitionOptions.with(animationObject))
                                .apply(new RequestOptions().override(holder.comeVideoTileImg.getWidth(), holder.comeVideoTileImg.getHeight()))
                                .into(holder.comeVideoTileImg);
                    } else {
                        holder.readyToPlayer.setVisibility(View.VISIBLE);
                        holder.startExoplayer(comeDataSoon.getTrailer_video(), false);
                        Log.d("onClick_test", "onClick: " + comeDataSoon.getTrailer_video());
                    }
                }
            }
        });

        if (comeDataSoon.getIsNotify() == 1) {
            holder.notify.setVisibility(View.GONE);
            holder.coming_notify.setVisibility(View.VISIBLE);
        } else {
            holder.notify.setVisibility(View.VISIBLE);
            holder.coming_notify.setVisibility(View.GONE);
        }

        holder.coming_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComingSoonFragment.UpComingNotification(user_id, session_token,comeDataSoon.getId());
                holder.notify.setVisibility(View.VISIBLE);
                holder.coming_notify.setVisibility(View.GONE);
                UiUtils.showShortToast(context, String.valueOf(ComingSoonFragment.notification_msg));
            }
        });

        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onBindViewHolder", "onBindViewHolder: notify"+user_id+" "+session_token);
                ComingSoonFragment.UpComingNotification(user_id, session_token, comeDataSoon.getId());
                holder.notify.setVisibility(View.GONE);
                holder.coming_notify.setVisibility(View.VISIBLE);
                UiUtils.showShortToast(context, String.valueOf(ComingSoonFragment.notification_msg));

            }
        });

        holder.img_exo_fullscreen.setOnClickListener(view -> {
            new ElasticAnimation(view).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                    .setOnFinishListener(new ElasticFinishListener() {
                        @Override
                        public void onFinished() {
                            // Do something after duration time
                            Intent intent = new Intent(context, FullScreenVideoActivity.class);
                            intent.putExtra("Trailer_video",comeDataSoon.getTrailer_video());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }).doAction();
        });
        
    }

    private void toggleFullScreen(StyledPlayerView player) {
        if (abc) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            abc = false;
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            abc = true;
        }
    }

    @Override
    public int getItemCount() {
        return comData.size();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onRefresh() {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context = itemView.getContext();
        APIInterface apiInterface;
        ShapeableImageView comeVideoTileImg;
        TextView comeVideoTitle, ComingSoonInfo, comingDesc;
        LinearLayout coming_notify;
        StyledPlayerView exoplayer;
        private ExoPlayer player;
        ConstraintLayout brightnesscontainerView;
        ConstraintLayout volumecontainerView;
        private boolean mExoPlayerIsLocked = false;
        ImageView exoUnLock, img_exo_fullscreen, notify;
        ImageButton exo_play, exo_pause, exoLock;
        private StyledPlayerControlView controlView;
        private DefaultTrackSelector trackSelector;
        SoundView soundView;
        TextView progress_volume_text;
        ImageView volumeicon, playVideoBtn;
        SoundView brightnessView;

        RelativeLayout rlThumbnail;
        RelativeLayout readyToPlayer;
        private MediaSource mVideoSource;
        private Video video;
        private PrefUtils prefUtils;


        private boolean isShowingTrackSelectionDialog;
        public static boolean trailerPlaying = false;
        public static boolean videoPlaying = false;
        private String currentlyStreamingUrl;
        private int playbackPosition;
        private ArrayList<String> timers = new ArrayList<>();
        private ArrayList<PlayerMessage> playerMessages = new ArrayList<>();
        RelativeLayout rl_exo_link;
        RelativeLayout btnSubtitles;
        private MergingMediaSource mergedSource;
        SpinKitView progressBar;
        LinearLayout ll_exo_rating;
        private ImageView mFullScreenIcon;
        //Full Screen
        RelativeLayout mFullScreenButton;
        private boolean mExoPlayerFullscreen = false;

        View btnSettings;
        SwipeRefreshLayout swipe;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comeVideoTileImg = itemView.findViewById(R.id.comeVideoTileImg);
            comeVideoTitle = itemView.findViewById(R.id.comeVideoTitle);
            ComingSoonInfo = itemView.findViewById(R.id.ComingSoonInfo);
            comingDesc = itemView.findViewById(R.id.comingDesc);
            coming_notify = itemView.findViewById(R.id.coming_notify);
            notify = itemView.findViewById(R.id.notify);
            exoplayer = itemView.findViewById(R.id.exoplayer);
            exo_play = itemView.findViewById(R.id.exo_play);
            exo_pause = itemView.findViewById(R.id.exo_pause);
            controlView = exoplayer.findViewById(R.id.exo_controller);
            brightnesscontainerView = itemView.findViewById(R.id.brightnesscontainer);
            volumecontainerView = itemView.findViewById(R.id.volumecontainer);
            exoUnLock = itemView.findViewById(R.id.exo_unlock);
            soundView = itemView.findViewById(R.id.volumeview);
            progress_volume_text = itemView.findViewById(R.id.progress_volume_text);
            volumeicon = itemView.findViewById(R.id.volumeicon);
            brightnessView = itemView.findViewById(R.id.brightnessview);
            rlThumbnail = itemView.findViewById(R.id.rlThumbnail);
            readyToPlayer = itemView.findViewById(R.id.readyToPlayer);
            img_exo_fullscreen = itemView.findViewById(R.id.img_exo_fullscreen);
            rl_exo_link = itemView.findViewById(R.id.rl_exo_link);
            btnSubtitles = itemView.findViewById(R.id.rl_exo_subtitle);
            progressBar = itemView.findViewById(R.id.exo_player_progress);
            ll_exo_rating = itemView.findViewById(R.id.ll_exo_rating);
            playVideoBtn = itemView.findViewById(R.id.playVideoBtn);
        }

        Handler mVolHandler = new Handler();
        Runnable mVolRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                volumecontainerView.startAnimation(fadeOut);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        volumecontainerView.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        };

        Handler mBrightHandler = new Handler();
        Runnable mBrightRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                brightnesscontainerView.startAnimation(fadeOut);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        brightnesscontainerView.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        };

        Handler mLockHandler = new Handler();
        Runnable mLockRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                exoUnLock.startAnimation(fadeOut);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        exoUnLock.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        };
        Handler mRatingHandler = new Handler();
        Runnable mRatingRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Animation slide_down_fade_out = AnimationUtils.loadAnimation(context, R.anim.slide_down_fade_out);
                ll_exo_rating.startAnimation(slide_down_fade_out);

                slide_down_fade_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ll_exo_rating.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        };

        AudioManager audioManager;


        void startExoplayer(String fileUrl, boolean isTrailer) {
            {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                trailerPlaying = isTrailer;
//                toolbar.setVisibility(View.GONE);
                UiUtils.log(TAG, "Started ");
                currentlyStreamingUrl = fileUrl;
                playbackPosition = 3;

//            if (parentMediaContent.getTrailerFileUrl() != null && FileUrl != null && FileUrl.contains(parentMediaContent.getTrailerFileUrl())) {
//                isTrailerPlaying = true;
//            } else {
//                isTrailerPlaying = false;
//            }

                String cookieValue = "CloudFront-Policy=" + COOKIEPOLICY +
                        ";CloudFront-Signature=" + COOKIESIGNATURE +
                        ";CloudFront-Key-Pair-Id=" + COOKIEKEYPAIR;

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Cookie", cookieValue);

                String userAgent = getUserAgent(context.getApplicationContext(), context.getApplicationInfo().packageName);
                DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
                httpDataSourceFactory.setUserAgent(userAgent);
                httpDataSourceFactory.setDefaultRequestProperties(headers);
                DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(context.getApplicationContext(), httpDataSourceFactory);

                //SubtitleConfig
                MediaItem.SubtitleConfiguration subtitleConfig = createSubtitleConfig(isTrailer);

                //Media Item
                MediaItem mediaItem = createMediaItem(fileUrl, subtitleConfig);

                if (getFileExtension(fileUrl).equalsIgnoreCase("m3u8")) {
                    UiUtils.log(TAG, "HlsMediaSource");
                    mVideoSource = new HlsMediaSource.Factory(dataSourceFactory)
                            .setExtractorFactory(HlsExtractorFactory.DEFAULT)
                            .createMediaSource(mediaItem);
                } else {
                    UiUtils.log(TAG, "ProgressiveMediaSource");
                    mVideoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(mediaItem);
                }

                ArrayList<MediaSource> combinedMediaSources = new ArrayList<>();
                combinedMediaSources.add(mVideoSource);

                //Multiple Videos
//                UiUtils.log(TAG, "Video Language->" + video.getVideoLanguageUrl().size());
//                UiUtils.log(TAG, "Trailer Language->" + video.getTrailerVideoLanguageUrl().size());
//                timers = new ArrayList<>();
//                rl_exo_link.setVisibility(View.GONE);
//                if(isTrailer) {
//                    ArrayList<MediaSource> trailerMediaSources = addLanguageMediaItem(video.getTrailerVideoLanguageUrl(), dataSourceFactory, subtitleConfig);
//                    if(trailerMediaSources.size()>0)
//                        combinedMediaSources.addAll(trailerMediaSources);
//
//                    if(!checkString(video.getRatingTrailerTime()))
//                        timers.addAll(Arrays.asList(video.getRatingTrailerTime().split(",")));
//
//                    //if(!(checkString(video.getRatingTrailerUrl()) || checkString(video.getRatingTrailerType())))
//                    rl_exo_link.setVisibility(View.GONE);
//                }
//                else
//                {
//                    ArrayList<MediaSource> videoMediaSources = addLanguageMediaItem(video.getVideoLanguageUrl(), dataSourceFactory, subtitleConfig);
//                    if(videoMediaSources.size()>0)
//                        combinedMediaSources.addAll(videoMediaSources);
//
//                    if(!checkString(video.getRatingTime()))
//                        timers.addAll(Arrays.asList(video.getRatingTime().split(",")));
//
//                    if(!(checkString(video.getRatingUrl()) || checkString(video.getRatingType())))
//                        rl_exo_link.setVisibility(View.VISIBLE);
//                }

                rl_exo_link.setOnClickListener(v -> {
                    new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                            .setOnFinishListener(new ElasticFinishListener() {
                                @Override
                                public void onFinished() {
                                    // Do something after duration time
                                    String type = isTrailer ? video.getRatingTrailerType() : video.getRatingType();
                                    switch (type.toLowerCase()) {
                                        case "media":
//                                            Intent i = new Intent(context, VideoPageActivity.class);
//                                            i.putExtra(ADMIN_VIDEO_ID, Integer.parseInt(video.getRatingUrl()));
//                                            i.putExtra(PARENT_ID, Integer.parseInt(video.getRatingUrl()));
//                                            startActivity(i);
//                                            finish();
                                            break;
                                     /*case "url":
                                         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getParent_media()));
                                         context.startActivity(browserIntent);
                                         break;*/
                                     /*case "youtube":
                                         Intent toYouTubePlayer = new Intent(context, YouTubePlayerActivity.class);
                                         toYouTubePlayer.putExtra(YouTubePlayerActivity.VIDEO_ID, data.get(position).getAdminVideoId());
                                         toYouTubePlayer.putExtra(YouTubePlayerActivity.VIDEO_URL, "https://youtu.be/VLTwnY7wY0Y");
                                                 context.startActivity(toYouTubePlayer);
                                         break;*/
                                        default:
                                            try {
//                                                if(!checkString(video.getRatingUrl())) {
//                                                    Intent browserIntentDefault = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getRatingUrl()));
//                                                    startActivity(browserIntentDefault);
//                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                UiUtils.log(TAG, Log.getStackTraceString(e));
                                            }
                        /*context.startActivity(new Intent(context, WebViewActivity.class)
                                .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.BANNER)
                                .putExtra("URL", "https://youtu.be/VLTwnY7wY0Y"));*/
                                            break;
                                    }
                                }
                            }).doAction();
                });

                if (subtitleConfig != null) {
                    MediaSource subtitleSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(subtitleConfig, C.TIME_UNSET);
                    combinedMediaSources.add(subtitleSource);
                    btnSubtitles.setVisibility(View.VISIBLE);
                } else {
                    //btnSubtitles.setVisibility(View.GONE);
                }

                //every media source becomes is assigned a renderer index , video=0,audio=1,subtitles=2
                //MergingMediaSource combines all the sources in the respective renderers ( all videos goes to renderer ID 0 , audio to 1 and so on)
                mergedSource = new MergingMediaSource(combinedMediaSources.toArray(new MediaSource[0]));

                initExoPlayer();

            }
        }

        private void addTimerMessage(ArrayList<String> timers) {
            //long position = 20 * 1000;
            for (PlayerMessage playerMessage : playerMessages) {
                playerMessage.cancel();
            }


            for (int i = 0; i < timers.size(); i++) {
                Handler handler = new Handler();
                playerMessages.add(player.createMessage(
                                (messageType, payload) -> {
                                    Log.e("TAG", "Hurray! We are at playback position 0:20 --> " + player.getCurrentPosition() / 1000);
                                    mRatingHandler.removeCallbacks(mRatingRunnable);
                                    ll_exo_rating.setVisibility(View.VISIBLE);
                                    Animation slide_up_fade_in = AnimationUtils.loadAnimation(context, R.anim.slide_up_fade_in);
                                    ll_exo_rating.startAnimation(slide_up_fade_in);
                                    slide_up_fade_in.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                        /*Animation slide_up = AnimationUtils.loadAnimation(VideoPageActivity.this, R.anim.slide_up);
                                        ll_exo_rating.startAnimation(slide_up);*/
                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            //ll_exo_rating.setVisibility(View.VISIBLE);
                                            mRatingHandler.postDelayed(mRatingRunnable, 7000);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                        }
                                    });
                                })
                        .setPosition(getTime(timers.get(i)))
                        .setHandler(handler)
                        .send()
                );
            }
        }

        private ArrayList<MediaSource> addLanguageMediaItem(List<String> streamUrls, DefaultDataSource.Factory dataSourceFactory, MediaItem.SubtitleConfiguration subtitleConfig) {
            ArrayList<MediaSource> extraMediaSource = new ArrayList<>();

            for (int i = 0; i < streamUrls.size(); i++) {
                MediaSource mediaSource = null;

                //Media Item
                MediaItem mediaItemExtra = createMediaItem(streamUrls.get(i), subtitleConfig);
                if (getFileExtension(streamUrls.get(i)).equalsIgnoreCase("m3u8")) {
                    UiUtils.log(TAG, "HlsMediaSource");
                    mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                            .setExtractorFactory(HlsExtractorFactory.DEFAULT)
                            .createMediaSource(mediaItemExtra);
                    UiUtils.log(TAG, "Language Hls->" + streamUrls.get(i));
                } else {
                    UiUtils.log(TAG, "ProgressiveMediaSource");
                    mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(mediaItemExtra);
                    UiUtils.log(TAG, "Language Progr->" + streamUrls.get(i));
                }

                extraMediaSource.add(mediaSource);
            }

            return extraMediaSource;
        }

        private MediaItem.SubtitleConfiguration createSubtitleConfig(boolean isTrailer) {
            String subtitle, subtitleLang;
            if (isTrailer) {
                subtitle = video.getTrailerSubtitle();
                subtitleLang = video.getTrailerSubtitleLang();
            } else {
//                subtitle = video.getSubTitleUrl();
//                subtitleLang = video.getSubTitleLang();
            }

//            UiUtils.log(TAG,"Subtitle: "+ subtitle);

//            if(!checkString(subtitle)) {
//
//                MediaItem.SubtitleConfiguration subtitleConfig = new MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitle))
//                        .setMimeType(MimeTypes.TEXT_VTT) // The correct MIME type (required).
//                        .setLabel(subtitle)
//                        .setLanguage(checkString(subtitleLang) ? "English" : subtitleLang) // The subtitle language (optional).
//                        .setSelectionFlags(C.SELECTION_FLAG_AUTOSELECT) // Selection flags for the track (optional).
//                        .build();
//                return subtitleConfig;
//            }else
            return null;
        }

        private MediaItem createMediaItem(String fileUrl, MediaItem.SubtitleConfiguration subtitleConfig) {

            MediaItem mediaItem;
            if (subtitleConfig != null) {
                mediaItem = new MediaItem.Builder()
                        .setUri(fileUrl)
                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .setSubtitleConfigurations(ImmutableList.of(subtitleConfig))
                        .build();
            } else {
                mediaItem = new MediaItem.Builder()
                        .setUri(fileUrl)
                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .build();
            }

            return mediaItem;
        }

        private void initExoPlayer() {
            //https://stackoverflow.com/questions/42228653/exoplayer-adaptive-hls-streaming
            UiUtils.log(TAG, "initExoPlayer ");
            ExoTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(0, 0, 0, 0);
            trackSelector = new DefaultTrackSelector(context, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

//        trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
//        subtitlesSelectionHelper = new SubtitlesSelectionHelper(trackSelector, videoTrackSelectionFactory);

            if (player == null) {
                UiUtils.log(TAG, "ExoPlayer");
                player = new ExoPlayer.Builder(context)
                        .setRenderersFactory(new DefaultRenderersFactory(context))
                        .setTrackSelector(trackSelector)
                        .setLoadControl(loadControl)
                        .build();
                exoplayer.setPlayer(player);
                exoplayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            } else {
                UiUtils.log(TAG, "Else ExoPlayer");
                player.stop();
                player.release();

                exoplayer.setPlayer(null);
                player = new ExoPlayer.Builder(context)
                        .setRenderersFactory(new DefaultRenderersFactory(context))
                        .setTrackSelector(trackSelector)
                        .setLoadControl(loadControl)
                        .build();
                exoplayer.setPlayer(player);
                exoplayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }

            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    Player.Listener.super.onPlaybackStateChanged(playbackState);
                    if (playbackState == Player.STATE_ENDED) {
                        controlView.setVisibility(View.VISIBLE);
                        player.seekTo(0);
                        player.setPlayWhenReady(false);
                        exo_play.setVisibility(View.VISIBLE);
                        AnimatedVectorDrawable animatedVectorDrawable;
                        exo_play.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.avd_pause_to_play, null));
                        //AnimationDrawable logoAnimation = (AnimationDrawable) exo_play.getBackground();
                        //logoAnimation.start();
                        Drawable drawable = exo_play.getDrawable();
                        if (drawable instanceof AnimatedVectorDrawable) {
                            animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                            animatedVectorDrawable.start();
                        }
                        //player.pause();
                    /*exo_play.setVisibility(View.VISIBLE);
                    exo_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            player.play();
                        }
                    });*/

                        timers = new ArrayList<>();
//                        if (trailerPlaying) {
//                            if (!checkString(video.getRatingTrailerTime()))
//                                timers.addAll(Arrays.asList(video.getRatingTrailerTime().split(",")));
//                        } else {
//                            if (!checkString(video.getRatingTime()))
//                                timers.addAll(Arrays.asList(video.getRatingTime().split(",")));
//                        }
                        addTimerMessage(timers);

                    } else if (playbackState == Player.STATE_BUFFERING) {
                        progressBar.setVisibility(View.VISIBLE);

                    } else if (playbackState == Player.STATE_READY) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

            /*@Override
            public void onCues(List<Cue> cues) {
                Player.Listener.super.onCues(cues);
                if (btnSubtitles != null) {
                    btnSubtitles.setma(cues);
                    btnSubtitles.onCues(cues);
                }
            }*/

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    Player.Listener.super.onIsPlayingChanged(isPlaying);
                    if (isPlaying) {
                        UiUtils.log(TAG, "isPlaying-> " + isPlaying);
                        soundView.setVisibility(View.VISIBLE);
                        //exoPlayPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_play_arrow_24,null));
                        //exoPlayPause.setImageDrawable(null);
                        exo_play.setVisibility(View.GONE);
                        exo_pause.setVisibility(View.VISIBLE);
                        videoPlaying = true;

                    } else {
                        UiUtils.log(TAG, "isPlaying-> " + isPlaying);
                        //exoPlayPause.setImageDrawable(null);
                        //exoPlayPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.exo_player_pause_btn_new,null));
                        exo_play.setVisibility(View.VISIBLE);
                        exo_pause.setVisibility(View.GONE);
                        videoPlaying = false;
                    }
                }

                @Override
                public void onMediaMetadataChanged(com.google.android.exoplayer2.MediaMetadata mediaMetadata) {
                    Player.Listener.super.onMediaMetadataChanged(mediaMetadata);

                    UiUtils.log("TextInformationFrame", "" + mediaMetadata.subtitle);

                }
            });

            //mExoPlayerView.setOnTouchListener(null);
            addTimerMessage(timers);

            ArrayList<MediaSource> mergedSources = new ArrayList<>();
            mergedSources.add(mergedSource);

            player.setMediaSources(mergedSources);
            //player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
        }

        private void initFullscreenButton() {

            controlView = exoplayer.findViewById(R.id.exo_controller);
            mFullScreenIcon = controlView.findViewById(R.id.img_exo_fullscreen);

            //btnSubtitles = controlView.findViewById(R.id.exo_subtitle);
            btnSubtitles = controlView.findViewById(R.id.rl_exo_subtitle);

            btnSettings = controlView.findViewById(R.id.exo_settings);
            //View btnAudio = controlView.findViewById(R.id.exo_audio);
            RelativeLayout btnAudio = controlView.findViewById(R.id.rl_exo_audio);
            btnSubtitles.setOnClickListener(view -> showSubtitlesSelectionDialog(view));
            btnSettings.setOnClickListener(view -> showQualitySelectionDialog(view, support_Fragment_Manager));
//            btnAudio.setOnClickListener(view -> showAudioSelectionDialog(view));


            //     Dev Ganesh  19/07/2023  Hide for work backword behaf of Harshvadhan sir

            //Forward & Rewind

            ImageButton exo_ffwd11 = controlView.findViewById(R.id.exo_ffwd);
            ImageButton exo_rew11 = controlView.findViewById(R.id.exo_rew);
            exo_ffwd11.setVisibility(View.GONE);
            exo_rew11.setVisibility(View.GONE);
//            exo_ffwd11.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Animation aniRotate = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.rotate_clockwise);
//                    exo_ffwd11.startAnimation(aniRotate);
//                    player.seekTo(player.getCurrentPosition() + 10000);
//                }
//            });
//            exo_rew11.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Animation aniRotate = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.rotate_anticlockwise);
//                    exo_rew11.startAnimation(aniRotate);
//                    player.seekTo(player.getCurrentPosition() - 10000);
//                }
//            });


            mFullScreenButton = controlView.findViewById(R.id.rl_exo_fullscreen);
            //mFullScreenButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_expand));
            mFullScreenButton.setVisibility(View.VISIBLE);
            mFullScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mExoPlayerFullscreen) {
                        Log.d("onClick112", "onClick: 1");
                        //    openFullscreenDialog(true);
                    } else {
                        Log.d("onClick112", "onClick: 2");
                        //   closeFullscreenDialog(true);
                    }
                }
            });

            exo_play = controlView.findViewById(R.id.exo_play);
            exo_pause = controlView.findViewById(R.id.exo_pause);
            exo_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.prepare();
                    player.play();
                    //if(!videoPlaying) {
                    AnimatedVectorDrawable animatedVectorDrawable;
                    exo_pause.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.avd_play_to_pause, null));
                    Drawable drawable = exo_pause.getDrawable();
                    if (drawable instanceof AnimatedVectorDrawable) {
                        animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                        animatedVectorDrawable.start();
                    }
                    videoPlaying = true;
                    //}
                }
            });
            exo_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.pause();
                    //if(videoPlaying) {
                    AnimatedVectorDrawable animatedVectorDrawable;
                    exo_play.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.avd_pause_to_play, null));
                    Drawable drawable = exo_play.getDrawable();
                    if (drawable instanceof AnimatedVectorDrawable) {
                        animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                        animatedVectorDrawable.start();
                    }
                    videoPlaying = false;
                    //}
                }
            });


            exoLock = controlView.findViewById(R.id.exo_lock);
            exoLock.setVisibility(View.GONE);
            exoLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UiUtils.log(TAG, "Lock Clicked");
                /*if(mExoPlayerIsLocked)
                {
                    mExoPlayerView.showController();
                    mExoPlayerView.setUseController(true);
                    //exoLock.setColorFilter(ContextCompat.getColor(VideoPageActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    exoUnLock.setVisibility(View.GONE);
                    mExoPlayerIsLocked = false;
                    mLockHandler.removeCallbacks(mLockRunnable);
                    mLockHandler.postDelayed(mLockRunnable, 2000);
                }else {*/
                    exoplayer.hideController();
                    exoplayer.setUseController(false);
                    //exoUnLock.setColorFilter(ContextCompat.getColor(VideoPageActivity.this, R.color.colorPrimaryHeaderPink), android.graphics.PorterDuff.Mode.SRC_IN);
                    exoUnLock.setVisibility(View.VISIBLE);
                    mExoPlayerIsLocked = true;
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                    mLockHandler.removeCallbacks(mLockRunnable);
                    mLockHandler.postDelayed(mLockRunnable, 2000);
                    //}
                }
            });

            exoUnLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exoplayer.showController();
                    exoplayer.setUseController(true);
                    //exoLock.setColorFilter(ContextCompat.getColor(VideoPageActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    mLockHandler.removeCallbacks(mLockRunnable);
                    exoUnLock.setVisibility(View.GONE);
                    mExoPlayerIsLocked = false;
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            });

            soundView.setOnsoundProgressChangeListner(new SoundProgressChangeListner() {
                @Override
                public void onchange(int progress) {
                    final float adjust = progress / 15f;
                    UiUtils.log("Touch", "Volume: " + progress);
                    UiUtils.log("Touch", "Adjust: " + adjust);
                    player.setVolume(adjust);
                    progress_volume_text.setText(String.valueOf(progress));
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    if (progress <= 0)
                        volumeicon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_vol_mute, null));
                    else
                        volumeicon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_vol_unmute, null));

                    mVolHandler.removeCallbacks(mVolRunnable);
                    mVolHandler.postDelayed(mVolRunnable, 2000);
                }
            });
            brightnessView.setOnsoundProgressChangeListner(new SoundProgressChangeListner() {
                @Override
                public void onchange(int progress) {
                    final float adjust = progress / 100;
                    UiUtils.log("Touch", "Brightness: " + progress);
                    mBrightHandler.removeCallbacks(mBrightRunnable);
                    mBrightHandler.postDelayed(mBrightRunnable, 2000);
                }
            });
        }

        //quality changes
        public void showQualitySelectionDialog(View view, FragmentManager support_Fragment_Manager) {

            if (!isShowingTrackSelectionDialog
                    && TrackSelectionDialog.willHaveContent(trackSelector)) {
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog =
                        TrackSelectionDialog.createForTrackSelector(
                                trackSelector,
                                dismissedDialog -> isShowingTrackSelectionDialog = false, C.TRACK_TYPE_DEFAULT, true);
                Log.d("showQualitySelectionDialog", "showQualitySelectionDialog: " + ComingSoonAdapter.support_Fragment_Manager);
                trackSelectionDialog.show(ComingSoonAdapter.support_Fragment_Manager, "Quality");
            }
        }

        //subtitles change
        private void showSubtitlesSelectionDialog(View view) {

            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (!isShowingTrackSelectionDialog
                        && TrackSelectionDialog.willHaveContent(trackSelector)) {
                    isShowingTrackSelectionDialog = true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector,
                                    /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false, 2, true);
                    trackSelectionDialog.show(ComingSoonAdapter.support_Fragment_Manager, /* tag= */ null);

                }
            }
        }


        public void releasePlayer() {
            if (player != null) {
                player.stop();
                exo_pause.setVisibility(View.GONE);
                exo_play.setVisibility(View.VISIBLE);
            } else {
                rlThumbnail.setVisibility(View.VISIBLE);
            }
        }

        @SuppressLint("SourceLockedOrientationActivity")
        private void changeOrientation() {
            Context context = exoplayer.getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoplayer.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    exoplayer.setLayoutParams(params);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        }


    }

}

