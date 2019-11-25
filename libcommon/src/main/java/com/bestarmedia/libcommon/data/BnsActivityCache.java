package com.bestarmedia.libcommon.data;

import android.util.SparseArray;

import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.activity.BnsActivity;
import com.bestarmedia.libcommon.model.activity.BnsActivityBean;

import static com.bestarmedia.libcommon.eventbus.EventBusId.Id.BNS_ACTIVITY_CHANGED;


/**
 * Created by J Wong on 2019/11/12
 * 北星云端店家活动
 */
public class BnsActivityCache {

    private volatile static BnsActivityCache bnsActivityCache;

    private BnsActivity mBnsActivity = null;
    private boolean reloadActivityImage = true;
    private SparseArray<BnsActivityBean> activities = new SparseArray<>();

    public static BnsActivityCache getInstance() {
        if (bnsActivityCache == null) {
            synchronized (BnsActivityCache.class) {
                if (bnsActivityCache == null)
                    bnsActivityCache = new BnsActivityCache();
            }
        }
        return bnsActivityCache;
    }

    private BnsActivityCache() {
    }

    public void setBnsActivity(BnsActivity bnsActivity) {
        if (bnsActivity == null || bnsActivity.ktvPrize == null || bnsActivity.ktvPrize.data == null || bnsActivity.ktvPrize.data.isEmpty()) {//云端无活动
            if (mBnsActivity != null) {//清除本地
                mBnsActivity = null;
                activities.clear();
                setReloadActivityImage(true);
                EventBusUtil.postSticky(BNS_ACTIVITY_CHANGED, 1);
                return;
            }
        }

        if (mBnsActivity == null || mBnsActivity.ktvPrize == null || mBnsActivity.ktvPrize.data == null) {//本地无活动，云端有活动；展示活动
            mBnsActivity = bnsActivity;
            saveActivitiesSparseArray();
            setReloadActivityImage(true);
            EventBusUtil.postSticky(BNS_ACTIVITY_CHANGED, 1);
        } else {//本地有活动，云端有活动；对比活动是否有变化
            boolean isActivityChanged = false;
            if (!BnsActivityCache.getInstance().getBnsActivity().ktvPrize.icon.equalsIgnoreCase(bnsActivity.ktvPrize.icon)
                    || BnsActivityCache.getInstance().getBnsActivity().ktvPrize.total != bnsActivity.ktvPrize.total) {
                isActivityChanged = true;
            }
            if (!isActivityChanged) {
                if (mBnsActivity.ktvPrize.data.size() != bnsActivity.ktvPrize.data.size()) {//活动个数不一样，有变化
                    isActivityChanged = true;
                } else {//个数一样，比较每个活动id
                    for (BnsActivityBean bean : bnsActivity.ktvPrize.data) {
                        if (activities.get(bean.id) == null) {
                            isActivityChanged = true;
                            break;
                        }
                    }
                }
            }
            if (isActivityChanged) {
                mBnsActivity = bnsActivity;
                saveActivitiesSparseArray();
                setReloadActivityImage(true);
                EventBusUtil.postSticky(BNS_ACTIVITY_CHANGED, 1);
            }
        }
    }

    public BnsActivity getBnsActivity() {
        return mBnsActivity;
    }

    private void saveActivitiesSparseArray() {
        if (getBnsActivity().ktvPrize != null && getBnsActivity().ktvPrize.data != null && !getBnsActivity().ktvPrize.data.isEmpty()) {
            for (BnsActivityBean bean : getBnsActivity().ktvPrize.data) {
                activities.put(bean.id, bean);
            }
        } else {
            activities.clear();
        }
    }

    public SparseArray<BnsActivityBean> getActivities() {
        return activities;
    }

    public BnsActivityBean getBnsActivity(int key) {
        return activities.get(key);
    }

    public boolean isReloadActivityImage() {
        return reloadActivityImage;
    }

    public void setReloadActivityImage(boolean isReload) {
        this.reloadActivityImage = isReload;
    }
}
