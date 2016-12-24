package com.bboylin.gank.Data.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lin on 2016/12/16.
 */

public class CategoryResponse extends BaseResponse {
    @SerializedName("results")
    public List<Gank> mGankList;
}
