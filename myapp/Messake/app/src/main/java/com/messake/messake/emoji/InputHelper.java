package com.messake.messake.emoji;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.widget.EditText;

import com.messake.messake.R;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class InputHelper {
    public static void backspace(EditText editText) {
        if (editText == null) {
            return;
        }
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
                0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    /**
     * 获取name对应的资源
     *
     *
     * @param name
     * @return
     */
    public static int getEmojiResId(String name) {
        Integer res = DisplayRules.getMapAll().get(name);
        if (res != null) {
            return res.intValue();
        } else {
            return -1;
        }
    }

    /**
     * Support OSChina Client，due to the need to support both 2 Format<br>
     * (I'm drunk, I go home)
     */
    public static Spannable displayEmoji(Resources res, CharSequence s) {
        String str = s.toString();
        Spannable spannable = null;
        if (s instanceof Spannable) {
            spannable = (Spannable) s;
        } else {
            // 构建文字span
            spannable = new SpannableString(str);
        }
        for (int i = 0; i < str.length(); i++) {
            int index1 = str.indexOf("[", i);
            int length1 = str.indexOf("]", index1 + 1);
            int index2 = str.indexOf(":", i);
            int length2 = str.indexOf(":", index2 + 1);
            int bound = (int) res.getDimension(R.dimen.space_20);
            try {
                String emojiStr = str.substring(index1, length1 + "]".length());
                int resId = getEmojiResId(emojiStr);
                if (resId > 0) {
                    // 构建图片span
                    Drawable drawable = res.getDrawable(resId);

                    drawable.setBounds(0, 20, bound, bound + 20);
                    ImageSpan span = new ImageSpan(drawable,
                            ImageSpan.ALIGN_BASELINE);
                    spannable.setSpan(span, index1, length1 + "]".length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            } catch (Exception e) {
            }
            try {
                String emojiStr2 = str
                        .substring(index2, length2 + ":".length());
                int resId2 = getEmojiResId(emojiStr2);
                if (resId2 > 0) {
                    Drawable emojiDrawable = res.getDrawable(resId2);
                    emojiDrawable.setBounds(0, 0, bound, bound);
                    // 构建图片span
                    ImageSpan imageSpan = new ImageSpan(emojiDrawable, str);
                    spannable.setSpan(imageSpan, index2,
                            length2 + ":".length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (Exception e) {
            }
        }
        return spannable;
    }

    public static void input2OSC(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            // 没有多选时，直接在当前光标处添加
            editText.append(displayEmoji(editText.getResources(),
                    emojicon.getRemote()));
        } else {
            // 将已选中的部分替换为表情(当长按文字时会多选刷中很多文字)
            Spannable str = displayEmoji(editText.getResources(),
                    emojicon.getRemote());
            editText.getText().replace(Math.min(start, end),
                    Math.max(start, end), str, 0, str.length());
        }
    }
}
