package com.bestarmedia.libcommon.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SystemSettingsUtil {
    private static final String TAG = SystemSettingsUtil.class.getSimpleName();
    private static final String SETTINGS_RECEIVER_ACTION = "action.ktv.settings.receiver";

    private static final String SETTING_TYPE_KEY = "settings_type_key";
    private static final String SETTING_VOLUME = "settings_volume";
    private static final String SETTING_CLEAR = "settings_clear";

    private static final String VOLUME_OUTPUT_TYPE_KEY = "volume_output_type_key";

    //	private static final int TYPE_BUILT_IN = 1;
    private static final int HDMI_720P_50HZ_VALUE = 0x404;
    private static final int HDMI_720P_60HZ_VALUE = 0x405;
    private static final int HDMI_1080P_50HZ_VALUE = 0x409;
    private static final int HDMI_1080P_60HZ_VALUE = 0x40a;
    private static final int HDMI_4K_30HZ_VALUE = 0x41c;
    private static final int HDMI_4K_60HZ_VALUE = 0x422;
    private static final int TYPE_HDMI = 2;/*android.view.Display.TYPE_HDMI*/
    private static final String DISPLAYOUTPUT_SERVICE = "display_output";/*Context.DISPLAYOUTPUT_SERVICE*/

    private static final String AUDIO_OUTPUT_ACTIVE = "audio_devices_out_active";

    private static final String AUDIO_CODEC = "AUDIO_CODEC";
    private static final String AUDIO_HDMI = "AUDIO_HDMI";
    private static final String AUDIO_SPDIF = "AUDIO_SPDIF";

    private static final int FLAG_AUDIO_CODEC = 0x01;
    private static final int FLAG_AUDIO_HDMI = 0x02;
    private static final int FLAG_AUDIO_SPDIF = 0x04;

    private static final int THROUGH_OFF = 0;
    private static final int THROUGH_HDMI = 1;
    private static final int THROUGH_SPDIF = 2;

    /**
     * 设置HDMI切边
     *
     * @param context
     * @param value   切边百分比，范围从80-100
     */
    public static void setDisplayMargin(Context context, int value) {
        if (value < 80) value = 80;
        if (value > 100) value = 100;
        Object mDisplayManager = context.getSystemService(DISPLAYOUTPUT_SERVICE);
        Invoke.invokeMethod(mDisplayManager, "setDisplayMargin", new Object[]{Display.DEFAULT_DISPLAY, value, value}, new Class[]{int.class, int.class, int.class});
    }

    /**
     * 获取切换的百分比
     */
    public static int getDisplayMargin(Context context) {
        Object mDisplayManager = context.getSystemService(DISPLAYOUTPUT_SERVICE);
        Integer[] ret = (Integer[]) Invoke.invokeMethod(mDisplayManager, "getDisplayMargin", new Object[]{Display.DEFAULT_DISPLAY}, new Class[]{int.class});
        return ret[0];
    }

    /**
     * 获取hdmi显示分辨率
     *
     * @param context context
     * @return hdmi显示分辨率
     */
    public static int getOutputDisplay(Context context) {
        Object mDisplayManager = context.getSystemService(DISPLAYOUTPUT_SERVICE);
        return (Integer) Invoke.invokeMethod(mDisplayManager, "getDisplayOutput", new Object[]{TYPE_HDMI}, new Class[]{int.class});
    }

    /**
     * 设置hdmi显示分辨率
     *
     * @param context
     * @return
     */
    public static boolean setOutputDisplay(Context context, int value) {
        Log.d(TAG, "setOutputDisplay value = " + value);
        Object mDisplayManager = context.getSystemService(DISPLAYOUTPUT_SERVICE);
        if (!isSupportHdmiMode(mDisplayManager, value)) {
            return false;
        }
        final int oldFormat = (Integer) Invoke.invokeMethod(mDisplayManager, "getDisplayOutput",
                new Object[]{TYPE_HDMI}, new Class[]{int.class});

        if (value == oldFormat) {
            Log.d(TAG, "setOutputDisplay value is same, return");
            return true;
        }
        int setDispOutputOk = (Integer) Invoke.invokeMethod(mDisplayManager, "setDisplayOutput",
                new Object[]{TYPE_HDMI, value}, new Class[]{int.class, int.class});
        Log.w(TAG, "setDisplayOutput return " + setDispOutputOk);
        if (setDispOutputOk == -1) {
            Invoke.invokeMethod(mDisplayManager, "setDisplayOutput",
                    new Object[]{TYPE_HDMI, oldFormat}, new Class[]{int.class, int.class});
            return false;
        }
        return true;
    }

    /**
     * 判断是否支持HDMI 分辨率
     * <item>402</item> <!-- HDMI 480P -->
     * <item>403</item> <!-- HDMI 576P -->
     * <item>404</item> <!-- HDMI 720P 50Hz -->
     * <item>405</item> <!-- HDMI 720P 60Hz -->
     * <item>406</item> <!-- HDMI 1080I 50Hz -->
     * <item>407</item> <!-- HDMI 1080I 60Hz -->
     * <item>408</item> <!-- HDMI 1080P 24Hz -->
     * <item>409</item> <!-- HDMI 1080P 50Hz-->
     * <item>40a</item> <!-- HDMI 1080P 60Hz -->
     */
    private static boolean isSupportHdmiMode(Object mDisplayManager, int value) {
        return (Boolean) Invoke.invokeMethod(mDisplayManager, "isSupportHdmiMode",
                new Object[]{2, value - 0x400}, new Class[]{int.class, int.class});
    }

    /**
     * 获取hdmi所有支持的分辨率
     *
     * <item>402</item> <!-- HDMI 480P -->
     * <item>403</item> <!-- HDMI 576P -->
     * <item>404</item> <!-- HDMI 720P 50Hz -->
     * <item>405</item> <!-- HDMI 720P 60Hz -->
     * <item>406</item> <!-- HDMI 1080I 50Hz -->
     * <item>407</item> <!-- HDMI 1080I 60Hz -->
     * <item>408</item> <!-- HDMI 1080P 24Hz -->
     * <item>409</item> <!-- HDMI 1080P 50Hz-->
     * <item>40a</item> <!-- HDMI 1080P 60Hz -->
     * 获取的数组中，匹配以上的值减去0x400
     */
    public static int[] getOutputSupportList(Context context) {
        Object mDisplayManager = context.getSystemService(DISPLAYOUTPUT_SERVICE);
        return (int[]) Invoke.invokeMethod(mDisplayManager, "getSupportModes", new Object[]{2/*android.view.Display.DEFAULT_DISPLAY*/, 4}, new Class[]{int.class, int.class});
    }

    /**
     * 获取当前的音频输出类型
     *
     * @param context
     * @return 当前的音频输出类型
     */
    public static int getAudioVolumeOutMode(Context context) {
        ArrayList<String> oldActived = getAudioOutDevices(context);
        if (oldActived != null) {
            int mode = 0;
            if (oldActived.contains(AUDIO_CODEC)) {
                mode = mode | FLAG_AUDIO_CODEC;
            }
            if (oldActived.contains(AUDIO_HDMI)) {
                mode = mode | FLAG_AUDIO_HDMI;
            }
            if (oldActived.contains(AUDIO_SPDIF)) {
                mode = mode | FLAG_AUDIO_SPDIF;
            }
            Log.d(SystemSettingsUtil.class.getSimpleName(), "getAudioVolumeOutMode mode=" + mode);
            return mode;
        }
        Log.d(SystemSettingsUtil.class.getSimpleName(), "getAudioVolumeOutMode is null,  mode=0");
        return 0;
    }

    /*
     * get audio devices list(the devices which are supported)
     */
    private static ArrayList<String> getAudioOutDevices(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String list = am.getParameters(AUDIO_OUTPUT_ACTIVE);
        if (list == null)
            return null;
        ArrayList<String> audioDevList = new ArrayList<>();
        Log.d("SystemSettingsUtil", "  list " + list);
        String[] audioList = list.split(",");
        for (String audio : audioList) {
            if (!"".equals(audio)) {
                audioDevList.add(audio);
            }
        }
        return audioDevList;
    }

    /**
     * 设置声音输出类型
     */
    public static void setAudioOutputType(Context context, int flag) {
        Log.d(SystemSettingsUtil.class.getSimpleName(), "setAudioOutputType mode=" + flag);
        if (flag > 0) {
            Intent intent = new Intent(SETTINGS_RECEIVER_ACTION);
            intent.putExtra(SETTING_TYPE_KEY, SETTING_VOLUME);
            intent.putExtra(VOLUME_OUTPUT_TYPE_KEY, flag);
            context.sendBroadcast(intent);
        }
    }

    /**
     * 恢复出厂设置接口
     *
     * @param context
     */
    public static void clearRecorvery(Context context) {
        Intent intent = new Intent(SETTINGS_RECEIVER_ACTION);
        intent.putExtra(SETTING_TYPE_KEY, SETTING_CLEAR);
        context.sendBroadcast(intent);
    }

    /**
     * 获取SN接口
     *
     * @param context
     * @return
     */
    public static String getSn(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Object obj = Invoke.invokeMethod(window, "getSn", new Object[]{}, new Class[]{});
        if (obj != null) {
            return (String) obj;
        }
        return "";
    }

    /**
     * 获取以太网MAC地址接口
     *
     * @param context
     * @return
     */
    public static String getEthMac(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Object obj = Invoke.invokeMethod(window, "getEthMac", new Object[]{}, new Class[]{});
        if (obj != null) {
            return (String) obj;
        }
        return "";
    }

    /**
     * 执行shell命令接口，具备system权限
     *
     * @param context
     * @param cmd
     */
    public static void execShellCmd(Context context, String cmd) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "execShellCmd", new Object[]{cmd}, new Class[]{String.class});
    }

    /**
     * 设置开机视频，下次启动有效
     *
     * @param context
     * @param path
     */
    public static void setBootVideo(Context context, String path) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "setBootVideo", new Object[]{path}, new Class[]{String.class});
    }

    /**
     * 设置开机logo，下次启动有效
     *
     * @param context
     * @param path
     */
    public static void setLogo(Context context, String path) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "setLogo", new Object[]{path}, new Class[]{String.class});
    }

    /**
     * 停止开机动画
     *
     * @param context
     */
    public static void stopAnimation(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "stopAnimation", new Object[]{}, new Class[]{});
    }

    /**
     * 设置系统属性，具有system权限
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setProp(Context context, String key, String value) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "setProp", new Object[]{key, value}, new Class[]{String.class, String.class});
    }

    /**
     * 获取系统属性值
     *
     * @param key
     * @return
     */
    public static String getProp(String key) {
        String ret = "";
        try {
            Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
            Method get = SystemProperties.getMethod("get", new Class[]{String.class});
            ret = (String) get.invoke(SystemProperties, new Object[]{key});
        } catch (Exception e) {
            ret = "";
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取当前正在运行的Activity的名字
     *
     * @param context
     * @return
     */
    public static String getRunningActivity(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Object obj = Invoke.invokeMethod(window, "getRunningActivity", new Object[]{}, new Class[]{});
        if (obj != null) {
            return (String) obj;
        }
        return "";
    }

    /**
     * 判断其他应用程序的后台服务是否在运行
     *
     * @param context
     * @param serviceName 服务的名称
     * @return
     */
    public static boolean isRunningService(Context context, String serviceName) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Object obj = Invoke.invokeMethod(window, "isRunningService", new Object[]{serviceName}, new Class[]{String.class});
        if (obj != null) {
            return (Boolean) obj;
        }
        return false;
    }

    /**
     * 获取当前正在运行的所有APP信息
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Object obj = Invoke.invokeMethod(mActivityManager, "getRunningAppProcesses2", new Object[]{}, new Class[]{});
        if (obj != null) {
            return (List<ActivityManager.RunningAppProcessInfo>) obj;
        }
        return null;
    }

    /**
     * 是否打开了透传
     *
     * @param context
     * @return
     */
    public static boolean getVolumeThrough(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Object obj = Invoke.invokeMethod(window, "getVolumeThrough", new Object[]{}, new Class[]{});
        if (obj != null) {
            return (Boolean) obj;
        }
        return false;
    }

    /**
     * 设置打开透传
     *
     * @param context
     * @param enable
     */
    public static void setVolumeThrough(Context context, boolean enable) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "setVolumeThrough", new Object[]{enable}, new Class[]{boolean.class});
    }

    /**
     * 重启系统
     *
     * @param context
     */
    public static void reboot(Context context) {
        context.sendBroadcast(new Intent("action_reboot"));
    }

    /**
     * 关机
     *
     * @param context
     */
    public static void shutdown(Context context) {
        context.sendBroadcast(new Intent("action_shutdown"));
    }

    /**
     * 设置红外触摸波特率
     *
     * @param context
     * @param baudrate
     */
    public static void setUartBaudrate(Context context, int baudrate) {
        setProp(context, "persist.sys.uart_baudrate", "" + baudrate);
        Intent i = new Intent("com.ktv.setbaudrate");
        i.putExtra("baudrate", baudrate);
        context.sendBroadcast(i);
    }

    /**
     * 开机动画是否停止
     *
     * @return
     */
    public static boolean isBootAnimationRunning() {
        return !"1".equals(getProp("service.bootanim.exit"));
    }

    /**
     * 是否关机中
     */
    public static boolean isShutdonwing() {
        return "1".equals(getProp("sys.shutdown.hdmi"));
    }

    /**
     * 获取home包名
     */
    public static String getDefaultHome() {
        return getProp("persist.sys.ktv_pkg");
    }

    /**
     * 设置开机默认启动Launcher
     *
     * @return 返回设置是否成功。 如果包名不存在，或者包名对应的App不是HomeActivity
     */
    public static boolean setDefaultHome(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean ret = (Boolean) Invoke.invokeMethod(pm, "setDefaultHome", new String[]{packageName}, new Class[]{String.class});
        if (ret) {
            setProp(context, "persist.sys.ktv_pkg", packageName);
        }
        return ret;
    }

    /**
     * 启动抓日志
     * 日志抓到/mnt/sdcard/Logger目录下
     */
    public static void startCatchLog(Context context) {
        if (!isStartLog()) {
            setProp(context, "sys.log", "1");
        }
    }

    /**
     * 停止抓日志
     */
    public static void stopCatchLog(Context context) {
        setProp(context, "sys.log", "0");
        setProp(context, "ctl.stop", "log_bg");
    }

    /**
     * 是否正在抓日志
     */
    public static boolean isStartLog() {
        return "1".equals(getProp("sys.log"));
    }

    /**
     * 是否开机自启动抓日志
     */
    public static boolean isAutoStartLog() {
        return "1".equals(getProp("persist.sys.log"));
    }

    /**
     * 设置开机自启动抓日志
     */
    public static void setAutoStartLog(Context context, boolean auto) {
        setProp(context, "persist.sys.log", auto ? "1" : "0");
    }

    /**
     * 是否有HDMI插入
     *
     * @return
     */
    public static boolean hasHdmi() {
        String ret = readFile("/sys/class/switch/hdmi/state");
        return "1".equals(ret);
    }

    /**
     * 读取
     */
    private static String readFile(String path) {
        String res = "";
        if (!new File(path).exists()) {
            return res;
        }
        String resStr = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            while ((res = reader.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
                resStr += res;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resStr;
    }

    /**
     * H6隐藏操作悬浮球
     *
     * @param context     context
     * @param packageName 包名
     * @param isHide      是否隐藏
     */
    public static void hideFloatBall(Context context, String packageName, boolean isHide) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Invoke.invokeMethod(window, "hideFloatBall", new Object[]{packageName, isHide}, new Class[]{String.class, Boolean.class});
    }
}
