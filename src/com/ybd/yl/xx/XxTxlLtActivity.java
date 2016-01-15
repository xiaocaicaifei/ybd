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

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ybd.common.tools.DateUtil;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.service.ReceiverService;

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

    @SuppressWarnings("unchecked")
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_lt);
        String titleName = "聊天";
        if (this.getIntent().hasExtra("hyObject")) {
            map = (Map<String, Object>) this.getIntent().getExtras().getSerializable("hyObject");
            titleName = PaseJson.getMapMsg(map, "nick_name");
        }
        initPublicView(titleName, R.drawable.login_fh, R.drawable.xx_txl_grzl, null, this);
        init();
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
                    m.put("type", "0");
                    m.put("time", DateUtil.getTimeFormat("MM-dd HH:mm", new Date()));
                    m.put("nr", xxEditText.getText().toString());
                    m.put("icon_url", PaseJson.getMapMsg(map, "icon_url"));
                    list.add(m);
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("type", "1");
                        jsonObject.put("content", xxEditText.getText().toString());
                        jsonObject.put("icon_url",  PaseJson.getMapMsg(map, "icon_url"));
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
