package com.bhoju.app.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppIntroSampleSlider#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppIntroSampleSlider extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;


    public AppIntroSampleSlider() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AppIntroSampleSlider newInstance(int layoutResId) {
        AppIntroSampleSlider sampleSlide = new AppIntroSampleSlider();

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(bundleArgs);


        return sampleSlide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_app_intro_sample_slide, container, false);
        return inflater.inflate(layoutResId, container, false);

    }
}
