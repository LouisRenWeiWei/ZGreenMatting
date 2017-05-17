package com.android.volley.toolbox;

import com.android.volley.*;
import com.android.volley.listener.Listener;
import com.android.volley.model.FormFile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传的request <br>
 * Description.
 * <p/>
 * Copyright: Copyright (c) 2015/9/6 17:21
 * <p/>
 * Company: 蓝莓
 * <p/>
 * Author: shixq
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class MultipartRequest extends Request<String> {
    private final Listener<String> mListener;
    private Map<String, String> headerMap;
    private Map<String, String> mParams;
    private FormFile[] files;
    private String BOUNDARY = "---------7dc05dba8f3e19";

    public MultipartRequest(String url, Listener<String> listener, Map<String, String> params, FormFile[] files) {
        this(Method.POST, url, listener, params, files);
    }

    public MultipartRequest(int method, String url, Listener<String> listener, Map<String, String> params, FormFile[] files) {
        super(method, url, listener);
        mListener = listener;
        mParams = params;
        this.files = files;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        headerMap = new HashMap<String, String>();
        headerMap.put("Charset", "UTF-8");
        //Keep-Alive
        headerMap.put("Connection", "Keep-Alive");
        headerMap.put("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        return headerMap;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        //传参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            // 构建表单字段内容
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            sb.append(entry.getValue());
            sb.append("\r\n");
        }
        return sb.toString().getBytes();
    }

    @Override
    public void handRequest(OutputStream out) {
        DataOutputStream dos = (DataOutputStream) out;
        try {
            //发送文件数据
            if (files != null) {
                for (FormFile file : files) {
                    // 发送文件数据
                    StringBuilder split = new StringBuilder();
                    split.append("--");
                    split.append(BOUNDARY);
                    split.append("\r\n");
                    split.append("Content-Disposition: form-data;name=\"" + file.getParameterName() + "\";filename=\"" + file.getFilname() + "\"\r\n");
                    split.append("Content-Type: " + file.getContentType() + "\r\n\r\n");
                    dos.write(split.toString().getBytes());
                    if (file.getInStream() != null) {
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        int count = 0;
                        while ((len = file.getInStream().read(buffer)) != -1) {
                            dos.write(buffer, 0, len);
                            count += len;
                            if (mListener != null) {
                                mListener.onProgressChange(file.getFileSize(), count);
                            }
                        }
                        file.getInStream().close();
                    } else {
                        dos.write(file.getData(), 0, file.getData().length);
                    }
                    dos.write("\r\n".getBytes());
                }
            }
            dos.writeBytes("--" + BOUNDARY + "--\r\n");
            dos.flush();
        } catch (IOException e) {
            mListener.onError(new VolleyError(e.toString()));
            try {
                dos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onSuccess(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mListener.onError(error);
    }
}
