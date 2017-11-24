package com.bboylin.gank.UI.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/10/29.
 */

public class BaseFragment extends Fragment {
    public String tag = "home";
    public static final int PAGE_SIZE = 20;
    protected View mNoDataView;
    protected View mSucceedView;

    public void setupToolBar(String str) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(str);
        } else {
            Logger.e("No Action Bar");
        }
    }

    protected void setNoDataView(View view, int layoutResID) {
        mNoDataView = view.findViewById(layoutResID);
    }

    protected void setSucceedView(View view, int layoutResID) {
        mSucceedView = view.findViewById(layoutResID);
    }

    public void onSucceed() {
        mSucceedView.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);
    }

    public void onNoData() {
        mSucceedView.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.VISIBLE);
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
