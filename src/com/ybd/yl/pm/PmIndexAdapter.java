package com.ybd.yl.pm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.GridViewRun;
import com.ybd.common.ListViewRun;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.DateUtil;
import com.ybd.common.tools.KeyboardOperate;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.ScreenDisplay;
import com.ybd.yl.R;
import com.ybd.yl.common.PreviewImg2Activity;
import com.ybd.yl.qz.QzXxzlActivity;

/**
 * 拍卖主页面的适配器
 * 
 * @author cyf
 * @version $Id: QzIndexAdapter.java, v 0.1 2015-12-10 下午12:53:23 cyf Exp $
 */
public class PmIndexAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private PmIndexActivity           activity;
    ImageLoader                       imageLoader = ImageLoader.getInstance();

    public PmIndexAdapter(List<Map<String, Object>> list, PmIndexActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String, Object> map = list.get(position);
        final List<Map<String, Object>> l = (List<Map<String, Object>>) map.get("picMsg");
        final List<Map<String, Object>> l2 = (List<Map<String, Object>>) map.get("comment");
        final List<Map<String, Object>> l3 = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> l4 = new ArrayList<Map<String, Object>>();

        final List<Map<String, Object>> l5 = (List<Map<String, Object>>) map.get("priceRecord");
        final List<Map<String, Object>> l6 = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> l7 = new ArrayList<Map<String, Object>>();

        if (l2.size() > 3) {
            l3.addAll(l2.subList(0, 3));
            l4.addAll(l2.subList(3, l2.size()));
        } else {
            l3.addAll(l2);
        }

        if (l5.size() > 3) {
            l6.addAll(l5.subList(0, 3));
            l7.addAll(l5.subList(3, l5.size()));
        } else {
            l6.addAll(l5);
        }

        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.pm_index_item, null);// 这个过程相当耗时间
            viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
            viewHoler.dvImageView = (ImageView) convertView.findViewById(R.id.dv_iv);
            viewHoler.userNameTextView = (TextView) convertView.findViewById(R.id.username_tv);
            viewHoler.zjImageView = (ImageView) convertView.findViewById(R.id.zj_iv);
            viewHoler.timeTextView = (TextView) convertView.findViewById(R.id.time_tv);
            viewHoler.ddTextView = (TextView) convertView.findViewById(R.id.dd_tv);
            viewHoler.gmTextView = (TextView) convertView.findViewById(R.id.gm_tv);
            viewHoler.mcTextView = (TextView) convertView.findViewById(R.id.mc_tv);
            viewHoler.xyTextView = (TextView) convertView.findViewById(R.id.xy_tv);
            viewHoler.fsTextView = (TextView) convertView.findViewById(R.id.fs_tv);

            viewHoler.zzTextView = (TextView) convertView.findViewById(R.id.zz_tv);
            viewHoler.ccTextView = (TextView) convertView.findViewById(R.id.cc_tv);
            viewHoler.zdTextView = (TextView) convertView.findViewById(R.id.zd_tv);
            viewHoler.xsTextView = (TextView) convertView.findViewById(R.id.xs_tv);
            viewHoler.pjfTextView = (TextView) convertView.findViewById(R.id.pjf_tv);
            viewHoler.pjgzTextView = (TextView) convertView.findViewById(R.id.pjgz_tv);
            viewHoler.descriptionTextView = (TextView) convertView
                .findViewById(R.id.description_tv);
            //            viewHoler.tsLinearLayout = (LinearLayout) convertView.findViewById(R.id.ts_ll);
            //            viewHoler.tsImageView = (ImageView) convertView.findViewById(R.id.ts_iv);
            //            viewHoler.tsTextView = (TextView) convertView.findViewById(R.id.ts_tv);
            viewHoler.zfLinearLayout = (LinearLayout) convertView.findViewById(R.id.zf_ll);
            viewHoler.zfImageView = (ImageView) convertView.findViewById(R.id.zf_iv);
            viewHoler.zfTextView = (TextView) convertView.findViewById(R.id.zf_tv);

            viewHoler.plLinearLayout = (LinearLayout) convertView.findViewById(R.id.pl_ll);
            viewHoler.plImageView = (ImageView) convertView.findViewById(R.id.pl_iv);
            viewHoler.plTextView = (TextView) convertView.findViewById(R.id.pl_tv);

            viewHoler.tpGridView = (GridViewRun) convertView.findViewById(R.id.tp_gv);

            viewHoler.plListViewRun = (ListViewRun) convertView.findViewById(R.id.pl_lv);
            viewHoler.plLinearLayout2 = (LinearLayout) convertView.findViewById(R.id.pl2_ll);
            viewHoler.xsqbTextView = (TextView) convertView.findViewById(R.id.xsqb_tv);

            viewHoler.cjListViewRun = (ListViewRun) convertView.findViewById(R.id.cj_lv);
            viewHoler.cjLinearLayout = (LinearLayout) convertView.findViewById(R.id.cj_ll);
            viewHoler.cjXsqbTextView = (TextView) convertView.findViewById(R.id.cj_xsqb_tv);

            viewHoler.mqjTextView = (TextView) convertView.findViewById(R.id.mqj_tv);
            viewHoler.jykButton = (Button) convertView.findViewById(R.id.jyk_b);

            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        if (!PaseJson.getMapMsg(map, "icon_url").equals("")) {
            imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "icon_url"),
                viewHoler.txImageView, MainApplication.getRoundOptions());
        }
        //是否是大V
        if (map.get("is_bv").toString().equals("0")) {
            viewHoler.dvImageView.setVisibility(View.GONE);
        } else {
            viewHoler.dvImageView.setVisibility(View.VISIBLE);
        }
        viewHoler.userNameTextView.setText(PaseJson.getMapMsg(map, "nick_name"));
        //是否是专家
        if (map.get("is_export").toString().equals("0")) {
            viewHoler.zjImageView.setVisibility(View.GONE);
        } else {
            viewHoler.zjImageView.setVisibility(View.VISIBLE);
        }
        viewHoler.txImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("bcrUserId", PaseJson.getMapMsg(map, "user_id"));
                intent.setClass(activity, QzXxzlActivity.class);
                activity.startActivity(intent);
            }
        });
        viewHoler.timeTextView.setText(DateUtil.getTimeFormat("HH:mm",
            PaseJson.getMapMsg(map, "issue_time")));
        //        Map<String, Object> m=(Map<String, Object>) map.get("json_attributes");
        viewHoler.ddTextView.setText(PaseJson.getMapMsg(map, "address"));
        viewHoler.gmTextView.setText(PaseJson.getMapMsg(map, "buy_vol") + "件");
        viewHoler.mcTextView.setText(PaseJson.getMapMsg(map, "sale_vol") + "件");
        viewHoler.xyTextView.setText(PaseJson.getMapMsg(map, "degree_credit") + "分");
        viewHoler.fsTextView.setText(PaseJson.getMapMsg(map, "followers_count"));
        viewHoler.zzTextView.setText("作者：" + PaseJson.getMapMsg(map, "author"));
        viewHoler.ccTextView.setText("尺寸：" + PaseJson.getMapMsg(map, "mea_len") + "*"
                                     + PaseJson.getMapMsg(map, "mea_wide"));
        viewHoler.zdTextView.setText("质地：" + PaseJson.getMapMsg(map, "texture"));
        viewHoler.xsTextView.setText("形式：" + PaseJson.getMapMsg(map, "xs"));
        viewHoler.pjfTextView.setText("平均分：" + PaseJson.getMapMsg(map, "avg_grade"));
        viewHoler.pjgzTextView.setText("评价估值：" + PaseJson.getMapMsg(map, "avg_appraise"));
        viewHoler.descriptionTextView.setText("详情：" + PaseJson.getMapMsg(map, "description"));
        viewHoler.mqjTextView.setText(PaseJson.getMapMsg(map, "curprice"));
        viewHoler.plTextView.setText(l2.size() + "");
        viewHoler.plLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Map<String, Object> m = new HashMap<String, Object>();
                activity.plMap.put("circleId", PaseJson.getMapMsg(map, "arttalk_id"));
                activity.plMap.put("note", "");
                activity.plMap.put("parentId", "");
                activity.plMap.put("parentUserid", "");
                activity.plMap.put("parent_username", "");
                activity.plMap.put("index", position);
                activity.plPopupWindow.showAsDropDown(activity.titleView, 0, 0);
                KeyboardOperate.hideOrOpenKeyboard(activity);
            }
        });
        BaseAdapter tpAdapter = new PmIndexTpAdapter(l, activity);
        viewHoler.tpGridView.setAdapter(tpAdapter);
        tpAdapter.notifyDataSetChanged();
        viewHoler.tpGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("object", (Serializable) l);
                intent.setClass(activity, PreviewImg2Activity.class);
                activity.startActivity(intent);
                activity.plPopupWindow.showAsDropDown(activity.titleView, 0, 0);
                KeyboardOperate.hideOrOpenKeyboard(activity);
            }
        });
        final BaseAdapter plAdapter = new PmIndexPlAdapter(l3, activity);
        viewHoler.plListViewRun.setAdapter(plAdapter);
        viewHoler.plListViewRun.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                Map<String, Object> map2 = l2.get(position2);
                activity.plMap.put("circleId", PaseJson.getMapMsg(map, "arttalk_id"));
                activity.plMap.put("note", "");
                activity.plMap.put("parentId", PaseJson.getMapMsg(map2, "id"));
                activity.plMap.put("parentUserid", PaseJson.getMapMsg(map2, "user_id"));
                activity.plMap.put("parent_username", PaseJson.getMapMsg(map2, "user_name"));
                activity.plMap.put("index", position);
                activity.plPopupWindow.showAsDropDown(activity.titleView, 0, 0);
                KeyboardOperate.hideOrOpenKeyboard(activity);
            }
        });
        plAdapter.notifyDataSetChanged();
        ScreenDisplay.setListViewHeightBasedOnChildren(viewHoler.plListViewRun);
        if (l2.size() == 0) {
            viewHoler.plLinearLayout2.setVisibility(View.GONE);
        } else {
            viewHoler.plLinearLayout2.setVisibility(View.VISIBLE);
        }
        if (l4.size() == 0) {
            viewHoler.xsqbTextView.setVisibility(View.GONE);
        } else {
            viewHoler.xsqbTextView.setVisibility(View.VISIBLE);
        }
        if (!PaseJson.getMapMsg(map, "isShowAllPl").equals("")
            && PaseJson.getMapMsg(map, "isShowAllPl").equals("true")) {//展开全部的情况
            l3.addAll(l4);
            viewHoler.xsqbTextView.setVisibility(View.GONE);
        }
        viewHoler.xsqbTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.addAll(l4);
                plAdapter.notifyDataSetChanged();
                v.setVisibility(View.GONE);
            }
        });

        viewHoler.jykButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWork.submit(activity, new JykNetWork(PaseJson.getMapMsg(map, "arttalk_id")));
            }
        });
        return convertView;
    }

    class ViewHoler {
        ImageView    txImageView;        //头像
        ImageView    dvImageView;        //头像的类型（用户的类型，是否是大V或者是别的）
        TextView     userNameTextView;   //用户名
        ImageView    zjImageView;        //是否是专家
        TextView     timeTextView;       //时间
        TextView     ddTextView;         //地点
        TextView     gmTextView;         //购买
        TextView     mcTextView;         //卖出
        TextView     xyTextView;         //信用
        TextView     fsTextView;         //粉丝
        TextView     zzTextView;         //作者
        TextView     ccTextView;         //尺寸
        TextView     zdTextView;         //质地
        TextView     xsTextView;         //形式
        TextView     pjfTextView;        //平均分
        TextView     pjgzTextView;       //评价估值
        TextView     descriptionTextView; //描述
        TextView     mqjTextView;        //目前价
                                          //        LinearLayout tsLinearLayout;     //投诉
                                          //        ImageView    tsImageView;        //投诉
                                          //        TextView     tsTextView;         //投诉
        LinearLayout zfLinearLayout;     //转发
        ImageView    zfImageView;        //转发
        TextView     zfTextView;         //转发
        LinearLayout plLinearLayout;     //评论的层
        ImageView    plImageView;        //评论
        TextView     plTextView;         //评论的数量
        GridViewRun  tpGridView;         //图片的列表
        ListViewRun  plListViewRun;      //艺论的评论
        LinearLayout plLinearLayout2;    //显示全部评论的层
        TextView     xsqbTextView;       //显示全部的评论

        ListViewRun  cjListViewRun;      //出价的评论
        LinearLayout cjLinearLayout;     //出价显示全部评论的层
        TextView     cjXsqbTextView;     //出价显示全部的评论

        Button       jykButton;          //加一口
    }
    
    /**
     * 加一口
     * 
     * @author cyf
     * @version $Id: QzIndexFragment.java, v 0.1 2015-12-22 下午5:03:48 cyf Exp $
     */
    class JykNetWork implements INetWork {
        private String arttalkid;
        public JykNetWork(String arttalkid) {
            this.arttalkid=arttalkid;
        }
        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("addpricerecord/improvePrice.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("arttalk_id", arttalkid);
            return data;
        }

        @Override
        public void result(String result) throws Exception {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("code").equals("0")) {
                activity.toastShow("加价成功");
            } else {
                activity.toastShow("加价失败");
            }
        }

    };
}
