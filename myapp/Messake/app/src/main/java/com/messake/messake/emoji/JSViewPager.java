package com.messake.messake.emoji;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *  重写ViewPager触屏操作，修正了系统ViewPager与Activity触摸屏事件冲突
 * Created by messake on 2015/12/26.
 */
public class JSViewPager extends ViewPager {

    public JSViewPager(Context context) {
        super(context);
    }

    public JSViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return false;
    }
}
