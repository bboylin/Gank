package com.bboylin.gank.Net.Repository;

import android.content.Context;

import com.bboylin.gank.Data.Treasure.CategoryPref;
import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Net.Refrofit.GankApi;
import com.bboylin.gank.Net.Refrofit.GankService;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lin on 2016/12/17.
 */

public class CategoryRepository{
    private static volatile CategoryRepository instance;
    private GankService mGankService;
    private CategoryPref mCategoryPref;

    private CategoryRepository(Context context){
        mGankService=GankService.Factory.getInstance();
        mCategoryPref=CategoryPref.Factory.create(context);
    }

    public static CategoryRepository getInstance(Context context) {
        if (instance==null){
            synchronized (CategoryRepository.class){
                if (instance==null){
                    instance=new CategoryRepository(context);
                }
            }
        }
        return instance;
    }

    public Observable<List<Gank>> getDataFromNet(String category,int num, int page, boolean refresh){
        return mGankService.getCategoryData(category,num,page)
                .filter(categoryResponse -> categoryResponse.error==false)
                .map(categoryResponse -> categoryResponse.mGankList)
                .doOnNext(list -> {
                    if (refresh){
                        if (category.equals(GankApi.ANDROID)){
                            mCategoryPref.setAndroidList(list);
                        }else if (category.equals(GankApi.IOS)){
                            mCategoryPref.setiOSList(list);
                        }else if (category.equals(GankApi.FRONT_END)){
                            mCategoryPref.setFrontEndList(list);
                        }else if (category.equals(GankApi.WELFARE)){
                            mCategoryPref.setGirlList(list);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<Gank> getCategoryUrlsFromDisk(String category){
        if (category.equals(GankApi.ANDROID)){
            return mCategoryPref.getAndroidList();
        }else if (category.equals(GankApi.IOS)){
            return mCategoryPref.getiOSList();
        }else if (category.equals(GankApi.FRONT_END)){
            return mCategoryPref.getFrontEndList();
        }else if (category.equals(GankApi.WELFARE)){
            return mCategoryPref.getGirlList();
        }
        return null;
    }

    public void clear(){
        mCategoryPref.clear();
    }
}
