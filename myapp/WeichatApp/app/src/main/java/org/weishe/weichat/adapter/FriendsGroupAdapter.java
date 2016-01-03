package org.weishe.weichat.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.weishe.weichat.R;
import org.weishe.weichat.activity.GroupActivity;
import org.weishe.weichat.api.remote.WeisheApi;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.FriendsGroup;
import org.weishe.weichat.bean.Result;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.cache.CacheManager;
import org.weishe.weichat.util.BroadcastHelper;
import org.weishe.weichat.view.HandyTextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FriendsGroupAdapter extends BaseAdapter {

	private List<FriendsGroup> data;
	private Context mContext;
	private Map<String, Boolean> states = new HashMap<String, Boolean>();

	private RadioButton mRadioButton;
	private User user;
	private Friends friends;
	private String token;

	public FriendsGroupAdapter(List<FriendsGroup> data, Context context,
			User user, Friends friends) {
		this.user = user;
		this.friends = friends;
		this.data = data;
		this.mContext = context;
		this.token = (String) CacheManager.readObject(mContext,
				Constants.CACHE_CURRENT_USER_TOKEN);
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				states.put(i + "", false);
			}
		}
	}

	@Override
	public int getCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}

	@Override
	public FriendsGroup getItem(int position) {
		if (data != null) {
			return data.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (data != null) {
			return data.get(position).getId();
		}
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final FriendsGroup fg = data.get(position);
		// states.put(position + "", false);
		ViewHodler vh = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.friends_group_move_item, null);
			vh = new ViewHodler(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHodler) convertView.getTag();
		}
		vh.friendsGroupName.setText(fg.getName());
		// mRadioButton = vh.checkBox;
		vh.fgMove.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				mRadioButton = (RadioButton) v.findViewById(R.id.check_box);
				boolean flag = !mRadioButton.isChecked();
				if (flag) {
					WeisheApi.moveFriendsGroup(mHandler, user.getId(), token,
							friends.getId(), fg.getId());
				} else {

				}
				// 重置，确保最多只有一项被选中
				for (String key : states.keySet()) {
					if (key.equals(position + "")) {
						states.put(String.valueOf(position), flag);
					} else {
						states.put(key, false);
					}
				}

				FriendsGroupAdapter.this.notifyDataSetChanged();
			}
		});

		if (states.get(position + "") != null && states.get(position + "")) {
			vh.checkBox.setChecked(true);
			vh.checkBox.setBackground(mContext.getResources().getDrawable(
					R.drawable.btn_radio_on));
		} else {
			vh.checkBox.setChecked(false);
			vh.checkBox.setBackground(null);
		}
		return convertView;
	}

	class ViewHodler {
		@InjectView(R.id.fg_move)
		LinearLayout fgMove;

		@InjectView(R.id.friends_group_name)
		TextView friendsGroupName;
		@InjectView(R.id.check_box)
		RadioButton checkBox;
		boolean checkFlag;

		public ViewHodler(View view) {
			ButterKnife.inject(this, view);
		}

	}

	public void remove(FriendsGroup item) {
		data.remove(item);
		notifyDataSetChanged();
	}

	public void insert(FriendsGroup item, int to) {
		data.add(to, item);
	}

	public List<FriendsGroup> getData() {
		return data;
	}

	public void setData(List<FriendsGroup> data) {
		this.data = data;
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				states.put(i + "", false);
			}
		}
		notifyDataSetChanged();
	}

	protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBytes, Throwable arg3) {
			showCustomToast("移动好友分组发生异常！");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			String data = new String(responseBytes);
			Result r = (Result) JSON.parseObject(data, Result.class);
			if (r != null) {
				showCustomToast(r.getMessage());
				if (r.isSuccess()) {
					showCustomToast("移动好友分组成功！");
					// 发出更新好友分组通知
					BroadcastHelper.refreshFriendsData(mContext);
				}
			}
		}

	};

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(mContext).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(mContext);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
}
