package com.weishe.weichat.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.User;

@Repository
public class FriendsDao extends BaseDao {

	/**
	 * 通过userId查找好友
	 * 
	 * @param userId
	 * @return
	 */
	public List<User> getFriendsUserByUserId(int userId) {
		String hql = "from  Friends where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);

		List<Friends> list = query.list();
		List<User> users = new ArrayList<User>();
		if (list != null) {
			for (Friends f : list) {
				User user = f.getFriend();
				users.add(user);
			}
		}
		return users;
	}

	public List<Friends> getFriendsByUserId(int userId) {
		String hql = "from  Friends where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);

		List<Friends> list = query.list();
		return list;
	}

	public Friends getFriendsByUserIdAndFriendsId(int userId, int friendsId) {
		String hql = "from  Friends where userId = ? and id=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, friendsId);
		return (Friends) query.uniqueResult();
	}

	public List<Friends> getFriendsByGroupId(int groupId) {
		String hql = "from  Friends where friendsGroupId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, groupId);

		List<Friends> list = query.list();
		return list;
	}

	public void moveFriendsGroup(Friends f, FriendsGroup fg) {
		String hql = "update Friends set friendsGroupId=? where id=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, fg.getId());
		query.setInteger(1, f.getId());
		query.executeUpdate();
	}

	public Friends getFriendsByUserIdAndFriendsUserId(int userId, int friendsId) {
		String hql = "from  Friends where userId = ? and friendId=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		query.setInteger(1, friendsId);
		return (Friends) query.uniqueResult();
	}

	public boolean isFriends(User user, User friends) {
		Friends f = getFriendsByUserIdAndFriendsUserId(user.getId(),
				friends.getId());
		if (f != null && f.getId() > 0) {
			return true;
		}
		return false;
	}
}
