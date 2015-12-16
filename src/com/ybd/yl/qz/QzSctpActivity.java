package com.ybd.yl.qz;


import android.view.View;
import android.view.View.OnClickListener;

import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 圈子-上传图片
 * 
 * @author cyf
 * @version $Id: YlSjtjActivity.java, v 0.1 2015-12-1 上午10:45:51 cyf Exp $
 */
public class QzSctpActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.qz_sctp);
        initPublicView("上传图片",R.drawable.login_fh, 0,null, null);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {

    }

	@Override
	public void onClick(View arg0) {
		
		
	}
}
