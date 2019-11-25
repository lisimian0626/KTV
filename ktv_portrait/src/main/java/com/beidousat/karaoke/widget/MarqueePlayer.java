package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.bestarmedia.libcommon.ad.LocalMarqueeGetter;
import com.bestarmedia.libcommon.model.ad.LocalMarquee;
import com.bestarmedia.libcommon.model.ad.LocalMarqueeAd;
import com.bestarmedia.libwidget.text.MarqueeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/12 13:50.
 */
public class MarqueePlayer extends MarqueeView implements LocalMarqueeGetter.AdMarqueeRequestListener {

    private LocalMarqueeGetter mMarqueeGetter;
    private boolean mIsPlay;
    private List<LocalMarqueeAd> mAds = new ArrayList<>();
    private LocalMarqueeAd mPlayingAd;
    private String mPreAdId = "";

    private int mInterval = 15;//走马灯间隔，单位秒

    public static String DEVICE_AUTH_EX = "";

    private long mPreAddDeviceAuthExTime = 0;


    public MarqueePlayer(Context context) {
        super(context);
        init();
    }

    public MarqueePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnMarqueeListener(() -> {
            setText("");
            reset();
            mPlayingAd = null;
            if (mIsPlay) {
                MarqueePlayer.this.postDelayed(() -> {
                    if (mIsPlay) {
                        playNextAd();
                    }
                }, mInterval * 1000);
            }
        });
        mMarqueeGetter = new LocalMarqueeGetter(getContext(), this);
        setZOrderOnTop(true);
    }


    private void requestAds() {
        if (mAds == null || mAds.size() <= 0) {//列表为空才请求
            mMarqueeGetter.getMarquee(mPreAdId);
        }
    }

    public void startPlayer() {
        if (mIsPlay)
            return;
        mIsPlay = true;
        requestAds();
    }

    public void stopPlayer() {
        setText("");
        reset();
        mPlayingAd = null;
        mIsPlay = false;
        stopScroll();
    }

    @Override
    public void onAdMarqueeRequestFail() {
        if (mIsPlay) {
            mIsPlay = false;
            this.postDelayed(() -> {
                requestAds();
                mIsPlay = true;
            }, mInterval * 1000);
        }
    }


    private void playNextAd() {
        if (mAds != null && mAds.size() > 0) {
            mPlayingAd = mAds.remove(0);
            if (!mPlayingAd.isPush) {//非推送
                mPreAdId = mPlayingAd.id;
            }
            String text = mPlayingAd.content;
            String color = mPlayingAd.font_color;
            if (!TextUtils.isEmpty(color)) {
                try {
                    setText(Color.parseColor(color), text);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "颜色转换出错了", e);
                    setText(Color.WHITE, text);
                }
            } else {
                setText(Color.WHITE, text);
            }
            startScroll();
        } else {
            if (mIsPlay) {
                MarqueePlayer.this.postDelayed(() -> {
                    if (mIsPlay) {
                        requestAds();
                    }
                }, mInterval * 1000);
            }
        }
    }

    @Override
    public void onAdMarqueeRequest(LocalMarquee localMarquee) {
        if (localMarquee != null && localMarquee.notice != null && localMarquee.notice.size() > 0) {
            mInterval = localMarquee.notice.get(0).interval > 0 ? localMarquee.notice.get(0).interval : 15;
            mAds.addAll(localMarquee.notice);
        }
        if (!TextUtils.isEmpty(DEVICE_AUTH_EX) && System.currentTimeMillis() - mPreAddDeviceAuthExTime >= 15 * 60 * 1000) {//15分钟以上才提示一次
            mPreAddDeviceAuthExTime = System.currentTimeMillis();
            mAds.add(0, createDeviceAuthEx());
        }

        if (mAds != null && mAds.size() > 0) {
            if (!isScrolling())//没有在滚动
                playNextAd();

        } else {
            this.postDelayed(() -> {
                requestAds();
                mIsPlay = true;
            }, mInterval * 1000);
        }
    }

    public void playPushAd(LocalMarqueeAd localMarqueeAd) {
        if (localMarqueeAd != null) {
            localMarqueeAd.isPush = true;
            int times = localMarqueeAd.times;
            if (times > 100) {//大于100次截断
                times = 100;
            }
            for (int i = 0; i < times; i++) {
                mAds.add(0, localMarqueeAd);
            }
            if (mPlayingAd != null) {//正在播放的走马灯排队到推送走马灯后面
                mAds.add(times, mPlayingAd);
            }
            setText("");
            playNextAd();
        }
    }

    public void playPushMsg(String msg, int repeatTimes, String color) {
        Log.d(getClass().getSimpleName(), "playPushMsg msg:" + msg + " repeatTimes:" + repeatTimes + " color:" + color);
        LocalMarqueeAd ad = new LocalMarqueeAd();
        ad.id = String.valueOf(0);
        ad.content = msg;
        ad.font_color = TextUtils.isEmpty(color) ? "#FFFFFF00" : color;
        ad.times = repeatTimes;
        ad.isPush = true;
        playPushAd(ad);

    }


    private LocalMarqueeAd createDeviceAuthEx() {
        LocalMarqueeAd adDeviceEx = new LocalMarqueeAd();
        adDeviceEx.id = String.valueOf(0);
        adDeviceEx.font_color = "#FFFFFF00";
        adDeviceEx.content = DEVICE_AUTH_EX;
        return adDeviceEx;
    }

}
