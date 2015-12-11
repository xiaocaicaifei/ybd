package com.ybd.yl.qz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.GridViewRun;
import com.ybd.common.L;
import com.ybd.common.ListViewRun;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.DateUtil;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.ScreenDisplay;
import com.ybd.yl.R;

/**
 * 圈子主页面的适配器
 * 
 * @author cyf
 * @version $Id: QzIndexAdapter.java, v 0.1 2015-12-10 下午12:53:23 cyf Exp $
 */
public class QzIndexAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    ImageLoader                       imageLoader = ImageLoader.getInstance();

    public QzIndexAdapter(List<Map<String, Object>> list, Activity activity) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = list.get(position);
        List<Map<String, Object>> l = (List<Map<String, Object>>) map.get("circlePicMsg");
        List<Map<String, Object>> l2 = (List<Map<String, Object>>) map.get("comment");
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.qz_index_item, null);// 这个过程相当耗时间
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
            viewHoler.descriptionTextView = (TextView) convertView
                .findViewById(R.id.description_tv);
            viewHoler.tsLinearLayout = (LinearLayout) convertView.findViewById(R.id.ts_ll);
            viewHoler.tsImageView = (ImageView) convertView.findViewById(R.id.ts_iv);
            viewHoler.tsTextView = (TextView) convertView.findViewById(R.id.ts_tv);
            viewHoler.zfLinearLayout = (LinearLayout) convertView.findViewById(R.id.zf_ll);
            viewHoler.zfImageView = (ImageView) convertView.findViewById(R.id.zf_iv);
            viewHoler.zfTextView = (TextView) convertView.findViewById(R.id.zf_tv);

            viewHoler.plLinearLayout = (LinearLayout) convertView.findViewById(R.id.pl_ll);
            viewHoler.plImageView = (ImageView) convertView.findViewById(R.id.pl_iv);
            viewHoler.plTextView = (TextView) convertView.findViewById(R.id.pl_tv);
            viewHoler.zanLinearLayout = (LinearLayout) convertView.findViewById(R.id.zan_ll);
            viewHoler.zanTextView = (TextView) convertView.findViewById(R.id.zan_tv);
            viewHoler.zanImageView = (ImageView) convertView.findViewById(R.id.zan_iv);
            viewHoler.tpGridView = (GridViewRun) convertView.findViewById(R.id.tp_gv);
            viewHoler.plListViewRun = (ListView) convertView.findViewById(R.id.pl_lvr);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "icon_url"), viewHoler.txImageView,
            MainApplication.getRoundOptions());
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
        viewHoler.timeTextView.setText(DateUtil.getTimeFormat("HH:mm", PaseJson.getMapMsg(map, "issue_time")));
        Map<String, Object> m=(Map<String, Object>) map.get("json_attributes");
        viewHoler.ddTextView.setText(PaseJson.getMapMsg(m, "address"));
        viewHoler.gmTextView.setText(PaseJson.getMapMsg(map, "buy_vol") + "件");
        viewHoler.mcTextView.setText(PaseJson.getMapMsg(map, "sale_vol") + "件");
        viewHoler.xyTextView.setText(PaseJson.getMapMsg(map, "degree_credit") + "分");
        viewHoler.fsTextView.setText(PaseJson.getMapMsg(map, "followers_count"));
        viewHoler.descriptionTextView.setText(PaseJson.getMapMsg(map, "description"));
        viewHoler.plTextView.setText(l2.size() + "");
        viewHoler.zanTextView.setText(PaseJson.getMapMsg(map, "thum_count").toString());
        BaseAdapter tpAdapter = new QzIndexTpAdapter(l, activity);
        viewHoler.tpGridView.setAdapter(tpAdapter);
        tpAdapter.notifyDataSetChanged();
        BaseAdapter plAdapter = new QzIndexPlAdapter(l2, activity);
        viewHoler.plListViewRun.setAdapter(plAdapter);
        plAdapter.notifyDataSetChanged();
        ScreenDisplay.setListViewHeightBasedOnChildren(viewHoler.plListViewRun);
        if(l2.size()==0){
            viewHoler.plListViewRun.setVisibility(View.GONE);
        }else{
            viewHoler.plListViewRun.setVisibility(View.VISIBLE);
        }

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
        TextView     descriptionTextView; //描述
        LinearLayout tsLinearLayout;     //投诉
        ImageView    tsImageView;        //投诉
        TextView     tsTextView;         //投诉
        LinearLayout zfLinearLayout;     //转发
        ImageView    zfImageView;        //转发
        TextView     zfTextView;         //转发
        LinearLayout plLinearLayout;     //评论的层
        ImageView    plImageView;        //评论
        TextView     plTextView;         //评论的数量
        TextView     zanTextView;        //赞的数量
        ImageView    zanImageView;       //赞的图标
        LinearLayout zanLinearLayout;    //赞的层
        GridViewRun     tpGridView;         //图片的列表
        ListView  plListViewRun;      //艺论的评论
    }
}
