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
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;

/**
 * 通讯录的适配器
 * 
 * @author cyf
 * @version $Id: GrIndexAdapter.java, v 0.1 2015-11-30 下午2:47:35 cyf Exp $
 */
public class XxTxlXdpyTxlhyAdapter extends BaseAdapter implements SectionIndexer {
    private List<Map<String, Object>> list        = new ArrayList<Map<String, Object>>();
    private XxTxlXdpyTxlhyActivity             activity;
    private ImageLoader               imageLoader = ImageLoader.getInstance();

    public XxTxlXdpyTxlhyAdapter(List<Map<String, Object>> list, XxTxlXdpyTxlhyActivity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.xx_txl_xdpy_txlhy_item, null);// 这个过程相当耗时间
            viewHoler.zmTextView=(TextView) convertView.findViewById(R.id.zm_tv);
            viewHoler.txImageView = (ImageView) convertView.findViewById(R.id.tx_iv);
            viewHoler.zmLayout=(LinearLayout) convertView.findViewById(R.id.zm_ll);
            viewHoler.nameTextView = (TextView) convertView.findViewById(R.id.name_tv);
            viewHoler.phoneTextView = (TextView) convertView.findViewById(R.id.phone_tv);
            viewHoler.selectImageView=(ImageView) convertView.findViewById(R.id.select_iv);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        String szm=PaseJson.getMapMsg(map, "first_letter");
        viewHoler.zmTextView.setText(szm);
        if(PaseJson.getMapMsg(map,"isSame").equals("1")){
            viewHoler.zmLayout.setVisibility(View.GONE);
        }else{
            viewHoler.zmLayout.setVisibility(View.VISIBLE);
        }
        viewHoler.nameTextView.setText(PaseJson.getMapMsg(map, "name"));
        viewHoler.phoneTextView.setText(PaseJson.getMapMsg(map, "phone"));
//        imageLoader.displayImage(C.IP+PaseJson.getMapMsg(map, "icon_url"), viewHoler.txImageView,MainApplication.getRoundOffOptions());
        viewHoler.txImageView.setBackgroundResource(R.drawable.tp_jzsb);
        if(PaseJson.getMapMsg(map, "isSelect").equals("")||PaseJson.getMapMsg(map, "isSelect").equals("0")){
            viewHoler.selectImageView.setBackgroundResource(R.drawable.xx_txl_xdpy_txlhy_unsel);
        }else{
            viewHoler.selectImageView.setBackgroundResource(R.drawable.xx_txl_xdpy_txlhy_sel);
        }
        return convertView;
    }

    class ViewHoler {
        TextView  zmTextView;  //字母
        ImageView txImageView; //头像
        TextView  nameTextView;  //名字
        TextView  phoneTextView;//电话号码
        LinearLayout zmLayout;//字母的层
        ImageView selectImageView;//是否选中
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据哪个字段进行排序
     * @see android.widget.SectionIndexer#getPositionForSection(int)
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < list.size(); i++) {  
            Map<String, Object> map=list.get(i);
            String szm=PaseJson.getMapMsg(map, "first_letter");
            char firstChar = szm.toUpperCase().charAt(0); 
            if (firstChar == sectionIndex) {  
                return i;  
            }  
        } 
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

}
