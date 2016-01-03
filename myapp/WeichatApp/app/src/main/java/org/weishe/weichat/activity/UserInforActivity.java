package org.weishe.weichat.activity;

import java.util.List;

import org.apache.http.Header;
import org.weishe.weichat.R;
import org.weishe.weichat.api.remote.WeisheApi;
import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.Result;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.core.bean.ChatMessage;
import org.weishe.weichat.util.UIHelper;
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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserInforActivity extends FragmentActivity {
	@InjectView(R.id.user_photo)
	protected CircularImage photoIV;

	@InjectView(R.id.user_name)
	protected TextView nameTV;
	@InjectView(R.id.account)
	protected TextView accountTV;
	@InjectView(R.id.gender_tv)
	protected TextView genderTV;
	@InjectView(R.id.signature_tv)
	protected TextView signatureTV;
	@InjectView(R.id.action_button)
	protected Button actionButton;
	@InjectView(R.id.three)
	protected View friendGroup;
	private int myId;
	private String token;
	private int userId;
	private String type;
	private User user;

	private Friends mFriends;
	private FriendsGroup mFriendsGroup;
	private List<Friends> friends;
	private List<FriendsGroup> friendsGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_infor);
		ButterKnife.inject(this);
		Intent intent = getIntent();
		myId = intent.getIntExtra(Constants.INTENT_EXTRA_MY_ID, 0);
		userId = intent.getIntExtra(Constants.INTENT_EXTRA_USER_ID, 0);
		type = intent.getStringExtra(Constants.INTENT_EXTRA_USER_INFOR_TYPE);
		token = intent.getStringExtra(Constants.INTENT_EXTRA_TOKEN);
		// 获取数据
		WeisheApi.getUser(mHandler, userId);

	}

	private void initView() {
		if (user != null) {
			nameTV.setText(user.getName());
			accountTV.setText(user.getAccount());
			signatureTV.setText(user.getSignature());
			switch (user.getGender()) {
			case User.GENDER_MALE:
				genderTV.setText("男");
				break;
			case User.GENDER_FEMALE:
				genderTV.setText("女");
				break;
			case User.GENDER_UNKNOWN:
				genderTV.setText("未知");
				break;
			default:
				genderTV.setText("未知");
				break;
			}

			Attachment a = JSON.parseObject(user.getAvatar(), Attachment.class);
			if (a != null) {
				photoIV.setImage(a.getGroupName(), a.getPath());
			}
		}

		switch (type) {
		case Constants.INTENT_EXTRA_USER_INFOR_TYPE_ADD_FRIENDS:
			actionButton.setText("加为好友");
			actionButton.setOnClickListener(addFriends);
			friendGroup.setVisibility(View.GONE);
			break;
		case Constants.INTENT_EXTRA_USER_INFOR_TYPE_USERINFOR:
			actionButton.setText("发送消息");
			actionButton.setOnClickListener(sendMessage);
			friendGroup.setVisibility(View.VISIBLE);
			friends = (List<Friends>) CacheManager.readObject(this,
					Friends.getCacheKey(myId));
			friendsGroups = (List<FriendsGroup>) CacheManager.readObject(this,
					FriendsGroup.getCacheKey(myId));

			TextView groupText = (TextView) friendGroup
					.findViewById(R.id.friends_group);
			if (mFriendsGroup == null) {
				if (friends != null && friends.size() > 0
						&& friendsGroups != null && friendsGroups.size() > 0) {
					for (Friends f : friends) {
						if (f.getUserId() == user.getId()) {
							mFriends = f;
							for (FriendsGroup fg : friendsGroups) {
								if (fg.getId() == f.getFriendsGroupId()) {
									mFriendsGroup = fg;
									groupText.setText(mFriendsGroup.getName());
									break;
								}
							}
						}
					}
				} else {
					for (Friends f : friends) {
						if (f.getUserId() == user.getId()) {
							mFriends = f;
						}
					}
					groupText.setText("未分组");
				}
			} else {
				groupText.setText(mFriendsGroup.getName());
			}

			friendGroup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIHelper.startGroupSelectorActivity(UserInforActivity.this,
							mFriends);
				}
			});
			break;
		default:
			break;
		}
	}

	private OnClickListener sendMessage = new OnClickListener() {
		@Override
		public void onClick(View v) {
			UIHelper.startChatActivity(UserInforActivity.this,
					ChatMessage.MSG_TYPE_UU, mFriends, null, null);
			UserInforActivity.this.finish();
		}
	};

	private OnClickListener addFriends = new OnClickListener() {

		@Override
		public void onClick(View v) {
			WeisheApi.addFriends(addFriendHandler, myId, token, userId);
		}
	};
	protected AsyncHttpResponseHandler addFriendHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result u = (Result) JSON.parseObject(data, Result.class);
			if (u != null && u.isSuccess()) {
				showCustomToast("添加好友请求已发出！");
				// actionButton.setEnabled(false);
				actionButton.setVisibility(View.GONE);
			} else {
				showCustomToast("添加好友发生异常！");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			showCustomToast("添加好友发生异常！");

		}

	};
	protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			User u = (User) JSON.parseObject(data, User.class);
			if (u != null) {
				user = u;
				initView();
			} else {
				showCustomToast("获取用户信息发生异常！");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			showCustomToast("获取用户信息发生异常！");

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
