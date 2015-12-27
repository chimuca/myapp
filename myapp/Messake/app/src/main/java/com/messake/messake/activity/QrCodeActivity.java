package com.messake.messake.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.messake.messake.R;
import com.messake.messake.bean.Attachment;
import com.messake.messake.bean.Constants;
import com.messake.messake.bean.User;
import com.messake.messake.cache.CacheManager;
import com.messake.messake.utils.QrCodeUtil;
import com.messake.messake.view.CircularImage;
import com.messake.messake.view.TitleBarView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class QrCodeActivity extends FragmentActivity {

    @InjectView(R.id.title_bar)
    protected TitleBarView titleView;

    @InjectView(R.id.pic)
    protected CircularImage photo;

    @InjectView(R.id.name)
    protected TextView name;
    @InjectView(R.id.gender)
    protected ImageView gender;

    @InjectView(R.id.qr_code_img)
    protected ImageView qrCodeImg;

    private String token;
    private User user;
    private Attachment avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.inject(this);
        user = (User) CacheManager.readObject(this,
                Constants.CACHE_CURRENT_USER);
        token = (String) CacheManager.readObject(this,
                Constants.CACHE_CURRENT_USER_TOKEN);
        avatar = JSON.parseObject(user.getAvatar(), Attachment.class);

        initView();

        user.setAvatar("");
        user.setPassword("");
        user.setSignature("");
        String jsr = JSON.toJSONString(user);

        qrCodeImg.setImageBitmap(QrCodeUtil.makeQrCode(jsr));
    }

    private void initView() {
        titleView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE,
                View.VISIBLE);

        titleView.setTitleText(R.string.my_qr_code);

        titleView.setBtnRight(R.drawable.conversation_options_share_photo);

        name.setText(user.getName());
        switch (user.getGender()) {
            case User.GENDER_FEMALE:
                gender.setImageResource(R.drawable.female);
                break;

            case User.GENDER_MALE:
                gender.setImageResource(R.drawable.male);
                break;
        }
        if (avatar != null) {
            photo.setImage(avatar.getGroupName(), avatar.getPath());
        }
    }

}
