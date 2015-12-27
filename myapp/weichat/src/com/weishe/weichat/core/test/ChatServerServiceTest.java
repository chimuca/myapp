package com.weishe.weichat.core.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weishe.weichat.bean.ChatServer;
import com.weishe.weichat.service.ChatServerService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ChatServerServiceTest {

	// @Autowired
	private ChatServerService chatServerService;

	// @Test
	public void getChatServersTest() {
		List<ChatServer> list = chatServerService.getOnlineServer();

		System.out.println("list size:" + list.size());
		while (true) {

		}
	}

	// @Test
	public void getChatServerTest() {
		ChatServer server = this.chatServerService.getChatServerByIpAndPort(
				"10.1.11.33", 8888);

		System.out.println("id:" + server.getId());
		System.out.println("online:" + server.isOnline());
	}

	// @Test
	public void registServer() {
		this.chatServerService.regist("192.168.1.1", 8888, "测试服务器");
	}

	// @Test
	public void offlineTest() {
		this.chatServerService.offline("192.168.1.21", 8888);
	}
}