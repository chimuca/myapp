package org.weishe.weichat.activity;

import java.util.List;

import org.apache.http.Header;
import org.weishe.weichat.R;
import org.weishe.weichat.api.remote.WeisheApi;
import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.bean.ChatGroup;
import org.weishe.weichat.bean.ChatGroupMember;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Result;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.view.CircularImage;
import org.weishe.weichat.view.HandyTextView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GroupInforActivity extends FragmentActivity {

	@InjectView(R.id.group_photo)
	protected ImageView groupPhoto;
	@InjectView(R.id.chat_group_account)
	protected TextView chatGroupAccount;
	@InjectView(R.id.chat_group_name)
	protected TextView chatGroupName;

	@InjectView(R.id.create_by)
	protected TextView createBy;

	@InjectView(R.id.group_class_tv)
	protected TextView groupClass;

	@InjectView(R.id.slogan_tv)
	protected TextView slogan;
	@InjectView(R.id.group_member_count)
	protected TextView memberCount;

	@InjectView(R.id.action_button)
	protected Button actionButton;

	@InjectView(R.id.chat_group_member)
	protected LinearLayout groupMember;

	private String token;
	private int groupId;
	private String type;
	private User user;
	private ChatGroup mChatGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_group_infor);
		ButterKnife.inject(this);
		Intent intent = getIntent();
		groupId = intent.getIntExtra(Constants.INTENT_EXTRA_CHAT_GROUP_ID, 0);
		type = intent
				.getStringExtra(Constants.INTENT_EXTRA_CHATGROUP_INFOR_TYPE);
		token = (String) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER_TOKEN);
		user = (User) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER);
		// 获取数据
		WeisheApi.getChatGroup(mHandler, groupId);

	}

	private void initView() {
		chatGroupAccount.setText(mChatGroup.getAccount());
		chatGroupName.setText(mChatGroup.getName());
		createBy.setText(mChatGroup.getCreateBy().getName());
		if (mChatGroup.getBigClass() != null) {
			groupClass.setText(mChatGroup.getBigClass().getName());
		}
		slogan.setText(mChatGroup.getSlogan());

		switch (type) {
		case Constants.INTENT_EXTRA_CHATGROUP_INFOR_TYPE_JOINCHATGROUP:
			actionButton.setText(getResources().getText(
					R.string.action_join_chat_group));
			actionButton.setOnClickListener(joinChatGroup);
			actionButton.setBackgroundResource(R.drawable.login_btn);
			break;
		case Constants.INTENT_EXTRA_CHATGROUP_INFOR_TYPE_LEAVECHATGROUP:
			actionButton.setText(getResources().getText(
					R.string.action_leave_chat_group));
			actionButton.setOnClickListener(leaveChatGroup);
			actionButton.setBackgroundResource(R.drawable.delete_btn);
			break;
		}
		// 群成员
		WeisheApi.getChatGroupMember(handler, mChatGroup.getId(), token,
				user.getId());
	}

	private OnClickListener joinChatGroup = new OnClickListener() {
		@Override
		public void onClick(View v) {
			WeisheApi.joinChatGroup(joinHandler, mChatGroup.getId(), token,
					user.getId());
		}
	};

	private OnClickListener leaveChatGroup = new OnClickListener() {

		@Override
		public void onClick(View v) {
			WeisheApi.leaveChatGroup(leaveHandler, mChatGroup.getId(), token,
					user.getId());
		}
	};
	protected AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			List<ChatGroupMember> members = JSON.parseArray(data,
					ChatGroupMember.class);
			if (members != null && members.size() > 0) {
				memberCount.setText("(" + members.size() + ")");
				for (ChatGroupMember m : members) {
					View v = View.inflate(GroupInforActivity.this,
							R.layout.photo, null);
					CircularImage i = (CircularImage) v
							.findViewById(R.id.user_photo);
					User u = m.getUser();
					Attachment a = JSON.parseObject(u.getAvatar(),
							Attachment.class);
					if (a != null) {
						i.setImage(a.getGroupName(), a.getPath());
					}
					groupMember.addView(v);
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
		}

	};
	protected AsyncHttpResponseHandler joinHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result u = (Result) JSON.parseObject(data, Result.class);
			if (u != null && u.isSuccess()) {
				showCustomToast("加群申请已发出！");
				// actionButton.setEnabled(false);
				actionButton.setVisibility(View.GONE);
			} else {
				showCustomToast("加群发生异常！");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			showCustomToast("加群发生异常！");
		}

	};
	protected AsyncHttpResponseHandler leaveHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result u = (Result) JSON.parseObject(data, Result.class);
			if (u != null && u.isSuccess()) {
				showCustomToast("已退出该群！");
				actionButton.setVisibility(View.GONE);
				finish();
			} else {
				showCustomToast("退群发生异常！");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			showCustomToast("退群发生异常！");
		}

	};
	protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			ChatGroup g = JSON.parseObject(data, ChatGroup.class);

			if (g != null) {
				mChatGroup = g;
				initView();
			} else {
				showCustomToast("获取群信息发生异常！");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			showCustomToast("获取群信息发生异常！");

		}

	};

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
}
