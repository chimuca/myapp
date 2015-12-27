package com.messake.messake.emoji;



import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 *表情页适配器（FragmentPagerAdapter的好处是fragment常驻内存，对于要求效率而页卡很少的表情控件最合适）
 * Created by messake on 2015/12/26.
 */
public class EmojiPagerAdapter  extends FragmentPagerAdapter {

    private OnEmojiClickListener listener;

    public EmojiPagerAdapter(FragmentManager fm, int tabCount,
                             OnEmojiClickListener l) {
        super(fm);
        KJEmojiFragment.EMOJI_TAB_CONTENT = tabCount;
        listener = l;
    }

    public EmojiPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public EmojiPageFragment getItem(int index) {
        if (KJEmojiFragment.EMOJI_TAB_CONTENT > 1) {
            return new EmojiPageFragment(index, index, listener);
        } else {
            return new EmojiPageFragment(index, 0, listener);
        }
    }

    /**
     * 显示模式：如果只有一种Emoji表情，则像QQ表情一样左右滑动分页显示<br>
     * 如果有多种Emoji表情，每页显示一种，Emoji筛选时上下滑动筛选。
     */
    @Override
    public int getCount() {
        if (KJEmojiFragment.EMOJI_TAB_CONTENT > 1) {
            return KJEmojiFragment.EMOJI_TAB_CONTENT;
        } else {
            // 采用进一法取小数
            return (DisplayRules.getAllByType(0).size() - 1 + KJEmojiConfig.COUNT_IN_PAGE)
                    / KJEmojiConfig.COUNT_IN_PAGE;
        }
    }
}
