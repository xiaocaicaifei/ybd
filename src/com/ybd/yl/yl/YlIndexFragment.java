/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.yl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;
import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.home.HomeClickListener;

/**
 * 个人-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class YlIndexFragment extends BaseFragment implements HomeClickListener, OnClickListener {
    private ListView                  listView;
    private BaseAdapter               adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    DrawerLayout                      drawerLayout;

    @Override
    protected void initComponentBase() {
        view = inflater.inflate(R.layout.yl_index, null, false);
        initPublicView("艺论", R.drawable.yl_sz, R.drawable.yl_sc, YlIndexFragment.this,
            YlIndexFragment.this);
        initListView();
        initDrawerLayout();
    }

    private void initListView() {
        listView = (ListView) view.findViewById(R.id.list_lv);
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            list.add(map);
        }
        adapter = new YlIndexAdapter(list, activity);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
            Gravity.RIGHT);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        LayoutParams lp = drawerLayout.getLayoutParams();
        lp.height = dm.heightPixels;
        drawerLayout.setLayoutParams(lp);//设置高度为全屏高度
        drawerLayout.setDrawerListener(new DrawerListener() {

            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                float leftScale = 1 - 0.3f * scale;

                ViewHelper.setScaleX(mMenu, leftScale);
                ViewHelper.setScaleY(mMenu, leftScale);
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                ViewHelper.setPivotX(mContent, 0);
                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
                ViewHelper.setScaleX(mContent, rightScale);
                ViewHelper.setScaleY(mContent, rightScale);
            }

            @Override
            public void onDrawerOpened(View arg0) {
            }

            @Override
            public void onDrawerClosed(View arg0) {
                drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    @Override
    public void onclick(View v) {

    }

    @Override
    public void onClick(View v) {
    }

}
