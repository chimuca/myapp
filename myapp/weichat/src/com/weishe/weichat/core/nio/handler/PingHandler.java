package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.core.Session;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.core.bean.PingMessage;

/**
 * 1、保持服务器间的心态 2、转发消息
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class PingHandler extends SimpleChannelInboundHandler<PingMessage> {
	// 本地日志记录对象
	private static final Logger LOGGER = Logger.getLogger(PingHandler.class);
	@Autowired
	private SessionManager sessionManager;

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// channel失效，从Map中移除
		SessionManager.remove((SocketChannel) ctx.channel());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE)) {
				LOGGER.info("READER_IDLE");
				// 超时关闭channel
				ctx.close();
				SessionManager.remove((SocketChannel) ctx.channel());
			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				LOGGER.info("WRITER_IDLE");
				sendPing(ctx);
			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				LOGGER.info("ALL_IDLE");
				// 发送心跳
				sendPing(ctx);
			}
		}

	}

	/**
	 * 当出现WRITER_IDLE、ALL_IDLE时则需要向客户端发出ping消息，试探远端是否还在
	 */
	private void sendPing(ChannelHandlerContext ctx) {
		Session session = SessionManager.get(ctx.channel());
		if (session != null) {
			Message ping = MsgHelper.newPingMessage(
					Msg.MessageType.SERVER_PING, SessionManager.getIp() + ":"
							+ SessionManager.getPort());
			session.send(ping);
		}

	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			PingMessage message) throws Exception {

		switch (message.getMessageType()) {

		case SERVER_PING:
			LOGGER.info("服务器收到ping。");
			if (sessionManager.serverAuth(channelHandlerContext)) {
				sessionManager.serverPing(channelHandlerContext);
			} else {
				return;
			}
			break;
		case SERVER_PONG:
			LOGGER.info("收到服务器pong");
			// 啥也不做
			break;
		case CLIENT_PING:
			Session session = sessionManager.clientAuth(message.getClientId(),"");
			if (session != null) {
				sessionManager.clientPing(session);
			} else {

				Message rtMessage = MsgHelper.newResultMessage(
						Msg.MessageType.AUTH_ERROR, "用户认证失败，重新认证!");

				LOGGER.info("用户认证失败,重新认证！");
				channelHandlerContext.channel().writeAndFlush(rtMessage);
				return;
			}

		case CLIENT_PONG:
			LOGGER.info("收到客户端pong");
			// 啥也不做
			break;

		default:
			break;
		}
		ReferenceCountUtil.release(message);
	}
}