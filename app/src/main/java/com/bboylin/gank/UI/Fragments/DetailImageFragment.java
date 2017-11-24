package com.bboylin.gank.UI.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.MainActivity;
import com.bboylin.gank.Utils.PermissionUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin on 2016/11/28.
 */

public class DetailImageFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.big_image)
    ImageView mImageView;
    private String date;
    private Gank gank;
    private PopupWindow popupWindow;
    private CommonPref commonPref;
    private View view;
    private static final String TAG = DetailImageFragment.class.getCanonicalName();
    private static final String[] STORAGE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_STORAGE = 1;

    public DetailImageFragment() {
        tag = "image";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_image, container, false);
        ButterKnife.bind(this, view);
        commonPref = CommonPref.Factory.create(getContext());
        gank = commonPref.getWebViewGank();
        date = gank.publishedAt.split("T")[0];
        setupToolBar(date);
        if (commonPref.getFirstImage()) {
            Toast.makeText(getContext(), "长按图片可保存到本地哦", Toast.LENGTH_SHORT).show();
            commonPref.setFirstImage(false);
        }
        Picasso.with(getContext())
                .load(gank.url)
                .resize(MainActivity.screenWidth, MainActivity.screenHeight / 2)
                .centerCrop()
                .into(mImageView);
        mImageView.setOnLongClickListener(v -> {
            openPopupWindow();
            return true;
        });
        mImageView.setOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }

    private void openPopupWindow() {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1);
            }
        });
        //设置PopupWindow的View点击事件
        setOnPopupViewClickListener(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClickListener(View view) {
        TextView save, like, cancel;
        save = (TextView) view.findViewById(R.id.save_image);
        like = (TextView) view.findViewById(R.id.like_image);
        cancel = (TextView) view.findViewById(R.id.tv_cancel);
        save.setOnClickListener(this);
        like.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    private void saveImage() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "write external storage permissions has NOT been granted. Requesting permissions.");
            requestStoragePermissions();
            return;
        }
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

    /**
     * Requests the external storage permissions.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying EXTERNAL STORAGE permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(view, R.string.permission_storage_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(getActivity(), STORAGE_PERMISSION,
                                            REQUEST_STORAGE);
                        }
                    })
                    .show();
        } else {
            // permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(getActivity(), STORAGE_PERMISSION, REQUEST_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            Log.i(TAG, "Received response for storage permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(view, R.string.permision_available_storage,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Log.i(TAG, "storage permissions were NOT granted.");
                Snackbar.make(view, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_image:
                saveImage();
                popupWindow.dismiss();
                break;
            case R.id.like_image:
                List<Gank> list = commonPref.getLikeItems();
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(gank);
                commonPref.setLikeItems(list);
                Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
            default:
                break;
        }
    }
}
