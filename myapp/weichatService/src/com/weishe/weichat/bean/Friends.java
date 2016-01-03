package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Friends")
public class Friends extends BaseEntity {
	/**
	 * 正常状态
	 */
	public final static int VISIBLE_ONLINE_NORMAL = 0;
	/**
	 * 在线隐身
	 */
	public final static int VISIBLE_ONLINE_INVISIBLE = 1;
	/**
	 * 隐身可见
	 */
	public final static int VISIBLE_INVISIBLE_VISIBLE = 2;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "userId", updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "friendId", updatable = false)
	private User friend;

	/**
	 * 备注名称
	 */
	@Column(length = 15)
	private String remarkName;

	/**
	 * 是否被屏蔽消息
	 */
	@Column
	private boolean shield;
	/**
	 * 该朋友所在的组
	 */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FriendsGroup.class)
	@JoinColumn(name = "friendsGroupId", updatable = false)
	private FriendsGroup friendsGroup;

	/**
	 * 好友对自己的可见状态,在线对其隐身，隐身可见
	 */
	@Column
	private int visible;

	/**
	 * 用户在线状态，非持久化字段
	 */
	@Transient
	private int onlineStatus;

	/**
	 * 用户在线方式，非持久化字段
	 */
	@Transient
	private int onlineType;

	public FriendsGroup getFriendsGroup() {
		return friendsGroup;
	}

	public void setFriendsGroup(FriendsGroup friendsGroup) {
		this.friendsGroup = friendsGroup;
	}

	public User getUser() {
		return user;
	}

	public User getFriend() {
		return friend;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getOnlineType() {
		return onlineType;
	}

	public void setOnlineType(int onlineType) {
		this.onlineType = onlineType;
	}
}
