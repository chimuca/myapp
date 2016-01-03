package org.weishe.weichat.activity.register;

import org.apache.http.Header;
import org.weishe.weichat.R;
import org.weishe.weichat.activity.LoginActivity;
import org.weishe.weichat.activity.RegisterActivity;
import org.weishe.weichat.api.remote.WeisheApi;
import org.weishe.weichat.base.BaseDialog;
import org.weishe.weichat.bean.Result;
import org.weishe.weichat.bean.User;
import org.weishe.weichat.util.VerifyUtils;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class StepBaseInfo extends RegisterStep implements TextWatcher,
		OnCheckedChangeListener {

	private EditText mEtName;
	private RadioGroup mRgGender;
	private RadioButton mRbMale;
	private RadioButton mRbFemale;

	private int mGender = 0;// 0代表女 1代表男
	private boolean mIsChange = true;
	private boolean mIsGenderAlert;
	private BaseDialog mBaseDialog;

	private String mName;
	private String mAccount;
	private String mPassword;

	public StepBaseInfo(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initViews() {
		mEtName = (EditText) findViewById(R.id.reg_baseinfo_et_name);
		mRgGender = (RadioGroup) findViewById(R.id.reg_baseinfo_rg_gender);
		mRbMale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_male);
		mRbFemale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_female);
	}

	@Override
	public void initEvents() {
		mEtName.addTextChangedListener(this);
		mRgGender.setOnCheckedChangeListener(this);
	}

	@Override
	public void doNext() {

		mOnNextActionListener.next();
	}

	@Override
	public boolean validate() {
		mName = mEtName.getText().toString().trim();
		if (VerifyUtils.isNull(mEtName)) {
			showCustomToast("请输入用户名");
			mEtName.requestFocus();
			return false;
		}

		if (mRgGender.getCheckedRadioButtonId() < 0) {
			showCustomToast("请选择性别");
			return false;
		}
		return true;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mIsChange = true;
		if (!mIsGenderAlert) {
			mIsGenderAlert = true;
			mBaseDialog = BaseDialog.getDialog(mContext, "提示", "注册成功后性别将不可更改",
					"确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			mBaseDialog.show();
		}
		switch (checkedId) {
		case R.id.reg_baseinfo_rb_male:
			mRbMale.setChecked(true);
			mGender = 1;
			break;

		case R.id.reg_baseinfo_rb_female:
			mRbFemale.setChecked(true);
			mGender = 0;
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mIsChange = true;
	}

	public String getName() {
		return mName;
	}

	public int getGender() {
		return mGender;
	}

	public String getmName() {
		return mName;
	}

	public String getmAccount() {
		return mAccount;
	}

	public String getmPassword() {
		return mPassword;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public void setmAccount(String mAccount) {
		this.mAccount = mAccount;
	}

	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

}
