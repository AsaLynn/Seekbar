package com.zxn.seekbar.listener;

import com.zxn.seekbar.api.ISeekBar;

/**
 * Created by zxn on 2020/9/19.
 */
public interface LeftCursorChangListener {
    void onLeftCursorChanged(ISeekBar cursorSeekBar, int progressMark);
}
