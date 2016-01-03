package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.ClientLoginMessage;
import com.weishe.weichat.core.bean.ClientRequestMessage;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.PingMessage;
import com.weishe.weichat.core.bean.ServerLoginMessage;
import com.weishe.weichat.service.AttachmentService;

/**
 * 主消息分发
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class MsgChatHandler extends SimpleChannelInboundHandler<Message> {
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private AttachmentService attachmentService;
	// 本地日志记录对象
	private static final Logger LOGGER = Logger.getLogger(MsgChatHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			Message message) throws Exception {

		switch (message.getMessageType()) {
		case CLIENT_LOGIN:
			if (message.getClientLoginMessage() != null) {
				ClientLoginMessage msg = new ClientLoginMessage();
				msg.setToken(message.getClientLoginMessage().getToken());
				msg.setUserId(message.getClientLoginMessage().getUserId());
				channelHandlerContext.fireChannelRead(msg);
			} else {
				LOGGER.info("消息异常，用户登录消息为null。");
			}
			break;
		case CLIENT_PONG:
			if (message.getPingMessage() != null
					&& (message.getMessageType()
							.equals(Msg.MessageType.CLIENT_PONG))) {
				LOGGER.info("client pong消息不用处理");
			} else {
				LOGGER.info("消息异常，client pong消息为null或者消息类型异常。");
			}
			break;
		case CLIENT_PING:
			if (message.getPingMessage() != null
					&& (message.getMessageType()
							.equals(Msg.MessageType.CLIENT_PING))) {
				PingMessage msg = new PingMessage();
				msg.setClientId(message.getPingMessage().getClientId());
				msg.setMessageType(Msg.MessageType.CLIENT_PING);
				channelHandlerContext.fireChannelRead(msg);
			} else {
				LOGGER.info("消息异常，client ping消息为null或者消息类型异常。");
			}
			break;
		case SERVER_LOGIN:
			if (message.getServerLoginMessage() != null) {
				ServerLoginMessage msg = new ServerLoginMessage();
				msg.setIp(message.getServerLoginMessage().getIp());
				msg.setPort(message.getServerLoginMessage().getPort());
				channelHandlerContext.fireChannelRead(msg);
			}
			break;
		case SERVER_PING:
			if (message.getPingMessage() != null
					&& (message.getMessageType()
							.equals(Msg.MessageType.SERVER_PING))) {
				PingMessage msg = new PingMessage();
				msg.setClientId(message.getPingMessage().getClientId());
				msg.setMessageType(Msg.MessageType.SERVER_PING);
				channelHandlerContext.fireChannelRead(msg);
			} else {
				LOGGER.info("消息异常，client ping消息为null或者消息类型异常。");
			}
			break;
		case SERVER_PONG:
			LOGGER.info("server pong消息不用处理");
			break;
		case CHAT_MESSAGE:
			if (message.getChatMessage() != null) {
				ChatMessage msg = new ChatMessage();
				msg.setContent(message.getChatMessage().getContent());
				msg.setFromId(message.getChatMessage().getFromId());
				msg.setToId(message.getChatMessage().getToId());
				msg.setToken(message.getChatMessage().getToken());
				msg.setTransfer(message.getChatMessage().getTransfer());
				msg.setType(ChatMessage.TYPE_SEND);
				msg.setMsgType(message.getChatMessage().getMsgType());
				msg.setChatGroupId(message.getChatMessage().getChatGroupId());
				msg.setDiscussionGroupId(message.getChatMessage()
						.getDiscussionGroupId());
				msg.setUuid(message.getChatMessage().getUuid());
				msg.setStatus(ChatMessage.STATUS_SEND);
				// 都以当前服务器时间为准
				msg.setDate(new Date());
				msg.setContentType(message.getChatMessage().getContentType());
				// 处理附件消息
				if (message.getChatMessage().getContentType() == ChatMessage.CONTENT_TYPE_ATTACHMENT) {
					Attachment attachment = attachmentService
							.getAttachmentByGroupNameAndPath(message
									.getChatMessage().getFileGroupName(),
									message.getChatMessage().getFilePath());

					// 因为消息是异步的有可能附件消息还未保存
					if (attachment == null) {
						attachment = new Attachment();

						attachment.setCreateDate(new Date());
						attachment.setGroupName(message.getChatMessage()
								.getFileGroupName());
						attachment.setName("");
						attachment.setPath(message.getChatMessage()
								.getFilePath());
						attachment = attachmentService
								.saveAttachment(attachment);
					}
					msg.setAttachment(attachment);
				}

				channelHandlerContext.fireChannelRead(msg);
			} else {
				LOGGER.info("消息异常，chat message消息为null。");
			}
			break;
		case CLIENT_REQUEST:
			if (message.getClientRequestMessage() != null) {
				ClientRequestMessage r = new ClientRequestMessage();
				r.setParameter(message.getClientRequestMessage().getParameter());
				r.setRequestType(message.getClientRequestMessage()
						.getRequestType());
				r.setUserId(message.getClientRequestMessage().getUserId());
				r.setToken(message.getClientRequestMessage().getToken());
				channelHandlerContext.fireChannelRead(r);
			} else {
				LOGGER.info("消息异常，ClientRequest message消息为null。");
			}
			break;
		case FIEL:
			if (message.getFileUpload() != null) {
				Attachment a = new Attachment();

				a.setCreateDate(new Date());
				a.setGroupName(message.getFileUpload().getGroupName());
				a.setName(message.getFileUpload().getName());
				a.setPath(message.getFileUpload().getPath());
				a.setSize(message.getFileUpload().getSize());
				a.setType(message.getFileUpload().getType());
				a.setUserId(message.getFileUpload().getUserId());
				a.setToken(message.getFileUpload().getToken());
				channelHandlerContext.fireChannelRead(a);
			} else {
				LOGGER.info("消息异常，FileUpload message消息为null。");
			}
			break;
		default:
			break;
		}
		ReferenceCountUtil.release(message);
	}

	/**
	 * 发生异常时如何处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LOGGER.info("发生异常,用户将下线，session 将被移除，channel 将被关闭！");

		// 用户下线
		sessionManager.logout(ctx.channel());
		ctx.channel().close();
		ctx.close();
	}
}