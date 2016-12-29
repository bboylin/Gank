package com.bboylin.gank.UI.Widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.DrawableRes;

import com.bboylin.gank.R;

/**
 * Created by lin on 2016/12/29.
 */

public class MyProgressDialog {
    private MyProgressDialog() {
        throw new Error("Do not need instantiate!");
    }

    public static ProgressDialog initProgressDialog(Context context, String title, String message, @DrawableRes int iconRes) {

        // 设置mProgressDialog风格
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);//圆形
        progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);//水平
        // 设置mProgressDialog标题
        progressDialog.setTitle(title);
        // 设置mProgressDialog提示
        progressDialog.setMessage(message);
        // 设置mProgressDialog进度条的图标
        progressDialog.setIcon(iconRes);
        // 设置mProgressDialog的进度条是否不明确
        //不滚动时，当前值在最小和最大值之间移动，一般在进行一些无法确定操作时间的任务时作为提示，明确时就是根据你的进度可以设置现在的进度值
        progressDialog.setIndeterminate(false);
        //mProgressDialog.setProgress(m_count++);
        // 是否可以按回退键取消
//        mProgressDialog.setCancelable(true);
        // 显示mProgressDialog
//        mProgressDialog.show();
        return progressDialog;
    }

    public static ProgressDialog initProgressDialog(Context context, String title, String message) {
        return initProgressDialog(context, title, message, R.drawable.ic_bookmark_blue_grey_700_24dp);
    }
}
