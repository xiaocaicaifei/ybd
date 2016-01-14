/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.SideBar;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.xx.XxIndexSlideView.OnSlideListener;

/**
 * 消息-通讯录
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxTxlActivity extends BaseActivity implements OnClickListener {

    private LinearLayout         xdpyLayout;                               //新的朋友
    private LinearLayout         tjdvLayout;                               //推荐大V
    BaseAdapter               txlAdapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    ListView                  txlListView;                                //通讯录
    SideBar                   zmSideBar;                                  //字母

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl);
        initPublicView("通讯录", R.drawable.login_fh, 0, null, null);
        init();
        NetWork.submit(activity, init);
    }

    /**
     * 初始化控件
     */
    private void init() {
        xdpyLayout=(LinearLayout) findViewById(R.id.xdpy_ll);
        xdpyLayout.setOnClickListener(this);
        tjdvLayout=(LinearLayout) findViewById(R.id.tjdv_ll);
        tjdvLayout.setOnClickListener(this);
        
        txlListView = (ListView) findViewById(R.id.txl_lv);
        for (int i = 0; i < 5; i++) {
            Map<String, Object> m = new HashMap<String, Object>();
            list.add(m);
        }
        zmSideBar = (SideBar) findViewById(R.id.zm_sb);
        zmSideBar.setListView(txlListView);
        txlAdapter = new XxTxlAdapter(list, this);
        txlListView.setAdapter(txlAdapter);
        txlAdapter.notifyDataSetChanged();
    }

    INetWork init = new INetWork() {

                      @Override
                      public boolean validate() {
                          return true;
                      }

                      @Override
                      public Data getSubmitData() throws Exception {
                          Data data = new Data("myfriend/selectMyfriendByUserId.json");
                          data.addData("user_id",
                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                          return data;
                      }

                      @SuppressWarnings("unchecked")
                      @Override
                      public void result(String result) throws Exception {
                          Map<String, Object> map = (Map<String, Object>) PaseJson
                              .paseJsonToObject(result);
                          list.clear();
                          list.addAll((List<Map<String, Object>>) map.get("data"));
                          txlAdapter.notifyDataSetChanged();
                      }
                  };

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.xdpy_ll://新的朋友
                
                break;
            case R.id.tjdv_ll://推荐大V
                intent.setClass(activity, XxTxlTjdvActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
