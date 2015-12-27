package com.weishe.weichat.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.UserOnlineServer;

@Repository
public class UserOnlineServerDao extends BaseDao {
	/**
	 * 
	 * @param tokenId
	 * @return
	 */
	public UserOnlineServer getOnlineServerByToken(int tokenId) {
		String hql = "from  UserOnlineServer where userAuthTokenId =  ?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, tokenId);
		return (UserOnlineServer) query.uniqueResult();
	}

	/**
	 * 删除
	 * 
	 * @param userAuthTokenId
	 * @param chatServerId
	 */
	public void deleteUserOnlineServer(int userAuthTokenId, int chatServerId) {
		String hql = "delete from  UserOnlineServer where userAuthTokenId =  ? and chatServerId=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userAuthTokenId);
		query.setInteger(1, chatServerId);
		query.executeUpdate();
	}

	public UserOnlineServer getUserOnlineServer(int userAuthTokenId,
			int chatServerId) {
		String hql = "  from  UserOnlineServer where userAuthTokenId =  ? and chatServerId=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userAuthTokenId);
		query.setInteger(1, chatServerId);
		return (UserOnlineServer) query.uniqueResult();
	}
}
