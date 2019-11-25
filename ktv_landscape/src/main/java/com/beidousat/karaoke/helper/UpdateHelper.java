package com.beidousat.karaoke.helper;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.beidousat.karaoke.ui.dialog.DlgProgress;
import com.beidousat.karaoke.ui.dialog.DlgPrompt;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.upgrade.AppUpgrader;
import com.bestarmedia.libcommon.upgrade.SystemUpgrader;


/**
 * Created by J Wong on 2019/3/8.
 */

public class UpdateHelper {

    private Activity mActivity;

    private DlgProgress mDlgSystemUpdate;

    private DlgProgress mDlgAppUpdate;

    private Handler handler = new Handler();

    public UpdateHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void checkUpdate() {
        checkAppUpdate();
        checkSystemUpdate();
    }

    public void release() {
        if (mDlgAppUpdate != null && mDlgAppUpdate.isShowing()) {
            mDlgAppUpdate.dismiss();
            mDlgAppUpdate = null;
        }

        if (mDlgSystemUpdate != null && mDlgSystemUpdate.isShowing()) {
            mDlgSystemUpdate.dismiss();
            mDlgSystemUpdate = null;
        }
    }

    private void checkSystemUpdate() {
        SystemUpgrader systemUpgrader = new SystemUpgrader(mActivity.getApplicationContext());
        systemUpgrader.setOnSystemUpdateListener(new SystemUpgrader.OnSystemUpdateListener() {
            @Override
            public void onSystemUpdateStart() {
                try {
                    mDlgSystemUpdate = new DlgProgress(mActivity);
                    mDlgSystemUpdate.setTitle("固件升级");
                    mDlgSystemUpdate.setTip("固件升级中，请勿关机...");
                    mDlgSystemUpdate.show();
                } catch (Exception ex) {
                    Log.e("VODService", "onSystemUpdateStart ex:" + ex.toString());
                }
            }

            @Override
            public void onSystemUpdateProgress(long progress, long total) {
                try {
                    if (mDlgSystemUpdate != null && mDlgSystemUpdate.isShowing()) {
                        mDlgSystemUpdate.setProgress(progress, total);
                    }
                } catch (Exception ex) {
                    Log.e("VODService", "onSystemUpdateProgress ex:" + ex.toString());
                }
            }

            @Override
            public void onSystemUpdateCompletion() {
                try {
                    if (mDlgSystemUpdate != null && mDlgSystemUpdate.isShowing()) {
                        mDlgSystemUpdate.dismiss();
                        mDlgSystemUpdate = null;
                    }
                } catch (Exception ex) {
                    Log.e("VODService", "onSystemUpdateCompletion ex:" + ex.toString());
                }
            }

            @Override
            public void onSystemUpdateFail(String msg) {
                try {
                    if (mDlgSystemUpdate != null && mDlgSystemUpdate.isShowing()) {
                        mDlgSystemUpdate.dismiss();
                        mDlgSystemUpdate = null;
                    }
                    DlgPrompt promptDialog = new DlgPrompt(mActivity);
                    promptDialog.setMessage("固件升级失败：" + msg);
                    promptDialog.show();
                } catch (Exception ex) {
                    Log.w("VODService", "onSystemUpdateFail ex:" + ex.toString());
                }
            }

            @Override
            public void onInterfaceError(String error) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkSystemUpdate();
                    }
                }, 5000);
            }
        });
        systemUpgrader.checkVersion(OkConfig.boxManufacturer() == 1 ? 20 : OkConfig.boxManufacturer() == 2 ? 25 : 15);
    }


    private void checkAppUpdate() {
        AppUpgrader appUpgrader = new AppUpgrader(mActivity.getApplicationContext());
        appUpgrader.setOnSystemUpdateListener(new AppUpgrader.OnAppUpdateListener() {
            @Override
            public void onAppUpdateStart() {
                try {
                    mDlgAppUpdate = new DlgProgress(mActivity);
                    mDlgAppUpdate.setTitle("APK升级");
                    mDlgAppUpdate.setTip("APK升级中，请勿关机...");
                    mDlgAppUpdate.show();
                } catch (Exception ex) {
                    Log.e("VODService", "onAppUpdateStart ex:" + ex.toString());
                }
            }

            @Override
            public void onAppUpdateProgress(long progress, long total) {
                try {
                    if (mDlgAppUpdate != null && mDlgAppUpdate.isShowing()) {
                        mDlgAppUpdate.setProgress(progress, total);
                    }
                } catch (Exception ex) {
                    Log.e("VODService", "onAppUpdateProgress ex:" + ex.toString());
                }
            }

            @Override
            public void onAppUpdateCompletion() {
                try {
                    if (mDlgAppUpdate != null && mDlgAppUpdate.isShowing()) {
                        mDlgAppUpdate.dismiss();
                        mDlgAppUpdate = null;
                    }
                } catch (Exception ex) {
                    Log.e("VODService", "onAppUpdateCompletion ex:" + ex.toString());
                }
            }

            @Override
            public void onAppUpdateFail(String msg) {
                try {
                    if (mDlgAppUpdate != null && mDlgAppUpdate.isShowing()) {
                        mDlgAppUpdate.dismiss();
                        mDlgAppUpdate = null;
                    }
                    DlgPrompt promptDialog = new DlgPrompt(mActivity);
                    promptDialog.setMessage("APK升级失败：" + msg);
                    promptDialog.show();
                } catch (Exception ex) {
                    Log.e("VODService", "onAppUpdateFail ex:" + ex.toString());
                }
            }

            @Override
            public void onInterfaceError(String error) {//接口出错延迟5s再重试，原因：开机后网络模块激活需要时间
                handler.postDelayed(() ->
                        checkAppUpdate(), 5000);
            }
        });
        appUpgrader.checkVersion(16);
    }

}
