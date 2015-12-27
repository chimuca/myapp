package com.weishe.weichat.core.nio.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatGroupMember;
import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.DiscussionGroupMember;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.service.ChatGroupMemberService;
import com.weishe.weichat.service.ChatMessageService;
import com.weishe.weichat.service.DiscussionGroupMemberService;
import com.weishe.weichat.service.FriendsService;
import com.weishe.weichat.service.UserAuthTokenService;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * 1、保持服务器间的心态 2、转发消息
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class ChatHandler extends SimpleChannelInboundHandler<ChatMessage> {
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private ChatGroupMemberService chatGroupMemberService;
	@Autowired
	private DiscussionGroupMemberService discussionGroupMemberService;
	@Autowired
	private UserAuthTokenService userAuthTokenService;
	@Autowired
	private FriendsService friendsService;
	@Autowired
	private ChatMessageService chatMessageService;
	// 本地日志记录对象
	private static final Logger LOGGER = Logger.getLogger(ChatHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			ChatMessage message) throws Exception {

		// 客户端认证
		if (sessionManager.clientAuth(message.getFromId() + "",
				message.getToken()) != null) {
			// 先储存消息,只存储客户端发送来的消息，防止转发重复存储消息
			if (message.isTransfer()) {
				message.setType(ChatMessage.TYPE_SEND);
				message = chatMessageService.addChatMessage(message);
				// 发送回执消息
				Msg.Message msg = MsgHelper.newReceiptMessage(
						message.getUuid(), ChatMessage.STATUS_SEND);
				channelHandlerContext.channel().writeAndFlush(msg);
			}

			// 发送消息
			switch (message.getMsgType()) {
			case ChatMessage.MSG_TYPE_UU:
				// 查看本人消息是否被好友屏蔽
				Friends friends = friendsService
						.getFriendsByUserIdAndFriendsUserId(
								message.getFromId(), message.getToId());

				if (friends != null && !friends.isShield()) {
					sessionManager.sendMessage(message);
				}
				break;
			case ChatMessage.MSG_TYPE_UCG:
				List<ChatGroupMember> members = chatGroupMemberService
						.getUnshieldMemberByChatGroupId(message
								.getChatGroupId());
				if (members != null) {
					for (ChatGroupMember member : members) {
						message.setToId(member.getUser().getId());
						if (message.getFromId() != member.getUser().getId()) {
							sessionManager.sendMessage(message);
						}
					}
				}
				break;
			case ChatMessage.MSG_TYPE_UDG:
				List<DiscussionGroupMember> members2 = discussionGroupMemberService
						.getUnshieldMemberByDiscussionGroupId(message
								.getDiscussionGroupId());
				if (members2 != null) {
					for (DiscussionGroupMember member : members2) {
						message.setToId(member.getUser().getId());
						if (message.getFromId() != member.getUser().getId()) {
							sessionManager.sendMessage(message);
						}
					}
				}
				break;
			default:
				break;
			}

		} else {

			Message rtMessage = MsgHelper.newResultMessage(
					Msg.MessageType.AUTH_ERROR, "用户认证失败，重新认证!");

			LOGGER.info("用户认证失败,重新认证！");
			channelHandlerContext.channel().writeAndFlush(rtMessage);
		}

		ReferenceCountUtil.release(message);
	}
}