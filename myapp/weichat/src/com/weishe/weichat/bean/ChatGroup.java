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
 * 聊天群
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "ChatGroup")
public class ChatGroup extends BaseEntity {
	/**
	 * 群号,由数组组成，唯一id
	 */
	@Column(length = 10)
	private String account;
	/**
	 * 群名称
	 */

	@Column(length = 20)
	private String name;

	/**
	 * 群口号
	 */
	@Column(length = 300)
	private String slogan;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createDate;

	/**
	 * 群主
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "createById", updatable = false)
	private User createBy;

	/**
	 * 管理员1
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "manager1Id", updatable = false)
	private User manager1;
	/**
	 * 管理员2
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "manager2Id", updatable = false)
	private User manager2;
	/**
	 * 群组所属大类
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = GroupClass.class)
	@JoinColumn(name = "bigClassId", updatable = false)
	private GroupClass bigClass;

	/**
	 * 群租所属小类
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = GroupClass.class)
	@JoinColumn(name = "smallClassId", updatable = false)
	private GroupClass smallClass;

	public String getAccount() {
		return account;
	}

	public String getName() {
		return name;
	}

	public String getSlogan() {
		return slogan;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public User getCreateBy() {
		return createBy;
	}

	public User getManager1() {
		return manager1;
	}

	public User getManager2() {
		return manager2;
	}

	public GroupClass getBigClass() {
		return bigClass;
	}

	public GroupClass getSmallClass() {
		return smallClass;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public void setManager1(User manager1) {
		this.manager1 = manager1;
	}

	public void setManager2(User manager2) {
		this.manager2 = manager2;
	}

	public void setBigClass(GroupClass bigClass) {
		this.bigClass = bigClass;
	}

	public void setSmallClass(GroupClass smallClass) {
		this.smallClass = smallClass;
	}
}
