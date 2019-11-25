package com.beidousat.karaoke.ui.fragment.setting;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.DlgExitInput;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.util.UIUtils;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.SystemBroadcastSender;
import com.bestarmedia.libcommon.util.SystemSettingsUtil;
import com.bestarmedia.libwidget.util.GlideUtils;


/**
 * Created by J Wong on 2016/8/24.
 */
public class FmSetting extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_setting, null);
        mRootView.findViewById(R.id.tv_back).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_system_setting).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_system_setting).setOnLongClickListener(this);
        mRootView.findViewById(R.id.tv_room).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_large_screen).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_version_info).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_song_info).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_calibration).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_video).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_rotation).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_set_baudrate).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_serial).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_set_baudrate).setVisibility(OkConfig.boxManufacturer() != 0 ? View.VISIBLE : View.GONE);
        mRootView.findViewById(R.id.tv_rotation).setVisibility(OkConfig.boxManufacturer() == 2 ? View.VISIBLE : View.GONE);
        ((TextView) mRootView.findViewById(R.id.tv_sn)).setText("SN(" + DeviceUtil.getCupSerial() + ")\nMAC(" + DeviceUtil.getMacAddress() + ")");
        mRootView.findViewById(R.id.tv_sn).setOnLongClickListener(this);
        return mRootView;
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.tv_system_setting:
                final DlgExitInput dlgExitInput = new DlgExitInput(getContext());
                dlgExitInput.setTitle(getString(R.string.server_ip));
                dlgExitInput.setEditText(OkConfig.getServerAddress());
                dlgExitInput.setPositiveButton(getString(R.string.save), editText -> {
                    if (!TextUtils.isEmpty(editText)) {
                        OkConfig.setServerAddress(editText);
                        dlgExitInput.dismiss();
                        tipRestart();
                    } else {
                        PromptDialogSmall dialog = new PromptDialogSmall(getContext());
                        dialog.setMessage(getString(R.string.server_ip_empty_tip));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
                dlgExitInput.show();
                break;
            case R.id.tv_sn:
                Toast.makeText(getContext(), "清理缓存中.....", Toast.LENGTH_LONG).show();
                GlideUtils.clearImageDiskCache(getContext());
                GlideUtils.clearImageMemoryCache(getContext());
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_version_info:
                FragmentUtil.addFragment(new FmVersionInfo(), false, true, true, true);
                break;
            case R.id.tv_song_info:
                FragmentUtil.addFragment(new FmSongInfo(), false, true, true, true);
                break;
            case R.id.tv_back:
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.tv_system_setting:
                EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
                Intent intent = new Intent("/");
                ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.Settings");
                intent.setComponent(cm);
                intent.setAction("android.intent.action.VIEW");
                startActivity(intent);
                exitApp();
                break;
            case R.id.tv_calibration:
                toCalibrationActivity();
                exitApp();
                break;
            case R.id.tv_video:
                FragmentUtil.addFragment(new FmSettingVideo(), false, true, true, true);
                break;
            case R.id.tv_room:
                if (TextUtils.isEmpty(PrefData.getJWT(getContext()))) {
                    FragmentUtil.addFragment(new FmSettingRoomRegist(), false, true, true, true);
                } else {
                    FragmentUtil.addFragment(new FmSettingRoomInfo(), false, true, true, true);
                }
                break;
            case R.id.tv_large_screen:
                FragmentUtil.addFragment(new FmSettingLargeScreen(), false, true, true, true);
                break;
            case R.id.tv_rotation:
                AlertDialog.Builder dlgRotation = new AlertDialog.Builder(getContext());
                dlgRotation.setTitle("请选择旋转角度");
                if (getContext() != null) {
                    final String[] rotations = getContext().getResources().getStringArray(R.array.screen_rotation);
                    final int curRotation = getH6Orientation();
                    int selectPs = 0;
                    int ps = 0;
                    for (String b : rotations) {
                        if (b.equals(String.valueOf(curRotation))) {
                            selectPs = ps;
                        }
                        ps++;
                    }
                    dlgRotation.setSingleChoiceItems(rotations, selectPs, null);
                    dlgRotation.setPositiveButton("确定", (dialog, which) -> {
                        int ps1 = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (OkConfig.boxManufacturer() == 2) {//H6
                            try {
                                int setRotation = Integer.valueOf(rotations[ps1]);
                                String setRotationStr = "";
                                switch (setRotation) {
                                    case 0:
                                        setRotationStr = "landscape";
                                        break;
                                    case 90:
                                        setRotationStr = "portrait";
                                        break;
                                    case 180:
                                        setRotationStr = "re_landscape";
                                        break;
                                    case 270:
                                        setRotationStr = "re_portrait";
                                        break;
                                }
                                if (TextUtils.isEmpty(setRotationStr)) {
                                    SystemSettingsUtil.setProp(getContext(), "persist.sys.orientation", setRotationStr);
                                    PromptDialogSmall dialogSmall = new PromptDialogSmall(getContext());
                                    dialogSmall.setMessage(getString(R.string.setting_success_pls_restart));
                                    dialogSmall.setCanceledOnTouchOutside(false);
                                    dialogSmall.setOkButton(true, getString(R.string.reset_hdmi), view -> {
                                        DeviceUtil.reboot();
                                    });
                                    dialogSmall.show();
                                } else {
                                    Toast.makeText(getContext(), "请选择旋转角度", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "设置旋转出错了", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dlgRotation.create().show();
                }
                break;
            case R.id.tv_set_baudrate:
                AlertDialog.Builder mySortAlertDialog = new AlertDialog.Builder(getContext());
                mySortAlertDialog.setTitle("请选择波特率");
                if (getContext() != null) {
                    final String[] baudrates = getContext().getResources().getStringArray(R.array.baudrate_list);
                    String curBaudrate = OkConfig.boxManufacturer() == 2 ? SystemSettingsUtil.getProp("persist.sys.uart_baudrate")
                            : SystemBroadcastSender.getSerialBaudrate(getContext());
                    int selectPs = 0;
                    int ps = 0;
                    for (String b : baudrates) {
                        if (b.equals(curBaudrate)) {
                            selectPs = ps;
                        }
                        ps++;
                    }
                    mySortAlertDialog.setSingleChoiceItems(baudrates, selectPs, null);
                    mySortAlertDialog.setPositiveButton("确定", (dialog, which) -> {
                        int ps1 = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (OkConfig.boxManufacturer() == 2) {//H6
                            try {
                                SystemSettingsUtil.setUartBaudrate(getContext(), Integer.valueOf(baudrates[ps1]));
                                PromptDialogSmall dialogSmall = new PromptDialogSmall(getContext());
                                dialogSmall.setMessage(getString(R.string.setting_success_pls_restart));
                                dialogSmall.setCanceledOnTouchOutside(false);
                                dialogSmall.setOkButton(true, getString(R.string.reset_hdmi), view -> {
                                    DeviceUtil.reboot();
                                });
                                dialogSmall.show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "设置波特率出错了", Toast.LENGTH_SHORT).show();
                            }
                        } else if (OkConfig.boxManufacturer() == 1) {//音诺恒
                            SystemBroadcastSender.setSerialBaudrate(getContext(), Integer.valueOf(baudrates[ps1]));
                        }
                    });
                    mySortAlertDialog.create().show();
                }
                break;
            case R.id.tv_serial:
                startAPP();
                break;
        }
    }

    private void startAPP() {
        try {
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.mdeveloper.serialtool");
            startActivity(intent);
            exitApp();
        } catch (Exception e) {
            Toast.makeText(getContext(), "未安装！！！", Toast.LENGTH_LONG).show();
        }
    }

    private void tipRestart() {
        PromptDialogSmall dialog = new PromptDialogSmall(getContext());
        dialog.setMessage(getString(R.string.setting_success_pls_restart));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOkButton(true, getString(R.string.reset_hdmi), v -> {
            PrefData.setJWT(getContext(), "");
            VodConfigData.getInstance().setJwtMessage(null);
            exitApp();
        });
        dialog.show();
    }


    private void toCalibrationActivity() {
        try {
            Intent intent = new Intent("/");
            ComponentName cm;
            if (OkConfig.boxManufacturer() == 1) {
                cm = new ComponentName("org.zeroxlab.util.tscal", "org.zeroxlab.util.tscal.TSCalibration");
            } else if (OkConfig.boxManufacturer() == 2) {
                cm = new ComponentName("sc.ktv.calibration", "sc.ktv.calibration.CalibrationActivity");
            } else {
                cm = new ComponentName("com.cx.calibration", "com.cx.calibration.MainActivity");
            }
            intent.setComponent(cm);
            intent.setAction("android.intent.action.VIEW");
            startActivity(intent);
        } catch (Exception e) {
            Logger.w("FmSetting", "open Calibration ex:" + e.toString());
        }
    }

    private void exitApp() {
        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
        if (getContext() == null || getActivity() == null)
            return;
        UIUtils.hideNavibar(getContext(), false);
        getActivity().finish();
        android.os.Process.killProcess(android.os.Process.myPid());//获取PID
        System.exit(0);//直接结束程序
    }


    private int getH6Orientation() {
//        0度：SystemProperties.get("persist.sys.orientation", "landscape");
//        90度：SystemProperties.get("persist.sys.orientation", "portrait");
//        180度：SystemProperties.get("persist.sys.orientation", "re_landscape");
//        270度：SystemProperties.get("persist.sys.orientation", "re_portrait");
//        SystemProperties.get("persist.sys.orientation", "auto");
        String orientationStr = SystemSettingsUtil.getProp("persist.sys.orientation");
        int orientation = 0;
        if ("landscape".equalsIgnoreCase(orientationStr)) {
            orientation = 0;
        } else if ("portrait".equalsIgnoreCase(orientationStr)) {
            orientation = 90;
        } else if ("re_landscape".equalsIgnoreCase(orientationStr)) {
            orientation = 180;
        } else if ("re_portrait".equalsIgnoreCase(orientationStr)) {
            orientation = 270;
        }
        return orientation;
    }
}
