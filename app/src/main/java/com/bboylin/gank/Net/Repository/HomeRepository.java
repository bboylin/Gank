package com.bboylin.gank.Net.Repository;

import android.content.Context;

import com.bboylin.gank.Data.Treasure.HomePref;
import com.bboylin.gank.Data.Entity.HomeResponse;
import com.bboylin.gank.Net.Refrofit.GankService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/11/13.
 */

public class HomeRepository{
    private static volatile HomeRepository sHomeRepository;
    HomePref mHomePref;
    GankService mGankService;
    public boolean update = false;

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

    public Observable<HomeResponse> getDateListFromNet() {
        return mGankService.getHistoryDate()
                .filter(dateResponse -> dateResponse.error == false)
                .doOnNext(dateResponse -> update = (dateResponse != null && dateResponse != mHomePref.getDateResponse()))
                .doOnNext(dateResponse -> {
                    if (update) {
                        mHomePref.setDateResponse(dateResponse);
                    }
                })
                .map(dateResponse -> dateResponse.results)
                .map(strings -> strings.get(0))
                .flatMap(s -> getTodayDataFromNet(s, update))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<HomeResponse> getTodayDataFromNet(String date, boolean update) {
        if (date != null && update) {
            String[] dates = date.split("-");
            return getTodayDataFromNet(Integer.parseInt(dates[0]),
                    Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
        }
        return null;
    }

    public Observable<HomeResponse> getTodayDataFromNet(int year, int month, int day) {
        return GankService.Factory.getInstance().getTodayData(year, month, day)
                .filter(homeResponse -> homeResponse.error == false)
                .doOnNext(homeResponse -> {
                    mHomePref.setTodayResponse(homeResponse);
                    update = false;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<HomeResponse.Result> loadMore(int year, int month, int day) {
        return GankService.Factory.getInstance().getTodayData(year, month, day)
                .filter(todayResponse -> todayResponse.error == false)
                .map(todayResponse -> todayResponse.results)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void clear() {
        mHomePref.clear();
    }
}
