/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybd.common.C;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.SlidePageAdapter;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.xListView.XListView;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 消息-通讯录-新的朋友
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxTxlXdpyActivity extends BaseActivity implements OnClickListener {

    private LinearLayout      ylpyLayout;                                         //艺论朋友
    private LinearLayout      txlhyLayout;                                        //通讯录好友
                                                                                   //    private ListViewRun ylhyListViewRun;//艺论好友
    private ViewPager         ylhyViewPager;                                      //艺论好友
    private LayoutInflater    inflater;
    private View              mjphInflaterView;                                   //卖家排行
    private View              mjph2InflaterView;                                  //买家排行
    private View              pjphInflaterView;                                   //评价排行
    private View              fsphInflaterView;                                   //粉丝排行
    private TextView          mjphTextView;                                       //卖家排行（加载页面）
    private TextView          mjph2TextView;                                      //买家排行（加载页面）
    private TextView          pjphTextView;                                       //评价排行（加载页面）
    private TextView          fsphTextView;                                       //粉丝排行（加载页面）
    private View              mjphView;                                           //卖家排行（选择）
    private View              mjph2View;                                          //卖家排行（选择）
    private View              pjphView;                                           //评价排行（选择）
    private View              fsphView;                                           //粉丝排行（选择）
    List<View>                viewList     = new ArrayList<View>();
    XListView                 mjphListView;                                       //卖家排行
    XListView                 mjph2ListView;                                      //卖家排行
    XListView                 pjphListView;                                       //信用排行
    XListView                 fsphListView;                                       //粉丝
    BaseAdapter               mjphAdapter;                                        //
    BaseAdapter               mjph2Adapter;
    BaseAdapter               pjphAdapter;                                        //
    BaseAdapter               fsphAdapter;
    List<Map<String, Object>> mjphList     = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> mjph2List    = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> pjphList     = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> fsphList     = new ArrayList<Map<String, Object>>();
    private boolean           isFirstMjph  = false;                                //是否是第一查询卖家排行
    private boolean           isFirstMjph2 = false;                                //是否是第一查询买家排行
    private boolean           isFirstPjph  = false;                                //是否是第一查询评价排行
    private boolean           isFirstFsph  = false;                                //是否是第一查询粉丝排行

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.xx_txl_xdpy);
        initPublicView("新的朋友", R.drawable.login_fh, 0, null, null);
        init();
        initViewPage();
        initMjph();
        initMjph2();
        initPjph();
        initFsph();
        NetWork.submit(activity, new SelectYlhy(0));
    }

    /**
     * 初始化控件
     */
    private void init() {
        inflater = this.getLayoutInflater();
        ylpyLayout = (LinearLayout) findViewById(R.id.ylpy_ll);
        ylpyLayout.setOnClickListener(this);
        txlhyLayout = (LinearLayout) findViewById(R.id.txlhy_ll);
        txlhyLayout.setOnClickListener(this);
        mjphTextView = (TextView) findViewById(R.id.mjph_tv);
        mjphTextView.setOnClickListener(this);
        mjphView = findViewById(R.id.mjph_v);
        mjph2TextView = (TextView) findViewById(R.id.mjph2_tv);
        mjph2TextView.setOnClickListener(this);
        mjph2View = findViewById(R.id.mjph2_v);
        pjphTextView = (TextView) findViewById(R.id.pjph_tv);
        pjphTextView.setOnClickListener(this);
        pjphView = findViewById(R.id.pjph_v);
        fsphTextView = (TextView) findViewById(R.id.fsph_tv);
        fsphTextView.setOnClickListener(this);
        fsphView = findViewById(R.id.fsph_v);
    }

    /**
     * 初始化ViewPage
     */
    private void initViewPage() {
        ylhyViewPager = (ViewPager) findViewById(R.id.ylhy_vp);
        mjphInflaterView = inflater.inflate(R.layout.xx_txl_xdpy_list, null);
        mjph2InflaterView = inflater.inflate(R.layout.xx_txl_xdpy_list, null);
        pjphInflaterView = inflater.inflate(R.layout.xx_txl_xdpy_list, null);
        fsphInflaterView = inflater.inflate(R.layout.xx_txl_xdpy_list, null);
        viewList.add(mjphInflaterView);
        viewList.add(mjph2InflaterView);
        viewList.add(pjphInflaterView);
        viewList.add(fsphInflaterView);
        SlidePageAdapter ylhyAdapter = new SlidePageAdapter(viewList, activity);
        ylhyViewPager.setAdapter(ylhyAdapter);
        ylhyViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                changeViewPageSelect(arg0);
                NetWork.submit(activity, new SelectYlhy(arg0));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void changeViewPageSelect(int selectId) {
        mjphTextView.setTextColor(this.getResources().getColor(R.color.xx_txl_xdpy_color));
        mjph2TextView.setTextColor(this.getResources().getColor(R.color.xx_txl_xdpy_color));
        pjphTextView.setTextColor(this.getResources().getColor(R.color.xx_txl_xdpy_color));
        fsphTextView.setTextColor(this.getResources().getColor(R.color.xx_txl_xdpy_color));
        mjphView.setBackgroundColor(this.getResources().getColor(R.color.white));
        mjph2View.setBackgroundColor(this.getResources().getColor(R.color.white));
        pjphView.setBackgroundColor(this.getResources().getColor(R.color.white));
        fsphView.setBackgroundColor(this.getResources().getColor(R.color.white));
        switch (selectId) {
            case 0:
                mjphTextView
                    .setTextColor(this.getResources().getColor(R.color.unitform_button_red));
                mjphView.setBackgroundColor(this.getResources().getColor(
                    R.color.unitform_button_red));
                break;
            case 1:
                mjph2TextView.setTextColor(this.getResources()
                    .getColor(R.color.unitform_button_red));
                mjph2View.setBackgroundColor(this.getResources().getColor(
                    R.color.unitform_button_red));
                break;
            case 2:
                pjphTextView
                    .setTextColor(this.getResources().getColor(R.color.unitform_button_red));
                pjphView.setBackgroundColor(this.getResources().getColor(
                    R.color.unitform_button_red));
                break;
            case 3:
                fsphTextView
                    .setTextColor(this.getResources().getColor(R.color.unitform_button_red));
                fsphView.setBackgroundColor(this.getResources().getColor(
                    R.color.unitform_button_red));
                break;

            default:
                break;
        }
    }

    /**
     * 初始化卖家排行
     */
    private void initMjph() {
        mjphListView = (XListView) mjphInflaterView.findViewById(R.id.ylhy_lvr);
        mjphAdapter = new XxTxlXdpyAdapter(mjphList, this);
        mjphListView.setAdapter(mjphAdapter);
        mjphListView.setPullRefreshEnable(false);
        mjphListView.setPullLoadMorEnable(true);
    }

    /**
     * 初始化买家排行
     */
    private void initMjph2() {
        mjph2ListView = (XListView) mjph2InflaterView.findViewById(R.id.ylhy_lvr);
        mjph2Adapter = new XxTxlXdpyAdapter(mjph2List, this);
        mjph2ListView.setAdapter(mjph2Adapter);
        mjph2ListView.setPullRefreshEnable(false);
        mjph2ListView.setPullLoadMorEnable(true);
    }

    /**
     * 初始化卖家排行
     */
    private void initPjph() {
        pjphListView = (XListView) pjphInflaterView.findViewById(R.id.ylhy_lvr);
        pjphAdapter = new XxTxlXdpyAdapter(pjphList, this);
        pjphListView.setAdapter(pjphAdapter);
        pjphListView.setPullRefreshEnable(false);
        pjphListView.setPullLoadMorEnable(true);
    }

    /**
     * 初始化粉丝排行
     */
    private void initFsph() {
        fsphListView = (XListView) fsphInflaterView.findViewById(R.id.ylhy_lvr);
        fsphAdapter = new XxTxlXdpyAdapter(fsphList, this);
        fsphListView.setAdapter(fsphAdapter);
        fsphListView.setPullRefreshEnable(false);
        fsphListView.setPullLoadMorEnable(true);
    }

    /**
     * 查询艺论好友的信息
     */
    class SelectYlhy implements INetWork {
        private int sort; //排序方式，0：卖家；1：买家；2评价排行；3粉丝排行

        public SelectYlhy(int sort) {
            this.sort = sort;
        }

        @Override
        public boolean validate() {
            switch (sort) {
                case 0:
                    if(isFirstMjph){
                        return false;
                    }else{
                        isFirstMjph=true;
                    }
                    break;
                case 1:
                    if(isFirstMjph2){
                        return false;
                    }else{
                        isFirstMjph2=true;
                    }
                    break;
                case 2:
                    if(isFirstPjph){
                        return false;
                    }else{
                        isFirstPjph=true;
                    }
                    break;
                case 3:
                    if(isFirstFsph){
                        return false;
                    }else{
                        isFirstFsph=true;
                    }
                    break;

                default:
                    break;
            }
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("auser/searchUser.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("is_bv", "");
            data.addData("sort", "desc");
            data.addData("sale_vol", sort == 0 ? "1" : "");
            data.addData("buy_vol", sort == 1 ? "1" : "");
            data.addData("degree_credit", sort == 2 ? "1" : "");
            data.addData("followers_count", sort == 3 ? "1" : "");
            data.addData("page", page);
            data.addData("limit", C.PAGE_SIZE_USER);
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> map = (Map<String, Object>) PaseJson.paseJsonToObject(result);

            if (sort == 0) {
                mjphList.addAll((List<Map<String, Object>>) map.get("userList"));
                mjphAdapter.notifyDataSetChanged();
                int total = Integer.parseInt(map.get("totalCount").toString());
                if (mjphList.size() + (page - 1) * C.PAGE_SIZE_USER >= total) {
                    mjphListView.setPullLoadMorEnable(false);
                } else {
                    mjphListView.setPullLoadMorEnable(true);
                }
            } else if (sort == 1) {
                mjph2List.addAll((List<Map<String, Object>>) map.get("userList"));
                mjph2Adapter.notifyDataSetChanged();
                int total = Integer.parseInt(map.get("totalCount").toString());
                if (mjph2List.size() + (page - 1) * C.PAGE_SIZE_USER >= total) {
                    mjph2ListView.setPullLoadMorEnable(false);
                } else {
                    mjph2ListView.setPullLoadMorEnable(true);
                }
            } else if (sort == 2) {
                pjphList.addAll((List<Map<String, Object>>) map.get("userList"));
                pjphAdapter.notifyDataSetChanged();
                int total = Integer.parseInt(map.get("totalCount").toString());
                if (pjphList.size() + (page - 1) * C.PAGE_SIZE_USER >= total) {
                    pjphListView.setPullLoadMorEnable(false);
                } else {
                    pjphListView.setPullLoadMorEnable(true);
                }
            } else if (sort == 3) {
                fsphList.addAll((List<Map<String, Object>>) map.get("userList"));
                fsphAdapter.notifyDataSetChanged();
                int total = Integer.parseInt(map.get("totalCount").toString());
                if (fsphList.size() + (page - 1) * C.PAGE_SIZE_USER >= total) {
                    fsphListView.setPullLoadMorEnable(false);
                } else {
                    fsphListView.setPullLoadMorEnable(true);
                }
            }

        }
    };
    @Override
    public void onClick(View v) {
//        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.mjph_tv://卖家排行
                ylhyViewPager.setCurrentItem(0);
                break;
            case R.id.mjph2_tv://买家排行
                ylhyViewPager.setCurrentItem(1);
                break;
            case R.id.pjph_tv://评价排行
                ylhyViewPager.setCurrentItem(2);
                break;
            case R.id.fsph_tv://粉丝排行
                ylhyViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

}
