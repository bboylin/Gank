package com.bboylin.gank.Data.Treasure;

import android.content.Context;

import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Treasure;
import com.bboylin.gank.Data.Entity.DateResponse;
import com.bboylin.gank.Data.Entity.HomeResponse;
import com.bboylin.gank.Utils.GsonConverterFactory;

/**
 * Created by lin on 2016/11/1.
 */

@Preferences
public interface HomePref {

    void setDateResponse(DateResponse dateResponse);

    DateResponse getDateResponse();

    void setTodayResponse(HomeResponse homeResponse);

    HomeResponse getTodayResponse();

    class Factory {
        public static HomePref create(Context context) {
            Treasure.setConverterFactory(new GsonConverterFactory());
            return Treasure.get(context, HomePref.class);
        }
    }
}
