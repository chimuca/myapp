package org.weishe.weichat.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.core.bean.ChatMessage;
import org.weishe.weichat.core.bean.ClientRequestMessage;
import org.weishe.weichat.core.bean.JsonMessage;
import org.weishe.weichat.util.DBHelper;

import android.content.Intent;

import com.alibaba.fastjson.JSON;

@Sharable
public class JsonDataHandler extends SimpleChannelInboundHandler<JsonMessage> {
	private Session session;

	public JsonDataHandler(Session session) {
		this.session = session;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			JsonMessage jsonMessage) throws Exception {

		switch (jsonMessage.getJsonMessageType()) {
		case ClientRequestMessage.CHAT_MESSAGE_LIST:
			// 存储消息到本地
			List<ChatMessage> list = JSON.parseArray(jsonMessage.getJsonStr(),
					ChatMessage.class);
			if (list != null && list.size() > 0) {
				for (ChatMessage m : list) {
					m.setChecked(false);
					m.setWhoId(session.getUser().getId());
					DBHelper.getgetInstance(session.getApplicationContext())
							.addChatMessage(m, session.getUser().getId());
				}
			}
			// 广播事件
			Intent intent0 = new Intent();
			intent0.setAction(Constants.INTENT_ACTION_RECEIVE_CHAT_MESSAGE_LIST);
			session.sendBroadcast(intent0);
			break;
		case ClientRequestMessage.FRIEND_LIST:
			// 广播事件
			Intent intent = new Intent();
			intent.setAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_LIST);
			intent.putExtra(Constants.INTENT_EXTRA_FRIEND_LIST,
					jsonMessage.getJsonStr());
			List<Friends> friends = JSON.parseArray(jsonMessage.getJsonStr(),
					Friends.class);

			session.setFriends(friends);
			session.sendBroadcast(intent);
			break;
		default:
			break;
		}

		ReferenceCountUtil.release(jsonMessage);
	}
}