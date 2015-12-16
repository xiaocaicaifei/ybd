/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.gr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.home.HomeClickListener;

/**
 * 个人-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrIndexFragment extends BaseFragment implements HomeClickListener{
   
    private ImageView txImageView;//头像
    private TextView usernameTextView;//用户名
    private TextView moneyTextView;//金额
    private TextView xyValueTextView;//信用值
    private TextView gmValueTextView;//购买
    private TextView mcValueTextView;//卖出
    private TextView fsValueTextView;//粉丝
    private ImageLoader imageLoader=ImageLoader.getInstance();
    private GridView xcGridView;
    BaseAdapter xcAdapter;
    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    
    
    
    @Override
    public void onHomeclick(View v) {
        
    }
    @Override
    protected void initComponentBase() {
        view = inflater.inflate(R.layout.gr_index, null, false);
        init();
        NetWork.submit(activity, init);
    }
    
    /**
     * 初始化控件
     */
    private void init(){
        txImageView=(ImageView) view.findViewById(R.id.tx_iv);
        usernameTextView=(TextView) view.findViewById(R.id.username_tv);
        moneyTextView=(TextView) view.findViewById(R.id.money_tv);
        xyValueTextView=(TextView) view.findViewById(R.id.xy_value_tv);
        gmValueTextView=(TextView) view.findViewById(R.id.gm_value_tv);
        mcValueTextView=(TextView) view.findViewById(R.id.mc_value_tv);
        fsValueTextView=(TextView) view.findViewById(R.id.fs_value_tv);
        xcGridView=(GridView) view.findViewById(R.id.xc_gv);
        xcAdapter=new GrIndexAdapter(list, activity);
        xcGridView.setAdapter(xcAdapter);
        xcAdapter.notifyDataSetChanged();
    }
    
    INetWork init=new INetWork() {
        
        @Override
        public boolean validate() {
            return true;
        }
        
        @Override
        public Data getSubmitData() throws Exception {
            Data data=new Data("auser/selectUserById.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map=(Map<String, Object>) PaseJson.paseJsonToObject(result);
            Map<String, Object> map2=(Map<String, Object>) map.get("data");
            imageLoader.displayImage(C.IP+map2.get("icon_url"),txImageView,MainApplication.getRoundOptions());
            usernameTextView.setText(PaseJson.getMapMsg(map2, "nick_name"));
            moneyTextView.setText(PaseJson.getMapMsg(map2, "coin_amount")+" ￥");
            xyValueTextView.setText(PaseJson.getMapMsg(map2, "degree_credit")+" 分");
            gmValueTextView.setText(PaseJson.getMapMsg(map2, "buy_vol")+" 件");
            mcValueTextView.setText(PaseJson.getMapMsg(map2, "sale_vol")+" 件");
            fsValueTextView.setText(PaseJson.getMapMsg(map2, "followers_count"));
            list.clear();
            list.addAll((List<Map<String, Object>>) map2.get("photos"));
            xcAdapter.notifyDataSetChanged();
        }
    };

}
