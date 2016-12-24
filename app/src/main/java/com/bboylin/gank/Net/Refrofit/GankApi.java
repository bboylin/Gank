package com.bboylin.gank.Net.Refrofit;

/**
 * Created by lin on 2016/11/1.
 */

public class GankApi {
    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * •数据类型： welfare | android | iOS | video | resource | 前端 | all
     * •请求个数： 数字，大于0
     * •第几页：数字，大于0
     * <p>
     * 例：•http://gank.io/api/data/Android/10/1
     * •http://gank.io/api/data/福利/10/1
     * •http://gank.io/api/data/iOS/20/2
     * •http://gank.io/api/data/all/20/2
     * <p>
     * 每日数据： http://gank.io/api/day/年/月/日
     * <p>
     * 例：
     * •http://gank.io/api/day/2015/08/06
     * <p>
     * 随机数据：http://gank.io/api/random/data/分类/个数
     * •数据类型：welfare | android | iOS | video | resource | 前端
     * •个数： 数字，大于0
     * <p>
     * 例：•http://gank.io/api/random/data/Android/20
     */
    public static final String BASE_URL="http://gank.io/api/";

    public static final String HOME="TODAY";
    public static final String ANDROID="Android";
    public static final String IOS="iOS";
    public static final String FRONT_END="前端";
    public static final String WELFARE="福利";
}
