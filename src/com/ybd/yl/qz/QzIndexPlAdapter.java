package com.ybd.yl.qz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.L;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 圈子主页面-评论的适配器
 * 
 * @author cyf
 * @version $Id: QzIndexTpAdapter.java, v 0.1 2015-12-10 下午12:53:47 cyf Exp $
 */
public class QzIndexPlAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    ImageLoader                       imageLoader = ImageLoader.getInstance();

    public QzIndexPlAdapter(List<Map<String, Object>> list, Activity activity) {
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
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(activity).inflate(R.layout.qz_index_pl_item, null);// 这个过程相当耗时间
            viewHoler.plrTextView = (TextView) convertView.findViewById(R.id.plr_tv);
            viewHoler.hfTextView = (TextView) convertView.findViewById(R.id.hf_tv);
            viewHoler.bplrTextView = (TextView) convertView.findViewById(R.id.bplr_tv);
            viewHoler.nrTextView = (TextView) convertView.findViewById(R.id.nr_tv);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
       viewHoler.plrTextView.setText(PaseJson.getMapMsg(map, "user_name"));
       if(PaseJson.getMapMsg(map, "parent_username").equals("")){
           viewHoler.hfTextView.setVisibility(View.GONE);
           viewHoler.bplrTextView.setVisibility(View.GONE);
       }else{
           viewHoler.hfTextView.setVisibility(View.VISIBLE);
           viewHoler.bplrTextView.setVisibility(View.VISIBLE);
           viewHoler.bplrTextView.setText(PaseJson.getMapMsg(map, "parent_username"));
       }
       viewHoler.nrTextView.setText(PaseJson.getMapMsg(map, "note"));
        return convertView;
    }

    class ViewHoler {
        TextView plrTextView;//评论人
        TextView hfTextView;//隔开评论人和被评论人的回复
        TextView bplrTextView;//被评论人
        TextView nrTextView;//内容
    }
}
