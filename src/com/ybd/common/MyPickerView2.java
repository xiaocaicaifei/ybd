package com.ybd.common;

import java.util.List;

import android.content.Context;
import android.drm.DrmStore.RightsStatus;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyPickerView2 extends View {
    private List<String> mDataList;      //展示所有要显示的数据

    private Paint        lowTextPaint;   //显示下方标注的字体

    private Paint        leftLinePaint;  //显示选择的线

    private Paint        rightLinePaint; //显示未选择的显示线

    private Paint        circlePaint;    //圆

    private Paint        circleTextPaint; //圆上的数字

    private int          mViewHeight;
    private int          mViewWidth;

    private int          mCurrentSelect;

    private float        lastX;           //圆圈移动的最后x轴的坐标

    public void setData(List<String> data) {
        this.mDataList = data;
        //        invalidate();

        Log.v("dddd", "bbbbb");
    }

    public MyPickerView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyPickerView2(Context context) {
        super(context);

        init();
    }

    /**
     * 初始化控件的基本信息
     */
    private void init() {
        lowTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lowTextPaint.setTextSize(40f);
        lowTextPaint.setColor(Color.BLACK);
        leftLinePaint = new Paint();
        leftLinePaint.setColor(Color.RED);
        leftLinePaint.setStrokeWidth(20f);
        rightLinePaint = new Paint();
        rightLinePaint.setColor(Color.GRAY);
        rightLinePaint.setStrokeWidth(20f);
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circleTextPaint=new Paint();
        circleTextPaint.setColor(Color.WHITE);
        circleTextPaint.setTextSize(40f);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //        Log.v("dddd", "3333" + ":" + widthMeasureSpec + ":" + heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        lastX = mViewWidth / 2-10;
        mCurrentSelect = (int) (lastX / (mViewWidth / 11));
        

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //        return super.onTouchEvent(event);
        Log.v("dddd", event.getX() + "::::");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = event.getX();
                mCurrentSelect = (int) (lastX / ((mViewWidth+20) / 11));
                Log.v("dddd", mCurrentSelect + "::::::");
                break;
            case MotionEvent.ACTION_UP:
                lastX = event.getX();
                break;

            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(10, 30, lastX, 30, leftLinePaint);//绘制选中的线
        canvas.drawLine(lastX, 30, mViewWidth - 10, 30, rightLinePaint);//绘制未选中的线
        Log.v("dddd", "cccc");

        canvas.drawCircle(lastX, 30, 30f, circlePaint);
        canvas.drawText(mCurrentSelect+"", lastX-8, 40, circleTextPaint);
        for (int i = 0; i < mDataList.size(); i++) {
            //            Log.v("dddd", "aaaa"+i * 65);
            canvas.drawText(mDataList.get(i), (mViewWidth) / 11 * i + 10, 100, lowTextPaint);
        }
    }

    public interface onSelectListener {
        void onSelect(String text);
    }
}
