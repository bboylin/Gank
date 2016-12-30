package com.bboylin.gank.UI.Widget;

import android.view.View;
import android.widget.Toast;

import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Activities.BaseActivity;
import com.bboylin.gank.UI.Fragments.DetailImageFragment;
import com.bboylin.gank.UI.Fragments.DetailWebFragment;

import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;

/**
 * Created by shaohui on 2016/12/10.
 */

public class ShareBottomDialog extends BaseBottomDialog implements View.OnClickListener {

    private ShareListener mShareListener;
    private int type;
    public static final int APP = 0;
    public static final int IMAGE = 1;
    public static final int WEBURL = 2;

    @Override
    public int getLayoutRes() {
        return R.layout.layout_bottom_share;
    }

    @Override
    public void bindView(final View v) {
        v.findViewById(R.id.share_qq).setOnClickListener(this);
        v.findViewById(R.id.share_qzone).setOnClickListener(this);
        v.findViewById(R.id.share_weibo).setOnClickListener(this);
        v.findViewById(R.id.share_wx).setOnClickListener(this);
        v.findViewById(R.id.share_wx_timeline).setOnClickListener(this);
        if (BaseActivity.fragmentTag.equals("home")) {
            type = APP;
        } else if (BaseActivity.fragmentTag.equals(DetailImageFragment.getInstance().tag)) {
            type = IMAGE;
        } else if (BaseActivity.fragmentTag.equals(DetailWebFragment.getInstance().tag)) {
            type = WEBURL;
        } else {
            type = -1;
        }

        mShareListener = new ShareListener() {
            @Override
            public void shareSuccess() {
                Toast.makeText(v.getContext(), "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shareFailure(Exception e) {
                Toast.makeText(v.getContext(), "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shareCancel() {
                Toast.makeText(v.getContext(), "取消分享", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    public void onClick(View view) {
        int platform=SharePlatform.QQ;
        switch (view.getId()) {
            case R.id.share_qq:
                break;
            case R.id.share_qzone:
                platform=SharePlatform.QZONE;
                break;
            case R.id.share_weibo:
                platform=SharePlatform.WEIBO;
                break;
            case R.id.share_wx_timeline:
                platform=SharePlatform.WX_TIMELINE;
                break;
            case R.id.share_wx:
                platform=SharePlatform.WX;
                break;
        }
        switch (type) {
            case APP:
                ShareUtil.shareText(getContext(), platform, "我发现了一款不错的APP，推荐给你哦，干货营下载地址：http://fir.im/3t7j",
                        mShareListener);
                break;
            case IMAGE:
                /*ShareUtil.shareImage(getContext(), platform, CommonPref.Factory.create(getContext()).getWebViewGank().url,
                        mShareListener);*/
                Toast.makeText(getContext(),"暂不支持分享图片，长按图片保存到本地",Toast.LENGTH_SHORT).show();
                break;
            case WEBURL:
                ShareUtil.shareText(getContext(), platform, "我在干货营上发现了一篇不错的文章《" + CommonPref.Factory.create(getContext()).getWebViewGank().desc + "》，推荐给你哦，链接:" + CommonPref.Factory.create(getContext()).getWebViewGank().url + "\n干货营APP下载地址：http://fir.im/3t7j",
                        mShareListener);
                break;
        }
        dismiss();
    }
}
