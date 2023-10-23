package com.bhoju.app.ui.activity;

import static com.bhoju.app.util.ParserUtils.getFileExtension;
import static com.bhoju.app.util.UiUtils.checkString;
import static com.bhoju.app.util.sharedpref.Utils.getTime;
import static com.google.android.exoplayer2.util.Util.getUserAgent;
import static org.greenrobot.eventbus.EventBus.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.TrackSelectionDialog;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.exoPlayer.SoundProgressChangeListner;
import com.bhoju.app.util.exoPlayer.SoundView;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.bhoju.app.util.sharedpref.Utils;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class FullScreenVideoActivity extends BaseActivity {
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
    boolean isRotated = false;
    private Dialog mFullScreenDialog;
    @BindView(R.id.progress_brightness_text)
    TextView progress_brightness_text;

    RelativeLayout rlThumbnail;
    RelativeLayout readyToPlayer;
    private MediaSource mVideoSource;
    private Video video;
    private PrefUtils prefUtils;

    @BindView(R.id.exo_txt_rating)
    TextView exo_txt_rating;
    @BindView(R.id.exo_txt_genre)
    TextView exo_txt_genre;

    @BindView(R.id.exoplayer)
    StyledPlayerView mExoPlayerView;
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
    AudioManager audioManager;
    View btnSettings;
    SwipeRefreshLayout swipe;
    Timer mRestoreOrientation;
    boolean zoom=false;
    private boolean isscalegesture;
    private float downy;
    private float endheight;
    private float diffheight;
    private int currentprogress;
    private int currentbrightprogress;
    private float lastx;
    private boolean first = true, second = true;
    private int selected = 0;
    @BindView(R.id.full_Screen_Right_Top)
    ImageView full_Screen_Right_Top;

    @Override
    public void onBackPressed() {
        //To support reverse transitions when user clicks the device back button
        supportFinishAfterTransition();
        player.pause();
    }

    Handler mVolHandler = new Handler();
    Runnable mVolRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
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
            Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
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
            Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
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
            Animation slide_down_fade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_fade_out);
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

    float scaleFactor = 1.0f;
    float minScaleFactor = 1.0f;
    float maxScaleFactor = 3.0f;

    private ScaleGestureDetector scaleGestureDetector;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_video);

        comeVideoTileImg = findViewById(R.id.comeVideoTileImg);
        comeVideoTitle = findViewById(R.id.comeVideoTitle);
        ComingSoonInfo = findViewById(R.id.ComingSoonInfo);
        comingDesc = findViewById(R.id.comingDesc);
        coming_notify = findViewById(R.id.coming_notify);
        notify = findViewById(R.id.notify);
        exoplayer = findViewById(R.id.exoplayer);
        exo_play = findViewById(R.id.exo_play);
        exo_pause = findViewById(R.id.exo_pause);
        controlView = exoplayer.findViewById(R.id.exo_controller);
        brightnesscontainerView = findViewById(R.id.brightnesscontainer);
        volumecontainerView = findViewById(R.id.volumecontainer);
        exoUnLock = findViewById(R.id.exo_unlock);
        soundView = findViewById(R.id.volumeview);
        progress_volume_text = findViewById(R.id.progress_volume_text);
        volumeicon = findViewById(R.id.volumeicon);
        brightnessView = findViewById(R.id.brightnessview);
        rlThumbnail = findViewById(R.id.rlThumbnail);
        readyToPlayer = findViewById(R.id.readyToPlayer);
        img_exo_fullscreen = findViewById(R.id.img_exo_fullscreen);
        rl_exo_link = findViewById(R.id.rl_exo_link);
        btnSubtitles = findViewById(R.id.rl_exo_subtitle);
        progressBar = findViewById(R.id.exo_player_progress);
        ll_exo_rating = findViewById(R.id.ll_exo_rating);
        playVideoBtn = findViewById(R.id.playVideoBtn);



        initFullscreenButton();


        full_Screen_Right_Top=findViewById(R.id.full_Screen_Right_Top);
        exoplayer = findViewById(R.id.exoplayer);
        full_Screen_Right_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoom==false){
                    full_Screen_Right_Top.setBackgroundResource(R.drawable.ic_baseline_zoom_in_map_24);
                    exoplayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    zoom=true;
                }else{
                    full_Screen_Right_Top.setBackgroundResource(R.drawable.ic_baseline_zoom_out_map_24);
                    exoplayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    Log.d("onClick_1", "onClick: 2");
                    zoom=false;
                }
                //          Toast.makeText(activity, "bala wo bala", Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = getIntent();
        String Trailer_video = intent.getStringExtra("Trailer_video");

        readyToPlayer.setVisibility(View.VISIBLE);
        String videoUri = String.valueOf(Uri.parse(Trailer_video));
        startExoplayer(videoUri, false);

        openFullscreenDialog(true);


        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        exoplayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });
//        comeVideoTileImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                readyToPlayer.setVisibility(View.VISIBLE);
//
//                if (!PrefUtils.getInstance(getApplicationContext().getApplicationContext()).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
//                    Intent intent = new Intent(getApplicationContext().getApplicationContext(), NewLoginActivity.class);
//                    intent.putExtra("Coming", "Upcoming");  // Replace "key" with your desired key and "value" with the actual value you want to send
//                    getApplicationContext().startActivity(intent);
////                    context.startActivity(new Intent(context.getApplicationContext(), NewLoginActivity.class));
//                    readyToPlayer.setVisibility(View.GONE);
//                } else {
//                    getUserLoginStatus(getApplicationContext().getApplicationContext());
//
//                }
//            }
//        });

        mFullScreenButton = controlView.findViewById(R.id.rl_exo_fullscreen);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
                player.pause();
            }
        });

        openFullscreenDialog(true);

    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, maxScaleFactor));
            exoplayer.setScaleX(scaleFactor);
            exoplayer.setScaleY(scaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // Optional: You can perform any action after the scaling ends.
        }
    }



    public void openFullscreenDialog(boolean change) {
        try {
            if (change) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //  reActivateOrientationDetection();
            }
            /*((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_skrink));
            mExoPlayerFullscreen = true;
            mFullScreenDialog.show();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void reActivateOrientationDetection() {
        if (mRestoreOrientation != null) mRestoreOrientation.cancel();
        mRestoreOrientation = new Timer();
        mRestoreOrientation.schedule(new TimerTask() {
            @Override
            public void run() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        }, 10000);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*if (!mExoPlayerFullscreen)
            openFullscreenDialog(false);
        else
            closeFullscreenDialog(false);*/
        UiUtils.log(TAG, "New Config : " + newConfig.orientation + " --> " + Configuration.ORIENTATION_LANDSCAPE);
        //Toast.makeText(this, "New Config : "+ newConfig.orientation + " --> "+Configuration.ORIENTATION_LANDSCAPE, Toast.LENGTH_LONG).show();
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mExoPlayerFullscreen) openFullScreenUiChange();
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mExoPlayerIsLocked) closeFullScreenUiChange();
            else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFullScreenUiChange() {
        try {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mExoPlayerView.setPlayer(player);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.exo_ic_fullscreen_exit));
            mExoPlayerFullscreen = true;
            volumecontainerView.setVisibility(View.VISIBLE);
            brightnesscontainerView.setVisibility(View.VISIBLE);
            exoLock.setVisibility(View.VISIBLE);
            exo_txt_rating.setTextSize(14);
            exo_txt_genre.setTextSize(12);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_exo_rating.getLayoutParams();
            params.setMargins(Utils.dpToPx(30), Utils.dpToPx(20), 0, 0);
            ll_exo_rating.setLayoutParams(params);
            rl_exo_link.setGravity(Gravity.END | Gravity.BOTTOM);
            FrameLayout.LayoutParams paramsNew = (FrameLayout.LayoutParams) rl_exo_link.getLayoutParams();
            paramsNew.setMargins(0, 0, 0, Utils.dpToPx(40));
            paramsNew.setMarginEnd(Utils.dpToPx(25));
            rl_exo_link.setLayoutParams(paramsNew);
            playerTouch();
            if (ll_exo_rating.getVisibility() == View.VISIBLE) {
                mRatingHandler.removeCallbacks(mRatingRunnable);
                mRatingHandler.postDelayed(mRatingRunnable, 5000);
            }
            mFullScreenDialog.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playerTouch() {
        soundView.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        brightnessView.setProgress(8);

        mExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                UiUtils.log("Touch", "pointer " + motionEvent.getPointerCount() + "," + motionEvent.getAction());
                //scaleGestureDetector.onTouchEvent(motionEvent);
                if (mExoPlayerIsLocked) {
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
                    currentbrightprogress = brightnessView.getProgress();
                    first = true;
                    second = true;
                    selected = 0;

                    UiUtils.log("Touch", "width " + mExoPlayerView.getWidth() + "");

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                    UiUtils.log("Touch", "myrb " + "x=" + lastx + "y=" + downy + "");
                    UiUtils.log("Touch", "myrb " + "x=" + motionEvent.getX() + "y=" + motionEvent.getY() + "," + motionEvent.getAction());
                    float xdistance = motionEvent.getX() - lastx;
                    float ydistance = motionEvent.getY() - downy;
                    UiUtils.log("Touch", "Value " + "x=" + xdistance + "y=" + ydistance + "");
                    if (first && Math.abs(xdistance) == 0 && Math.abs(ydistance) == 0) {

                    } else if ((second && Math.abs(xdistance) < Math.abs(ydistance)) || selected == 1) {
                        if (selected == 0) {
                            selected = 1;
                            first = false;
                            second = true;
                            if (motionEvent.getX() < view.getWidth() / 2.0f) {
                                volumecontainerView.setVisibility(View.VISIBLE);
                                brightnesscontainerView.setVisibility(View.GONE);
                            } else {
                                volumecontainerView.setVisibility(View.GONE);
                                brightnesscontainerView.setVisibility(View.VISIBLE);
                            }
                        }
                        UiUtils.log("Touch", "endheight " + endheight + " y= " + motionEvent.getY());
                        float tempwidth = endheight - motionEvent.getY();
                        UiUtils.log("Touch", "tempwidth " + tempwidth);
                        UiUtils.log("Touch", "sound " + soundView.getMaxprogess() + " diffheight " + diffheight);
                        float progress = (tempwidth * soundView.getMaxprogess()) / diffheight;
                        UiUtils.log("Touch", "progress " + (soundView.getMaxprogess() - progress) + "");
                        float tempnumber = (soundView.getMaxprogess() - progress);
                        int jprogress = (int) tempnumber /*(soundView.getMaxprogess() - progress)*/;
                        if (volumecontainerView.getVisibility() == View.VISIBLE) {
                            int prog = (currentprogress + jprogress);
                            UiUtils.log("Touch", "final progress " + prog + "");
                            //int soundProg =
                            soundView.setProgress(prog);
                            if (prog > soundView.getMaxprogess())
                                soundView.setProgress(soundView.getMaxprogess());
                            else if (prog < 0) soundView.setProgress(0);
                            else {
                                soundView.setProgress(prog);
                            }

                        } else {
                            int prog = currentbrightprogress + jprogress;
                            UiUtils.log("Touch", "final progress " + prog + "");
                            if (prog > brightnessView.getMaxprogess())
                                brightnessView.setProgress(brightnessView.getMaxprogess());
                            else if (prog < 0) brightnessView.setProgress(0);
                            else {
                                float brightness = prog / 15.0f;
                                WindowManager.LayoutParams lp = getWindow().getAttributes();
                                lp.screenBrightness = brightness;
                                getWindow().setAttributes(lp);
                                UiUtils.log("Touch", "final brightness " + brightness + "");
                                brightnessView.setProgress(prog);
                                progress_brightness_text.setText(String.valueOf(prog));
                            }
                        }

                        UiUtils.log("Touch", "scroll " + "vertical");
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
                } else if (motionEvent.getPointerCount() == 1 && motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (isscalegesture) {
                        isscalegesture = false;
                    } else {
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


                } else if (isscalegesture) {

                }


                return false;
            }
        });
    }

    private void closeFullScreenUiChange() {
        try {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mExoPlayerView.setPlayer(player);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
            mExoPlayerFullscreen = false;
            //exoBackBtn.setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rlPlayer)).addView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenDialog.dismiss();
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.exo_ic_fullscreen_enter));
            volumecontainerView.setVisibility(View.GONE);
            brightnesscontainerView.setVisibility(View.GONE);
            exoUnLock.setVisibility(View.GONE);
            exoLock.setVisibility(View.GONE);
            exo_txt_rating.setTextSize(12);
            exo_txt_genre.setTextSize(10);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_exo_rating.getLayoutParams();
            params.setMargins(Utils.dpToPx(16), Utils.dpToPx(16), 0, 0);
            ll_exo_rating.setLayoutParams(params);
            rl_exo_link.setGravity(Gravity.END | Gravity.BOTTOM);
            FrameLayout.LayoutParams paramsNew = (FrameLayout.LayoutParams) rl_exo_link.getLayoutParams();
            paramsNew.setMargins(0, 0, 0, Utils.dpToPx(40));
            paramsNew.setMarginEnd(Utils.dpToPx(8));
            rl_exo_link.setLayoutParams(paramsNew);
            mExoPlayerIsLocked = false;
            mExoPlayerView.setOnTouchListener(null);
            // Reactivate sensor orientation after delay
            if (ll_exo_rating.getVisibility() == View.VISIBLE) {
                mRatingHandler.removeCallbacks(mRatingRunnable);
                mRatingHandler.postDelayed(mRatingRunnable, 5000);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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

            String cookieValue = "CloudFront-Policy=" + MainActivity.COOKIEPOLICY + ";CloudFront-Signature=" + MainActivity.COOKIESIGNATURE + ";CloudFront-Key-Pair-Id=" + MainActivity.COOKIEKEYPAIR;

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookieValue);

            String userAgent = getUserAgent(getApplicationContext(), getApplicationInfo().packageName);
            DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
            httpDataSourceFactory.setUserAgent(userAgent);
            httpDataSourceFactory.setDefaultRequestProperties(headers);
            DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(getApplicationContext(), httpDataSourceFactory);

            //SubtitleConfig
            MediaItem.SubtitleConfiguration subtitleConfig = createSubtitleConfig(isTrailer);

            //Media Item
            MediaItem mediaItem = createMediaItem(fileUrl, subtitleConfig);

            if (getFileExtension(fileUrl).equalsIgnoreCase("m3u8")) {
                UiUtils.log(TAG, "HlsMediaSource");
                mVideoSource = new HlsMediaSource.Factory(dataSourceFactory).setExtractorFactory(HlsExtractorFactory.DEFAULT).createMediaSource(mediaItem);
            } else {
                UiUtils.log(TAG, "ProgressiveMediaSource");
                mVideoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
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
                new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300).setOnFinishListener(new ElasticFinishListener() {
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
//                                    Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
                MediaSource subtitleSource = new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(subtitleConfig, C.TIME_UNSET);
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
            playerMessages.add(player.createMessage((messageType, payload) -> {
                Log.e("TAG", "Hurray! We are at playback position 0:20 --> " + player.getCurrentPosition() / 1000);
                mRatingHandler.removeCallbacks(mRatingRunnable);
                ll_exo_rating.setVisibility(View.VISIBLE);
                Animation slide_up_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_fade_in);
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
            }).setPosition(getTime(timers.get(i))).setHandler(handler).send());
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
                mediaSource = new HlsMediaSource.Factory(dataSourceFactory).setExtractorFactory(HlsExtractorFactory.DEFAULT).createMediaSource(mediaItemExtra);
                UiUtils.log(TAG, "Language Hls->" + streamUrls.get(i));
            } else {
                UiUtils.log(TAG, "ProgressiveMediaSource");
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItemExtra);
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
            mediaItem = new MediaItem.Builder().setUri(fileUrl).setMimeType(MimeTypes.APPLICATION_M3U8).setSubtitleConfigurations(ImmutableList.of(subtitleConfig)).build();
        } else {
            mediaItem = new MediaItem.Builder().setUri(fileUrl).setMimeType(MimeTypes.APPLICATION_M3U8).build();
        }
        return mediaItem;
    }

    private void initExoPlayer() {
        //https://stackoverflow.com/questions/42228653/exoplayer-adaptive-hls-streaming
        UiUtils.log(TAG, "initExoPlayer ");
        ExoTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(0, 0, 0, 0);
        trackSelector = new DefaultTrackSelector(getApplicationContext(), videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

//        trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
//        subtitlesSelectionHelper = new SubtitlesSelectionHelper(trackSelector, videoTrackSelectionFactory);

        if (player == null) {
            UiUtils.log(TAG, "ExoPlayer");
            player = new ExoPlayer.Builder(getApplicationContext()).setRenderersFactory(new DefaultRenderersFactory(getApplicationContext())).setTrackSelector(trackSelector).setLoadControl(loadControl).build();
            exoplayer.setPlayer(player);
            exoplayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        } else {
            UiUtils.log(TAG, "Else ExoPlayer");
            player.stop();
            player.release();

            exoplayer.setPlayer(null);
            player = new ExoPlayer.Builder(getApplicationContext()).setRenderersFactory(new DefaultRenderersFactory(getApplicationContext())).setTrackSelector(trackSelector).setLoadControl(loadControl).build();
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
                    exo_play.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.avd_pause_to_play, null));
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
                    if (trailerPlaying) {
                        if (!checkString(video.getRatingTrailerTime()))
                            timers.addAll(Arrays.asList(video.getRatingTrailerTime().split(",")));
                    } else {
                        if (!checkString(video.getRatingTime()))
                            timers.addAll(Arrays.asList(video.getRatingTime().split(",")));
                    }
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
        btnSettings.setOnClickListener(view -> showQualitySelectionDialog(view));
//      btnAudio.setOnClickListener(view -> showAudioSelectionDialog(view));

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
                Log.d("onClick1e2", "onClick: 1");
                //  initFullscreenDialog();
//                    if (!mExoPlayerFullscreen) {
//                        Log.d("onClick112", "onClick: 1");
//                    //    openFullscreenDialog(true, activity);
//                    } else {
//                        Log.d("onClick112", "onClick: 2");
//                    //    closeFullscreenDialog(true,activity);
//                    }
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
                exo_pause.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.avd_play_to_pause, null));
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
                exo_play.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.avd_pause_to_play, null));
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
                    volumeicon.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.ic_vol_mute, null));
                else
                    volumeicon.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.ic_vol_unmute, null));
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
    private void showQualitySelectionDialog(View view) {

        if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,
                    /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false, C.TRACK_TYPE_DEFAULT, true);
            //trackSelectionDialog.getView().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
        }
    }


    //subtitles change
    private void showSubtitlesSelectionDialog(View view) {

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,
                        /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false, 2, true);
                trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);

            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
            exo_pause.setVisibility(View.GONE);
            exo_play.setVisibility(View.VISIBLE);
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
