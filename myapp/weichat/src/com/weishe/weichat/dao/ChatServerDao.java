package com.weishe.weichat.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.ChatServer;

@Repository
public class ChatServerDao extends BaseDao {

	/**
	 * 获取所有在线的服务器
	 * 
	 * @return
	 */
	public List<ChatServer> getOnlineServer() {
		String hql = "from  ChatServer where online = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setBoolean(0, true);
		return query.list();
	}

	public ChatServer getChatServerByIpAndPort(String ip, int port) {
		String hql = "from  ChatServer where ip = ? and port=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, ip);
		query.setInteger(1, port);
		return (ChatServer) query.uniqueResult();
	}

}
