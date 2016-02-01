package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 消息-通讯录-聊天的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class XxQzLtAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private XxQzLtActivity         activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public XxQzLtAdapter(List<Map<String, Object>> list, XxQzLtActivity activity) {
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
    public int getViewTypeCount() {
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        Map<String, Object> map=list.get(position);
        if(PaseJson.getMapMsg(map, "sender_type").equals("1")){
            return 1;//别人发的文字
        }else if(PaseJson.getMapMsg(map, "sender_type").equals("0")){
            return 0;//自己发的文字
        }else if(PaseJson.getMapMsg(map, "sender_type").equals("2")){
            return 2;//自己发的图片
        }else if(PaseJson.getMapMsg(map, "sender_type").equals("3")){
            return 3;//别人发的图片
        }
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        ViewHoler viewHoler2 = null;
        ViewHoler2 viewHoler3=null;
        ViewHoler2 viewHoler4=null;
        int type=getItemViewType(position);
        
        if(type==0){
            if (convertView == null) {
                viewHoler = new ViewHoler();
                convertView = LayoutInflater.from(activity).inflate(R.layout.xx_qz_lt_item, null);// 这个过程相当耗时间
                viewHoler.timeTextView=(TextView) convertView.findViewById(R.id.time_tv);
                viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
                viewHoler.nrTextView=(TextView) convertView.findViewById(R.id.nr_tv);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }
            viewHoler.timeTextView.setText(PaseJson.getMapMsg(map, "send_time"));
            viewHoler.nrTextView.setText(PaseJson.getMapMsg(map, "send_content"));
            imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "sender_icon_url"), viewHoler.txImageView,
                MainApplication.getRoundOffOptions());
        }else if(type==1){
            if (convertView == null) {
                viewHoler2 = new ViewHoler();
                convertView = LayoutInflater.from(activity).inflate(R.layout.xx_qz_lt_item2, null);// 这个过程相当耗时间
                viewHoler2.timeTextView=(TextView) convertView.findViewById(R.id.time_tv);
                viewHoler2.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
                viewHoler2.nrTextView=(TextView) convertView.findViewById(R.id.nr_tv);
                convertView.setTag(viewHoler2);
            } else {
                viewHoler2 = (ViewHoler) convertView.getTag();
            }
            viewHoler2.timeTextView.setText(PaseJson.getMapMsg(map, "send_time"));
            viewHoler2.nrTextView.setText(PaseJson.getMapMsg(map, "send_content"));
            imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "sender_icon_url"), viewHoler2.txImageView,
                MainApplication.getRoundOffOptions());
        }else if(type==2){
            if (convertView == null) {
                viewHoler3 = new ViewHoler2();
                convertView = LayoutInflater.from(activity).inflate(R.layout.xx_qz_lt_item3, null);// 这个过程相当耗时间
                viewHoler3.timeTextView=(TextView) convertView.findViewById(R.id.time_tv);
                viewHoler3.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
                viewHoler3.nrImageView=(ImageView) convertView.findViewById(R.id.nr_iv);
                convertView.setTag(viewHoler3);
            } else {
                viewHoler3 = (ViewHoler2) convertView.getTag();
            }
            viewHoler3.timeTextView.setText(PaseJson.getMapMsg(map, "send_time"));
//            viewHoler2.nrTextView.setText(PaseJson.getMapMsg(map, "send_content"));
            imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "sender_icon_url"), viewHoler3.txImageView,
                MainApplication.getRoundOffOptions());
            L.v(PaseJson.getMapMsg(map, "send_content")+"ad 3");
            imageLoader.displayImage(PaseJson.getMapMsg(map, "send_content"), viewHoler3.nrImageView,
                MainApplication.getRoundOffOptions());
        }else if(type==3){
            if (convertView == null) {
                viewHoler4= new ViewHoler2();
                convertView = LayoutInflater.from(activity).inflate(R.layout.xx_qz_lt_item4, null);// 这个过程相当耗时间
                viewHoler4.timeTextView=(TextView) convertView.findViewById(R.id.time_tv);
                viewHoler4.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
                viewHoler4.nrImageView=(ImageView) convertView.findViewById(R.id.nr_iv);
                convertView.setTag(viewHoler4);
            } else {
                viewHoler4 = (ViewHoler2) convertView.getTag();
            }
            viewHoler4.timeTextView.setText(PaseJson.getMapMsg(map, "send_time"));
            imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "sender_icon_url"), viewHoler4.txImageView,
                MainApplication.getRoundOffOptions());
            imageLoader.displayImage(PaseJson.getMapMsg(map, "send_content"), viewHoler4.nrImageView,
                MainApplication.getRoundOffOptions());
        }
        

        return convertView;
    }

    class ViewHoler {
        TextView timeTextView;//时间
        ImageView txImageView; //头像
        TextView  nrTextView;  //内容
      
    }
    class ViewHoler2 {
        TextView timeTextView;//时间
        ImageView txImageView; //头像
        ImageView  nrImageView;  //发送的图片
      
    }

}
