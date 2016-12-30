package com.bboylin.gank.Utils;

import android.app.Application;

import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;

/**
 * Created by lin on 2016/11/13.
 */

public class GankApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareConfig config = ShareConfig.instance()
                .qqId("101372401")
                .weiboId("442131804")
                .wxId("wx6ae3c95761af5d0e");
        ShareManager.init(config);
    }
}
