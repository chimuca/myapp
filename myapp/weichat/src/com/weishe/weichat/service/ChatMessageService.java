package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.dao.ChatMessageDao;

@Service
public class ChatMessageService {

	@Autowired
	private ChatMessageDao chatMessageDao;

	/**
	 * 
	 * @param toId
	 * @param fromMessageId
	 * @return
	 */
	public List<ChatMessage> getAllChatMessageByToId(int toId, int fromMessageId) {
		return chatMessageDao.getAllChatMessageByToId(toId, fromMessageId);
	}

	/**
	 * 添加消息
	 * 
	 * @param message
	 * @return
	 */
	public ChatMessage addChatMessage(ChatMessage message) {
		return chatMessageDao.addChatMessage(message);
	}
}
