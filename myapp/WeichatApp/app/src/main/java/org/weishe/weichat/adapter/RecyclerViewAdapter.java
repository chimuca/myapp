package org.weishe.weichat.adapter;

import java.util.ArrayList;
import java.util.List;

import org.weishe.weichat.R;
import org.weishe.weichat.util.StringUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class RecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public static final int STATE_EMPTY_ITEM = 0;
	public static final int STATE_LOAD_MORE = 1;
	public static final int STATE_NO_MORE = 2;
	public static final int STATE_NO_DATA = 3;
	public static final int STATE_LESS_ONE_PAGE = 4;
	public static final int STATE_NETWORK_ERROR = 5;
	public static final int STATE_OTHER = 6;

	protected int state = STATE_LESS_ONE_PAGE;

	protected int _loadmoreText;
	protected int _loadFinishText;
	protected int _noDateText;

	protected List mDatas;

	public RecyclerViewAdapter() {
		_loadmoreText = R.string.loading;
		_loadFinishText = R.string.loading_no_more;
		_noDateText = R.string.error_view_no_data;
		this.mDatas = new ArrayList();
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public List getData() {
		return mDatas;
	}

	public void setData(List data) {
		this.mDatas = data;
	}

	private LinearLayout mFooterView;

	public View getFooterView() {
		return this.mFooterView;
	}

	public void setFooterViewLoading(String loadMsg) {
		ProgressBar progress = (ProgressBar) mFooterView
				.findViewById(R.id.progressbar);
		TextView text = (TextView) mFooterView.findViewById(R.id.text);
		mFooterView.setVisibility(View.VISIBLE);
		progress.setVisibility(View.VISIBLE);
		text.setVisibility(View.VISIBLE);
		if (StringUtils.isEmpty(loadMsg)) {
			text.setText(_loadmoreText);
		} else {
			text.setText(loadMsg);
		}
	}

	public void setFooterViewLoading() {
		setFooterViewLoading("");
	}

	public void clear() {
		mDatas.clear();
		notifyDataSetChanged();
	}

	public void addData(List data) {
		if (mDatas != null && data != null && !data.isEmpty()) {
			mDatas.addAll(data);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if (this.mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}
}
