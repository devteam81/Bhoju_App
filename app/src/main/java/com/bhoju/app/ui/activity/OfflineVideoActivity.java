package com.bhoju.app.ui.activity;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.mediarouter.app.MediaRouteButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhoju.app.util.exoPlayer.SoundProgressChangeListner;
import com.bhoju.app.util.exoPlayer.SoundView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.cast.CastPlayer;
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.id3.UrlLinkFrame;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.bhoju.app.R;
import com.bhoju.app.util.TrackSelectionDialog;
import com.bhoju.app.util.UiUtils;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bhoju.app.network.APIConstants.Params.VIDEO_URL;
import static com.google.android.exoplayer2.util.Util.getUserAgent;
import static com.bhoju.app.util.ParserUtils.getFileExtension;

public class OfflineVideoActivity extends AppCompatActivity implements SessionAvailabilityListener {

    private final String TAG = OfflineVideoActivity.class.getSimpleName();

    @BindView(R.id.exoplayer)
    StyledPlayerView mExoPlayerView;
    @BindView(R.id.exo_player_progress)
    SpinKitView progressBar;
    @BindView(R.id.media_route_button)
    MediaRouteButton mediaRouteButton;

    @BindView(R.id.exo_unlock)
    ImageView exoUnLock;

    @BindView(R.id.volumeview)
    SoundView soundView;
    @BindView(R.id.volumecontainer)
    ConstraintLayout volumecontainerView;
    @BindView(R.id.progress_volume_text)
    TextView progress_volume_text;
    @BindView(R.id.volumeicon)
    ImageView volumeicon;

    @BindView(R.id.brightnessview)
    SoundView brightnessView;
    @BindView(R.id.brightnesscontainer)
    ConstraintLayout brightnesscontainerView;
    @BindView(R.id.progress_brightness_text)
    TextView progress_brightness_text;
    @BindView(R.id.brightnessicon)
    ImageView brightnessicon;

    private String currentlyStreamingUrl;
    private MediaSource mVideoSource;
    private MergingMediaSource mergedSource;
    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    private DefaultTrackSelector trackSelector;
    private ExoPlayer player;
    private StyledPlayerControlView controlView;
    ImageButton exoLock;
    ImageButton exo_play,exo_pause;
    private boolean mExoPlayerIsLocked = false;

    private static boolean videoPlaying = false;

    private CastContext castContext;
    private CastPlayer castPlayer;

    private Dialog mFullScreenDialog;

    private boolean isShowingTrackSelectionDialog;

    private boolean isscalegesture;
    private float downy;
    private float endheight;
    private float diffheight;
    private int currentprogress;
    private int currentbrightprogress;
    private float lastx;
    private boolean first = true, second = true;
    private int selected = 0;

    Handler mVolHandler=new Handler();
    Runnable mVolRunnable=new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Animation fadeOut = AnimationUtils.loadAnimation(OfflineVideoActivity.this, R.anim.fade_out);
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

    Handler mBrightHandler=new Handler();
    Runnable mBrightRunnable=new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Animation fadeOut = AnimationUtils.loadAnimation(OfflineVideoActivity.this, R.anim.fade_out);
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

    Handler mLockHandler=new Handler();
    Runnable mLockRunnable=new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Animation fadeOut = AnimationUtils.loadAnimation(OfflineVideoActivity.this, R.anim.fade_out);
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

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_video);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        String uri = getIntent().getExtras().getString(VIDEO_URL);
        initFullscreenButton();
        initFullscreenDialog();

        startExoplayer(uri);
    }

    private void initFullscreenButton() {

        controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        //View mFullScreenIcon = controlView.findViewById(R.id.exo_minimal_fullscreen);
        View mFullScreenButton = controlView.findViewById(R.id.rl_exo_fullscreen);
        View btnSubtitles = controlView.findViewById(R.id.rl_exo_subtitle);
        View btnSettings = controlView.findViewById(R.id.exo_settings);
        View exoAudio = controlView.findViewById(R.id.rl_exo_audio);
        exoAudio.setLayoutParams(new LinearLayout.LayoutParams(1, 10));

        //mFullScreenIcon.setVisibility(View.GONE);
        mFullScreenButton.setVisibility(View.GONE);
        //btnSubtitles.setVisibility(View.GONE);

        btnSubtitles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubtitlesSelectionDialog(view);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQualitySelectionDialog(view);
            }
        });

        //Forward & Rewind
        ImageButton exo_ffwd11 = controlView.findViewById(R.id.exo_ffwd);
        ImageButton exo_rew11 = controlView.findViewById(R.id.exo_rew);
        exo_ffwd11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
                exo_ffwd11.startAnimation(aniRotate);
                player.seekTo(player.getCurrentPosition()+10000);
            }
        });
        exo_rew11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
                exo_rew11.startAnimation(aniRotate);
                player.seekTo(player.getCurrentPosition()-10000);
            }
        });

        ImageButton exoPlayPause = controlView.findViewById(R.id.exo_play_pause);
        exo_play = controlView.findViewById(R.id.exo_play);
        exo_pause = controlView.findViewById(R.id.exo_pause);
        exo_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.play();
                //if(!videoPlaying) {
                AnimatedVectorDrawable animatedVectorDrawable;
                exo_pause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.avd_play_to_pause, null));
                //AnimationDrawable logoAnimation = (AnimationDrawable) exo_play.getBackground();
                //logoAnimation.start();
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
                exo_play.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.avd_pause_to_play, null));

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
        exoLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.log(TAG,"Lock Clicked");
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
                mExoPlayerView.hideController();
                mExoPlayerView.setUseController(false);
                //exoUnLock.setColorFilter(ContextCompat.getColor(VideoPageActivity.this, R.color.colorPrimaryHeaderPink), android.graphics.PorterDuff.Mode.SRC_IN);
                exoUnLock.setVisibility(View.VISIBLE);
                mExoPlayerIsLocked = true;
                mLockHandler.removeCallbacks(mLockRunnable);
                mLockHandler.postDelayed(mLockRunnable, 2000);
                //}
            }
        });

        exoUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExoPlayerView.showController();
                mExoPlayerView.setUseController(true);
                //exoLock.setColorFilter(ContextCompat.getColor(VideoPageActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                mLockHandler.removeCallbacks(mLockRunnable);
                exoUnLock.setVisibility(View.GONE);
                mExoPlayerIsLocked = false;
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
                //if(!touchKey)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                if(progress<=0)
                    volumeicon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_vol_mute,null));
                else
                    volumeicon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_vol_unmute,null));

                mVolHandler.removeCallbacks(mVolRunnable);
                mVolHandler.postDelayed(mVolRunnable, 2000);
            }
        });
        brightnessView.setOnsoundProgressChangeListner(new SoundProgressChangeListner() {
            @Override
            public void onchange(int progress) {
                final float adjust = progress / 100;
                UiUtils.log("Touch", "Brightness: " + progress);
                /*player.setVolume(progress);
                progress_volume_text.setText(String.valueOf(progress));
                if(progress<=0)
                    volumeicon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_vol_mute,null));
                else
                    volumeicon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_vol_unmute,null));*/

                mBrightHandler.removeCallbacks(mBrightRunnable);
                mBrightHandler.postDelayed(mBrightRunnable, 2000);
            }
        });

    }

    private void initFullscreenDialog() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mExoPlayerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mExoPlayerView.setLayoutParams(params);

        /*mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                super.onBackPressed();
            }
        };

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        //exoBackBtn.setVisibility(View.VISIBLE);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenDialog.show();*/
        playerTouch();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
            volumecontainerView.setVisibility(View.VISIBLE);
            UiUtils.log(TAG,"touch volume down -> "+ audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            int newStream = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)-1;
            soundView.setProgress(newStream);
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (!mExoPlayerIsLocked)
                finish();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
            volumecontainerView.setVisibility(View.VISIBLE);
            UiUtils.log(TAG,"touch volume up -> "+ audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            int newStream = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)+1;
            soundView.setProgress(newStream);
        }

        return  true;
    }


    //video quality selection
    private void showQualitySelectionDialog(View view) {

        if(!isShowingTrackSelectionDialog
                && TrackSelectionDialog.willHaveContent(trackSelector)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false,0, false);
            //trackSelectionDialog.getView().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
        }
    }

    //subtitles change
    private void showSubtitlesSelectionDialog(View view) {

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if(!isShowingTrackSelectionDialog
                    && TrackSelectionDialog.willHaveContent(trackSelector)) {
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog =
                        TrackSelectionDialog.createForTrackSelector(
                                trackSelector,
                                /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false,2, false);
                trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

            }
        }
    }

    /*private void initFullscreenDialog() {

        try {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            //exoBackBtn.setVisibility(View.VISIBLE);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
           // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_skrink));
            mExoPlayerFullscreen = true;
            mFullScreenDialog.show();

        }catch (Exception ex){ex.printStackTrace();}
    }*/

    void startExoplayer(String fileUrl) {

        UiUtils.log(TAG, "Started ");
        currentlyStreamingUrl = fileUrl;

        String userAgent = getUserAgent(this, getApplicationContext().getApplicationInfo().packageName);
        DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
        httpDataSourceFactory.setUserAgent(userAgent);
        //httpDataSourceFactory.setDefaultRequestProperties(headers);
        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this, httpDataSourceFactory);

        //Media Item
        MediaItem mediaItem = createMediaItem(fileUrl,null);

        if(getFileExtension(fileUrl).equalsIgnoreCase("m3u8"))
        {
            mVideoSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .setExtractorFactory(HlsExtractorFactory.DEFAULT)
                    .createMediaSource(mediaItem);
        }else
        {
            mVideoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem);
        }

        ArrayList<MediaSource> combinedMediaSources = new ArrayList<>();
        combinedMediaSources.add(mVideoSource);

        //every media source becomes is assigned a renderer index , video=0,audio=1,subtitles=2
        //MergingMediaSource combines all the sources in the respective renderers ( all videos goes to renderer ID 0 , audio to 1 and so on)
        mergedSource = new MergingMediaSource(combinedMediaSources.toArray(new MediaSource[0]));

        initExoPlayer();



    }

    private MediaItem createMediaItem(String fileUrl, MediaItem.SubtitleConfiguration subtitleConfig)
    {

        MediaItem mediaItem;
        if(subtitleConfig!=null) {
            mediaItem = new MediaItem.Builder()
                    .setUri(fileUrl)
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .setSubtitleConfigurations(ImmutableList.of(subtitleConfig))
                    .build();
        }else {
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
        ExoTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(0,0,0,0);
        trackSelector = new DefaultTrackSelector(OfflineVideoActivity.this, videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();


//        trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
//        subtitlesSelectionHelper = new SubtitlesSelectionHelper(trackSelector, videoTrackSelectionFactory);

        if (player == null) {
            UiUtils.log(TAG, "ExoPlayer");
            player = new ExoPlayer.Builder(this)
                    .setRenderersFactory(new DefaultRenderersFactory(this))
                    .setTrackSelector(trackSelector)
                    .setLoadControl(loadControl)
                    .build();
            mExoPlayerView.setPlayer(player);
            mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        } else {
            UiUtils.log(TAG, "Else ExoPlayer");
            player.stop();
            player.release();

            mExoPlayerView.setPlayer(null);
            player = new ExoPlayer.Builder(this)
                    .setRenderersFactory(new DefaultRenderersFactory(this))
                    .setTrackSelector(trackSelector)
                    .setLoadControl(loadControl)
                    .build();
            mExoPlayerView.setPlayer(player);
            mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }

        ArrayList<MediaSource> mergedSources = new ArrayList<>();
        mergedSources.add(mergedSource);

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
                    exo_play.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.avd_pause_to_play, null));
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

                } else if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if (isPlaying) {
                    UiUtils.log(TAG, "isPlaying-> " + isPlaying);
                    soundView.setVisibility(View.VISIBLE);
                    exo_play.setVisibility(View.GONE);
                    exo_pause.setVisibility(View.VISIBLE);
                    videoPlaying = true;
                } else {
                    UiUtils.log(TAG, "isPlaying-> " + isPlaying);
                    exo_play.setVisibility(View.VISIBLE);
                    exo_pause.setVisibility(View.GONE);
                    videoPlaying = false;

                }
            }
        });

        player.setMediaSources(mergedSources);
        player.prepare();
        player.setPlayWhenReady(true);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            if(getSupportActionBar() != null){
                getSupportActionBar().hide();
            }
        }
    }

    @Override
    protected void onPause() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.release();
        }
        super.onDestroy();
    }

    private void playerTouch()
    {
        //soundView.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        //brightnessView.setProgress(8);

        mExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UiUtils.log("Touch","pointer "+motionEvent.getPointerCount()+","+motionEvent.getAction());
                //scaleGestureDetector.onTouchEvent(motionEvent);
                if(mExoPlayerIsLocked)
                {
                    exoUnLock.setVisibility(View.VISIBLE);
                    mLockHandler.removeCallbacks(mLockRunnable);
                    mLockHandler.postDelayed(mLockRunnable, 2000);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastx = motionEvent.getX();
                    downy = motionEvent.getY();
                    endheight = downy - getResources().getDimensionPixelSize(R.dimen.dp_180);
                    diffheight = endheight - downy;
                    currentprogress = soundView.getProgress();
                    currentbrightprogress= brightnessView.getProgress();
                    first = true;
                    second = true;
                    selected = 0;
                    //currentseek = dragseek.getProgress();

                    UiUtils.log("Touch", "width "+mExoPlayerView.getWidth() + "");

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                    UiUtils.log("Touch","myrb "+ "x=" + lastx + "y=" + downy + "");
                    UiUtils.log("Touch","myrb "+ "x=" + motionEvent.getX() + "y=" + motionEvent.getY() + "," + motionEvent.getAction());
                    float xdistance = motionEvent.getX() - lastx;
                    float ydistance = motionEvent.getY() - downy;
                    UiUtils.log("Touch","Value "+ "x=" + xdistance + "y=" + ydistance + "");
                    if (first && Math.abs(xdistance) == 0 && Math.abs(ydistance) == 0) {

                    } else if ((second && Math.abs(xdistance) < Math.abs(ydistance)) || selected == 1) {
                        if (selected == 0) {
                            selected = 1;
                            first = false;
                            second = true;
                            if(motionEvent.getX()<view.getWidth()/2.0f){
                                volumecontainerView.setVisibility(View.VISIBLE);
                                brightnesscontainerView.setVisibility(View.GONE);
                            }
                            else {
                                volumecontainerView.setVisibility(View.GONE);
                                brightnesscontainerView.setVisibility(View.VISIBLE);
                            }
                        }
                        UiUtils.log("Touch","endheight "+ endheight + " y= "+ motionEvent.getY());
                        float tempwidth = endheight - motionEvent.getY();
                        UiUtils.log("Touch","tempwidth "+ tempwidth);
                        UiUtils.log("Touch","sound "+ soundView.getMaxprogess() + " diffheight "+ diffheight);
                        float progress = (tempwidth * soundView.getMaxprogess()) / diffheight;
                        UiUtils.log("Touch","progress "+ (soundView.getMaxprogess() - progress) + "");
                        float tempnumber = (soundView.getMaxprogess() - progress);
                        int jprogress = (int)tempnumber /*(soundView.getMaxprogess() - progress)*/ ;
                        if(volumecontainerView.getVisibility()==View.VISIBLE){
                            int prog = (currentprogress + jprogress) ;
                            UiUtils.log("Touch","final progress "+ prog + "");
                            //int soundProg =
                            soundView.setProgress(prog);
                            if (prog > soundView.getMaxprogess())
                                soundView.setProgress(soundView.getMaxprogess());
                            else if (prog < 0) soundView.setProgress(0);
                            else {
                                soundView.setProgress(prog);
                            }

                        }
                        else {
                            int prog = currentbrightprogress + jprogress;
                            UiUtils.log("Touch","final progress "+ prog + "");
                            if (prog > brightnessView.getMaxprogess())
                                brightnessView.setProgress(brightnessView.getMaxprogess());
                            else if (prog < 0) brightnessView.setProgress(0);
                            else {
                                float brightness = prog/ 15.0f;
                                WindowManager.LayoutParams lp = getWindow().getAttributes();
                                lp.screenBrightness = brightness;
                                getWindow().setAttributes(lp);
                                UiUtils.log("Touch","final brightness "+ brightness + "");
                                brightnessView.setProgress(prog);
                                progress_brightness_text.setText(String.valueOf(prog));
                            }
                        }

                        UiUtils.log("Touch","scroll "+ "vertical");
                    } /*else if (third || selected == 2) {

                        if (selected == 0) {
                            if (player.isPlaying()) {
                                isdonebyus = true;
                                player.pause();
                            }
                            second = false;
                            first = false;
                            third = true;
                            selected = 2;
                            playpausebutton.setVisibility(View.GONE);
                            bottomview.setVisibility(View.VISIBLE);
                            //toolbar.setVisibility(View.GONE);
                            seeklay.setVisibility(View.VISIBLE);

                        }

                        int progress = (int) ((60000 * (motionEvent.getX() - trackx)) / touchview.getWidth());

                        if (lastprogress != progress) {
                            player.seekTo(currentseek + progress);
                            dragseek.setProgress(currentseek + progress);
                            seektime.setText(milltominute(dragseek.getProgress()));
                            seekdelay.setText("[" + milltominute(progress) + "]");
                            // Log.e("scroll","horizontal"+milltominute(dragseek.getProgress())+","+milltominute(progress));
                        }
                        lastprogress = progress;


                    }*/
                    lastx = motionEvent.getX();
                    downy = motionEvent.getY();
                } else if(motionEvent.getPointerCount()==1&&motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(isscalegesture){
                        isscalegesture=false;
                    }
                    else {
                        /*seeklay.setVisibility(View.GONE);
                        if (isdonebyus) player.play();
                        isdonebyus = false;
                        if(islock){
                            if(unlockbtn.getVisibility()==View.GONE){
                                unlockbtn.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        unlockbtn.setVisibility(View.GONE);
                                    }
                                },2000);
                            }
                        }
                        else {
                            if (motionEvent.getX() == putx && motionEvent.getY() == puty && System.currentTimeMillis() - lasttime <= 1000) {
                                speedView.setVisibility(View.GONE);
                                if (isshow) {
                                    hideSystemUI();
                                    bottomview.setVisibility(View.GONE);
                                    toolbar.setVisibility(View.GONE);
                                } else {
                                    showSystemUI();
                                    playpausebutton.setVisibility(View.VISIBLE);
                                    bottomview.setVisibility(View.VISIBLE);
                                    toolbar.setVisibility(View.VISIBLE);
                                    hidehandler.postDelayed(hiderunnable,4000);

                                }
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        hideSystemUI();
                                        volumecontainerView.setVisibility(View.INVISIBLE);
                                        brightcontainer.setVisibility(View.INVISIBLE);
                                        bottomview.setVisibility(View.GONE);
                                        toolbar.setVisibility(View.GONE);
                                        playpausebutton.setVisibility(View.VISIBLE);

                                    }
                                }, 300);
                            }
                        }*/
                    }



                }
                else if(isscalegesture){

                }


                return false;
            }
        });
    }


    @Override
    public void onCastSessionAvailable() {

    }

    @Override
    public void onCastSessionUnavailable() {

    }
}