package org.weishe.weichat.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.weishe.weichat.R;
import org.weishe.weichat.SessionService;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.fragment.ConstactFatherFragment;
import org.weishe.weichat.fragment.MessageListFragment;
import org.weishe.weichat.fragment.SettingFragment;
import org.weishe.weichat.util.DBHelper;
import org.weishe.weichat.view.TableView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public static final int START_TYPE_NORMAL = 0;
	public static final int START_TYPE_TODO = 1;

	private SessionService mSessionService;
	private User user;
	private List<Friends> friends;
	private List<FriendsGroup> friendsGroups;

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private View mPopView;
	private View currentButton;

	private TextView app_cancle;
	private TextView app_exit;
	private TextView app_change;

	private PopupWindow mPopupWindow;

	private FragmentTabHost mTabHost;
	private TableView messageView, myView, contactsView, trendView;
	private TabSpec messageTabSpec, contactsTabSpec, trendTabSpec, myTabSpec;
	private View top;
	private String previousTab;// 之前的，不能是publish

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("org.weishe.weichat", "main 收到广播消息(好友列表发生变法)！");

			String key = intent.getAction();
			switch (key) {
			case Constants.INTENT_ACTION_RECEIVE_FRIEND_LIST:

				friends = (List<Friends>) CacheManager.readObject(
						MainActivity.this, Friends.getCacheKey(user.getId()));
				break;
			case Constants.INTENT_ACTION_RECEIVE_FRIEND_GROUP_LIST:
				List<FriendsGroup> fg = null;
				friendsGroups = (List<FriendsGroup>) CacheManager.readObject(
						MainActivity.this,
						FriendsGroup.getCacheKey(user.getId()));
				break;
			default:
				break;
			}
		}
	};
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSessionService = SessionService.Stub.asInterface(service);
			Log.v("org.weishe.weichat", "获取  SessionService！");
			try {
				int fromMessageId = 0;
				try {
					fromMessageId = DBHelper.getgetInstance(mContext)
							.getMaxMessageIdByUserId(
									mSessionService.getUserId());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				mSessionService.getFriendList();
				mSessionService.getFriendGroupsList();
				mSessionService.getMessageList(fromMessageId);
				mSessionService.getChatGroupList();
				mSessionService.getDiscussionGroupList();
				mSessionService.getRelateUser();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
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
		setContentView(R.layout.activity_main);
		mContext = this;

		// 初始化一部分数据
		user = (User) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER);
		friends = (List<Friends>) CacheManager.readObject(this,
				Friends.getCacheKey(user.getId()));

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_LIST);
		intentFilter
				.addAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_GROUP_LIST);
		this.registerReceiver(receiver, intentFilter);

		findView();
		initView();
		init();
	}

	private void findView() {
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.app_exit,
				null);

		app_cancle = (TextView) mPopView.findViewById(R.id.app_cancle);
		app_change = (TextView) mPopView.findViewById(R.id.app_change_user);
		app_exit = (TextView) mPopView.findViewById(R.id.app_exit);
	}

	private void initView() {

		top = this.findViewById(R.id.top);

		mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// View view =layoutInflater.inflate(R.layout.tab_view, null);
		messageView = new TableView(this);
		contactsView = new TableView(this);
		trendView = new TableView(this);
		myView = new TableView(this);

		messageView.setTitle(
				this.getResources()
						.getDrawable(R.drawable.tab_message_selector),
				this.getResources().getString(R.string.tab_view_title_message));
		contactsView.setTitle(
				this.getResources().getDrawable(
						R.drawable.tab_contacts_selector), this.getResources()
						.getString(R.string.tab_view_title_contacts));
		trendView.setTitle(
				this.getResources().getDrawable(R.drawable.tab_trend_selector),
				this.getResources().getString(R.string.tab_view_title_trend));
		myView.setTitle(
				this.getResources().getDrawable(R.drawable.tab_my_selector),
				this.getResources().getString(R.string.tab_view_title_my));

		previousTab = "message";

		messageTabSpec = mTabHost.newTabSpec("message").setIndicator(
				messageView);
		contactsTabSpec = mTabHost.newTabSpec("contacts").setIndicator(
				contactsView);
		trendTabSpec = mTabHost.newTabSpec("trend").setIndicator(trendView);
		myTabSpec = mTabHost.newTabSpec("my").setIndicator(myView);

		mTabHost.addTab(messageTabSpec, MessageListFragment.class, null);
		mTabHost.addTab(contactsTabSpec, ConstactFatherFragment.class, null);
		mTabHost.addTab(trendTabSpec, Fragment.class, null);
		mTabHost.addTab(myTabSpec, SettingFragment.class, null);
		mTabHost.getTabWidget().setDividerDrawable(null);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				Log.v("org.weishe", tabId);
				previousTab = tabId;
			}
		});
	}

	private void init() {

		mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		app_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});

		app_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.activity_up, R.anim.fade_out);
				finish();
			}
		});

		app_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#b0000000")));
			mPopupWindow.showAtLocation(top, Gravity.BOTTOM, 0, 0);
			mPopupWindow.setAnimationStyle(R.style.app_pop);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.update();
		}
		return super.onKeyDown(keyCode, event);

	}

	public SessionService getSessionService() {
		return mSessionService;
	}

	public List<Friends> getFriends() {
		return friends;
	}

	public void setFriends(List<Friends> friends) {
		this.friends = friends;
	}

	public void setFriendsGroups(List<FriendsGroup> friendsGroups) {
		this.friendsGroups = friendsGroups;
	}

	/**
	 * 当好友列表发生该表时调用
	 * 
	 * @param data
	 */
	public void addData(List<Friends> data) {
		if (friends == null) {
			friends = new ArrayList<Friends>();
		}
		this.friends.addAll(data);
	}

	protected void onDestroy() {
		this.unbindService(connection);
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
}
