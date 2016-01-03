package com.weishe.weichat.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatServer;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.bean.UserOnlineServer;
import com.weishe.weichat.dao.ChatServerDao;
import com.weishe.weichat.dao.UserAuthTokenDao;
import com.weishe.weichat.dao.UserOnlineServerDao;

@Service
public class UserOnlineServerService {

	@Autowired
	private UserOnlineServerDao userOnlineServerDao;

	@Autowired
	private UserAuthTokenDao userAuthTokenDao;
	@Autowired
	private ChatServerDao chatServerDao;

	/**
	 * 从数据库查询用户所在位置
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	public ChatServer getOnlineServer(int userId, String token) {
		UserAuthToken uToken = userAuthTokenDao
				.getUserAuthTokenByUserIdAndToken(userId, token);
		if (uToken != null) {
			UserOnlineServer userOnlineServer = userOnlineServerDao
					.getOnlineServerByToken(uToken.getId());
			if (userOnlineServer != null) {
				return userOnlineServer.getChatServer();
			}
		}
		return null;
	}

	/**
	 * 获取一个用户所在的服务器
	 * 
	 * @param userId
	 * @return
	 */
	public Set<ChatServer> getOnlineServer(int userId) {
		List<UserAuthToken> tokens = userAuthTokenDao
				.getUserAuthTokenByUserId(userId);
		Set<ChatServer> set = new HashSet<ChatServer>();
		if (tokens != null && tokens.size() > 0) {
			for (UserAuthToken token : tokens) {
				UserOnlineServer userOnlineServer = userOnlineServerDao
						.getOnlineServerByToken(token.getId());
				if (userOnlineServer != null) {
					set.add(userOnlineServer.getChatServer());
				}
			}
		}
		return set;
	}

	/**
	 * 获取一个用户所有在线的授权token
	 * 
	 * @param userId
	 * @return
	 */
	public Set<UserAuthToken> getOnlineToken(int userId) {
		List<UserAuthToken> tokens = userAuthTokenDao
				.getUserAuthTokenByUserId(userId);
		Set<UserAuthToken> set = new HashSet<UserAuthToken>();
		if (tokens != null && tokens.size() > 0) {
			for (UserAuthToken token : tokens) {
				UserOnlineServer userOnlineServer = userOnlineServerDao
						.getOnlineServerByToken(token.getId());
				if (userOnlineServer != null) {
					set.add(userOnlineServer.getUserAuthToken());
				}
			}
		}
		return set;
	}

	/**
	 * 设置用户登录的服务器
	 * 
	 * @param userId
	 * @param ip
	 * @param port
	 */
	public void saveUserOnlineServer(int userId, String token, String ip,
			int port) {
		ChatServer server = chatServerDao.getChatServerByIpAndPort(ip, port);
		UserOnlineServer onlineServer = new UserOnlineServer();
		UserAuthToken userAuthToken = userAuthTokenDao
				.getUserAuthTokenByUserIdAndToken(userId, token);
		UserOnlineServer os = userOnlineServerDao.getUserOnlineServer(
				userAuthToken.getId(), server.getId());
		if (os == null || os.getId() < 1) {
			onlineServer.setChatServer(server);
			onlineServer.setUserAuthToken(userAuthToken);
			userOnlineServerDao.save(onlineServer);
		}
	}

	/**
	 * 取消在线状态
	 * 
	 * @param userId
	 * @param token
	 * @param ip
	 * @param port
	 */
	public void removeUserOnlineServer(int userId, String token, String ip,
			int port) {
		ChatServer server = chatServerDao.getChatServerByIpAndPort(ip, port);
		UserAuthToken userAuthToken = userAuthTokenDao
				.getUserAuthTokenByUserIdAndToken(userId, token);
		userOnlineServerDao.deleteUserOnlineServer(userAuthToken.getId(),
				server.getId());
	}

}
