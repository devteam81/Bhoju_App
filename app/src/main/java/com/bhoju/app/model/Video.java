package com.bhoju.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Video  implements Serializable {

    private int adminVideoId;
    private int seasonId;
    private String seasonName;
    private int parentId;
    private int categoryId;
    private int genreId;
    private int subCategoryId;
    private int ratings;
    private int a_record;
    private int age;
    private int numDaysToExpire;
    private int seekHere;
    private int payPerViewId;
    private long likes;
    private long viewCount;
    private double amount;
    private double couponAmount;
    private double totalAmount;
    private boolean isInWishList;
    private boolean isInHistory;
    private boolean isLiked;
    private boolean isInSpam;
    private boolean isPayPerView;
    private boolean isSeasonVideo;
    private boolean isKidsVideo;
    private boolean isExpired;
    private boolean isTrailerVideo;
    private boolean isInWeb;
    private boolean isDownloadable;
    private boolean isUserSubscribed;
    private String adminUniqueId;
    private String thumbNailUrl;
    private String videoUrl;
    private String shareUrl;
    private String shareMessage;
    private String subTitleUrl;
    private String subTitleLang;
    private String title;
    private String epTitle;
    private String subCategoryName;
    private String detail;
    private String currency;
    private String description;
    private String defaultImage;
    private String browseImage;
    private String publishTime;
    private String duration;
    private String trailerDuration;
    private String paidDate;
    private String couponCode;
    private String paymentMode;
    private String paymentId;
    private String typeOfSubscription;
    private String discountFromReferral;
    private String userType;

    private String director;
    private String genresList;
    private String slider_type;
    private String parent_media;
    private int isSingle;
    private int isMain;
    private int isTrailerFree;
    private String trailerVideo;
    private String trailerDefaultImage;
    private String trailerBrowseImage;
    private String trailerMobileImage;
    private String trailerSubtitle;
    private String trailerSubtitleLang;

    private String ratingTime;
    private String ratingType;
    private String ratingUrl;
    private String ratingTrailerTime;
    private String ratingTrailerType;
    private String ratingTrailerUrl;

    private int downloadId;
    private String downloadUrl;
    private String downloadSavePath;
    private String downloadFileName;
    private String downloadedPercentage;

    private List<String> videoLanguageUrl;
    private List<String> trailerVideoLanguageUrl;
    private List<Audio> audioLanguageUrl;
    private List<Audio> trailerAudioLanguageUrl;

    private List<Cast> casts;
    private List<Cast> choosePlans;
    private List<GenreSeason> genres;
    private List<GenreSeason> genreSeasons;
    private List<GenreSeason> seasons;
    private List<GenreSeason> seasonEpisodes;
    private List<Video> trailerVideos;
    private List<DownloadUrl> downloadResolutions;
    private VideoType videoType;
    private DownloadStatus downloadStatus;
    private DownloadStatus downloadedStatus;
    private HashMap<String, String> resolutions;
    private PayPerViewType payPerViewType;
    private SubscriptionType subscriptionType;

    public Video() {

    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }

    public String getDiscountFromReferral() {
        return discountFromReferral;
    }

    public void setDiscountFromReferral(String discountFromReferral) {
        this.discountFromReferral = discountFromReferral;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isInSpam() {
        return isInSpam;
    }

    public void setInSpam(boolean inSpam) {
        isInSpam = inSpam;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public PayPerViewType getPayPerViewType() {
        return payPerViewType;
    }

    public void setPayPerViewType(PayPerViewType payPerViewType) {
        this.payPerViewType = payPerViewType;
    }

    public String getTypeOfSubscription() {
        return typeOfSubscription;
    }

    public void setTypeOfSubscription(String typeOfSubscription) {
        this.typeOfSubscription = typeOfSubscription;
    }

    public int getPayPerViewId() {
        return payPerViewId;
    }

    public void setPayPerViewId(int payPerViewId) {
        this.payPerViewId = payPerViewId;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isUserSubscribed() {
        return isUserSubscribed;
    }

    public void setUserSubscribed(boolean userSubscribed) {
        isUserSubscribed = userSubscribed;
    }

    public int getSeekHere() {
        return seekHere;
    }

    public void setSeekHere(int seekHere) {
        this.seekHere = seekHere;
    }

    public List<String> getVideoLanguageUrl() {
        return videoLanguageUrl;
    }
    public void setVideoLanguageUrl(List<String> videoLanguageUrl) {
        this.videoLanguageUrl = videoLanguageUrl;
    }
    public List<String> getTrailerVideoLanguageUrl() {
        return trailerVideoLanguageUrl;
    }
    public void setTrailerVideoLanguageUrl(List<String> trailerVideoLanguageUrl) {
        this.trailerVideoLanguageUrl = trailerVideoLanguageUrl;
    }
    public List<Audio> getAudioLanguageUrl() {
        return audioLanguageUrl;
    }
    public void setAudioLanguageUrl(List<Audio> audioLanguageUrl) {
        this.audioLanguageUrl = audioLanguageUrl;
    }
    public List<Audio> getTrailerAudioLanguageUrl() {
        return trailerAudioLanguageUrl;
    }
    public void setTrailerAudioLanguageUrl(List<Audio> trailerAudioLanguageUrl) {
        this.trailerAudioLanguageUrl = trailerAudioLanguageUrl;
    }


    public ArrayList<Cast> getChoosePlans() {
        return (ArrayList<Cast>) choosePlans;
    }

    public void setChoosePlans(ArrayList<Cast> choosePlans) {
        this.choosePlans = choosePlans;
    }

    public List<GenreSeason> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<GenreSeason> genres) {
        this.genres = genres;
    }


    public boolean isInWeb() {
        return isInWeb;
    }

    public void setInWeb(boolean inWeb) {
        isInWeb = inWeb;
    }

    public int getNumDaysToExpire() {
        return numDaysToExpire;
    }

    public void setNumDaysToExpire(int numDaysToExpire) {
        if(numDaysToExpire==0)
            this.numDaysToExpire = 3;
        else
            this.numDaysToExpire = numDaysToExpire;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public ArrayList<DownloadUrl> getDownloadResolutions() {
        return (ArrayList<DownloadUrl>) downloadResolutions;
    }

    public void setDownloadResolutions(ArrayList<DownloadUrl> downloadResolutions) {
        this.downloadResolutions = downloadResolutions;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        isDownloadable = downloadable;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    public boolean isTrailerVideo() {
        return isTrailerVideo;
    }

    public void setTrailerVideo(boolean trailerVideo) {
        isTrailerVideo = trailerVideo;
    }

    public HashMap<String, String> getResolutions() {
        return resolutions;
    }

    public void setResolutions(HashMap<String, String> resolutions) {
        this.resolutions = resolutions;
    }

    public boolean isSeasonVideo() {
        return isSeasonVideo;
    }

    public void setSeasonVideo(boolean seasonVideo) {
        isSeasonVideo = seasonVideo;
    }

    public ArrayList<Video> getTrailerVideos() {
        return (ArrayList<Video>) trailerVideos;
    }

    public void setTrailerVideos(ArrayList<Video> trailerVideos) {
        this.trailerVideos = trailerVideos;
    }

    public ArrayList<GenreSeason> getGenreSeasons() {
        return (ArrayList<GenreSeason>) genreSeasons;
    }

    public void setGenreSeasons(ArrayList<GenreSeason> genreSeasons) {
        this.genreSeasons = genreSeasons;
    }

    public List<GenreSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<GenreSeason> seasons) {
        this.seasons = seasons;
    }

    public List<GenreSeason> getSeasonEpisodes() {
        return seasonEpisodes;
    }

    public void setSeasonEpisodes(List<GenreSeason> seasonEpisodes) {
        this.seasonEpisodes = seasonEpisodes;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isInWishList() {
        return isInWishList;
    }

    public void setInWishList(boolean inWishList) {
        isInWishList = inWishList;
    }

    public boolean isInHistory() {
        return isInHistory;
    }

    public void setInHistory(boolean inHistory) {
        isInHistory = inHistory;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTrailerDuration() {
        return trailerDuration;
    }

    public void setTrailerDuration(String trailerDuration) {
        this.trailerDuration = trailerDuration;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isPayPerView() {
        return isPayPerView;
    }

    public void setPayPerView(boolean payPerView) {
        isPayPerView = payPerView;
    }

    public boolean isKidsVideo() {
        return isKidsVideo;
    }

    public void setKidsVideo(boolean kidsVideo) {
        isKidsVideo = kidsVideo;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public DownloadStatus getDownloadedStatus() {
        return downloadedStatus;
    }

    public void setDownloadedStatus(DownloadStatus downloadedStatus) {
        this.downloadedStatus = downloadedStatus;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSubTitleUrl() {
        return subTitleUrl;
    }

    public void setSubTitleUrl(String subTitleUrl) {
        this.subTitleUrl = subTitleUrl;
    }

    public String getSubTitleLang() {
        return subTitleLang;
    }

    public void setSubTitleLang(String subTitleLang) {
        this.subTitleLang = subTitleLang;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public String getAdminUniqueId() {
        return adminUniqueId;
    }

    public void setAdminUniqueId(String adminUniqueId) {
        this.adminUniqueId = adminUniqueId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAdminVideoId() {
        return adminVideoId;
    }

    public void setAdminVideoId(int adminVideoId) {
        this.adminVideoId = adminVideoId;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEpTitle() {
        return epTitle;
    }

    public void setEpTitle(String epTitle) {
        this.epTitle = epTitle;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getA_record() {
        return a_record;
    }

    public void setA_record(int a_record) {
        this.a_record = a_record;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getBrowseImage() {
        return browseImage;
    }

    public void setBrowseImage(String browseImage) {
        this.browseImage = browseImage;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenresList() {
        return genresList;
    }

    public void setGenresList(String genresList) {
        this.genresList = genresList;
    }

    public String getSlider_type() {
        return slider_type;
    }

    public void setSlider_type(String slider_type) {
        this.slider_type = slider_type;
    }

    public String getParent_media() {
        return parent_media;
    }

    public void setParent_media(String parent_media) {
        this.parent_media = parent_media;
    }

    public int getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(int isSingle) {
        this.isSingle = isSingle;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public int getIsTrailerFree() {
        return isTrailerFree;
    }

    public void setIsTrailerFree(int isTrailerFree) {
        this.isTrailerFree = isTrailerFree;
    }

    public String getTrailerVideo() {
        return trailerVideo;
    }

    public void setTrailerVideo(String trailerVideo) {
        this.trailerVideo = trailerVideo;
    }

    public String getTrailerDefaultImage() {
        return trailerDefaultImage;
    }

    public void setTrailerDefaultImage(String trailerDefaultImage) {
        this.trailerDefaultImage = trailerDefaultImage;
    }

    public String getTrailerBrowseImage() {
        return trailerBrowseImage;
    }

    public void setTrailerBrowseImage(String trailerBrowseImage) {
        this.trailerBrowseImage = trailerBrowseImage;
    }

    public String getTrailerMobileImage() {
        return trailerMobileImage;
    }

    public void setTrailerMobileImage(String trailerMobileImage) {
        this.trailerMobileImage = trailerMobileImage;
    }

    public String getTrailerSubtitle() {
        return trailerSubtitle;
    }

    public void setTrailerSubtitle(String trailerSubtitle) {
        this.trailerSubtitle = trailerSubtitle;
    }

    public String getTrailerSubtitleLang() {
        return trailerSubtitleLang;
    }

    public void setTrailerSubtitleLang(String trailerSubtitleLang) {
        this.trailerSubtitleLang = trailerSubtitleLang;
    }

    public String getRatingTime() {
        return ratingTime;
    }

    public void setRatingTime(String ratingTime) {
        this.ratingTime = ratingTime;
    }

    public String getRatingType() {
        return ratingType;
    }

    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }

    public String getRatingUrl() {
        return ratingUrl;
    }

    public void setRatingUrl(String ratingUrl) {
        this.ratingUrl = ratingUrl;
    }

    public String getRatingTrailerTime() {
        return ratingTrailerTime;
    }

    public void setRatingTrailerTime(String ratingTrailerTime) {
        this.ratingTrailerTime = ratingTrailerTime;
    }

    public String getRatingTrailerType() {
        return ratingTrailerType;
    }

    public void setRatingTrailerType(String ratingTrailerType) {
        this.ratingTrailerType = ratingTrailerType;
    }

    public String getRatingTrailerUrl() {
        return ratingTrailerUrl;
    }

    public void setRatingTrailerUrl(String ratingTrailerUrl) {
        this.ratingTrailerUrl = ratingTrailerUrl;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadSavePath() {
        return downloadSavePath;
    }

    public void setDownloadSavePath(String downloadSavePath) {
        this.downloadSavePath = downloadSavePath;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadedPercentage() {
        return downloadedPercentage;
    }

    public void setDownloadedPercentage(String downloadedPercentage) {
        this.downloadedPercentage = downloadedPercentage;
    }

//    @Override
//    public String toString() {
//        return "Video{" +
//                "adminVideoId=" + adminVideoId +
//                ", adminUniqueId='" + adminUniqueId + '\'' +
//                ", thumbNailUrl='" + thumbNailUrl + '\'' +
//                ", seasonId=" + seasonId +
//                ", videoType=" + videoType +
//                ", videoUrl='" + videoUrl + '\'' +
//                ", shareUrl='" + shareUrl + '\'' +
//                ", subTitleUrl='" + subTitleUrl + '\'' +
//                ", title='" + title + '\'' +
//                ", categoryId=" + categoryId +
//                ", genreId=" + genreId +
//                ", subCategoryId=" + subCategoryId +
//                ", subCategoryName='" + subCategoryName + '\'' +
//                ", ratings=" + ratings +
//                ", detail='" + detail + '\'' +
//                ", isInWishList=" + isInWishList +
//                ", isInHistory=" + isInHistory +
//                ", isLiked=" + isLiked +
//                ", likes=" + likes +
//                ", currency='" + currency + '\'' +
//                ", casts=" + casts +
//                ", choosePlans=" + choosePlans +
//                ", genreSeasons=" + genreSeasons +
//                ", trailerVideos=" + trailerVideos +
//                ", description='" + description + '\'' +
//                ", defaultImage='" + defaultImage + '\'' +
//                ", publishTime='" + publishTime + '\'' +
//                ", age=" + age +
//                ", duration='" + duration + '\'' +
//                ", trailerDuration='" + trailerDuration + '\'' +
//                ", viewCount=" + viewCount +
//                ", isPayPerView=" + isPayPerView +
//                ", isSeasonVideo=" + isSeasonVideo +
//                ", numDaysToExpire=" + numDaysToExpire +
//                ", amount=" + amount +
//                ", isKidsVideo=" + isKidsVideo +
//                ", isExpired=" + isExpired +
//                ", isTrailerVideo=" + isTrailerVideo +
//                ", isInWeb=" + isInWeb +
//                ", downloadStatus=" + downloadStatus +
//                ", resolutions=" + resolutions +
//                ", downloadResolutions=" + downloadResolutions +
//                ", isDownloadable=" + isDownloadable +
//                ", seekHere=" + seekHere +
//                ", isUserSubscribed=" + isUserSubscribed +
//                ", payPerViewId=" + payPerViewId +
//                ", paidDate='" + paidDate + '\'' +
//                ", couponAmount=" + couponAmount +
//                ", totalAmount=" + totalAmount +
//                ", couponCode='" + couponCode + '\'' +
//                ", paymentMode='" + paymentMode + '\'' +
//                ", paymentId='" + paymentId + '\'' +
//                ", typeOfSubscription='" + typeOfSubscription + '\'' +
//                '}';
//    }

    public enum VideoType {
        VIDEO_YOUTUBE, VIDEO_MANUAL, VIDEO_OTHER
    }


    public enum DownloadStatus {
        SHOW_DOWNLOAD, DOWNLOAD_PROGRESS, DOWNLOAD_PAUSED, DOWNLOAD_COMPLETED, NEED_TO_SUBSCRIBE, NEED_TO_PAY, DO_NOT_SHOW_DOWNLOAD
    }


    public enum PayPerViewType {
        ONE, TWO, THREE
    }

    public enum SubscriptionType {
        ONE, TWO, THREE
    }
}
