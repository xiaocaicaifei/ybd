package com.ybd.yl.yl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.common.tools.ScreenDisplay;
import com.ybd.yl.R;

/**
 * 艺论主页面-图片的适配器
 * 
 * @author cyf
 * @version $Id: QzIndexTpAdapter.java, v 0.1 2015-12-10 下午12:53:47 cyf Exp $
 */
public class YlIndexTpAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private Activity                  activity;
    ImageLoader                       imageLoader = ImageLoader.getInstance();

    public YlIndexTpAdapter(List<Map<String, Object>> list, Activity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.yl_index_tp_item, null);// 这个过程相当耗时间
            viewHoler.tpImageView = (ImageView) convertView.findViewById(R.id.tp_iv);
            LayoutParams lp=viewHoler.tpImageView.getLayoutParams();
            lp.height=(ScreenDisplay.getScreenWidth(activity)-60)/3;
            viewHoler.tpImageView.setLayoutParams(lp);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        if(!PaseJson.getMapMsg(map, "thumb_url").equals("")){
            imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "thumb_url"), viewHoler.tpImageView,
                MainApplication.getOptions());
        }
        return convertView;
    }

    class ViewHoler {
        ImageView    tpImageView;        //图片
    }
}
