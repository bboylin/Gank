package com.bboylin.gank.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lin on 2017/11/24.
 */

public class NetUtil {
    private static Context context;

    public static void init(Context context) {
        NetUtil.context = context;
    }

    public static boolean networkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
