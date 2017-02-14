package com.bboylin.gank.UI.Fragments.Category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.Treasure.CategoryPref;
import com.bboylin.gank.Net.Refrofit.GankApi;
import com.bboylin.gank.Net.Repository.CategoryRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.GirlAdapter;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by lin on 2016/12/16.
 */

public class GirlFragment extends BaseFragment {
    private static final GirlFragment instance = new GirlFragment();
    @BindView(R.id.phoenix_refresh_layout)
    PullToRefreshView mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private GirlAdapter mGirlAdapter;
    private CategoryRepository mCategoryRepository;
    private CategoryPref mGirlPref;
    private int page = 1;
    private int mCurrentCounter = 0;
    private List<Gank> mList = new ArrayList<>();

    public static GirlFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phoenix_fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        setupToolBar("福利");
        mRecyclerView.setPadding(0, 0, 0, 0);
        mCategoryRepository = CategoryRepository.getInstance(getActivity());
        mGirlPref = CategoryPref.Factory.create(getActivity());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        mGirlAdapter = new GirlAdapter(getActivity(), R.layout.girl_item, mGirlPref.getGirlList());
        mGirlAdapter.openLoadAnimation();
        mGirlAdapter.openLoadMore(PAGE_SIZE, true);
        mGirlAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                mCategoryRepository.getDataFromNet(GankApi.WELFARE, 10, page, false)
                        .subscribe(strings -> mList.addAll(strings),
                                throwable -> Logger.e(throwable, "error in load more"),
                                () -> {
                                    mRecyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mCurrentCounter >= 1000) {
                                                mGirlAdapter.notifyDataChangedAfterLoadMore(false);
                                            } else {
                                                mGirlAdapter.notifyDataChangedAfterLoadMore(mList, true);
                                                mCurrentCounter = mGirlAdapter.getItemCount();
                                            }
                                        }

                                    });
                                });
            }
        });
        mRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                mRefreshLayout.postDelayed(() -> {
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                    page = 1;
                }, 2000);
            }
        });
        mRecyclerView.setAdapter(mGirlAdapter);
        if (mList == null) {
            refresh();
        }
        return view;
    }

    private void refresh() {
        mCategoryRepository.getDataFromNet(GankApi.WELFARE, 10, 1, true)
                .subscribe(strings -> System.out.println(""),
                        throwable -> Logger.e(throwable, "error in girl refresh"),
                        () -> {
                            mList = new ArrayList<>();
                            mGirlAdapter = new GirlAdapter(getActivity(), R.layout.girl_item, mGirlPref.getGirlList());
                            mRecyclerView.setAdapter(mGirlAdapter);
                        });
    }

}
