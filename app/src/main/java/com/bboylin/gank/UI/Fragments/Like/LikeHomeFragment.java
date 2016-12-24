package com.bboylin.gank.UI.Fragments.Like;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.HomeAdapter;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.UI.Widget.SimpleItemDecoration;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/12/20.
 */

public class LikeHomeFragment extends BaseFragment {
    @BindView(R.id.phoenix_refresh_layout)
    PullToRefreshView mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CommonPref mCommonPref;
    private List<Gank> mGankList = new ArrayList<>();
    private HomeAdapter mHomeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phoenix_fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        mCommonPref = CommonPref.Factory.create(getActivity());
        mGankList = mCommonPref.getLikeItems();
        mHomeAdapter = new HomeAdapter(mGankList, getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(0));
        mRecyclerView.setAdapter(mHomeAdapter);
        mRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mRefreshLayout.postDelayed(() -> {
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                }, 2000);
            }
        });
        return view;
    }

    public void refresh() {
        mGankList = mCommonPref.getLikeItems();
        mHomeAdapter = new HomeAdapter(mGankList, getActivity());
        mRecyclerView.setAdapter(mHomeAdapter);
    }
}
