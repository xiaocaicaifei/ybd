package com.ybd.yl.qz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 圈子-上传-上传图片
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzScSctpActivity extends BaseActivity implements OnClickListener {
    private GridView    tpGridView;
    private BaseAdapter tpAdapter;
    private List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_sc_sctp);
        initPublicView("上传图片", R.drawable.login_fh, 0, null, null);
        init();
    }

    /**
     * 初始化控件
     */
    @SuppressWarnings("unchecked")
    private void init() {
        tpGridView = (GridView) findViewById(R.id.tp_gv);
        tpAdapter=new QzScSctpAdapter(list,activity);
        tpGridView.setAdapter(tpAdapter);
        if(this.getIntent().hasExtra("path")){
            list.clear();
            list.addAll((List<Map<String, Object>>) this.getIntent().getExtras().getSerializable("path"));
            tpAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View arg0) {

    }
}
