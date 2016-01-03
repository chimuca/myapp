package org.weishe.weichat.service;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.bean.ChatGroup;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.DiscussionGroup;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.Todo;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.core.bean.ChatMessage;
import org.weishe.weichat.core.bean.Msg;
import org.weishe.weichat.util.BroadcastHelper;
import org.weishe.weichat.util.DBHelper;
import org.weishe.weichat.util.StringUtils;

import android.content.Intent;

@Sharable
public class ListMessageHandler extends SimpleChannelInboundHandler<List> {
	private Session session;

	public ListMessageHandler(Session session) {
		this.session = session;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			List messages) throws Exception {
		//
		if (messages != null && messages.size() > 0) {
			Object o = messages.get(0);
			// 聊天记录
			if (o instanceof Msg.ChatMessage) {
				onreciveChatMessageList(messages);
			}
			// 好友列表
			if (o instanceof Msg.Friends) {
				onReceiveFriendsList(messages);
			}
			// 好友分组列表
			if (o instanceof Msg.FriendsGroup) {
				onReceiveFriendsGroupList(messages);
			}
			// 代办列表
			if (o instanceof Msg.TodoMessage) {
				onReceiveTodoMessageList(messages);
			}

			// 群
			if (o instanceof Msg.ChatGroup) {
				onReceiveChatGroupMessageList(messages);
			}
			// 讨论组
			if (o instanceof Msg.DiscussionGroup) {
				onReceiveDiscussionGroupMessageList(messages);
			}
			// 用户
			if (o instanceof Msg.User) {
				onReceiveUserMessageList(messages);
			}
		}

		ReferenceCountUtil.release(messages);
	}

	private void onReceiveUserMessageList(List<Msg.User> messages) {
		if (messages != null && messages.size() > 0) {
			for (Msg.User u : messages) {
				User user = new User();
				user.setAccount(u.getAccount());
				user.setAvatar(u.getAvatarPath());
				user.setGender(u.getGender());
				user.setId(u.getId());
				user.setName(u.getName());
				user.setSignature(u.getSignature());
				CacheManager.saveObject(session, user,
						User.getCacheKey(user.getId()));
			}
		}
	}

	private void onreciveChatMessageList(List<Msg.ChatMessage> messages) {

		// 存储消息到本地
		if (messages != null && messages.size() > 0) {
			for (Msg.ChatMessage m : messages) {
				ChatMessage msg = new ChatMessage();
				msg.setChecked(false);
				msg.setWhoId(session.getUser().getId());
				msg.setChatGroupId(m.getChatGroupId());
				msg.setContent(m.getContent());
				Date date = new Date(StringUtils.toLong(m.getDate()));
				msg.setDate(date);
				msg.setChatMessageId(m.getChatMessageId());
				// 获取的消息都是接收消息
				msg.setType(ChatMessage.TYPE_RECEIVE);
				msg.setMsgType(msg.getMsgType());
				msg.setFromId(m.getFromId());
				msg.setToId(m.getToId());
				msg.setContentType(m.getContentType());
				msg.setFileGroupName(m.getFileGroupName());
				msg.setFilePath(m.getFilePath());
				msg.setWhoId(session.getUser().getId());
				msg.setUuid(m.getUuid());
				msg.setStatus(m.getStatus());
				// 如果是带附件的消息
				if (m.getContentType() == ChatMessage.CONTENT_TYPE_ATTACHMENT) {
					String groupName = m.getFileGroupName();
					String path = m.getFilePath();
					Attachment a = DBHelper.getgetInstance(session)
							.getAttachment(groupName, path);
					if (a != null) {
						msg.setAttachmentId(a.getId());
					}
				}
				DBHelper.getgetInstance(session.getApplicationContext())
						.addChatMessage(msg, session.getUser().getId());
			}
		}
		// 广播事件
		BroadcastHelper.onReceiveChatMessageList(session);

	}

	private void onReceiveFriendsList(List<Msg.Friends> friends) {
		List<Friends> _fl = new ArrayList<Friends>();
		if (friends != null && friends.size() > 0) {
			for (Msg.Friends f : friends) {
				Friends _f = new Friends();
				_f.setAge(f.getAge());
				_f.setAvatarPath(f.getAvatarPath());
				_f.setFriendsGroupId(f.getFriendsGroupId());
				_f.setGender(f.getGender());
				_f.setId(f.getId());
				_f.setName(f.getName());
				_f.setOnline(f.getOnline());
				_f.setOnlineType(f.getOnlineType());
				_f.setRemarkName(f.getRemarkName());
				_f.setSignature(f.getSignature());
				_f.setUserId(f.getUserId());
				_fl.add(_f);
			}
		}
		// 将对象写入本地
		CacheManager.saveObject(session, _fl,
				Friends.getCacheKey(session.getUser().getId()));
		// 发出通知

		// 广播事件
		Intent intent0 = new Intent();
		intent0.setAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_LIST);
		session.sendBroadcast(intent0);
	}

	private void onReceiveFriendsGroupList(List<Msg.FriendsGroup> friendsGroup) {
		List<FriendsGroup> fg = new ArrayList<FriendsGroup>();
		if (friendsGroup != null && friendsGroup.size() > 0) {
			for (Msg.FriendsGroup f : friendsGroup) {
				FriendsGroup g = new FriendsGroup();
				g.setId(f.getId());
				g.setName(f.getName());
				g.setPosition(f.getPosition());
				fg.add(g);
			}
		}
		// 将对象写入本地
		CacheManager.saveObject(session, fg,
				FriendsGroup.getCacheKey(session.getUser().getId()));
		// 发出通知
		// 广播事件
		Intent intent0 = new Intent();
		intent0.setAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_GROUP_LIST);
		session.sendBroadcast(intent0);
	}

	private void onReceiveChatGroupMessageList(List<Msg.ChatGroup> chatGroup) {
		if (chatGroup != null && chatGroup.size() > 0) {
			ArrayList<ChatGroup> list = new ArrayList<ChatGroup>();
			for (Msg.ChatGroup c : chatGroup) {
				ChatGroup cg = new ChatGroup();
				cg.setName(c.getName());
				// cg.setAvatarPath(avatarPath);
				cg.setId(c.getId());
				cg.setSlogan(c.getSlogan());
				list.add(cg);
			}
			// 将数据写入缓存
			CacheManager.saveObject(session, list,
					ChatGroup.getCacheKey(session.getUser().getId()));
		}
	}

	private void onReceiveDiscussionGroupMessageList(
			List<Msg.DiscussionGroup> dGroup) {
		if (dGroup != null && dGroup.size() > 0) {
			ArrayList<DiscussionGroup> list = new ArrayList<DiscussionGroup>();
			for (Msg.DiscussionGroup c : dGroup) {
				DiscussionGroup cg = new DiscussionGroup();
				cg.setId(c.getId());
				cg.setName(c.getName());
				list.add(cg);
			}
			// 将数据写入缓存
			CacheManager.saveObject(session, list,
					ChatGroup.getCacheKey(session.getUser().getId()));
		}
	}

	private void onReceiveTodoMessageList(List<Msg.TodoMessage> todoList) {
		boolean flag = false;
		// 先将数据存入本地
		List<Todo> list = new ArrayList<Todo>();
		if (todoList != null && todoList.size() > 0) {
			for (Msg.TodoMessage m : todoList) {

				Todo todo = new Todo();
				todo.setCreateDate(new Date());
				todo.setChecked(false);
				todo.setWhoId(session.getUser().getId());
				todo.setRequestMsg(m.getRequestMsg());
				todo.setTodoSubject(m.getTodoSubject());
				// 对于我来说所有的都是接收消息
				todo.setType(m.getType());
				todo.setTodoId(m.getTodoId());
				todo.setWhoId(session.getUser().getId());
				Todo old = DBHelper.getgetInstance(
						session.getApplicationContext()).getTodoByTodoId(
						m.getTodoId(), session.getUser().getId());
				if (old == null || old.getId() <= 0) {
					DBHelper.getgetInstance(session.getApplicationContext())
							.addTodo(todo);
					flag = true;
				}
			}
		}
		// 然后发出通知
		if (flag) {
			// 广播事件
			BroadcastHelper.onReceiveChatMessageList(session);
		}
	}
}