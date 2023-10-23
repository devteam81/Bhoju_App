package com.bhoju.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.util.ResponsivenessUtils;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.activity.VideoPageActivity;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.skydoves.elasticviews.ElasticAnimation;
import com.skydoves.elasticviews.ElasticFinishListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.APIs.CLEAR_HISTORY;
import static com.bhoju.app.network.APIConstants.APIs.CLEAR_SPAM_LIST;
import static com.bhoju.app.network.APIConstants.APIs.CLEAR_WISH_LIST;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.ADMIN_VIDEO_ID;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static com.bhoju.app.network.APIConstants.Params.IMAGE;
import static com.bhoju.app.network.APIConstants.Params.PARENT_ID;
import static com.bhoju.app.network.APIConstants.Params.SEASON_ID;
import static com.bhoju.app.network.APIConstants.Params.SUCCESS;
import static com.bhoju.app.network.APIConstants.Params.TITLE;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEKEYPAIR;
import static com.bhoju.app.ui.activity.MainActivity.COOKIEPOLICY;
import static com.bhoju.app.ui.activity.MainActivity.COOKIESIGNATURE;
import static com.bhoju.app.ui.activity.VideoPageActivity.trailerPlaying;
import static com.bhoju.app.util.UiUtils.animationObject;

public class VideoListAdapter extends RecyclerView.Adapter {

    private final String TAG = VideoListAdapter.class.getSimpleName();

    private static final int VIDEOS = 1;
    private static final int LOADING = 2;
    private APIInterface apiInterface;
    private LayoutInflater inflater;
    private Context context;
    private boolean isLoading = true;
    private OnLoadMoreVideosListener listener;
    private ArrayList<Video> videos;
    private VideoListType videoListType;
    private OnDataChangedListener dataChanged;

    private int currentVideoId=-1;

    public VideoListAdapter(Context activity,int currentVideoId, ArrayList<Video> singleItem, VideoListType videoListType, OnDataChangedListener dataChanged) {
        this.context = activity;
        this.videos = singleItem;
        this.currentVideoId = currentVideoId;
        this.videoListType = videoListType;
        this.dataChanged = dataChanged;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    /*public VideoListAdapter(Context activity, ArrayList<Video> singleItem, VideoListType videoListType, OnDataChangedListener dataChanged, int currentId) {
        this.context = activity;
        this.videos = singleItem;
        this.videoListType = videoListType;
        this.dataChanged = dataChanged;
        this.currentVideoId = currentId;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.apiInterface = APIClient.getClient().create(APIInterface.class);
    }*/

    public void setOnLoadMoreVideosListener(OnLoadMoreVideosListener onLoadMoreVideosListener) {
        this.listener = onLoadMoreVideosListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == LOADING) {
            view = inflater.inflate(R.layout.loading, viewGroup, false);
            return new LoadingViewHolder(view);
        } else {
            switch (videoListType) {
                case TYPE_SEASONS:
                    //view = inflater.inflate(R.layout.item_season_video_in_list, viewGroup, false);
                    //return new VideoViewHolder(view);
                case TYPE_HISTORY:
                case TYPE_SPAM:
                case TYPE_WISH_LIST:
                case TYPE_OTHERS:
                default:
                    view = inflater.inflate(R.layout.item_season_video_in_list, viewGroup, false);
                    //view = inflater.inflate(R.layout.item_video_in_list, viewGroup, false);
                    return new VideoViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof VideoViewHolder) {
            final VideoViewHolder holder = (VideoViewHolder) viewHolder;
            Video singleItem = videos.get(i);
            String cookieValue = "CloudFront-Policy="+COOKIEPOLICY+
                    ";CloudFront-Signature="+COOKIESIGNATURE+
                    ";CloudFront-Key-Pair-Id="+COOKIEKEYPAIR;
            UiUtils.log(TAG,"Header Added");
            GlideUrl glideUrl = new GlideUrl(singleItem.getThumbNailUrl(), new LazyHeaders.Builder()
                    .addHeader("Cookie",cookieValue)
                    .addHeader("User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                    .build());
            Glide.with(context).load(glideUrl).thumbnail(0.5f)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().placeholder(R.drawable.bhoju_placeholder_horizontal).error(R.drawable.bhoju_placeholder_horizontal).diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(holder.videoTileImg);
            //holder.desc.setText(singleItem.getDescription());
            holder.title.setText(singleItem.getEpTitle());
            //setRating(holder.imgRating,singleItem.getA_record());
            checkWidth(holder.title, holder.imgRating);
            holder.videoTileImg.setLayoutParams(ResponsivenessUtils.getLayoutParmsForPosterActivitySeasonView(context));
            holder.layout.setOnClickListener(view -> {
                new ElasticAnimation(view).setScaleX(0.85f).setScaleY(0.85f).setDuration(300)
                        .setOnFinishListener(new ElasticFinishListener() {
                            @Override
                            public void onFinished() {
                                // Do something after duration time
                                if (videoListType != VideoListType.TYPE_SPAM) {
                                    UiUtils.log(TAG,"Current Id:" + currentVideoId + " --> Video Id : " + singleItem.getAdminVideoId());
                                    if(currentVideoId!= singleItem.getAdminVideoId()) {
                                        Intent intent = new Intent(context, VideoPageActivity.class);
                                        intent.putExtra(ADMIN_VIDEO_ID, singleItem.getAdminVideoId());
                                        intent.putExtra(PARENT_ID, singleItem.getParentId());
                                        intent.putExtra(SEASON_ID, singleItem.getSeasonId());
                                        intent.putExtra(IMAGE, singleItem.getDefaultImage());
                                        intent.putExtra(TITLE, singleItem.getTitle());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();

                                        /*Pair<View, String> p1 = Pair.create(holder.wishListImage, context.getResources().getString(R.string.transition_image));
                                        Pair<View, String> p2 = Pair.create(holder.title, context.getResources().getString(R.string.transition_title));

                                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                                makeSceneTransitionAnimation((Activity) context, p1, p2);
                                        context.startActivity(intent, options.toBundle());*/
                                    }else {
                                        UiUtils.log(TAG, "trailerPlaying: " + trailerPlaying);
                                        if (trailerPlaying) {
                                            Intent intent = new Intent(context, VideoPageActivity.class);
                                            intent.putExtra(ADMIN_VIDEO_ID, singleItem.getAdminVideoId());
                                            intent.putExtra(PARENT_ID, singleItem.getParentId());
                                            intent.putExtra(SEASON_ID, singleItem.getSeasonId());
                                            intent.putExtra(IMAGE, singleItem.getDefaultImage());
                                            intent.putExtra(TITLE, singleItem.getTitle());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            trailerPlaying = false;
                                            context.startActivity(intent);
                                            ((Activity) context).finish();
                                        } else
                                            Toast.makeText(context, "Video is already playing", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).doAction();
            });

            /*switch (videoListType) {
                case TYPE_SEASONS:
                    holder.duration.setText(singleItem.getDuration());
                    break;
            }*/

            holder.layout.setOnLongClickListener(view -> {
                switch (videoListType) {
                    case TYPE_HISTORY:
                    case TYPE_SPAM:
                    case TYPE_WISH_LIST:
                    case TYPE_OTHERS:
                    default:
                        //showPopupMenu(view, i);
                        break;

                    case TYPE_SEASONS:
                        break;
                }
                return true;
            });
        }
    }

    private void showPopupMenu(View view, final int position) {
        Context activity = new ContextThemeWrapper(context, R.style.AppTheme_NoActionBar);
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_clear, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.remove:
                    switch (videoListType) {
                        case TYPE_SPAM:
                            new AlertDialog.Builder(context)
                                    .setMessage(R.string.are_you_sure_to_clear_spam)
                                    .setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> {
                                        dialogInterface.cancel();
                                        clearSpamVideosInBackend(position, 0);
                                        removeAtPosition(position);
                                        if (videos.size() == 0) {
                                            /*Intent recreate = new Intent(context, SpamVideosActivity.class);
                                            recreate.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(recreate);*/
                                        }
                                    })
                                    .setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel())
                                    .create().show();
                            break;
                        case TYPE_HISTORY:
                            new AlertDialog.Builder(context)
                                    .setMessage(R.string.are_you_sure_to_clear_your_history)
                                    .setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> {
                                        dialogInterface.cancel();
                                        clearHistoryinBackend(position, 0);
                                        removeAtPosition(position);
                                        if (videos.size() == 0) {
                                            /*Intent recreate = new Intent(context, HistoryActivity.class);
                                            recreate.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(recreate);*/
                                        }
                                    })
                                    .setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel())
                                    .create().show();
                            break;
                        case TYPE_WISH_LIST:
                            new AlertDialog.Builder(context)
                                    .setMessage(R.string.are_you_sure_to_clear_wishlist)
                                    .setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> {
                                        dialogInterface.cancel();
                                        clearWishlistInBackend(position, 0);
                                        removeAtPosition(position);
                                        if (videos.size() == 0) {
                                           /* Intent recreate = new Intent(context, WishListActivity.class);
                                            recreate.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(recreate);*/
                                        }
                                    })
                                    .setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel())
                                    .create().show();
                            break;
                        case TYPE_SEASONS:
                            break;
                        case TYPE_OTHERS:
                            popupMenu.getMenu().findItem(R.id.remove).setVisible(false);
                            break;
                    }
            }
            return true;
        });
        popupMenu.show();
    }

    public void showLoading() {
        if (isLoading && videos != null && listener != null) {
            isLoading = false;
            new android.os.Handler().post(() -> {
                videos.add(null);
                notifyItemInserted(videos.size() - 1);
                listener.onLoadMore(videos.size());
            });
        }
    }

    public void dismissLoading() {
        if (videos != null && videos.size() > 0) {
            videos.remove(videos.size() - 1);
            notifyItemRemoved(videos.size());
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
        if (videos.get(position) != null) {
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
        return videos == null ? 0 : videos.size();
    }

    public void clearWishlistInBackend(int position, int status) {
        int id = videos.get(position).getAdminVideoId();
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.ADMIN_VIDEO_ID, id);
        params.put(Params.CLEAR_ALL_STATUS, status);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.clearWishList(CLEAR_WISH_LIST, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject clearWishListResponse = null;
                try {
                    clearWishListResponse = new JSONObject(response.body());

                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (clearWishListResponse != null) {
                    if (clearWishListResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        if (status == 1) {
                            videos.clear();
                            notifyDataSetChanged();
                            Toast.makeText(context, "Videos cleared", Toast.LENGTH_SHORT).show();
                        }
                        dataChanged.onDataChanged();
                    } else {
                        UiUtils.showShortToast(context, clearWishListResponse.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(context);
            }
        });
    }

    public void clearHistoryinBackend(int position, int status) {
        UiUtils.showLoadingDialog(context);
        int id = videos.get(position).getAdminVideoId();
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.ADMIN_VIDEO_ID, id);
        params.put(Params.STATUS, status);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.clearHistory(CLEAR_HISTORY, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject clearWishListResponse = null;
                try {
                    clearWishListResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (clearWishListResponse != null) {
                    if (clearWishListResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        if (status == 1) {
                            videos.clear();
                            notifyDataSetChanged();
                        }
                        dataChanged.onDataChanged();
                    } else {
                        UiUtils.showShortToast(context, clearWishListResponse.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(context);
            }
        });
    }

    private void clearSpamVideosInBackend(int position, int status) {
        UiUtils.showLoadingDialog(context);
        int id = videos.get(position).getAdminVideoId();
        PrefUtils prefUtils = PrefUtils.getInstance(context);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.ADMIN_VIDEO_ID, id);
        params.put(Params.STATUS, status);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.clearSpamList(CLEAR_SPAM_LIST, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject clearWishListResponse = null;
                try {
                    clearWishListResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (clearWishListResponse != null) {
                    if (clearWishListResponse.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                        if (status == 1) {
                            videos.clear();
                            notifyDataSetChanged();
                            Toast.makeText(context, "Videos cleared", Toast.LENGTH_SHORT).show();
                        }
                        dataChanged.onDataChanged();
                    } else {
                        UiUtils.showShortToast(context, clearWishListResponse.optString(ERROR_MESSAGE));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                NetworkUtils.onApiError(context);
            }
        });
    }

    private void removeAtPosition(int position) {
        try {
            videos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, videos.size());
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }

    public enum VideoListType {
        TYPE_SPAM,
        TYPE_WISH_LIST,
        TYPE_HISTORY,
        TYPE_OTHERS,
        TYPE_SEASONS
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress)
        ProgressBar indicatorView;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.videoTileImg)
        ImageView videoTileImg;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.imgRating)
        ImageView imgRating;
        @BindView(R.id.layout)
        RelativeLayout layout;
        @BindView(R.id.imgCard)
        RelativeLayout imgCard;

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
            params.setMargins(0, 30, 30, 0);
            imgRating.setLayoutParams(params);
        }
    }
}
