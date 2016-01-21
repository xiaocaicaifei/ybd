package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 消息列表的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class XxIndexAdapter extends BaseAdapter {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private XxIndexActivity                  activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public XxIndexAdapter(List<Map<String, Object>> list, XxIndexActivity activity) {
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
        XxIndexSlideView slideView = (XxIndexSlideView) convertView;
        if (slideView == null) {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.xx_index_item, null);

            slideView = new XxIndexSlideView(activity);
            slideView.setContentView(itemView);

            viewHoler = new ViewHoler();
            viewHoler.delRelativeLayout=(RelativeLayout) slideView.findViewById(R.id.holder_rl);
            viewHoler.txImageView=(ImageView) slideView.findViewById(R.id.tx_iv);
            viewHoler.titleTextView=(TextView) slideView.findViewById(R.id.title_tv);
            viewHoler.nrTextView=(TextView) slideView.findViewById(R.id.nr_tv);
            viewHoler.timeTextView=(TextView) slideView.findViewById(R.id.time_tv);
            viewHoler.numButton=(Button) slideView.findViewById(R.id.num_b);
            viewHoler.delRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                    activity.delXxList(PaseJson.getMapMsg(map, "sender_id"));
                }
            });
            slideView.setOnSlideListener(activity);
            slideView.setTag(viewHoler);
            
        } else {
            viewHoler = (ViewHoler) slideView.getTag();
        }
        map.put("slideView", slideView);
        slideView.shrink();
        imageLoader.displayImage(PaseJson.getMapMsg(map, "sender_icon_url"),viewHoler.txImageView,MainApplication.getRoundOffOptions());
        viewHoler.titleTextView.setText(PaseJson.getMapMsg(map, "sender_name"));
        viewHoler.nrTextView.setText(PaseJson.getMapMsg(map, "send_content"));
        viewHoler.timeTextView.setText(PaseJson.getMapMsg(map, "send_time"));
        if(PaseJson.getMapMsg(map, "unread_num").equals("0")){
            viewHoler.numButton.setVisibility(View.GONE);
        }else{
            viewHoler.numButton.setVisibility(View.VISIBLE);
            viewHoler.numButton.setText(PaseJson.getMapMsg(map, "unread_num"));
        }
        return slideView;
    }

    class ViewHoler {
        ImageView txImageView;
        RelativeLayout delRelativeLayout;
        TextView titleTextView;//标题头
        TextView nrTextView;//内容
        TextView timeTextView;//时间
//        TextView numTextView;//消息的数量
        Button numButton;//消息数量
    }
    
    /**
     * 滑动删除穿的对象
     * 
     * @author cyf
     * @version $Id: XxIndexAdapter.java, v 0.1 2016-1-12 上午10:41:18 cyf Exp $
     */
    public class MessageItem {
        public XxIndexSlideView slideView;
    }
    
}
