package com.ybd.yl.dialog;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.ybd.common.SlidePageAdapter;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 用户估值
 * @author cyf
 * @version $Id: SelectPhoto.java, v 0.1 2015-11-19 上午10:09:03 cyf Exp $
 */
public class SelectGzActivity extends BaseActivity {
    private ViewPager viewPager;

    @Override
    protected void initComponentBase() {
       setContentView(R.layout.select_gz);
       init();
    }

    /**
     * 初始化页面控件
     */
    private void init() {
      viewPager=(ViewPager) findViewById(R.id.gz_vp);
      List<View> viewPage = new ArrayList<View>();
      for(int i=0;i<11;i++){
          TextView textView=new TextView(activity);
          textView.setText(i+"万");
          viewPage.add(textView);
      }
      SlidePageAdapter myAdapter = new SlidePageAdapter(viewPage, this);
      viewPager.setAdapter(myAdapter);
      //下面的点图
      viewPager.setOnPageChangeListener(new OnPageChangeListener() {
          @Override
          public void onPageSelected(int arg0) {
              
          }
          @Override
          public void onPageScrolled(int arg0, float arg1, int arg2) {
              
          }
          @Override
          public void onPageScrollStateChanged(int arg0) {

          }
      });
    }
   
}
