package org.weishe.weichat.activity;

import java.util.Date;
import java.util.List;

import org.weishe.weichat.R;
import org.weishe.weichat.adapter.ChatMessageAdapter;
import org.weishe.weichat.SessionService;
import org.weishe.weichat.base.BaseActivity;
import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.bean.ChatGroup;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.DiscussionGroup;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.core.bean.ChatMessage;
import org.weishe.weichat.emoji.KJEmojiFragment;
import org.weishe.weichat.emoji.OnSendClickListener;
import org.weishe.weichat.fragment.VoiceFragment;
import org.weishe.weichat.util.DBHelper;
import org.weishe.weichat.util.UIHelper;
import org.weishe.weichat.util.UUIDUtil;
import org.weishe.weichat.view.VoiceButton.OnSendVoiceListener;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends BaseActivity implements OnSendClickListener,
		OnCheckedChangeListener, OnSendVoiceListener, OnClickListener {
	public final static int CURRENT_INPUT_TYPE_KEYBOARD = 0;// 键盘文字输入
	public final static int CURRENT_INPUT_TYPE_VOICE = 1;// 语音输入

	private BroadcastReceiver receiver;
	private SessionService mSessionService;

	private User user;
	int userId;
	int chatWithId = 0;

	private Friends friend;
	private ChatGroup chatGroup;
	private DiscussionGroup discussionGroup;

	private int chatType;
	private Button groupInforButton;

	private ListView chatMeessageListView;
	private ChatMessageAdapter chatMessageAdapter;

	private int currentInputType;
	private List<ChatMessage> chatList;

	private KJEmojiFragment emojiFragment = new KJEmojiFragment();

	private VoiceFragment voiceFragment = new VoiceFragment();

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSessionService = SessionService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mSessionService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		groupInforButton = (Button) this.findViewById(R.id.group_infor);
		Intent intent = getIntent();
		user = (User) CacheManager.readObject(ChatActivity.this,
				Constants.CACHE_CURRENT_USER);

		chatType = intent.getIntExtra(Constants.INTENT_EXTRA_CHAT_TYPE, 0);
		switch (chatType) {
		case ChatMessage.MSG_TYPE_UU:
			friend = (Friends) intent
					.getSerializableExtra(Constants.INTENT_EXTRA_CHAT_FRIEND);
			break;
		case ChatMessage.MSG_TYPE_UCG:

			chatGroup = (ChatGroup) intent
					.getSerializableExtra(Constants.INTENT_EXTRA_CHAT_CHAT_GROUP);
			groupInforButton.setVisibility(View.VISIBLE);
			groupInforButton.setOnClickListener(this);
			break;
		case ChatMessage.MSG_TYPE_UDG:
			groupInforButton.setVisibility(View.VISIBLE);
			discussionGroup = (DiscussionGroup) intent
					.getSerializableExtra(Constants.INTENT_EXTRA_CHAT_DISCUSSION_GROUP);
			groupInforButton.setVisibility(View.VISIBLE);
			groupInforButton.setOnClickListener(this);
			break;
		}

		initViews();
		initEvents();

		Intent i = new Intent(Constants.INTENT_SERVICE_SESSION);

		i.setAction(Constants.INTENT_SERVICE_SESSION);

		i.setPackage("org.weishe.weichat");
		this.bindService(i, connection, Context.BIND_ADJUST_WITH_ACTIVITY);

		// 注册监听消息
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String key = intent.getAction();
				switch (key) {
				case Constants.INTENT_ACTION_RECEIVE_CHAT_MESSAGE:
					ChatMessage chatMessage = (ChatMessage) intent
							.getSerializableExtra(Constants.INTENT_EXTRA_CHAT_MESSAGE);

					chatList = DBHelper.getgetInstance(ChatActivity.this)
							.getChatMessageByPage(userId, chatWithId, chatType,
									200);
					chatMessageAdapter.setData(chatList);
					chatMessageAdapter.notifyDataSetChanged();
					chatMeessageListView.setSelection(chatList.size());
					// 更新消息为已读
					DBHelper.getgetInstance(ChatActivity.this)
							.updateChatMessageChecked(userId,
									chatMessage.getFromId());

					break;
				case Constants.INTENT_ACTION_RECEIVE_RECEIPT_MESSAGE:
					chatList = DBHelper.getgetInstance(ChatActivity.this)
							.getChatMessageByPage(userId, chatWithId, chatType,
									200);
					chatMessageAdapter.setData(chatList);
					chatMessageAdapter.notifyDataSetChanged();
					chatMeessageListView.setSelection(chatList.size());
				case Constants.INTENT_ACTION_VOICE_MSG_DOWLOAD:
					chatList = DBHelper.getgetInstance(ChatActivity.this)
							.getChatMessageByPage(userId, chatWithId, chatType,
									200);
					chatMessageAdapter.setData(chatList);
					chatMessageAdapter.notifyDataSetChanged();
					chatMeessageListView.setSelection(chatList.size());
					break;
				default:
					;
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.INTENT_ACTION_RECEIVE_CHAT_MESSAGE);
		intentFilter.addAction(Constants.INTENT_ACTION_RECEIVE_RECEIPT_MESSAGE);
		intentFilter.addAction(Constants.INTENT_ACTION_VOICE_MSG_DOWLOAD);
		this.registerReceiver(receiver, intentFilter);
		emojiFragment.setOnSendClickListener(this);
		voiceFragment.setOnSendClickListener(this);
		voiceFragment.setOnSendVoiceListener(this);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.emoji_keyboard, emojiFragment).commit();
		currentInputType = CURRENT_INPUT_TYPE_KEYBOARD;

	}

	@Override
	public void onClickSendButton(Editable text) {

		String content = text.toString();
		// 空消息不发送，不包括空格。
		if (content == null || content.isEmpty()) {
			return;
		}

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setContent(content);

		chatMessage.setFromId(user.getId());
		chatMessage.setDate(new Date());
		chatMessage.setType(ChatMessage.TYPE_SEND);
		chatMessage.setContentType(ChatMessage.CONTENT_TYPE_NORMAL);
		int toId = 0;

		switch (chatType) {
		case ChatMessage.MSG_TYPE_UU:
			chatMessage.setMsgType(ChatMessage.MSG_TYPE_UU);
			chatMessage.setToId(friend.getUserId());
			toId = friend.getUserId();
			break;
		case ChatMessage.MSG_TYPE_UCG:
			chatMessage.setMsgType(ChatMessage.MSG_TYPE_UCG);
			chatMessage.setChatGroupId(chatGroup.getId());
			toId = chatGroup.getId();
			break;
		case ChatMessage.MSG_TYPE_UDG:
			chatMessage.setMsgType(ChatMessage.MSG_TYPE_UDG);
			chatMessage.setDiscussionGroupId(discussionGroup.getId());
			toId = discussionGroup.getId();
			break;
		}

		chatMessage.setWhoId(user.getId());
		chatMessage.setChecked(true);
		String uuid = UUIDUtil.uuid();
		chatMessage.setUuid(uuid);
		DBHelper.getgetInstance(ChatActivity.this).addChatMessage(chatMessage,
				user.getId());

		try {

			mSessionService.sendMessage(uuid, ChatMessage.CONTENT_TYPE_NORMAL,
					content, toId, chatType, "", "");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (isMyMessage(chatMessage)) {
			if (!compareTo(chatList, chatMessage)) {
				chatList.add(chatMessage);
			}
		}
		chatMessageAdapter.notifyDataSetChanged();
		chatMeessageListView.setSelection(chatList.size());
	}

	@Override
	public void onClickFlagButton() {
		switch (currentInputType) {
		case CURRENT_INPUT_TYPE_KEYBOARD:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.emoji_keyboard, voiceFragment).commit();
			currentInputType = CURRENT_INPUT_TYPE_VOICE;
			break;
		case CURRENT_INPUT_TYPE_VOICE:
			emojiFragment.setOnSendClickListener(this);
			// 修复再次替换时表情不显示bug
			emojiFragment.setAdapter(getSupportFragmentManager());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.emoji_keyboard, emojiFragment).commit();
			currentInputType = CURRENT_INPUT_TYPE_KEYBOARD;
			chatMeessageListView.setSelection(chatList.size());
			break;
		default:
			break;
		}
	}

	@Override
	protected void initViews() {
		TextView title = (TextView) findViewById(R.id.title_bar);

		switch (chatType) {
		case ChatMessage.MSG_TYPE_UU:
			title.setText(friend.getName());
			break;
		case ChatMessage.MSG_TYPE_UCG:
			title.setText(chatGroup.getName());
			break;
		case ChatMessage.MSG_TYPE_UDG:
			title.setText(discussionGroup.getName());
			break;
		}
		chatMeessageListView = (ListView) findViewById(R.id.chat_Listview);
	}

	@Override
	protected void initEvents() {

		userId = user.getId();
		chatWithId = 0;
		switch (chatType) {
		case ChatMessage.MSG_TYPE_UU:
			chatWithId = friend.getUserId();
			break;
		case ChatMessage.MSG_TYPE_UCG:
			chatWithId = chatGroup.getId();
			break;
		case ChatMessage.MSG_TYPE_UDG:
			chatWithId = discussionGroup.getId();
			break;
		}
		chatList = DBHelper.getgetInstance(this).getChatMessageByPage(userId,
				chatWithId, chatType, 200);

		chatMessageAdapter = new ChatMessageAdapter(ChatActivity.this, chatList);
		chatMeessageListView.setAdapter(chatMessageAdapter);
		chatMeessageListView.setSelection(chatList.size());
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver);
		this.unbindService(connection);
		super.onDestroy();

	}

	/**
	 * 是这个activity该接受的消息
	 * 
	 * @param message
	 * @return
	 */
	private boolean isMyMessage(ChatMessage message) {
		int toId = 0;
		if (message.getMsgType() != chatType) {
			return false;
		}
		try {
			switch (chatType) {
			case ChatMessage.MSG_TYPE_UU:
				if ((message.getToId() == mSessionService.getUserId()
						&& message.getType() == ChatMessage.TYPE_RECEIVE && message
						.getFromId() == friend.getUserId())
						|| (message.getFromId() == mSessionService.getUserId()
								&& message.getType() == ChatMessage.TYPE_SEND && message
								.getToId() == friend.getUserId())) {
					return true;
				}
				break;
			case ChatMessage.MSG_TYPE_UCG:
				if ((message.getToId() == mSessionService.getUserId()
						&& message.getType() == ChatMessage.TYPE_RECEIVE && message
						.getChatGroupId() == chatGroup.getId())
						|| (message.getFromId() == mSessionService.getUserId()
								&& message.getType() == ChatMessage.TYPE_SEND && message
								.getChatGroupId() == chatGroup.getId())) {
					return true;
				}
				break;
			case ChatMessage.MSG_TYPE_UDG:
				if ((message.getToId() == mSessionService.getUserId()
						&& message.getType() == ChatMessage.TYPE_RECEIVE && message
						.getDiscussionGroupId() == discussionGroup.getId())
						|| (message.getFromId() == mSessionService.getUserId()
								&& message.getType() == ChatMessage.TYPE_SEND && message
								.getDiscussionGroupId() == discussionGroup
								.getId())) {
					return true;
				}
				break;
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected boolean compareTo(List<ChatMessage> data, ChatMessage enity) {
		int s = data.size();
		if (enity != null) {
			for (int i = 0; i < s; i++) {
				if (enity.getUuid().equals(data.get(i).getUuid())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	@Override
	public void onSend(Attachment a) {
		try {
			mSessionService.sendAttachment(a.getId());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		ChatMessage chatMessage = new ChatMessage();
		// / a = DBHelper.getgetInstance(ChatActivity.this).addAttachment(a);

		chatMessage.setContent("");

		chatMessage.setFromId(user.getId());
		int toId = 0;

		switch (chatType) {
		case ChatMessage.MSG_TYPE_UU:
			chatMessage.setMsgType(ChatMessage.MSG_TYPE_UU);
			chatMessage.setToId(friend.getUserId());
			toId = friend.getUserId();
			break;
		case ChatMessage.MSG_TYPE_UCG:
			chatMessage.setMsgType(ChatMessage.MSG_TYPE_UCG);
			chatMessage.setChatGroupId(chatGroup.getId());
			toId = chatGroup.getId();
			break;
		case ChatMessage.MSG_TYPE_UDG:
			chatMessage.setMsgType(ChatMessage.MSG_TYPE_UDG);
			chatMessage.setDiscussionGroupId(discussionGroup.getId());
			toId = discussionGroup.getId();
			break;
		}

		chatMessage.setDate(new Date());
		chatMessage.setType(ChatMessage.TYPE_SEND);
		chatMessage.setWhoId(user.getId());
		chatMessage.setContentType(ChatMessage.CONTENT_TYPE_ATTACHMENT);
		chatMessage.setChecked(true);
		chatMessage.setFileGroupName(a.getGroupName());
		chatMessage.setFilePath(a.getPath());
		chatMessage.setAttachmentId(a.getId());

		String uuid = UUIDUtil.uuid();
		chatMessage.setUuid(uuid);
		DBHelper.getgetInstance(ChatActivity.this).addChatMessage(chatMessage,
				user.getId());

		try {
			mSessionService.sendMessage(uuid,
					ChatMessage.CONTENT_TYPE_ATTACHMENT, "", toId, chatType,
					a.getGroupName(), a.getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (isMyMessage(chatMessage)) {
			if (!compareTo(chatList, chatMessage)) {
				chatList.add(chatMessage);
			}
		}
		chatMessageAdapter.notifyDataSetChanged();
		chatMeessageListView.setSelection(chatList.size());
	}

	public SessionService getSessionService() {
		return mSessionService;
	}

	@Override
	public void onClick(View v) {
		switch (chatType) {
		case ChatMessage.MSG_TYPE_UCG:
			UIHelper.startChatGroupInforActivity(this, chatGroup.getId(),
					Constants.INTENT_EXTRA_CHATGROUP_INFOR_TYPE_LEAVECHATGROUP);
			break;
		case ChatMessage.MSG_TYPE_UDG:

			break;
		}

	}
}
