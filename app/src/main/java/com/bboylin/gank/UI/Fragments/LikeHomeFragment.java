package com.bboylin.gank.UI.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bboylin.gank.Data.CommonPref;
import com.bboylin.gank.Data.Gank;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.HomeAdapter;
import com.bboylin.gank.UI.Widget.SimpleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/12/20.
 */

public class LikeHomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CommonPref mCommonPref;
    private List<Gank> mGankList = new ArrayList<>();
    private HomeAdapter mHomeAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Handler mHandler;
    private static final int REFRESH_COMPLETE = 0x123;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        mCommonPref = CommonPref.Factory.create(getActivity());
        mGankList = mCommonPref.getLikeItems();
        mHomeAdapter = new HomeAdapter(mGankList, getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(0));
        mRecyclerView.setAdapter(mHomeAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                }
            }
        };
        return view;
    }

    @Override
    public void onRefresh() {
        mGankList = mCommonPref.getLikeItems();
        mHomeAdapter=new HomeAdapter(mGankList, getActivity());
        mRecyclerView.setAdapter(mHomeAdapter);
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }
}
