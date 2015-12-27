package com.weishe.weichat.core;

public enum MsgType {
	/**
	 * 服务器登入
	 */
	SERVER_LOGIN,
	/**
	 * 服务器登入异常
	 */
	SERVER_LOGIN_ERROR,
	/**
	 * 客户端登入
	 */
	CLIENT_LOGIN,
	/**
	 * 服务器发出的ping
	 */
	SERVER_PING,
	/**
	 * 客户端发出的ping
	 */
	CLIENT_PING,
	/**
	 * 服务器发出的pong
	 */
	SERVER_PONG,
	/**
	 * 客户端发出的pong
	 */
	CLIENT_PONG,
	/**
	 * 服务端错误异常
	 */
	SERVER_ERROR,
	/**
	 * 登录成功
	 */
	LOGIN_SUCESS,
	/**
	 * 登录失败
	 */
	LOGIN_ERROR,
	/**
	 * 收到的消息
	 */
	MESSAGE
}