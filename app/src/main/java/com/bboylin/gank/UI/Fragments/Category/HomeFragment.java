package com.bboylin.gank.UI.Fragments.Category;

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

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.Treasure.HomePref;
import com.bboylin.gank.Data.Entity.HomeResponse;
import com.bboylin.gank.Event.HomeUpdateEvent;
import com.bboylin.gank.Net.Repository.HomeRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Adapter.HomeAdapter;
import com.bboylin.gank.UI.Fragments.BaseFragment;
import com.bboylin.gank.UI.Widget.SimpleItemDecoration;
import com.bboylin.gank.Utils.RxBus;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/10/29.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private static final HomeFragment INSTANCE = new HomeFragment();
    private LinearLayoutManager mLinearLayoutManager;
    private Handler mHandler;
    private static final int REFRESH_COMPLETE = 0x123;
    private HomePref mHomePref;
    private Calendar mCalendar;
    private HomeRepository mHomeRepository;
    private int page;
    private List<Gank> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        page = 1;
        setupToolBar("首页");
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mHomeRepository = HomeRepository.getInstance(getContext());
        register();
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(0));
        mCalendar = Calendar.getInstance();
        mHomePref = HomePref.Factory.create(getContext());
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), networkConnected() ? "刷新成功" : "网络无连接", Toast.LENGTH_SHORT).show();
                }
            }
        };
        showLocalData();
        loadMore();
        return view;
    }

    private void register() {
        RxBus.getDefault().toObserverable(HomeUpdateEvent.class)
                .compose(applySchedulers())
                .subscribe(todayEvent -> showLocalData());
    }

    private void showLocalData() {
        if (mHomePref.getTodayResponse() != null) {
            setupRecyclerView(getDataList(mHomeRepository.getTodayDataFromDisk(), true));
        }
    }

    public void loadMore() {
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
                        loadPage(page);
                        page++;
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
                    .subscribe(result -> getDataList(result, false),
                            throwable -> Logger.e(throwable,date + "数据加载失败"),
                            () -> setupRecyclerView(mList));
        } else {
            mHomeRepository.getDateListFromNet();
        }
    }

    public static HomeFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        mHomeRepository.getDateListFromNet();
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

    private void setupRecyclerView(List<Gank> dataList) {
        mHomeAdapter = new HomeAdapter(dataList, getContext());
        mRecyclerView.setAdapter(mHomeAdapter);
    }

    private List<Gank> getDataList(HomeResponse.Result result, boolean first) {
        if (first) {
            mList = new ArrayList<>();
        }
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
