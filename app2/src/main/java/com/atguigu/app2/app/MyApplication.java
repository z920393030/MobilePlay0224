package com.atguigu.app2.app;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by My on 2017/5/23.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
