package com.weishe.weichat.core.nio.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.DiscussionGroup;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.ClientRequestMessage;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.service.ChatGroupMemberService;
import com.weishe.weichat.service.ChatGroupService;
import com.weishe.weichat.service.ChatMessageService;
import com.weishe.weichat.service.DiscussionGroupMemberService;
import com.weishe.weichat.service.FriendsGroupService;
import com.weishe.weichat.service.FriendsService;
import com.weishe.weichat.service.TodoService;
import com.weishe.weichat.service.UserService;
import com.weishe.weichat.util.StringUtils;

/**
 * 1、保持服务器间的心态 2、转发消息
 * 
 * @author chenbiao
 *
 */
@Sharable
@Service
public class ClientRequesHandler extends
		SimpleChannelInboundHandler<ClientRequestMessage> {
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private FriendsService friendsService;
	@Autowired
	private ChatMessageService chatMessageService;
	@Autowired
	private FriendsGroupService friendsGroupService;
	@Autowired
	private ChatGroupService chatGroupService;
	@Autowired
	private TodoService todoService;
	@Autowired
	private ChatGroupMemberService chatGroupMemberService;
	@Autowired
	private DiscussionGroupMemberService discussionGroupMemberService;

	@Autowired
	private UserService userService;
	// 本地日志记录对象
	private static final Logger LOGGER = Logger
			.getLogger(ClientRequesHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			ClientRequestMessage message) throws Exception {

		// 客户端认证
		if (sessionManager.clientAuth(message.getUserId() + "",
				message.getToken()) != null) {
			// 处理特定的消息
			String sessionKey = message.getUserId() + message.getToken();
			switch (message.getRequestType()) {
			case ClientRequestMessage.FRIEND_LIST:
				Message msg = getFriendsList(message);

				sessionManager.sendMessage(msg, sessionKey);
				break;
			case ClientRequestMessage.FRIEND_GROUP_LIST:
				// 获取好友分组列表
				List<FriendsGroup> fg = friendsGroupService
						.getFriendsGroupByUserId(message.getUserId());

				Message mfg = MsgHelper.newFriendsGroupListMessage(fg);
				sessionManager.sendMessage(mfg, sessionKey);
				break;
			case ClientRequestMessage.CHAT_MESSAGE_LIST:
				String p = message.getParameter();
				int fromMessageId = StringUtils.toInt(p);

				List<ChatMessage> ms = chatMessageService
						.getAllChatMessageByToId(message.getUserId(),
								fromMessageId);
				if (ms != null) {
					for (ChatMessage m : ms) {
						// 重置token
						m.setToken("");
					}
				}
				Message cmg = MsgHelper.newChatMessageListMessage(ms);
				sessionManager.sendMessage(cmg, sessionKey);
				break;
			case ClientRequestMessage.TODO_LIST:
				String p2 = message.getParameter();
				int fid = StringUtils.toInt(p2);

				List<Todo> td = todoService.getAllTodoByToId(
						message.getUserId(), fid);
				Message tmg = MsgHelper.newTodoListMessage(td);
				sessionManager.sendMessage(tmg, sessionKey);
				break;

			case ClientRequestMessage.CHAT_GROUP_LIST:

				List<ChatGroup> cg = chatGroupMemberService
						.getChatGroupByMember(message.getUserId());
				Message tcg = MsgHelper.newChatGroupListMessage(cg);
				sessionManager.sendMessage(tcg, sessionKey);
				break;
			case ClientRequestMessage.CHAT_GROUP_MEMBER_LIST:
				List<DiscussionGroup> dg = discussionGroupMemberService
						.getDiscussionGroupByMemberId(message.getUserId());
				Message tdg = MsgHelper.newDiscussionGroupListMessage(dg);
				sessionManager.sendMessage(tdg, sessionKey);
				break;
			case ClientRequestMessage.DISCUSSION_GROUP_LIST:

				break;
			case ClientRequestMessage.DISCUSSION_GROUP_MEMBER_LIST:

				break;
			case ClientRequestMessage.RELATE_USER_LIST:
				List<User> users = userService.getRelateUser(message
						.getUserId());
				Message ul = MsgHelper.newUserListMessage(users);
				sessionManager.sendMessage(ul, sessionKey);
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

	private Message getFriendsList(ClientRequestMessage message) {
		// 获取用户的好友列表
		List<Friends> friends = friendsService.getFriendsByUserId(message
				.getUserId());
		// 设置好友的在线状态
		for (Friends u : friends) {
			u.setOnlineStatus(friendsService.getFriendsOnlineStatus(
					message.getUserId(), u.getFriend().getId()));
		}
		return MsgHelper.newFriendsListMessage(friends);
	}
}