package com.bboylin.gank.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bboylin.gank.Data.CommonPref;
import com.bboylin.gank.Data.Gank;
import com.bboylin.gank.Event.UrlClickEvent;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.MainActivity;
import com.bboylin.gank.Utils.RxBus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/10/29.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int NORMAL = 1;
    private static final int IMAGE = 2;
    private List<Gank> mList;
    private Context mContext;
    CommonPref mCommonPref;

    public HomeAdapter(List<Gank> list, Context context) {
        mList = list;
        mContext = context;
        mCommonPref = CommonPref.Factory.create(mContext);
        if (mCommonPref.getLikeItems()==null){
            mCommonPref.setLikeItems(new ArrayList<>());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NORMAL:
                return new NormalViewHolder(parent);
            case IMAGE:
                return new ImageViewHolder(parent);
            case HEADER:
                //to do : add date title
                return null;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Gank gank = mList.get(position);
        if (gank.type.equals("福利")) {
            return IMAGE;
        } else {
            return NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            Gank gank = mList.get(position);
            normalViewHolder.itemView.setOnClickListener(v ->
                    RxBus.getDefault().post(new UrlClickEvent(gank.url,UrlClickEvent.TEXT)));
            normalViewHolder.dateTextView.setText(gank.publishedAt.split("T")[0]);
            normalViewHolder.typeTextView.setText("分类:" + gank.type);
            normalViewHolder.titleTextView.setText(gank.desc);
            if (mCommonPref.getLikeItems().contains(gank)){
                normalViewHolder.starImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
            normalViewHolder.starImageView.setOnClickListener(v -> {
                if (mCommonPref.getLikeItems().contains(gank)){
                    normalViewHolder.starImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    List<Gank> list=mCommonPref.getLikeItems();
                    list.remove(gank);
                    mCommonPref.setLikeItems(list);
                    Toast.makeText(mContext,"已取消收藏",Toast.LENGTH_SHORT).show();
                }else {
                    normalViewHolder.starImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                    List<Gank> list=mCommonPref.getLikeItems();
                    list.add(gank);
                    mCommonPref.setLikeItems(list);
                    Toast.makeText(mContext,"已收藏",Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof ImageViewHolder) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            String url=mList.get(position).url;
            imageViewHolder.itemView.setOnClickListener(v -> RxBus.getDefault().post(new UrlClickEvent(url,UrlClickEvent.IMAGE)));
            Picasso.with(mContext)
                    .load(url)
                    .resize(MainActivity.screenWidth,(int)((float)MainActivity.screenWidth/1.618f))
                    .centerCrop()
                    .into(imageViewHolder.mRatioImageView);
        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_girl_image) ImageView mRatioImageView;

        public ImageViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_girl_image, parent, false));
            ButterKnife.bind(this,itemView);
            int height=(int)((float)mRatioImageView.getMaxWidth()/1.618f);
            mRatioImageView.setMaxHeight(height);
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.today_title) TextView titleTextView;
        @BindView(R.id.today_type) TextView typeTextView;
        @BindView(R.id.today_date) TextView dateTextView;
        @BindView(R.id.btn_star) ImageView starImageView;

        public NormalViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.today_item, parent, false));
            ButterKnife.bind(this,itemView);
        }
    }
}