package com.example.hh.androidbaseproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.example.hh.androidbaseproject.DataAndHelper.DataCache;

import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private static WeakReference<Activity> currentActivity = null;

    public static Activity getCurActivity() {
        Activity res = null;
        if (currentActivity != null) {
            res = currentActivity.get();
        }
        return res;
    }

    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private static Context context;

    public static Context getAppContext() {
        return App.context;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        App.context = getApplicationContext();
        x.Ext.init(App.this);

        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
        DataCache.getInstance().init();

        registerActivityLifecycleCallbacks(this);


    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
