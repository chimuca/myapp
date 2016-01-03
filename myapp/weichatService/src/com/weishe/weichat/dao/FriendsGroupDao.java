package com.weishe.weichat.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.User;

@Repository
public class FriendsGroupDao extends BaseDao {

	/**
	 * 通过userId查找好友分组列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<FriendsGroup> getFriendsGroupByUserId(int userId) {
		String hql = "from  FriendsGroup where userId = ? order by position ASC";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		List<FriendsGroup> list = query.list();
		return list;
	}

	public FriendsGroup getFriendsGroupById(int id) {
		String hql = "from  FriendsGroup where id = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, id);
		return (FriendsGroup) query.uniqueResult();
	}

	public FriendsGroup saveFriendsGroup(FriendsGroup group) {
		Integer id = (Integer) save(group);
		group.setId(id);
		return group;
	}

	public int getUserNextFriendsGroupPosition(int userId) {
		List list = getFriendsGroupByUserId(userId);
		if (list != null) {
			return list.size() + 1;
		}
		return 1;
	}
}
