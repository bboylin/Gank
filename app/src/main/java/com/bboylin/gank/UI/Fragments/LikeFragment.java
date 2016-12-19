package com.bboylin.gank.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bboylin.gank.Data.CommonPref;
import com.bboylin.gank.Net.GankApi;
import com.bboylin.gank.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/12/20.
 */

public class LikeFragment extends BaseFragment {
    public static final String TAG = "LIKE";
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.viewpagertab)
    SmartTabLayout mSmartTabLayout;
    private CommonPref mCommonPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_like,container,false);
        ButterKnife.bind(this,view);
        mCommonPref=CommonPref.Factory.create(getActivity());
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(),
                FragmentPagerItems.with(getActivity())
                        .add(GankApi.HOME,LikeHomeFragment.class)
                        .add(GankApi.WELFARE, LikeWelfareFragment.class)
                        .add(GankApi.ANDROID, LikeCategoryFragment.class,new Bundler().putString(TAG,GankApi.ANDROID).get())
                        .add(GankApi.IOS,LikeCategoryFragment.class,new Bundler().putString(TAG,GankApi.IOS).get())
                        .add(GankApi.FRONT_END,LikeCategoryFragment.class,new Bundler().putString(TAG,GankApi.FRONT_END).get())
                        .create());
        mViewPager.setAdapter(adapter);
        mSmartTabLayout.setViewPager(mViewPager);
        return view;
    }
}
