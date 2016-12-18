package com.bboylin.gank.UI.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bboylin.gank.Event.UrlClickEvent;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.MainActivity;
import com.bboylin.gank.Utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lin on 2016/12/16.
 */

public class GirlAdapter extends BaseQuickAdapter<String>{
    Context mContext;
    public GirlAdapter(Context context,int layoutResId, List<String> data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String string) {
        baseViewHolder.setOnClickListener(R.id.girl_image,v -> RxBus.getDefault().post(new UrlClickEvent(string,UrlClickEvent.IMAGE)));
        Picasso.with(mContext)
                .load(string)
                .resize(MainActivity.screenWidth/2-2,(int)((float)MainActivity.screenWidth/1.618f))
                .centerCrop()
                .into((ImageView) baseViewHolder.getView(R.id.girl_image));
    }
}
