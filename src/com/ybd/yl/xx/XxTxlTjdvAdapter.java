package com.ybd.yl.xx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 消息-通讯录-推荐大V的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class XxTxlTjdvAdapter extends BaseAdapter{
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private XxTxlTjdvActivity             activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public XxTxlTjdvAdapter(List<Map<String, Object>> list, XxTxlTjdvActivity activity) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.xx_txl_tjdv_item, null);// 这个过程相当耗时间
            viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
            viewHoler.ncTextView = (TextView) convertView.findViewById(R.id.nc_tv);
            viewHoler.gmTextView = (TextView) convertView.findViewById(R.id.gm_tv);
            viewHoler.mcTextView = (TextView) convertView.findViewById(R.id.mc_tv);
            viewHoler.fsTextView = (TextView) convertView.findViewById(R.id.fs_tv);
            viewHoler.xyTextView = (TextView) convertView.findViewById(R.id.xy_tv);
            viewHoler.jhyImageView=(ImageView) convertView.findViewById(R.id.jhy_iv);
            viewHoler.zjImageView=(ImageView) convertView.findViewById(R.id.zj_iv);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        imageLoader.displayImage(C.IP+PaseJson.getMapMsg(map, "icon_url"), viewHoler.txImageView,MainApplication.getRoundOffOptions());
        viewHoler.ncTextView.setText(PaseJson.getMapMsg(map, "nick_name"));
        viewHoler.gmTextView.setText(PaseJson.getMapMsg(map, "buy_vol")+"分");
        viewHoler.mcTextView.setText(PaseJson.getMapMsg(map, "sale_vol")+"件");
        viewHoler.xyTextView.setText(PaseJson.getMapMsg(map, "degree_credit")+"分");
        viewHoler.fsTextView.setText(PaseJson.getMapMsg(map, "followers_count"));
        if(PaseJson.getMapMsg(map, "is_myfriend").equals("0")){
            viewHoler.jhyImageView.setBackgroundResource(R.drawable.xx_txl_jhy_sel);
            viewHoler.jhyImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.putExtra("hyObject", (Serializable)map);
                    intent.setClass(activity, XxTxlTjdvHyyzActivity.class);
                    activity.startActivity(intent);
                }
            });
        }else{
            viewHoler.jhyImageView.setBackgroundResource(R.drawable.xx_txl_jhy_unsel);
        }
        if(PaseJson.getMapMsg(map, "is_export").equals("0")){
            viewHoler.zjImageView.setBackgroundResource(R.drawable.xx_txl_zj_sel);
        }else{
            viewHoler.zjImageView.setBackgroundResource(R.drawable.xx_txl_zj_unsel);
        }

        return convertView;
    }

    class ViewHoler {
        ImageView txImageView; //头像
        TextView  ncTextView;  //昵称
        TextView gmTextView;//购买
        TextView mcTextView;//卖出
        TextView xyTextView;//信用
        TextView fsTextView;//粉丝
        ImageView jhyImageView;//加好友
        ImageView zjImageView;//设专家
    }
    

}
