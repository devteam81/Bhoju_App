package com.bhoju.app.model;

import java.io.Serializable;

/**
 * Created by codegama on 16/10/17.
 */

public class GenreSeason implements Serializable {

    private int id;
    private String name;
    private boolean isSelected;

    public GenreSeason(){

    }

    public GenreSeason(int id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
