package com.zxn.seekbar.api;

import com.zxn.seekbar.listener.CursorChangeListener;
import com.zxn.seekbar.listener.LeftCursorChangListener;
import com.zxn.seekbar.listener.RightCursorChangListener;

/**
 * 可以拖动的刻度条
 * Created by zxn on 2020/9/19.
 */
public interface ISeekBar {

    /**
     * 设置进度条的上部范围
     *
     * @param max 这个刻度尺的上部范围
     */
    void setMax(int max);

    /**
     * 是否启用左游标（默认启用）
     *
     * @param enabled 是否启用
     */
    void setEnableLeftCursor(boolean enabled);

    /**
     * 设置是否启用右游标（默认启用）
     *
     * @param enabled 是否启用
     */
    void setEnableRightCursor(boolean enabled);

    /**
     * 左侧游标滑动变化监听
     *
     * @param listener LeftCursorChangListener 变化监听器
     */
    void setLeftCursorChangListener(LeftCursorChangListener listener);

    /**
     * 右侧游标滑动变化监听
     *
     * @param listener LeftCursorChangListener 变化监听器
     */
    void setRightCursorChangListener(RightCursorChangListener listener);


    /**
     * 同时设置左右游标监听器
     *
     * @param listener CursorChangeListener
     */
    void setCursorChangeListener(CursorChangeListener listener);


}
