package com.beidousat.karaoke.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.beidousat.karaoke.im.VodCommunicateHelper;
import com.beidousat.karaoke.interf.OnScreenAdListener;
import com.beidousat.karaoke.ui.dialog.play.DlgPay;
import com.beidousat.karaoke.ui.dialog.safety.DlgRoomSafety;
import com.beidousat.karaoke.ui.dialog.safety.DlgStoreSafety;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.model.view.FragmentModel;
import com.bestarmedia.libwidget.floatview.FloatingView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends FragmentActivity implements HttpRequestListener, OnScreenAdListener {

    private final static int SYS_UI = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE;

    public static long mLastTouchTime;
    public static boolean mCanShowAd = true;
    private int mTimerCount = 0;
    private ScheduledExecutorService mScheduledExecutorService;
    protected DlgStoreSafety dlgStoreSafety;
    protected DlgRoomSafety dlgRoomSafety;
    protected DlgPay dlgPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLastTouchTime = 0;
        getWindow().getDecorView().setSystemUiVisibility(SYS_UI);
        startScreenTimer();
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
    }


    @Override
    public void onFailed(String method, Object obj) {
    }

    @Override
    public void onError(String method, String error) {

    }


    @Override
    protected void onDestroy() {
        stopScreenTimer();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mLastTouchTime = System.currentTimeMillis();
        return super.dispatchTouchEvent(ev);
    }


    private void startScreenTimer() {
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown())
            return;
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);

    }

    private void stopScreenTimer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            mTimerCount++;
            Log.d("BaseActivity", "计时器：" + mTimerCount);
            if (VodConfigData.getInstance().getQrCodeInterval() > 0) {//店家自定义间隔
                if (mTimerCount % VodConfigData.getInstance().getQrCodeInterval() == 0) {//每个间隔周期
                    onTvQrCodeShow();
                }
            } else {
                Log.d("BaseActivity", "店家定义从不出现二维码!!!");
            }
            if (mCanShowAd && System.currentTimeMillis() - mLastTouchTime > VodConfigData.getInstance().getAdScreenShowNotTouch() * 1000) {//进入屏保
                onEnterScreenAd();
            }
            if (mTimerCount % 60 == 0) {//每60秒
                long timeNotTouch = (currentTime - mLastTouchTime) / 1000;//累计多少秒无触摸
                VodCommunicateHelper.sendTouchStatus(timeNotTouch > 600 ? 0 : 1, (int) timeNotTouch / 60);
                Log.d(BaseActivity.class.getSimpleName(), "通知检查消防");
                EventBusUtil.postSticky(EventBusId.Id.CHECK_SAFETY, "");
            }
            if (mTimerCount % 30 == 0) {//每30秒，检查付费情况
                Log.d(BaseActivity.class.getSimpleName(), "通知检查付费");
                EventBusUtil.postSticky(EventBusId.Id.CHECK_PAY, "");
            }
            if (mTimerCount % 10 == 0) {//每60秒，检查活动
                Log.d(BaseActivity.class.getSimpleName(), "通知检查活动");
                EventBusUtil.postSticky(EventBusId.Id.CHECK_BNS_ACTIVITY, "");
            }
            if (mTimerCount % 300 == 0) {//每5分钟店家logo
                Log.d(BaseActivity.class.getSimpleName(), "显示店家logo");
                EventBusUtil.postSticky(EventBusId.PresentationId.SHOW_STORE_LOGO_ON_TV, 15);
            }
        }, 30, 1, TimeUnit.SECONDS);
    }


    @Override
    public void onEnterScreenAd() {
    }

    @Override
    public void onTvQrCodeShow() {
    }

    public void addFragment(Fragment fragment, boolean isHideTopBar, boolean isHideLeftBar, boolean isHideBottomBar) {
        FragmentModel fragmentModel = new FragmentModel(fragment, false, isHideTopBar, isHideLeftBar, isHideBottomBar);
        EventBusUtil.postSticky(EventBusId.Id.ADD_FRAGMENT, fragmentModel);
    }

    protected void closeSafetyDialog() {
        if (dlgRoomSafety != null) {
            dlgRoomSafety.dismiss();
            dlgRoomSafety = null;
        }
        if (dlgStoreSafety != null) {
            dlgStoreSafety.dismiss();
            dlgStoreSafety = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingView.get().attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FloatingView.get().detach(this);
    }
}

