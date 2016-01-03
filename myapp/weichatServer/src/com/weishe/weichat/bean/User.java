package com.weishe.weichat.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "User")
public class User extends BaseEntity {

	public static int GENDER_MALE = 0;// 男性
	public static int GENDER_FEMALE = 1;// 女性
	public static int GENDER_UNKNOWN = 2;// 未知

	/**
	 * 账号
	 */
	@Column(length = 30)
	private String account;
	/**
	 * 密码
	 */
	@Column(length = 50)
	private String password;

	/**
	 * 昵称
	 */
	@Column(length = 30)
	private String name;

	/**
	 * 头像路径
	 */
	@OneToOne(fetch = FetchType.EAGER, targetEntity = Attachment.class)
	@JoinColumn(name = "avatarId", updatable = false)
	private Attachment avatar;

	/**
	 * 生日
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date birthday;

	/**
	 * 个性签名
	 */
	@Column(length = 200)
	private String signature;

	/**
	 * 个人性别，0男1女2未知
	 */
	@Column(nullable = false, columnDefinition = "INT default 0")
	private int gender;

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Attachment getAvatar() {
		return avatar;
	}

	public void setAvatar(Attachment avatar) {
		this.avatar = avatar;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

}
