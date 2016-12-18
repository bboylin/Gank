package com.bboylin.gank.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lin on 2016/11/21.
 */

public class ItemTypeList {
    public static final int TODAY=0;
    public static final int ANDROID=1;
    public static final int IOS=2;
    public static final int FRONT_END=3;
    public static final int WARFARE=4;

    public List<Integer> typeList;

    public ItemTypeList(List<Integer> typeList){
        this.typeList=typeList;
    }

    public ItemTypeList(){
        this.typeList= Arrays.asList(TODAY,ANDROID,IOS,FRONT_END,WARFARE);
    }
}
