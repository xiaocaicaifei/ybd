package com.ybd.yl.gr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.MainApplication;
import com.ybd.yl.R;

/**
 * 显示相册的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class GrIndexAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public GrIndexAdapter(List<Map<String, Object>> list, Activity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.gr_index_item, null);// 这个过程相当耗时间
            viewHoler = new ViewHoler();
            viewHoler.xcImageView = (ImageView) convertView.findViewById(R.id.xc_iv);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        if (!map.get("thumb_url").toString().equals("")) {
            imageLoader.displayImage(C.IP+map.get("thumb_url").toString(), viewHoler.xcImageView,
                MainApplication.getRoundOffOptions());
        }
        return convertView;
    }

    class ViewHoler {
        ImageView xcImageView;
    }
}
