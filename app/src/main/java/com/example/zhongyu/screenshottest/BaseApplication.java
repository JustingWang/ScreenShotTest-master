package com.example.zhongyu.screenshottest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * 作者：wangsai
 * 时间：2018/9/12 0012:15:24
 * 邮箱：1729340905@qq.com
 * 说明：
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
