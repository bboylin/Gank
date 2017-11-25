package com.bboylin.gank.Utils;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lin on 2016/11/13.
 */

public class GankApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
