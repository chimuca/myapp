package com.messake.messake.bean;

import java.io.Serializable;

/**
 * APP版本信息
 * Created by messake on 2015/12/26.
 */
public class AppVersion implements Serializable {

    private int versionCode;
    private String downloadUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
