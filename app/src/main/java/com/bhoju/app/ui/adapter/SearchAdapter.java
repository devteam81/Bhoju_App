package com.bhoju.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bhoju.app.util.UiUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.ui.activity.VideoPageActivity;
import com.bhoju.app.util.ResponsivenessUtils;
import com.skydoves.elasticviews.ElasticAnimation;
import com.skydoves.elasticviews.ElasticFinishListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.network.APIConstants.Params.IMAGE;
import static com.bhoju.app.network.APIConstants.Params.ORIGINAL;
import static com.bhoju.app.network.APIConstants.Params.PARENT_ID;
import static com.bhoju.app.network.APIConstants.Params.TITLE;
import static com.bhoju.app.util.UiUtils.animationObject;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = SearchAdapter.class.getSimpleName();

    public static final int VIDEO_SECTION_TYPE_ORIGINALS = 1;
    public static final int VIDEO_SECTION_TYPE_NORMAL = 2;

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Video> videos;
    private int viewType;
    private boolean isInSinglePage;

    public SearchAdapter(Context context, ArrayList<Video> videos, int viewType, boolean isInSinglePage) {
        this.context = context;
        this.videos = videos;
        this.viewType = viewType;
        this.isInSinglePage = isInSinglePage;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (viewType) {
            case VIDEO_SECTION_TYPE_ORIGINALS:
                view = inflater.inflate(R.layout.item_video_long, viewGroup, false);
                return new SearchAdapter.OriginalsViewHolder(view);
            default:
                view = inflater.inflate(R.layout.item_video_search, viewGroup, false);
                return new SearchAdapter.NormalVideoViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Video video = videos.get(position);
        switch (viewType) {
            case VIDEO_SECTION_TYPE_ORIGINALS:
                SearchAdapter.OriginalsViewHolder originalsViewHolder = (SearchAdapter.OriginalsViewHolder) viewHolder;
                Glide.with(context).load(video.getThumbNailUrl()).thumbnail(0.5f)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .apply(new RequestOptions().placeholder(R.drawable.bhoju_placeholder_vertical).error(R.drawable.bhoju_placeholder_vertical).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                        .into(originalsViewHolder.item);
                originalsViewHolder.item.setOnClickListener(v -> {
                    new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                            .setOnFinishListener(new ElasticFinishListener() {
                                @Override
                                public void onFinished() {
                                    // Do something after duration time
                                    takeToVideoPage(originalsViewHolder.item, originalsViewHolder.tvMovieTitle, video);
                                }
                            }).doAction();

                });
              /*  normalVideoViewHolder.videoTileRoot.setOnLongClickListener(v -> {
                    UiUtils.showShortToast(context, video.getTitle());
                    return true;
                });*/
                //setRating(originalsViewHolder.imgRating,video.getA_record());
                originalsViewHolder.tvMovieTitle.setText(video.getTitle());
                originalsViewHolder.item.setLayoutParams(ResponsivenessUtils.getLayoutParamsForOrignalAdapter(context, true));
                checkWidth(originalsViewHolder.tvMovieTitle, originalsViewHolder.imgRating);
                break;
            default:
                SearchAdapter.NormalVideoViewHolder normalVideoViewHolder = (SearchAdapter.NormalVideoViewHolder) viewHolder;
                Glide.with(context).load(video.getDefaultImage()).thumbnail(0.5f)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .apply(new RequestOptions().placeholder(R.drawable.bhoju_placeholder_horizontal).error(R.drawable.bhoju_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                        .into(normalVideoViewHolder.item);
                normalVideoViewHolder.item.setOnClickListener(v -> {
                    new ElasticAnimation(v).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                        .setOnFinishListener(new ElasticFinishListener() {
                            @Override
                            public void onFinished() {
                                // Do something after duration time
                                takeToVideoPage(normalVideoViewHolder.item, normalVideoViewHolder.tvMovieTitle, video);
                            }
                        }).doAction();

        });
              /*  normalVideoViewHolder.videoTileRoot.setOnLongClickListener(v -> {
                    UiUtils.showShortToast(context, video.getTitle());
                    return true;
                });*/
                //setRating(normalVideoViewHolder.imgRating,video.getA_record());
                normalVideoViewHolder.tvMovieTitle.setText(video.getTitle());
                normalVideoViewHolder.item.setLayoutParams(ResponsivenessUtils.getLayoutParmsForSearchScreenAdapter(context));
                checkWidth(normalVideoViewHolder.tvMovieTitle, normalVideoViewHolder.imgRating);
                break;
        }
    }

    private void takeToVideoPage(ImageView imageView, TextView textView, Video video) {
        if (video.isTrailerVideo()) {
            String url = "";
            if (video.getResolutions() != null)
                url = video.getResolutions().get(ORIGINAL);
            switch (video.getVideoType()) {
                case VIDEO_MANUAL:
                case VIDEO_OTHER:
                    if (url != null && Uri.parse(url) != null) {
                        UiUtils.log(TAG,"PlayerActivity");
                        Intent toVideo = new Intent(context, VideoPageActivity.class);
                        toVideo.putExtra(ADMIN_VIDEO_ID, video.getAdminVideoId());
                        toVideo.putExtra(PARENT_ID, video.getParentId());
                        toVideo.putExtra(IMAGE, video.getDefaultImage());
                        toVideo.putExtra(TITLE, video.getTitle());
                        context.startActivity(toVideo);
                        /*Intent toPlayer = new Intent(context, PlayerActivity.class);
                        toPlayer.putExtra(PlayerActivity.VIDEO_ID, video.getAdminVideoId());
                        toPlayer.putExtra(PlayerActivity.VIDEO_URL, url);
                        toPlayer.putExtra(PlayerActivity.VIDEO_SUBTITLE, video.getSubTitleUrl());
                        toPlayer.putExtra(PlayerActivity.IS_TRAILER_VIDEO, video.isTrailerVideo());
                        context.startActivity(toPlayer);*/
                    } else {
                        Toast.makeText(context, "Something wrong with the trailer video. Sorry for the inconvenience.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                /*case VIDEO_YOUTUBE:
                    Intent toYouTubePlayer = new Intent(context, YouTubePlayerActivity.class);
                    toYouTubePlayer.putExtra(YouTubePlayerActivity.VIDEO_ID, video.getAdminVideoId());
                    toYouTubePlayer.putExtra(YouTubePlayerActivity.VIDEO_URL, url);
                    context.startActivity(toYouTubePlayer);
                    break;*/
            }
        } else {
            Intent toVideo = new Intent(context, VideoPageActivity.class);
            toVideo.putExtra(ADMIN_VIDEO_ID, video.getAdminVideoId());
            toVideo.putExtra(PARENT_ID, video.getParentId());
            toVideo.putExtra(IMAGE, video.getDefaultImage());
            toVideo.putExtra(TITLE, video.getTitle());
            context.startActivity(toVideo);

            /*Pair<View, String> p1 = Pair.create(imageView, context.getResources().getString(R.string.transition_image));
            Pair<View, String> p2 = Pair.create(textView, context.getResources().getString(R.string.transition_title));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, p1, p2);
            context.startActivity(toVideo, options.toBundle());*/
        }
    }

    private void setRating(ImageView imgRating, int rating)
    {
        imgRating.setVisibility(View.VISIBLE);
        switch (rating)
        {
            case 1:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_a,null));
                break;
            case 2:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_ua,null));
                break;
            case 3:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_u,null));
                break;
            case 4:
                imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_kids,null));
                break;
            default:
                imgRating.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class NormalVideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoTileImg)
        ImageView item;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.videoTileRoot)
        ViewGroup videoTileRoot;
        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;

        NormalVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OriginalsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoTileImg)
        ImageView item;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.videoTileRoot)
        ViewGroup videoTileRoot;
        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;
        OriginalsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void checkWidth(TextView title, ImageView imgRating)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        UiUtils.log(TAG, "Width: "+ width);
        UiUtils.log(TAG, "Width dp: "+ dp);

        if(dp>500 && dp<600)
        {
            UiUtils.log(TAG, "margin(500-600)");
            title.setTextSize(14);
        }else if(dp>600 && dp<700)
        {
            UiUtils.log(TAG, "margin(600-700)");
            title.setTextSize(14);
        }else if(dp>700 && dp<800)
        {
            UiUtils.log(TAG, "margin(700-800)");
            title.setTextSize(14);
        }else if(dp>800)
        {
            UiUtils.log(TAG, "margin(800)");
            title.setTextSize(16);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgRating.getLayoutParams();
            params.width = 100;
            params.height = 100;
            params.setMargins(0, 30, 30, 0);
            imgRating.setLayoutParams(params);
        }
    }
}
