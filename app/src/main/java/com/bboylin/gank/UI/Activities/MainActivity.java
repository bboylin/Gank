package com.bboylin.gank.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.bboylin.gank.Data.CommonPref;
import com.bboylin.gank.Data.ItemTypeList;
import com.bboylin.gank.Event.UrlClickEvent;
import com.bboylin.gank.Net.GankApi;
import com.bboylin.gank.Net.MainRepository;
import com.bboylin.gank.R;
import com.bboylin.gank.UI.Fragments.CategoryFragment;
import com.bboylin.gank.UI.Fragments.DetailImageFragment;
import com.bboylin.gank.UI.Fragments.DetailWebFragment;
import com.bboylin.gank.UI.Fragments.GirlFragment;
import com.bboylin.gank.UI.Fragments.HomeFragment;
import com.bboylin.gank.Utils.RxBus;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.search_view)
    MaterialSearchView searchView;
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
        initSearchView();
        mMainRepository = MainRepository.getInstance(this);
        mCommonPref=CommonPref.Factory.create(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mMainRepository.getDataFromNet(new ItemTypeList())
                .subscribe(o -> Logger.d(o),
                        throwable -> Logger.e(throwable,"error in main http queue"),
                        () -> Logger.d("finish"));
        replaceFragment(HomeFragment.getInstance(), R.id.fragment_container);
        navigationView.setNavigationItemSelectedListener(this);
        register();
        WindowManager mWindowManager = this.getWindowManager();
        screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
    }

    private void register() {
        RxBus.getDefault().toObserverable(UrlClickEvent.class)
                .compose(applySchedulers())
                .doOnNext(urlClickEvent -> mCommonPref.setWebViewUrl(urlClickEvent.url))
                .subscribe(urlClickEvent -> {
                    switch (urlClickEvent.type){
                        case UrlClickEvent.TEXT:
                            replaceFragment(DetailWebFragment.getInstance(), R.id.fragment_container);
                            break;
                        case UrlClickEvent.IMAGE:
                            replaceFragment(DetailImageFragment.getInstance(), R.id.fragment_container);
                            break;
                    }
                },throwable -> Toast.makeText(this,"出错了",Toast.LENGTH_SHORT));
    }

    private void initSearchView() {
        searchView.setVoiceSearch(false);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
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
                replaceFragment(HomeFragment.getInstance(), R.id.fragment_container);
                break;
            case R.id.nav_android:
                mCommonPref.setCategory(GankApi.ANDROID);
                replaceFragment(new CategoryFragment(),R.id.fragment_container);
                break;
            case R.id.nav_ios:
                mCommonPref.setCategory(GankApi.IOS);
                replaceFragment(new CategoryFragment(),R.id.fragment_container);
                break;
            case R.id.nav_front_end:
                mCommonPref.setCategory(GankApi.FRONT_END);
                replaceFragment(new CategoryFragment(),R.id.fragment_container);
                break;
            case R.id.nav_welfare:
                replaceFragment(GirlFragment.getInstance(),R.id.fragment_container);
                break;
            case R.id.nav_about:
                Toast.makeText(this, "about is under construction", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(this, "feedback is under construction", Toast.LENGTH_LONG).show();
                break;
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
