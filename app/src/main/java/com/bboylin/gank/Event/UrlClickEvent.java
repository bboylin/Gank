package com.bboylin.gank.Event;

/**
 * Created by lin on 2016/11/12.
 */

public class UrlClickEvent {
    public String url;
    public int type;
    public static final int TEXT = 0;
    public static final int IMAGE = 1;

    public UrlClickEvent(String url, int type) {
        this.url = url;
        this.type = type;
    }
}
