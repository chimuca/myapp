package com.messake.messake.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messake.messake.R;
import com.messake.messake.activity.MainActivity;
import com.messake.messake.utils.UIHelper;
import com.messake.messake.view.TitleBarView;

/**
 * Created by messake on 2015/12/26.
 */
public class ConstactFatherFragment extends Fragment {

    private MainActivity mContext;
    private View mBaseView;
    private TitleBarView mTitleBarView;
    private AllFriendsFragment friendFragment;

    private FriendListFragment constactFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_constact_father, null);
        findView();
        initTitleView();
        init();

        return mBaseView;
    }

    private void findView() {
        mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
    }

    private void initTitleView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE,
                View.VISIBLE);
        mTitleBarView.setBtnLeft(R.string.control);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startGroupControlActivity(mContext);
            }
        });
        mTitleBarView.setBtnRight(R.drawable.qq_constact);
        mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    UIHelper.startSearchActivity(getActivity(), mContext
                            .getSessionService().getUserId(), mContext
                            .getSessionService().getToken());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mTitleBarView.setTitleLeft(R.string.group);
        mTitleBarView.setTitleRight(R.string.all);

        mTitleBarView.getTitleLeft().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (mTitleBarView.getTitleLeft().isEnabled()) {
                    mTitleBarView.getTitleLeft().setEnabled(false);
                    mTitleBarView.getTitleRight().setEnabled(true);

                    FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    constactFragment = getFriendListFragment();
                    ft.replace(R.id.rl_content, constactFragment);
                    ft.commit();

                }
            }
        });

        mTitleBarView.getTitleRight().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (mTitleBarView.getTitleRight().isEnabled()) {
                    mTitleBarView.getTitleLeft().setEnabled(true);
                    mTitleBarView.getTitleRight().setEnabled(false);

                    FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    friendFragment = getFriendsFragment();
                    ft.replace(R.id.rl_content, friendFragment);
                    ft.commit();
                }

            }
        });

        mTitleBarView.getTitleLeft().performClick();
    }

    private void init() {

    }

    private AllFriendsFragment getFriendsFragment() {
        // if (friendFragment == null) {
        friendFragment = new AllFriendsFragment();
        // }
        return friendFragment;
    }

    private FriendListFragment getFriendListFragment() {
        // if (constactFragment == null) {
        constactFragment = new FriendListFragment();
        // }
        return constactFragment;
    }
}
