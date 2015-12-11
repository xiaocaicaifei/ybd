package com.ybd.common.defined;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.ybd.yl.R;

/**
 * 自定义柱状图
 * @author cyf
 * @version $Id: HistogramView.java, v 0.1 2015-12-1 上午10:36:48 cyf Exp $
 */
public class HistogramView extends View{

    private Paint xLinePaint;// 坐标轴 轴线 画笔：
    private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
    private Paint titlePaint;// 绘制文本的画笔
    private Paint paint;// 矩形画笔 柱状图的样式信息
    private Paint paint2;//柱状图上方的字体
    private int[] progress = { 2000, 5000, 6000, 8000, 500, 6000, 9000 };// 7条，显示各个柱状的数据
    private int[] aniProgress;// 实现动画的值(柱状图的值)
    private final int TRUE = 1;// 在柱状图上显示数字
    private int[] text;// //显示在柱状图上面的文字
    private Bitmap bitmap;
    // 坐标轴左侧的数标
    private String[] ySteps;
    // 坐标轴底部的星期数
    private String[] xWeeks;
    private int flag;// 是否使用动画
    int x, y, preX, preY;
 
    private HistogramAnimation ani;
    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public HistogramView(Context context) {
        super(context);
        init();
    }
    
    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    

    public void start(int flag) {
        this.flag = flag;
        this.startAnimation(ani);
    }
    
    /**
     * 初始化页面
     */
    private void init(){
        ySteps = new String[] { "60", "50", "40", "30", "20","10","0"};
        xWeeks = new String[] { "1 ↓", "1-2", "2-3", "3-4", "4-5", "5-6", "6-7","7-8","8-9","9-10","10 ↑" };
        text = new int[] {45, 60, 43, 15, 50 ,50,60,20,33,10,56 };//显示在柱状图上面的文字
        aniProgress = new int[] { 45, 60, 43, 15, 50 ,50,60,20,33,10,56};
        ani = new HistogramAnimation();
        ani.setDuration(2000);
        xLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        paint = new Paint();
        paint2=new Paint();
 
        // 给画笔设置颜色
        xLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);
        
        paint2.setColor(Color.BLACK);
        paint2.setTextAlign(Align.RIGHT);
        paint2.setTextSize(sp2px(9));
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);
        // 加载画图
        bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.column);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        
        int width = getWidth();
        int height = getHeight() - dp2px(50);
        // 绘制底部的线条(X轴线)
        canvas.drawLine(dp2px(27), height + dp2px(0), width - dp2px(15), height
                + dp2px(0), xLinePaint);
     // 绘制左边的线条(Y轴线)
        canvas.drawLine(dp2px(27), height - dp2px(0), dp2px(27), dp2px(0), xLinePaint);
        
        int leftHeight = height - dp2px(5);// 左侧外周的 需要划分的高度：
 
        int hPerHeight = leftHeight / (ySteps.length-1);// 分成y轴坐标个数的份数-1
 
//        hLinePaint.setTextAlign(Align.CENTER);
        // 设置四条虚线
//        for (int i = 0; i < 4; i++) {
//            canvas.drawLine(dp2px(30), dp2px(10) + i * hPerHeight, width
//                    - dp2px(30), dp2px(10) + i * hPerHeight, hLinePaint);
//        }
 
        // 绘制 Y 周坐标
        titlePaint.setTextAlign(Align.RIGHT);
        titlePaint.setTextSize(sp2px(12));
        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);
        // 设置左部的数字
        for (int i = 0; i < ySteps.length; i++) {
            canvas.drawText(ySteps[i], dp2px(25), dp2px(12) + i * hPerHeight,
                    titlePaint);
        }
 
        // 绘制X轴坐标
        int xAxisLength = width - dp2px(30);
        int columCount = xWeeks.length + 1;
        int step = xAxisLength / columCount;
 
        // 设置底部的数字
        for (int i = 0; i < columCount - 1; i++) {
            // text, baseX, baseY, textPaint
            canvas.drawText(xWeeks[i], dp2px(30) + step * (i + 1), height
                    + dp2px(20), titlePaint);
        }
 
        // 绘制矩形
        if (aniProgress != null && aniProgress.length > 0) {
            for (int i = 0; i < aniProgress.length; i++) {// 循环遍历将7条柱状图形画出来
                int value = aniProgress[i];
                paint.setAntiAlias(true);// 抗锯齿效果
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(sp2px(15));// 字体大小
                paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
                Rect rect = new Rect();// 柱状图的形状
 
                rect.left = dp2px(17)+step * (i + 1);
                rect.right = dp2px(25) + step * (i + 1);
                int rh = (int) (leftHeight - hPerHeight * (value / 10.0));
                rect.top = rh + dp2px(10);
                rect.bottom = height;
 
                canvas.drawBitmap(bitmap, null, rect, paint);
                // 是否显示柱状图上方的数字
//                if (this.text[i] == TRUE) {
                    canvas.drawText(value + "", dp2px(40) + step * (i + 1)
                            - dp2px(15), rh + dp2px(8), paint2);
//                }
 
            }
        }
    }
    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }
 
    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }
    
    /**
     * 集成animation的一个动画类
     * 
     * @author 
     */
    private class HistogramAnimation extends Animation {
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f && flag == 2) {
                for (int i = 0; i < aniProgress.length; i++) {
                    aniProgress[i] = (int) (progress[i] * interpolatedTime);
                }
            } else {
                for (int i = 0; i < aniProgress.length; i++) {
                    aniProgress[i] = progress[i];
                }
            }
            invalidate();
        }
    }
    /**
     * 设置点击事件，是否显示数字
     */
    public boolean onTouchEvent(MotionEvent event) {
//        int step = (getWidth() - dp2px(30)) / 8;
//        int x = (int) event.getX();
//        for (int i = 0; i < 7; i++) {
//            if (x > (dp2px(15) + step * (i + 1) - dp2px(15))
//                    && x < (dp2px(15) + step * (i + 1) + dp2px(15))) {
//                text[i] = 1;
//                for (int j = 0; j < 7; j++) {
//                    if (i != j) {
//                        text[j] = 0;
//                    }
//                }
//                if (Looper.getMainLooper() == Looper.myLooper()) {
//                    invalidate();
//                } else {
//                    postInvalidate();
//                }
//            }
//        }
        return super.onTouchEvent(event);
    }
}
