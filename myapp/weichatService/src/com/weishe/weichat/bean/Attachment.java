package com.weishe.weichat.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 文件附件
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "Attachment")
public class Attachment extends BaseEntity {
	public static final int TYPE_UNKNOWN = 0;// 未知文件类型
	public static final int TYPE_JPG = 1;
	public static final int TYPE_PNG = 2;
	public static final int TYPE_GIF = 3;
	public static final int TYPE_DOC = 4;
	public static final int TYPE_DOCX = 5;
	public static final int TYPE_PPT = 6;
	public static final int TYPE_PPTX = 7;
	public static final int TYPE_XLS = 8;
	public static final int TYPE_XLSX = 9;
	public static final int TYPE_PDF = 10;
	public static final int TYPE_TXT = 11;
	public static final int TYPE_RAR = 12;
	public static final int TYPE_ZIP = 13;

	public static final int TYPE_VOICE = 101;// 语音
	public static final int TYPE_VIDEO = 102;// 视频
	/**
	 * 文件名称
	 */
	@Column(length = 80)
	private String name;

	/**
	 * 对应在FastDFS上的group
	 */
	@Column(length = 10)
	private String groupName;

	/**
	 * 文件位置，所有的文件都存在网络上
	 */
	@Column(length = 300)
	private String path;
	/**
	 * 文件类型
	 */
	@Column
	private int type;
	/**
	 * 用户id，非持久化字段
	 */
	@Transient
	private int userId;
	/**
	 * 用户token
	 */
	@Transient
	private String token;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * 文件大小
	 */
	@Column
	private long size;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getPath() {
		return path;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createDate;

	public int getUserId() {
		return userId;
	}

	public String getToken() {
		return token;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
