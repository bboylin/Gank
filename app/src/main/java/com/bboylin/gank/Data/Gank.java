package com.bboylin.gank.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lin on 2016/11/1.
 */

public class Gank {
    /**
     * _id : 56cc6d23421aa95caa707bba
     * createdAt : 2015-08-06T02:05:32.826Z
     * desc : 一个查看设备规格的库，并且可以计算哪一年被定为“高端”机
     * publishedAt : 2015-08-06T04:16:55.575Z
     * type : android
     * url : https://github.com/facebook/device-year-class
     * used : true
     * who : 有时放纵
     */
    @SerializedName("_id")
    public String id;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("desc")
    public String desc;
    @SerializedName("publishedAt")
    public String publishedAt;
    @SerializedName("type")
    public String type;
    @SerializedName("url")
    public String url;
    @SerializedName("used")
    public boolean used;
    @SerializedName("who")
    public String who;

    @Override
    public boolean equals(Object obj) {
        if (obj==null){
            return this==null;
        }else if (getClass()!=obj.getClass()){
            return false;
        }else {
            return ((Gank) obj).url.equals(url);
        }
    }
}