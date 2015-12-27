package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 群类别实体
 * 
 * @author chenbiao
 *
 */
@Entity
@Table(name = "GroupClass")
public class GroupClass extends BaseEntity {
	public final static int TYPE_BIG_CLASS = 0;
	public final static int TYPE_SMALL_CLASS = 1;

	/**
	 * 分类名称
	 */
	@Column(length = 200)
	private String name;

	/**
	 * 分类类型
	 */
	@Column
	private int type;

	/**
	 * 所属大类
	 */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = GroupClass.class)
	@JoinColumn(name = "bigClassId", updatable = false)
	private GroupClass bigClass;

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public GroupClass getBigClass() {
		return bigClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setBigClass(GroupClass bigClass) {
		this.bigClass = bigClass;
	}

}
