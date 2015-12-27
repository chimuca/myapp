package com.messake.messake.sort;

import com.messake.messake.bean.Pinyin;

import java.util.Comparator;

/**
 * 根据拼音来排列ListView里面的数据类
 * Created by messake on 2015/12/26.
 */
public class PinyinComparator implements Comparator<Pinyin> {

    public int compare(Pinyin o1, Pinyin o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
