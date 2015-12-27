package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.bean.UserOnlineServer;
import com.weishe.weichat.dao.FriendsDao;
import com.weishe.weichat.dao.UserAuthTokenDao;
import com.weishe.weichat.dao.UserDao;
import com.weishe.weichat.dao.UserOnlineServerDao;

@Service
public class FriendsService {

	@Autowired
	private FriendsDao friendsDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserOnlineServerDao userOnlineServerDao;

	@Autowired
	private UserAuthTokenDao userAuthTokenDao;

	/**
	 * 通过userId查找好友
	 * 
	 * @param userId
	 * @return
	 */
	public List<Friends> getFriendsByUserId(int userId) {
		return friendsDao.getFriendsByUserId(userId);
	}

	/**
	 * 添加好友
	 * 
	 * @param userId
	 * @param friendId
	 */
	public void addFriend(User user, User friend) {

		if (isFriends(user, friend)) {
			return;
		}
		Friends friends = new Friends();
		friends.setUser(user);
		friends.setFriend(friend);

		friendsDao.save(friends);
	}

	public boolean isFriends(User user, User friends) {
		return friendsDao.isFriends(user, friends);
	}

	/**
	 * 两个人相互加好友
	 * 
	 * @param user1
	 * @param user2
	 */
	public void makeFriends(User user1, User user2) {
		Friends f = friendsDao.getFriendsByUserIdAndFriendsId(user1.getId(),
				user2.getId());
		if (f == null || f.getId() < 0) {
			addFriend(user1, user2);
		}

		Friends f2 = friendsDao.getFriendsByUserIdAndFriendsId(user2.getId(),
				user1.getId());
		if (f2 == null || f2.getId() < 0) {
			addFriend(user2, user1);
		}
	}

	/**
	 * 得到用户指定好友
	 * 
	 * @param userId
	 * @param friendsId
	 * @return
	 */
	public Friends getFriendsByUserIdAndFriendsUserId(int userId, int friendsId) {
		return friendsDao.getFriendsByUserIdAndFriendsUserId(userId, friendsId);
	}

	/**
	 * 获取好友的在线状态
	 * 
	 * @param userId
	 * @param friendsId
	 * @return
	 */
	public int getFriendsOnlineStatus(int userId, int friendsId) {
		int onlineStatus = UserOnlineServer.ONLINE_STATUS_OFFLINE;
		// 1、是否被隐身
		Friends f = friendsDao
				.getFriendsByUserIdAndFriendsId(userId, friendsId);
		if (f == null) {
			// 此为异常情况
			return UserOnlineServer.ONLINE_STATUS_OFFLINE;
		}
		if (f.isShield()) {
			return UserOnlineServer.ONLINE_STATUS_OFFLINE;
		} else {
			// 先获取用户的授权token，然后去查看
			List<UserAuthToken> list = userAuthTokenDao
					.getUserAuthTokenByUserId(friendsId);
			if (list != null) {
				for (UserAuthToken t : list) {
					UserOnlineServer us = userOnlineServerDao
							.getOnlineServerByToken(t.getId());
					if (us != null && onlineStatus > us.getOnlineStatus()) {
						// 获取最小的
						onlineStatus = us.getOnlineStatus();
					}
				}
			}
		}
		return onlineStatus;
	}

	public void moveFriendsGroup(Friends f, FriendsGroup fg) {
		friendsDao.moveFriendsGroup(f, fg);
	}

	public Friends getFriendsByUserIdAndFriendsId(int userId, int friendsId) {

		return friendsDao.getFriendsByUserIdAndFriendsId(userId, friendsId);
	}
}
