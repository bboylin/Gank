package com.bboylin.gank.UI;

/**
 * Created by lin on 2017/11/24.
 */

public interface ILoadingState {
    /**
     * 第一页加载没拿到数据时调用
     *
     * @param imageResId    提示图片的ResId
     * @param tipsTitleId   提示标题的ResId
     * @param tipsContentId 提示内容的ResId
     */
    void onLoadEmpty(int imageResId, int tipsTitleId, int tipsContentId);

    void onFailed(OnReloadListener onReloadListener);

    interface OnReloadListener {
        void onReload();
    }
}
