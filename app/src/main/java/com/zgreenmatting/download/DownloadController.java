package com.zgreenmatting.download;


import com.igoda.dao.entity.MattingImage;

public enum DownloadController {

    INSTANCE;



    public void onFinishTask(Object entity) {
        DownloadManager.INSTANCE.onFinished((MattingImage)entity);

    }

    public void onFailedTask(Object entity) {
        DownloadManager.INSTANCE.onFailed((MattingImage)entity);
    }

}
