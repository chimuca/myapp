package com.messake.messake.emoji;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.messake.messake.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情页，每页的显示
 * Created by messake on 2015/12/26.
 */
public class EmojiPageFragment extends Fragment {
    private List<Emojicon> datas;
    private GridView sGrid;
    private EmojiGridAdapter adapter;
    private final OnEmojiClickListener listener;

    public EmojiPageFragment(int index, int type, OnEmojiClickListener l) {
        initData(index, type);
        this.listener = l;
    }

    private void initData(int index, int type) {
        datas = new ArrayList<Emojicon>();
        if (KJEmojiFragment.EMOJI_TAB_CONTENT > 1) {
            datas = DisplayRules.getAllByType(type);
        } else {
            List<Emojicon> dataAll = DisplayRules.getAllByType(type);
            int max = Math.min((index + 1) * KJEmojiConfig.COUNT_IN_PAGE,
                    dataAll.size());
            for (int i = index * KJEmojiConfig.COUNT_IN_PAGE; i < max; i++) {
                datas.add(dataAll.get(i));
            }
            datas.add(new Emojicon(KJEmojiConfig.DELETE_EMOJI_ID, 1, "delete:",
                    "delete:"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        sGrid = new GridView(getActivity());
        sGrid.setNumColumns(KJEmojiConfig.COLUMNS);
        adapter = new EmojiGridAdapter(getActivity(), datas);
        sGrid.setAdapter(adapter);
        sGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EditText editText = (EditText) getActivity().findViewById(
                        R.id.emoji_titile_input);
                if (listener != null) {
                    listener.onEmojiClick((Emojicon) parent.getAdapter()
                            .getItem(position));
                }
                InputHelper.input2OSC(editText, (Emojicon) parent.getAdapter()
                        .getItem(position));
            }
        });
        sGrid.setSelector(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return sGrid;
    }

    public GridView getRootView() {
        return sGrid;
    }
}
