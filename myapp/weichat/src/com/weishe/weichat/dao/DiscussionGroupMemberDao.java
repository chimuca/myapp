package com.weishe.weichat.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatGroupMember;
import com.weishe.weichat.bean.DiscussionGroup;
import com.weishe.weichat.bean.DiscussionGroupMember;

@Repository
public class DiscussionGroupMemberDao extends BaseDao {
	public List<DiscussionGroup> getDiscussionGroupByMember(int userId) {
		String hql = "from  DiscussionGroupMember where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		List<DiscussionGroupMember> cgm = query.list();
		if (cgm != null && cgm.size() > 0) {
			ArrayList<DiscussionGroup> list = new ArrayList<DiscussionGroup>();
			for (DiscussionGroupMember dg : cgm) {
				list.add(dg.getDiscussionGroup());
			}
			return list;
		} else {
			return null;
		}

	}

	/**
	 * 通过群查找群成员
	 * 
	 * @param DiscussionGroupId
	 * @return
	 */
	public List<DiscussionGroupMember> getDiscussionGroupMemberByDiscussionGroup(
			int DiscussionGroupId) {
		String hql = "from  DiscussionGroupMember where DiscussionGroupId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, DiscussionGroupId);
		return query.list();
	}

	/**
	 * 通过群查找群成员
	 * 
	 * @param DiscussionGroupId
	 * @return
	 */
	public List<DiscussionGroupMember> getUnshieldMemberByDiscussionGroupId(
			int DiscussionGroupId) {
		String hql = "from  DiscussionGroupMember where DiscussionGroupId = ? and shield=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, DiscussionGroupId);
		query.setBoolean(1, false);
		return query.list();
	}

	/**
	 * 得到个人所在的讨论组
	 */
	public List<DiscussionGroup> getAllMyDiscussionGroup(int userId) {
		String hql = "from  DiscussionGroupMember where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);

		query.setInteger(0, userId);
		List<DiscussionGroupMember> list = query.list();
		List<DiscussionGroup> groups = new ArrayList<DiscussionGroup>();
		if (list != null) {
			for (DiscussionGroupMember m : list) {
				groups.add(m.getDiscussionGroup());
			}
		}
		return groups;
	}
}
