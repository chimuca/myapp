package org.weishe.weichat.sort;

import java.util.Comparator;

import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.Pinyin;

/**
 * 
 * @author xiaanming
 *
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
