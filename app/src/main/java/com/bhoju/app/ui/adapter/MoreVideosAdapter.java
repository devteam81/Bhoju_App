package com.bhoju.app.ui.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhoju.app.util.UiUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
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
import static com.bhoju.app.network.APIConstants.Params.PARENT_ID;
import static com.bhoju.app.network.APIConstants.Params.SEASON_ID;
import static com.bhoju.app.network.APIConstants.Params.TITLE;
import static com.bhoju.app.util.UiUtils.animationObject;

public class MoreVideosAdapter extends RecyclerView.Adapter {

    private final String TAG = MoreVideosAdapter.class.getSimpleName();

    private static final int VIDEOS = 1;
    private static final int LOADING = 2;

    private Context context;
    private boolean isLoading = true;
    private ArrayList<Video> moreVideos;
    private LayoutInflater layoutInflater;
    private OnLoadMoreVideosListener listener;

    public MoreVideosAdapter(Context applicationContext, ArrayList<Video> moreVideos) {
        this.context = applicationContext;
        this.moreVideos = moreVideos;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnLoadMoreVideosListener(OnLoadMoreVideosListener onLoadMoreVideosListener) {
        this.listener = onLoadMoreVideosListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIDEOS) {
            View view = layoutInflater.inflate(R.layout.item_video_large, viewGroup, false);
            return new VideoViewHolder(view);
        }
        View view = layoutInflater.inflate(R.layout.loading, viewGroup, false);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof VideoViewHolder) {
            final VideoViewHolder holder = (VideoViewHolder) viewHolder;
            Video singleItem = moreVideos.get(i);
            Glide.with(context).load(singleItem.getThumbNailUrl()).thumbnail(0.5f)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().placeholder(R.drawable.bhoju_placeholder_horizontal).error(R.drawable.bhoju_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(holder.videoTileImg);
            holder.videoTileRoot.setOnClickListener(view -> {
                new ElasticAnimation(view).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                        .setOnFinishListener(new ElasticFinishListener() {
                            @Override
                            public void onFinished() {
                                Intent intent = new Intent(context, VideoPageActivity.class);
                                intent.putExtra(ADMIN_VIDEO_ID, singleItem.getAdminVideoId());
                                intent.putExtra(PARENT_ID, singleItem.getParentId());
                                intent.putExtra(SEASON_ID, singleItem.getSeasonId());
                                intent.putExtra(IMAGE, singleItem.getDefaultImage());
                                intent.putExtra(TITLE, singleItem.getTitle());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                                 /*Pair<View, String> p1 = Pair.create(holder.videoTileImg, context.getResources().getString(R.string.transition_image));
                                 Pair<View, String> p2 = Pair.create(holder.tvMovieTitle, context.getResources().getString(R.string.transition_title));

                                 ActivityOptionsCompat options = ActivityOptionsCompat.
                                         makeSceneTransitionAnimation((Activity) context, p1, p2);
                                 context.startActivity(intent, options.toBundle());*/
                            }
                        }).doAction();

            });

            //setRating(holder.imgRating,singleItem.getA_record());
            holder.tvMovieTitle.setText(singleItem.getTitle());
            holder.videoTileImg.setLayoutParams(ResponsivenessUtils.getLayoutParmsForSearchScreenAdapter(context));
            checkWidth(holder.tvMovieTitle, holder.imgRating);
        }
    }

    public void showLoading() {
        if (isLoading && moreVideos != null && listener != null) {
            isLoading = false;
            new android.os.Handler().post(() -> {
                moreVideos.add(null);
                notifyItemInserted(moreVideos.size() - 1);
                UiUtils.log(TAG, "NEW SiZE: " + moreVideos.size());
                listener.onLoadMore(moreVideos.size());
            });
        }
    }

    public void dismissLoading() {
        if (moreVideos != null && moreVideos.size() > 0) {
            moreVideos.remove(moreVideos.size() - 1);
            notifyItemRemoved(moreVideos.size());
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
    public int getItemViewType(int position) {
        if (moreVideos.get(position) != null) {
            return VIDEOS;
        } else {
            return LOADING;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return moreVideos == null ? 0 : moreVideos.size();
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar indicatorView;

        LoadingViewHolder(View itemView) {
            super(itemView);
            indicatorView = itemView.findViewById(R.id.progress);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoTileImg)
        ImageView videoTileImg;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;
        @BindView(R.id.videoTileRoot)
        ViewGroup videoTileRoot;
        @BindView(R.id.imgCard)
        ViewGroup imgCard;

        VideoViewHolder(@NonNull View itemView) {
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
            params.setMargins(0, 20, 20, 0);
            imgRating.setLayoutParams(params);
        }
    }
}
