package com.ybd.yl.qz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.L;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.ScreenDisplay;
import com.ybd.yl.R;

/**
 * 圈子-上传-上传图片-图片的适配器
 * 
 * @author cyf
 * @version $Id: QzIndexTpAdapter.java, v 0.1 2015-12-10 下午12:53:47 cyf Exp $
 */
public class QzScSctpAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    ImageLoader                       imageLoader = ImageLoader.getInstance();

    public QzScSctpAdapter(List<Map<String, Object>> list, Activity activity) {
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
        Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.qz_sc_sctp_item, null);// 这个过程相当耗时间
            viewHoler.tpImageView = (ImageView) convertView.findViewById(R.id.tp_iv);
            viewHoler.tpCloseImageView = (ImageView) convertView.findViewById(R.id.tpclose_iv);
            LayoutParams lp=viewHoler.tpImageView.getLayoutParams();
            lp.height=(ScreenDisplay.getScreenWidth(activity)-60)/4;
            viewHoler.tpImageView.setLayoutParams(lp);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        if(!PaseJson.getMapMsg(map, "path").equals("")){
            L.v(PaseJson.getMapMsg(map, "path"));
            imageLoader.displayImage(PaseJson.getMapMsg(map, "path"), viewHoler.tpImageView,
                MainApplication.getOptions());
        }
        if(position==list.size()-1){
            viewHoler.tpCloseImageView.setVisibility(View.GONE);
            viewHoler.tpImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(activity, QzScActivity.class);
                    List<Map<String, Object>> l=new ArrayList<Map<String,Object>>();
                    l.addAll(list.subList(0, list.size()-1));
                    intent.putExtra("path", (Serializable)l);
                    activity.startActivity(intent);
                }
            });
        }else{
            viewHoler.tpCloseImageView.setVisibility(View.VISIBLE);
        }
        viewHoler.tpCloseImageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHoler {
        ImageView    tpImageView;        //图片
        ImageView    tpCloseImageView;        //关闭图片
    }
}
