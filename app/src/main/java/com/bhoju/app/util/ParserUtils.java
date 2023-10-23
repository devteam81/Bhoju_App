package com.bhoju.app.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bhoju.app.model.Audio;
import com.bhoju.app.model.Card;
import com.bhoju.app.model.Cast;
import com.bhoju.app.model.DownloadUrl;
import com.bhoju.app.model.GenreSeason;
import com.bhoju.app.model.OfflineVideo;
import com.bhoju.app.model.OfflineVideoSections;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.model.Video;
import com.bhoju.app.model.VideoSection;
import com.bhoju.app.model.comingSoon;
import com.bhoju.app.ui.adapter.VideoTileAdapter;
import com.bhoju.app.util.database.DatabaseClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.bhoju.app.model.Video.DownloadStatus;
import static com.bhoju.app.model.Video.PayPerViewType;
import static com.bhoju.app.model.Video.SubscriptionType;
import static com.bhoju.app.model.Video.VideoType;
import static com.bhoju.app.network.APIConstants.Params;
import static com.bhoju.app.network.APIConstants.Params.DESCRIPTION;
import static com.bhoju.app.network.APIConstants.Params.TITLE;
import static com.bhoju.app.network.APIConstants.Params.TYPE;

public class ParserUtils {

    private static final String TAG = ParserUtils.class.getSimpleName();

    private ParserUtils() {

    }

    public static Video parseVideoData(JSONObject dataObj) {
        Video video = new Video();
        video.setTrailerVideo(false);
        video.setAdminVideoId(dataObj.optInt(Params.ADMIN_VIDEO_ID));
        video.setAdminUniqueId(dataObj.optString(Params.ADMIN_UNIQUE_ID));
        video.setCategoryId(dataObj.optInt(Params.CATEGORY_ID));
        video.setSubCategoryId(dataObj.optInt(Params.SUB_CATEGORY_ID));
        video.setGenreId(dataObj.optInt(Params.GENRE_ID));
        video.setTitle(dataObj.optString(TITLE));
        video.setEpTitle(dataObj.optString(Params.EP_TITLE));
        video.setDescription(dataObj.optString(DESCRIPTION));
        video.setDetail(dataObj.optString(Params.DETAILS));
        video.setDefaultImage(dataObj.optString(Params.DEFAULT_IMAGE));
        video.setThumbNailUrl(dataObj.optString(Params.MOBILE_IMAGE));
        video.setPublishTime(dataObj.optString(Params.PUBLISH_TIME));
        video.setAge(dataObj.optInt(Params.AGE));
        video.setVideoUrl(dataObj.optString(Params.VIDEO_URL));
        video.setVideoLanguageUrl(parseStringLanguage(dataObj.optJSONArray(Params.VIDEO_URL_LANGUAGE)));
        video.setAudioLanguageUrl(parseLanguage(dataObj.optJSONArray(Params.AUDIO_URL_LANGUAGE)));
        video.setShareUrl(dataObj.optString(Params.SHARE_LINK));
        video.setShareMessage(dataObj.optString(Params.SHARE_MESSAGE));
        video.setSubTitleUrl(dataObj.optString(Params.SUBTITLE_URL));
        video.setSubTitleLang(dataObj.optString(Params.SUBTITLE_LANG));
        video.setDuration(dataObj.optString(Params.DURATION));
        video.setRatings(dataObj.optInt(Params.RATINGS));
        video.setViewCount(dataObj.optInt(Params.WATCH_COUNT));
        video.setPayPerView(dataObj.optInt(Params.SHOULD_DISPLAY_PPV) == 1);
        video.setSeekHere(dataObj.optInt(Params.SEEK_HERE));
        video.setInWishList(dataObj.optInt(Params.WISHLIST_STATUS) == 1);
        video.setInHistory(dataObj.optInt(Params.HISTORY_STATUS) == 1);
        video.setInSpam(dataObj.optInt(Params.IS_SPAM) == 1);
        video.setSeasonVideo(dataObj.optInt(Params.IS_SEASON_VIDEO) == 1);
        video.setLiked(dataObj.optInt(Params.IS_LIKED) == 1);
        video.setKidsVideo(dataObj.optInt(Params.IS_KIDS) == 1);
        video.setLikes(dataObj.optInt(Params.LIKES));
        video.setCurrency(dataObj.optString(Params.CURRENCY));
        video.setUserSubscribed(dataObj.optInt(Params.USER_TYPE) == 1);
        video.setAmount(dataObj.optInt(Params.PPV_AMOUNT));
        video.setUserType(dataObj.optString(Params.USER_TYPE));
        video.setResolutions(parseResolutions(dataObj.optJSONObject(Params.MAIN_VIDEO_RESOLUTIONS)));
        video.setNumDaysToExpire(dataObj.optInt(Params.NO_OF_DAYS));
//        video.setDownloadResolutions(parseDownloadResolutions(dataObj.optJSONArray(Params.DOWNLOAD_URLS)));
        video.setCasts(parseCastAndCrew(dataObj.optJSONArray(Params.CAST_CREW)));
        video.setGenres(parseGenre(dataObj.optJSONArray(Params.GENRE)));
        video.setSeasons(parseSeason(dataObj.optJSONArray(Params.SEASON)));
        video.setChoosePlans(parseChoosePlan(dataObj.optJSONArray(Params.PPV_TYPE_CONTENT)));

        video.setDirector(dataObj.optString(Params.DIRECTOR));
        video.setGenresList(dataObj.optString(Params.GENREMAIN));

        video.setSeasonName(dataObj.optString(Params.SEASON_NAME));
        video.setSeasonId(dataObj.optInt(Params.SEASON_ID));
        video.setParentId(dataObj.optInt(Params.PARENT_ID));
        video.setA_record(dataObj.optInt(Params.A_RECORD));

        video.setRatingTime(dataObj.optString(Params.RATING_TIME));
        video.setRatingType(dataObj.optString(Params.RATING_TYPE));
        video.setRatingUrl(dataObj.optString(Params.RATING_URL));
        video.setRatingTrailerTime(dataObj.optString(Params.RATING_TRAILER_TIME));
        video.setRatingTrailerType(dataObj.optString(Params.RATING_TRAILER_TYPE));
        video.setRatingTrailerUrl(dataObj.optString(Params.RATING_TRAILER_URL));

        video.setIsSingle(dataObj.optInt(Params.IS_SINGLE));
        video.setIsMain(dataObj.optInt(Params.IS_MAIN));
        video.setIsTrailerFree(dataObj.optInt(Params.IS_TRAILER_FREE));
        video.setTrailerVideo(dataObj.optString(Params.TRAILER_VIDEO));
        video.setTrailerDefaultImage(dataObj.optString(Params.TRAILER_DEFAULT_IMAGE));
        video.setTrailerBrowseImage(dataObj.optString(Params.TRAILER_BROWSE_IMAGE));
        video.setTrailerMobileImage(dataObj.optString(Params.TRAILER_MOBILE_IMAGE));
        video.setTrailerVideoLanguageUrl(parseStringLanguage(dataObj.optJSONArray(Params.TRAILER_VIDEO_URL_LANGUAGE)));
        video.setTrailerAudioLanguageUrl(parseLanguage(dataObj.optJSONArray(Params.TRAILER_AUDIO_URL_LANGUAGE)));
        video.setTrailerSubtitle(dataObj.optString(Params.TRAILER_SUBTITLE));
        video.setTrailerSubtitleLang(dataObj.optString(Params.TRAILER_SUBTITLE_LANG));

        video.setBrowseImage(dataObj.optString(Params.BROWSE_IMAGE));

        PayPerViewType payPerViewType;
        switch (dataObj.optInt(Params.PPV_PAGE_TYPE)) {
            case 1:
                payPerViewType = PayPerViewType.ONE;
                break;
            case 2:
                payPerViewType = PayPerViewType.TWO;
                break;
            case 3:
            default:
                payPerViewType = PayPerViewType.THREE;
                break;
        }
        video.setPayPerViewType(payPerViewType);

        SubscriptionType subscriptionType;
        switch (dataObj.optInt(Params.TYPE_OF_SUBSCRIPTION)) {
            case 1:
                subscriptionType = SubscriptionType.ONE;
                break;
            case 2:
                subscriptionType = SubscriptionType.TWO;
                break;
            case 3:
            default:
                subscriptionType = SubscriptionType.THREE;
                break;
        }
        video.setSubscriptionType(subscriptionType);

        VideoType videoType;
        switch (dataObj.optInt(Params.VIDEO_TYPE)) {
            case 2:
                videoType = VideoType.VIDEO_YOUTUBE;
                break;
            case 3:
                videoType = VideoType.VIDEO_OTHER;
                break;
            case 1:
            default:
                videoType = VideoType.VIDEO_MANUAL;
                break;
        }
        video.setVideoType(videoType);

        video.setDownloadId(dataObj.optInt(Params.DOWNLOAD_ID));
        video.setDownloadUrl(dataObj.optString(Params.DOWNLOAD_URL));
        video.setDownloadSavePath(dataObj.optString(Params.DOWNLOAD_SAVE_PATH));
        video.setDownloadFileName(dataObj.optString(Params.DOWNLOAD_FILE_NAME));

        video.setDownloadable(dataObj.optInt(Params.DOWNLOAD_BUTTON_STATUS) != 0);
        DownloadStatus downloadStatus;
        switch (dataObj.optInt(Params.DOWNLOAD_VIEW_STATUS)) {
            case 0:
                downloadStatus = DownloadStatus.SHOW_DOWNLOAD;
                break;
            case 1:
                downloadStatus = DownloadStatus.SHOW_DOWNLOAD;
                break;
            case 2:
                downloadStatus = DownloadStatus.DOWNLOAD_PROGRESS;
                break;
            case 3:
                downloadStatus = DownloadStatus.DOWNLOAD_PAUSED;
                break;
            case 4:
                downloadStatus = DownloadStatus.DOWNLOAD_COMPLETED;
                break;
            case 5:
                downloadStatus = DownloadStatus.SHOW_DOWNLOAD;
                break;
            default:
                downloadStatus = DownloadStatus.NEED_TO_PAY;
                break;
        }
        video.setDownloadStatus(downloadStatus);

        //Downloaded
        DownloadStatus downloadedStatus;
        switch (dataObj.optInt(Params.DOWNLOADED_VIEW_STATUS)) {
            case 0:
                downloadedStatus = DownloadStatus.SHOW_DOWNLOAD;
                break;
            case 1:
                downloadedStatus = DownloadStatus.SHOW_DOWNLOAD;
                break;
            case 2:
                downloadedStatus = DownloadStatus.DOWNLOAD_PROGRESS;
                break;
            case 3:
                downloadedStatus = DownloadStatus.DOWNLOAD_PAUSED;
                break;
            case 4:
                downloadedStatus = DownloadStatus.DOWNLOAD_COMPLETED;
                break;
            case 5:
                downloadedStatus = DownloadStatus.SHOW_DOWNLOAD;
                break;
            default:
                downloadedStatus = DownloadStatus.NEED_TO_PAY;
                break;
        }
        video.setDownloadedStatus(downloadedStatus);
        return video;
    }

    public static void parseVideoRelatedData(Video video, JSONObject dataObj) {
        video.setSeasonVideo(dataObj.optInt(Params.IS_SEASON_VIDEO) == 1);
        video.setTrailerVideos(parseTrailerVideos(dataObj.optJSONArray(Params.TRAILER_SECTION)));
        video.setGenreSeasons(parseGenreSeasons(video, dataObj.optJSONArray(Params.GENRES)));
        //video.setSeasonEpisodes(parseSeasonEpisodes(video, dataObj.optJSONArray(Params.SEASON)));
    }

    private static ArrayList<Video> parseTrailerVideos(JSONArray trailerArr) {
        ArrayList<Video> trailers = new ArrayList<>();
        if (trailerArr == null)
            return trailers;

        for (int i = 0; i < trailerArr.length(); i++) {
            try {
                JSONObject trailerObj = trailerArr.getJSONObject(i);
                Video trailer = new Video();
                trailer.setTrailerVideo(true);
                trailer.setTitle(trailerObj.optString(Params.NAME));
                VideoType videoType;
                switch (trailerObj.optInt(Params.VIDEO_TYPE)) {
                    case 2:
                        videoType = VideoType.VIDEO_YOUTUBE;
                        break;
                    case 3:
                        videoType = VideoType.VIDEO_OTHER;
                        break;
                    case 1:
                    default:
                        videoType = VideoType.VIDEO_MANUAL;
                        break;
                }
                trailer.setVideoType(videoType);
                trailer.setThumbNailUrl(trailerObj.optString(Params.DEFAULT_IMAGE));
                trailer.setResolutions(parseResolutions(trailerObj.optJSONObject(Params.RESOLUTIONS)));
                trailer.setVideoUrl(trailer.getResolutions().get(Params.ORIGINAL));
                trailers.add(trailer);
            } catch (JSONException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return trailers;
    }

    private static HashMap<String, String> parseResolutions(JSONObject resolutionsObj) {
        HashMap<String, String> resolutions = new HashMap<>();
        if (resolutionsObj == null)
            return resolutions;

        Iterator<String> keys = resolutionsObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            resolutions.put(key, resolutionsObj.optString(key));
        }
        return resolutions;
    }

    private static ArrayList<DownloadUrl> parseDownloadResolutions(JSONArray downloadResolutionsArr) {
        ArrayList<DownloadUrl> downloadUrls = new ArrayList<>();
        if (downloadResolutionsArr == null)
            return downloadUrls;

        for (int i = 0; i < downloadResolutionsArr.length(); i++) {
            try {
                JSONObject downloadResObj = downloadResolutionsArr.getJSONObject(i);
                downloadUrls.add(new DownloadUrl(downloadResObj.optString(TITLE),
                        downloadResObj.optString(Params.LINK),
                        downloadResObj.optString(TYPE),
                        UiUtils.checkString(downloadResObj.optString(Params.LANGUAGE))? "":downloadResObj.optString(Params.LANGUAGE)));
            } catch (JSONException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return downloadUrls;
    }

    private static ArrayList<GenreSeason> parseGenreSeasons(Video curVideo, JSONArray seasons) {
        ArrayList<GenreSeason> genreSeasons = new ArrayList<>();

        int currentSeasonId = -1;

        for (int i = 0; i < seasons.length(); i++) {
            try {
                JSONObject object = seasons.getJSONObject(i);
                boolean isSelfSeason = object.optInt(Params.IS_SELECTED) == 1;

                GenreSeason genre = new GenreSeason();
                genre.setId(object.optInt(Params.GENRE_ID));
                genre.setName(object.optString(Params.GENRE_NAME));
                genreSeasons.add(genre);

                if (isSelfSeason)
                    currentSeasonId = genre.getId();
            } catch (JSONException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
            if(currentSeasonId == -1 && seasons.length()>0)
            {
                try {
                    currentSeasonId = seasons.getJSONObject(0).optInt(Params.GENRE_ID);
                } catch (JSONException e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        }

        curVideo.setSeasonId(currentSeasonId);

        return genreSeasons;
    }

    private static ArrayList<GenreSeason> parseSeasonEpisodes(Video curVideo, JSONArray seasons) {
        ArrayList<GenreSeason> seasonEpisodes = new ArrayList<>();

        int currentSeasonId = -1;

        for (int i = 0; i < seasons.length(); i++) {
            try {
                JSONObject object = seasons.getJSONObject(i);
                boolean isSelfSeason = object.optInt(Params.IS_SELECTED) == 1;

                GenreSeason season = new GenreSeason();
                season.setId(object.optInt(Params.SEASON_ID));
                season.setName(object.optString(Params.SEASON_NAME));
                seasonEpisodes.add(season);

                if (isSelfSeason)
                    currentSeasonId = season.getId();
            } catch (JSONException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }

        curVideo.setSeasonId(currentSeasonId);

        return seasonEpisodes;
    }


    public static SubscriptionPlan parsePlan(JSONObject planObj) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(planObj.optInt(Params.SUBSCRIPTION_ID));
        plan.setTitle(planObj.optString(TITLE));
        plan.setDescription(planObj.optString(DESCRIPTION));
        plan.setAmount(planObj.optDouble(Params.AMOUNT));
        plan.setCurrency(planObj.optString(Params.CURRENCY));
        plan.setChkCountry(planObj.optString(Params.CHK_COUNTRY));
        plan.setCode(planObj.optString(Params.CODE));
        plan.setSymbol(planObj.optString(Params.SYMBOL));
        plan.setGooglePlayProductId(planObj.optString(Params.PRODUCT_ID));
        plan.setNoOfAccounts(planObj.optInt(Params.NO_OF_ACCOUNTS));
        plan.setPaymentId(planObj.optString(Params.PAYMENT_ID));
        plan.setPopular(planObj.optInt(Params.POPULAR_STATUS) == 1);
        plan.setCouponAmt(planObj.optString(Params.COUPON_AMT));
        plan.setCouponCode(planObj.optString(Params.COUPON_CODE));
        plan.setActivePlan(planObj.optInt(Params.ACTIVE_PLAN) == 1);
        plan.setCancelled(planObj.optInt(Params.CANCEL_STATUS) == 0);
        plan.setPaymentStatus(planObj.optString(Params.PAYMENT_STATUS));
        plan.setMonths(planObj.optInt(Params.PLAN));
        plan.setMonthFormatted(planObj.optString(Params.PLAN_FORMATTED));
        plan.setExpires(planObj.optString(Params.EXPIRIES_ON));
        plan.setPaymentMode(planObj.optString(Params.PAYMENT_MODE));
        plan.setTotalAmt(planObj.optDouble(Params.TOTAL_AMT));
        plan.setReferralDiscountAmt(planObj.optString(Params.WALLET_AMOUNT));
        plan.setListedPrice(planObj.optDouble(Params.LISTEDPRICE));
        plan.setDiscount(planObj.optInt(Params.DISCOUNT));
        plan.setDays(planObj.optInt(Params.DAYS));
        plan.setPercentage(planObj.optInt(Params.PERCENTAGE));
        return plan;
    }

    public static VideoSection parseOriginalsVideos(JSONObject originalsObj) {
        VideoSection original = null;
        JSONArray originalVideoItems = originalsObj.optJSONArray(Params.DATA);
        ArrayList<Video> originalVideos = parseVideoItemsArray(originalVideoItems);
        if (!originalVideos.isEmpty()) {
            original = new VideoSection("Originals"
                    , originalsObj.optString(Params.SEE_ALL_URL)
                    , originalsObj.optString(Params.URL_TYPE)
                    , originalsObj.optString(Params.URL_PAGE_ID)
                    , VideoTileAdapter.VIDEO_SECTION_TYPE_ORIGINALS
                    , originalVideos);
        }
        return original;
    }

    public static ArrayList<VideoSection> parseVideoSections(JSONArray videoSectionsObj, Context context) {
        ArrayList<VideoSection> videoSections = new ArrayList<>();
        ArrayList<Video> videos;
        for (int i = 0; i < videoSectionsObj.length(); i++) {
            try {
                JSONObject videoSectionObj = videoSectionsObj.getJSONObject(i);
                JSONArray videosForThisSection = videoSectionObj.optJSONArray(Params.DATA);
                if (videosForThisSection != null) {
                    videos = parseVideoItemsArray(videosForThisSection);
                    if (!videos.isEmpty()) {
                        videoSections.add(new VideoSection(
                                videoSectionObj.optString(TITLE)
                                , videoSectionObj.optString(Params.SEE_ALL_URL)
                                , videoSectionObj.optString(Params.URL_TYPE)
                                , videoSectionObj.optString(Params.URL_PAGE_ID)
                                , VideoTileAdapter.VIDEO_SECTION_TYPE_NORMAL
                                , videos));
                        saveVideoSection(videoSections.get(0), context, videoSections.get(0).getVideos());
                    }
                }
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return videoSections;
    }

    // Saving sections in offline
    private static void saveVideoSection(VideoSection data, Context context, ArrayList<Video> videoList) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            List<OfflineVideoSections> offlineVideoSections = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .dataBaseAction()
                        .truncateVideoSectiondata();

                OfflineVideoSections videoSections = new OfflineVideoSections();
                videoSections.setTitle(data.getTitle());
                offlineVideoSections.add(videoSections);
                saveVideosOffline(videoList, context);

                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .dataBaseAction()
                        .insertIntoVideoSection(offlineVideoSections);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private static void saveVideosOffline(ArrayList<Video> videoArrayList, Context context) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            List<OfflineVideo> videos = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .dataBaseAction()
                        .truncateOfflineVideo();

                if (videoArrayList != null) {
                    for (int i = 0; i < videoArrayList.size(); i++) {
                        try {
                            OfflineVideo video = new OfflineVideo();
                            video.setTitle(videoArrayList.get(i).getTitle());
                            video.setThumbNailUrl(videoArrayList.get(i).getThumbNailUrl());
                            video.setAdminVideoId(videoArrayList.get(i).getAdminVideoId());
                            videos.add(video);
                        } catch (Exception e) {
                            UiUtils.log(TAG, Log.getStackTraceString(e));
                        }
                    }
                }
                DatabaseClient.getInstance(context).getAppDatabase()
                        .dataBaseAction()
                        .insertVideoOffline(videos);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    public static ArrayList<Video> parseVideoItemsArray(JSONArray videoArray) {
        ArrayList<Video> videos = new ArrayList<>();
        for (int i = 0; i < videoArray.length(); i++) {
            try {
                JSONObject object = videoArray.getJSONObject(i);
                Video video = parseVideoData(object);
                videos.add(video);
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return videos;
    }

    private static ArrayList<String> parseStringLanguage(JSONArray audioObj) {
        ArrayList<String> videoLanguage = new ArrayList<>();

        if (audioObj == null)
            return videoLanguage;

        for (int i = 0; i < audioObj.length(); i++) {
            try {
                UiUtils.log(TAG,"Language->"+ audioObj.getString(i));
                videoLanguage.add(audioObj.getString(i));
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return videoLanguage;
    }

    private static ArrayList<Audio> parseLanguage(JSONArray audioObj) {
        ArrayList<Audio> audio = new ArrayList<>();

        if (audioObj == null)
            return audio;

        for (int i = 0; i < audioObj.length(); i++) {
            try {
                JSONObject audioItem = audioObj.getJSONObject(i);
                audio.add(new Audio(audioItem.optInt(Params.ID)
                        , audioItem.optString(TITLE)
                        , audioItem.optString(Params.URL)));
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return audio;
    }

    private static ArrayList<Cast> parseCastAndCrew(JSONArray castCrewObj) {
        ArrayList<Cast> casts = new ArrayList<>();

        if (castCrewObj == null)
            return casts;

        for (int i = 0; i < castCrewObj.length(); i++) {
            try {
                JSONObject castItem = castCrewObj.getJSONObject(i);
                casts.add(new Cast(castItem.optInt(Params.CAST_CREW_ID)
                        , castItem.optString(Params.NAME)));
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return casts;
    }

    private static ArrayList<Cast> parseChoosePlan(JSONArray castCrewObj) {
        ArrayList<Cast> choose = new ArrayList<>();

        if (castCrewObj == null)
            return choose;

        for (int i = 0; i < castCrewObj.length(); i++) {
            try {
                JSONObject content = castCrewObj.getJSONObject(i);
                Cast cont = new Cast();
                cont.setName(content.optString(TITLE));
                cont.setDesc(content.optString(DESCRIPTION));
                cont.setType(content.optString(TYPE));
                choose.add(cont);
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return choose;
    }

    private static ArrayList<GenreSeason> parseGenre(JSONArray genreObj) {
        ArrayList<GenreSeason> genres = new ArrayList<>();

        if (genreObj == null)
            return genres;

        for (int i = 0; i < genreObj.length(); i++) {
            try {
                JSONObject genreItem = genreObj.getJSONObject(i);
                genres.add(new GenreSeason(genreItem.optInt(Params.ID)
                        , genreItem.optString(Params.NAME)));
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return genres;
    }

    private static ArrayList<GenreSeason> parseSeason(JSONArray seasonObj) {
        ArrayList<GenreSeason> seasons = new ArrayList<>();

        if (seasonObj == null)
            return seasons;

        for (int i = 0; i < seasonObj.length(); i++) {
            try {
                JSONObject genreItem = seasonObj.getJSONObject(i);
                seasons.add(new GenreSeason(genreItem.optInt(Params.ID)
                        , genreItem.optString(Params.NAME)));
            } catch (Exception e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
        }
        return seasons;
    }

    public static Card parseCardData(JSONObject cardObj) {
        Card card = new Card();
        card.setDefault(cardObj.optInt(Params.IS_DEFAULT) == 1);
        card.setId(cardObj.optInt(Params.USER_CARD_ID));
        card.setCardToken(cardObj.optString(Params.CARD_TOKEN));
        card.setCvv(cardObj.optString(Params.CARD_CVV));
        card.setLast4(cardObj.optString(Params.CARD_LAST_FOUR));
        card.setMonth(cardObj.optString(Params.CARD_MONTH));
        card.setYear(cardObj.optString(Params.CARD_YEAR));
        return card;
    }

    public static Video parsePaidVideoData(JSONObject paidVideoObj) {
        Video paidVideo = new Video();
        paidVideo.setAdminVideoId(paidVideoObj.optInt(Params.ADMIN_VIDEO_ID));
        paidVideo.setPayPerViewId(paidVideoObj.optInt(Params.PAY_PER_VIEW_ID));
        paidVideo.setTitle(paidVideoObj.optString(TITLE));
        paidVideo.setThumbNailUrl(paidVideoObj.optString(Params.PICTURE));
        paidVideo.setDescription(paidVideoObj.optString(DESCRIPTION));
        paidVideo.setPaidDate(paidVideoObj.optString(Params.PAID_DATE));
        paidVideo.setCouponAmount(paidVideoObj.optDouble(Params.COUPON_AMT));
        paidVideo.setAmount(paidVideoObj.optDouble(Params.TOTAL_AMT));
        paidVideo.setAmount(paidVideoObj.optInt(Params.AMOUNT));
        paidVideo.setCurrency(paidVideoObj.optString(Params.CURRENCY));
        paidVideo.setCouponCode(paidVideoObj.optString(Params.COUPON_CODE));
        paidVideo.setPaymentMode(paidVideoObj.optString(Params.PAYMENT_MODE));
        paidVideo.setPaymentId(paidVideoObj.optString(Params.PAYMENT_ID));
        paidVideo.setTypeOfSubscription(paidVideoObj.optString(Params.TYPE_OF_SUBSCRIPTION));
        paidVideo.setDuration(paidVideoObj.optString(Params.DURATION));
        paidVideo.setDiscountFromReferral(paidVideoObj.optString(Params.WALLET_AMOUNT));
        return paidVideo;
    }

    public static String getFileExtension(String fileUrl) {
        if(fileUrl.lastIndexOf(".") != -1 && fileUrl.lastIndexOf(".") != 0)
            return fileUrl.substring(fileUrl.lastIndexOf(".")+1);
        else return "";
    }
    public static comingSoon comingSoonDataParser(JSONObject comeData){
        comingSoon CData = new comingSoon();
        CData.setId(comeData.optInt(Params.ID));
        CData.setTitle(comeData.optString(Params.TITLE));
        CData.setEp_title(comeData.optString(Params.EP_TITLE));
        CData.setDescription(comeData.optString(Params.DESCRIPTION));
        CData.setTrailer_video(comeData.optString(Params.TRAILER_VIDEO));
        CData.setMobile_image(comeData.optString(Params.MOBILE_IMAGE));
        CData.setTrailer_subtitle(comeData.optString("trailer_subtitle_vtt"));
        CData.setTailer_rate_time(comeData.optString("tailer_rate_time"));
        CData.setMedia_type(comeData.optString("Media_type"));
        CData.setRate_time(comeData.optString(Params.RATING_TIME));
        CData.setIsNotify(comeData.optInt(Params.IS_NOTIFY));
        return CData;
    }
}
