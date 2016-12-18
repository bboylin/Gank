package com.bboylin.gank.UI.Adapter;

import com.bboylin.gank.Data.Gank;
import com.bboylin.gank.Event.UrlClickEvent;
import com.bboylin.gank.R;
import com.bboylin.gank.Utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lin on 2016/12/17.
 */

public class CategoryAdapter extends BaseQuickAdapter<Gank> {
    public CategoryAdapter(int layoutResId, List<Gank> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Gank gank) {
        baseViewHolder.setText(R.id.category_title,gank.desc)
                .setText(R.id.category_who,gank.who)
                .setText(R.id.category_date,gank.publishedAt.split("T")[0])
                .setOnClickListener(R.id.category_title,v -> RxBus.getDefault().post(new UrlClickEvent(gank.url,UrlClickEvent.TEXT)));
    }
}
