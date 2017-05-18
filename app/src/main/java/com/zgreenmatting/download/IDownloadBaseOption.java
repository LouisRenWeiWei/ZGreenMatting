package com.zgreenmatting.download;

/**
 * BaseTask
 * 策略基类,提供操作
 */
public interface IDownloadBaseOption {

    void onPrepareOption();

    void onStartOption();

    void onPauseOption();

    void onStopOption();

    void onCancelOption();

}
