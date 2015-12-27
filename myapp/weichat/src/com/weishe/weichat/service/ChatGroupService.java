package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.bean.UserOnlineServer;
import com.weishe.weichat.dao.ChatGroupDao;
import com.weishe.weichat.dao.FriendsDao;
import com.weishe.weichat.dao.UserAuthTokenDao;
import com.weishe.weichat.dao.UserDao;
import com.weishe.weichat.dao.UserOnlineServerDao;

@Service
public class ChatGroupService {
	@Autowired
	private ChatGroupDao chatGroupDao;

	public List<ChatGroup> search(String condition) {
		return chatGroupDao.search(condition);
	}

	public ChatGroup addChatGroup(String name, User user) {
		return chatGroupDao.addChatGroup(name, user);
	}

	public ChatGroup getChatGroupById(int chatGroupId) {
		return chatGroupDao.getChatGroupById(chatGroupId);
	}
}
