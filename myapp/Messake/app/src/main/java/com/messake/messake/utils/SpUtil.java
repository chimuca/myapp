package com.messake.messake.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by messake on 2015/12/26.
 */
public class SpUtil {
    private static final String NAME = "QQ";
    private static SpUtil instance;
    static {
        instance = new SpUtil();
    }

    public static SpUtil getInstance() {
        if (instance == null) {
            instance = new SpUtil();
        }
        return instance;
    }

    public static SharedPreferences getSharePerference(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFirst(SharedPreferences sp) {
        return sp.getBoolean("isFirst", true);
    }

    public static void setStringSharedPerference(SharedPreferences sp,
                                                 String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBooleanSharedPerference(SharedPreferences sp,
                                                  String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
