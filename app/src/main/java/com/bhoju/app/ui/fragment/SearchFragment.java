package com.bhoju.app.ui.fragment;

import static com.bhoju.app.network.APIConstants.APIs.SEARCH_VIDEOS;
import static com.bhoju.app.network.APIConstants.Params.VERSION_CODE;
import static com.bhoju.app.ui.activity.MainActivity.CURRENT_IP;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bhoju.app.BuildConfig;
import com.bhoju.app.R;
import com.bhoju.app.model.Video;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIConstants.Params;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.adapter.SearchAdapter;
import com.bhoju.app.util.KeyboardUtils;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.ui.activity.MainActivity;
import com.bhoju.app.ui.adapter.VideoTileAdapter;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private final String TAG = SearchFragment.class.getSimpleName();
    MainActivity activity;
    Unbinder unbinder;

    SearchAdapter searchAdapter;
    ArrayList<Video> searchResults = new ArrayList<>();
    @BindView(R.id.searchView)
    EditText searchView;

    APIInterface apiInterface;
    @BindView(R.id.hintLayout)
    LinearLayout hintLayout;
    /*@BindView(R.id.searching)
    ProgressBar searching;*/
    @BindView(R.id.img_loader)
    ImageView img_loader;
    @BindView(R.id.no_results)
    TextView noResults;
    @BindView(R.id.searchRecycler)
    RecyclerView searchRecycler;
    @BindView(R.id.clearSearchText)
    ImageView clearSearchText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Bug: in api< 23 this is never called
        // so mActivity=null
        // so app crashes with null-pointer exception
        //mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSearchView();
        img_loader.setBackgroundResource(R.drawable.anim_loader);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString();
                if (key.length() == 0) {
                    searchAndUpdateViews("");
                    searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_white_36dp, 0);
                    clearSearchText.setVisibility(View.GONE);
                    hintLayout.setVisibility(View.VISIBLE);
                } else {
                    hintLayout.setVisibility(View.GONE);
                    searchAndUpdateViews(key);
                    searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    clearSearchText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        KeyboardUtils.showSoftInput(searchView, activity);
        searchAndUpdateViews("");
        hintLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.clearSearchText)
    protected void clearSearchText() {
        searchView.setText(null);
    }

    private void setUpSearchView() {
        noResults.setVisibility(View.GONE);
        searchAdapter = new SearchAdapter(activity, searchResults, VideoTileAdapter.VIDEO_SECTION_TYPE_NORMAL, true);
        searchRecycler.setHasFixedSize(true);
        searchRecycler.setLayoutManager(new GridLayoutManager(activity, 2));
        searchRecycler.setAdapter(searchAdapter);
    }

    private void searchAndUpdateViews(String key) {
        //UiUtils.showLoadingDialog(activity);
        //searching.setVisibility(View.VISIBLE);
        showLoader();
        PrefUtils prefUtils = PrefUtils.getInstance(getActivity());
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, prefUtils.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, 0));
        params.put(Params.SEARCH_TERM, key);
        params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));
        params.put(Params.IP, CURRENT_IP);
        params.put(APIConstants.Params.APPVERSION, BuildConfig.VERSION_NAME);
        params.put(VERSION_CODE, BuildConfig.VERSION_CODE);

        Call<String> call = apiInterface.searchVideos(SEARCH_VIDEOS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (isAdded()) {
                    //UiUtils.hideLoadingDialog();
                    //searching.setVisibility(View.GONE);
                    img_loader.setVisibility(View.GONE);
                    JSONObject searchResponse = null;
                    try {
                        searchResponse = new JSONObject(response.body());
                    } catch (Exception e) {
                        UiUtils.log(TAG, Log.getStackTraceString(e));
                    }
                    if (searchResponse != null) {
                        searchResults.clear();
                        if (searchResponse.optString(APIConstants.Params.SUCCESS).equals(APIConstants.Constants.TRUE)) {
                            JSONArray data = searchResponse.optJSONArray(APIConstants.Params.DATA);
                            for (int i = 0; i < data.length(); i++) {
                                try {
                                    JSONObject user = data.getJSONObject(i);
                                    Video video = ParserUtils.parseVideoData(user);
                                    searchResults.add(video);
                                } catch (Exception e) {
                                    UiUtils.log(TAG, Log.getStackTraceString(e));
                                }
                            }
                            onDataChange();
                        } else {
                            UiUtils.showShortToast(activity, searchResponse.optString(APIConstants.Params.ERROR_MESSAGE));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (isAdded()) {
                    //UiUtils.hideLoadingDialog();
                    //searching.setVisibility(View.GONE);
                    img_loader.setVisibility(View.GONE);
                    NetworkUtils.onApiError(activity);
                }
            }
        });
    }

    private void onDataChange() {
        if (isAdded()) {
            boolean isEmptyData = searchResults.isEmpty();
            searchRecycler.setVisibility(isEmptyData ? View.GONE : View.VISIBLE);
            noResults.setVisibility(isEmptyData ? View.VISIBLE : View.GONE);
            searchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showLoader() {
        if (!img_loader.isShown()) {
            img_loader.setVisibility(View.VISIBLE);
            AnimationDrawable rocketAnimation = (AnimationDrawable) img_loader.getBackground();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    rocketAnimation.start();
                }
            }).start();
        }
    }
}
