package com.bhoju.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bhoju.app.R;
import com.bhoju.app.model.GenreSeason;
import com.bhoju.app.ui.activity.VideoPageActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeasonTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private VideoPageActivity context;
    private ArrayList<GenreSeason> videos;

    public SeasonTitleAdapter(VideoPageActivity context, ArrayList<GenreSeason> videos) {
        this.context = context;
        this.videos = videos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = inflater.inflate(R.layout.item_season_title, viewGroup, false);
        return new SeasonTitleAdapter.NormalVideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        GenreSeason video = videos.get(position);
        SeasonTitleAdapter.NormalVideoViewHolder normalVideoViewHolder = (SeasonTitleAdapter.NormalVideoViewHolder) viewHolder;

        if(video.getName().length()<3)
        normalVideoViewHolder.title.setText(video.getName());
        else
        {
            normalVideoViewHolder.title.setText(video.getName().substring(0,1) + video.getName().substring(video.getName().length()-1));
        }
        /*if(video.isSelected()){
            normalVideoViewHolder.title.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_season));
        }else {
            normalVideoViewHolder.title.setBackgroundColor(Color.TRANSPARENT);
        }*/
        if(video.isSelected()){
            normalVideoViewHolder.title.setBackgroundTintList(AppCompatResources.getColorStateList(context,R.color.blue_color_new));
            normalVideoViewHolder.title.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            normalVideoViewHolder.title.setBackgroundTintList(AppCompatResources.getColorStateList(context,R.color.theme_yellow));
            normalVideoViewHolder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        }
        normalVideoViewHolder.title.setOnClickListener(view -> context.updateSeason(position));
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }

    class NormalVideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        NormalVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
