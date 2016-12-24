package com.bboylin.gank.Net.Refrofit;

import com.bboylin.gank.Data.Entity.DateResponse;
import com.bboylin.gank.Data.Entity.CategoryResponse;
import com.bboylin.gank.Data.Entity.HomeResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lin on 2016/11/1.
 */

public interface GankService {

    @GET("day/history")
    Observable<DateResponse> getHistoryDate();

    @GET("day/{year}/{month}/{day}")
    Observable<HomeResponse> getTodayData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("data/{category}/{num}/{page}")
    Observable<CategoryResponse> getCategoryData(@Path("category") String category, @Path("num") int num, @Path("page") int page);

    class Factory {
        private static GankService INSTANCE = new Retrofit.Builder()
                .baseUrl(GankApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(GankService.class);

        public static GankService getInstance() {
            return INSTANCE;
        }
    }
}
