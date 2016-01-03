package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatGroupMember;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.dao.ChatGroupDao;
import com.weishe.weichat.dao.ChatGroupMemberDao;

@Service
public class ChatGroupMemberService {
	@Autowired
	private ChatGroupMemberDao chatGroupMemberDao;
	@Autowired
	private ChatGroupDao chatGroupDao;

	/**
	 * 得到群中没有屏蔽群消息的成员
	 * 
	 * @param chatGroupId
	 * @return
	 */
	public List<ChatGroupMember> getUnshieldMemberByChatGroupId(int chatGroupId) {
		return chatGroupMemberDao.getUnshieldMemberByChatGroupId(chatGroupId);
	}

	public void addMember(int chatGroupId, User user) {
		ChatGroupMember m = chatGroupMemberDao
				.getChatGroupMemberByGroupIdAndUserId(chatGroupId, user.getId());
		if (m == null) {
			ChatGroup g = chatGroupDao.getChatGroupById(chatGroupId);
			m = new ChatGroupMember();
			m.setChatGroup(g);
			m.setRemarkName(user.getName());
			m.setShield(false);
			m.setUser(user);
			chatGroupDao.save(m);
		}

	}

	public List<ChatGroup> getChatGroupByMember(int userId) {
		return chatGroupMemberDao.getChatGroupByMember(userId);
	}

	public void deleteMemberByUserId(int chatGroupId, int memberId) {
		chatGroupMemberDao.deleteMemberByUserId(chatGroupId, memberId);
	}

	public List<ChatGroupMember> getMemberByChatGroupId(int chatGroupId) {
		return chatGroupMemberDao.getChatGroupMemberByChatGroup(chatGroupId);
	}
}
