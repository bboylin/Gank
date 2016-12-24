package com.bboylin.gank.Net.Repository;

import android.content.Context;

import com.bboylin.gank.Data.ItemTypeList;
import com.bboylin.gank.Net.Refrofit.GankApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.bboylin.gank.Data.ItemTypeList.ANDROID;
import static com.bboylin.gank.Data.ItemTypeList.FRONT_END;
import static com.bboylin.gank.Data.ItemTypeList.IOS;
import static com.bboylin.gank.Data.ItemTypeList.TODAY;
import static com.bboylin.gank.Data.ItemTypeList.WARFARE;

/**
 * Created by lin on 2016/11/13.
 */

public class MainRepository extends BaseRepository {
    private static volatile MainRepository sMainRepository;
    private HomeRepository mHomeRepository;
    private CategoryRepository mCategoryRepository;

    private MainRepository(Context context){
        mHomeRepository = HomeRepository.getInstance(context);
        mCategoryRepository=CategoryRepository.getInstance(context);
    }

    public static MainRepository getInstance(Context context){
        if (sMainRepository==null){
            synchronized (MainRepository.class){
                if (sMainRepository==null){
                    sMainRepository=new MainRepository(context);
                }
            }
        }
        return sMainRepository;
    }

    public Observable<Object> getDataFromNet(ItemTypeList itemTypeList){
        List<Observable<Object>> list=new ArrayList<>();
        for (Integer type:itemTypeList.typeList){
            Observable<Object> objectObservable=getDataFromNet(type);
            if (objectObservable!=null){
                list.add(objectObservable.map(o -> type));
            }
        }
        return Observable.mergeDelayError(list);
    }

    private Observable<Object> getDataFromNet(Integer type) {
        Observable observable=null;
        switch (type){
            case TODAY:
                mHomeRepository.getDateListFromNet();
                break;
            case WARFARE:
                observable=mCategoryRepository.getDataFromNet(GankApi.WELFARE,10,1,true);
                break;
            case ANDROID:
                observable=mCategoryRepository.getDataFromNet(GankApi.ANDROID,10,1,true);
                break;
            case IOS:
                observable=mCategoryRepository.getDataFromNet(GankApi.IOS,10,1,true);
                break;
            case FRONT_END:
                observable=mCategoryRepository.getDataFromNet(GankApi.FRONT_END,10,1,true);
                break;
            default:
                break;
        }
        return observable;
    }
}
