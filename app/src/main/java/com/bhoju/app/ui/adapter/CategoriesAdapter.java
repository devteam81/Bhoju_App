package com.bhoju.app.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.Category;
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.ui.fragment.VideoContentFragment;
import com.bhoju.app.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bhoju.app.network.APIConstants.Params.CATEGORY;
import static com.bhoju.app.util.Fragments.HOME_FRAGMENTS;
import static com.bhoju.app.util.UiUtils.animationObject;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.VideoTileHolder> {

    private VideoContentFragment contentFragment;
    private LayoutInflater inflater;
    private MainActivity context;
    private ArrayList<Category> categories;
    private OnLoadMoreVideosListener listener;

    public CategoriesAdapter(MainActivity context, OnLoadMoreVideosListener listener, ArrayList<Category> categories) {
        this.context = context;
        this.listener = listener;
        this.categories = categories;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public VideoTileHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_category, viewGroup, false);
        return new VideoTileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoTileHolder videoTileHolder, int position) {
        Category category = categories.get(position);
        Glide.with(context).load(category.getThumbnailUrl())
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                .into(videoTileHolder.categoryImg);
        videoTileHolder.categoryName.setText(category.getTitle());
        videoTileHolder.categoryRoot.setOnClickListener(v -> {
            //CategoryFragment.categoryBeingViewed = category.getTitle();
            contentFragment = VideoContentFragment.getInstance(CATEGORY, category.getId(), 0, 0);
            replaceFragmentWithAnimation(contentFragment, HOME_FRAGMENTS[2]);
        });
        videoTileHolder.categoryRoot.setOnLongClickListener(v -> {
            UiUtils.showShortToast(context, category.getTitle());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        MainActivity.CURRENT_FRAGMENT = tag;
        transaction.addToBackStack(tag);
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void showLoading() {
        if(listener!=null)
            listener.onLoadMore(categories.size());
    }

    class VideoTileHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.categoryImg)
        ImageView categoryImg;
        @BindView(R.id.categoryName)
        TextView categoryName;
        @BindView(R.id.categoryRoot)
        ViewGroup categoryRoot;

        VideoTileHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
