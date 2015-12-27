package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class BaseEntity {
	/**
	 * 主键标示
	 */
	@Id
	@GeneratedValue(generator = "_increment")
	@GenericGenerator(name = "_increment", strategy = "increment")
	@Column(length = 32)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
