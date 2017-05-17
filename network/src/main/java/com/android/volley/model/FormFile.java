package com.android.volley.model;

import java.io.*;

/**
 * Title <br>
 * Description.
 * <p/>
 * Copyright: Copyright (c) 2015/9/9 11:35
 * <p/>
 * Company: 蓝莓盈动
 * <p/>
 * Author: shixq
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class FormFile implements Serializable {
    /* 上传文件的数据 当文件较小时使用*/
    private byte[] data;
    /* 文件较大时，dada传null，使用InputStream */
    private InputStream inStream;

    private File file;

    private long fileSize;

    /* 文件名称 */
    private String filname;

    /* 请求参数名称 */
    private String parameterName;

    /* 内容类型 */
    private String contentType = "application/octet-stream";

    public FormFile(String filname, byte[] data, long fileSize, String parameterName, String contentType) {
        this.data = data;
        this.filname = filname;
        this.parameterName = parameterName;
        this.fileSize = fileSize;
        if (contentType != null) {
            this.contentType = contentType;
        }
    }

    public FormFile(String filname, File file, String parameterName, String contentType) {
        this.filname = filname;
        this.parameterName = parameterName;
        this.file = file;
        this.fileSize = file.length();
        try {
            this.inStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (contentType != null) {
            this.contentType = contentType;
        }
    }

    public FormFile(InputStream inStream, int fileSize, String filname, String parameterName, String contentType) {
        super();
        this.inStream = inStream;
        this.fileSize = fileSize;
        this.filname = filname;
        this.parameterName = parameterName;
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public File getFile() {
        return file;
    }

    public InputStream getInStream() {
        return inStream;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilname() {
        return filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
