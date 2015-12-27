package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.dao.FriendsDao;
import com.weishe.weichat.dao.FriendsGroupDao;

@Service
public class FriendsGroupService {
	@Autowired
	private FriendsGroupDao friendsGroupDao;
	@Autowired
	private FriendsDao friendsDao;

	/**
	 * 添加好友
	 * 
	 * @param userId
	 * @param friendId
	 */
	public FriendsGroup addFriendsGroup(User user, String groupName,
			int position) {
		FriendsGroup group = new FriendsGroup();
		group.setName(groupName);
		group.setPosition(position);
		group.setUser(user);
		group = friendsGroupDao.saveFriendsGroup(group);
		// 更新其他的顺序
		List<FriendsGroup> groups = getFriendsGroupByUserId(user.getId());
		if (groups != null) {
			for (FriendsGroup g : groups) {
				if (g.getId() != group.getId()
						&& (g.getPosition() > group.getPosition() || g
								.getPosition() == group.getPosition())) {
					g.setPosition(g.getPosition() + 1);
					friendsGroupDao.update(g);
				}
			}
		}

		return group;
	}

	/**
	 * 调整排列顺序
	 * 
	 * @param groupId
	 * @param toPosition
	 */
	public void adjustPosition(int groupId, int toPosition) {
		FriendsGroup group = friendsGroupDao.getFriendsGroupById(groupId);
		List<FriendsGroup> groups = getFriendsGroupByUserId(group.getUser()
				.getId());
		// 当前顺序大于调整之后的顺序
		if (group.getPosition() > toPosition) {
			for (FriendsGroup g : groups) {
				if (g.getPosition() < group.getPosition()
						&& (g.getPosition() >= toPosition)) {
					g.setPosition(g.getPosition() + 1);
					friendsGroupDao.update(g);
				}
			}
		}
		// 当前顺序小于调整之后的顺序
		if (group.getPosition() < toPosition) {
			for (FriendsGroup g : groups) {
				if (g.getPosition() > group.getPosition()
						&& (g.getPosition() <= toPosition)) {
					g.setPosition(g.getPosition() - 1);
					friendsGroupDao.update(g);
				}
			}
		}

		group.setPosition(toPosition);
		friendsGroupDao.update(group);
	}

	public void deleteFriendsGroupById(int groupId) {
		FriendsGroup group = friendsGroupDao.getFriendsGroupById(groupId);
		List<FriendsGroup> groups = getFriendsGroupByUserId(group.getUser()
				.getId());
		if (groups != null) {
			for (FriendsGroup g : groups) {
				if (g.getPosition() < group.getPosition()) {
					g.setPosition(g.getPosition() - 1);
					friendsGroupDao.update(g);
				}
			}
		}
	}

	public List<FriendsGroup> getFriendsGroupByUserId(int userId) {
		return friendsGroupDao.getFriendsGroupByUserId(userId);
	}

	public int getUserNextFriendsGroupPosition(int userId) {
		return friendsGroupDao.getUserNextFriendsGroupPosition(userId);
	}

	public FriendsGroup getFriendsGroupById(int groupId) {
		return friendsGroupDao.getFriendsGroupById(groupId);
	}

	public void removeFriendsGroup(int groupId) {
		// 先将好友移动至未分组中
		FriendsGroup group = friendsGroupDao.getFriendsGroupById(groupId);
		List<Friends> list = friendsDao.getFriendsByGroupId(groupId);
		if (list != null) {
			for (Friends f : list) {
				f.setFriendsGroup(null);
				friendsDao.update(f);
			}
		}
		// 删除本分组
		friendsGroupDao.delete(group);
	}
}
