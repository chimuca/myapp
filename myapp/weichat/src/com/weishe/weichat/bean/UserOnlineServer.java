package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 用户所在线的服务器
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "UserOnlineServer", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "userAuthTokenId", "chatServerId" }) })
public class UserOnlineServer extends BaseEntity {

	public final static int ONLINE_STATUS_WANNA_TALK = 1;// 求聊
	public final static int ONLINE_STATUS_ONLINE = 2;// 在线
	public final static int ONLINE_STATUS_BUSY = 3;// 忙碌
	public final static int ONLINE_STATUS_NO_DISTURB = 4;// 勿扰
	public final static int ONLINE_STATUS_LEAVE = 5;// 离开
	public final static int ONLINE_STATUS_INVISIBLE = 6;// 隐身
	public final static int ONLINE_STATUS_OFFLINE = 7;// 离线

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = UserAuthToken.class)
	@JoinColumn(name = "userAuthTokenId", updatable = false)
	private UserAuthToken userAuthToken;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ChatServer.class)
	@JoinColumn(name = "chatServerId", updatable = false)
	private ChatServer chatServer;

	/**
	 * 在线状态
	 */
	@Column
	private int onlineStatus;

	public UserAuthToken getUserAuthToken() {
		return userAuthToken;
	}

	public ChatServer getChatServer() {
		return chatServer;
	}

	public void setUserAuthToken(UserAuthToken userAuthToken) {
		this.userAuthToken = userAuthToken;
	}

	public void setChatServer(ChatServer chatServer) {
		this.chatServer = chatServer;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
}
