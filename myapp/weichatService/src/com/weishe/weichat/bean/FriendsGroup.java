package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 好友分组
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "FriendsGroup")
public class FriendsGroup extends BaseEntity {

	/**
	 * 谁的好友组
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "userId", updatable = false)
	private User user;

	/**
	 * 分组名称
	 */
	@Column(length = 20)
	private String name;

	public User getUser() {
		return user;
	}

	/**
	 * 排列顺序
	 */
	private int position;

	public String getName() {
		return name;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
