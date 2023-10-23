package com.bhoju.app.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.util.sharedpref.Utils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.ui.activity.OfflineVideoActivity;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.download.DownloadUtils;
import com.bhoju.app.util.download.Downloader;
import com.bhoju.app.util.UiUtils;
import com.downloader.PRDownloader;
import com.downloader.Status;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bhoju.app.network.APIConstants.Constants.VIDEO_ELAPSED;
import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.network.APIConstants.Params.SUBTITLE_URL;
import static com.bhoju.app.network.APIConstants.Params.VIDEO_URL;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEKEYPAIR;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEPOLICY;
import static com.bhoju.app.ui.activity.MainActivity.COOKIESIGNATURE;
import static com.bhoju.app.util.UiUtils.animationObject;
import static com.bhoju.app.util.UiUtils.checkString;
import static com.bhoju.app.util.download.DownloadUtils.isValidVideoFile;
import static com.bhoju.app.util.download.DownloadUtils.isVideoFile;
import static com.bhoju.app.util.download.Downloader.downloadCanceled;

public class OfflineVideoAdapter extends RecyclerView.Adapter<OfflineVideoAdapter.OfflineViewHolder> {

    private final String TAG = OfflineVideoAdapter.class.getSimpleName();

    private ArrayList<Video> offlineVideos;
    private Context context;
    private OfflineVideoInterface offlineVideoInterface;

    public OfflineVideoAdapter(Context context, ArrayList<Video> data) {
        this.context = context;
        this.offlineVideos = data;
    }


    public void setDownloadVideoListener(OfflineVideoInterface offlineVideoInterface) {
        this.offlineVideoInterface = offlineVideoInterface;
    }

    @Override
    public OfflineVideoAdapter.OfflineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_offline_video, parent, false);
        return new OfflineVideoAdapter.OfflineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OfflineVideoAdapter.OfflineViewHolder holder, final int position) {
        final Video videoItem = offlineVideos.get(position);

        /*Bitmap thumbNail = ThumbnailUtils.
                createVideoThumbnail(videoItem.getVideoUrl(), MediaStore.Video.Thumbnails.MICRO_KIND);
        if (thumbNail == null)*/


        //holder.videoTitle.setText(TAG);
        //holder.videoThumb.setImageBitmap(thumbNail);
        UiUtils.log(TAG, "Image: "+ videoItem.getDefaultImage());
        Glide.with(context).load(videoItem.getDefaultImage()).thumbnail(0.5f)
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(new RequestOptions().placeholder(R.drawable.bhoju_placeholder_horizontal).error(R.drawable.bhoju_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                .into(holder.videoThumb);

        holder.videoTitle.setText(videoItem.getTitle());
        holder.episodeTitle.setText(videoItem.getEpTitle());
        if(videoItem.getEpTitle().equalsIgnoreCase("") || videoItem.getEpTitle().equalsIgnoreCase("null"))
            holder.episodeTitle.setVisibility(View.GONE);
        holder.videoGenre.setText(String.format(Locale.ENGLISH,"%d Days until expiry", videoItem.getNumDaysToExpire()));
        setRating(null,holder.txt_rating, videoItem.getA_record());
        checkWidth(holder);
        //holder.videoThumb.setLayoutParams(ResponsivenessUtils.getLayoutParmsForDownloadPageAdapter(context));

        if (videoItem.isInSpam()) {
            holder.root.setBackgroundColor(context.getResources().getColor(R.color.light_red));
            holder.root.setOnClickListener(v -> showSpamAndPlay(videoItem));
        } else {
            if(position%2==0) {
                holder.root.setBackgroundColor(context.getResources().getColor(R.color.download_bg1));
                holder.line_view.setBackgroundColor(context.getResources().getColor(R.color.download_bg2));
            }
            else {
                holder.root.setBackgroundColor(context.getResources().getColor(R.color.download_bg2));
                holder.line_view.setBackgroundColor(context.getResources().getColor(R.color.download_bg1));
            }
            holder.root.setOnClickListener(v -> {
                if(videoItem.getNumDaysToExpire()>0) {
                    UiUtils.log(TAG,"ARECORD: "+ videoItem.getA_record());
                    if(videoItem.getA_record()==1)
                        showARatedPopup(videoItem);
                    else
                        playVideo(videoItem);
                }
            });
        }

        //start/resume
        holder.downloadVideo.setOnClickListener(v->{
            UiUtils.log(TAG,"Download Status "+ PRDownloader.getStatus(videoItem.getDownloadId()));
            Status status = PRDownloader.getStatus(videoItem.getDownloadId());
            UiUtils.log(TAG,"Download Status "+ status);
            if (status == Status.PAUSED) {
                PRDownloader.resume(videoItem.getDownloadId());
            }else if (status == Status.UNKNOWN) {

                String cookieValue = "CloudFront-Policy=" + COOKIEPOLICY +
                        ";CloudFront-Signature=" + COOKIESIGNATURE +
                        ";CloudFront-Key-Pair-Id=" + COOKIEKEYPAIR;

                Downloader downloader = new Downloader(context);
                downloader.setOnDownloadListener(context);
                downloader.downloadVideo(PRDownloader.download(videoItem.getDownloadUrl(), videoItem.getDownloadSavePath(), videoItem.getDownloadFileName()).setHeader("Cookie", cookieValue).build(), videoItem.getDownloadId());
                showDownloadingVideo(holder);
            }
        });

        //pause
        holder.pauseDownloadingView.setOnClickListener(v->{
            Status status = PRDownloader.getStatus(videoItem.getDownloadId());
            UiUtils.log(TAG,"Pause Status "+ videoItem.getDownloadId());
            UiUtils.log(TAG,"Pause Status "+ status);
            if (status == Status.RUNNING ) {
                UiUtils.log(TAG,"Video Paused");
                PRDownloader.pause(videoItem.getDownloadId());
            }
            showDownloadVideo(holder);
        });

        //cancel
        holder.cancelDownloadingView.setOnClickListener(v->{
            UiUtils.log(TAG,"Video Paused");
            downloadCanceled(context, videoItem.getAdminVideoId(), videoItem.getDownloadId());
            PRDownloader.cancel(videoItem.getDownloadId());
        });

        //delete offline video
        holder.deleteVideo.setOnClickListener(v -> {

            Dialog dialog = new Dialog(context);

            dialog.setContentView(R.layout.dialog_delete_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView txtYes = dialog.findViewById(R.id.btnYes);
            TextView txtNo = dialog.findViewById(R.id.btnNo);

            txtNo.setOnClickListener((View view) -> {
                dialog.dismiss();
            });

            txtYes.setOnClickListener((View view) -> {
                dialog.dismiss();
                //handle delete action click
                try {
                    int adminVideoId = videoItem.getAdminVideoId();
                    DownloadUtils.deleteVideoFile(context, videoItem.getVideoUrl());
                    Downloader.downloadDeleted(context, adminVideoId,0);
                    offlineVideos.remove(position);
                    if (offlineVideoInterface != null)
                        offlineVideoInterface.onVideoDeleted(offlineVideos.size() == 0);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    UiUtils.showShortToast(context, videoItem.getTitle() + " Removed from offline");
                } catch (Exception e) {
                    UiUtils.log(TAG,Log.getStackTraceString(e));
                    UiUtils.showShortToast(context, "Something went wrong. Please try again!");
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        });


        if(!checkString(videoItem.getDownloadedPercentage()))
        {
            holder.downloadingView_percent.setText(String.format("%s%%", videoItem.getDownloadedPercentage()));
            holder.downloadingView_percent.setVisibility(View.VISIBLE);
        }else
        {
            holder.downloadingView_percent.setVisibility(View.GONE);
        }
        //if (videoItem.isDownloadable()) {
        try {

            switch (videoItem.getDownloadedStatus()) {
                case DO_NOT_SHOW_DOWNLOAD:
                    holder.deleteVideo.setVisibility(View.GONE);
                    break;
                case DOWNLOAD_PROGRESS:
                    showDownloadingVideo(holder);
                    //holder.deleteVideo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tick_mark));
                    break;
                case DOWNLOAD_COMPLETED:
                    showDownloadedVideo(holder);
                    break;
                case DOWNLOAD_PAUSED:
                case SHOW_DOWNLOAD:
                    showDownloadVideo(holder);
                    break;
                /*case NEED_TO_SUBSCRIBE:
                    showDownloadButton(needToSubscribeToDownloadListener);
                    break;
                case NEED_TO_PAY:
                    showDownloadButton(needToPayToDownloadListener);
                    break;*/
            }
        }catch (Exception e)
        {
            holder.deleteVideo.setVisibility(View.VISIBLE);
        }
       /* } else {
            holder.deleteVideo.setVisibility(View.GONE);
        }*/


    }

    private void showDownloadVideo(OfflineViewHolder holder)
    {
        holder.downloadVideo.setVisibility(View.VISIBLE);
        holder.ll_downloadingView.setVisibility(View.GONE);
        holder.deleteVideo.setVisibility(View.GONE);
    }

    private void showDownloadingVideo(OfflineViewHolder holder)
    {
        holder.downloadVideo.setVisibility(View.GONE);
        holder.ll_downloadingView.setVisibility(View.VISIBLE);
        holder.deleteVideo.setVisibility(View.GONE);
    }

    private void showDownloadedVideo(OfflineViewHolder holder)
    {
        holder.downloadVideo.setVisibility(View.GONE);
        holder.ll_downloadingView.setVisibility(View.GONE);
        holder.deleteVideo.setVisibility(View.VISIBLE);

    }

    private void showSpamAndPlay(Video video) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.spam_video))
                .setMessage(R.string.you_have_marked_this_video_as_spam)
                .setPositiveButton(context.getText(R.string.yes), (dialog, which) -> playVideo(video))
                .setNegativeButton(context.getText(R.string.no), null)
                .setIcon(R.mipmap.ic_launcher)
                .create().show();
    }

    public void showARatedPopup(Video videoItem)
    {

        //Now we need an AlertDialog.Builder object
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_rating);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        dialog.findViewById(R.id.btnYes).setOnClickListener((View v) -> {
            dialog.dismiss();
            playVideo(videoItem);

        });
        dialog.findViewById(R.id.btnNo).setOnClickListener((View v) -> dialog.dismiss());


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void setRating(ImageView imgRating,TextView txtRating, int rating)
    {
        if(imgRating!= null) imgRating.setVisibility(View.VISIBLE);
        switch (rating)
        {
            case 1:
                if(imgRating!= null) imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_a,null));
                if(txtRating!= null) txtRating.setText(context.getResources().getString(R.string.rating_a_18).substring(8));
                break;
            case 2:
                if(imgRating!= null) imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_ua,null));
                if(txtRating!= null) txtRating.setText(context.getResources().getString(R.string.rating_ua_16).substring(8));
                break;
            case 3:
                if(imgRating!= null) imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_u,null));
                if(txtRating!= null) txtRating.setText(context.getResources().getString(R.string.rating_u_12).substring(8));
                break;
            case 4:
                if(imgRating!= null) imgRating.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rating_kids,null));
                if(txtRating!= null) txtRating.setText(context.getResources().getString(R.string.rating_kids).substring(8));
                break;
            default:
                if(imgRating!= null) imgRating.setVisibility(View.GONE);
                if(txtRating!= null) txtRating.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return offlineVideos.size();
    }

    private void playVideo(Video video) {
        Uri videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".myprovider", new File(video.getVideoUrl()));
        if (isValidVideoFile(context, videoUri) && isVideoFile(video.getVideoUrl())) {
            UiUtils.log(TAG,"GiraffePlayerActivity");
            Intent toVideo = new Intent(context, OfflineVideoActivity.class);
            //Intent toVideo = new Intent(context, GiraffePlayerActivity.class);
            toVideo.putExtra(ADMIN_VIDEO_ID, video.getAdminVideoId());
            toVideo.putExtra(VIDEO_URL, String.valueOf(videoUri));
            toVideo.putExtra(VIDEO_ELAPSED, video.getSeekHere());
            toVideo.putExtra(SUBTITLE_URL, video.getSubTitleUrl());
            context.startActivity(toVideo);
        } else {
            UiUtils.showShortToast(context, context.getString(R.string.problem_with_downloaded_video));
        }
    }

    private void downloadVideo(String downloadUrl,Video video) {
        Toast.makeText(context, downloadUrl, Toast.LENGTH_SHORT).show();
        String episodeName = video.getEpTitle()!=null? video.getEpTitle(): "";
        NetworkUtils.downloadVideo(context, video.getAdminVideoId(), video.getTitle(),episodeName, downloadUrl, video.getNumDaysToExpire());
        //downloadView.setVisibility(View.GONE);
        //downloadingView.setVisibility(View.VISIBLE);
    }


    public interface OfflineVideoInterface {
        void onVideoDeleted(boolean isEmpty);
    }

    class OfflineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView videoThumb;
        @BindView(R.id.title)
        TextView videoTitle;
        @BindView(R.id.ep_title)
        TextView episodeTitle;
        @BindView(R.id.genre)
        TextView videoGenre;
        @BindView(R.id.txt_rating)
        TextView txt_rating;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.downloadVideo)
        ImageView downloadVideo;
        @BindView(R.id.deleteVideo)
        ImageView deleteVideo;
        @BindView(R.id.ll_downloadingView)
        LinearLayout ll_downloadingView;
        @BindView(R.id.downloadingView_percent)
        TextView downloadingView_percent;
        @BindView(R.id.pauseDownloadingView)
        ImageView pauseDownloadingView;
        @BindView(R.id.cancelDownloadingView)
        ImageView cancelDownloadingView;
        @BindView(R.id.offlineVideoItem)
        ViewGroup root;
        @BindView(R.id.line_view)
        TextView line_view;

        OfflineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void checkWidth(OfflineViewHolder holder)
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
            holder.videoTitle.setTextSize(14);
        }else if(dp>600 && dp<700)
        {
            UiUtils.log(TAG, "margin(600-700)");
            holder.videoTitle.setTextSize(14);
        }else if(dp>700 && dp<800)
        {
            UiUtils.log(TAG, "margin(700-800)");
            holder.videoTitle.setTextSize(14);
        }else if(dp>800)
        {
            UiUtils.log(TAG, "margin(800)");
            holder.videoTitle.setTextSize(16);
            holder.videoGenre.setTextSize(14);

            RelativeLayout.LayoutParams thumbParams = (RelativeLayout.LayoutParams) holder.videoThumb.getLayoutParams();
            thumbParams.width = Utils.dpToPx(220);
            thumbParams.height = Utils.dpToPx(150);
            thumbParams.setMargins(20, 20, 20, 20);
            holder.videoThumb.setLayoutParams(thumbParams);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.imgRating.getLayoutParams();
            params.width = 60;
            params.height = 60;
            params.setMargins(0, 30, 40, 0);
            holder.imgRating.setLayoutParams(params);
        }
    }
}