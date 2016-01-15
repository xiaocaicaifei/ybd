/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.delListView.ListViewCompat;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.xx.XxIndexSlideView.OnSlideListener;

/**
 * 消息-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxIndexActivity extends BaseActivity implements OnClickListener,OnSlideListener {

    BaseAdapter               xxAdapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    ListViewCompat          listView;
    private XxIndexSlideView mLastSlideViewWithStatusOn;

    @Override
    protected void initComponentBase() {
        //        view = inflater.inflate(R.layout.xx_index, null, false);
        setContentView(R.layout.xx_index);
        initPublicView("消息列表", R.drawable.xx_title_left, 0, XxIndexActivity.this, null);
        init();
        initBroadcast();
        //        NetWork.submit(activity, init);
    }

    /**
     * 初始化控件
     */
    private void init() {
        listView = (ListViewCompat) findViewById(R.id.xx_dslv);
//        for(int i=0;i<5;i++){
//            Map<String, Object> m=new HashMap<String, Object>();
//            list.add(m);
//        }
        xxAdapter=new XxIndexAdapter(list, this);
        listView.setAdapter(xxAdapter);
        xxAdapter.notifyDataSetChanged();
    }
    
    /**
     * 初始化广播
     */
    private void initBroadcast(){
        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
            @SuppressWarnings("unchecked")
            @Override
            public void onReceive(Context context, Intent intent) {
               Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(intent.getExtras().getString("content"));
               list.add(map);
               xxAdapter.notifyDataSetChanged();
            }
        }, BroadcaseUtil.XX_LT);
    }

    //    INetWork init=new INetWork() {
    //        
    //        @Override
    //        public boolean validate() {
    //            return true;
    //        }
    //        
    //        @Override
    //        public Data getSubmitData() throws Exception {
    //            Data data=new Data("auser/selectUserById.json");
    //            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
    //            return data;
    //        }
    //
    //        @SuppressWarnings("unchecked")
    //        @Override
    //        public void result(String result) throws Exception {
    //            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
    //            
    //        }
    //    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_rl:
                Intent intent=new Intent();
                intent.setClass(activity, XxTxlActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (XxIndexSlideView) view;
        }
    }

}
