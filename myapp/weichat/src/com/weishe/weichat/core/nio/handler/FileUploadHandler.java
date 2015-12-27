package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.ClientRequestMessage;
import com.weishe.weichat.core.bean.JsonMessage;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.service.AttachmentService;
import com.weishe.weichat.service.ChatMessageService;
import com.weishe.weichat.service.FriendsGroupService;
import com.weishe.weichat.service.FriendsService;
import com.weishe.weichat.util.StringUtils;

/**
 * 1、保持服务器间的心态 2、转发消息
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class FileUploadHandler extends SimpleChannelInboundHandler<Attachment> {
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private AttachmentService attachmentService;
	// 本地日志记录对象
	private static final Logger LOGGER = Logger
			.getLogger(FileUploadHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			Attachment message) throws Exception {
		// 客户端认证
		if (sessionManager.clientAuth(message.getUserId() + "",
				message.getToken()) != null) {
			attachmentService.saveAttachment(message);
		} else {
			Message rtMessage = MsgHelper.newResultMessage(
					Msg.MessageType.AUTH_ERROR, "用户认证失败，重新认证!");
			LOGGER.info("用户认证失败,重新认证！");
			channelHandlerContext.channel().writeAndFlush(rtMessage);
		}
		ReferenceCountUtil.release(message);
	}
}