package com.ybd.yl.xx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
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
        Map<String, Object> map = list.get(position);
        ViewHoler viewHoler = null;
        XxIndexSlideView slideView = (XxIndexSlideView) convertView;
        if (slideView == null) {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.xx_index_item, null);

            slideView = new XxIndexSlideView(activity);
            slideView.setContentView(itemView);

            viewHoler = new ViewHoler();
            viewHoler.delRelativeLayout=(RelativeLayout) slideView.findViewById(R.id.holder_rl);
            viewHoler.delRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            slideView.setOnSlideListener(activity);
            slideView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) slideView.getTag();
        }
        map.put("slideView", slideView);
        slideView.shrink();
        return slideView;
    }

    class ViewHoler {
        ImageView xcImageView;
        RelativeLayout delRelativeLayout;
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
