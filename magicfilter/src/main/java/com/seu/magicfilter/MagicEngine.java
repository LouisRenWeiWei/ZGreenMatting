package com.seu.magicfilter;

import com.seu.magicfilter.camera.CameraEngine;
import com.seu.magicfilter.helper.SavePictureTask;
import com.seu.magicfilter.utils.MagicParams;
import com.seu.magicfilter.widget.base.MagicBaseView;

import java.io.File;

/**
 * Created by why8222 on 2016/2/25.
 */
public class MagicEngine {
    private static MagicEngine magicEngine;

    public static MagicEngine getInstance(){
        if(magicEngine == null)
            throw new NullPointerException("MagicEngine must be built first");
        else
            return magicEngine;
    }

    private MagicEngine(Builder builder){

    }

    public void setFilter(boolean needFilter){
        MagicParams.magicBaseView.setFilter(needFilter);
    }

    public void savePicture(File file, SavePictureTask.OnPictureSaveListener listener){
        SavePictureTask savePictureTask = new SavePictureTask(file, listener);
        MagicParams.magicBaseView.savePicture(savePictureTask);
    }

    public void setFilterListener(MagicBaseView.OnFilterChangedListener onFilterChangedListener){
        MagicParams.magicBaseView.setOnFilterChangedListener(onFilterChangedListener);
    }


    public void switchCamera(){
        CameraEngine.switchCamera();
    }

    public static class Builder{

        public MagicEngine build(MagicBaseView magicBaseView) {
            MagicParams.context = magicBaseView.getContext();
            MagicParams.magicBaseView = magicBaseView;
            return new MagicEngine(this);
        }

        public Builder setVideoPath(String path){
            MagicParams.videoPath = path;
            return this;
        }

        public Builder setVideoName(String name){
            MagicParams.videoName = name;
            return this;
        }

    }
}
