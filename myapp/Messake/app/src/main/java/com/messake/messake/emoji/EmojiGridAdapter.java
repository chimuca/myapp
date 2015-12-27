package com.messake.messake.emoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.messake.messake.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情适配器
 * Created by messake on 2015/12/26.
 */
public class EmojiGridAdapter extends BaseAdapter {

    private List<Emojicon> datas;
    private final Context cxt;

    public EmojiGridAdapter(Context cxt, List<Emojicon> datas) {
        this.cxt = cxt;
        if (datas == null) {
            datas = new ArrayList<Emojicon>(0);
        }
        this.datas = datas;
    }

    public void refresh(List<Emojicon> datas) {
        if (datas == null) {
            datas = new ArrayList<Emojicon>(0);
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = new ImageView(cxt);
            int bound = (int) cxt.getResources().getDimension(R.dimen.space_49);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(bound, bound);
            convertView.setLayoutParams(params);
            int padding = (int) cxt.getResources().getDimension(
                    R.dimen.space_10);
            convertView.setPadding(padding, padding, padding, padding);
            holder.image = (ImageView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageResource(datas.get(position).getResId());
        return convertView;
    }
}
