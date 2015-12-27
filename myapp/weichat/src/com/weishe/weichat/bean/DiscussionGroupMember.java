package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DiscussionGroupMember")
public class DiscussionGroupMember extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "userId", updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ChatGroup.class)
	@JoinColumn(name = "discussionGroupId", updatable = false)
	private DiscussionGroup discussionGroup;
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

	public DiscussionGroup getDiscussionGroup() {
		return discussionGroup;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setDiscussionGroup(DiscussionGroup discussionGroup) {
		this.discussionGroup = discussionGroup;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

}
