package com.beidousat.karaoke;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.beidousat.karaoke.helper.KaraokeController;
import com.beidousat.karaoke.util.UnCeHandler;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libskin.SkinManager;

import java.util.ArrayList;


public class VodApplication extends Application {

    private static VodApplication vodApplication;

    private ArrayList<Activity> list = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        vodApplication = this;
//        Glide.get(this).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        OkConfig.init(this);
        SkinManager.getInstance().init(this);
        KaraokeController.getInstance();
    }

    public static KaraokeController getKaraokeController() {
        return KaraokeController.getInstance();
    }

    public void init() {
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(new UnCeHandler(this));
    }

    public static VodApplication getVodApplication() {
        return vodApplication;
    }

    public static Context getVodApplicationContext() {
        return getVodApplication().getApplicationContext();
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(0, a);
    }

    /**
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public Activity getStackTopActivity() {
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
