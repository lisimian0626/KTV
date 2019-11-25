package com.beidousat.karaoke.util;

import android.support.v4.app.Fragment;

import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.view.FragmentModel;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by J Wong on 2016/6/20.
 */
public class FragmentUtil {

    /**
     * @param fragment
     * @param isCleanStacks   是否清除Fragment栈，只有 点歌，歌单，好玩，服务，周边 5个主页为true，其他均传false
     * @param isHideTopBar    是否隐藏顶部操作栏
     * @param isHideLeftBar   是否隐藏左侧操作栏
     * @param isHideBottomBar 是否隐藏底部操作栏
     */
    public static void addFragment(Fragment fragment, boolean isCleanStacks, boolean isHideTopBar, boolean isHideLeftBar, boolean isHideBottomBar) {
        EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.Id.ADD_FRAGMENT, new FragmentModel(fragment, isCleanStacks, isHideTopBar, isHideLeftBar, isHideBottomBar)));
    }
}
