package org.weishe.weichat.adapter;

import java.util.List;

import org.weishe.weichat.R;
import org.weishe.weichat.activity.MainActivity;
import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.Todo;
import org.weishe.weichat.core.bean.ChatMessage;
import org.weishe.weichat.emoji.InputHelper;
import org.weishe.weichat.util.DBHelper;
import org.weishe.weichat.util.StringUtils;
import org.weishe.weichat.util.UIHelper;
import org.weishe.weichat.view.CircularImage;
import org.weishe.weichat.view.TweetTextView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageListAdapter_back extends RecyclerViewAdapter {

	public static final int TYPE_TODO = 0;
	public static final int TYPE_CHATMESSAGE = 1;
	public static final int TYPE_TYPE_SEPARATOR = 2;

	private MainActivity mActivity;

	private float x, ux;
	private Button curDel_btn;

	public MessageListAdapter_back(MainActivity activity) {
		mActivity = activity;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		List list = this.getData();
		ChatMessage message = null;
		Todo todo = null;
		if (list.get(position) instanceof ChatMessage) {
			message = (ChatMessage) list.get(position);
		} else if (list.get(position) instanceof Todo) {
			todo = (Todo) list.get(position);
		}

		// 代办消息
		if (todo != null) {
			TodoHolder td = (TodoHolder) viewHolder;
			td.subjectView.setText(todo.getTodoSubject());
			td.requestMsgView.setText(todo.getRequestMsg());
			td.agreeBtn.setTag(todo.getTodoId());
		}
		// 聊天消息
		if (message != null) {
			MessageHolder vh = (MessageHolder) viewHolder;

			vh.avatarView.setImageResource(R.drawable.channel_qq);
			String name = "";
			if (mActivity.getFriends() != null) {
				for (Friends friends : mActivity.getFriends()) {
					switch (message.getType()) {
					case ChatMessage.TYPE_RECEIVE:
						if (friends.getUserId() == message.getFromId()) {
							name = friends.getName();
							if (friends.getAvatarPath() != null
									&& !friends.getAvatarPath().isEmpty()) {
								String[] str = friends.getAvatarPath().split(
										"#");
								if (str.length == 2) {
									vh.avatarView.setImage(str[0], str[1]);
								}
							}
						}
						break;
					case ChatMessage.TYPE_SEND:
						if (friends.getUserId() == message.getToId()) {
							name = friends.getName();
							if (friends.getAvatarPath() != null
									&& !friends.getAvatarPath().isEmpty()) {
								String[] str = friends.getAvatarPath().split(
										"#");
								if (str.length == 2) {
									vh.avatarView.setImage(str[0], str[1]);
								}
							}
						}
						break;
					default:
						break;
					}
				}
				vh.nameView.setText(name);
				vh.dateView
						.setText(StringUtils.friendly_time(message.getDate()));
				if (message.getContentType() == ChatMessage.CONTENT_TYPE_ATTACHMENT) {
					Attachment a = DBHelper.getgetInstance(mActivity)
							.getAttachment(message.getFileGroupName(),
									message.getFilePath());
					if (a != null) {
						switch (a.getType()) {
						case Attachment.TYPE_VIDEO:
							vh.messageView.setText("[视频]");
							break;
						case Attachment.TYPE_VOICE:
							vh.messageView.setText("[语音]");
							break;
						default:
							vh.messageView.setText("[文件]");
							break;
						}

					}
				} else {

					Spanned span = Html.fromHtml(TweetTextView
							.modifyPath(message.getContent()));
					span = InputHelper.displayEmoji(mActivity.getResources(),
							span.toString());

					vh.messageView.setText(span);
				}
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		List list = this.getData();
		ChatMessage message = null;
		Todo todo = null;
		if (list.get(position) instanceof ChatMessage) {
			return TYPE_CHATMESSAGE;
		} else if (list.get(position) instanceof Todo) {
			return TYPE_TODO;
		}
		return -1;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		RecyclerView v = (RecyclerView) viewGroup;
		View view = null;
		switch (viewType) {
		case TYPE_CHATMESSAGE:
			// 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
			view = View.inflate(viewGroup.getContext(),
					R.layout.message_list_item, null);
			// 创建一个ViewHolder
			MessageHolder holder = new MessageHolder(view);
			return holder;
		case TYPE_TODO:
			// 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
			view = View.inflate(viewGroup.getContext(), R.layout.todo_item,
					null);
			// 创建一个ViewHolder
			TodoHolder todoHolder = new TodoHolder(view);
			return todoHolder;

		}
		return null;

	}

	class TodoHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.user_photo)
		CircularImage avatarView;
		@InjectView(R.id.subject)
		TextView subjectView;
		@InjectView(R.id.request_msg)
		TextView requestMsgView;

		//@InjectView(R.id.agree_btn)
		Button agreeBtn;

		// @InjectView(R.id.delete_btn)
		// Button btnDel;

		public TodoHolder(View itemView) {
			super(itemView);
			ButterKnife.inject(this, itemView);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			agreeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = (int) agreeBtn.getTag();
					// /WeisheApi.addFriends(handler, userId, token, friendsId);
					Log.v("org.weishe.weichat", "todoId:" + id);
				}
			});

			// 为每一个view项设置触控监听
			itemView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {

					// final ViewHolder holder = (ViewHolder) v.getTag();

					// 当按下时处理
					if (event.getAction() == MotionEvent.ACTION_DOWN) {

						// //设置背景为选中状态
						// v.setBackgroundResource(R.drawable.mm_listitem_pressed);
						// 获取按下时的x轴坐标
						x = event.getX();
						// 判断之前是否出现了删除按钮如果存在就隐藏
						if (curDel_btn != null) {
							if (curDel_btn.getVisibility() == View.VISIBLE) {
								curDel_btn.setVisibility(View.GONE);
								return true;
							}
						}

					} else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开处理

						// 设置背景为未选中正常状态
						// v.setBackgroundResource(R.drawable.mm_listitem_simple);
						// 获取松开时的x坐标
						ux = event.getX();

						// 判断当前项中按钮控件不为空时
						// if (btnDel != null) {
						//
						// // 按下和松开绝对值差当大于20时显示删除按钮，否则不显示
						//
						// if (Math.abs(x - ux) > 20) {
						// btnDel.setVisibility(View.VISIBLE);
						// curDel_btn = btnDel;
						// return true;
						// }
						// }
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 当滑动时背景为选中状态
						return true;
						// v.setBackgroundResource(R.drawable.mm_listitem_pressed);

					} else {// 其他模式
							// 设置背景为未选中正常状态
							// v.setBackgroundResource(R.drawable.mm_listitem_simple);

					}

					return false;
				}
			});

		}
	}

	class MessageHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.user_photo)
		CircularImage avatarView;
		@InjectView(R.id.friends_name)
		TextView nameView;
		@InjectView(R.id.message_date)
		TextView dateView;
		@InjectView(R.id.friends_message)
		TextView messageView;

		public MessageHolder(View itemView) {
			super(itemView);
			ButterKnife.inject(this, itemView);
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ChatMessage msg = (ChatMessage) getData()
							.get(getPosition());
					Friends friend = null;
					for (Friends friends : mActivity.getFriends()) {
						switch (msg.getType()) {
						case ChatMessage.TYPE_RECEIVE:
							if (friends.getUserId() == msg.getFromId()) {
								friend = friends;
							}
							break;
						case ChatMessage.TYPE_SEND:
							if (friends.getUserId() == msg.getToId()) {
								friend = friends;
							}
							break;
						default:
							break;
						}
					}
					if (friend != null) {
					//	UIHelper.startChatActivity(mActivity, friend);
					} else {
						Log.v("org.weishe.weichat", "好友不存在！");
					}
				}
			});

		}
	}

	// @Override
	// public void addData(List data) {
	// List<ChatMessage> data0 = data;
	// if (mDatas != null && data != null && !data.isEmpty()) {
	// mDatas.addAll(data);
	// for (ChatMessage m : data0) {
	// for (Object o : mDatas) {
	// ChatMessage x = (ChatMessage) o;
	// switch (x.getType()) {
	// case ChatMessage.TYPE_RECEIVE:
	//
	// break;
	// case ChatMessage.TYPE_SEND:
	//
	// break;
	// default:
	// break;
	// }
	// }
	//
	// }
	// }
	//
	// notifyDataSetChanged();
	// }
}
