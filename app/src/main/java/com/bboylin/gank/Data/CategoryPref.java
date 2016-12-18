package com.bboylin.gank.Data;

import android.content.Context;

import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Treasure;
import com.bboylin.gank.Utils.GsonConverterFactory;

import java.util.List;

/**
 * Created by lin on 2016/12/17.
 */

@Preferences
public interface CategoryPref {

    void setAndroidList(List<Gank> list);

    List<Gank> getAndroidList();

    void setiOSList(List<Gank> list);

    List<Gank> getiOSList();

    void setFrontEndList(List<Gank> list);

    List<Gank> getFrontEndList();

    void setGirlList(List<Gank> list);

    List<Gank> getGirlList();

    class Factory {
        public static CategoryPref create(Context context) {
            Treasure.setConverterFactory(new GsonConverterFactory());
            return Treasure.get(context, CategoryPref.class);
        }
    }
}
