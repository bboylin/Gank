package com.bboylin.gank.UI.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/11/28.
 */

public class DetailImageFragment extends BaseFragment {
    private static volatile DetailImageFragment instance = null;
    @BindView(R.id.big_image)
    ImageView mImageView;
    private String date;

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
        ButterKnife.bind(this, view);
        date = CommonPref.Factory.create(getActivity()).getWebViewGank().publishedAt.split("T")[0];
        setupToolBar(date);
        if (CommonPref.Factory.create(getActivity()).getFirstImage()) {
            Toast.makeText(getContext(), "长按图片可保存到本地哦", Toast.LENGTH_SHORT).show();
            CommonPref.Factory.create(getContext()).setFirstImage(false);
        }
        Picasso.with(getContext())
                .load(CommonPref.Factory.create(getContext()).getWebViewGank().url)
                .resize(MainActivity.screenWidth, MainActivity.screenHeight / 2)
                .centerCrop()
                .into(mImageView);
        mImageView.setOnLongClickListener(v -> {
            saveImage();
            return true;
        });
        return view;
    }

    private void saveImage() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com/bboylin/gank/";
        //获取内部存储状态  
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写  
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(dir + date + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            Bitmap mBitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
