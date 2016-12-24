package com.bboylin.gank.UI.Fragments.Like;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.Net.Refrofit.GankApi;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Fragments.BaseFragment;
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
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        ButterKnife.bind(this, view);
        setupToolBar("收藏");
        mCommonPref = CommonPref.Factory.create(getActivity());
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(),
                FragmentPagerItems.with(getActivity())
                        .add("全部", LikeHomeFragment.class)
                        .add("福利", LikeWelfareFragment.class)
                        .add("安卓", LikeCategoryFragment.class, new Bundler().putString(TAG, GankApi.ANDROID).get())
                        .add("iOS", LikeCategoryFragment.class, new Bundler().putString(TAG, GankApi.IOS).get())
                        .add("前端", LikeCategoryFragment.class, new Bundler().putString(TAG, GankApi.FRONT_END).get())
                        .create());
        mViewPager.setAdapter(adapter);
        mSmartTabLayout.setViewPager(mViewPager);
        return view;
    }
}
