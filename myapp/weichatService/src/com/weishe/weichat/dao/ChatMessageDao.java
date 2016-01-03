package com.weishe.weichat.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.DiscussionGroup;

@Repository
public class ChatMessageDao extends BaseDao {
	@Autowired
	private ChatGroupMemberDao chatGroupMemberDao;
	@Autowired
	private DiscussionGroupMemberDao discussionGroupMemberDao;

	/**
	 * 通过fromId查找消息
	 * 
	 * @param fromId
	 * @return
	 */
	public List<ChatMessage> getChatMessageByFromId(int fromId) {
		String hql = "from  ChatMessage where fromId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, fromId);
		return query.list();
	}

	/**
	 * 给定一个起始id查找
	 * 
	 * @param fromId
	 * @param beginId
	 * @return
	 */
	public List<ChatMessage> getChatMessageByFromIdAndBeginId(int fromId,
			int beginId) {
		String hql = "from  ChatMessage where fromId = ? and id>?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, fromId);
		query.setInteger(1, beginId);
		return query.list();
	}

	/**
	 * 通过toId 和消息起始Id查找
	 * 
	 * @param toId
	 *            接收消息的人
	 * @param beginId
	 * @return
	 */
	public List<ChatMessage> getChatMessageByToId(int toId, int beginId) {
		String hql = "from  ChatMessage where toId = ? and id >? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, toId);
		query.setInteger(1, beginId);
		return query.list();
	}

	/**
	 * 得到我的所有消息，包括群消息
	 * 
	 * @param toId
	 * @param fromMessageId
	 * @return
	 */
	public List<ChatMessage> getAllChatMessageByToId(int toId, int fromMessageId) {
		// 首先得到我所有的群和讨论组号
		List<ChatGroup> lg = chatGroupMemberDao.getAllMyChatGroup(toId);
		List<DiscussionGroup> dg = discussionGroupMemberDao
				.getAllMyDiscussionGroup(toId);
		List chatGroup = new ArrayList();

		List discussionGroup = new ArrayList();
		// 防止没有群sql语句出现错误
		chatGroup.add(-111);
		discussionGroup.add(-111);
		for (ChatGroup g : lg) {
			chatGroup.add(g.getId());
		}
		for (DiscussionGroup d : dg) {
			discussionGroup.add(d.getId());
		}
		String hql = "from  ChatMessage where id>? and (toId = ? or chatGroupId in (:chatGroup) or discussionGroupId  in (:discussionGroup) ) ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, fromMessageId);
		query.setInteger(1, toId);
		query.setParameterList("chatGroup", chatGroup);
		query.setParameterList("discussionGroup", discussionGroup);
		return query.list();
	}

	public List<ChatMessage> getChatMessageByToIdAndBeginId(int toId) {
		String hql = "from  ChatMessage where toId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, toId);
		return query.list();
	}

	/**
	 * 保存消息
	 * 
	 * @param message
	 * @return
	 */
	public ChatMessage addChatMessage(ChatMessage message) {
		Integer id = (Integer) this.save(message);
		message.setId(id);
		return message;
	}

}
