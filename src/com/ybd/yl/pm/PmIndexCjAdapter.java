package com.ybd.yl.pm;

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
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 艺论主页面-出价的适配器
 * 
 * @author cyf
 * @version $Id: QzIndexTpAdapter.java, v 0.1 2015-12-10 下午12:53:47 cyf Exp $
 */
public class PmIndexCjAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    ImageLoader                       imageLoader = ImageLoader.getInstance();

    public PmIndexCjAdapter(List<Map<String, Object>> list, Activity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.pm_index_cj_item, null);// 这个过程相当耗时间
            viewHoler.nrTextView = (TextView) convertView.findViewById(R.id.nr_tv);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        String str = PaseJson.getMapMsg(map, "remark")+"：出价";
        str+=PaseJson.getMapMsg(map, "endprice");
        str+="元";
        viewHoler.nrTextView.setText(str);
        return convertView;
    }

    class ViewHoler {
        TextView nrTextView; //内容
    }

}
