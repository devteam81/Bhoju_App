package com.bhoju.app.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class OfflineVideo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "adminVideoId")
    int adminVideoId;
    @ColumnInfo(name = "thumbnailUrl")
    String thumbNailUrl;
    @ColumnInfo(name = "title")
    String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
