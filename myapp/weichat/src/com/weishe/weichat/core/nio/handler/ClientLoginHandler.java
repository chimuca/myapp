package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.core.Session;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.ClientLoginMessage;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.service.TodoService;

/**
 * 1、保持服务器间的心态 2、转发消息
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class ClientLoginHandler extends
		SimpleChannelInboundHandler<ClientLoginMessage> {
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private TodoService todoService;
	// 本地日志记录对象
	private static final Logger LOGGER = Logger
			.getLogger(ClientLoginHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			ClientLoginMessage message) throws Exception {

		Session session = sessionManager.clientLoginAuth(channelHandlerContext,
				message.getToken(), message.getUserId());
		if (session != null) {

			Msg.Message m = MsgHelper.newResultMessage(
					Msg.MessageType.LOGIN_SUCCESS, "登录成功！");
			session.send(m);
			// 发送代办消息
			List<Todo> todos = todoService.getUnCompleteTodoByUserId(message
					.getUserId());
			if (todos != null && todos.size() > 0) {
				Msg.Message m2 = MsgHelper.newTodoListMessage(todos);
				session.send(m2);
			}
		} else {

			Msg.Message rtMessage = MsgHelper.newResultMessage(
					Msg.MessageType.LOGIN_ERROR, "用户认证失败!");
			LOGGER.info("用户登录失败，关闭。");
			channelHandlerContext.channel().writeAndFlush(rtMessage);
			channelHandlerContext.channel().close();
		}
		ReferenceCountUtil.release(message);
	}
}