package com.messake.messake.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.messake.messake.AppContext;
import com.messake.messake.R;
import com.messake.messake.api.ApiHttpClient;
import com.messake.messake.api.remote.WeisheApi;
import com.messake.messake.bean.Constants;
import com.messake.messake.bean.Result;
import com.messake.messake.bean.User;
import com.messake.messake.cache.CacheManager;
import com.messake.messake.utils.SpUtil;
import com.messake.messake.utils.UIHelper;
import com.messake.messake.view.HandyTextView;

import org.apache.http.Header;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class WelcomeActivity extends Activity {
    protected static final String TAG = "WelcomeActivity";
    private Context mContext;
    private AppContext mAppContext;
    private ImageView mImageView;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAppContext = (AppContext) this.getApplication();
        mContext = this;
        findView();
        init();
    }

    private void findView() {
        mImageView = (ImageView) findViewById(R.id.iv_welcome);
    }

    private void init() {
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                SpUtil.getInstance();
                sp = SpUtil.getSharePerference(mContext);
                SpUtil.getInstance();
                boolean isFirst = SpUtil.isFirst(sp);
                if (!isFirst) {
                    // 快速登陆
                    quickLogin();
                } else {
                    SpUtil.getInstance();
                    SpUtil.setBooleanSharedPerference(sp, "isFirst", false);

                    UIHelper.startLonginActivity(WelcomeActivity.this);
                }
            }
        }, 2000);

    }

    private void quickLogin() {
        User user = (User) CacheManager.readObject(this,
                Constants.CACHE_CURRENT_USER);
        String token = (String) CacheManager.readObject(this,
                Constants.CACHE_CURRENT_USER_TOKEN);
        if (user != null && token != null && !token.isEmpty()) {
            WeisheApi.quickLogin(mHandler, user, mAppContext.getAppId(), token);
        } else {
            UIHelper.startLonginActivity(this);
        }
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            String data = new String(responseBytes);
            Result r = (Result) JSON.parseObject(data, Result.class);
            if (r != null) {
                if (r.isSuccess()) {

                    User user = (User) JSON.parseObject(r.getObj().toString(),
                            User.class);

                    Intent intent = new Intent();
                    // Constants.INTENT_SERVICE_SESSION
                    intent.setAction(Constants.INTENT_SERVICE_SESSION);
                    intent.setPackage(ApiHttpClient.PACKAGE_NAME);
                    startService(intent);

                    Intent intent2 = new Intent(mContext, MainActivity.class);
                    startActivity(intent2);
                    finish();
                } else {
                    showCustomToast("快速登陆失败，启动常规登陆！");
                    UIHelper.startLonginActivity(WelcomeActivity.this);
                }
            } else {
                showCustomToast("登录发生异常！");
                UIHelper.startLonginActivity(WelcomeActivity.this);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            showCustomToast("登录发生异常！");
            UIHelper.startLonginActivity(WelcomeActivity.this);
        }

    };

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
