package com.bboylin.gank.UI.Fragments;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/11/12.
 */

public class DetailWebFragment extends BaseFragment {
    private static volatile DetailWebFragment sDetailWebFragment = null;
    @BindView(R.id.detail_webView)
    WebView mWebView;

    public DetailWebFragment() {
        tag = "web";
    }

    public static DetailWebFragment getInstance() {
        if (sDetailWebFragment == null) {
            synchronized (DetailWebFragment.class) {
                if (sDetailWebFragment == null) {
                    sDetailWebFragment = new DetailWebFragment();
                }
            }
        }
        return sDetailWebFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_web, container, false);
        ButterKnife.bind(this, view);
        setupToolBar("阅读");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        mWebView.setWebViewClient(new MyWeebViewClient());
        mWebView.loadUrl(CommonPref.Factory.create(getContext()).getWebViewUrl());
        return view;
    }

    public class MyWeebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
