package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.weishe.weichat.core.Session;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.core.bean.PingMessage;

@Service
public class ServerClientHandler extends
		SimpleChannelInboundHandler<PingMessage> {
	// 本地日志记录对象
	private static final Logger LOGGER = Logger
			.getLogger(ServerClientHandler.class);

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
			ctx.fireChannelRead(ping);
		}

	}

	// 利用写空闲发送心跳检测消息
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case WRITER_IDLE:
				LOGGER.info("服务器客户端：WRITER_IDLE");
				sendPing(ctx);
				LOGGER.info("服务器客户端 send ping to server。");
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			PingMessage baseMsg) throws Exception {

		InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext
				.channel().remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		int port = insocket.getPort();
		LOGGER.info("服务器客户端receive ping from server port:" + port);
		LOGGER.info("msgType:" + baseMsg.getMessageType());
		System.out.println("msgType:" + baseMsg.getMessageType());
		/*
		 * switch (msgType) { case LOGIN: { // 向服务器发起登录 LoginMsg loginMsg = new
		 * LoginMsg(); loginMsg.setPassword("yao");
		 * loginMsg.setUserName("robin");
		 * channelHandlerContext.writeAndFlush(loginMsg); } break; case PING: {
		 * LOGGER.info("receive ping from server----------"); } break; case ASK:
		 * { ReplyClientBody replyClientBody = new ReplyClientBody(
		 * "client info **** !!!"); ReplyMsg replyMsg = new ReplyMsg();
		 * replyMsg.setBody(replyClientBody);
		 * channelHandlerContext.writeAndFlush(replyMsg); } break; case REPLY: {
		 * ReplyMsg replyMsg = (ReplyMsg) baseMsg; ReplyServerBody
		 * replyServerBody = (ReplyServerBody) replyMsg .getBody();
		 * LOGGER.info("receive client msg: " +
		 * replyServerBody.getServerInfo()); } default: break; }
		 */
		ReferenceCountUtil.release(baseMsg);
	}
}