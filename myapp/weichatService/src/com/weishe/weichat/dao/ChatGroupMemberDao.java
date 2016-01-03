package com.weishe.weichat.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatGroupMember;

@Repository
public class ChatGroupMemberDao extends BaseDao {
	public List<ChatGroup> getChatGroupByMember(int userId) {
		String hql = "from  ChatGroupMember where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		List<ChatGroupMember> cgm = query.list();
		if (cgm != null && cgm.size() > 0) {
			ArrayList<ChatGroup> list = new ArrayList<ChatGroup>();
			for (ChatGroupMember g : cgm) {
				list.add(g.getChatGroup());
			}
			return list;
		} else {
			return null;
		}

	}

	/**
	 * 通过群查找群成员
	 * 
	 * @param chatGroupId
	 * @return
	 */
	public List<ChatGroupMember> getChatGroupMemberByChatGroup(int chatGroupId) {
		String hql = "from  ChatGroupMember where chatGroupId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, chatGroupId);
		return query.list();
	}

	/**
	 * 通过群查找群成员
	 * 
	 * @param chatGroupId
	 * @return
	 */
	public List<ChatGroupMember> getUnshieldMemberByChatGroupId(int chatGroupId) {
		String hql = "from  ChatGroupMember where chatGroupId = ? and shield=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, chatGroupId);
		query.setBoolean(1, false);
		return query.list();
	}

	/**
	 * 得到个人的所有群
	 * 
	 * @param userId
	 * @return
	 */
	public List<ChatGroup> getAllMyChatGroup(int userId) {
		String hql = "from  ChatGroupMember where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);

		query.setInteger(0, userId);
		List<ChatGroupMember> list = query.list();
		List<ChatGroup> groups = new ArrayList<ChatGroup>();
		if (list != null) {
			for (ChatGroupMember m : list) {
				groups.add(m.getChatGroup());
			}
		}
		return groups;
	}

	public ChatGroupMember getChatGroupMemberByGroupIdAndUserId(int groupId,
			int userId) {
		String hql = "from  ChatGroupMember where chatGroupId = ? and userId =? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, groupId);
		query.setInteger(1, userId);
		return (ChatGroupMember) query.uniqueResult();
	}

	public void deleteMemberByUserId(int chatGroupId, int memberId) {
		String hql = "from  ChatGroupMember where userId = ? and chatGroupId=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, memberId);
		query.setInteger(1, chatGroupId);
		ChatGroupMember cgm = (ChatGroupMember) query.uniqueResult();
		if (cgm != null) {
			delete(cgm);
		}
	}
}
