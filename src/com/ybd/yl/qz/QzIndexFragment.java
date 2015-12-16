/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.qz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;

import com.nineoldandroids.view.ViewHelper;
import com.ybd.common.C;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.ScreenDisplay;
import com.ybd.common.xListView.XListView;
import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.home.HomeClickListener;

/**
 * 圈子-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class QzIndexFragment extends BaseFragment implements HomeClickListener, OnClickListener {
    private XListView                 listView;
    private BaseAdapter               adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    DrawerLayout                      drawerLayout;

    @Override
    protected void initComponentBase() {
        view = inflater.inflate(R.layout.qz_index, null, false);
        initPublicView("圈子", R.drawable.yl_sz, R.drawable.yl_sc, QzIndexFragment.this,
            QzIndexFragment.this);
        initListView();
        //        initDrawerLayout();
        page = 1;
        NetWork.submit(activity, qzList);
    }

    private void initListView() {
        listView = (XListView) view.findViewById(R.id.list_lv);
        int height = ScreenDisplay.getScreenHeight2(activity)
                     - ScreenDisplay.dip2px(activity, R.dimen.uniform_title_height)
                     - ScreenDisplay.dip2px(activity, R.dimen.nav_bar_size)
                     -ScreenDisplay.dip2px(activity, 35);
        //动态设置Listview的高度
        ScreenDisplay.setViewWidthAndHeight(listView, 0, height);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        setXListViewListener(listView, qzList,list);
        adapter = new QzIndexAdapter(list, activity);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化左边滑动设置
     */
    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
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
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    @Override
    public void onHomeclick(View v) {

    }

    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.right_rl:
			Intent intent=new Intent();
			intent.setClass(activity, QzSctpActivity.class);
			startActivityForResult(intent,0);
			break;

		default:
			break;
		}
    }

    /**
     * 查询列表的信息
     */
    INetWork qzList = new INetWork() {

                        @Override
                        public boolean validate() {
                            return true;
                        }

                        @Override
                        public Data getSubmitData() throws Exception {
                            Data data = new Data("circle/listCircle.json");
                            data.addData("user_id",
                                PropertiesUtil.read(activity, PropertiesUtil.USERID));
                            data.addData("is_friend", "1");
                            data.addData("is_export", "1");
                            data.addData("is_fans", "1");
                            data.addData("is_dv", "1");
                            data.addData("fbtype_1", "1");
                            data.addData("fbtype_2", "1");
                            data.addData("fbtype_3", "1");
                            data.addData("page", page);
                            data.addData("limit", C.PAGE_SIZE);
                            return data;
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void result(String result) throws Exception {
                            Map<String, Object> map = (Map<String, Object>) PaseJson
                                .paseJsonToObject(result);
                            List<Map<String, Object>> l=(List<Map<String, Object>>) map.get("circleList");
                            list.addAll(l);
                            adapter.notifyDataSetChanged();
                            int total=Integer.parseInt(map.get("totalCount").toString());
                            if (l.size()+(page-1)*C.PAGE_SIZE >=total) {
                                listView.setPullLoadMorEnable(false);
                            }else{
                                listView.setPullLoadMorEnable(true);
                            }
                        }
                    };
}
