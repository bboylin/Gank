package com.bboylin.gank.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.bboylin.gank.Data.Entity.Gank;
import com.bboylin.gank.Data.ItemTypeList;
import com.bboylin.gank.Data.Treasure.CommonPref;
import com.bboylin.gank.Event.GankClickEvent;
import com.bboylin.gank.Net.Refrofit.GankApi;
import com.bboylin.gank.Net.Repository.MainRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Fragments.Category.CategoryFragment;
import com.bboylin.gank.UI.Fragments.Category.GirlFragment;
import com.bboylin.gank.UI.Fragments.Category.HomeFragment;
import com.bboylin.gank.UI.Fragments.DetailImageFragment;
import com.bboylin.gank.UI.Fragments.DetailWebFragment;
import com.bboylin.gank.UI.Fragments.Like.LikeFragment;
import com.bboylin.gank.Utils.RxBus;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private MainRepository mMainRepository;
    private CommonPref mCommonPref;
    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mMainRepository = MainRepository.getInstance(this);
        mCommonPref = CommonPref.Factory.create(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mMainRepository.getDataFromNet(new ItemTypeList())
                .subscribe(o -> Logger.d(o),
                        throwable -> Logger.e(throwable, "error in main http queue"),
                        () -> Logger.d("finish"));
        replaceFragment(new HomeFragment(), R.id.fragment_container);
        navigationView.setNavigationItemSelectedListener(this);
        register();
        WindowManager mWindowManager = this.getWindowManager();
        screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
    }

    private void register() {
        RxBus.getDefault().toObserverable(GankClickEvent.class)
                .doOnNext(gankClickEvent -> mCommonPref.setWebViewGank(gankClickEvent.mGank))
                .compose(applySchedulers())
                .subscribe(gankClickEvent -> {
                    switch (gankClickEvent.type) {
                        case GankClickEvent.TEXT:
                            replaceFragment(new DetailWebFragment(), R.id.fragment_container);
                            break;
                        case GankClickEvent.IMAGE:
                            replaceFragment(new DetailImageFragment(), R.id.fragment_container);
                            break;
                    }
                }, throwable -> Toast.makeText(this, "出错了", Toast.LENGTH_SHORT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                if (fragmentTag.equals("web")){
                    // TODO: 2017/11/26 应该用activity承载webview
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_today:
                replaceFragment(new HomeFragment(), R.id.fragment_container);
                break;
            case R.id.nav_android:
                mCommonPref.setCategory(GankApi.ANDROID);
                replaceFragment(new CategoryFragment(), R.id.fragment_container);
                break;
            case R.id.nav_ios:
                mCommonPref.setCategory(GankApi.IOS);
                replaceFragment(new CategoryFragment(), R.id.fragment_container);
                break;
            case R.id.nav_front_end:
                mCommonPref.setCategory(GankApi.FRONT_END);
                replaceFragment(new CategoryFragment(), R.id.fragment_container);
                break;
            case R.id.nav_welfare:
                replaceFragment(new GirlFragment(), R.id.fragment_container);
                break;
            case R.id.nav_like:
                replaceFragment(new LikeFragment(), R.id.fragment_container);
                //startActivity(new Intent(MainActivity.this,LikeFragment.class));
                break;
            case R.id.nav_about:
                Gank gank = new Gank();
                gank.url = "https://github.com/bboylin/gank/blob/master/README.md";
                gank.desc = "关于";
                mCommonPref.setWebViewGank(gank);
                replaceFragment(new DetailWebFragment(), R.id.fragment_container);
                break;
            case R.id.nav_clear_cache:
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("确定要清空缓存吗？此操作不会清空收藏数据")
                        .setPositiveButton("确定", ((dialog, which) -> {
                            mMainRepository.clear();
                            Toast.makeText(this, "缓存已清空", Toast.LENGTH_SHORT).show();
                        }))
                        .setNegativeButton("取消", ((dialog, which) -> {
                        }))
                        .create()
                        .show();
                break;
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
