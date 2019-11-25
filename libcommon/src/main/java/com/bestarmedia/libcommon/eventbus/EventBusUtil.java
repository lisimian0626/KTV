package com.bestarmedia.libcommon.eventbus;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by J Wong on 2016/6/22.
 */
public class EventBusUtil {

    public static void postSticky(int id, Object ogj) {
        EventBus.getDefault().postSticky(BusEvent.getEvent(id, ogj));
    }
}
