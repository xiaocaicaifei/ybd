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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.L;
import com.ybd.common.delListView.ListViewCompat;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.xx.XxIndexSlideView.OnSlideListener;
import com.ybd.yl.xx.dao.XxLtDao;

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
    XxLtDao xxLtDao;//聊天的数据库操作类

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_index);
        initPublicView("消息列表", R.drawable.xx_title_left, 0, XxIndexActivity.this, null);
        xxLtDao=new XxLtDao(activity);
        init();
        initBroadcast();
        findDbMsg();
    }

    /**
     * 初始化控件
     */
    private void init() {
        listView = (ListViewCompat) findViewById(R.id.xx_dslv);
        xxAdapter=new XxIndexAdapter(list, this);
        listView.setAdapter(xxAdapter);
        xxAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map=list.get(position);//一直存在报序列化问题,所以下面又新建了一个Map
                if(PaseJson.getMapMsg(map,"type").equals("1")){//如果是1说明是对话类型
                    Intent intent=new Intent();
                    Map<String, Object> m=new HashMap<String, Object>();
                    m.put("nick_name", PaseJson.getMapMsg(map,"sender_name"));
                    m.put("buser_id", PaseJson.getMapMsg(map,"sender_id"));
                    m.put("icon_url", PaseJson.getMapMsg(map,"icon_url"));
                    m.put("voipAccount", PaseJson.getMapMsg(map,"voip_account"));
                    intent.putExtra("xxObject", (Serializable)m);
                    intent.setClass(activity, XxTxlLtActivity.class);
                    startActivity(intent);
                    xxLtDao.updateTalkUser(0, PaseJson.getMapMsg(map,"sender_id"));//修改数据库未读消息数量
//                    findDbMsg();
                    ((Map<String, Object>)list.get(position)).put("unread_num", "0");
                    xxAdapter.notifyDataSetChanged();
                }else if(PaseJson.getMapMsg(map,"type").equals("4")){//艺论群组
                    Intent intent=new Intent();
                    intent.setClass(activity, XxQzActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    
    /**
     * 查询本地数据库中的聊天记录信息
     */
    private void findDbMsg(){
        list.clear();
        list.addAll(xxLtDao.findAllLt());
        //添加艺论一下，在列表的最上面
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("sender_icon_url", "assets://xx_index_qz.png");
        map.put("sender_name", "艺论一下");
        map.put("send_content", "畅玩艺论群组，分享拍卖乐趣");
        map.put("send_time", "");
        map.put("unread_num", "0");
        map.put("type", "4");
        list.add(0,map);
        xxAdapter.notifyDataSetChanged();
        listView.setSelection(listView.getBottom());
    }
    
    /**
     * 初始化广播()
     */
    private void initBroadcast(){
        //接收聊天消息的广播
        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
//            @SuppressWarnings("unchecked")
            @Override
            public void onReceive(Context context, Intent intent) {
//               Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(intent.getExtras().getString("content"));
//               list.add(map);
//               xxAdapter.notifyDataSetChanged();
                findDbMsg();//重新查询数据库更新（这样可能会出现刷新不动的情况，如果出现后面再做调整）
            }
        }, BroadcaseUtil.XX_LT);
        
        //接收到当前聊天窗口已经接收到信息的广播
        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
              findDbMsg();//重新查询数据库更新（这样可能会出现刷新不动的情况，如果出现后面再做调整）
          }
      }, BroadcaseUtil.XX_LT_RECEIVED);
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
    
    public void delXxList(String userid){
        xxLtDao.delete(userid);
    }

}
