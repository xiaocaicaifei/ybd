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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.tools.DateUtil;
import com.ybd.common.tools.KeyboardOperate;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.common.SelectPhoto2Activity;
import com.ybd.yl.xx.dao.XxTxlLtDao;

/**
 * 消息-通讯录-开始聊天
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxQzLtActivity extends BaseActivity implements OnClickListener {
    Map<String, Object>       map;                                                    //传过来的信息
    List<Map<String, Object>> list             = new ArrayList<Map<String, Object>>();
    ListView                  listView;
    BaseAdapter               adapter;
    EditText                  xxEditText;                                             //聊天输入框
    Button                    fsButton;                                               //发送消息的按钮
    Button                    tjButton;                                               //添加按钮
    XxTxlLtDao                   ltDao;
    BroadcastReceiver         xxLtBroadcastReceiver;                                  //消息聊天的
    LinearLayout              fjgnLayout;                                             //附件功能的层
    ImageView                 tpImageView;                                            //图片
    ImageView                 zzImageView;                                            //转账
    ImageView                 wzImageView;                                            //位置
    private final int         SELECT_TP_RESULT = 0;
    private final int         SELECT_ZZ_RESULT = 1;
    private final int         SELECT_WZ_RESULT = 2;

    @SuppressWarnings("unchecked")
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_lt);
        ltDao = new XxTxlLtDao(activity);
        String titleName = "聊天";
        if (this.getIntent().hasExtra("hyObject")) {//从通讯录进入
            map = (Map<String, Object>) this.getIntent().getExtras().getSerializable("hyObject");
            titleName = PaseJson.getMapMsg(map, "nick_name");
        }
        if (this.getIntent().hasExtra("xxObject")) {//从消息列表进入
            map = (Map<String, Object>) this.getIntent().getExtras().getSerializable("xxObject");
            titleName = PaseJson.getMapMsg(map, "nick_name");
        }
        if (this.getIntent().hasExtra("qzObject")) {//从群组列表进入
            map = (Map<String, Object>) this.getIntent().getExtras().getSerializable("qzObject");
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
        adapter = new XxQzLtAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fsButton = (Button) findViewById(R.id.fs_b);
        fsButton.setOnClickListener(this);
        tjButton = (Button) findViewById(R.id.tj_b);
        tjButton.setOnClickListener(this);
        fjgnLayout = (LinearLayout) findViewById(R.id.fjgn_ll);
        tpImageView = (ImageView) findViewById(R.id.tp_iv);
        tpImageView.setOnClickListener(this);
        zzImageView = (ImageView) findViewById(R.id.zz_iv);
        zzImageView.setOnClickListener(this);
        wzImageView = (ImageView) findViewById(R.id.wz_iv);
        wzImageView.setOnClickListener(this);
        xxEditText = (EditText) findViewById(R.id.xx_ev);
        xxEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    fsButton.setVisibility(View.GONE);
                    tjButton.setVisibility(View.VISIBLE);
                } else {
                    fsButton.setVisibility(View.VISIBLE);
                    tjButton.setVisibility(View.GONE);
                }
            }
        });

        xxEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fjgnLayout.setVisibility(View.GONE);
                } else {
                    fjgnLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    /**
     * 查询该用户的聊天记录
     */
    private void findAllMsg() {
        list.clear();
        list.addAll(ltDao.findUserAllLt(PaseJson.getMapMsg(map, "buser_id")));
        adapter.notifyDataSetChanged();
        listView.setSelection(list.size() - 1);
    }

    /**
     * 初始化广播()
     */
    private void initBroadcast() {
        xxLtBroadcastReceiver = new BroadcastReceiver() {
            @SuppressWarnings("unchecked")
            @Override
            public void onReceive(Context context, Intent intent) {
                Map<String, Object> m = (Map<String, Object>) PaseJson.paseJsonToObject(intent
                    .getExtras().getString("content"));
                if (PaseJson.getMapMsg(m, "voip_account").equals(
                    PaseJson.getMapMsg(map, "voipAccount"))) {//如果当前界面聊天的Id和收到的发送消息的ID一样则显示，否则不显示
                    list.add(m);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(listView.getBottom());

                    //发送广播，当前人已经显示信息，在列表页面就不需要再显示未读数量，也就是说这个人的未读数量是0
                    ltDao.updateTalkUser(0, PaseJson.getMapMsg(map, "buser_id"));
                    //                   BroadcaseUtil.sendBroadcase(BroadcaseUtil.XX_LT_RECEIVED, activity);
                }
            }
        };
        //接收聊天消息的广播
        BroadcaseUtil.registBroadcase(activity, xxLtBroadcastReceiver, BroadcaseUtil.XX_LT);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.right_rl:
                //                NetWork.submit(activity, new jwhyNetWork());
                break;
            case R.id.fs_b://发送按钮
                if (xxEditText.getText().toString().equals("")) {
                    toastShow("发送消息不能为空！");
                } else {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("type", "1");//说明是对话类型
                    m.put("sender_type", "0");//0，代表是本人发的，1代表是接收到他人发的消息
                    m.put("send_time", DateUtil.getTimeFormat("yyyy-MM-dd HH:mm:ss", new Date()));
                    m.put("send_content", xxEditText.getText().toString());
                    m.put("sender_icon_url", PaseJson.getMapMsg(map, "icon_url"));
                    m.put("sender_id", PaseJson.getMapMsg(map, "buser_id"));//不是本人的Id，是接收人的ID
                    m.put("sender_name", PaseJson.getMapMsg(map, "nick_name"));//不是本人的name，是接收人的name
                    m.put("void_account", PaseJson.getMapMsg(map,"voipAccount"));//接收人的voip账号
                    list.add(m);
                    ltDao.add(m);//将记录插入到聊天记录表中
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type", "1");
                        jsonObject.put("sender_type", "1");
                        jsonObject.put("send_time",
                            DateUtil.getTimeFormat("MM-dd HH:mm", new Date()));
                        jsonObject.put("send_content", xxEditText.getText().toString());
                        jsonObject.put("sender_icon_url", PaseJson.getMapMsg(map, "icon_url"));
                        jsonObject.put("sender_id",
                            PropertiesUtil.read(activity, PropertiesUtil.USERID));
                        jsonObject.put("sender_name",
                            PropertiesUtil.read(activity, PropertiesUtil.NICKNAME));
                        jsonObject.put("voip_account",
                            PropertiesUtil.read(activity, PropertiesUtil.VOIPACCOUNT));
//                        ReceiverService.sendMsg(PaseJson.getMapMsg(map, "voipAccount"),
//                            jsonObject.toString(), "",activity);//发送人的voip账号
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    listView.setSelection(listView.getBottom());
                    xxEditText.setText("");
                }
                break;
            case R.id.tj_b://打开添加附件的按钮
                KeyboardOperate.setInputManager(false, activity);
                fjgnLayout.setVisibility(View.VISIBLE);
                xxEditText.clearFocus();
                break;
            case R.id.tp_iv:
                tpImageView.setBackgroundResource(R.drawable.xx_lt_tp_sel);
                zzImageView.setBackgroundResource(R.drawable.xx_lt_zz);
                wzImageView.setBackgroundResource(R.drawable.xx_lt_wz);
                intent.setClass(activity, SelectPhoto2Activity.class);
                startActivityForResult(intent, SELECT_TP_RESULT);
                break;
            case R.id.zz_iv:
                tpImageView.setBackgroundResource(R.drawable.xx_lt_tp);
                zzImageView.setBackgroundResource(R.drawable.xx_lt_zz_sel);
                wzImageView.setBackgroundResource(R.drawable.xx_lt_wz);
                break;
            case R.id.wz_iv:
                tpImageView.setBackgroundResource(R.drawable.xx_lt_tp);
                zzImageView.setBackgroundResource(R.drawable.xx_lt_zz);
                wzImageView.setBackgroundResource(R.drawable.xx_lt_wz_sel);
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
        //        unregisterReceiver(xxLtBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if(arg1==RESULT_OK){
            if(arg0==SELECT_TP_RESULT){
                //选择完图片后，发送图片
                String path=arg2.getExtras().getString("path");
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "1");
                    jsonObject.put("send_content", xxEditText.getText().toString());
                    jsonObject.put("sender_icon_url", PaseJson.getMapMsg(map, "icon_url"));
                    jsonObject.put("send_time",
                        DateUtil.getTimeFormat("MM-dd HH:mm", new Date()));
                    jsonObject.put("sender_type", "3");
                    jsonObject.put("sender_id",
                        PropertiesUtil.read(activity, PropertiesUtil.USERID));
                    jsonObject.put("sender_name",
                        PropertiesUtil.read(activity, PropertiesUtil.NICKNAME));
                    jsonObject.put("voip_account",
                        PropertiesUtil.read(activity, PropertiesUtil.VOIPACCOUNT));
//                    ReceiverService.sendMsg(PaseJson.getMapMsg(map, "voipAccount"),
//                        jsonObject.toString(), path,activity);
                    
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("type", "1");
                    m.put("sender_type", "2");//0，代表是本人发的，1代表是接收到他人发的消息,2本人发的图片，3他人发的图片
                    m.put("send_time", DateUtil.getTimeFormat("yyyy-MM-dd HH:mm:ss", new Date()));
                    m.put("send_content", "file://"+path);
                    m.put("sender_icon_url", PaseJson.getMapMsg(map, "icon_url"));
                    m.put("sender_id", PaseJson.getMapMsg(map, "buser_id"));//不是本人的Id，是接收人的ID
                    m.put("sender_name", PaseJson.getMapMsg(map, "nick_name"));//不是本人的name，是接收人的name
                    m.put("void_account", PaseJson.getMapMsg(map,"voipAccount"));//接收人的voip账号
                    list.add(m);
                    ltDao.add(m);//将记录插入到聊天记录表中
                    adapter.notifyDataSetChanged();
                    listView.setSelection(listView.getBottom());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
