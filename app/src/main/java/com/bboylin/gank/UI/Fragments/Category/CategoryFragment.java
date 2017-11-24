package com.bboylin.gank.UI.Fragments.Category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.Treasure.CategoryPref;
import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.Net.Repository.CategoryRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.CategoryAdapter;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.UI.Widget.CustomLoadMoreView;
import com.bboylin.gank.UI.Widget.SimpleItemDecoration;
import com.bboylin.gank.Utils.NetUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/12/16.
 */

public class CategoryFragment extends BaseFragment {
    @BindView(R.id.phoenix_refresh_layout)
    PullToRefreshView mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private CategoryRepository mCategoryRepository;
    private CategoryPref mCategoryPref;
    private LinearLayoutManager mLinearLayoutManager;
    private int page = 1;
    private int mCurrentCounter = 0;
    private String category;
    private List<Gank> mList = new ArrayList<>();
    private boolean ableToLoadMore = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phoenix_fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        mCategoryRepository = CategoryRepository.getInstance(getActivity());
        mCategoryPref = CategoryPref.Factory.create(getActivity());
        category = CommonPref.Factory.create(getActivity()).getCategory();
        setupToolBar(category);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(0));
        mList = mCategoryRepository.getCategoryUrlsFromDisk(category);
        mCategoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_item, mList);
        mCategoryAdapter.openLoadAnimation();
        mCategoryAdapter.setEnableLoadMore(true);
        mCategoryAdapter.setAutoLoadMoreSize(1);
        mCategoryAdapter.setLoadMoreView(new CustomLoadMoreView());
        mRecyclerView.setAdapter(mCategoryAdapter);
        setLoadMoreListener();
        mRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        if (mList == null) {
            refresh();
        }
        return view;
    }

    private void setLoadMoreListener() {
        mCategoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (ableToLoadMore) {
                    ableToLoadMore = false;
                    page++;
                    mCategoryRepository.getDataFromNet(category, 20, page, false)
                            .subscribe(list -> mList = list,
                                    throwable -> mCategoryAdapter.loadMoreFail(),
                                    () -> {
                                        mRecyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mCurrentCounter >= 1000) {
                                                    mCategoryAdapter.loadMoreEnd();
                                                } else {
                                                    mCategoryAdapter.addData(mList);
                                                    mCurrentCounter = mCategoryAdapter.getData().size();
                                                    ableToLoadMore = true;
                                                    mCategoryAdapter.loadMoreComplete();
                                                }
                                            }
                                        });
                                    });
                }
            }
        }, mRecyclerView);
    }

    private void refresh() {
        mCategoryRepository.getDataFromNet(category, 20, 1, true)
                .subscribe(list -> System.out.println(""),
                        throwable -> mCategoryAdapter.loadMoreFail(),
                        () -> {
                            mList = mCategoryRepository.getCategoryUrlsFromDisk(category);
                            mCategoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_item, mList);
                            mCategoryAdapter.openLoadAnimation();
                            mCategoryAdapter.setEnableLoadMore(true);
                            mCategoryAdapter.setAutoLoadMoreSize(1);
                            mCategoryAdapter.setLoadMoreView(new CustomLoadMoreView());
                            setLoadMoreListener();
                            ableToLoadMore = true;
                            mRecyclerView.setAdapter(mCategoryAdapter);
                            mRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), NetUtil.networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                            page = 1;
                            mCategoryAdapter.loadMoreComplete();
                        });
    }
}
