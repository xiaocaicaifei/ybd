package com.ybd.common;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class SlidePageAdapter extends PagerAdapter{

	private List<View> mViewList;  
//    private Context mContext;  
      
    public SlidePageAdapter(List<View> mViewList, Context mContext) {  
        super();  
        this.mViewList = mViewList;  
//        this.mContext = mContext;  
    }  
    @Override  
    public void destroyItem(View arg0, int arg1, Object arg2) {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "destroyItem");  
        ((ViewPager)arg0).removeView(mViewList.get(arg1));  
    }  

    @Override  
    public void finishUpdate(View arg0) {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "finishUpdate");  
    }  

    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
          
        return mViewList.size();  
    }  

    @Override  
    public Object instantiateItem(View arg0, int arg1) {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "instantiateItem");  
        ((ViewPager)arg0).addView(mViewList.get(arg1));  
        return mViewList.get(arg1);  
    }  

    @Override  
    public boolean isViewFromObject(View arg0, Object arg1) {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "isViewFromObject");  
        return arg0 == arg1;  
    }  

    @Override  
    public void restoreState(Parcelable arg0, ClassLoader arg1) {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "restoreState");  
    }  

    @Override  
    public Parcelable saveState() {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "saveState");  
        return null;  
    }  

    @Override  
    public void startUpdate(View arg0) {  
        // TODO Auto-generated method stub  
//        Log.d("CODE", "startUpdate");  
          
    }  
}
