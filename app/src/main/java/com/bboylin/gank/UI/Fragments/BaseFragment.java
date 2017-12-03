package com.bboylin.gank.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/10/29.
 */

public class BaseFragment extends Fragment {
    public String tag = "home";
    protected static final String TAG = BaseFragment.class.getCanonicalName();
    public static final int PAGE_SIZE = 20;
    protected View mNoDataView;
    protected View mSucceedView;

    public void setupToolBar(String str) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(str);
        } else {
            Log.e(TAG, "No Action Bar");
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onactivitycreated");
    }
}
