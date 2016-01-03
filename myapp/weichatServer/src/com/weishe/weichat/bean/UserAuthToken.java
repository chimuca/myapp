package com.weishe.weichat.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用户认证码
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "UserAuthToken")
public class UserAuthToken extends BaseEntity {
	/**
	 * token所属用户
	 */
	/**
	 * 创建人Id
	 */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "userId", updatable = false)
	private User user;
	/**
	 * 认证token
	 */
	@Column(length = 100)
	private String token;
	/**
	 * 还是否有效
	 */
	@Column
	private boolean enable;
	/**
	 * 使用该token的客户端类型
	 */
	@Column(length = 250)
	private String clientType;
	/**
	 * 创建日期
	 */
	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createDate;
	/**
	 * 用户客户端唯一标示
	 */
	@Column(length = 50, nullable = false)
	private String clientId;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public User getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isEnable() {
		return enable;
	}

	public String getClientType() {
		return clientType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
