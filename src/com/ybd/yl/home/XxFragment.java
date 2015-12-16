/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.xx.XxIndexFragment;

/**
 * 首页-圈子
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class XxFragment extends BaseFragment implements HomeClickListener, OnClickListener {
   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_xx, container, false);
        toFragmentContent(new XxIndexFragment(),R.id.xx_fragment,null);
        return v;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onHomeclick(View v) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void initComponentBase() {
    }

}
