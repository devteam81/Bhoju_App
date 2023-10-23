package com.bhoju.app.model;

import java.io.Serializable;

/**
 * Created by codegama on 16/10/17.
 */

public class Audio implements Serializable {

    private int id;
    private String title;
    private String url;

    public Audio(){

    }

    public Audio(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
