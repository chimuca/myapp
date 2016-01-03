package org.weishe.weichat.adapter;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.weishe.weichat.R;
import org.weishe.weichat.bean.DiscussionGroup;
import org.weishe.weichat.util.ImgUtil;
import org.weishe.weichat.view.CircularImage;
import org.weishe.weichat.view.CustomListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class DiscussionGroupAdapter extends BaseAdapter implements
		SectionIndexer {
	private List<DiscussionGroup> list = null;
	private Context mContext;
	private Map<String, String> mCallRecords;
	private CustomListView mCustomListView;
	private HashMap<String, SoftReference<Bitmap>> hashMaps = new HashMap<String, SoftReference<Bitmap>>();

	public DiscussionGroupAdapter(Context mContext, List<DiscussionGroup> list,
			Map<String, String> callRecods, CustomListView customListView) {
		this.mContext = mContext;
		this.list = list;
		this.mCallRecords = callRecods;
		this.mCustomListView = customListView;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<DiscussionGroup> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		if (list != null) {
			return this.list.size();
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final DiscussionGroup mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.fragment_chatgroup_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.iconView = (CircularImage) view.findViewById(R.id.icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		viewHolder.tvLetter.setVisibility(View.GONE);

		viewHolder.tvTitle.setText(this.list.get(position).getName());
		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		CircularImage iconView;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}