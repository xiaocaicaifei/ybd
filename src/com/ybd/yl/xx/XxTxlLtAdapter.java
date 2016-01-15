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
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 消息-通讯录-聊天的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class XxTxlLtAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private XxTxlLtActivity         activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public XxTxlLtAdapter(List<Map<String, Object>> list, XxTxlLtActivity activity) {
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
        return super.getViewTypeCount();
    }
    @Override
    public int getItemViewType(int position) {
        Map<String, Object> map=list.get(position);
        if(PaseJson.getMapMsg(map, "type").equals("1")){
            return 1;//别人
        }else{
            return 0;//自己
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        int type=getItemViewType(position);
        if(type==0){
            if (convertView == null) {
                viewHoler = new ViewHoler();
                convertView = LayoutInflater.from(activity).inflate(R.layout.xx_txl_lt_item, null);// 这个过程相当耗时间
                viewHoler.timeTextView=(TextView) convertView.findViewById(R.id.time_tv);
                viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
                viewHoler.nrTextView=(TextView) convertView.findViewById(R.id.nr_tv);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }
            viewHoler.timeTextView.setText(PaseJson.getMapMsg(map, "time"));
            viewHoler.nrTextView.setText(PaseJson.getMapMsg(map, "nr"));
            
        }else if(type==1){
            if (convertView == null) {
                viewHoler = new ViewHoler();
                convertView = LayoutInflater.from(activity).inflate(R.layout.xx_txl_lt_item2, null);// 这个过程相当耗时间
                viewHoler.timeTextView=(TextView) convertView.findViewById(R.id.time_tv);
                viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
                viewHoler.nrTextView=(TextView) convertView.findViewById(R.id.nr_tv);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }
            viewHoler.timeTextView.setText(PaseJson.getMapMsg(map, "time"));
            viewHoler.nrTextView.setText(PaseJson.getMapMsg(map, "nr"));
        }
        imageLoader.displayImage(C.IP + PaseJson.getMapMsg(map, "icon_url"), viewHoler.txImageView,
            MainApplication.getRoundOffOptions());

        return convertView;
    }

    class ViewHoler {
        TextView timeTextView;//时间
        ImageView txImageView; //头像
        TextView  nrTextView;  //内容
      
    }

}
