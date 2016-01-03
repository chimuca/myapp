package com.weishe.weichat.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.ChatServer;
import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.core.bean.JsonMessage;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.FileDownload;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.service.ChatServerService;
import com.weishe.weichat.service.UserAuthTokenService;
import com.weishe.weichat.service.UserOnlineServerService;
import com.weishe.weichat.util.PropertyUtils;

/**
 * 保持服务间通信通道
 * 
 * @author chenbiao
 *
 */
@Service
public class SessionManager {
	// 本地日志记录对象
	private static final Logger LOGGER = Logger.getLogger(SessionManager.class);
	@Autowired
	private ChatServerService chatServerService;
	@Autowired
	private UserAuthTokenService userAuthTokenService;
	@Autowired
	private UserOnlineServerService userOnlineServerService;
	/**
	 * 保存一对map是为了以空间换时间，让查找更迅速
	 */
	private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
	private static Map<Session, String> sessionKeyMap = new ConcurrentHashMap<Session, String>();

	private static Map<Channel, String> channelMap = new ConcurrentHashMap<Channel, String>();
	private static Map<String, Channel> channelKeyMap = new ConcurrentHashMap<String, Channel>();

	/**
	 * 本机外网ip
	 */
	// @Value("#{configProperties['netty.server.ip']}")
	// private static String ip;
	/**
	 * 本机服务端监听端口
	 */
	// @Value("#{configProperties['netty.server.port']}")
	// private static int port;

	public static void add(String clientId, Session session) {
		sessionMap.put(clientId, session);
		sessionKeyMap.put(session, clientId);
		channelMap.put(session.getChannel(), clientId);
		channelKeyMap.put(clientId, session.getChannel());
	}

	public static Session get(String clientId) {
		return sessionMap.get(clientId);
	}

	public static void remove(Session session) {
		String key = sessionKeyMap.get(session);
		// 防止重复操作
		if (key == null) {
			return;
		}
		Channel channel = channelKeyMap.get(key);
		sessionMap.remove(key);
		sessionKeyMap.remove(session);
		channelMap.remove(channel);
		channelKeyMap.remove(key);
	}

	/**
	 * 用户将下线，session 将被移除，channel 将被关闭
	 * 
	 * @param channel
	 */
	public void logout(Channel channel) {
		Session session = SessionManager.get(channel);
		if (session != null && session.getType().equals(Session.SessionType.SC)) {
			userOnlineServerService.removeUserOnlineServer(session.getUserId(),
					session.getToken(), SessionManager.getIp(),
					SessionManager.getPort());
		}
		remove(channel);
	}

	public static void remove(Channel channel) {
		String key = channelMap.get(channel);
		// 防止重复操作
		if (key == null) {
			return;
		}
		Session session = sessionMap.get(key);
		sessionMap.remove(key);
		sessionKeyMap.remove(session);
		channelMap.remove(channel);
		channelKeyMap.remove(key);
	}

	public static Session get(Channel channel) {
		String key = channelMap.get(channel);
		return sessionMap.get(key);
	}

	public static String getIp() {
		String ip = PropertyUtils.getValue("netty.server.ip");
		return ip;
	}

	public static int getPort() {
		int port = Integer
				.parseInt(PropertyUtils.getValue("netty.server.port"));
		return port;
	}

	// 客户端登录认证
	public Session clientLoginAuth(ChannelHandlerContext channelHandlerContext,
			String token, int userId) {
		Session session = null;

		User user = userAuthTokenService.getUserByToken(token);

		if (user.getId() == userId) {
			session = new Session(channelHandlerContext.channel(), userId,
					token);
			// 添加进本地，并保存到服务器
			// 采用用户id作为key方便查找
			SessionManager.add(userId + token, session);
			userOnlineServerService.saveUserOnlineServer(userId, token,
					SessionManager.getIp(), SessionManager.getPort());
		}
		return session;
	}

	// 客户端认证检查
	public Session clientAuth(String userId, String token) {

		Session session = SessionManager.get(userId + token);
		return session;
	}

	/**
	 * 处理收到的server login消息
	 * 
	 * @param channelHandlerContext
	 * @return
	 */
	public boolean serverLogin(ChannelHandlerContext channelHandlerContext) {
		InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext
				.channel().localAddress();
		String ip = insocket.getAddress().getHostAddress();
		int port = insocket.getPort();
		ChatServer server = chatServerService
				.getChatServerByIpAndPort(ip, port);

		if (server != null && server.isOnline()) {
			Session session = new Session(
					(SocketChannel) channelHandlerContext.channel());
			SessionManager.add(ip + ":" + port, session);

			LOGGER.info("服务器(" + ip + "," + port + ") 登录连接成功。");

			Msg.Message rtMessage = MsgHelper.newResultMessage(
					Msg.MessageType.LOGIN_SUCCESS, "认证成功！");
			session.send(rtMessage);
			LOGGER.info("认证成功！");
			return true;
		} else {
			String msg;
			if (server == null) {
				msg = "服务器(" + ip + "," + port + ") 登录连接失败，未查找到该服务器。";
			} else if (server != null && !server.isOnline()) {
				msg = "服务器(" + ip + "," + port + ") 登录连接失败,连接服务器已不在线。";
			} else {
				msg = "服务器(" + ip + "," + port + ") 登录连接失败,未知原因。";
			}
			LOGGER.info(msg);

			Msg.Message rtMessage = MsgHelper.newResultMessage(
					Msg.MessageType.LOGIN_ERROR, "服务器认证失败，重新认证!");

			LOGGER.info("服务器认证失败，重新认证!！");
			channelHandlerContext.channel().writeAndFlush(rtMessage);
			return false;
		}
	}

	/**
	 * 检查服务端的认证
	 * 
	 * @param channelHandlerContext
	 * @return
	 */
	public boolean serverAuth(ChannelHandlerContext channelHandlerContext) {
		InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext
				.channel().localAddress();
		String ip = insocket.getAddress().getHostAddress();
		int port = insocket.getPort();
		String clientId = ip + ":" + port;
		Session session = SessionManager.get(clientId);
		// 服务器掉线了重新认证
		if (session == null) {
			return serverLogin(channelHandlerContext);
		}
		return true;
	}

	/**
	 * 当收到ping消息的时候响应pong消息
	 * 
	 * @param channelHandlerContext
	 */
	public void serverPing(ChannelHandlerContext channelHandlerContext) {
		InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext
				.channel().localAddress();
		String ip = insocket.getAddress().getHostAddress();
		int port = insocket.getPort();
		String clientId = ip + ":" + port;
		Session session = SessionManager.get(clientId);
		if (session != null) {
			Message pong = MsgHelper.newPingMessage(
					Msg.MessageType.SERVER_PONG, SessionManager.getIp() + ":"
							+ SessionManager.getPort());
			session.send(pong);
		} else {
			LOGGER.info("响应服务器ping时获取channel失败。");
		}
	}

	public void clientPing(Session session) {
		if (session != null) {
			Message pong = MsgHelper.newPingMessage(
					Msg.MessageType.CLIENT_PONG, SessionManager.getIp() + ":"
							+ SessionManager.getPort());
			session.send(pong);
		} else {
			LOGGER.info("响客户端ping时获取channel失败。");
		}
	}

	/**
	 * 1、当服务器收到消息时，先查询自己SessionManager中是否有链接到目标的session，如果没有则查询数据库，
	 * 看目标客户端链接在哪个服务器上 然后将消息转发至这个服务器 2、将消息转发到自己其他的设备上
	 */
	public void sendMessage(ChatMessage msg) {
		Message message = MsgHelper.newChatMessage(msg);

		Set<UserAuthToken> tokenSet = userOnlineServerService
				.getOnlineToken(msg.getToId());
		if (tokenSet != null) {
			for (UserAuthToken token : tokenSet) {
				Session session = SessionManager.get(msg.getToId()
						+ token.getToken());
				if (session != null) {
					session.send(message);
					if (msg.getAttachment() != null) {
						Msg.Message d = MsgHelper.newFileDownloadMessage(msg
								.getAttachment());
						session.send(d);
					}
				}
			}
		}

		// 转发其他服务器
		if (msg.isTransfer()) {
			// 查询服务器目标所在位置
			Set<ChatServer> set = userOnlineServerService.getOnlineServer(msg
					.getToId());
			if (set != null) {
				for (ChatServer server : set) {
					// 获取本地存储的其他server的session
					String clientId = server.getIp() + ":" + server.getPort();
					Session serverSession = SessionManager.get(clientId);
					if (serverSession != null) {
						serverSession.send(message);
					}
				}
			}
		}

		// 将消息同步推送到自己的其他设备上
		List<UserAuthToken> tokens = userAuthTokenService
				.getUserAuthTokenByUserId(msg.getFromId());
		// 1、在同一服务器上寻找自己其他的设备
		for (UserAuthToken token : tokens) {
			if (!token.getToken().equals(msg.getToken())) {
				Session session = SessionManager.get(msg.getFromId()
						+ token.getToken());
				if (session != null) {
					session.send(message);
					if (msg.getAttachment() != null) {
						Msg.Message d = MsgHelper.newFileDownloadMessage(msg
								.getAttachment());
						session.send(d);
					}
				}
			}
		}
		// 2、在其他的服务器上寻找自己其他的设备

		// 查询服务器目标所在位置
		// 转发其他服务器
		if (msg.isTransfer()) {
			Set<ChatServer> set = userOnlineServerService.getOnlineServer(msg
					.getFromId());
			if (set != null) {
				for (ChatServer server : set) {
					// 获取本地存储的其他server的session
					String clientId = server.getIp() + ":" + server.getPort();
					Session serverSession = SessionManager.get(clientId);
					if (serverSession != null) {
						serverSession.send(message);
					}
				}
			}
		}

	}

	/**
	 * 向客户端发送json包装的消息类型
	 * 
	 * @param message
	 * @param sessionKey
	 *            userId+token
	 */
	public void sendMessage(JsonMessage message, String sessionKey) {
		Session session = SessionManager.get(sessionKey);
		if (session != null) {
			Msg.Message msg = MsgHelper.newJSonMessage(
					message.getJsonMessageType(), message.getJsonStr());
			session.send(msg);
		}
	}

	public void sendMessage(Message message, String sessionKey) {
		Session session = SessionManager.get(sessionKey);
		if (session != null) {
			session.send(message);
		}
	}

	/**
	 * 按人推送消息
	 * 
	 * @param userId
	 * @param message
	 * @return
	 */
	public boolean sendMessage(int userId, Message message) {
		boolean r = false;
		// 将消息发送出去
		List<UserAuthToken> uts = userAuthTokenService
				.getUserAuthTokenByUserId(userId);
		Session session = null;
		if (uts != null) {
			for (UserAuthToken u : uts) {
				session = SessionManager.get(userId + u.getToken());
				if (session != null) {
					session.send(message);
					r = true;
				}
			}
		}
		return r;
	}
}