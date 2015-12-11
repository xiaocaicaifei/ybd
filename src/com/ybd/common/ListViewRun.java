package com.ybd.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 扩展了{@link ListView}，可以解决1个布局中存在多个{@link ListView}时，出现多个滚动条的问题<br>
 * 
 * 
 * @author cyf
 * @version $Id: ListViewRun.java, v 0.1 2013-9-2 下午5:05:42 wum Exp $
 */
public class ListViewRun extends ListView {

    public ListViewRun(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ListViewRun(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewRun(Context context) {
        super(context);
    }

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);      
        super.onMeasure(widthMeasureSpec, expandSpec);  
    }  

}
