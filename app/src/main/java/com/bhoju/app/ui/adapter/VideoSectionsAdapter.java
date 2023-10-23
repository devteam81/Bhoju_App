package com.bhoju.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.model.VideoSection;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.ui.activity.MoreVideosActivity;
import com.bhoju.app.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoSectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = VideoSectionsAdapter.class.getSimpleName();

    private static final int VIDEOS = 1;
    private static final int LOADING = 2;
    private String pageType;
    private String categoryId;
    private Context context;
    private ArrayList<VideoSection> videoSections;
    private LayoutInflater inflater;
    private boolean isLoading = true;
    private VideoSectionsInterface videoSectionsInterface;
    private int staticContentLength = 0;

    public VideoSectionsAdapter(Context activity, VideoSectionsInterface videoSectionsInterface, ArrayList<VideoSection> videoSections, String pageType, String categoryId) {
        this.context = activity;
        this.videoSectionsInterface = videoSectionsInterface;
        this.videoSections = videoSections;
        this.pageType = pageType;
        this.categoryId = categoryId;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == LOADING) {
            view = inflater.inflate(R.layout.loading, viewGroup, false);
            return new LoadingViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_video_section, viewGroup, false);
            return new SectionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VideoSection videoSection = videoSections.get(position);
        if (viewHolder instanceof SectionViewHolder) {
            SectionViewHolder originalsViewHolder = (SectionViewHolder) viewHolder;
            originalsViewHolder.title.setText(videoSection.getTitle().toUpperCase());
            //UiUtils.log(TAG, "Title: "+ videoSection.getTitle().toUpperCase());
            originalsViewHolder.recyclerView.setHasFixedSize(true);
            originalsViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            VideoTileAdapter videoTileAdapter = new VideoTileAdapter(context, videoSection.getVideos(), videoSection.getViewType(), false);
            //if( videoSection.getViewType() != 1) {
                if (position%2!=0)
                    videoTileAdapter = new VideoTileAdapter(context, videoSection.getVideos(), 0, false);
            //}
            originalsViewHolder.recyclerView.setAdapter(videoTileAdapter);
            videoTileAdapter.notifyDataSetChanged();

            if (videoSection.getUrlType() == null
                    || videoSection.getUrlType().equalsIgnoreCase("")) {
                originalsViewHolder.videoSeeAll.setVisibility(View.GONE);
            } else {
                originalsViewHolder.videoSeeAll.setVisibility(View.VISIBLE);
            }

            originalsViewHolder.videoSeeAll.setOnClickListener(view -> {
                Intent i = new Intent(context, MoreVideosActivity.class);
                i.putExtra(APIConstants.Params.URL_TYPE, videoSection.getUrlType());
                i.putExtra(APIConstants.Params.PAGE_TYPE, pageType);
                i.putExtra(APIConstants.Params.URL_TYPE_ID, videoSection.getUrlPageId());
                i.putExtra(APIConstants.Params.CATEGORY_ID, categoryId);
                i.putExtra(APIConstants.Params.SUB_CATEGORY_ID, 0);
                i.putExtra(APIConstants.Params.CAST_CREW_ID, 0);
                i.putExtra(APIConstants.Params.GENRE_ID, 0);
                i.putExtra(APIConstants.Params.SKIP, 0);
                i.putExtra(APIConstants.Params.TITLE, videoSection.getTitle());
                context.startActivity(i);
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoSections.size();
    }

    public void showLoading() {
        if (isLoading && videoSections != null && videoSectionsInterface != null) {
            isLoading = false;
            new android.os.Handler().post(() -> {
                UiUtils.log(TAG,"Size: " + videoSections.size() + " Length: "+ staticContentLength);
                videoSectionsInterface.onLoadMore(videoSections.size() - staticContentLength);
                notifyDataSetChanged();
            });
        }
    }

    public void dismissLoading() {
        if (videoSections != null && videoSections.size() > 0) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (videoSections.get(position) != null) {
            return VIDEOS;
        } else {
            return LOADING;
        }
    }
    /**
     * Store the count of static sections before calling dynamic content(for skip take)
     */
    public void setDynamicSectionPadding() {
        staticContentLength = videoSections.size();
    }

    public interface VideoSectionsInterface {
        void onLoadMore(int skip);
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress)
        ProgressBar indicatorView;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoRowTitle)
        TextView title;
        @BindView(R.id.videoRowRecyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.videoRowRoot)
        ViewGroup videoRowRoot;
        @BindView(R.id.videoSeeAll)
        TextView videoSeeAll;

        SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}