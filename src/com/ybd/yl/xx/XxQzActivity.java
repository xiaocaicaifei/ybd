/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.ybd.common.C;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.DateUtil;
import com.ybd.common.tools.LatLngOperate;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.xListView.XListView;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.service.ReceiverService;

/**
 * 消息-群组
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxQzActivity extends BaseActivity implements OnClickListener {
    private XListView         qzListView;
    private BaseAdapter       qzAdapter;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private LocationManager   locationManager;
    private String            locationProvider;
    private Location          location;

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_qz);
        initPublicView("艺论群组", R.drawable.login_fh, 0, null, null);
        init();
        NetWork.submit(activity, qzxxNetwork);
        initPosition();
    }

    /**
     * 初始化位置
     */
    private void initPosition() {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
        }
        //获取Location
        location = locationManager.getLastKnownLocation(locationProvider);

        if (location != null) {
            //不为空,显示地理位置经纬度
            //            showLocation(location);
        } else {
            Toast.makeText(this, "location为空", Toast.LENGTH_SHORT).show();
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {
                                          @Override
                                          public void onStatusChanged(String provider, int status,
                                                                      Bundle arg2) {
                                          }

                                          @Override
                                          public void onProviderEnabled(String provider) {
                                          }

                                          @Override
                                          public void onProviderDisabled(String provider) {
                                          }

                                          @Override
                                          public void onLocationChanged(Location location) {
                                              //如果位置发生变化,重新显示
                                              //            showLocation(location);
                                              XxQzActivity.this.location = location;
                                          }
                                      };

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
                boolean isInstanceAmont = false;//是否在范围之内
                Map<String, Object> map = ((Map<String, Object>) list.get(arg2 - 1));
                double groupLat = Double.parseDouble(PaseJson.getMapMsg(map, "ypoint"));
                double groupLng = Double.parseDouble(PaseJson.getMapMsg(map, "xpoint"));
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                double instance = LatLngOperate.getInstance(groupLat, groupLng, lat, lng);
                double groupRadius = Double.parseDouble(PaseJson.getMapMsg(map, "group_radius"));
                if (instance <= groupRadius) {
                    isInstanceAmont = true;
                }
                String state = "";
                if (PropertiesUtil.read(activity, PropertiesUtil.ISDV).equals("1")) {
                    state = "3";
                } else {
                    if (isInstanceAmont) {
                        state = "2";
                    } else {
                        state = "4";
                    }
                }
                String groupId = PaseJson.getMapMsg(map, "groupid");
                String groupName = PaseJson.getMapMsg(map, "groupname");
                NetWork.submit(activity, new groupJoinNetwork(groupId, groupName, state));
                
              //设置消息已读
                ReceiverService.mRongIMClient.clearMessagesUnreadStatus(
                    ConversationType.PRIVATE, groupId, null);
                qzAdapter.notifyDataSetChanged();
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

    private INetWork qzxxNetwork = new INetWork() {

                                     @Override
                                     public boolean validate() {
                                         return true;
                                     }

                                     @Override
                                     public Data getSubmitData() throws Exception {
                                         Data data = new Data("group/selectGroup.json");
                                         data.addData("page", page);
                                         data.addData("limit", C.PAGE_SIZE);
                                         return data;
                                     }

                                     @SuppressWarnings("unchecked")
                                     @Override
                                     public void result(String result) throws Exception {
                                         Map<String, Object> map = (Map<String, Object>) PaseJson
                                             .paseJsonToObject(result);
                                         list.clear();
                                         List<Map<String, Object>> l=(List<Map<String, Object>>) map.get("groupList");
                                         for(Map<String, Object> m:l){
                                             Conversation conversation=ReceiverService.mRongIMClient.getConversation(ConversationType.GROUP, PaseJson.getMapMsg(m, "id"));
                                             if(conversation!=null){
                                                 m.put("unread_num", conversation.getUnreadMessageCount());
                                             }else{
                                                 m.put("unread_num", "0");
                                             }
                                         }
                                         list.addAll(l);
                                         qzAdapter.notifyDataSetChanged();
                                     }
                                 };

    /**
     * 用户申请加入群
     */
    private class groupJoinNetwork implements INetWork {
        String groupId   = "";
        String groupName = "";
        String state     = "";

        public groupJoinNetwork(String groupId, String groupName, String state) {
            this.groupId = groupId;
            this.groupName = groupName;
            this.state = state;
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("group/groupJoin.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("group_id", groupId);
            data.addData("group_name", groupName);
            data.addData("is_disturb", "");
            data.addData("groupcard", PropertiesUtil.read(activity, PropertiesUtil.NICKNAME));
            data.addData("state", state);
            data.addData("enter_time", DateUtil.getTimeFormat("yyyy-MM-dd HH:mm:ss", new Date()));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map = (Map<String, Object>) PaseJson.paseJsonToObject(result);
            String code = PaseJson.getMapMsg(map, "code");
            Intent intent = new Intent();
            intent.setClass(activity, XxQzLtActivity.class);
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("nick_name", groupName);
            m.put("buser_id", groupId);
            intent.putExtra("qzObject", (Serializable)m);
            if (code.equals("0")) {
                startActivity(intent);
            } else if (code.equals("1")) {
                startActivity(intent);
            } else if (code.equals("2")) {
                toastShow("群组已满，请选择其他群组");
            } else if (code.equals("-1")) {
                toastShow("加入群组失败");
            }
        }
    };
}
