package com.messake.messake.emoji;

import android.text.Editable;

/**
 * Created by messake on 2015/12/26.
 */
public interface OnSendClickListener {
    void onClickSendButton(Editable str);

    void onClickFlagButton();
}
