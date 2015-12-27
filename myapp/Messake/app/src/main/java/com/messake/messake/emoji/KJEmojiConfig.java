package com.messake.messake.emoji;

import com.messake.messake.R;

/**
 * Created by messake on 2015/12/26.
 */
public class KJEmojiConfig {
    public static final String flag_Start = "[";
    public static final String flag_End = "]";

    public static final int COUNT_IN_PAGE = 20; // 每页显示多少个表情(要减去一个删除符号:例如这里是三行七列)
    public static final int COLUMNS = 7; // 每页显示多少列

    public static final int DELETE_EMOJI_ID = R.drawable.btn_del;
}
