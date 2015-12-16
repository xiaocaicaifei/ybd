/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ybd.yl.BaseFragment;
import com.ybd.yl.R;
import com.ybd.yl.gr.GrIndexFragment;

/**
 * 首页-个人
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrFragment extends BaseFragment implements HomeClickListener{
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gr_index, container, false);
        toFragmentContent(new GrIndexFragment(),R.id.gr_fragment,null);
        return v;
    }
    @Override
    public void onHomeclick(View v) {
        // TODO Auto-generated method stub
        
    }
    @Override
    protected void initComponentBase() {
    }

}
