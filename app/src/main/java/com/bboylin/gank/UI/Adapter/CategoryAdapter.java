package com.bboylin.gank.UI.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bboylin.gank.Data.CommonPref;
import com.bboylin.gank.Data.Gank;
import com.bboylin.gank.Event.UrlClickEvent;
import com.bboylin.gank.R;
import com.bboylin.gank.Utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2016/12/17.
 */

public class CategoryAdapter extends BaseQuickAdapter<Gank> {
    Context mContext;
    CommonPref mCommonPref;
    public CategoryAdapter(Context context,int layoutResId, List<Gank> data) {
        super(layoutResId, data);
        mContext=context;
        mCommonPref = CommonPref.Factory.create(mContext);
        if (mCommonPref.getLikeItems()==null){
            mCommonPref.setLikeItems(new ArrayList<>());
        }
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Gank gank) {
        ImageView starImageView = (ImageView) baseViewHolder.getView(R.id.btn_star);
        if (mCommonPref.getLikeItems().contains(gank)){
            starImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        baseViewHolder.setText(R.id.category_title,gank.desc)
                .setText(R.id.category_who,gank.who)
                .setText(R.id.category_date,gank.publishedAt.split("T")[0])
                .setOnClickListener(R.id.btn_star, v -> {
                    if (mCommonPref.getLikeItems().contains(gank)){
                        starImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        List<Gank> list=mCommonPref.getLikeItems();
                        list.remove(gank);
                        mCommonPref.setLikeItems(list);
                        Toast.makeText(mContext,"已取消收藏",Toast.LENGTH_SHORT).show();
                    }else {
                        starImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                        List<Gank> list=mCommonPref.getLikeItems();
                        list.add(gank);
                        mCommonPref.setLikeItems(list);
                        Toast.makeText(mContext,"已收藏",Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(R.id.category_title,v -> RxBus.getDefault().post(new UrlClickEvent(gank.url,UrlClickEvent.TEXT)));
    }
}
