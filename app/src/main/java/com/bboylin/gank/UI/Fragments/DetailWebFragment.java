package com.bboylin.gank.UI.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Widget.MyProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

/**
 * Created by lin on 2016/11/12.
 */

public class DetailWebFragment extends BaseFragment {
    @BindView(R.id.detail_webView)
    CacheWebView mWebView;
    ProgressDialog progressDialog;

    public DetailWebFragment() {
        tag = "web";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_web, container, false);
        ButterKnife.bind(this, view);
        setupToolBar(CommonPref.Factory.create(getContext()).getWebViewGank().desc);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        mWebView.setCacheStrategy(WebViewCache.CacheStrategy.FORCE);
        mWebView.setBlockNetworkImage(true);
        mWebView.setWebViewClient(new MyWeebViewClient());
        mWebView.loadUrl(CommonPref.Factory.create(getContext()).getWebViewGank().url);
        showProgressDialog("正在加载中", "请稍候");
        return view;
    }

    public class MyWeebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            cancelProgressDialog();
        }
    }

    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {
            progressDialog = MyProgressDialog.initProgressDialog(getContext(), title, message);
            progressDialog.show();
        } else {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    //隐藏ProgressDialog
    public void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
