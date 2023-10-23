package com.bhoju.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoSection implements  Serializable {

    private int  id;
    private String title;
    private String seeAllUrl;
    private String urlType;
    private String urlPageId;
    private int viewType;
    private List<Video> videos;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getUrlPageId() {
        return urlPageId;
    }

    public void setUrlPageId(String urlPageId) {
        this.urlPageId = urlPageId;
    }

    public VideoSection(String title, String seeAllUrl, String urlType, String urlPageId, int viewType, ArrayList<Video> videos) {
        this.title = title;
        this.seeAllUrl = seeAllUrl;
        this.viewType = viewType;
        this.urlType = urlType;
        this.urlPageId = urlPageId;
        this.videos = videos;
    }

    public VideoSection(){
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void setTitle(String title) {
        this.title = title;
    }
//
    public String getSeeAllUrl() {
        return seeAllUrl;
    }

    public void setSeeAllUrl(String seeAllUrl) {
        this.seeAllUrl = seeAllUrl;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Video> getVideos() {
        return (ArrayList<Video>) videos;
    }
}

