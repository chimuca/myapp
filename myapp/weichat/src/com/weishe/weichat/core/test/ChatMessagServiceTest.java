package com.weishe.weichat.core.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.ChatServer;
import com.weishe.weichat.service.ChatMessageService;
import com.weishe.weichat.service.ChatServerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ChatMessagServiceTest {

	@Autowired
	private ChatMessageService chatMessageService;

	@Test
	public void getChatMessageTest() {
		List<ChatMessage> list = chatMessageService.getAllChatMessageByToId(1,
				0);

		System.out.println("list size:" + list.size());

	}

}