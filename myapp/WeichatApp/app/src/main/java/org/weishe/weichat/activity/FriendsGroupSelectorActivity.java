package org.weishe.weichat.activity;

import java.util.List;

import org.weishe.weichat.R;
import org.weishe.weichat.adapter.FriendsGroupAdapter;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.view.HandyTextView;
import org.weishe.weichat.view.TitleBarView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FriendsGroupSelectorActivity extends Activity {

	@InjectView(R.id.group_listview)
	protected ListView listView;

	@InjectView(R.id.group_selector_title_bar)
	protected TitleBarView mTitleBarView;

	private User user;
	private Friends mFriends;
	private FriendsGroupAdapter adapter;
	private List<FriendsGroup> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_group_selector);
		ButterKnife.inject(this);
		Intent intent = getIntent();
		mFriends = (Friends) intent
				.getSerializableExtra(Constants.INTENT_EXTRA_FRIENDS);
		user = (User) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER);

		data = (List<FriendsGroup>) CacheManager.readObject(this,
				FriendsGroup.getCacheKey(user.getId()));
		initView();
		adapter = new FriendsGroupAdapter(data, this, user, mFriends);
		listView.setAdapter(adapter);
	}

	private void initView() {
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setTitleText(R.string.group_move);
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
}
