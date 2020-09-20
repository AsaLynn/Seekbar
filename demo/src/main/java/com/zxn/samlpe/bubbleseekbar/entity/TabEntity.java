package com.zxn.samlpe.bubbleseekbar.entity;

import com.zxn.tablayout.listener.CustomTabEntity;

/**
 * Created by zxn on 2020/9/19.
 */
public class TabEntity implements CustomTabEntity {

    public String tabTitle;

    public TabEntity(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    @Override
    public String getTabContent() {
        return null;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
