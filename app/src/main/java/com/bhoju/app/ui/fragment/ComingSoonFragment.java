package com.bhoju.app.ui.fragment;

import static com.bhoju.app.network.APIConstants.APIs.GETUP_COMING_NOTIFICATION;
import static com.bhoju.app.network.APIConstants.APIs.GETUP_COMING_VIDEO;
import static com.bhoju.app.network.APIConstants.Constants.SUCCESS;
import static com.bhoju.app.network.APIConstants.Params.ERROR_MESSAGE;
import static org.greenrobot.eventbus.EventBus.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.model.comingSoon;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.ui.adapter.ComingSoonAdapter;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComingSoonFragment extends Fragment {

    RecyclerView comingSoonRecycler;
    TextView noOfflineVideos;
    ComingSoonAdapter comingSoonAdapter;
    private static APIInterface apiInterface;
    ArrayList<comingSoon> comingSoonList = new ArrayList<>();
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    Context context ;

    public static String notification_msg="Will notify you!";
    public ComingSoonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comingSoonRecycler = view.findViewById(R.id.ComingSoonRecycler);
        noOfflineVideos = view.findViewById(R.id.noOfflineVideos);
        comingSoonRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        comingSoonRecycler.setHasFixedSize(true);
        UpComing();
        PrefUtils prefUtils = PrefUtils.getInstance(getActivity());
        Map<String, Object> params = new HashMap<>();

        params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        Log.d("onViewCreatedData", "onViewCreated: tyty 01" +params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1)));
        Log.d("onViewCreatedData", "onViewCreated: tyty 02" +   params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "")));

        comingSoonAdapter = new ComingSoonAdapter(getContext(), comingSoonList, getActivity().getSupportFragmentManager(), getActivity(),params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1)),params.put(APIConstants.Params.TOKEN, prefUtils.getStringValue(PrefKeys.SESSION_TOKEN, "")));
        comingSoonRecycler.setAdapter(comingSoonAdapter);

    }

    public void UpComing() {
        UiUtils.showLoadingDialog(getActivity());
        PrefUtils prefUtils = PrefUtils.getInstance(getActivity());
        Map<String, Object> params = new HashMap<>();
//        params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1));
        params.put(APIConstants.Params.ID,params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1)));
        Log.d("userId", "onResponse: "+params.put(APIConstants.Params.ID, prefUtils.getIntValue(PrefKeys.USER_ID, 1)));
        Call<String> call = apiInterface.getUpcomingVideos(GETUP_COMING_VIDEO,params);
        call.enqueue(new Callback<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject comingVideo = null;

                String responseBody = response.body(); // Assuming response.body() returns a String

                if (responseBody != null) {
                    Log.d("onResponseTest", "onResponse: 1");

                    try {
                        comingVideo = new JSONObject(response.body());
                        Log.d("onResponse_test", "onResponse: ");
                        UiUtils.log(TAG, String.valueOf(comingVideo));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (comingVideo != null) {
                        if (comingVideo.optString(SUCCESS).equals(APIConstants.Constants.TRUE)) {
                            JSONArray data = comingVideo.optJSONArray(APIConstants.Params.DATA);
                            if (data.length() <= 0) {
                                noOfflineVideos.setVisibility(View.VISIBLE);
                            } else {
                                noOfflineVideos.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject comeVideo;
                                try {
                                    comeVideo = data.getJSONObject(i);
//                              UiUtils.log(TAG, "apiarraydata " + comeVideo);
                                    Log.d("onResponse_test", "onResponse: "+comeVideo);
                                    comingSoon comeVideoData = ParserUtils.comingSoonDataParser(comeVideo);
                                    Log.d("onResponse_test", "onResponse: "+comeVideoData);
                                    comingSoonList.add(comeVideoData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            UiUtils.log(TAG, comingVideo.optString(ERROR_MESSAGE));
                        }
                        comingSoonAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("onResponseTest", "onResponse: 2");
                    noOfflineVideos.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    public static void UpComingNotification(Object user_id, Object session_token, int id) {
        Log.d("getId", "notificationData " +  id);
        Map<String, Object> params = new HashMap<>();
        params.put(APIConstants.Params.COMING_SOON_ADMIN_VIDEO_ID, id);
        params.put(APIConstants.Params.USER_ID, user_id);
        params.put(APIConstants.Params.TOKEN,session_token);
        params.put(APIConstants.Params.TYPE_PAGE, "UPCOMING");

        Log.d("UpComingNotification", "User ID  " +user_id);
        Log.d("UpComingNotification", "Session id  " + session_token);
        Log.d("UpComingNotification", "video id  " + id);

        Call<String> call = apiInterface.getComeComingUpNotification(GETUP_COMING_NOTIFICATION, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject comingVideo = null;
                try {
                    comingVideo = new JSONObject(response.body());
                    Log.d("UpComingNotification", "Responce" + comingVideo);
                    notification_msg=comingVideo.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseAllPlayers();
    }

    private void releaseAllPlayers() {
        int count = comingSoonRecycler.getChildCount();
        for (int i = 0; i < count; i++) {
            RecyclerView.ViewHolder viewHolder = comingSoonRecycler.getChildViewHolder(comingSoonRecycler.getChildAt(i));
            if (viewHolder instanceof ComingSoonAdapter.ViewHolder) {
                ((ComingSoonAdapter.ViewHolder) viewHolder).releasePlayer();
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Hide any other UI elements if needed
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Show UI elements again if needed
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}