package com.ktr.newsapp.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;

import com.ktr.newsapp.R;
import com.ktr.newsapp.ui.abstractui.AbstractActivity;
import com.ktr.newsapp.ui.home.fragmentManager.HomeFragmentManager;
import com.ktr.newsapp.ui.home.menu.DrawerLeftMenuFragment;

public class MainActivity extends AbstractActivity implements DrawerLeftMenuFragment.OnLeftMenuItemClickListener {

    DrawerLayout drawerLayout;

    public HomeFragmentManager homeFragmentManager;

    public static int MAIN_TAG = HomeFragmentManager.news_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragmentManager = new HomeFragmentManager(this, R.id.fragment_container_layout);
        initView();

//        startActivity(new Intent(this, TestActivity.class));
//        finish();
    }

    private void initView() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayou);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                drawerLayout.setTag(null);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (drawerLayout.getTag() == null) return;

                String title = drawerLayout.getTag().toString();

                switch (title) {
                    case "新闻":
                        MAIN_TAG = HomeFragmentManager.news_tag;
                        homeFragmentManager.changeFragmentByCache(HomeFragmentManager.news_tag);
                        break;
                }
            }
        };

        toggle.syncState();
        drawerLayout.setDrawerListener(toggle);

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDrawerLayout();
            }
        });

        homeFragmentManager.changeFragmentByCache(HomeFragmentManager.news_tag);
    }

    @Override
    public void onMenuItemClick(String title) {

        drawerLayout.setTag(title);

        closeDrawerLayout();
    }

    private void closeDrawerLayout() {

        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }
}
