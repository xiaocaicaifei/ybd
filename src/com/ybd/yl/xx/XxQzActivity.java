/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.ybd.common.C;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.xListView.XListView;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 消息-群组
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxQzActivity extends BaseActivity implements OnClickListener {
    private XListView          qzListView;
    private BaseAdapter       qzAdapter;
    List<Map<String, Object>> list            = new ArrayList<Map<String, Object>>();

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_qz);
        initPublicView("艺论群组", R.drawable.login_fh,0, null, null);
        init();
        NetWork.submit(activity, qzxxNetwork);
    }

    /**
     * 初始化控件
     */
    private void init() {
        qzListView = (XListView) findViewById(R.id.qz_lv);
        qzAdapter = new XxQzAdapter(list, this);
        qzListView.setAdapter(qzAdapter);
        qzAdapter.notifyDataSetChanged();
        qzListView.setPullLoadMorEnable(true);
        qzListView.setPullRefreshEnable(false);

        qzListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Map<String, Object> map = ((Map<String, Object>) list.get(arg2-1));
                Intent intent=new Intent();
                Map<String, Object> m=new HashMap<String, Object>();
                m.put("nick_name", PaseJson.getMapMsg(map,"groupname"));
                m.put("buser_id", PaseJson.getMapMsg(map,"id"));
                m.put("icon_url", PaseJson.getMapMsg(map,"logo_url"));
                m.put("voipAccount", PaseJson.getMapMsg(map,"groupid"));//发送对象的voip 账号
                intent.putExtra("qzObject", (Serializable)m);
                intent.setClass(activity, XxQzLtActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        //        Intent intent = new Intent();
        switch (v.getId()) {//邀请按钮的点击事件
            case R.id.right_rl:
               
                break;
            default:
                break;
        }
    }
    private INetWork qzxxNetwork=new INetWork() {
        
        @Override
        public boolean validate() {
            return true;
        }
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("group/selectGroup.json");
            data.addData("page", page);
            data.addData("limit", C.PAGE_SIZE);
            return data;
        }
        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
            list.clear();
            list.addAll((List<Map<String, Object>> )map.get("groupList"));
            qzAdapter.notifyDataSetChanged();
        }
        
    };
}
