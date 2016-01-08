/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.yl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.C;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.KeyboardOperate;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.ScreenDisplay;
import com.ybd.common.xListView.XListView;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 艺论-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class YlIndexActivity extends BaseActivity implements  OnClickListener {
    private XListView                 listView;
    private YlIndexAdapter               adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    DrawerLayout                      drawerLayout;
    private View                      cxPopupView;
    public PopupWindow               plPopupWindow;
    private EditText                  plEditText;                                 //评论
    private Button                    fsButton;                                    //发送按钮
    public Map<String, Object>       plMap=new HashMap<String, Object>();                                      //点击评论后，带回来的信息
                                                                                   //评论
    public PopupWindow gzPopupWindow;//估值的弹出框
    private View gzPopupView;
    private ImageView gzgbImageView;//估值关闭
    private EditText srgzjeEditText;//输入估值金额
    private Button tjButton;//估值的提交
    private Button oneswButton,twoswButton,threeswButton,fourswButton,fiveswButton,sixswButton,sevenswButton,eightswButton,nineswButton,tenupswButton;
    private Button onewButton,twowButton,threewButton,fourwButton,fivewButton,sixwButton,sevenwButton,eightwButton,ninewButton;
    private Button oneqButton,twoqButton,threeqButton,fourqButton,fiveqButton,sixqButton,sevenqButton,eightqButton,nineqButton,onedownqButton;
    private String gzTitle;//估值选择的数值的名字
    private String gzValue;//估值的选择的数值
    private String gzAverage;//估值的平均值
    public static String selectCircleID;//选择的议论的ID
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.yl_index);
        initPublicView("艺论", R.drawable.yl_sz, R.drawable.yl_sc, YlIndexActivity.this,
            YlIndexActivity.this);
        initPlPopWindow();
        initGzPopWindow();
        initListView();
        //        initDrawerLayout();
        page = 1;
        NetWork.submit(activity, ylList);
        registBroadcast();
    }

    private void initListView() {
        listView = (XListView) findViewById(R.id.list_lv);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        setXListViewListener(listView, ylList, list);
        adapter = new YlIndexAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化评论弹出框
     */
    private void initPlPopWindow() {
        cxPopupView = inflater.inflate(R.layout.yl_pl_popupwindow, null);
        plPopupWindow = createPopupWindwo(cxPopupView, ScreenDisplay.getScreenWidth(activity));
        fsButton = (Button) cxPopupView.findViewById(R.id.fs_b);
        fsButton.setOnClickListener(this);
        plEditText = (EditText) cxPopupView.findViewById(R.id.pl_ev);
        fsButton.setEnabled(false);
        plEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    fsButton.setEnabled(true);
                    fsButton.setBackgroundResource(R.drawable.qz_grzl_button2_onpress);
                    fsButton.setTextColor(activity.getResources().getColor(R.color.white));
                }else{
                    fsButton.setEnabled(false);
                    fsButton.setBackgroundResource(R.drawable.qz_grzl_button1_onpress);
                    fsButton.setTextColor(activity.getResources().getColor(R.color.black));
                }
            }
        });
    }
    /**
     * 初始化估值弹出框
     */
    private void initGzPopWindow() {
        gzPopupView = inflater.inflate(R.layout.yl_gz_popupwindow, null);
        gzPopupWindow = createPopupWindwo(gzPopupView, ScreenDisplay.getScreenWidth(activity));
        gzgbImageView=(ImageView) gzPopupView.findViewById(R.id.close_iv);
        gzgbImageView.setOnClickListener(this);
        srgzjeEditText=(EditText) gzPopupView.findViewById(R.id.srgzje_et);
        srgzjeEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                changeZt(0);
                if(!s.toString().equals("")){
                    inputGz(Integer.parseInt(s.toString()));
                }
            }
        });
        tjButton=(Button) gzPopupView.findViewById(R.id.tj_b);
        tjButton.setOnClickListener(this);
        
        oneswButton=(Button) gzPopupView.findViewById(R.id.onesw_b);
        oneswButton.setOnClickListener(this);
        twoswButton=(Button) gzPopupView.findViewById(R.id.twosw_b);
        twoswButton.setOnClickListener(this);
        threeswButton=(Button) gzPopupView.findViewById(R.id.threesw_b);
        threeswButton.setOnClickListener(this);
        fourswButton=(Button) gzPopupView.findViewById(R.id.foursw_b);
        fourswButton.setOnClickListener(this);
        fiveswButton=(Button) gzPopupView.findViewById(R.id.fivesw_b);
        fiveswButton.setOnClickListener(this);
        sixswButton=(Button) gzPopupView.findViewById(R.id.sixsw_b);
        sixswButton.setOnClickListener(this);
        sevenswButton=(Button) gzPopupView.findViewById(R.id.sevensw_b);
        sevenswButton.setOnClickListener(this);
        eightswButton=(Button) gzPopupView.findViewById(R.id.eightsw_b);
        eightswButton.setOnClickListener(this);
        nineswButton=(Button) gzPopupView.findViewById(R.id.ninesw_b);
        nineswButton.setOnClickListener(this);
        tenupswButton=(Button) gzPopupView.findViewById(R.id.tenupsw_b);
        tenupswButton.setOnClickListener(this);
        
        onewButton=(Button) gzPopupView.findViewById(R.id.onew_b);
        onewButton.setOnClickListener(this);
        twowButton=(Button) gzPopupView.findViewById(R.id.twow_b);
        twowButton.setOnClickListener(this);
        threewButton=(Button) gzPopupView.findViewById(R.id.threew_b);
        threewButton.setOnClickListener(this);
        fourwButton=(Button) gzPopupView.findViewById(R.id.fourw_b);
        fourwButton.setOnClickListener(this);
        fivewButton=(Button) gzPopupView.findViewById(R.id.fivew_b);
        fivewButton.setOnClickListener(this);
        sixwButton=(Button) gzPopupView.findViewById(R.id.sixw_b);
        sixwButton.setOnClickListener(this);
        sevenwButton=(Button) gzPopupView.findViewById(R.id.sevenw_b);
        sevenwButton.setOnClickListener(this);
        eightwButton=(Button) gzPopupView.findViewById(R.id.eightw_b);
        eightwButton.setOnClickListener(this);
        ninewButton=(Button) gzPopupView.findViewById(R.id.ninew_b);
        ninewButton.setOnClickListener(this);
        
        onedownqButton=(Button) gzPopupView.findViewById(R.id.onedownq_b);
        onedownqButton.setOnClickListener(this);
        oneqButton=(Button) gzPopupView.findViewById(R.id.oneq_b);
        oneqButton.setOnClickListener(this);
        twoqButton=(Button) gzPopupView.findViewById(R.id.twoq_b);
        twoqButton.setOnClickListener(this);
        threeqButton=(Button) gzPopupView.findViewById(R.id.threeq_b);
        threeqButton.setOnClickListener(this);
        fourqButton=(Button) gzPopupView.findViewById(R.id.fourq_b);
        fourqButton.setOnClickListener(this);
        fiveqButton=(Button) gzPopupView.findViewById(R.id.fiveq_b);
        fiveqButton.setOnClickListener(this);
        sixqButton=(Button) gzPopupView.findViewById(R.id.sixq_b);
        sixqButton.setOnClickListener(this);
        sevenqButton=(Button) gzPopupView.findViewById(R.id.sevenq_b);
        sevenqButton.setOnClickListener(this);
        eightqButton=(Button) gzPopupView.findViewById(R.id.eightq_b);
        eightqButton.setOnClickListener(this);
        nineqButton=(Button) gzPopupView.findViewById(R.id.nineq_b);
        nineqButton.setOnClickListener(this);
    }

    /**
     * 创建popupview弹出框
     * @param view,popupview中要加载的页面
     * @return
     */
    public static PopupWindow createPopupWindwo(View view, int width) {
        final PopupWindow popupWindow = new PopupWindow(view, width,
            WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        view.findViewById(R.id.clickbg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
//                KeyboardOperate.hideOrOpenKeyboard(activity);
            }
        });
        return popupWindow;
    }

    /**
     * 注册广播
     */
    private void registBroadcast(){
        //支付成功的广播
        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                page=1;
                list.clear();
                NetWork.submit(activity, false, ylList);
            }
        }, BroadcaseUtil.YL_SCCG);
    }

    /**
     * 初始化左边滑动设置
     */
//    private void initDrawerLayout() {
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
//        LayoutParams lp = drawerLayout.getLayoutParams();
//        lp.height = dm.heightPixels;
//        drawerLayout.setLayoutParams(lp);//设置高度为全屏高度
//        drawerLayout.setDrawerListener(new DrawerListener() {
//
//            @Override
//            public void onDrawerStateChanged(int arg0) {
//            }
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                View mContent = drawerLayout.getChildAt(0);
//                View mMenu = drawerView;
//                float scale = 1 - slideOffset;
//                float rightScale = 0.8f + scale * 0.2f;
//
//                float leftScale = 1 - 0.3f * scale;
//
//                ViewHelper.setScaleX(mMenu, leftScale);
//                ViewHelper.setScaleY(mMenu, leftScale);
//                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
//                ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
//                ViewHelper.setPivotX(mContent, 0);
//                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
//                mContent.invalidate();
//                ViewHelper.setScaleX(mContent, rightScale);
//                ViewHelper.setScaleY(mContent, rightScale);
//            }
//
//            @Override
//            public void onDrawerOpened(View arg0) {
//            }
//
//            @Override
//            public void onDrawerClosed(View arg0) {
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_rl:
                Intent intent = new Intent();
                intent.setClass(activity, YlScActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.fs_b:
                String circleId = PaseJson.getMapMsg(plMap, "circleId");
                String note = plEditText.getText().toString();
                String parentId = PaseJson.getMapMsg(plMap, "parentId");
                String parentUserid = PaseJson.getMapMsg(plMap, "parentUserid");
                NetWork.submit(activity, new HfNetWork(circleId, note, parentId, parentUserid));
                break;
            case R.id.close_iv://估值弹出窗的关闭
                gzPopupWindow.dismiss();
                break;
            case R.id.tj_b://估值-提交
                if(gzAverage.equals("")){
                    toastShow("请输入或选择估值金额！");
                    return;
                }
                NetWork.submit(activity, new GzNetWork(selectCircleID));
                break;
            case R.id.onesw_b:
                changeZt(1);
                srgzjeEditText.setText("");
                gzTitle=oneswButton.getText().toString();
                gzValue="100000-200000";
                gzAverage="150000";
                break;
            case R.id.twosw_b:
                changeZt(2);
                gzTitle=twoswButton.getText().toString();
                gzValue="200000-300000";
                gzAverage="250000";
                break;
            case R.id.threesw_b:
                changeZt(3);
                gzTitle=threeswButton.getText().toString();
                gzValue="300000-400000";
                gzAverage="350000";
                break;
            case R.id.foursw_b:
                changeZt(4);
                gzTitle=fourswButton.getText().toString();
                gzValue="400000-500000";
                gzAverage="450000";
                break;
            case R.id.fivesw_b:
                changeZt(5);
                gzTitle=fiveswButton.getText().toString();
                gzValue="500000-600000";
                gzAverage="550000";
                break;
            case R.id.sixsw_b:
                changeZt(6);
                gzTitle=sixswButton.getText().toString();
                gzValue="600000-700000";
                gzAverage="650000";
                break;
            case R.id.sevensw_b:
                changeZt(7);
                gzTitle=sevenswButton.getText().toString();
                gzValue="700000-800000";
                gzAverage="750000";
                break;
            case R.id.eightsw_b:
                changeZt(8);
                gzTitle=eightswButton.getText().toString();
                gzValue="800000-900000";
                gzAverage="850000";
                break;
            case R.id.ninesw_b:
                changeZt(9);
                gzTitle=nineswButton.getText().toString();
                gzValue="900000-1000000";
                gzAverage="950000";
                break;
            case R.id.tenupsw_b:
                changeZt(10);
                gzTitle=tenupswButton.getText().toString();
                gzValue="1000000+";
                gzAverage="1000000";
                break;
                
            case R.id.onew_b:
                changeZt(11);
                gzTitle=onewButton.getText().toString();
                gzValue="10000-20000";
                gzAverage="15000";
                break;
            case R.id.twow_b:
                changeZt(12);
                gzTitle=twowButton.getText().toString();
                gzValue="20000-30000";
                gzAverage="25000";
                break;
            case R.id.threew_b:
                changeZt(13);
                gzTitle=threewButton.getText().toString();
                gzValue="30000-40000";
                gzAverage="350000";
                break;
            case R.id.fourw_b:
                changeZt(14);
                gzTitle=fourwButton.getText().toString();
                gzValue="40000-50000";
                gzAverage="45000";
                break;
            case R.id.fivew_b:
                changeZt(15);
                gzTitle=fivewButton.getText().toString();
                gzValue="50000-60000";
                gzAverage="55000";
                break;
            case R.id.sixw_b:
                changeZt(16);
                gzTitle=sixwButton.getText().toString();
                gzValue="60000-70000";
                gzAverage="65000";
                break;
            case R.id.sevenw_b:
                changeZt(17);
                gzTitle=sevenwButton.getText().toString();
                gzValue="70000-80000";
                gzAverage="75000";
                break;
            case R.id.eightw_b:
                changeZt(18);
                gzTitle=eightwButton.getText().toString();
                gzValue="80000-90000";
                gzAverage="85000";
                break;
            case R.id.ninew_b:
                changeZt(19);
                gzTitle=ninewButton.getText().toString();
                gzValue="90000-100000";
                gzAverage="95000";
                break;
                
            case R.id.onedownq_b:
                changeZt(20);
                gzTitle=onedownqButton.getText().toString();
                gzValue="0-1000";
                gzAverage="500";
                break;
            case R.id.oneq_b:
                changeZt(21);
                gzTitle=oneqButton.getText().toString();
                gzValue="1000-2000";
                gzAverage="1500";
                break;
            case R.id.twoq_b:
                changeZt(22);
                gzTitle=twoqButton.getText().toString();
                gzValue="2000-3000";
                gzAverage="2500";
                break;
            case R.id.threeq_b:
                changeZt(23);
                gzTitle=threeqButton.getText().toString();
                gzValue="3000-4000";
                gzAverage="35000";
                break;
            case R.id.fourq_b:
                changeZt(24);
                gzTitle=fourqButton.getText().toString();
                gzValue="4000-5000";
                gzAverage="4500";
                break;
            case R.id.fiveq_b:
                changeZt(25);
                gzTitle=fiveqButton.getText().toString();
                gzValue="5000-6000";
                gzAverage="5500";
                break;
            case R.id.sixq_b:
                changeZt(26);
                gzTitle=sixqButton.getText().toString();
                gzValue="6000-7000";
                gzAverage="6500";
                break;
            case R.id.sevenq_b:
                changeZt(27);
                gzTitle=sevenqButton.getText().toString();
                gzValue="7000-8000";
                gzAverage="7500";
                break;
            case R.id.eightq_b:
                changeZt(28);
                gzTitle=eightqButton.getText().toString();
                gzValue="8000-9000";
                gzAverage="8500";
                break;
            case R.id.nineq_b:
                changeZt(29);
                gzTitle=nineqButton.getText().toString();
                gzValue="9000-10000";
                gzAverage="9500";
                break;
            default:
                break;
        }
    }
    
    private void changeZt(int i){
        oneswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        twoswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        threeswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        fourswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        fiveswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        sixswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        sevenswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        eightswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        nineswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        tenupswButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        
        onewButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        twowButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        threewButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        fourwButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        fivewButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        sixwButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        sevenwButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        eightwButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        ninewButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        
        onedownqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        oneqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        twoqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        threeqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        fourqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        fiveqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        sixqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        sevenqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        eightqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        nineqButton.setTextAppearance(activity, R.style.yl_index_gz_button_unselect);
        switch (i) {
            
            case 1:
                oneswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 2:
                twoswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 3:
                threeswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 4:
                fourswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 5:
                fiveswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 6:
                sixswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 7:
                sevenswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 8:
                eightswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 9:
                nineswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 10:
                tenupswButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;

                
            case 11:
                onewButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 12:
                twowButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 13:
                threewButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 14:
                fourwButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 15:
                fivewButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 16:
                sixwButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 17:
                sevenwButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 18:
                eightwButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 19:
                ninewButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
                
            case 20:
                onedownqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 21:
                oneqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 22:
                twoqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 23:
                threeqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 24:
                fourqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 25:
                fiveqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 26:
                sixqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 27:
                sevenqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 28:
                eightqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            case 29:
                nineqButton.setTextAppearance(activity, R.style.yl_index_gz_button_select);
                break;
            default:
                break;
        }
    }
    
    private void inputGz(int num){
        gzAverage=num+"";
        if (num < 1000) {
            gzTitle="1千以下";
            gzValue="1000";
        } else if (num >= 1000 && num < 2000) {
            gzTitle="1-2千";
            gzValue="1000-2000";
        } else if (num >= 2000 && num < 3000) {
            gzTitle="2-3千";
            gzValue="2000-3000";
        } else if (num >= 3000 && num < 4000) {
            gzTitle="3-4千";
            gzValue="3000-4000";
        } else if (num >= 4000 && num < 5000) {
            gzTitle="4-5千";
            gzValue="4000-5000";
        } else if (num >= 5000 && num < 6000) {
            gzTitle="5-6千";
            gzValue="5000-6000";
        } else if (num >= 6000 && num < 7000) {
            gzTitle="6-7千";
            gzValue="6000-7000";
        } else if (num >= 7000 && num < 8000) {
            gzTitle="7-8千";
            gzValue="7000-8000";
        } else if (num >= 8000 && num < 9000) {
            gzTitle="8-9千";
            gzValue="8000-9000";
        } else if (num >= 9000 && num < 10000) {
            gzTitle="9千-1万";
            gzValue="9000-10000";
        } else if (num >= 10000 && num < 20000) {
            gzTitle="1-2万";
            gzValue="10000-20000";
        } else if (num >= 20000 && num < 30000) {
            gzTitle="2-3万";
            gzValue="20000-30000";
        } else if (num >= 30000 && num < 40000) {
            gzTitle="3-4万";
            gzValue="30000-40000";
        } else if (num >= 40000 && num < 50000) {
            gzTitle="4-5万";
            gzValue="40000-50000";
        } else if (num >= 50000 && num < 60000) {
            gzTitle="5-6万";
            gzValue="50000-60000";
        } else if (num >= 60000 && num < 70000) {
            gzTitle="6-7万";
            gzValue="60000-70000";
        } else if (num >= 70000 && num < 80000) {
            gzTitle="7-8万";
            gzValue="70000-80000";
        } else if (num >= 80000 && num < 90000) {
            gzTitle="8-9万";
            gzValue="80000-90000";
        } else if (num >= 90000 && num < 100000) {
            gzTitle="9-10万";
            gzValue="90000-100000";
        } else if (num >= 100000 && num < 200000) {
            gzTitle="10-20万";
            gzValue="100000-200000";
        } else if (num >= 200000 && num < 300000) {
            gzTitle="20-30万";
            gzValue="200000-300000";
        } else if (num >= 300000 && num < 400000) {
            gzTitle="30-40万";
            gzValue="300000-400000";
        } else if (num >= 400000 && num < 500000) {
            gzTitle="40-50万";
            gzValue="400000-500000";
        } else if (num >= 500000 && num < 600000) {
            gzTitle="50-60万";
            gzValue="500000-600000";
        } else if (num >= 600000 && num < 700000) {
            gzTitle="60-70万";
            gzValue="600000-700000";
        } else if (num >= 700000 && num < 800000) {
            gzTitle="70-80万";
            gzValue="600000-700000";
        } else if (num >= 800000 && num < 900000) {
            gzTitle="80-90万";
            gzValue="700000-800000";
        } else if (num >= 900000 && num < 100000) {
            gzTitle="90-100万";
            gzValue="800000-900000";
        } else if (num >= 1000000) {
            gzTitle="100万以上";
            gzValue="1000000+";
        }

    }

    /**
     * 查询列表的信息
     */
    INetWork ylList = new INetWork() {

                        @Override
                        public boolean validate() {
                            return true;
                        }

                        @Override
                        public Data getSubmitData() throws Exception {
                            Data data = new Data("arttalk/listArttalk.json");
                            data.addData("user_id",
                                PropertiesUtil.read(activity, PropertiesUtil.USERID));
                            data.addData("fans_first", "1");
                            JSONArray array=new JSONArray();
                            JSONObject ddJsonObject=new JSONObject();
                            ddJsonObject.put("dd","1");
                            JSONObject jxdJsonObject=new JSONObject();
                            jxdJsonObject.put("jxd","1");
                            JSONObject gdJsonObject=new JSONObject();
                            gdJsonObject.put("gd","1");
                            array.put(ddJsonObject);
                            array.put(jxdJsonObject);
                            array.put(gdJsonObject);
                            data.addData("fbtypes", array.toString());
                            data.addData("page", page);
                            data.addData("limit", C.PAGE_SIZE);
                            return data;
                        }

                        @SuppressWarnings("unchecked")
                        @Override
                        public void result(String result) throws Exception {
                            Map<String, Object> map = (Map<String, Object>) PaseJson
                                .paseJsonToObject(result);
                            List<Map<String, Object>> l = (List<Map<String, Object>>) map
                                .get("arttalkList");
                            list.addAll(l);
                            adapter.notifyDataSetChanged();
                            int total = Integer.parseInt(map.get("totalCount").toString());
                            if (l.size() + (page - 1) * C.PAGE_SIZE >= total) {
                                listView.setPullLoadMorEnable(false);
                            } else {
                                listView.setPullLoadMorEnable(true);
                            }
                        }
                    };

//    private class ReceiverBroadCase extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            page = 1;
//            list.clear();
//            NetWork.submit(activity, false, ylList);
//        }
//    }

    /**
     * 评论
     * 
     * @author cyf
     * @version $Id: QzIndexFragment.java, v 0.1 2015-12-22 下午5:03:48 cyf Exp $
     */
    class HfNetWork implements INetWork {
        private String circleId;    //圈子ID
        private String note;        //内容
        private String parentId;    //评论用户的ID
        private String parentUserid; //被评论用户ID

        public HfNetWork(String circleId, String note, String parentId, String parentUserid) {
            this.circleId = circleId;
            this.note = note;
            this.parentId = parentId;
            this.parentUserid = parentUserid;
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("artcomment/disArtTalk.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("arttalk_id", circleId);
            data.addData("note", note);
            data.addData("parent_id", parentId);
            data.addData("parent_userid", parentUserid);
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject = new JSONObject(result);
            KeyboardOperate.hideOrOpenKeyboard(activity);//关闭键盘
            plEditText.setText("");
            if (jsonObject.getString("code").equals("0")) {
                int index=Integer.parseInt(plMap.get("index").toString());
                Map<String, Object> m=list.get(index);
                m.put("isShowAllPl", "true");
                
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("circleId", PaseJson.getMapMsg(plMap, "arttalk_id"));
                map.put("parentId", PaseJson.getMapMsg(plMap, "parentId"));
                map.put("parentUserid", PaseJson.getMapMsg(plMap, "parentUserid"));
                map.put("parent_username", PaseJson.getMapMsg(plMap, "parent_username"));
                map.put("user_name", PropertiesUtil.read(activity, PropertiesUtil.NICKNAME));
                map.put("note",this.note);
                ((List<Map<String, Object>>)m.get("comment")).add(map);
                adapter.notifyDataSetChanged();
                toastShow("评论成功");
                plPopupWindow.dismiss();
            } else {
                toastShow("评论失败");
                plPopupWindow.dismiss();
            }
        }

    };
    
    /**
     * 估值
     * 
     * @author cyf
     * @version $Id: QzIndexFragment.java, v 0.1 2015-12-22 下午5:03:48 cyf Exp $
     */
    class GzNetWork implements INetWork {
        private String circleid;
        public GzNetWork(String circleid) {
            this.circleid=circleid;
        }
        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("grathrecord/appraiseRecord.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("arttalk_id", circleid);
            data.addData("appraise", gzValue);
            data.addData("appraise_han", gzTitle);
            data.addData("avg_appraise", gzAverage);
            return data;
        }

        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("code").equals("0")) {
                toastShow("估值成功");
                gzPopupWindow.dismiss();
            } else {
                toastShow("估值失败");
                gzPopupWindow.dismiss();
            }
        }

    };
}
