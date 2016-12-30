package com.bboylin.gank.Event;

import com.bboylin.gank.Data.Entity.Gank;

/**
 * Created by lin on 2016/11/12.
 */

public class GankClickEvent {
    public Gank mGank;
    public int type;
    public static final int TEXT = 0;
    public static final int IMAGE = 1;

    public GankClickEvent(Gank mGank, int type) {
        this.mGank = mGank;
        this.type = type;
    }
}
