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
import com.bboylin.gank.Data.Entity.HomeResponse;
import com.bboylin.gank.Data.Treasure.HomePref;
import com.bboylin.gank.Net.Repository.HomeRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.HomeAdapter;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.UI.Widget.SimpleItemDecoration;
import com.orhanobut.logger.Logger;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by lin on 2016/10/29.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.phoenix_refresh_layout)
    PullToRefreshView mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private HomePref mHomePref;
    private Calendar mCalendar;
    private HomeRepository mHomeRepository;
    private int page;
    private boolean ableToLoadMore = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phoenix_fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        page = 1;
        setupToolBar("首页");
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mHomeRepository = HomeRepository.getInstance(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(0));
        mCalendar = Calendar.getInstance();
        mHomePref = HomePref.Factory.create(getContext());
        mRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                mHomeRepository.getDateListFromNet()
                        .map(homeResponse -> homeResponse.results)
                        .subscribe(result -> setupRecyclerView(getDataList(result), true),
                                throwable -> Logger.e(throwable, "onError"),
                                () -> {
                                    mRefreshLayout.setRefreshing(false);
                                    Toast.makeText(getContext(), networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                                    page = 1;
                                });
            }
        });
        initData();
        setLoadMoreListener();
        return view;
    }

    private void initData() {
        Observable<HomeResponse> disk = Observable.create(subscriber -> {
            HomeResponse mHomeResponse = mHomePref.getTodayResponse();
            if (mHomeResponse == null) {
                subscriber.onCompleted();
            } else {
                subscriber.onNext(mHomeResponse);
            }
        });
        Observable<HomeResponse> net = mHomeRepository.getDateListFromNet();
        Observable.concat(disk, net)
                .first()
                .map(homeResponse -> homeResponse.results)
                .compose(applySchedulers())
                .subscribe(result -> setupRecyclerView(getDataList(result), true),
                        throwable -> Logger.e(throwable, "onError"),
                        () -> Logger.d("onCompleted"));
    }

    public void setLoadMoreListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int past = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if ((visible + past) >= total) {
                    //加载更多
                    if (networkConnected()) {
                        if (ableToLoadMore) {
                            ableToLoadMore = false;
                            loadPage(page);
                            page++;
                        }
                    } else {
                        Toast.makeText(getContext(), "网络无连接", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadPage(int i) {
        if (mHomePref.getDateResponse() != null) {
            String[] date = mHomePref.getDateResponse().results.get(i).split("-");
            mHomeRepository.loadMore(Integer.parseInt(date[0]),
                    Integer.parseInt(date[1]), Integer.parseInt(date[2]))
                    .subscribe(result -> setupRecyclerView(getDataList(result), false),
                            throwable -> Logger.e(throwable, date + "数据加载失败"),
                            () -> ableToLoadMore = true);
        }
    }

    private void setupRecyclerView(List<Gank> dataList, boolean reset) {
        if (mHomeAdapter == null || reset) {
            mHomeAdapter = new HomeAdapter(dataList, getContext());
            mRecyclerView.setAdapter(mHomeAdapter);
        } else {
            mHomeAdapter.addData(dataList);
        }
    }

    private List<Gank> getDataList(HomeResponse.Result result) {
        ArrayList<Gank> mList = new ArrayList<>();
        if (result != null) {
            if (result.welfareList != null) {
                for (Gank gank : result.welfareList) {
                    mList.add(gank);
                }
            }
            if (result.androidList != null) {
                for (Gank gank : result.androidList) {
                    mList.add(gank);
                }
            }
            if (result.iosList != null) {
                for (Gank gank : result.iosList) {
                    mList.add(gank);
                }
            }
            if (result.frontEndList != null) {
                for (Gank gank : result.frontEndList) {
                    mList.add(gank);
                }
            }
            if (result.appList != null) {
                for (Gank gank : result.appList) {
                    mList.add(gank);
                }
            }
            if (result.casualList != null) {
                for (Gank gank : result.casualList) {
                    mList.add(gank);
                }
            }
            if (result.extraList != null) {
                for (Gank gank : result.extraList) {
                    mList.add(gank);
                }
            }
            if (result.videoList != null) {
                for (Gank gank : result.videoList) {
                    mList.add(gank);
                }
            }
        }
        return mList;
    }

}
