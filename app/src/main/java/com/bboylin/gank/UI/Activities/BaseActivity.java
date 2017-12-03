package com.bboylin.gank.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bboylin.gank.R;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.UI.ILoadingState;
import com.bboylin.gank.Utils.NetUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lin on 2016/10/29.
 */

public class BaseActivity extends AppCompatActivity {
    public static String fragmentTag = "home";
    private static final String TAG = BaseActivity.class.getCanonicalName();
    protected View mRefreshView;
    protected View mFailedView;
    protected View mSucceedView;
    protected View mNoDataView;
    protected CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetUtil.init(this);
        compositeSubscription = new CompositeSubscription();
        Log.d(TAG, "oncreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onrestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onresume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onstart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
        Log.d(TAG, "ondestory");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "onSaveInstanceState2");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState1");
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
        if (fragmentTag.equals("web") || fragmentTag.equals("image")) {
            getSupportFragmentManager().popBackStack();
            fragmentTag = "home";
        } else {
            showTips("提示", "是否退出程序", "确定", "取消");
        }
    }

    private void showTips(String title, String message, String posiBtn, String negaBtn) {
        AlertDialog a = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(posiBtn, ((dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }))
                .setNegativeButton(negaBtn, ((dialog, which) -> {
                }))
                .create();
        a.show();
    }

    protected void setRefreshView(int layoutResID) {
        mRefreshView = findViewById(layoutResID);
    }

    protected void setFailedView(int layoutResID) {
        mFailedView = findViewById(layoutResID);
    }

    protected void setSucceedView(int layoutResID) {
        mSucceedView = findViewById(layoutResID);
    }

    protected void setNoDataView(int layoutResID) {
        mNoDataView = findViewById(layoutResID);
    }

    public void onFailed(final ILoadingState.OnReloadListener listener) {
        mSucceedView.setVisibility(View.GONE);
        mRefreshView.setVisibility(View.GONE);
        mFailedView.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);
        mFailedView.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReload();
            }
        });
    }

    public void onSucceed() {
        mSucceedView.setVisibility(View.VISIBLE);
        mRefreshView.setVisibility(View.GONE);
        mFailedView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
    }

    public void onRefresh() {
        mSucceedView.setVisibility(View.GONE);
        mRefreshView.setVisibility(View.VISIBLE);
        mFailedView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.GONE);
    }

    public void onNoData() {
        mSucceedView.setVisibility(View.GONE);
        mRefreshView.setVisibility(View.GONE);
        mFailedView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.VISIBLE);
    }

}
