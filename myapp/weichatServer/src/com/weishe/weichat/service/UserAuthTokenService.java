package com.weishe.weichat.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.dao.UserAuthTokenDao;

@Service
public class UserAuthTokenService {

	@Autowired
	private UserAuthTokenDao userAuthTokenDao;

	/**
	 * 通过认证码查找认证用户,改token必须是生效的
	 * 
	 * @return
	 */
	public User getUserByToken(String token) {
		return userAuthTokenDao.getUserByToken(token);
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
		return userAuthTokenDao.getUserAuthTokenByUserIdAndToken(userId, token);
	}

	/**
	 * 查找所有该用户的token
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserAuthToken> getUserAuthTokenByUserId(int userId) {
		return userAuthTokenDao.getUserAuthTokenByUserId(userId);
	}

	/**
	 * 保存用户登录信息
	 * 
	 * @param user
	 * @param clientId
	 * @param clientType
	 */
	public void save(User user, String clientId, String clientType, String token) {
		UserAuthToken ut = userAuthTokenDao
				.getUserAuthTokenByUserIdAndClientId(user.getId(), clientId);
		if (ut == null) {
			ut = new UserAuthToken();
			ut.setUser(user);
			ut.setClientId(clientId);
		}
		ut.setEnable(true);
		ut.setCreateDate(new Date());
		ut.setToken(token);
		ut.setClientType(clientType);
		userAuthTokenDao.save(ut);
	}
}
