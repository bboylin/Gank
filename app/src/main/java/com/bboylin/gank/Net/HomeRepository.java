package com.bboylin.gank.Net;

import android.content.Context;

import com.bboylin.gank.Data.HomePref;
import com.bboylin.gank.Data.HomeResponse;
import com.bboylin.gank.Event.HomeUpdateEvent;
import com.bboylin.gank.Utils.RxBus;
import com.orhanobut.logger.Logger;

import rx.Observable;

/**
 * Created by lin on 2016/11/13.
 */

public class HomeRepository extends BaseRepository {
    private static volatile HomeRepository sHomeRepository;
    HomePref mHomePref;
    GankService mGankService;
    String[] date;
    public static boolean update = false;

    private HomeRepository(Context context) {
        mHomePref = HomePref.Factory.create(context);
        mGankService = GankService.Factory.getInstance();
    }

    public static HomeRepository getInstance(Context context) {
        if (sHomeRepository == null) {
            synchronized (HomeRepository.class) {
                if (sHomeRepository == null) {
                    sHomeRepository = new HomeRepository(context);
                }
            }
        }
        return sHomeRepository;
    }

    public void getDateListFromNet() {
        mGankService.getHistoryDate()
                .compose(applySchedulers())
                .filter(dateResponse -> dateResponse.error == false)
                .doOnNext(dateResponse -> update = (dateResponse != null && dateResponse != mHomePref.getDateResponse()))
                .doOnNext(dateResponse -> mHomePref.setDateResponse(dateResponse))
                .map(dateResponse -> dateResponse.results)
                .map(strings -> strings.get(0))
                .subscribe(s -> date = s.split("-"),
                        throwable -> Logger.e(throwable, "xyz error in get date"),
                        () -> {
                            if (update) {
                                getTodayDataFromNet(Integer.parseInt(date[0]),
                                        Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                            }else {
                                RxBus.getDefault().post(new HomeUpdateEvent());
                            }
                        });
    }

    public void getTodayDataFromNet(int year, int month, int day) {
        GankService.Factory.getInstance().getTodayData(year, month, day)
                .compose(applySchedulers())
                .filter(todayResponse -> todayResponse.error == false)
                .subscribe(todayResponse -> {
                            mHomePref.setTodayResponse(todayResponse);
                            update = false;
                        }, throwable -> Logger.e(throwable, "xyzdataerror---")
                        , () -> RxBus.getDefault().post(new HomeUpdateEvent()));
    }

    public HomeResponse.Result getTodayDataFromDisk() {
        if (mHomePref.getTodayResponse() != null) {
            return mHomePref.getTodayResponse().results;
        }
        HomeResponse.Result result = new HomeResponse.Result();
        return result;
    }

    public Observable<HomeResponse.Result> loadMore(int year, int month, int day){
        return GankService.Factory.getInstance().getTodayData(year, month, day)
                .compose(applySchedulers())
                .filter(todayResponse -> todayResponse.error == false)
                .map(todayResponse -> todayResponse.results);
    }
}
