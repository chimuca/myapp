package org.weishe.weichat.bean;

import java.io.Serializable;

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
