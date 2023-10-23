package com.bhoju.app.ui.fragment;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.gcm.MessagingServices;
import com.bhoju.app.model.AgeItem;
import com.bhoju.app.model.Banner;
import com.bhoju.app.model.OfflineVideo;
import com.bhoju.app.model.OfflineVideoSections;
import com.bhoju.app.model.Video;
import com.bhoju.app.model.VideoSection;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.PassCodeCreateActivity;
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.ui.activity.WebViewActivity;
import com.bhoju.app.ui.adapter.AgeAdapter;
import com.bhoju.app.ui.adapter.BannerAdapter;
import com.bhoju.app.ui.adapter.VideoSectionsAdapter;
import com.bhoju.app.util.FixedSpeedScroller;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.ResponsivenessUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.database.DatabaseClient;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.bhoju.app.util.sharedpref.Utils;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.HOME_SLIDER;
import static com.bhoju.app.network.APIConstants.APIs.UPDATE_AGE;
import static com.bhoju.app.network.APIConstants.APIs.USER_SUBSCRIPTION;
import static com.bhoju.app.network.APIConstants.Constants.PAGE_TYPE;
import static com.bhoju.app.network.APIConstants.Params.CATEGORY_ID;
import static com.bhoju.app.network.APIConstants.Params.COOKIE_KEY_PAIR;
import static com.bhoju.app.network.APIConstants.Params.COOKIE_POLICY;
import static com.bhoju.app.network.APIConstants.Params.COOKIE_SIGNATURE;
import static com.bhoju.app.network.APIConstants.Params.DATA;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.GENRE_ID;
import static com.bhoju.app.network.APIConstants.Params.PARENT_MEDIA;
import static com.bhoju.app.network.APIConstants.Params.SLIDER_TYPE;
import static com.bhoju.app.network.APIConstants.Params.SUB_CATEGORY_ID;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEKEYPAIR;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEPOLICY;
import static com.bhoju.app.ui.activity.MainActivity.COOKIESIGNATURE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;
import static com.bhoju.app.ui.activity.MainActivity.skipLock;
import static com.bhoju.app.util.sharedpref.PrefKeys.SUBSCRIPTION_DAYS;
import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.util.ParserUtils.parseVideoSections;

public class VideoContentFragment extends Fragment implements VideoSectionsAdapter.VideoSectionsInterface {

    private final String TAG = VideoContentFragment.class.getSimpleName();

    public static String page_type, category_id;
    MainActivity activity;
    Unbinder unbinder;
    BannerAdapter bannerAdapter;
    VideoSectionsAdapter videoSectionAdapter;
    AgeAdapter ageAdapter;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.gradient_view)
    View gradientView;
    @BindView(R.id.sliderDots)
    DotsIndicator sliderDots;
    @BindView(R.id.homeToolbar)
    Toolbar homeToolbar;
    @BindView(R.id.category_toolbar)
    Toolbar categoryToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView videoSectionsRecycler;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.app_header_icon)
    View appHeaderIcon;
    @BindView(R.id.bannerLayout)
    ViewGroup bannerLayout;
    @BindView(R.id.contentView)
    ViewGroup contentView;
    @BindView(R.id.series)
    TextView series;
    @BindView(R.id.films)
    TextView films;
    @BindView(R.id.kid)
    TextView kid;
    @BindView(R.id.noResultLayout)
    TextView noResultLayout;
    /*@BindView(R.id.view)
    View viewLayout;*/
    @BindView(R.id.layout)
    LinearLayout layout;

    @BindView(R.id.ll_banner_details)
    LinearLayout ll_banner_details;
    @BindView(R.id.banner_video_title)
    TextView banner_video_title;

    @BindView(R.id.rl_parent_control)
    RelativeLayout rl_parent_control;
    @BindView(R.id.txt_parental_control)
    TextView txt_parental_control;
    @BindView(R.id.setup_btn)
    Button setup_btn;
    @BindView(R.id.checkbox_dont_show)
    CheckBox checkbox_dont_show;
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @BindDrawable(R.drawable.ic_done_white_24dp)
    Drawable addedToWishListDrawable;
    @BindDrawable(R.drawable.ic_add_white_24dp)
    Drawable notAddedToWishListDrawable;
    List<Banner> offlineBannerData = new ArrayList<>();
    private boolean isRevealEnabled = true;
    //private Spotlight spotLight;
    private static final String INTRO_CARD = "fab_intro";
    private APIInterface apiInterface;
    private PrefUtils prefUtils;
    private List<Video> bannerVideos = new ArrayList<>();
    private List<AgeItem> ageList = new ArrayList<>();
    private List<VideoSection> videoSections = new ArrayList<>();
    private RecyclerView.OnScrollListener videoSectionsScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager == null) return;
            if (layoutManager.findLastVisibleItemPosition() == (videoSections.size() - 1)) {
                videoSectionAdapter.showLoading();
            }
        }
    };
    private String pageType;
    private int categoryId;
    private int subCategoryId;
    private int genreId;
    //Target seriesTarget, filmTarget, kidTarget, playButtonTarget;
    LinearLayout seriesLayout, kidsLayout, filmLayout;
    CoordinatorLayout seriesRootLayout, kidsRootLayout, filmRootLayout;
    private int count = 0;

    Dialog dialog;
    String selectedAge="";

    private int dotscount;
    private ImageView[] dots;

    public static VideoContentFragment getInstance(String pageType, int categoryId, int subCategoryId, int genreId) {
        VideoContentFragment videoContentFragment = new VideoContentFragment();
        page_type = pageType;
        category_id = String.valueOf(categoryId);
        Bundle bundle = new Bundle();
        bundle.putString(PAGE_TYPE, pageType);
        bundle.putInt(CATEGORY_ID, categoryId);
        bundle.putInt(SUB_CATEGORY_ID, subCategoryId);
        bundle.putInt(GENRE_ID, genreId);
        videoContentFragment.setArguments(bundle);
        return videoContentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new mytimerTask(), 3000, 3000);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(getActivity());
        if(prefUtils.getStringValue(PrefKeys.FCM_TOKEN,"").equalsIgnoreCase(""))
            MessagingServices.getFCMToken(activity);
        homeToolbar.getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        Utils.getPublicIpAddress(getActivity(), true);

//        if(!PrefUtils.getInstance(getActivity()).getBoolanValue(PrefKeys.FIRST_TIME_LOGIN, false)) {
        /*series.post(() -> {
            if (series.isShown()) {
                seriesTarget = generateTarget(series);
                setTargetsOne(seriesTarget);
            }
        });

        films.post(() -> {
            if (films.isShown()) {
                filmTarget = generateTargetTwo(films);
            }
        });

        kid.post(() -> {
            if (kid.isShown()) {
                kidTarget = generateTargetThree(kid);
            }
        });*/

//        }
        //if (PrefUtils.getInstance(activity).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)
        //        && prefUtils.getStringValue(PrefKeys.USER_AGE, "").equalsIgnoreCase("")) {
        /*if(!prefUtils.getBoolanValue(PrefKeys.TERMS_AND_CONDITION, false))
            showTermsPrivacyPopup();*/
        //}

        if (PrefUtils.getInstance(activity).getBoolanValue(PrefKeys.IS_PASS, false) || skipLock)
            rl_parent_control.setVisibility(View.GONE);

        /* rl_parent_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PassCodeActivity.class);
                startActivityForResult(intent,1234);
            }
        });*/

        txt_skip.setOnClickListener(v -> {
            if(checkbox_dont_show.isChecked()) {
                PrefUtils prefUtils = PrefUtils.getInstance(activity);
                prefUtils.setValue(PrefKeys.IS_PASS, true);
                prefUtils.setValue(PrefKeys.PASS_CODE, "");
            }else
                skipLock = true;
            rl_parent_control.setVisibility(View.GONE);

        });

        viewPager.setClipToPadding(false);
        viewPager.setPadding(0,0,0,0);
        //viewPager.setPageMargin(5);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

               /* for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));*/
                Video video = bannerVideos.get(position);
                banner_video_title.setText(video.getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setLayoutParams(ResponsivenessUtils.getLayoutParamsForFullWidthSeasonView(getContext()));
        gradientView.setLayoutParams(ResponsivenessUtils.getLayoutParamsForFullWidthSeasonView(getContext()));

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            // scroller.setFixedDuration(5000);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        setup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PassCodeCreateActivity.class);
                intent.putExtra("screen", false);
                startActivityForResult(intent,1234);
            }
        });

        checkWidth();

        /*SpannableString parental_Control = new SpannableString(getString(R.string.parental_control));
        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(activity, PassCodeCreateActivity.class);
                intent.putExtra("screen", false);
                startActivityForResult(intent,1234);

            }
        };

        parental_Control.setSpan(teremsAndCondition, 0, 10, 0);
        parental_Control.setSpan(new ForegroundColorSpan(activity.getColor(R.color.pink)), 0, 10, 0);

        txt_parental_control.setMovementMethod(LinkMovementMethod.getInstance());
        txt_parental_control.setText(parental_Control, TextView.BufferType.SPANNABLE);
        txt_parental_control.setSelected(true);*/

        //Temp Notification
        //id=256&token=2y10sKupYzqxPPKjXCIDztHn96HxW2zjQbMul5lLvJ0PhnFODCGM7y&sub_profile_id=0&admin_video_id=41&language=
        /*Intent intent = new Intent(getApplicationContext(), VideoPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("videoId", 41);
        intent.putExtra("userName", "");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Test")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
        if (manager != null) {
            manager.notify(0, builder.build());
        }*/


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1234:
                if (PrefUtils.getInstance(activity).getBoolanValue(PrefKeys.IS_PASS, false))
                    rl_parent_control.setVisibility(View.GONE);

        }
    }

    @Override
    public void onResume() {
        Utils.sendFCMTokenToServerForRef(getActivity());
        super.onResume();
    }

    public void showTermsPrivacyPopup()
    {

        //Now we need an AlertDialog.Builder object
        dialog = new Dialog(activity);

        dialog.setContentView(R.layout.dialog_terms_and_privacy);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        dialog.findViewById(R.id.submit_btn).setOnClickListener((View v) -> {
            dialog.dismiss();
            prefUtils.setValue(PrefKeys.TERMS_AND_CONDITION, true);
        });

        /*dialog.findViewById(R.id.close_btn).setOnClickListener((View v) -> {
            dialog.dismiss();
            getActivity().finish();
        });*/

        TextView termsCondition = dialog.findViewById(R.id.termsCondition);
        SpannableString terms_Condition = new SpannableString(getString(R.string.terms_and_privacypolicy));
        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));

            }
        };

        terms_Condition.setSpan(teremsAndCondition, 13, 31, 0);
        terms_Condition.setSpan(new ForegroundColorSpan(activity.getColor(R.color.theme_green)), 13, 31, 0);

        termsCondition.setMovementMethod(LinkMovementMethod.getInstance(

        ));
        termsCondition.setText(terms_Condition, TextView.BufferType.SPANNABLE);
        termsCondition.setSelected(true);


        TextView termsPrivacy = dialog.findViewById(R.id.termsPrivacy);
        SpannableString terms_Privacy = new SpannableString(getString(R.string.terms_privacy_policy));
        ClickableSpan teremsPrivacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.PRIVACY));

            }
        };

        terms_Privacy.setSpan(teremsPrivacy, 0, 14, 0);
        terms_Privacy.setSpan(new ForegroundColorSpan(activity.getColor(R.color.theme_green)), 0, 14, 0);

        termsPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        termsPrivacy.setText(terms_Privacy, TextView.BufferType.SPANNABLE);
        termsPrivacy.setSelected(true);

        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_UP) {
                // TODO do the "back pressed" work here
                getActivity().finish();
                return true;
            }
            return false;
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showAgePicker()
    {

        //Now we need an AlertDialog.Builder object
        dialog = new Dialog(activity);

        dialog.setContentView(R.layout.dialog_age_selection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        LinearLayout ll_subscription = dialog.findViewById(R.id.ll_subscription);
        ll_subscription.setVisibility(View.GONE);

        CheckBox checkBox =dialog.findViewById(R.id.termsConditionCheck);
        RecyclerView rv_age = dialog.findViewById(R.id.rv_age);
        setAgeListAdapter(rv_age,checkBox);

        TextView termsPrivacy = dialog.findViewById(R.id.termsPrivacy);
        //dialog.findViewById(R.id.btnCancel).setOnClickListener((View v) -> { dialog.dismiss(); });
        SpannableString terms_Privacy = new SpannableString(getString(R.string.terms_and_privacypolicy));
        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.TERMS));

            }
        };

        terms_Privacy.setSpan(teremsAndCondition, 12, 31, 0);
        terms_Privacy.setSpan(new ForegroundColorSpan(activity.getColor(R.color.theme_green)), 12, 31, 0);
        //terms_Privacy.setSpan(new ForegroundColorSpan(activity.getColor(R.color.theme_green)), 41, 55, 0);

        termsPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        termsPrivacy.setText(terms_Privacy, TextView.BufferType.SPANNABLE);
        termsPrivacy.setSelected(true);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UiUtils.log("TAG","sgfr: "+ isChecked );
            if(isChecked)
            {
                if(selectedAge.equals(""))
                    Toast.makeText(activity,"Please select age",Toast.LENGTH_SHORT).show();
                else
                    sendAgeToBackend(selectedAge);
            }
        });

        dialog.show();
    }

    private void setAgeListAdapter(RecyclerView rv_age, CheckBox checkBox) {

        List<String> ageArray = Arrays.asList(getResources().getStringArray(R.array.ageList));
        ageList.clear();
        for(int i=0;i<ageArray.size();i++) {
            //sendAgeToBackend();
            ageList.add(new AgeItem(ageArray.get(i),v -> {
                selectedAge = ((TextView) v.findViewById(R.id.ageName)).getText().toString();
                UiUtils.log(TAG,"sgfr: "+ ((TextView) v.findViewById(R.id.ageName)).getText().toString());
                if(checkBox.isChecked())
                    sendAgeToBackend(selectedAge);
                else
                    Toast.makeText(activity,"Please accept terms and condition",Toast.LENGTH_SHORT).show();
                ageAdapter.notifyDataSetChanged();
            }));//UiUtils.showShortToast(activity,((TextView)v.findViewById(R.id.ageName)).getText().toString())));
        }

        ageAdapter = new AgeAdapter(activity, (ArrayList<AgeItem>) ageList);
        rv_age.setLayoutManager(new LinearLayoutManager(activity));
        rv_age.setItemAnimator(new DefaultItemAnimator());
        rv_age.setAdapter(ageAdapter);
    }

    private void sendAgeToBackend(String age) {
        UiUtils.showLoadingDialog(activity);

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.AGE, age);
        params.put(Params.REGION_NAME, "");
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.updateAge(UPDATE_AGE, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();

                JSONObject homeDynamicResponse = null;
                try {
                    homeDynamicResponse = new JSONObject(response.body());
                }  catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (homeDynamicResponse != null) {
                    if (homeDynamicResponse.optString(SUCCESS).equals(Constants.TRUE)) {
                        //JSONArray dynamicVideoArr = homeDynamicResponse.optJSONArray(Params.DATA);
                        dialog.dismiss();
                        prefUtils.setValue(PrefKeys.USER_AGE, age);

                    } else {
                        UiUtils.showShortToast(activity, homeDynamicResponse.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(activity);
            }
        });
    }

//    public void setTargetsOne(Target view1) {
//        if(view1 != null)
//        new Spotlight.Builder(getActivity())
//                .setAnimation(new DecelerateInterpolator(2f))
//                .setTargets(seriesTarget, filmTarget, kidTarget)
//                .setOnSpotlightListener(new OnSpotlightListener() {
//                    @Override
//                    public void onStarted() {
//                        Toast.makeText(getActivity(), "Spotlight 1 started", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onEnded() {
//                        Toast.makeText(getActivity(), "spotlight 1 ended", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .build().start();
//    }
//
//    public Target generateTarget(View point) {
//        seriesRootLayout = new CoordinatorLayout(getActivity());
//        View seriesView = getLayoutInflater().inflate(R.layout.spotlight_layout, seriesRootLayout);
//        seriesLayout = seriesView.findViewById(R.id.series_layout);
//        seriesLayout.setOnClickListener(v -> {
//            seriesLayout.setVisibility(View.GONE);
////            setTargetTwo(filmTarget);
//        });
//        return new Target.Builder()
//                .setAnchor(point)
//                .setOverlay(seriesView)
//                .setShape(new Circle(100, 1000, new DecelerateInterpolator(2f)))
//                .build();
//    }
//
//    public void setTargetTwo(Target view) {
//        if(view != null)
//        new Spotlight.Builder(getActivity())
//                .setAnimation(new DecelerateInterpolator(2f))
//                .setTargets(view)
//                .setOnSpotlightListener(new OnSpotlightListener() {
//                    @Override
//                    public void onStarted() {
//                        seriesLayout.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "Spotlight 2 started", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onEnded() {
//                        Toast.makeText(getActivity(), "spotlight 2 ended", Toast.LENGTH_SHORT).show();
//                        filmLayout.setVisibility(View.GONE);
//                    }
//                })
//                .build().start();
//    }
//
//    public Target generateTargetTwo(View point) {
//        filmRootLayout = new CoordinatorLayout(getActivity());
//        View filmView = getLayoutInflater().inflate(R.layout.film_layout, filmRootLayout);
//        LinearLayout filmLayout = filmView.findViewById(R.id.filmLayout);
//        filmLayout.setOnClickListener(v -> {
//            filmLayout.setVisibility(View.GONE);
////            setTargetThree(kidTarget);
//        });
//        return new Target.Builder()
//                .setAnchor(point)
//                .setOverlay(filmView)
//                .setShape(new Circle(100, 1000, new DecelerateInterpolator(2f)))
//                .build();
//    }
//
//    public Target generateTargetThree(View point) {
//        kidsRootLayout = new CoordinatorLayout(getActivity());
//        View kidsView = getLayoutInflater().inflate(R.layout.kids_layout, kidsRootLayout);
//        LinearLayout kidLayout = kidsView.findViewById(R.id.kidsLayout);
//        kidLayout.setOnClickListener(v -> {
//            kidsView.setVisibility(View.GONE);
//        });
//
//        return new Target.Builder()
//                .setAnchor(point)
//                .setOverlay(kidsView)
//                .setShape(new Circle(100, 1000, new DecelerateInterpolator(2f)))
//                .build();
//    }
//
//    public void setTargetThree(Target view) {
//        if(view != null)
//        new Spotlight.Builder(getActivity())
//                .setAnimation(new DecelerateInterpolator(2f))
//                .setTargets(view)
//                .setOnSpotlightListener(new OnSpotlightListener() {
//                    @Override
//                    public void onStarted() {
//                        Toast.makeText(getActivity(), "Spotlight 3 started", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void onEnded() {
//                        Toast.makeText(getActivity(), "spotlight 3 ended", Toast.LENGTH_SHORT).show();
//                        kidsLayout.setVisibility(View.GONE);
//                    }
//                })
//                .build().start();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryToolbar.setVisibility(View.GONE);
        homeToolbar.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        appHeaderIcon.setOnClickListener(null);
        /*if (bundle != null) {
            switch (bundle.getString(PAGE_TYPE, TYPE_HOME)) {
                case TYPE_SERIES:
                    series.setVisibility(View.VISIBLE);
                    appHeaderIcon.setOnClickListener(v -> onActivityBackPressed());
                    break;
                case TYPE_FILMS:
                    films.setVisibility(View.VISIBLE);
                    appHeaderIcon.setOnClickListener(v -> onActivityBackPressed());
                    break;
                case TYPE_KIDS:
                    kid.setVisibility(View.VISIBLE);
                    appHeaderIcon.setOnClickListener(v -> onActivityBackPressed());
                    break;
                case TYPE_CATEGORY:
                    homeToolbar.setVisibility(View.GONE);
                    categoryToolbar.setVisibility(View.VISIBLE);
                    //categoryToolbar.setTitle(CategoryFragment.categoryBeingViewed);
                    categoryToolbar.setNavigationOnClickListener(v -> onActivityBackPressed());
                    homeToolbar.setVisibility(View.GONE);
                    viewLayout.setVisibility(View.GONE);
                    break;
                default:
                    kid.setVisibility(View.VISIBLE);
                    films.setVisibility(View.VISIBLE);
                    series.setVisibility(View.VISIBLE);
                    break;
            }
        }*/


        if (bundle != null) {
            pageType = bundle.getString(PAGE_TYPE);
            categoryId = bundle.getInt(CATEGORY_ID);
            subCategoryId = bundle.getInt(SUB_CATEGORY_ID);
            genreId = bundle.getInt(GENRE_ID);
        }

        getVideoSections();
        getHomeSlider();
        if(PrefUtils.getInstance(activity).getBoolanValue(PrefKeys.IS_LOGGED_IN, false))
            getCookieData();
    }

    private void getCookieData() {
        UiUtils.showLoadingDialog(activity);
        Call<String> call = apiInterface.getAllCookies(APIConstants.APIs.GET_ALL_COOKIES);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject cookieResponse = null;
                try {
                    cookieResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (cookieResponse != null) {
                    if (cookieResponse.optString(SUCCESS).equals(Constants.TRUE)) {
                        JSONObject data = cookieResponse.optJSONObject(DATA);
                        COOKIEKEYPAIR = data.optString(COOKIE_KEY_PAIR);
                        COOKIEPOLICY = data.optString(COOKIE_POLICY);
                        COOKIESIGNATURE = data.optString(COOKIE_SIGNATURE);
                        UiUtils.log(TAG,"COOKIEKEYPAIR:"+COOKIEKEYPAIR
                                +"\nCOOKIEPOLICY:"+COOKIEPOLICY
                                +"\nCOOKIESIGNATURE:"+COOKIESIGNATURE);
                    } else {
                        UiUtils.showShortToast(activity, cookieResponse.optString(ERROR_MESSAGE));
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

    private void getBannerData() {
        class GetOfflineVideos extends AsyncTask<Void, Void, List<VideoSection>> {

            @Override
            protected List<VideoSection> doInBackground(Void... voids) {
                offlineBannerData = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllBannerVideos();

                if(offlineBannerData.size()>0) {
                    Video video = new Video();
                    video.setThumbNailUrl(offlineBannerData.get(0).getThumbNailUrl());
                    video.setDefaultImage(offlineBannerData.get(0).getDefault_image());
                    video.setAdminVideoId(offlineBannerData.get(0).getAdminVideoId());
                    video.setTitle(offlineBannerData.get(0).getTitle());
                    video.setSlider_type("media");
                    bannerVideos.add(video);
                }
                return videoSections;
            }


            @Override
            protected void onPostExecute(List<VideoSection> tasks) {
                super.onPostExecute(tasks);
                setBannerAdapter(bannerVideos);
                UiUtils.hideLoadingDialog();
            }
        }

        GetOfflineVideos offlineVideos = new GetOfflineVideos();
        offlineVideos.execute();
    }

    private void onActivityBackPressed() {
        if (getActivity() != null)
            getActivity().onBackPressed();
    }

    //Banner wishListAdapter
    private void setUpBannerItems(JSONObject bannerObj) {
        if (bannerObj != null) {
            JSONArray data = bannerObj.optJSONArray(DATA);
            UiUtils.log("Banner  List", String.valueOf(data));
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject bannerVideoObj = data.getJSONObject(i);
                        Video bannerVideo = new Video();
                        bannerVideo.setThumbNailUrl(bannerVideoObj.optString(Params.MOBILE_IMAGE));
                        bannerVideo.setDefaultImage(bannerVideoObj.optString(Params.DEFAULT_IMAGE));
                        bannerVideo.setTitle(bannerVideoObj.optString(Params.TITLE));
                        bannerVideo.setAdminVideoId(bannerVideoObj.optInt(Params.ADMIN_VIDEO_ID));
                        //bannerVideo.setCategoryId(bannerVideoObj.optInt(Params.CATEGORY_ID));
                        //bannerVideo.setSubCategoryId(bannerVideoObj.optInt(Params.SUB_CATEGORY_ID));
                        //bannerVideo.setInWishList(bannerVideoObj.optInt(Params.WISHLIST_STATUS) == 1);
                        //bannerVideo.setGenreId(bannerVideoObj.optInt(Params.GENRE_ID));
                        bannerVideo.setA_record(Math.max(bannerVideoObj.optInt(Params.A_RECORD), 0));
                        bannerVideo.setSlider_type(bannerVideoObj.optString(SLIDER_TYPE));
                        bannerVideo.setParent_media(bannerVideoObj.optString(PARENT_MEDIA));

                        bannerVideos.add(bannerVideo);
                        //saveBannerData(data);
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                }
                /*Video bannerVideo = new Video();
                bannerVideo.setTitle("Embedded Youtube");
                bannerVideo.setSlider_type("youtube");
                bannerVideos.add(bannerVideo);
                Video bannerVideo1 = new Video();
                bannerVideo1.setTitle("Inside App");
                bannerVideo1.setSlider_type("Inside App");
                bannerVideos.add(bannerVideo1);*/
            }
        }
        setBannerAdapter(bannerVideos);

    }

    private void setBannerAdapter(List<Video> bannerVideos) {
        try {
            bannerAdapter = new BannerAdapter(activity, (ArrayList<Video>) bannerVideos);
            viewPager.setAdapter(bannerAdapter);
            bannerLayout.setVisibility(bannerVideos.isEmpty() ? View.GONE : View.VISIBLE);
            //banner_video_title.setVisibility(bannerVideos.isEmpty() ? View.GONE : View.VISIBLE);
            if (bannerVideos.size() > 0) {
                banner_video_title.setText(bannerVideos.get(0).getTitle());
                sliderDots.setViewPager(viewPager);
                //setDots();
            }
        }catch (Exception e)
        {}
    }

    private void setDots()
    {
        try {

            dotscount = bannerAdapter.getCount();
            dots = new ImageView[dotscount];

            for (int i = 0; i < dotscount; i++) {

                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                sliderDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * Method to get the videos for the fragment in appropriate screens
     */
    private void getHomeSlider() {
        /*UiUtils.showLoadingDialog(activity);
        noResultLayout.setVisibility(View.GONE);

        contentView.setVisibility(View.GONE);*/
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);
        params.put(APIConstants.Params.APPVERSION, BuildConfig.VERSION_NAME);
        params.put(VERSION_CODE, BuildConfig.VERSION_CODE);

        Call<String> call = apiInterface.getHomeSliderContentFor(HOME_SLIDER, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;

                /*UiUtils.hideLoadingDialog();
                contentView.setVisibility(View.VISIBLE);*/
                JSONObject sliderResponse = null;
                try {
                    sliderResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (sliderResponse != null) {
                    if (sliderResponse.optString(SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.log(TAG," Slider API");
                        /*prefUtils.setValue(SUBSCRIPTION_DAYS,sliderResponse.optInt(Params.SUBSCRIPTION_COUNT));
                        if(prefUtils.getBoolanValue(PrefKeys.IS_LOGGED_IN, false) && sliderResponse.optInt(Params.SUBSCRIPTION_COUNT)<1)
                            getSubscriptionStatus();*/
                        parseSliderContentAndSetDisplay(sliderResponse);
                    } else {
                        UiUtils.showShortToast(activity, sliderResponse.optString(ERROR_MESSAGE));
                    }
                }

                //Show empty
                if (bannerVideos.isEmpty()) {
                    //contentView.setVisibility(View.GONE);
                    bannerLayout.setVisibility(View.GONE);
                    //noResultLayout.setVisibility(View.VISIBLE);
                } else {
                    //contentView.setVisibility(View.VISIBLE);
                    bannerLayout.setVisibility(View.VISIBLE);
                    //noResultLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*if (!isAdded()) return;
                contentView.setVisibility(View.VISIBLE);
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(activity);
                getBannerData();
                getOfflineVideoSec();*/
                setBannerAdapter(bannerVideos);
            }
        });

    }

    private void getVideoSections() {
        UiUtils.showLoadingDialog(activity);
        noResultLayout.setVisibility(View.GONE);
        //contentView.setVisibility(View.GONE);

        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.PAGE_TYPE, pageType);
        params.put(CATEGORY_ID, categoryId);
        params.put(SUB_CATEGORY_ID, subCategoryId);
        params.put(GENRE_ID, genreId);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);
        params.put(APIConstants.Params.APPVERSION, BuildConfig.VERSION_NAME);
        params.put(VERSION_CODE, BuildConfig.VERSION_CODE);

        Call<String> call = apiInterface.getVideoContentFor(APIConstants.APIs.GET_VIDEO_CONTENT_FOR, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!isAdded()) return;

                UiUtils.hideLoadingDialog();
                contentView.setVisibility(View.VISIBLE);
                JSONObject homeResponse = null;
                try {
                    homeResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (homeResponse != null) {
                    videoSections.clear();
                    if (homeResponse.optString(SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.log(TAG," parseContentAndSetDisplay");
                        prefUtils.setValue(SUBSCRIPTION_DAYS,homeResponse.optInt(Params.SUBSCRIPTION_COUNT));
                        if(prefUtils.getBoolanValue(PrefKeys.IS_LOGGED_IN, false) && homeResponse.optInt(Params.SUBSCRIPTION_COUNT)<1)
                            getSubscriptionStatus();
                        parseContentAndSetDisplay(homeResponse);
                    } else {
                        UiUtils.showShortToast(activity, homeResponse.optString(ERROR_MESSAGE));
                    }
                }

                //Show empty
                if (videoSections.isEmpty()) {
                    //videoSectionsRecycler.setVisibility(View.GONE);
                    //noResultLayout.setVisibility(View.VISIBLE);
                    //getVideoSectionsDynamicWith(0);
                } else {
                    videoSectionsRecycler.setVisibility(View.VISIBLE);
                    noResultLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!isAdded()) return;
                contentView.setVisibility(View.VISIBLE);
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(activity);
                //getBannerData();
                //getOfflineVideoSec();
            }
        });

    }

    private void getSubscriptionStatus()
    {
        UiUtils.log("TAG","API Called");
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.IP, CURRENT_IP);

        Call<String> call = apiInterface.getSubscriptionStatus(USER_SUBSCRIPTION, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject subscriptionStatusResponse = null;
                try {
                    subscriptionStatusResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                /*if (subscriptionStatusResponse != null) {
                    supportArrayList = parseSupportData(subscriptionStatusResponse);
                }*/
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(getActivity());
            }
        });
    }

    private void getVideoSectionsDynamicWith(int skip) {

        UiUtils.showLoadingDialog(activity);

        UiUtils.log(TAG,"Second IP: "+ CURRENT_IP);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.PAGE_TYPE, pageType);
        params.put(CATEGORY_ID, categoryId);
        params.put(SUB_CATEGORY_ID, subCategoryId);
        params.put(GENRE_ID, genreId);
        params.put(Params.SKIP, skip);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);
        params.put(APIConstants.Params.APPVERSION, BuildConfig.VERSION_NAME);
        params.put(VERSION_CODE, BuildConfig.VERSION_CODE);

        Call<String> call = apiInterface.getVideoContentDynamicFor(APIConstants.APIs.GET_VIDEO_CONTENT_DYNAMIC_FOR, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                if (isAdded()) {
                    videoSectionAdapter.dismissLoading();
                    JSONObject homeDynamicResponse = null;
                    try {
                        homeDynamicResponse = new JSONObject(response.body());
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                    if (homeDynamicResponse != null) {
                        if (homeDynamicResponse.optString(SUCCESS).equals(Constants.TRUE)) {
                            JSONArray dynamicVideoArr = homeDynamicResponse.optJSONArray(DATA);
                            parseAndAddVideoSections(dynamicVideoArr);

                            //Should be after adding because scroll listener should only act on adding first set of dynamic content
                            if (skip == 0) {
                                videoSectionsRecycler.addOnScrollListener(videoSectionsScrollListener);
                            }


                        } else {
                            UiUtils.showShortToast(activity, homeDynamicResponse.optString(ERROR_MESSAGE));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(activity);
                UiUtils.hideLoadingDialog();
            }
        });
    }

    private void saveBannerData(JSONArray data) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction()
                        .truncateBannerVideos();

                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject bannerVideoObj = data.getJSONObject(i);
                            Banner bannerVideo = new Banner();
                            bannerVideo.setThumbNailUrl(bannerVideoObj.optString(Params.BANNER_IMAGE));
                            bannerVideo.setDefault_image(bannerVideoObj.optString(Params.BANNER_IMAGE));
                            bannerVideo.setTitle(bannerVideoObj.optString(Params.TITLE));
                            bannerVideo.setAdminVideoId(bannerVideoObj.optInt(Params.ADMIN_VIDEO_ID));
                            offlineBannerData.add(bannerVideo);
                        } catch (Exception e) {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                    }
                }
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction()
                        .insertVideoIntoBanner(offlineBannerData);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    private void parseSliderContentAndSetDisplay(JSONObject sliderResponseObj) {
        bannerVideos.clear();

        if (NetworkUtils.isNetworkConnected(getActivity())) {
            //Banner items
            //JSONObject bannerObj = sliderResponseObj.optJSONObject(Params.DATA);
            setUpBannerItems(sliderResponseObj);
        } else {
            //getBannerData();
        }
    }

    private void parseContentAndSetDisplay(JSONObject videoResponseObj) {
        /*bannerVideos.clear();

        if (NetworkUtils.isNetworkConnected(getActivity())) {
            //Banner items
            JSONObject bannerObj = videoResponseObj.optJSONObject(Params.BANNER);
            setUpBannerItems(bannerObj);
        } else {
            getBannerData();
        }*/

        //Video sections
        //Set wishListAdapter

        if (NetworkUtils.isNetworkConnected(getActivity())) {
            setWishListAdapter(videoSections);
            JSONArray data = videoResponseObj.optJSONArray(DATA);
            parseAndAddVideoSections(data);
        } else {
            //getOfflineVideoSec();
        }

        JSONObject originals = videoResponseObj.optJSONObject(Params.ORIGINALS);
        parseAndAddOriginalVideoSections(originals);

        //Dynamic stuff
        videoSectionAdapter.setDynamicSectionPadding();
        videoSectionsRecycler.setNestedScrollingEnabled(false);
//        videoSectionAdapter.showLoading();

        getVideoSectionsDynamicWith(0);
    }

    private void getOfflineVideoSec() {
        class GetOfflineVideos extends AsyncTask<Void, Void, List<VideoSection>> {
            List<OfflineVideoSections> videoSection = new ArrayList<>();
            List<OfflineVideo> offlineVideos = new ArrayList<>();
            List<Video> videoList = new ArrayList<>();

            @Override
            protected List<VideoSection> doInBackground(Void... voids) {
                videoSection = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getVideoSections();

                offlineVideos = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getVideoOffline();

                try {
                    for (int i = 0; i < videoSection.size(); i++) {
                        VideoSection section = new VideoSection();
                        section.setTitle(videoSection.get(i).getTitle());
                        for (int j = 0; j < offlineVideos.size(); j++) {
                            Video offlineVideo = new Video();
                            offlineVideo.setTitle(offlineVideos.get(j).getTitle());
                            offlineVideo.setThumbNailUrl(offlineVideos.get(j).getThumbNailUrl());
                            offlineVideo.setAdminVideoId(offlineVideos.get(j).getAdminVideoId());
                            videoList.add(offlineVideo);
                        }
                        section.setVideos(videoList);
                        videoSections.add(section);
                    }
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                return videoSections;
            }

            @Override
            protected void onPostExecute(List<VideoSection> tasks) {
                super.onPostExecute(tasks);
                setWishListAdapter(videoSections);
                UiUtils.hideLoadingDialog();
            }
        }

        GetOfflineVideos offlineVideos = new GetOfflineVideos();
        offlineVideos.execute();
    }

    private void setWishListAdapter(List<VideoSection> videoSections) {
        videoSectionAdapter = new VideoSectionsAdapter(activity, this, (ArrayList<VideoSection>) videoSections, page_type, category_id);
        videoSectionsRecycler.setLayoutManager(new LinearLayoutManager(activity));
        videoSectionsRecycler.setItemAnimator(new DefaultItemAnimator());
        videoSectionsRecycler.setAdapter(videoSectionAdapter);
    }

    private void parseAndAddVideoSections(JSONArray normalSections) {
        //Setup Normal sections
        if (normalSections != null) {
            ArrayList<VideoSection> videoSection = parseVideoSections(normalSections, getActivity());
            if (videoSection.isEmpty())
                videoSectionsRecycler.removeOnScrollListener(videoSectionsScrollListener);
            videoSections.addAll(videoSection);
            videoSectionAdapter.notifyDataSetChanged();
        }
    }

    private void parseAndAddOriginalVideoSections(JSONObject originals) {
        //Setup Originals sections
        VideoSection originalsSection = ParserUtils.parseOriginalsVideos(originals);
        if (originalsSection != null) {
            videoSections.add(originalsSection);
            videoSectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void replaceFragmentWithAnimation(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        MainActivity.CURRENT_FRAGMENT = tag;
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onLoadMore(int skip) {
        getVideoSectionsDynamicWith(skip);
    }

    public class mytimerTask extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if(bannerAdapter!=null) {
                        if (count == bannerAdapter.getCount()) {
                            count = 0;
                        } else {
                            count++;
                        }
                        if (count <= bannerAdapter.getCount()) {
                            if(viewPager!=null) {
                                viewPager.setCurrentItem(count);
                            }
                        }
                    }
                });
            }

        }
    }

    private void checkWidth()
    {
        try {

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

            UiUtils.log(TAG, "Width: " + width);
            UiUtils.log(TAG, "Width dp: " + dp);

            if (dp < 400) {
                txt_parental_control.setTextSize(15);
                checkbox_dont_show.setTextSize(14);
                txt_skip.setTextSize(14);
                setup_btn.setTextSize(11);
            }

            if (dp > 800) {

            /*viewPager.setPadding(30,0,30,0);
            viewPager.setPageMargin(5);*/
                RelativeLayout.LayoutParams title_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                title_params.addRule(RelativeLayout.ALIGN_BOTTOM,viewPager.getId());
                title_params.setMargins(150, 0, 150, 100);
                ll_banner_details.setLayoutParams(title_params);
                banner_video_title.setTextSize(18);
            }

        /*if(dp>700)
        {
            int margin = (int) (30f * Resources.getSystem().getDisplayMetrics().density);
            UiUtils.log(TAG, "margin: "+ margin);

            RelativeLayout.LayoutParams head_params = (RelativeLayout.LayoutParams)viewPager.getLayoutParams();
            head_params.height = (int) getResources().getDimension(R.dimen.videoThumbnailHeight2);
            viewPager.setLayoutParams(head_params);
        }*/
        }catch (Exception e)
        {
            UiUtils.log(TAG, "Error: " + e.getMessage());
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }
}
