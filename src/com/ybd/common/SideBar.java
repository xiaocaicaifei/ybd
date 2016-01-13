package com.ybd.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SideBar extends View {  
	 private char[] l;  
	    private SectionIndexer sectionIndexter = null;  
	    private ListView list;
//	    private TextView mDialogText;
	    private int m_nItemHeight = 32;
	    public SideBar(Context context) {  
	        super(context);  
	        
//	        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
//	        Display display = wm.getDefaultDisplay();
//	        int height = display.getHeight();
//	        m_nItemHeight=(height-130)/26;

	        init();  
	    }  
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    }
	    
	    @Override
	    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	        super.onLayout(changed, left, top, right, bottom);
	    }
	    
	    @SuppressWarnings("deprecation")
		public SideBar(Context context, AttributeSet attrs) {  
	        super(context, attrs); 
	        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	        Display display = wm.getDefaultDisplay();
	        int height = display.getHeight();
//	        m_nItemHeight=(height-150)/26;
//	        if(height==800){
//	        	m_nItemHeight=26;
//	        }else if(height==1280){
//	        	m_nItemHeight=43;
//	        }else if(height==1920){
//	        	m_nItemHeight=65;
//	        }
	        init();  
	    }  
	    private void init() {  
	        l = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',  
	                'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };  
	    }  
	    public SideBar(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle); 
	        init();  
	    }  
	    public void setListView(ListView _list) {  
	        list = _list;  
	        sectionIndexter = (SectionIndexer) _list.getAdapter(); 
//	        l=lableList.toString().toCharArray();
	    }  
	    public void setTextView(TextView mDialogText) {  
//	    	this.mDialogText = mDialogText;  
	    }  
	    public boolean onTouchEvent(MotionEvent event) {  
	        super.onTouchEvent(event);  
	        int i = (int) event.getY();  
	        int idx = i / m_nItemHeight;  
	        if (idx >= l.length) {  
	            idx = l.length - 1;  
	        } else if (idx < 0) {  
	            idx = 0;  
	        }  
//	        Log.v("test","我被点击了");
	        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {  
//	        	mDialogText.setVisibility(View.VISIBLE);
//	        	mDialogText.setText(""+l[idx]);
	            if (sectionIndexter == null) {  
	                sectionIndexter = (SectionIndexer) list.getAdapter();  
	            }  
	            try {
	                int position = sectionIndexter.getPositionForSection(l[idx]);  
	                if (position == -1) {  
	                    return true;  
	                }  
	                list.setSelection(position);  
                } catch (Exception e) {
                    e.printStackTrace();
                }
	           
	        }else{
//	        	mDialogText.setVisibility(View.INVISIBLE);
	        }  
	        return true;  
	    }  
	    protected void onDraw(Canvas canvas) {  
	        Paint paint = new Paint();  
//	        paint.setColor(Color.rgb(0, 35, 23));
	        paint.setColor(Color.BLUE);
	        paint.setTextSize(m_nItemHeight-6);
	        paint.setTextAlign(Paint.Align.CENTER);
	        
	        float widthCenter = getMeasuredWidth() / 2;  
	        for (int i = 0; i < l.length; i++) {  
	            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);  
	        }  
	        super.onDraw(canvas);  
	    }  
}
