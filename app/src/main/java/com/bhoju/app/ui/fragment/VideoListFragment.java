package com.bhoju.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.ui.adapter.VideoTileAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoListFragment extends Fragment {

    public static final int TYPE_GRID = 1;
    public static final int TYPE_LIST = 2;

    Context activity;
    Unbinder unbinder;
    @BindView(R.id.videoRecycler)
    RecyclerView videoRecycler;

    ArrayList<Video> videos;
    VideoTileAdapter videoTileAdapter;
    private int type;

    public VideoListFragment() {
        videos = new ArrayList<>();
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpVideos();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpVideos() {
        videoTileAdapter = new VideoTileAdapter(activity, videos, VideoTileAdapter.VIDEO_SECTION_TYPE_NORMAL, true);
        if (type == TYPE_GRID)
            videoRecycler.setLayoutManager(new GridLayoutManager(activity, 3));
        else
            videoRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        videoRecycler.setHasFixedSize(true);
        videoRecycler.setAdapter(videoTileAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setType(int type) {
        this.type = type;
    }
}
