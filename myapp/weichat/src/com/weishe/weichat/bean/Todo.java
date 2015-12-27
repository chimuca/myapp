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
 * 待办事项
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "Todo")
public class Todo extends BaseEntity {

	public final static int TODO_TYPE_ADD_FRIENDS = 1;// 添加好友
	public final static int TODO_TYPE_JOIN_GROUP = 2;// 别人申请加群请求
	public final static int TODO_TYPE_ADD_CHAT_GROUP = 3;// 添加群
	public final static int TODO_TYPE_ADD_DISCUSSATION = 4;// 添加讨论组
	/**
	 * 用户id
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "userId", updatable = false)
	private User user;

	/**
	 * 待办类型
	 */
	@Column
	private int type;

	/**
	 * 发起人ID
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "fromId", updatable = false)
	private User from;

	/**
	 * 如果是入群请求则该字段不能为空
	 */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ChatGroup.class)
	@JoinColumn(name = "groupId", updatable = false)
	private ChatGroup group;

	/**
	 * 发起时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createDate;

	/**
	 * 是否已完成
	 */
	@Column
	private boolean complete;

	/**
	 * 是否同意
	 */
	@Column
	private boolean agree;

	/**
	 * 请求消息
	 */
	@Column(length = 100)
	private String requestMsg;

	/**
	 * 处理意见
	 */
	@Column(length = 100)
	private String handleMsg;

	/**
	 * 处理时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date handleDate;

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getUser() {
		return user;
	}

	public int getType() {
		return type;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public boolean isComplete() {
		return complete;
	}

	public boolean isAgree() {
		return agree;
	}

	public String getRequestMsg() {
		return requestMsg;
	}

	public String getHandleMsg() {
		return handleMsg;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}

	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	public ChatGroup getGroup() {
		return group;
	}

	public void setGroup(ChatGroup group) {
		this.group = group;
	}
}
