package com.zgreenmatting.update.utils;

import com.zgreenmatting.update.pojo.UpdateInfo;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Shelwee on 14-5-8.
 */
public class JSONHandler {

    public static UpdateInfo toUpdateInfo(InputStream is) throws Exception {
        if (is == null) {
            return null;
        }
        String byteData = new String(readStream(is));
        is.close();
        JSONObject jsonObject = new JSONObject(byteData);
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setApkUrl(jsonObject.getString("apkUrl"));
        updateInfo.setAppName(jsonObject.getString("appName"));
        updateInfo.setVersionCode(jsonObject.getString("versionCode"));
        updateInfo.setVersionName(jsonObject.getString("versionName"));
        updateInfo.setChangeLog(jsonObject.getString("changeLog"));
        updateInfo.setUpdateTips(jsonObject.getString("updateTips"));
        updateInfo.setForceUpgrade(jsonObject.getBoolean("forceUpgrade"));
        return updateInfo;
    }

    private static byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] array = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(array)) != -1) {
            outputStream.write(array, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

}
