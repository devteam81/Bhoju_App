package com.bhoju.app.util.download;

public class NavigateToScreenEvent {

    private int screenId;

    public NavigateToScreenEvent(int screenId) {
        this.screenId = screenId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

}
