package com.zgreenmatting.entity;


public enum DownloadStatus {

    NONE(0),
    WAIT(0x21),  //等待  33
    DLING(0x22), //下载中 34
    PAUSE(0x23), //暂停  35
    PAUSEING(0x24), //暂停中
    DONE(0x25),  //完成
    ERROR(0x26);  //失败

    private int value;
    private DownloadStatus(int val) {
        this.value = val;
    }
    public int getValue() {
        return value;
    }
}
