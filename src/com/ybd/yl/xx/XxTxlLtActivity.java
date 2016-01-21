/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.tools.DateUtil;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.service.ReceiverService;
import com.ybd.yl.xx.dao.XxLtDao;

/**
 * 消息-通讯录-开始聊天
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxTxlLtActivity extends BaseActivity implements OnClickListener {
    Map<String, Object>       map;                                        //传过来的信息
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    ListView                  listView;
    BaseAdapter               adapter;
    EditText                  xxEditText;                                  //聊天输入框
    Button                    fsButton;                                    //发送消息的按钮
    XxLtDao ltDao;
    BroadcastReceiver xxLtBroadcastReceiver;//消息聊天的

    @SuppressWarnings("unchecked")
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_lt);
        ltDao=new XxLtDao(activity);
        String titleName = "聊天";
        if (this.getIntent().hasExtra("hyObject")) {//从通讯录进入
            map = (Map<String, Object>) this.getIntent().getExtras().getSerializable("hyObject");
            titleName = PaseJson.getMapMsg(map, "nick_name");
        }
        if (this.getIntent().hasExtra("xxObject")) {//从消息列表进入
            map = (Map<String, Object>) this.getIntent().getExtras().getSerializable("xxObject");
            titleName = PaseJson.getMapMsg(map, "nick_name");
        }
        initPublicView(titleName, R.drawable.login_fh, R.drawable.xx_txl_grzl, null, this);
        init();
        findAllMsg();//查询聊天记录
    }

    /**
     * 初始化控件
     */
    private void init() {
        listView = (ListView) findViewById(R.id.lt_lv);
        adapter = new XxTxlLtAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        xxEditText = (EditText) findViewById(R.id.xx_ev);
        fsButton = (Button) findViewById(R.id.fs_b);
        fsButton.setOnClickListener(this);
    }
    
    /**
     * 查询该用户的聊天记录
     */
    private void findAllMsg(){
        list.clear();
        list.addAll(ltDao.findUserAllLt(PaseJson.getMapMsg(map, "buser_id")));
        adapter.notifyDataSetChanged();
        listView.setSelection(list.size()-1);
    }
    /**
     * 初始化广播()
     */
    private void initBroadcast(){
        xxLtBroadcastReceiver=new BroadcastReceiver() {
            @SuppressWarnings("unchecked")
            @Override
            public void onReceive(Context context, Intent intent) {
               Map<String, Object> m=(Map<String, Object>) PaseJson.paseJsonToObject(intent.getExtras().getString("content"));
               if(PaseJson.getMapMsg(m, "voidAccount").equals(PaseJson.getMapMsg(map, "voidAccount"))){//如果当前界面聊天的Id和收到的发送消息的ID一样则显示，否则不显示
                   list.add(m);
                   adapter.notifyDataSetChanged();
                   listView.setSelection(list.size()-1);
                   
                   //发送广播，当前人已经显示信息，在列表页面就不需要再显示未读数量，也就是说这个人的未读数量是0
                   ltDao.updateTalkUser(0, PaseJson.getMapMsg(map,"buser_id"));
//                   BroadcaseUtil.sendBroadcase(BroadcaseUtil.XX_LT_RECEIVED, activity);
               }
            }
        };
        //接收聊天消息的广播
        BroadcaseUtil.registBroadcase(activity,xxLtBroadcastReceiver , BroadcaseUtil.XX_LT);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_rl:
                //                NetWork.submit(activity, new jwhyNetWork());
                break;
            case R.id.fs_b://发送按钮
                if (xxEditText.getText().toString().equals("")) {
                    toastShow("发送消息不能为空！");
                } else {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("sender_type", "0");//0，代表是本人发的，1代表是接收到他人发的消息
                    m.put("send_time", DateUtil.getTimeFormat("MM-dd HH:mm", new Date()));
                    m.put("send_content", xxEditText.getText().toString());
                    m.put("sender_icon_url", PaseJson.getMapMsg(map, "icon_url"));
                    m.put("sender_id", PaseJson.getMapMsg(map, "buser_id"));//不是本人的Id，是接收人的ID
                    m.put("sender_name",PaseJson.getMapMsg(map, "nick_name"));//不是本人的name，是接收人的name
                    list.add(m);
                    ltDao.addTalk(m);//将记录插入到聊天记录表中
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("type", "1");
                        jsonObject.put("send_content", xxEditText.getText().toString());
                        jsonObject.put("sender_icon_url",  PaseJson.getMapMsg(map, "icon_url"));
                        jsonObject.put("send_time", DateUtil.getTimeFormat("MM-dd HH:mm", new Date()));
                        jsonObject.put("sender_type", "1");
                        jsonObject.put("sender_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
                        jsonObject.put("sender_name", PropertiesUtil.read(activity, PropertiesUtil.NICKNAME));
                        jsonObject.put("voip_account", PropertiesUtil.read(activity, PropertiesUtil.VOIPACCOUNT));
                        ReceiverService.sendMsg(PaseJson.getMapMsg(map, "voipAccount"), jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    listView.setSelection(listView.getBottom());
                    xxEditText.setText("");
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
    @Override
    protected void onResume() {
        super.onResume();
        initBroadcast();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(xxLtBroadcastReceiver);
    }

    //    /**
    //     * 申请加为好友
    //     */
    //    class jwhyNetWork implements INetWork {
    //        @Override
    //        public boolean validate() {
    //            if (tjhysmEditText.getText().toString().equals("")) {
    //                toastShow("好友验证信息不能为空！");
    //                return false;
    //            }
    //            return true;
    //        }
    //
    //        @Override
    //        public Data getSubmitData() throws Exception {
    //            Data data = new Data("myfriend/addFriend.json");
    //            data.addData("relate_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
    //            data.addData("brelate_id", PaseJson.getMapMsg(map, "user_id"));
    //            data.addData("apply_note", tjhysmEditText.getText().toString());
    //            return data;
    //        }
    //
    //        @Override
    //        public void result(String result) throws Exception {
    //            JSONObject jsonObject = new JSONObject(result);
    //            if (jsonObject.getString("code").equals("0")) {
    //                toastShow("操作成功！");
    //            } else if (jsonObject.getString("code").equals("1")) {
    //                toastShow("不能重复添加好友！");
    //            } else {
    //                toastShow("操作失败！");
    //            }
    //        }
    //    };
}
