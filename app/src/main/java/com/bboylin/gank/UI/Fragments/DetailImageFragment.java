package com.bboylin.gank.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.MainActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/11/28.
 */

public class DetailImageFragment extends BaseFragment {
    private static volatile DetailImageFragment instance = null;
    @BindView(R.id.big_image)
    ImageView mImageView;

    public DetailImageFragment() {
        tag = "image";
    }

    public static DetailImageFragment getInstance() {
        if (instance == null) {
            synchronized (DetailImageFragment.class) {
                if (instance == null) {
                    instance = new DetailImageFragment();
                }
            }
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_image, container, false);
        ButterKnife.bind(this,view);
        setupToolBar("图片详情");
        Picasso.with(getContext())
                .load(CommonPref.Factory.create(getContext()).getWebViewUrl())
                .resize(MainActivity.screenWidth,MainActivity.screenHeight/2)
                .centerCrop()
                .into(mImageView);
        return view;
    }
}
