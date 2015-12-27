package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatServer;
import com.weishe.weichat.dao.ChatServerDao;

@Service
public class ChatServerService {

	@Autowired
	private ChatServerDao chatServerDao;

	/**
	 * 得到所有在线server
	 * 
	 * @return
	 */
	public List<ChatServer> getOnlineServer() {
		return this.chatServerDao.getOnlineServer();
	}

	public ChatServer getChatServerByIpAndPort(String ip, int port) {
		return this.chatServerDao.getChatServerByIpAndPort(ip, port);
	}

	/**
	 * 当服务器启动成功后将自己注册到系统中， 1、如果数据库中存在该服务器则设置上线状态为true 2、如果不存在则插入，设置上线状态为true
	 * 
	 * @param ip
	 * @param port
	 */
	public void regist(String ip, int port, String name) {

		ChatServer server = this.chatServerDao.getChatServerByIpAndPort(ip,
				port);
		if (server != null && server.getId() > 0) {
			if (!server.isOnline()) {
				server.setOnLine(true);
				this.chatServerDao.update(server);
			}
		} else {
			server = new ChatServer();
			server.setIp(ip);
			server.setPort(port);
			server.setName(name);
			server.setOnLine(true);
			this.chatServerDao.insert(server);
		}
	}

	/**
	 * 将已经上线的服务器下线
	 * 
	 * @param ip
	 * @param port
	 */
	public void offline(String ip, int port) {
		ChatServer server = this.chatServerDao.getChatServerByIpAndPort(ip,
				port);
		if (server != null && server.getId() > 0) {
			if (server.isOnline()) {
				server.setOnLine(false);
				this.chatServerDao.update(server);
			}
		}
	}
}
