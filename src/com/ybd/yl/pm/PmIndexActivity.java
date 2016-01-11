/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.pm;

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
 * 拍卖-主页
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class PmIndexActivity extends BaseActivity implements  OnClickListener {
    private XListView                 listView;
    private PmIndexAdapter               adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    DrawerLayout                      drawerLayout;
    private View                      cxPopupView;
    public PopupWindow               plPopupWindow;
    private EditText                  plEditText;                                 //评论
    private Button                    fsButton;                                    //发送按钮
    public Map<String, Object>       plMap=new HashMap<String, Object>();                                      //点击评论后，带回来的信息
                                                                                   //评论
    public PopupWindow gzPopupWindow;//估值的弹出框
    private String gzTitle;//估值选择的数值的名字
    private String gzValue;//估值的选择的数值
    private String gzAverage;//估值的平均值
    public static String selectCircleID;//选择的议论的ID
    public int selectPosition;//选择的议论的List的位置
    @Override
    protected void initComponentBase() {
        setContentView(R.layout.pm_index);
        initPublicView("拍卖", R.drawable.yl_sz, R.drawable.yl_sc, PmIndexActivity.this,
            PmIndexActivity.this);
        initPlPopWindow();
        initListView();
        //        initDrawerLayout();
        page = 1;
        NetWork.submit(activity, pmList);
        registBroadcast();
    }

    private void initListView() {
        listView = (XListView) findViewById(R.id.list_lv);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        setXListViewListener(listView, pmList, list);
        adapter = new PmIndexAdapter(list, this);
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
                NetWork.submit(activity, false, pmList);
            }
        }, BroadcaseUtil.YL_SCCG);
        
      //加一口成功的广播（加价成功后，刷新加价记录）
        BroadcaseUtil.registBroadcase(activity, new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                String circleid=list.get(selectPosition).get("arttalk_id").toString();
                NetWork.submit(activity, new JjjlNetWork(circleid));
            }
        }, BroadcaseUtil.PM_JYK_SUCCESS);
    }

 

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_rl:
                Intent intent = new Intent();
                intent.setClass(activity, PmScActivity.class);
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
            default:
                break;
        }
    }
    
    /**
     * 查询列表的信息
     */
    INetWork pmList = new INetWork() {

                        @Override
                        public boolean validate() {
                            return true;
                        }

                        @Override
                        public Data getSubmitData() throws Exception {
                            Data data = new Data("arttalk/listAuction.json");
                            data.addData("user_id",
                                PropertiesUtil.read(activity, PropertiesUtil.USERID));
                            data.addData("fbtype_1", "1");
                            data.addData("fbtype_2", "2");
                            data.addData("fbtype_3", "3");
                            data.addData("recommend_first", "1");//推荐优先
                            data.addData("sale_first", "1");//卖家排行
                            data.addData("buy_first", "1");//买家排行
                            data.addData("comment_first", "1");//评价排行
                            data.addData("fans_first", "1");//粉丝排行
                            data.addData("moods_first", "1");//人气排行
                            data.addData("price_range", "0-10000");//价格区间
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
                                .get("auctionList");
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
    /**
     * 获取加价记录
     * 
     * @author cyf
     * @version $Id: QzIndexFragment.java, v 0.1 2015-12-22 下午5:03:48 cyf Exp $
     */
    class JjjlNetWork implements INetWork {
        private String circleid;
        public JjjlNetWork(String circleid) {
            this.circleid=circleid;
        }
        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("addpricerecord/listAddPriceRecord.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            JSONArray array=new JSONArray();
            array.put(circleid);
            data.addData("artIds", array.toString());
            return data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void result(String result) throws Exception {
            Map<String, Object> m=(Map<String,Object>)PaseJson.paseJsonToObject(result);
            List<Map<String,Object>> l=(List<Map<String, Object>>) m.get("data");
            if (PaseJson.getMapMsg(m, "code").equals("0")) {
                toastShow("加价成功");
               ((List<Map<String,Object>>)list.get(selectPosition).get("priceRecord")).clear();
               ((List<Map<String,Object>>)list.get(selectPosition).get("priceRecord")).addAll(l);
               adapter.notifyDataSetChanged();
            } else {
                toastShow("加价失败");
            }
        }

    };
}
