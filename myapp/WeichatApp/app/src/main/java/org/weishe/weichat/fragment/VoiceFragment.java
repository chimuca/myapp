package org.weishe.weichat.fragment;

import org.weishe.weichat.R;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.emoji.OnSendClickListener;
import org.weishe.weichat.view.VoiceButton;
import org.weishe.weichat.view.VoiceButton.OnSendVoiceListener;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class VoiceFragment extends Fragment {
	private LinearLayout mRootView;
	private CheckBox mCheckBox;
	private VoiceButton mButton;
	private OnSendClickListener listener;
	private OnSendVoiceListener onSendVoiceListener;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = (LinearLayout) inflater.inflate(R.layout.voice_frag_main,
				container, false);
		initWidget(mRootView);

		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mButton.setOnSendVoiceListener(onSendVoiceListener);
	}

	private void initWidget(View rootView) {
		mCheckBox = (CheckBox) rootView.findViewById(R.id.voice_title_flag);
		mButton = (VoiceButton) rootView.findViewById(R.id.voidc_record_btn);
		mCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClickFlagButton();
				}
			}
		});

	}

	public void setOnSendClickListener(OnSendClickListener l) {
		this.listener = l;
	}

	public void setOnSendVoiceListener(OnSendVoiceListener onSendVoiceListener) {
		this.onSendVoiceListener = onSendVoiceListener;
	}

}
