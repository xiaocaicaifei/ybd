/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.yl;

import java.util.ArrayList;
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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.ybd.yl.home.HomeClickListener;

/**
 * 艺论-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class YlIndexActivity extends BaseActivity implements HomeClickListener, OnClickListener {
    private XListView                 listView;
    private YlIndexAdapter               adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    DrawerLayout                      drawerLayout;
    private View                      cxPopupView;
    private PopupWindow               popupWindow;
    private EditText                  plEditText;                                 //评论
    private Button                    fsButton;                                    //发送按钮
    private Map<String, Object>       plMap;                                      //点击评论后，带回来的信息
                                                                                   //评论
    private PopupWindow gzPopupWindow;//估值的弹出框
    private View gzPopupView;

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
        adapter = new YlIndexAdapter(list, activity, onClickListener,gzPopupWindow);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化评论弹出框
     */
    private void initPlPopWindow() {
        cxPopupView = inflater.inflate(R.layout.qz_pl_popupwindow, null);
        popupWindow = createPopupWindwo(cxPopupView, ScreenDisplay.getScreenWidth(activity));
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
        
       
    }
    /**
     * 评论的点击事件
     */
    private OnClickListener onClickListener = new OnClickListener() {

                                                @SuppressWarnings("unchecked")
                                                @Override
                                                public void onClick(View v) {
                                                    plMap = (Map<String, Object>) v.getTag();
                                                    popupWindow.showAsDropDown(
                                                        activity.getCurrentFocus(),
                                                        0, 0);
                                                    KeyboardOperate.hideOrOpenKeyboard(activity);
                                                }
                                            };

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
    public void onHomeclick(View v) {

    }

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
            default:
                break;
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

    private class ReceiverBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            page = 1;
            list.clear();
            NetWork.submit(activity, false, ylList);
        }
    }

    /**
     * 回复
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
                plMap.put("user_name", PropertiesUtil.read(activity, PropertiesUtil.NICKNAME));
                plMap.put("note",this.note);
                ((List<Map<String, Object>>)m.get("comment")).add(plMap);
                adapter.notifyDataSetChanged();
                toastShow("评论成功");
                popupWindow.dismiss();
            } else {
                toastShow("评论失败");
                popupWindow.dismiss();
            }
        }

    };
}
