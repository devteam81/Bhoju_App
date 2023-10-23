package com.bhoju.app.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;

import com.bhoju.app.ui.fragment.bottomsheet.PaymentBottomSheet;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.TrackNameProvider;
import com.google.android.exoplayer2.util.Assertions;
import com.bhoju.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackSelection  extends LinearLayout {

    private static final String TAG = TrackSelection.class.getSimpleName();

    /** Listener for changes to the selected tracks. */
    public interface TrackSelectionListener {

        /**
         * Called when the selected tracks changed.
         *
         * @param isDisabled Whether the renderer is disabled.
         * @param overrides List of selected track selection overrides for the renderer.
         */
        void onTrackSelectionChanged(boolean isDisabled, List<DefaultTrackSelector.SelectionOverride> overrides);
    }

    private final int selectableItemBackgroundResourceId;
    private final LayoutInflater inflater;
    private final CheckedTextView disableView;
    private CheckedTextView defaultView;
    private final TrackSelection.ComponentListener componentListener;
    private final SparseArray<DefaultTrackSelector.SelectionOverride> overrides;

    private boolean allowAdaptiveSelections;
    private boolean allowMultipleOverrides;
    private boolean isPlayerLive;

    private TrackNameProvider trackNameProvider;
    private CheckedTextView[][] trackViews;

    ArrayList<String> qualities = new ArrayList<>();

    //@MonotonicNonNull
    private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;
    private int rendererIndex;
    private TrackGroupArray trackGroups;
    private boolean isDisabled;

    int[] colors = new int[] {
            getResources().getColor(R.color.colorAccent),
            getResources().getColor(R.color.button_extra_color)
    };
    int[][] states = new int[][] {
            new int[] {android.R.attr.state_checked}, // disabled
            new int[] {-android.R.attr.state_checked}, // unchecked
    };
    ColorStateList myList = new ColorStateList(states, colors);

    @Nullable
    private TrackSelection.TrackSelectionListener listener;

    /** Creates a track selection view. */
    public TrackSelection(Context context) {
        this(context, null);
    }

    /** Creates a track selection view. */
    public TrackSelection(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /** Creates a track selection view. */
    @SuppressWarnings("nullness")
    public TrackSelection(
            Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);

        overrides = new SparseArray<>();

        // Don't save view hierarchy as it needs to be reinitialized with a call to init.
        setSaveFromParentEnabled(false);

        TypedArray attributeArray =
                context
                        .getTheme()
                        .obtainStyledAttributes(new int[] {android.R.attr.selectableItemBackground});
        selectableItemBackgroundResourceId = attributeArray.getResourceId(0, 0);
        attributeArray.recycle();

        inflater = LayoutInflater.from(context);
        componentListener = new TrackSelection.ComponentListener();
        trackNameProvider = new DefaultTrackNameProvider(getResources());
        trackGroups = TrackGroupArray.EMPTY;

        // View for disabling the renderer.
        disableView =
                (CheckedTextView)
                        inflater.inflate(android.R.layout.simple_list_item_single_choice, this, false);
        disableView.setBackgroundResource(selectableItemBackgroundResourceId);
        disableView.setText(R.string.exo_track_selection_none);
        disableView.setEnabled(false);
        disableView.setFocusable(true);
        disableView.setTextColor(getResources().getColor(R.color.button_extra_color));
        disableView.setCheckMarkTintList(myList);
        disableView.setOnClickListener(componentListener);
        disableView.setVisibility(View.GONE);
        //addView(disableView);
        // Divider view.
        //addView(inflater.inflate(R.layout.exo_list_divider_custom, this, false));
        // View for clearing the override to allow the selector to use its default selection logic.
        defaultView =
                (CheckedTextView)
                        inflater.inflate(android.R.layout.simple_list_item_single_choice, this, false);
        defaultView.setBackgroundResource(selectableItemBackgroundResourceId);
        defaultView.setText(R.string.exo_track_selection_auto);
        defaultView.setEnabled(false);
        defaultView.setFocusable(true);
        defaultView.setTextColor(getResources().getColor(R.color.button_extra_color));
        defaultView.setCheckMarkTintList(myList);
        defaultView.setOnClickListener(componentListener);
        addView(defaultView);
    }

    /**
     * Sets whether adaptive selections (consisting of more than one track) can be made using this
     * selection view.
     *
     * <p>For the view to enable adaptive selection it is necessary both for this feature to be
     * enabled, and for the target renderer to support adaptation between the available tracks.
     *
     * @param allowAdaptiveSelections Whether adaptive selection is enabled.
     */
    public void setAllowAdaptiveSelections(boolean allowAdaptiveSelections) {
        if (this.allowAdaptiveSelections != allowAdaptiveSelections) {
            this.allowAdaptiveSelections = allowAdaptiveSelections;
            updateViews();
        }
    }

    /**
     * Sets whether tracks from multiple track groups can be selected. This results in multiple {@link
     * DefaultTrackSelector.SelectionOverride SelectionOverrides} to be returned by {@link #getOverrides()}.
     *
     * @param allowMultipleOverrides Whether multiple track selection overrides can be selected.
     */
    public void setAllowMultipleOverrides(boolean allowMultipleOverrides) {
        if (this.allowMultipleOverrides != allowMultipleOverrides) {
            this.allowMultipleOverrides = allowMultipleOverrides;
            if (!allowMultipleOverrides && overrides.size() > 1) {
                for (int i = overrides.size() - 1; i > 0; i--) {
                    overrides.remove(i);
                }
            }
            updateViews();
        }
    }

    /**
     * Sets whether an option is available for disabling the renderer.
     *
     * @param showDisableOption Whether the disable option is shown.
     */
    public void setShowDisableOption(boolean showDisableOption) {
        disableView.setVisibility(showDisableOption ? View.VISIBLE : View.GONE);
    }

    /**
     * Sets the {@link TrackNameProvider} used to generate the user visible name of each track and
     * updates the view with track names queried from the specified provider.
     *
     * @param trackNameProvider The {@link TrackNameProvider} to use.
     */
    public void setTrackNameProvider(TrackNameProvider trackNameProvider) {
        this.trackNameProvider = Assertions.checkNotNull(trackNameProvider);
        updateViews();
    }

    /**
     * Initialize the view to select tracks for a specified renderer using {@link MappingTrackSelector.MappedTrackInfo} and
     * a set of {@link DefaultTrackSelector.Parameters}.
     *
     * @param mappedTrackInfo The {@link MappingTrackSelector.MappedTrackInfo}.
     * @param rendererIndex The index of the renderer.
     * @param isDisabled Whether the renderer should be initially shown as disabled.
     * @param overrides List of initial overrides to be shown for this renderer. There must be at most
     *     one override for each track group. If {@link #setAllowMultipleOverrides(boolean)} hasn't
     *     been set to {@code true}, only the first override is used.
     * @param listener An optional listener for track selection updates.
     */
    public void init(
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo,
            int rendererIndex,
            boolean isDisabled,
            List<DefaultTrackSelector.SelectionOverride> overrides,
            @Nullable TrackSelection.TrackSelectionListener listener,
            boolean isPlayerLive) {
        this.mappedTrackInfo = mappedTrackInfo;
        this.rendererIndex = rendererIndex;
        this.isDisabled = isDisabled;
        this.listener = listener;
        this.isPlayerLive = isPlayerLive;
        int maxOverrides = allowMultipleOverrides ? overrides.size() : Math.min(overrides.size(), 1);
        for (int i = 0; i < maxOverrides; i++) {
            DefaultTrackSelector.SelectionOverride override = overrides.get(i);
            this.overrides.put(override.groupIndex, override);
        }
        updateViews();
    }

    /** Returns whether the renderer is disabled. */
    public boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * Returns the list of selected track selection overrides. There will be at most one override for
     * each track group.
     */
    public List<DefaultTrackSelector.SelectionOverride> getOverrides() {
        List<DefaultTrackSelector.SelectionOverride> overrideList = new ArrayList<>(overrides.size());
        for (int i = 0; i < overrides.size(); i++) {
            overrideList.add(overrides.valueAt(i));
        }
        return overrideList;
    }

    // Private methods.

    private void updateViews() {
        // Remove previous per-track views.
        for (int i = getChildCount() - 1; i >= 3; i--) {
            removeViewAt(i);
        }

        if (mappedTrackInfo == null) {
            // The view is not initialized.
            disableView.setEnabled(false);
            defaultView.setEnabled(false);
            return;
        }
        disableView.setEnabled(true);
        defaultView.setEnabled(true);

        trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);

        if(trackGroups.length==0)
        {
            CheckedTextView trackView =
                    (CheckedTextView) inflater.inflate(android.R.layout.simple_list_item_single_choice, this, false);
            trackView.setBackgroundResource(selectableItemBackgroundResourceId);
            trackView.setText(getResources().getString(R.string.not_available));
            /*if (mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex)
                    == RendererCapabilities.FORMAT_HANDLED) {
                trackView.setFocusable(true);
                trackView.setTag(Pair.create(groupIndex, trackIndex));
                trackView.setOnClickListener(componentListener);
            } else {
                trackView.setFocusable(false);
                trackView.setEnabled(false);
            }*/
            trackView.setTextColor(getResources().getColor(R.color.button_extra_color));
            trackView.setCheckMarkTintList(myList);
            trackViews[0][0] = trackView;
            addView(trackView);
            // Divider view.
            addView(inflater.inflate(R.layout.exo_list_divider_custom, this, false));
        }
        // Add per-track views.
        trackViews = new CheckedTextView[trackGroups.length][];
        boolean enableMultipleChoiceForMultipleOverrides = shouldEnableMultiGroupSelection();
        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            TrackGroup group = trackGroups.get(groupIndex);
            boolean enableMultipleChoiceForAdaptiveSelections = shouldEnableAdaptiveSelection(groupIndex);
            trackViews[groupIndex] = new CheckedTextView[group.length];
            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                if (trackIndex == 0 && rendererIndex !=2) {
                    addView(inflater.inflate(R.layout.exo_list_divider_custom, this, false));
                }
                int trackViewLayoutId =
                        enableMultipleChoiceForAdaptiveSelections || enableMultipleChoiceForMultipleOverrides
                                ? android.R.layout.simple_list_item_multiple_choice
                                : android.R.layout.simple_list_item_single_choice;
                CheckedTextView trackView =
                        (CheckedTextView) inflater.inflate(trackViewLayoutId, this, false);
                trackView.setBackgroundResource(selectableItemBackgroundResourceId);
                String quality = getQuality(trackNameProvider.getTrackName(group.getFormat(trackIndex)), rendererIndex);
                if(rendererIndex==1 && quality.equalsIgnoreCase("Audio Track #")) {
                    int trackLayer = groupIndex+1;
                    trackView.setText(quality + trackLayer);
                }
                else
                    trackView.setText(quality);
                if (mappedTrackInfo.getTrackSupport(rendererIndex, groupIndex, trackIndex)
                        == RendererCapabilities.FORMAT_HANDLED) {
                    trackView.setFocusable(true);
                    trackView.setTag(Pair.create(groupIndex, trackIndex));
                    trackView.setOnClickListener(componentListener);
                } else {
                    trackView.setFocusable(false);
                    trackView.setEnabled(false);
                }
                trackView.setTextColor(getResources().getColor(R.color.button_extra_color));
                trackView.setCheckMarkTintList(myList);
                trackViews[groupIndex][trackIndex] = trackView;
                if(!qualities.contains(quality)) {
                    if(rendererIndex !=2) {
                        qualities.add(getQuality(trackNameProvider.getTrackName(group.getFormat(trackIndex)), rendererIndex));
                        addView(trackView);
                    }else
                    {
                        UiUtils.log(TAG,"quality: "+ quality);
                        if(!quality.equalsIgnoreCase(getResources().getString(R.string.exo_controls_cc_enabled_description)))
                        {
                            qualities.add(getQuality(trackNameProvider.getTrackName(group.getFormat(trackIndex)), rendererIndex));
                            addView(trackView);
                        }
                    }
                }

                if (rendererIndex == 2) {
                    //removeViewAt(0);
                    defaultView = (CheckedTextView) getChildAt(0);
                    defaultView.setText( getResources().getString(R.string.exo_controls_cc_enabled_description));
                }else
                {
                    if(!isPlayerLive) {
                        defaultView = (CheckedTextView) getChildAt(0);
                        defaultView.setText(quality);
                    }
                    addView(inflater.inflate(R.layout.exo_list_divider_custom, this, false));
                }
                // Divider view.
                addView(inflater.inflate(R.layout.exo_list_divider_custom, this, false));
            }
        }

        updateViewStates();
    }

    private void updateViewStates() {
        disableView.setChecked(isDisabled);
        defaultView.setChecked(!isDisabled && overrides.size() == 0);
        for (int i = 0; i < trackViews.length; i++) {
            DefaultTrackSelector.SelectionOverride override = overrides.get(i);
            for (int j = 0; j < trackViews[i].length; j++) {
                trackViews[i][j].setChecked(override != null && override.containsTrack(j));
            }
        }
    }

    private void onClick(View view) {
        if (view == disableView) {
            onDisableViewClicked();
        } else if (view == defaultView) {
            onDefaultViewClicked();
        } else {
            onTrackViewClicked(view);
        }
        updateViewStates();
        if (listener != null) {
            listener.onTrackSelectionChanged(getIsDisabled(), getOverrides());
        }
    }

    private void onDisableViewClicked() {
        isDisabled = true;
        overrides.clear();
    }

    private void onDefaultViewClicked() {
        isDisabled = false;
        overrides.clear();
    }

    private void onTrackViewClicked(View view) {
        isDisabled = false;
        @SuppressWarnings("unchecked")
        Pair<Integer, Integer> tag = (Pair<Integer, Integer>) view.getTag();
        int groupIndex = tag.first;
        int trackIndex = tag.second;
        DefaultTrackSelector.SelectionOverride override = overrides.get(groupIndex);
        Assertions.checkNotNull(mappedTrackInfo);
        if (override == null) {
            // Start new override.
            if (!allowMultipleOverrides && overrides.size() > 0) {
                // Removed other overrides if we don't allow multiple overrides.
                overrides.clear();
            }
            overrides.put(groupIndex, new DefaultTrackSelector.SelectionOverride(groupIndex, trackIndex));
        } else {
            // An existing override is being modified.
            int overrideLength = override.length;
            int[] overrideTracks = override.tracks;
            boolean isCurrentlySelected = ((CheckedTextView) view).isChecked();
            boolean isAdaptiveAllowed = shouldEnableAdaptiveSelection(groupIndex);
            boolean isUsingCheckBox = isAdaptiveAllowed || shouldEnableMultiGroupSelection();
            if (isCurrentlySelected && isUsingCheckBox) {
                // Remove the track from the override.
                if (overrideLength == 1) {
                    // The last track is being removed, so the override becomes empty.
                    overrides.remove(groupIndex);
                } else {
                    int[] tracks = getTracksRemoving(overrideTracks, trackIndex);
                    overrides.put(groupIndex, new DefaultTrackSelector.SelectionOverride(groupIndex, tracks));
                }
            } else if (!isCurrentlySelected) {
                if (isAdaptiveAllowed) {
                    // Add new track to adaptive override.
                    int[] tracks = getTracksAdding(overrideTracks, trackIndex);
                    overrides.put(groupIndex, new DefaultTrackSelector.SelectionOverride(groupIndex, tracks));
                } else {
                    // Replace existing track in override.
                    overrides.put(groupIndex, new DefaultTrackSelector.SelectionOverride(groupIndex, trackIndex));
                }
            }
        }
    }

    //@RequiresNonNull("mappedTrackInfo")
    private boolean shouldEnableAdaptiveSelection(int groupIndex) {
        return allowAdaptiveSelections
                && trackGroups.get(groupIndex).length > 1
                && mappedTrackInfo.getAdaptiveSupport(rendererIndex, groupIndex, false)
                != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED;
    }

    private boolean shouldEnableMultiGroupSelection() {
        return allowMultipleOverrides && trackGroups.length > 1;
    }

    private static int[] getTracksAdding(int[] tracks, int addedTrack) {
        tracks = Arrays.copyOf(tracks, tracks.length + 1);
        tracks[tracks.length - 1] = addedTrack;
        return tracks;
    }

    private static int[] getTracksRemoving(int[] tracks, int removedTrack) {
        int[] newTracks = new int[tracks.length - 1];
        int trackCount = 0;
        for (int track : tracks) {
            if (track != removedTrack) {
                newTracks[trackCount++] = track;
            }
        }
        return newTracks;
    }

    public String getQuality(String resolution, int rendererIndex)
    {
        if(resolution.equalsIgnoreCase("None")) {
            //if (rendererIndex == 0) return "Disable Quality";
            //if (rendererIndex == 1) return "Disable Language";
            if (rendererIndex == 2) return getResources().getString(R.string.exo_controls_cc_enabled_description);
        }

        String[] resolutions ={"Ultra Low", "Low","Medium","High","HD+"};
        try {
            String[] widthHeight = resolution.split(" ");
            long value=0;
            try {
                value = Integer.parseInt(widthHeight[0]) * Integer.parseInt(widthHeight[2]);
            }catch (Exception e)
            {
                if(resolution.contains("x")) {
                    widthHeight = resolution.split("x");
                    UiUtils.log("Download", "left: " + widthHeight[0] + " right: " + widthHeight[1]);
                    value = Integer.parseInt(widthHeight[0]) * Integer.parseInt(widthHeight[1]);
                }
            }

            if(value == 0)
                return resolution;
            if(value <= 130000)
                return "Ultra Low";
            else if(value < 230401)
                return "Low";
            else if(value < 518401)
                return "Medium";
            else if(value < 921601)
                return "High";
            else if(value < 2073601)
                return "HD";
            else return "HD+";

        }catch (Exception e)
        {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            UiUtils.log("Exception", "Message: "+ e.getMessage());
            return resolution;
        }


         /*3840x2160.
         2560x1440.
         1920x1080.
        1280x720.
        854x480.
        640x360.
        426x240*/
    }

    // Internal classes.

    private class ComponentListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            TrackSelection.this.onClick(view);
        }
    }
}
