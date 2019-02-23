package com.example.administrator.xposeddemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.xposeddemo.service.AccessibilityServiceMonitor;
import com.example.administrator.xposeddemo.utils.AlarmTaskUtil;

import org.litepal.LitePal;

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initValue();
        mContext = this.getApplicationContext();
        LitePal.initialize(this);
    }

    private void initValue() {
        startAlarmTask(this);
    }

    public static void startAlarmTask(Context mContext) {
        Intent intent = new Intent(mContext, AccessibilityServiceMonitor.class);
        intent.setAction(AccessibilityServiceMonitor.ACTION_ALAM_TIMER);
        AlarmTaskUtil.starRepeatAlarmTaskByService(mContext, 13, 41, 0, intent);
    }


    /**
     * 获取Application的context对象
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }
}