package com.weishe.weichat.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "AppVersion")
public class AppVersion extends BaseEntity implements Serializable {

	@Column
	private int versionCode;
	@Column(length = 250)
	private String downloadUrl;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date publishTime;

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
