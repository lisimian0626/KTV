package com.beidousat.karaoke.helper;

import android.content.Context;
import android.text.TextUtils;

import com.beidousat.karaoke.im.DeviceCommunicateHelper;
import com.bestarmedia.libcommon.ad.AdGetterV4;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.interf.AdsRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J Wong on 2015/11/9 09:09.
 */
public class ScreenAdSyncTimer implements AdsRequestListenerV4 {

    private Context mContext;
    private static ScreenAdSyncTimer mRomDetailTimer;
    private ScheduledExecutorService mScheduledExecutorService;
    private AdGetterV4 adGetterV4;

    public static ScreenAdSyncTimer getInstance(Context context) {
        if (mRomDetailTimer == null)
            mRomDetailTimer = new ScreenAdSyncTimer(context);
        return mRomDetailTimer;
    }

    private ScreenAdSyncTimer(Context context) {
        this.mContext = context.getApplicationContext();
        if (adGetterV4 == null) {
            adGetterV4 = new AdGetterV4(mContext, this);
        }
    }

    private void syncAd() {
        if (DeviceHelper.isMainVod(mContext)) {
            adGetterV4.getADbyPos("P2");
        }
    }


    public void startTimer() {
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown())
            return;
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);
    }

    public void stopTimer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() -> {
            Logger.d("ScreenAdSyncTimer", "scheduleAtFixedRate run ==================");
            if (DeviceHelper.isMainVod(mContext)) {
                syncAd();
            }
        }, 0, VodConfigData.getInstance().getAdScreenInterval(), TimeUnit.SECONDS);
    }


    @Override
    public void onAdsRequestSuccess(List<ADModel> adList) {
        if (adList != null && adList.size() > 0) {
            ADModel adModel = adList.get(0);
            String[] images;
            if (!TextUtils.isEmpty(adModel.getAdContent()) && (images = adModel.getAdContent().split("\\|")).length > 0 && !TextUtils.isEmpty(images[0])) {
                Logger.d(getClass().getSimpleName(), "onSuccess adScreen id:" + adModel.getId() + " ADContent:" + adModel.getAdContent());
                EventBusUtil.postSticky(EventBusId.Id.CURRENT_SCREEN_AD, adModel);
                DeviceCommunicateHelper.syncAd(adModel);
            } else {
                Logger.d(getClass().getSimpleName(), "onSuccess 图片url为空 >>>>>>>>> ");
            }
        } else {
            Logger.d(getClass().getSimpleName(), "onSuccess 无屏保广告 >>>>>>>>> ");
        }
    }

    @Override
    public void onAdsRequestFail() {
    }
}
