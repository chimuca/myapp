package com.weishe.weichat.core;

import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.core.bean.Msg.ChatGroup;
import com.weishe.weichat.core.bean.Msg.ChatMessage;

import io.netty.channel.Channel;

public class Session {
	public enum SessionType {
		/**
		 * 服务器到客户端的session
		 */
		SC,
		/**
		 * 服务器之间的session
		 */
		SS
	}

	private Channel channel;
	private SessionType type;

	/**
	 * 用户id
	 */
	private int userId;
	/**
	 * 登录授权码
	 */
	private String token;

	/**
	 * 生成服务端对服务端的session
	 * 
	 * @param channel
	 */
	public Session(Channel channel) {
		this.channel = channel;
		this.type = SessionType.SS;
	}

	/**
	 * 生成服务端对客户端session
	 * 
	 * @param channel
	 * @param userId
	 * @param token
	 */
	public Session(Channel channel, int userId, String token) {
		this.channel = channel;
		this.type = SessionType.SC;
		this.userId = userId;
		this.token = token;
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 */
	public void send(Object msg) {
		// 如果是语音消息则附带上语音
		channel.writeAndFlush(msg);
	}

	public Channel getChannel() {
		return channel;
	}

	public SessionType getType() {
		return type;
	}

	public int getUserId() {
		return userId;
	}

	public String getToken() {
		return token;
	}
}
