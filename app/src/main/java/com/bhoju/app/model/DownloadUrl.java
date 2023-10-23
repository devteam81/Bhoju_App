package com.bhoju.app.model;

public class DownloadUrl {
    private String title;
    private String url;
    private String type;
    private String language;

    public DownloadUrl(String title, String url, String type, String language) {
        this.title = title;
        this.url = url;
        this.type = type;
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
