package com.zgreenmatting.download;


public enum DownloadTaskType {
    MULTI(1), SINGLE(2);

    private int value;

    private DownloadTaskType(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
