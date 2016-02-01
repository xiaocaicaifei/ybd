/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.gr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.home.HomeClickListener;

/**
 * 个人-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrIndexActivity extends BaseActivity implements OnClickListener {

    private ImageView         txImageView;                                       //头像
    private TextView          usernameTextView;                                  //用户名
    private TextView          moneyTextView;                                     //金额
    private TextView          xyValueTextView;                                   //信用值
    private TextView          gmValueTextView;                                   //购买
    private TextView          mcValueTextView;                                   //卖出
    private TextView          fsValueTextView;                                   //粉丝
    private ImageLoader       imageLoader = ImageLoader.getInstance();
    private GridView          xcGridView;
    BaseAdapter               xcAdapter;
    List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();

    private LinearLayout      grzxLinearLayout;                                  //个人中心
    private LinearLayout      xcLinearLayout;                                    //相册
    private LinearLayout      qbLinearLayout;                                    //钱包
    private LinearLayout      zjLinearLayout;                                    //专家
    private LinearLayout      cyLinearLayout;                                    //常用
    private LinearLayout      szLinearLayout;                                    //设置
    private LinearLayout      sqrzLinearLayout;                                  //申请认证

    @Override
    protected void initComponentBase() {
        //        view = inflater.inflate(R.layout.gr_index, null, false);
        setContentView(R.layout.gr_index);
        init();
        NetWork.submit(activity, init);
    }

    /**
     * 初始化控件
     */
    private void init() {
        txImageView = (ImageView) findViewById(R.id.tx_iv);
        usernameTextView = (TextView) findViewById(R.id.username_tv);
        moneyTextView = (TextView) findViewById(R.id.money_tv);
        xyValueTextView = (TextView) findViewById(R.id.xy_value_tv);
        gmValueTextView = (TextView) findViewById(R.id.gm_value_tv);
        mcValueTextView = (TextView) findViewById(R.id.mc_value_tv);
        fsValueTextView = (TextView) findViewById(R.id.fs_value_tv);
        xcGridView = (GridView) findViewById(R.id.xc_gv);
        xcAdapter = new GrIndexAdapter(list, activity);
        xcGridView.setAdapter(xcAdapter);
        xcAdapter.notifyDataSetChanged();

        grzxLinearLayout = (LinearLayout) findViewById(R.id.grzx_ll);
        grzxLinearLayout.setOnClickListener(this);
        xcLinearLayout = (LinearLayout) findViewById(R.id.xc_ll);
        xcLinearLayout.setOnClickListener(this);
        qbLinearLayout = (LinearLayout) findViewById(R.id.qb_ll);
        qbLinearLayout.setOnClickListener(this);
        zjLinearLayout = (LinearLayout) findViewById(R.id.zj_ll);
        zjLinearLayout.setOnClickListener(this);
        cyLinearLayout = (LinearLayout) findViewById(R.id.cy_ll);
        cyLinearLayout.setOnClickListener(this);
        szLinearLayout = (LinearLayout) findViewById(R.id.sz_ll);
        szLinearLayout.setOnClickListener(this);
        sqrzLinearLayout = (LinearLayout) findViewById(R.id.sqrz_ll);
        sqrzLinearLayout.setOnClickListener(this);
    }

    INetWork init = new INetWork() {

                      @Override
                      public boolean validate() {
                          return true;
                      }

                      @Override
                      public Data getSubmitData() throws Exception {
                          Data data = new Data("auser/selectUserById.json");
                          data.addData("user_id",
                              PropertiesUtil.read(activity, PropertiesUtil.USERID));
                          return data;
                      }

                      @SuppressWarnings("unchecked")
                      @Override
                      public void result(String result) throws Exception {
                          Map<String, Object> map = (Map<String, Object>) PaseJson
                              .paseJsonToObject(result);
                          Map<String, Object> map2 = (Map<String, Object>) map.get("data");
                          imageLoader.displayImage(C.IP + map2.get("icon_url"), txImageView,
                              MainApplication.getRoundOptions());
                          usernameTextView.setText(PaseJson.getMapMsg(map2, "nick_name"));
                          moneyTextView.setText(PaseJson.getMapMsg(map2, "coin_amount") + " ￥");
                          xyValueTextView.setText(PaseJson.getMapMsg(map2, "degree_credit") + " 分");
                          gmValueTextView.setText(PaseJson.getMapMsg(map2, "buy_vol") + " 件");
                          mcValueTextView.setText(PaseJson.getMapMsg(map2, "sale_vol") + " 件");
                          fsValueTextView.setText(PaseJson.getMapMsg(map2, "followers_count"));
                          list.clear();
                          list.addAll((List<Map<String, Object>>) map2.get("photos"));
                          xcAdapter.notifyDataSetChanged();
                      }
                  };

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.grzx_ll://个人中心
                intent.setClass(activity, GrGrzlActivity.class);
                startActivity(intent);
                break;
            case R.id.xc_ll://相册

                break;
            case R.id.qb_ll://钱包

                break;
            case R.id.zj_ll://专家

                break;
            case R.id.cy_ll://常用
                intent.setClass(activity, GrCyActivity.class);
                startActivity(intent);
                break;
            case R.id.sz_ll://设置

                break;
            case R.id.sqrz_ll://申请认证

                break;
            default:
                break;
        }
    }

}
