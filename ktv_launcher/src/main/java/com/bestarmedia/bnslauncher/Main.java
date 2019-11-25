package com.bestarmedia.bnslauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class Main extends Activity implements View.OnClickListener {

    private boolean isRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        findViewById(R.id.root).setOnClickListener(this);
        findViewById(android.R.id.icon).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!openBnsApk()) {
            Intent intent = new Intent("/");
            ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.Settings");
            intent.setComponent(cm);
            intent.setAction("android.intent.action.VIEW");
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        isRotate = true;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRotate)
            handler.sendEmptyMessageDelayed(1, 1);
        isRotate = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    openBnsApk();
                default:
                    break;
            }
            return false;
        }
    });

    private boolean openBnsApk() {
        int screenrotation = -1;
        try {
            if (boxManufacturer() == 1) {
                screenrotation = Integer.valueOf(SystemProperties.get("persist.sys.hwrotation.bk"));
            } else if (boxManufacturer() == 2) {
                screenrotation = getH6Orientation();
            } else {
                screenrotation = Settings.System.getInt(getContentResolver(), "hdmi_orientation", -1);
            }
        } catch (Exception e) {
            Log.w("Main", "openBnsApk ex:" + e.toString());
        }
        if (screenrotation == 90 || screenrotation == 270) {
            //竖版点歌屏
            try {
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName("com.beidousat.karaoke.v", "com.beidousat.karaoke.ui.Main"));
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.GET_ACTIVITIES);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
//            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
//            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
            // 为应用程序的启动Activity 准备Intent
            if (!TextUtils.isEmpty(pkgName) && !pkgName.equals(getPackageName()) && pkgName.startsWith("com.beidousat.") && !pkgName.equalsIgnoreCase("com.beidousat.karaoke.v")) {
                try {
                    Intent launchIntent = new Intent();
                    launchIntent.setComponent(new ComponentName(pkgName, activityName));
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    Log.d("Main", "open app pkg:" + pkgName + "  activity:" + activityName);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    public static int boxManufacturer() {
        String model = android.os.Build.MODEL;
        if ("rk3288_box".equalsIgnoreCase(model)) {//音诺恒
            return 1;
        } else if ("ktv".equalsIgnoreCase(model)) {//全志H6
            return 2;
        }
        return 0;
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
