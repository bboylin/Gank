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

import com.bboylin.gank.Data.CategoryPref;
import com.bboylin.gank.Data.CommonPref;
import com.bboylin.gank.Data.Gank;
import com.bboylin.gank.Net.CategoryRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.CategoryAdapter;
import com.bboylin.gank.UI.Widget.SimpleItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by lin on 2016/12/16.
 */

public class CategoryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private CategoryRepository mCategoryRepository;
    private CategoryPref mCategoryPref;
    private Handler mHandler;
    private LinearLayoutManager mLinearLayoutManager;
    private static final int REFRESH_COMPLETE = 0x123;
    private int page = 1;
    private int mCurrentCounter = 0;
    private String category;
    private List<Gank> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        mCategoryRepository = CategoryRepository.getInstance(getActivity());
        mCategoryPref = CategoryPref.Factory.create(getActivity());
        category = CommonPref.Factory.create(getActivity()).getCategory();
        setupToolBar(category);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(0));
        mList = mCategoryRepository.getCategoryUrlsFromDisk(category);
        mCategoryAdapter = new CategoryAdapter(R.layout.category_item, mList);
        mCategoryAdapter.openLoadAnimation();
        mCategoryAdapter.openLoadMore(PAGE_SIZE, true);
        mCategoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                mCategoryRepository.getDataFromNet(category, 10, page, false)
                        .subscribe(list -> mList.addAll(list),
                                throwable -> Logger.e(throwable, "error in category load more"),
                                () -> {
                                    mRecyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mCurrentCounter >= 100) {
                                                mCategoryAdapter.notifyDataChangedAfterLoadMore(false);
                                            } else {
                                                mCategoryAdapter.notifyDataChangedAfterLoadMore(mList, true);
                                                mCurrentCounter = mCategoryAdapter.getItemCount();
                                            }
                                        }

                                    });
                                });
            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (mList==null){
            refresh();
        }
        return view;
    }

    private void refresh() {
        mCategoryRepository.getDataFromNet(category, 10, 1, true)
                .subscribe(list -> System.out.println(""),
                        throwable -> Logger.e(throwable,"error in category refresh"),
                        () -> {
                            mList=mCategoryRepository.getCategoryUrlsFromDisk(category);
                            mCategoryAdapter.setNewData(mList);
                        });
    }

    @Override
    public void onRefresh() {
        refresh();
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

}
