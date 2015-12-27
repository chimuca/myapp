package com.messake.messake.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.messake.messake.R;
import com.messake.messake.activity.LoginActivity;
import com.messake.messake.activity.RegisterActivity;
import com.messake.messake.api.remote.WeisheApi;
import com.messake.messake.bean.Attachment;
import com.messake.messake.bean.Result;
import com.messake.messake.bean.User;
import com.messake.messake.utils.FastDFSUtil;
import com.messake.messake.utils.PhotoUtils;

import org.apache.http.Header;
import org.csource.common.MyException;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by messake on 2015/12/26.
 */
public class StepPhoto  extends RegisterStep implements View.OnClickListener {

    private ImageView mIvUserPhoto;
    private LinearLayout mLayoutSelectPhoto;
    private LinearLayout mLayoutTakePicture;
    private LinearLayout mLayoutAvatars;

    private View[] mMemberBlocks;
    private String mTakePicturePath;
    private Bitmap mUserPhoto;
    private String mAccount;
    private String mPassword;
    private int mAvatarId;
    private Date mBirthday;
    private String mName;
    private int mGender;
    private static Result result = null;
    private static boolean mIsReceived = false;

    public StepPhoto(RegisterActivity activity, View contentRootView) {
        super(activity, contentRootView);
    }

    /**
     * 设置头像，并将头像上传服务器
     *
     * @param bitmap
     * @param path
     */
    public void setUserPhoto(Bitmap bitmap, String path) {
        // 上穿头像

        new AsyncTask<String, Object, Attachment>() {

            @Override
            protected Attachment doInBackground(String... params) {
                File file = new File(params[0]);
                if (file != null && file.exists()) {
                    try {
                        Attachment a = FastDFSUtil.getInstance().upload(file);
                        if (a != null) {
                            WeisheApi.addAttachment(aHandler, a);
                        }

                        return a;
                    } catch (IOException | MyException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute(path);

        // 设置头像
        if (bitmap != null) {
            mUserPhoto = bitmap;
            mIvUserPhoto.setImageBitmap(mUserPhoto);
            return;
        }
        showCustomToast("未获取到图片");
        mUserPhoto = null;
        mIvUserPhoto.setImageResource(R.drawable.ic_common_def_header);
    }

    public String getTakePicturePath() {
        return mTakePicturePath;
    }

    @Override
    public void initViews() {
        mIvUserPhoto = (ImageView) findViewById(R.id.reg_photo_iv_userphoto);
        mLayoutSelectPhoto = (LinearLayout) findViewById(R.id.reg_photo_layout_selectphoto);
        mLayoutTakePicture = (LinearLayout) findViewById(R.id.reg_photo_layout_takepicture);
    }

    @Override
    public void initEvents() {
        mLayoutSelectPhoto.setOnClickListener(this);
        mLayoutTakePicture.setOnClickListener(this);
    }

    @Override
    public boolean validate() {
        if (mUserPhoto == null) {
            showCustomToast("请添加头像");
            return false;
        }
        return true;
    }

    @Override
    public void doNext() {
        User user = new User();
        user.setAccount(mAccount);
        user.setName(mName);
        user.setPassword(mPassword);
        user.setGender(mGender);
        user.setSignature("好好学习，天天向上!");
        user.setBirthday(mBirthday);
        user.setAvatar(mAvatarId + "");

        WeisheApi.register(mHandler, user);
    }

    protected AsyncHttpResponseHandler aHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] responseBytes) {
            String data = new String(responseBytes);
            Result r = JSON.parseObject(data, Result.class);
            if (r != null && r.isSuccess()) {
                Attachment b = JSON.parseObject(r.getObj().toString(),
                        Attachment.class);
                if (b != null) {
                    mAvatarId = b.getId().intValue();
                }
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {

        }
    };
    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            String data = new String(responseBytes);
            Result r = (Result) JSON.parseObject(data, Result.class);

            if (r != null) {
                if (r.isSuccess()) {

                    showCustomToast("注册成功,请登录!");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);

                    if (mContext instanceof RegisterActivity) {
                        RegisterActivity activity = (RegisterActivity) mContext;
                        activity.finish();
                    }

                }
            } else {
                showCustomToast("注册发生异常！");
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            showCustomToast("注册发生异常！");

        }

    };

    @Override
    public boolean isChange() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_photo_layout_selectphoto:
                PhotoUtils.selectPhoto(mActivity);
                break;

            case R.id.reg_photo_layout_takepicture:
                mTakePicturePath = PhotoUtils.takePicture(mActivity);
                break;
        }
    }

    public Bitmap getPhoto() {
        return mUserPhoto;
    }

    public void setAccount(String account) {
        this.mAccount = account;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setGender(int gender) {
        this.mGender = gender;
    }

    public void setBirthday(Date birthday) {
        this.mBirthday = birthday;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

}
