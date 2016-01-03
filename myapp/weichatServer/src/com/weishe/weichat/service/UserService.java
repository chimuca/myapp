package com.weishe.weichat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatGroupMember;
import com.weishe.weichat.bean.DiscussionGroup;
import com.weishe.weichat.bean.DiscussionGroupMember;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.dao.ChatGroupMemberDao;
import com.weishe.weichat.dao.DiscussionGroupMemberDao;
import com.weishe.weichat.dao.FriendsDao;
import com.weishe.weichat.dao.UserDao;
import com.weishe.weichat.util.AuthUtils;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private FriendsDao friendsDao;
	@Autowired
	private ChatGroupMemberDao chatGroupMemberDao;
	@Autowired
	private DiscussionGroupMemberDao discussionGroupMemberDao;

	/**
	 * 通过账户查找用户
	 * 
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account) {
		return userDao.getUserByAccount(account);
	}

	/**
	 * 用户注册
	 * 
	 * @param name
	 * @param account
	 * @param password
	 * @param a
	 * @param gender
	 * @param signature
	 * @param birthday
	 * @return
	 */
	public User addUser(String name, String account, String password,
			Date birthday, String signature, int gender, Attachment a) {
		return userDao.addUser(name, account,
				AuthUtils.getPassword(password.trim()), birthday, signature,
				gender, a);
	}

	public List<User> search(String condition) {
		return userDao.search(condition);
	}

	public User getUserById(int userId) {

		return userDao.getUserById(userId);
	}

	/**
	 * 获取与我相关联的用户，好友 ，同一个群 ，同一个讨论组
	 * 
	 * @param userId
	 * @return
	 */
	public List<User> getRelateUser(int userId) {

		Map<Integer, User> um = new HashMap<Integer, User>();
		// 0.自己
		User u = userDao.getUserById(userId);
		um.put(u.getId(), u);
		// 1，好友
		List<Friends> fl = friendsDao.getFriendsByUserId(userId);
		if (fl != null && fl.size() > 0) {
			for (Friends f : fl) {
				um.put(f.getFriend().getId(), f.getFriend());
			}
		}
		// 2.群好友
		List<ChatGroup> cl = chatGroupMemberDao.getChatGroupByMember(userId);
		if (cl != null && cl.size() > 0) {
			for (ChatGroup cg : cl) {
				List<ChatGroupMember> cgm = chatGroupMemberDao
						.getChatGroupMemberByChatGroup(cg.getId());
				if (cgm != null && cgm.size() > 0) {
					for (ChatGroupMember m : cgm) {
						um.put(m.getUser().getId(), m.getUser());
					}
				}
			}
		}
		// 3.讨论组

		List<DiscussionGroup> dl = discussionGroupMemberDao
				.getAllMyDiscussionGroup(userId);
		if (cl != null && cl.size() > 0) {
			for (DiscussionGroup cg : dl) {
				List<DiscussionGroupMember> cgm = discussionGroupMemberDao
						.getDiscussionGroupMemberByDiscussionGroup(cg.getId());
				if (cgm != null && cgm.size() > 0) {
					for (DiscussionGroupMember m : cgm) {
						System.out.println("d user id:" + m.getUser().getId());
						um.put(m.getUser().getId(), m.getUser());
					}
				}
			}
		}

		List data = new ArrayList<User>();
		data.addAll(um.values());
		return data;
	}
}
