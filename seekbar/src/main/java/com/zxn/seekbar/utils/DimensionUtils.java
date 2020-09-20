package com.zxn.seekbar.utils;

import android.content.res.Resources;
import android.util.TypedValue;


/**
 * Dimension
 * Created by zxn on 2020/9/20.
 */
public class DimensionUtils {


    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }
}