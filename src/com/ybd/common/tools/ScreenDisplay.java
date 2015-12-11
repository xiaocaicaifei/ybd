package com.ybd.common.tools;

import com.ybd.common.L;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 获取屏幕的宽度和高度
 * 
 * @author cyf
 * @version $Id: ScreenDisplay.java, v 0.1 2015-12-10 下午2:42:43 cyf Exp $
 */
public class ScreenDisplay {

    /**
     * 获取屏幕的高度(去掉状态栏的高度)
     * @param activity
     * @return
     */
    public static int getScreenHeight2(Activity activity){
        Rect outRect = new Rect();  
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);  
        return outRect.height()-outRect.top;
    }
    /**
     * 获取屏幕的高度
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenHeight = dm.heightPixels;
        L.v(mScreenHeight+":::::");
        return mScreenHeight;
    }

    /**
     * 获取屏幕的宽度
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        return mScreenWidth;
    }

    /**
     * 根据Dip获得Px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据Px获得DiP
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * 后台动态设置控件的高度和宽度
     * @param view 所有的控件
     * @param width 设置控件的高度 ，如果不设置宽度传入 0 单位px
     * @param height 设置控件的宽度，如果不设置高度传入 0 单位 px
     */
    public static void setViewWidthAndHeight(View view,int width,int height){
      //设置ListView的高度
        LayoutParams lp=view.getLayoutParams();
        if(width!=0){
            lp.width=width;
        }
        if(height!=0){
            lp.height=height;
        }
        view.setLayoutParams(lp);
    }
    
        public static void setListViewHeightBasedOnChildren(ListView listView) {  
            ListAdapter listAdapter = listView.getAdapter();   
            if (listAdapter == null) {  
                return;  
            }  
            int totalHeight = 0;  
            for (int i = 0; i < listAdapter.getCount(); i++) {  
                View listItem = listAdapter.getView(i, null, listView);  
                listItem.measure(0, 0);  
                totalHeight += listItem.getMeasuredHeight();  
            }  
            ViewGroup.LayoutParams params = listView.getLayoutParams();  
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
            listView.setLayoutParams(params);  
        }  
}
