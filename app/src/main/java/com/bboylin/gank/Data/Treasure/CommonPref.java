package com.bboylin.gank.Data.Treasure;

import android.content.Context;

import com.baoyz.treasure.Clear;
import com.baoyz.treasure.Default;
import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Treasure;
import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Utils.GsonConverterFactory;

import java.util.List;

/**
 * Created by lin on 2016/11/13.
 */

@Preferences
public interface CommonPref {

    void setWebViewGank(Gank gank);

    Gank getWebViewGank();

    void setCategory(String category);

    String getCategory();

    void setLikeItems(List<Gank> likeItems);

    List<Gank> getLikeItems();

    void setFirstImage(boolean first);

    @Default("true")
    boolean getFirstImage();

    @Clear
    void clear();

    class Factory {
        public static CommonPref create(Context context) {
            Treasure.setConverterFactory(new GsonConverterFactory());
            return Treasure.get(context, CommonPref.class);
        }
    }
}
