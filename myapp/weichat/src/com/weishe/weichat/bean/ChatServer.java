package com.weishe.weichat.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ChatServer")
public class ChatServer extends BaseEntity {

	@Column(length = 50)
	private String ip;// ip地址
	@Column(length = 10)
	private int port;// 监听端口
	@Column
	private boolean online;// 在线

	@Column(length = 50)
	private String name;

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public boolean isOnline() {
		return online;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setOnLine(boolean online) {
		this.online = online;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
