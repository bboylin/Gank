package com.bboylin.gank.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.UI.Fragments.DetailImageFragment;
import com.bboylin.gank.UI.Fragments.DetailWebFragment;
import com.bboylin.gank.UI.Fragments.Like.LikeFragment;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/10/29.
 */

public class BaseActivity extends AppCompatActivity {
    public String fragmentTag="home";
    public String activityTag="main";

    public boolean networkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    protected void replaceFragment(BaseFragment fragment, @IdRes int layoutContentId) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(layoutContentId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
            fragmentTag = fragment.tag;
        }
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onBackPressed() {
        if (activityTag.equals(LikeFragment.TAG)){
            finish();
            return;
        }
        if (fragmentTag.equals(DetailWebFragment.getInstance().tag)||fragmentTag.equals(DetailImageFragment.getInstance().tag)) {
            getSupportFragmentManager().popBackStack();
            fragmentTag = "home";
        } else {
            showTips("提示","是否退出程序","确定","取消");
        }
    }

    private void showTips(String title, String message, String posiBtn, String negaBtn) {
        AlertDialog a=new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(posiBtn,((dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }))
                .setNegativeButton(negaBtn,((dialog, which) -> {}))
                .create();
                a.show();
    }
}
