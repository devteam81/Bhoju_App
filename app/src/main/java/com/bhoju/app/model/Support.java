package com.bhoju.app.model;

import java.io.Serializable;

/**
 * Created by codegama on 16/10/17.
 */

public class Support implements Serializable {

    private int id;
    private String name;
    private String image;
    private String status;
    private boolean checked;

    public Support(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    @Override
    public String toString() {
        return "Support{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                '}';
    }
}
