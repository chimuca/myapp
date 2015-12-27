package com.weishe.weichat.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.util.StringUtils;

@Repository
public class ChatGroupDao extends BaseDao {

	/**
	 * 搜索群
	 * 
	 * @param condition
	 * @return
	 */
	public List<ChatGroup> search(String condition) {
		if (condition == null || condition.isEmpty()) {
			return null;
		} else {
			if (!condition.startsWith("%")) {
				condition = "%" + condition;
			}
			if (!condition.endsWith("%")) {
				condition = condition + "%";
			}
		}
		String hql = "from  ChatGroup where account like ? or name like ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);

		query.setString(0, condition);
		query.setString(1, condition);
		return query.list();
	}

	public ChatGroup addChatGroup(String name, User user) {

		ChatGroup cg = new ChatGroup();

		cg.setCreateBy(user);
		cg.setName(name);
		cg.setCreateDate(new Date());
		cg.setAccount((getMaxAccout() + 1) + "");

		Integer id = (Integer) save(cg);
		cg.setId(id);
		return cg;
	}

	public int getMaxAccout() {
		int maxAccount = 100000;
		String hql = "from  ChatGroup   order by id desc ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setMaxResults(1);
		ChatGroup g = (ChatGroup) query.uniqueResult();
		if (g != null) {
			maxAccount = StringUtils.toInt(g.getAccount());
		}
		return maxAccount;
	}

	public ChatGroup getChatGroupById(int id) {
		String hql = "from  ChatGroup where id =? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, id);
		ChatGroup g = (ChatGroup) query.uniqueResult();
		return g;
	}
}
