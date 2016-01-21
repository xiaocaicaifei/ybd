package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 消息-群组的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class XxQzAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private XxQzActivity             activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public XxQzAdapter(List<Map<String, Object>> list, XxQzActivity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.xx_qz_item, null);// 这个过程相当耗时间
            viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
            viewHoler.nameTextView = (TextView) convertView.findViewById(R.id.name_tv);
            viewHoler.ztTextView = (TextView) convertView.findViewById(R.id.zt_tv);
            viewHoler.ldImageView=(ImageView) convertView.findViewById(R.id.ld_iv);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        imageLoader.displayImage(C.IP+PaseJson.getMapMsg(map, "logo_url"), viewHoler.txImageView,MainApplication.getRoundOffOptions());
        viewHoler.nameTextView.setText(PaseJson.getMapMsg(map, "groupname"));
       
        if(PaseJson.getMapMsg(map, "state").equals("1")){
            viewHoler.ztTextView.setText("未开放");
        }else if(PaseJson.getMapMsg(map, "state").equals("2")){
            viewHoler.ztTextView.setText("正在开放中...");
        }else if(PaseJson.getMapMsg(map, "state").equals("3")){
            viewHoler.ztTextView.setText("已关闭");
        }
        return convertView;
    }

    class ViewHoler {
        ImageView txImageView; //头像
        TextView  nameTextView;  //拍卖会的名字
        TextView  ztTextView;//拍卖的状态
        ImageView ldImageView;//是否屏蔽消息的标志
        Button slButton;//显示消息数量
    }

}
