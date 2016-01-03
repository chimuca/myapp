package com.weishe.weichat.core.bean;

import java.util.List;

import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatMessage;
import com.weishe.weichat.bean.DiscussionGroup;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserOnlineServer;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.Msg.MessageType;
import com.weishe.weichat.util.StringUtils;

public class MsgHelper {
	/**
	 * 生成一个带返回消息的消息
	 * 
	 * @param type
	 * @param message
	 * @return
	 */
	public static Message newResultMessage(Msg.MessageType type, String message) {
		Msg.ResultMessage.Builder builder = Msg.ResultMessage.newBuilder();
		Msg.ResultMessage rtMessage = builder.setMessage(message)
				.setMessageType(type).build();
		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setResultMessage(rtMessage).setMessageType(type)
				.build();
		return m;
	}

	/**
	 * 生成一个带ping/pong消息的消息
	 * 
	 * @param type
	 * @param clientId
	 * @return
	 */
	public static Message newPingMessage(MessageType type, String clientId) {
		Msg.PingMessage.Builder bu = Msg.PingMessage.newBuilder();
		Msg.PingMessage rtMessage = bu.setClientId(clientId)
				.setMessageType(type).build();
		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setPingMessage(rtMessage).setMessageType(type)
				.build();
		return m;
	}

	/**
	 * 用json包装比较难处理的数据
	 * 
	 * @param type
	 * @param jsonStr
	 * @return
	 */
	public static Message newJSonMessage(int type, String jsonStr) {
		Msg.JsonMessage jsonMessage = Msg.JsonMessage.newBuilder()
				.setJsonMessageType(type).setJsonStr(jsonStr).build();

		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setJsonMessage(jsonMessage)
				.setMessageType(Msg.MessageType.JSON_DATA).build();
		return m;
	}

	/**
	 * 聊天消息
	 * 
	 * @param fromId
	 * @param toId
	 * @param content
	 * @param token
	 * @param date
	 * @param contentType
	 * @return
	 */
	public static Message newChatMessage(int fromId, int toId, String content,
			String token, boolean transfer, String date, int id,
			int contentType, String fileGroupName, String path,
			int chatGroupId, int discussionGroupId, int msgType, String uuid,
			int status) {
		Msg.ChatMessage chatMessage = Msg.ChatMessage.newBuilder()
				.setContent(content).setFromId(fromId).setToId(toId)
				.setMsgType(ChatMessage.MSG_TYPE_UU).setToken(token)
				.setChatMessageId(id).setDate(date).setTransfer(transfer)
				.setFileGroupName(fileGroupName).setFilePath(path)
				.setChatGroupId(chatGroupId).setUuid(uuid).setStatus(status)
				.setDiscussionGroupId(discussionGroupId).setMsgType(msgType)
				.setContentType(contentType).build();

		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setChatMessage(chatMessage)
				.setMessageType(Msg.MessageType.CHAT_MESSAGE).build();
		return m;
	}

	public static Message newChatMessage(ChatMessage ms) {

		Attachment a = ms.getAttachment();
		String path = "";
		String fileGroupName = "";
		if (a != null) {
			fileGroupName = a.getGroupName();
			path = a.getPath();
		}
		Msg.ChatMessage chatMessage = Msg.ChatMessage.newBuilder()
				.setContent(ms.getContent()).setFromId(ms.getFromId())
				.setToId(ms.getToId()).setMsgType(ms.getMsgType())
				.setDate(ms.getDate().getTime() + "")
				.setChatMessageId(ms.getId()).setToken(ms.getToken())
				.setChatGroupId(ms.getChatGroupId())
				.setMsgType(ms.getMsgType()).setUuid(ms.getUuid())
				.setStatus(ms.getStatus())
				.setDiscussionGroupId(ms.getDiscussionGroupId())
				.setFileGroupName(fileGroupName).setFilePath(path)
				.setContentType(ms.getContentType()).setTransfer(false).build();
		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setChatMessage(chatMessage)
				.setMessageType(Msg.MessageType.CHAT_MESSAGE).build();
		return m;
	}

	/**
	 * 获取好友信息
	 * 
	 * @param friends
	 * @return
	 */
	public static Message newFriendsListMessage(List<Friends> friends) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (friends != null) {
			int index = 0;
			for (Friends f : friends) {
				Msg.Friends.Builder fb = Msg.Friends.newBuilder();

				fb.setAge(StringUtils.getAage(f.getFriend().getBirthday()));
				if (f.getFriend().getAvatar() != null) {
					fb.setAvatarPath(f.getFriend().getAvatar().getGroupName()
							+ "#" + f.getFriend().getAvatar().getPath());
				} else {
					fb.setAvatarPath("");
				}
				fb.setId(f.getId());
				String name = f.getFriend().getName();

				fb.setName(name);
				if (f.getRemarkName() == null || f.getRemarkName().isEmpty()) {
					fb.setRemarkName(name);
				} else {
					fb.setRemarkName(f.getRemarkName());
				}
				boolean online = false;
				if (f.getOnlineStatus() < UserOnlineServer.ONLINE_STATUS_INVISIBLE) {
					online = true;
				}
				fb.setOnline(online);
				fb.setOnlineType(f.getOnlineType());
				if (f.getFriend().getSignature() == null
						|| f.getFriend().getSignature().isEmpty()) {
					fb.setSignature("好好学习，天天向上。");
				} else {
					fb.setSignature(f.getFriend().getSignature());
				}
				fb.setUserId(f.getFriend().getId());

				if (f.getFriendsGroup() != null) {
					fb.setFriendsGroupId(f.getFriendsGroup().getId());
				} else {
					fb.setFriendsGroupId(0);
				}
				Msg.Friends fm = fb.build();
				b.addFriendsListMessage(index, fb.build());
				index++;
			}
		}

		b.setMessageType(Msg.MessageType.FRIENDS_LIST);
		Msg.Message m = b.build();
		return m;
	}

	/**
	 * 获取好友分组
	 * 
	 * @param friendsGroup
	 * @return
	 */
	public static Message newFriendsGroupListMessage(
			List<FriendsGroup> friendsGroup) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (friendsGroup != null) {
			int index = 0;
			for (FriendsGroup fg : friendsGroup) {
				Msg.FriendsGroup.Builder gb = Msg.FriendsGroup.newBuilder();

				gb.setId(fg.getId());
				gb.setName(fg.getName());
				gb.setPosition(fg.getPosition());
				Msg.FriendsGroup g = gb.build();
				b.addFriendsGroupListMessage(index, g);
				index++;
			}
		}
		b.setMessageType(Msg.MessageType.FRIENDS_GROUP_LIST);
		Msg.Message m = b.build();
		return m;
	}

	public static Message newChatMessageListMessage(List<ChatMessage> messages) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (messages != null) {

			for (ChatMessage ms : messages) {
				Attachment a = ms.getAttachment();
				String path = "";
				String fileGroupName = "";
				if (a != null) {
					fileGroupName = a.getGroupName();
					path = a.getPath();
				}
				Msg.ChatMessage chatMessage = Msg.ChatMessage.newBuilder()
						.setContent(ms.getContent()).setFromId(ms.getFromId())
						.setToId(ms.getToId()).setMsgType(ms.getMsgType())
						.setDate(ms.getDate().getTime() + "")
						.setChatMessageId(ms.getId()).setToken(ms.getToken())
						.setFileGroupName(fileGroupName).setFilePath(path)
						.setContentType(ms.getContentType()).setTransfer(false)
						.setChatGroupId(ms.getChatGroupId())
						.setUuid(ms.getUuid()).setStatus(ms.getStatus())
						.setDiscussionGroupId(ms.getDiscussionGroupId())
						.setMsgType(ms.getMsgType()).build();
				b.addChatMessageListMessage(chatMessage);
			}
		}
		b.setMessageType(Msg.MessageType.CHAT_MESSAGE_LIST);
		Msg.Message m = b.build();
		return m;
	}

	/**
	 * 新建代办事项
	 * 
	 * @param id
	 * @param subject
	 * @param requestMsg
	 * @return
	 */
	public static Message newTodoMessage(int id, String subject, int type,
			String requestMsg) {
		Msg.TodoMessage todoMessage = Msg.TodoMessage.newBuilder()
				.setTodoId(id).setTodoSubject(subject).setType(type)
				.setRequestMsg(requestMsg).build();

		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setTodoMessage(todoMessage)
				.setMessageType(Msg.MessageType.TODO).build();
		return m;
	}

	public static Message newTodoListMessage(List<Todo> messages) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (messages != null) {

			for (Todo ms : messages) {

				String subject = "";
				switch (ms.getType()) {
				case Todo.TODO_TYPE_ADD_FRIENDS:
					subject = "用户[" + ms.getFrom().getName() + "]请求添加您为好友！";
					break;
				case Todo.TODO_TYPE_JOIN_GROUP:
					subject = ms.getFrom().getName() + "请求添加入["
							+ ms.getGroup().getName() + "]群!";
					break;
				default:
					break;
				}

				Msg.TodoMessage todoMessage = Msg.TodoMessage.newBuilder()
						.setTodoId(ms.getId()).setTodoSubject(subject)
						.setType(ms.getType())
						.setRequestMsg(ms.getRequestMsg()).build();
				b.addTodoListMessage(todoMessage);
			}
		}
		b.setMessageType(Msg.MessageType.TODO_MESSAGE_LIST);
		Msg.Message m = b.build();
		return m;
	}

	public static Message newFileDownloadMessage(Attachment file) {
		Msg.FileDownload.Builder bu = Msg.FileDownload.newBuilder();
		Msg.FileDownload f = bu.setGroupName(file.getGroupName())
				.setName(file.getName()).setPath(file.getPath())
				.setSize(file.getSize()).setType(file.getType())
				.setFileId(file.getId()).build();
		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setFileDownload(f).setMessageType(MessageType.FIEL)
				.build();
		return m;
	}

	/**
	 * 聊天群
	 * 
	 * @param chatGroup
	 * @return
	 */
	public static Message newChatGroupListMessage(List<ChatGroup> chatGroup) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (chatGroup != null) {
			int index = 0;
			for (ChatGroup fg : chatGroup) {
				Msg.ChatGroup.Builder gb = Msg.ChatGroup.newBuilder();

				gb.setId(fg.getId());
				gb.setName(fg.getName());
				gb.setSlogan(fg.getSlogan() + "");
				Msg.ChatGroup g = gb.build();
				b.addChatGroupListMessage(index, g);
				index++;
			}
		}
		b.setMessageType(Msg.MessageType.CHAT_GROUP_LIST);
		Msg.Message m = b.build();
		return m;
	}

	/**
	 * 讨论组
	 * 
	 * @param chatGroup
	 * @return
	 */
	public static Message newDiscussionGroupListMessage(
			List<DiscussionGroup> dGroup) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (dGroup != null) {
			int index = 0;
			for (DiscussionGroup fg : dGroup) {
				Msg.DiscussionGroup.Builder gb = Msg.DiscussionGroup
						.newBuilder();
				gb.setId(fg.getId());
				gb.setName(fg.getName());
				Msg.DiscussionGroup g = gb.build();
				b.addDiscussionGroupListMessage(index, g);
				index++;
			}
		}
		b.setMessageType(Msg.MessageType.DISCUSSION_GROUP_LIST);
		Msg.Message m = b.build();
		return m;
	}

	public static Message newUserListMessage(List<User> users) {
		Msg.Message.Builder b = Msg.Message.newBuilder();
		if (users != null) {
			int index = 0;
			for (User u : users) {
				Msg.User.Builder gb = Msg.User.newBuilder();
				gb.setId(u.getId());
				gb.setName(u.getName());
				gb.setAccount(u.getAccount());
				if (u.getAvatar() != null) {
					gb.setAvatarPath(u.getAvatar().getGroupName() + "#"
							+ u.getAvatar().getPath());
				} else {
					gb.setAvatarPath("");
				}
				gb.setBirthday(StringUtils.getDateString(u.getBirthday()));
				gb.setGender(u.getGender());
				gb.setSignature(u.getSignature() + "");
				Msg.User g = gb.build();
				b.addUserListMessage(index, g);
				index++;
			}
		}
		b.setMessageType(Msg.MessageType.USER_LIST);
		Msg.Message m = b.build();
		return m;
	}

	public static Message newReceiptMessage(String uuid, int status) {
		Msg.Message.Builder b = Msg.Message.newBuilder();

		Msg.ReceiptMessage.Builder rb = Msg.ReceiptMessage.newBuilder();
		rb.setStatus(status).setUuid(uuid);

		Msg.ReceiptMessage rm = rb.build();

		b.setMessageType(Msg.MessageType.RECEIPT);
		b.setReceiptMessage(rm);
		Msg.Message m = b.build();
		return m;
	}

	public static Message newTodoMessage(Todo todo) {
		Msg.TodoMessage todoMessage = Msg.TodoMessage.newBuilder()
				.setTodoId(todo.getId()).setTodoSubject(todo.getRequestMsg())
				.setType(todo.getType()).setRequestMsg(todo.getRequestMsg())
				.build();

		Msg.Message.Builder b = Msg.Message.newBuilder();
		Msg.Message m = b.setTodoMessage(todoMessage)
				.setMessageType(Msg.MessageType.TODO).build();
		return m;
	}
}
