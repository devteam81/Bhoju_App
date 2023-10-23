package com.bhoju.app.util.download;

public interface DownloadCompleteListener {
    void downloadCompleted(int adminVideoId);

    void downloadCancelled(int adminVideoId);

    void downloadPaused(int adminVideoId);
}
