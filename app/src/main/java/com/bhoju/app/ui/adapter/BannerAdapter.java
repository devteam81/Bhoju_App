package com.bhoju.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.util.ResponsivenessUtils;
import com.bhoju.app.util.UiUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.ui.activity.VideoPageActivity;


import java.util.ArrayList;

import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.network.APIConstants.Params.PARENT_ID;
import static com.bhoju.app.util.UiUtils.animationObject;

public class BannerAdapter extends PagerAdapter {

    private final String TAG = BannerAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Video> data;

    public BannerAdapter(Context activity, ArrayList<Video> data) {
        this.context = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        ImageView bannerImage,imgRating;
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner_video, container, false);
        try {
            bannerImage = view.findViewById(R.id.image);
            Glide.with(context).load(data.get(position).getThumbNailUrl()).thumbnail(0.5f)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().placeholder(R.drawable.bhoju_placeholder_horizontal).error(R.drawable.bhoju_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(bannerImage);

            imgRating = view.findViewById(R.id.imgRating);
            checkWidth(null, imgRating);
            //setRating(imgRating,data.get(position).getA_record());
            bannerImage.setLayoutParams(ResponsivenessUtils.getLayoutParamsForFullWidthSeasonView(context));

            bannerImage.setOnClickListener(view1 -> {
                switch (data.get(position).getSlider_type().toLowerCase()) {
                    case "media":
                        Intent i = new Intent(context, VideoPageActivity.class);
                        i.putExtra(ADMIN_VIDEO_ID, data.get(position).getAdminVideoId());
                        i.putExtra(PARENT_ID, data.get(position).getParentId());
                        context.startActivity(i);
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
                            Intent browserIntentDefault = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getParent_media()));
                            context.startActivity(browserIntentDefault);
                        } catch (Exception e) {
                            Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                        /*context.startActivity(new Intent(context, WebViewActivity.class)
                                .putExtra(WebViewActivity.PAGE_TYPE, WebViewActivity.PageTypes.BANNER)
                                .putExtra("URL", "https://www.youtube.com/watch?v=VLTwnY7wY0Y&ab_channel=KOOKU"));*/
                        break;
                }
        });

    } catch (Exception e) {
        UiUtils.log(TAG, Log.getStackTraceString(e));
    }

        container.addView(view);
        return view;
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
            if(title!=null)
                title.setTextSize(14);
        }else if(dp>600 && dp<700)
        {
            UiUtils.log(TAG, "margin(600-700)");
            if(title!=null)
                title.setTextSize(14);
        }else if(dp>700 && dp<800)
        {
            UiUtils.log(TAG, "margin(700-800)");
            if(title!=null)
                title.setTextSize(14);
        }else if(dp>800)
        {
            UiUtils.log(TAG, "margin(800)");
            if(title!=null)
                title.setTextSize(16);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgRating.getLayoutParams();
            params.width = 100;
            params.height = 100;
            params.setMargins(0, 50, 40, 0);
            imgRating.setLayoutParams(params);
        }
    }
}
