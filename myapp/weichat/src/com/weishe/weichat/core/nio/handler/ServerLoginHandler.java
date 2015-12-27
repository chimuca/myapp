package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.ServerLoginMessage;

/**
 * 1、保持服务器间的心态 2、转发消息
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class ServerLoginHandler extends
		SimpleChannelInboundHandler<ServerLoginMessage> {
	// 本地日志记录对象
	private static final Logger LOGGER = Logger
			.getLogger(ServerLoginHandler.class);
	@Autowired
	private SessionManager sessionManager;

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			ServerLoginMessage message) throws Exception {

		LOGGER.info("服务器登录。");
		// 处理服务器到服务器的登录
		if (!sessionManager.serverLogin(channelHandlerContext)) {
			LOGGER.info("服务器登录失败，关闭。");
			channelHandlerContext.channel().close();
			return;
		}

		ReferenceCountUtil.release(message);
	}
}