package com.bboylin.gank.Net;

import android.content.Context;

import com.bboylin.gank.Data.CategoryPref;
import com.bboylin.gank.Data.Gank;

import java.util.List;

import rx.Observable;

/**
 * Created by lin on 2016/12/17.
 */

public class CategoryRepository extends BaseRepository {
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
                .compose(applySchedulers())
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
                });
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
}
