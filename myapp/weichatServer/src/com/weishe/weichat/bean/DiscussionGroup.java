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
 * 讨论组
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "DiscussionGroup")
public class DiscussionGroup extends BaseEntity {
	/**
	 * 讨论组名称
	 */
	@Column(length = 20)
	private String name;

	/**
	 * 创建时间
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	/**
	 * 创建者
	 */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "createById", updatable = false)
	private User createBy;

	public String getName() {
		return name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public User getCreateBy() {
		return createBy;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

}
