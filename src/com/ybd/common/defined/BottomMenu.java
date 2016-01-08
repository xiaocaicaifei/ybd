package com.ybd.common.defined;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybd.yl.R;
import com.ybd.yl.gr.GrIndexActivity;
import com.ybd.yl.pm.PmIndexActivity;
import com.ybd.yl.qz.QzIndexActivity;
import com.ybd.yl.xx.XxIndexActivity;
import com.ybd.yl.yl.YlIndexActivity;

/**
 * 底部菜单栏
 * 
 * @author cyf
 * @version $Id: BottomMenu.java, v 0.1 2015-12-24 下午4:42:20 cyf Exp $
 */
public class BottomMenu extends LinearLayout implements OnClickListener {

    private ImageView    xxImageView;    //消息
    private ImageView    qzImageView;    //圈子
    private ImageView    ylImageView;    //艺论
    private ImageView    pmImageView;    //拍卖
    private ImageView    grImageView;    //个人
    private TextView     xxTextView;     //消息
    private TextView     qzTextView;     //圈子
    private TextView     ylTextView;     //艺论
    private TextView     pmTextView;     //拍卖
    private TextView     grTextView;     //个人
    private LinearLayout xxLinearLayout;
    private LinearLayout qzLinearLayout;
    private LinearLayout ylLinearLayout;
    private LinearLayout pmLinearLayout;
    private LinearLayout grLinearLayout;
    private String selectMenu;//用户选择的
    Context context;

    public BottomMenu(Context context) {
        super(context);
        this.context=context;
//        init();
    }

    public BottomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(attrs,context);
    }

    /**
     * 初始化控件
     */
    private void init(AttributeSet attrs,Context context) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bottom_menu,this);
        xxImageView = (ImageView) findViewById(R.id.xx_iv);
        xxTextView = (TextView) findViewById(R.id.xx_tv);
        xxLinearLayout = (LinearLayout) findViewById(R.id.xx_ll);
        xxLinearLayout.setOnClickListener(this);

        qzImageView = (ImageView) findViewById(R.id.qz_iv);
        qzTextView = (TextView) findViewById(R.id.qz_tv);
        qzLinearLayout = (LinearLayout) findViewById(R.id.qz_ll);
        qzLinearLayout.setOnClickListener(this);

        pmImageView = (ImageView) findViewById(R.id.pm_iv);
        pmTextView = (TextView) findViewById(R.id.pm_tv);
        pmLinearLayout = (LinearLayout) findViewById(R.id.pm_ll);
        pmLinearLayout.setOnClickListener(this);

        ylImageView = (ImageView) findViewById(R.id.yl_iv);
        ylTextView = (TextView) findViewById(R.id.yl_tv);
        ylLinearLayout = (LinearLayout) findViewById(R.id.yl_ll);
        ylLinearLayout.setOnClickListener(this);

        grImageView = (ImageView) findViewById(R.id.gr_iv);
        grTextView = (TextView) findViewById(R.id.gr_tv);
        grLinearLayout = (LinearLayout) findViewById(R.id.gr_ll);
        grLinearLayout.setOnClickListener(this);
        
//        int selectId=attrs.getAttributeResourceValue(null, "Select", 0);
        selectMenu=attrs.getAttributeValue(null, "Select");
//        L.v(selectMenu);
//        selectMenu=context.getResources().getText(selectId).toString();
        if(selectMenu.equals("xx")){
            xxImageView.setBackgroundResource(R.drawable.home_xx_hover);
            xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
            qzImageView.setBackgroundResource(R.drawable.home_qz);
            qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            ylImageView.setBackgroundResource(R.drawable.home_yl);
            ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            pmImageView.setBackgroundResource(R.drawable.home_pm);
            pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            grImageView.setBackgroundResource(R.drawable.home_gr);
            grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
        }else if(selectMenu.equals("qz")){
            xxImageView.setBackgroundResource(R.drawable.home_xx);
            xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            qzImageView.setBackgroundResource(R.drawable.home_qz_hover);
            qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
            ylImageView.setBackgroundResource(R.drawable.home_yl);
            ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            pmImageView.setBackgroundResource(R.drawable.home_pm);
            pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            grImageView.setBackgroundResource(R.drawable.home_gr);
            grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
        }else if(selectMenu.equals("yl")){
            xxImageView.setBackgroundResource(R.drawable.home_xx);
            xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            qzImageView.setBackgroundResource(R.drawable.home_qz);
            qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            ylImageView.setBackgroundResource(R.drawable.home_yl_hover);
            ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
            pmImageView.setBackgroundResource(R.drawable.home_pm);
            pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            grImageView.setBackgroundResource(R.drawable.home_gr);
            grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
        }else if(selectMenu.equals("pm")){
            xxImageView.setBackgroundResource(R.drawable.home_xx);
            xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            qzImageView.setBackgroundResource(R.drawable.home_qz);
            qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            ylImageView.setBackgroundResource(R.drawable.home_yl);
            ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            pmImageView.setBackgroundResource(R.drawable.home_pm_hover);
            pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
            grImageView.setBackgroundResource(R.drawable.home_gr);
            grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
        }else if(selectMenu.equals("gr")){
            xxImageView.setBackgroundResource(R.drawable.home_xx);
            xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            qzImageView.setBackgroundResource(R.drawable.home_qz);
            qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            ylImageView.setBackgroundResource(R.drawable.home_yl);
            ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            pmImageView.setBackgroundResource(R.drawable.home_pm);
            pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
            grImageView.setBackgroundResource(R.drawable.home_gr_hover);
            grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        switch (v.getId()) {
            case R.id.xx_ll:
                xxImageView.setBackgroundResource(R.drawable.home_xx_hover);
                xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
                qzImageView.setBackgroundResource(R.drawable.home_qz);
                qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                ylImageView.setBackgroundResource(R.drawable.home_yl);
                ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                pmImageView.setBackgroundResource(R.drawable.home_pm);
                pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                grImageView.setBackgroundResource(R.drawable.home_gr);
                grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                intent.setClass(context, XxIndexActivity.class);
                break;
            case R.id.qz_ll:
                xxImageView.setBackgroundResource(R.drawable.home_xx);
                xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                qzImageView.setBackgroundResource(R.drawable.home_qz_hover);
                qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
                ylImageView.setBackgroundResource(R.drawable.home_yl);
                ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                pmImageView.setBackgroundResource(R.drawable.home_pm);
                pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                grImageView.setBackgroundResource(R.drawable.home_gr);
                grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                intent.setClass(context, QzIndexActivity.class);
                break;
            case R.id.yl_ll:
                xxImageView.setBackgroundResource(R.drawable.home_xx);
                xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                qzImageView.setBackgroundResource(R.drawable.home_qz);
                qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                ylImageView.setBackgroundResource(R.drawable.home_yl_hover);
                ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
                pmImageView.setBackgroundResource(R.drawable.home_pm);
                pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                grImageView.setBackgroundResource(R.drawable.home_gr);
                grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                intent.setClass(context, YlIndexActivity.class);
                break;
            case R.id.pm_ll:
                xxImageView.setBackgroundResource(R.drawable.home_xx);
                xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                qzImageView.setBackgroundResource(R.drawable.home_qz);
                qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                ylImageView.setBackgroundResource(R.drawable.home_yl);
                ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                pmImageView.setBackgroundResource(R.drawable.home_pm_hover);
                pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
                grImageView.setBackgroundResource(R.drawable.home_gr);
                grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                intent.setClass(context, PmIndexActivity.class);
                break;
            case R.id.gr_ll:
                xxImageView.setBackgroundResource(R.drawable.home_xx);
                xxTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                qzImageView.setBackgroundResource(R.drawable.home_qz);
                qzTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                ylImageView.setBackgroundResource(R.drawable.home_yl);
                ylTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                pmImageView.setBackgroundResource(R.drawable.home_pm);
                pmTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_unselect));
                grImageView.setBackgroundResource(R.drawable.home_gr_hover);
                grTextView.setTextColor(this.getResources().getColor(R.color.bottom_menu_text_select));
                intent.setClass(context, GrIndexActivity.class);
                break;

            default:
                break;
        }
        context.startActivity(intent);
    }
}
