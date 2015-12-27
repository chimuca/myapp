package com.messake.messake.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class IntentSpan  extends ClickableSpan {
    private final View.OnClickListener mOnClickListener;

    public IntentSpan(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        mOnClickListener.onClick(view);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(true);
    }
}
