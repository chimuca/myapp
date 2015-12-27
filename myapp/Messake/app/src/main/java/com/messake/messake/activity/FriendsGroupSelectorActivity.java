package com.messake.messake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.messake.messake.R;
import com.messake.messake.adapter.FriendsGroupAdapter;
import com.messake.messake.bean.Constants;
import com.messake.messake.bean.Friends;
import com.messake.messake.bean.FriendsGroup;
import com.messake.messake.bean.User;
import com.messake.messake.cache.CacheManager;
import com.messake.messake.view.HandyTextView;
import com.messake.messake.view.TitleBarView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by messake on 2015/12/26.
 */
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
