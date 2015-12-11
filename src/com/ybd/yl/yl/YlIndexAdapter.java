package com.ybd.yl.yl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.ybd.yl.R;
import com.ybd.yl.dialog.SelectGzActivity;

public class YlIndexAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;

    public YlIndexAdapter(List<Map<String, Object>> list, Activity activity) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler=new ViewHoler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.yl_index_item, null);// 这个过程相当耗时间
            viewHoler.wygzButton=(Button) convertView.findViewById(R.id.wygz_b);
            viewHoler.sjtjButton=(Button) convertView.findViewById(R.id.sjtj_b);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        viewHoler.wygzButton.setOnClickListener(new OnClickListener() {//我要估值
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(activity, SelectGzActivity.class);
                activity.startActivity(intent);
            }
        });
        viewHoler.sjtjButton.setOnClickListener(new OnClickListener() {//数据统计
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(activity, YlSjtjActivity.class);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHoler {
        ImageView photoImageView;
        Button wygzButton;
        Button sjtjButton;
    }
}
