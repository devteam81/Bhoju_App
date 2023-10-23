package com.bhoju.app.model;

import java.io.Serializable;
import java.util.List;

public class comingSoon implements Serializable {
    private int comeVideoTileImg;
    private String comeVideoTitle;
    private String ComingSoonInfo;
    private String comingDesc;
    private List<String> trailerVideoLanguageUrl;

    private int id;
    private int parent_id;
    private int a_record;
    private String unique_id;
    private String title;
    private String ep_title;
    private int is_main;
    private String director;
    private String description;
    private int download_status;
    private int age;
    private int details;
    private int app_version;
    private int display_type;
    private String countryName;
    private String blockCountry;
    private int category_id;
    private int sub_category_id;
    private int sub_category_ids;
    private int is_kids_video;
    private int genre_id;
    private int season_id;
    private int is_episode;
    private int episode_number;
    private String genres_main;
    private String video;
    private String video_subtitle;
    private int akmai_video;
    private String akmai_video_subtitle;
    private String trailer_video;
    private int akmai_trailer_video;
    private String akmai_trailer_subtitle;
    private int is_trailer_free;
    private String trailer_subtitle;
    private String default_image;
    private String mobile_image;
    private String browse_image;
    private String video_gif_image;
    private String video_image_mobile;
    private String banner_image;
    private String square_image;
    private String mobile_banner_image;
    private String ratings;
    private String reviews;
    private int status;
    private int is_original_video;
    private int is_approved;
    private int is_home_slider;
    private int is_banner;
    private String uploaded_by;
    private String publish_time;
    private String duration;
    private String trailer_duration;
    private String video_resolutions;
    private String trailer_video_resolutions;
    private int compress_status;
    private int main_video_compress_status;
    private int trailer_compress_status;
    private int video_resize_path;
    private String trailer_resize_path;
    private String edited_by;
    private int ppv_created_by;
    private int watch_count;
    private int is_pay_per_view;
    private int type_of_user;
    private int type_of_subscription;
    private int amount;
    private int discounted_amount;
    private int redeem_amount;
    private int admin_amount;
    private int user_amount;
    private String created_at;
    private String updated_at;
    private int video_type;
    private int video_upload_type;
    private int position;
    private int player_json;
    private int hls_main_video;
    private String video_subtitle_vtt;
    private String down_high;
    private String down_med;
    private String down_low;
    private String akmai_down_high;
    private String akmai_down_med;
    private String akmai_down_low;
    private int is_single;
    private String short_link;
    private String long_link;
    private String tailer_rate_time;
    private String rate_time;
    private String media_type;
    private String rate_url;
    private int server_enable;
    private int invoice_id;
    private int upcoming;
    private int isNotify;

    public int getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(int isNotify) {
        this.isNotify = isNotify;
    }

    public comingSoon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getA_record() {
        return a_record;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEp_title() {
        return ep_title;
    }

    public void setEp_title(String ep_title) {
        this.ep_title = ep_title;
    }

    public int getIs_main() {
        return is_main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrailer_video() {
        return trailer_video;
    }

    public void setTrailer_video(String trailer_video) {
        this.trailer_video = trailer_video;
    }

    public void setTrailer_subtitle(String trailer_subtitle) {
        this.trailer_subtitle = trailer_subtitle;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public String getMobile_image() {
        return mobile_image;
    }

    public void setMobile_image(String mobile_image) {
        this.mobile_image = mobile_image;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public void setTailer_rate_time(String tailer_rate_time) {
        this.tailer_rate_time = tailer_rate_time;
    }

    public void setRate_time(String rate_time) {
        this.rate_time = rate_time;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }


    public comingSoon(int comeVideoTileImg, String comeVideoTitle, String ComingSoonInfo, String comingDesc) {
        this.comeVideoTileImg = comeVideoTileImg;
        this.comeVideoTitle = comeVideoTitle;
        this.ComingSoonInfo = ComingSoonInfo;
        this.comingDesc = comingDesc;
    }

    public int getComeVideoTileImg() {
        return comeVideoTileImg;
    }

    public void setComeVideoTileImg(int comeVideoTileImg) {
        this.comeVideoTileImg = comeVideoTileImg;
    }

    public String getComeVideoTitle() {
        return comeVideoTitle;
    }

    public void setComeVideoTitle(String comeVideoTitle) {
        this.comeVideoTitle = comeVideoTitle;
    }

    public String getComingSoonInfo() {
        return ComingSoonInfo;
    }

    public void setComingSoonInfo(String comingSoonInfo) {
        ComingSoonInfo = comingSoonInfo;
    }

    public String getComingDesc() {
        return comingDesc;
    }

    public void setComingDesc(String comingDesc) {
        this.comingDesc = comingDesc;
    }
}

