package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ChatGroupMember")
public class ChatGroupMember extends BaseEntity {
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "userId", updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ChatGroup.class)
	@JoinColumn(name = "chatGroupId", updatable = false)
	private ChatGroup chatGroup;
	/**
	 * 备注名称
	 */
	@Column(length = 15)
	private String remarkName;

	/**
	 * 是否屏蔽消息
	 */
	@Column
	private boolean shield;

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public User getUser() {
		return user;
	}

	public ChatGroup getChatGroup() {
		return chatGroup;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setChatGroup(ChatGroup chatGroup) {
		this.chatGroup = chatGroup;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

}
