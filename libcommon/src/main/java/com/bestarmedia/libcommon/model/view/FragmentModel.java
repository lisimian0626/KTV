package com.bestarmedia.libcommon.model.view;

import android.support.v4.app.Fragment;

/**
 * Created by J Wong on 2016/6/14.
 */
public class FragmentModel {

    public Fragment fragment;

    public String tag;

    public boolean isCleanStacks;

    public boolean isHideTopBar;

    public boolean isHideLeftBar;

    public boolean isHideBottomBar;

    public FragmentModel(Fragment fragment, boolean isCleanStacks, boolean isHideTopBar, boolean isHideLeftBar, boolean isHideBottomBar) {
        this.fragment = fragment;
        this.isCleanStacks = isCleanStacks;
        this.isHideTopBar = isHideTopBar;
        this.isHideLeftBar = isHideLeftBar;
        this.isHideBottomBar = isHideBottomBar;
        this.tag = fragment.getClass().getName();
    }
}
