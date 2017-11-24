package com.bboylin.gank.UI.Fragments.Like;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.Net.Refrofit.GankApi;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.GirlAdapter;
import com.bboylin.gank.UI.Adapter.HomeAdapter;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.Utils.NetUtil;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/12/20.
 */

public class LikeWelfareFragment extends BaseFragment {
    @BindView(R.id.phoenix_refresh_layout)
    PullToRefreshView mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CommonPref mCommonPref;
    private GirlAdapter mGirlAdapter;
    private List<Gank> girlList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phoenix_fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        setNoDataView(view, R.id.nodataview);
        setSucceedView(view, R.id.phoenix_refresh_layout);
        mCommonPref = CommonPref.Factory.create(getActivity());
        refresh();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        mRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mRefreshLayout.postDelayed(() -> {
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), NetUtil.networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                }, 2000);
            }
        });
        return view;
    }

    public void refresh() {
        girlList.clear();
        for (Gank gank : mCommonPref.getLikeItems()) {
            if (gank.type.equals(GankApi.WELFARE)) {
                girlList.add(gank);
            }
        }
        if (girlList.size() > 0) {
            onSucceed();
            mGirlAdapter = new GirlAdapter(getActivity(), R.layout.girl_item, girlList);
            mRecyclerView.setAdapter(mGirlAdapter);
        } else {
            onNoData();
        }
    }
}
