package org.weishe.weichat.activity;

import java.util.List;

import org.apache.http.Header;
import org.weishe.weichat.AppContext;
import org.weishe.weichat.R;
import org.weishe.weichat.adapter.GroupControlAdapter;
import org.weishe.weichat.api.remote.WeisheApi;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.Result;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.view.HandyTextView;
import org.weishe.weichat.view.TitleBarView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mobeta.android.dslv.DragSortListView;

public class GroupActivity extends Activity {
	private TitleBarView mTitleBarView;
	private DragSortListView mListView;
	private AppContext mAppContext;
	private GroupControlAdapter adapter;
	private User user;
	private String token;
	private View addGroup;
	private AlertDialog mAlertDialog;

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			FriendsGroup item = adapter.getItem(from);

			adapter.notifyDataSetChanged();
			adapter.remove(item);
			adapter.insert(item, to);
			// 通知服务器顺序发生了改变
			WeisheApi.adjustPosition(adjustHandler, user.getId(), token,
					item.getId(), to + 1);
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			FriendsGroup item = adapter.getItem(which);
			adapter.remove(item);
			// 移除
			WeisheApi.removeFriendsGroup(removeHandler, user.getId(), token,
					item.getId());
		}
	};

	private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				return ((float) adapter.getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		mAppContext = (AppContext) this.getApplication();
		this.user = (User) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER);
		this.token = (String) CacheManager.readObject(this,
				Constants.CACHE_CURRENT_USER_TOKEN);
		initViews();
		initEvents();

	}

	protected void initViews() {
		mTitleBarView = (TitleBarView) this.findViewById(R.id.group_title_bar);
		mListView = (DragSortListView) this.findViewById(R.id.group_Listview);
		addGroup = this.findViewById(R.id.add_group);
	}

	protected void initEvents() {
		addGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final EditText input = new EditText(GroupActivity.this);
				input.setHint("请输入分组名称！");
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GroupActivity.this);
				builder.setTitle("添加好友分组");
				// builder.setIcon(android.R.drawable.ic_menu_info_details);
				builder.setView(input); // 将EditText添加到builder中
				builder.setNegativeButton(R.string.cancell,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mAlertDialog.dismiss();
							}

						}).setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								WeisheApi.addFriendsGroup(addHandler,
										user.getId(), token, input.getText()
												.toString().trim());
							}

						});
				mAlertDialog = builder.show();
			}
		});
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setTitleText(R.string.group_control);
		mTitleBarView.setTitleTextColor(getResources().getColor(
				R.color.text_color));

		List<FriendsGroup> fg = (List<FriendsGroup>) CacheManager.readObject(
				this, FriendsGroup.getCacheKey(user.getId()));
		adapter = new GroupControlAdapter(fg, this);
		mListView.setAdapter(adapter);

		mListView.setDropListener(onDrop);
		mListView.setRemoveListener(onRemove);
		mListView.setDragScrollProfile(ssProfile);
	}

	protected AsyncHttpResponseHandler addHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBytes, Throwable arg3) {
			showCustomToast("添加好友分组发生异常！");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result r = (Result) JSON.parseObject(data, Result.class);
			if (r != null) {
				showCustomToast(r.getMessage());
				if (r.isSuccess()) {
					showCustomToast("添加好友分组成功！");
					List<FriendsGroup> fg = (List<FriendsGroup>) JSON
							.parseArray(r.getObj().toString(),
									FriendsGroup.class);
					adapter.setData(fg);
				}
			}
		}

	};

	protected AsyncHttpResponseHandler adjustHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBytes, Throwable arg3) {
			showCustomToast("调整好友分组发生异常！");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result r = (Result) JSON.parseObject(data, Result.class);
			if (r != null) {
				showCustomToast(r.getMessage());
				if (r.isSuccess()) {
					showCustomToast("调整好友分组成功！");
					List<FriendsGroup> fg = (List<FriendsGroup>) JSON
							.parseArray(r.getObj().toString(),
									FriendsGroup.class);
					adapter.setData(fg);
				}
			}
		}

	};

	protected AsyncHttpResponseHandler removeHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBytes, Throwable arg3) {
			showCustomToast("删除好友分组发生异常！");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result r = (Result) JSON.parseObject(data, Result.class);
			if (r != null) {
				showCustomToast(r.getMessage());
				if (r.isSuccess()) {
					showCustomToast("删除好友分组成功！");
					List<FriendsGroup> fg = (List<FriendsGroup>) JSON
							.parseArray(r.getObj().toString(),
									FriendsGroup.class);

					adapter.setData(fg);
				}
			}
		}

	};

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(GroupActivity.this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(GroupActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
}
