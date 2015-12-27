package com.weishe.weichat.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;

@Repository
public class UserAuthTokenDao extends BaseDao {

	/**
	 * 通过认证码查找认证用户,改token必须是生效的
	 * 
	 * @return
	 */
	public User getUserByToken(String token) {
		String hql = "from  UserAuthToken where token = ? and enable=?";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, token);
		query.setBoolean(1, true);
		UserAuthToken t = (UserAuthToken) query.uniqueResult();
		return t.getUser();
	}

	/**
	 * 通过组合主键查找
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	public UserAuthToken getUserAuthTokenByUserIdAndToken(int userId,
			String token) {
		String hql = "from  UserAuthToken where token = ? and enable=? and userId=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, token);
		query.setBoolean(1, true);
		query.setInteger(2, userId);
		return (UserAuthToken) query.uniqueResult();
	}

	/**
	 * 通过用户id查找用户所在的服务器
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserAuthToken> getUserAuthTokenByUserId(int userId) {
		String hql = "from  UserAuthToken where   enable=? and userId=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setBoolean(0, true);
		query.setInteger(1, userId);
		return query.list();
	}

	public UserAuthToken getUserAuthTokenByUserIdAndClientId(int userId,
			String clientId) {
		String hql = "from  UserAuthToken where clientId = ?  and userId=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, clientId);
		query.setInteger(1, userId);
		return (UserAuthToken) query.uniqueResult();
	}
}
