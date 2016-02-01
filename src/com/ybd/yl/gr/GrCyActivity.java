/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.gr;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 个人-常用
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrCyActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout ndcxRelativeLayout; //年代查询
    private RelativeLayout jgcxRelativeLayout; //价格查询
    private RelativeLayout kdcxRelativeLayout; //快递查询
    private RelativeLayout xgnRelativeLayout;  //新功能查询

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.gr_cy);
        initPublicView("常用", R.drawable.login_fh, 0, null, null);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        ndcxRelativeLayout = (RelativeLayout) findViewById(R.id.ndcx_rl);
        ndcxRelativeLayout.setOnClickListener(this);
        jgcxRelativeLayout = (RelativeLayout) findViewById(R.id.jgcx_rl);
        jgcxRelativeLayout.setOnClickListener(this);
        kdcxRelativeLayout = (RelativeLayout) findViewById(R.id.kdcx_rl);
        kdcxRelativeLayout.setOnClickListener(this);
        xgnRelativeLayout = (RelativeLayout) findViewById(R.id.xgn_rl);
        xgnRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ndcx_rl:

                break;
            case R.id.jgcx_rl:

                break;
            case R.id.kdcx_rl:

                break;
            case R.id.xgn_rl:

                break;

            default:
                break;
        }
    }
}
