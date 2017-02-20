package com.bboylin.gank.UI.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Event.GankClickEvent;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.MainActivity;
import com.bboylin.gank.Utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2016/12/16.
 */

public class GirlAdapter extends BaseQuickAdapter<Gank> {
    Context mContext;
    CommonPref mCommonPref;

    public GirlAdapter(Context context, int layoutResId, List<Gank> data) {
        super(layoutResId, data);
        this.mContext = context;
        mCommonPref = CommonPref.Factory.create(mContext);
        if (mCommonPref.getLikeItems() == null) {
            mCommonPref.setLikeItems(new ArrayList<>());
        }
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Gank gank) {
        ImageView starImageView = (ImageView) baseViewHolder.getView(R.id.btn_star);
        if (mCommonPref.getLikeItems().contains(gank)) {
            starImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
        }else {
            starImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
        baseViewHolder.setOnClickListener(R.id.btn_star, v -> {
            if (mCommonPref.getLikeItems().contains(gank)) {
                starImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                List<Gank> list = new ArrayList<>();
                list.addAll(mCommonPref.getLikeItems());
                list.remove(gank);
                mCommonPref.setLikeItems(list);
                Toast.makeText(mContext, "已取消收藏", Toast.LENGTH_SHORT).show();
            } else {
                starImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
                List<Gank> list = new ArrayList<>();
                list.addAll(mCommonPref.getLikeItems());
                list.add(0,gank);
                mCommonPref.setLikeItems(list);
                Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
            }
        })
                .setText(R.id.tvShowTime, gank.publishedAt.split("T")[0])
                .setOnClickListener(R.id.girl_image, v -> RxBus.getDefault().post(new GankClickEvent(gank, GankClickEvent.IMAGE)));
        Picasso.with(mContext)
                .load(gank.url)
                .resize(MainActivity.screenWidth / 2 - 2, (int) ((float) MainActivity.screenWidth / 1.618f))
                .centerCrop()
                .into((ImageView) baseViewHolder.getView(R.id.girl_image));
    }
}
