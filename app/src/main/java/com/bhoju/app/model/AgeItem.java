package com.bhoju.app.model;

import android.view.View;

public class AgeItem {

    private String ageName;
    private boolean selected=false;
    private View.OnClickListener clickListener;

    public AgeItem(String ageName, View.OnClickListener click) {
        this.ageName = ageName;
        this.clickListener = click;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
