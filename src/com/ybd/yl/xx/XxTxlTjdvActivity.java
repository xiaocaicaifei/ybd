/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import com.ybd.common.C;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.xListView.XListView;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 消息-通讯录-推荐大V
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxTxlTjdvActivity extends BaseActivity implements OnClickListener {

    BaseAdapter               tjdvAdapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    XListView                  tjdvListView;                               //通讯录

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_tjdv);
        initPublicView("推荐大V", R.drawable.login_fh, 0, null, null);
        init();
        initList();
        NetWork.submit(activity, init);
    }

    /**
     * 初始化控件
     */
    private void init() {
       
    }
    /**
     * 初始化列表集合
     */
    /**
     * 初始化控件
     */
    private void initList() {
        tjdvListView = (XListView) findViewById(R.id.tjdv_lv);
        tjdvAdapter = new XxTxlTjdvAdapter(list, this);
        tjdvListView.setAdapter(tjdvAdapter);
        tjdvAdapter.notifyDataSetChanged();
        tjdvListView.setPullRefreshEnable(false);
        tjdvListView.setPullLoadMorEnable(true);
    }

    INetWork init = new INetWork() {

                      @Override
                      public boolean validate() {
                          return true;
                      }

                      @Override
                      public Data getSubmitData() throws Exception {
                          Data data = new Data("auser/selectDV.json");
                          data.addData("user_id",
                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                          data.addData("page", page);
                          data.addData("limit", C.PAGE_SIZE_USER);
                          return data;
                      }

                      @SuppressWarnings("unchecked")
                      @Override
                      public void result(String result) throws Exception {
                          Map<String, Object> map = (Map<String, Object>) PaseJson
                              .paseJsonToObject(result);
                          list.clear();
                          list.addAll((List<Map<String, Object>>) map.get("data"));
                          tjdvAdapter.notifyDataSetChanged();
                          
                          int total = Integer.parseInt(map.get("totalCount").toString());
                          if (list.size() + (page - 1) * C.PAGE_SIZE_USER >= total) {
                              tjdvListView.setPullLoadMorEnable(false);
                          } else {
                              tjdvListView.setPullLoadMorEnable(true);
                          }
                      }
                  };

    @Override
    public void onClick(View v) {
    }

}
