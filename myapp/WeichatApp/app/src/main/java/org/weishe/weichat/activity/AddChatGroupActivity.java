package org.weishe.weichat.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.weishe.weichat.R;
import org.weishe.weichat.adapter.AddGroupFriendListAdapter;
import org.weishe.weichat.SessionService;
import org.weishe.weichat.api.remote.WeisheApi;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.Result;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.util.DBHelper;
import org.weishe.weichat.view.HandyTextView;
import org.weishe.weichat.view.IphoneTreeView;
import org.weishe.weichat.view.TitleBarView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddChatGroupActivity extends Activity {
	@InjectView(R.id.group_title_bar)
	protected TitleBarView mTitleBarView;
	@InjectView(R.id.et_chat_group_name)
	protected EditText mEditText;
	@InjectView(R.id.add_img)
	protected ImageView mImageView;
	@InjectView(R.id.iphone_tree_view)
	protected IphoneTreeView mIphoneTreeView;

	private User user;
	private String token;
	private List<Friends> friends;
	private List<FriendsGroup> friendsGroups;
	protected AddGroupFriendListAdapter mAdapter;
	private SessionService mSessionService;

	private String groupName;
	private List<Friends> groupMember = new ArrayList<Friends>();

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSessionService = SessionService.Stub.asInterface(service);
			Log.v("org.weishe.weichat",
					" AddChatGroupActivity 获取  SessionService！");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mSessionService = null;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(Constants.INTENT_SERVICE_SESSION);
		intent.setAction(Constants.INTENT_SERVICE_SESSION);
		intent.setPackage("org.weishe.weichat");
		this.bindService(intent, connection, Context.BIND_AUTO_CREATE);

		setContentView(R.layout.activity_add_chat_group);
		ButterKnife.inject(this);
		user = (User) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER);
		token = (String) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER_TOKEN);
		friends = (List<Friends>) CacheManager.readObject(this,
				Friends.getCacheKey(user.getId()));
		friendsGroups = (List<FriendsGroup>) CacheManager.readObject(this,
				FriendsGroup.getCacheKey(user.getId()));
		init();
	}

	private boolean check() {
		groupName = mEditText.getText().toString().trim();
		if (groupName == null || groupName.isEmpty()) {
			showCustomToast("群名称不能为空！");
			return false;
		}

		List<Friends> friend = mAdapter.getAllChild();
		if (friend != null && friend.size() > 0) {
			for (Friends f : friend) {
				if (f.isSelect()) {
					groupMember.add(f);
				}
			}
		}
		return true;
	}

	private void init() {

		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (check()) {
					WeisheApi.createChatGroup(mHandler, user.getId(), token,
							groupName, groupMember);
				}
			}
		});
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setTitleText(R.string.add_chat_group);

		mAdapter = new AddGroupFriendListAdapter(this, mIphoneTreeView);
		mIphoneTreeView.setAdapter(mAdapter);
		mIphoneTreeView.setGroupIndicator(null);

		mIphoneTreeView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				boolean flag = !mAdapter.getChild(groupPosition, childPosition)
						.isSelect();
				mAdapter.getChild(groupPosition, childPosition).setSelect(flag);
				ImageView cb = (ImageView) v.findViewById(R.id.cb_select);

				// 如果是取消则取消组上的全选
				if (!flag) {
					mAdapter.getGroup(groupPosition).setSelect(flag);
				} else {
					// 如果子被全选上了怎组也选上
					boolean f = true;
					int count = mAdapter.getChildrenCount(groupPosition);
					for (int i = 0; i < count; i++) {
						if (!mAdapter.getChild(groupPosition, i).isSelect()) {
							// 存在未选上的
							f = false;
						}
					}
					if (f && count > 0) {
						mAdapter.getGroup(groupPosition).setSelect(true);
					}
				}

				mAdapter.notifyDataSetChanged();
				return true;
			}
		});
	}

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

	protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBytes, Throwable arg3) {
			showCustomToast("创建群发生异常！");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result r = (Result) JSON.parseObject(data, Result.class);
			if (r != null) {
				if (r.isSuccess()) {
					showCustomToast("创建群成功！");
					try {
						mSessionService.getChatGroupList();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}

	};

	protected void onDestroy() {
		this.unbindService(connection);
		super.onDestroy();
	}
}
