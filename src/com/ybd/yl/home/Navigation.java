/**
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.ybd.yl.home;

import com.ybd.yl.R;
import com.ybd.yl.gr.GrIndexActivity;


/**
 * 导航
 * 
 * @author cyf
 * @version $Id: Navigation.java, v 0.1 2014年6月13日 下午6:54:25 cyf Exp $
 */
public enum Navigation {
    //消息
    XX(0, "消息", R.drawable.home_xx, R.drawable.home_xx_hover, XxFragment.class),
    //圈子
    QZ(1, "圈子", R.drawable.home_qz, R.drawable.home_qz_hover, QzFragment.class),
    //艺论
    YL(2, "艺论", R.drawable.home_yl, R.drawable.home_yl_hover, YlFragment.class),
    //拍卖
    PM(3, "拍卖", R.drawable.home_pm, R.drawable.home_pm_hover, QzFragment.class),
    //个人
    GR(4, "个人", R.drawable.home_gr, R.drawable.home_gr_hover, GrIndexActivity.class), ;

    /**序号，从0开始*/
    private int      index;
    /** 名称 */
    private String   name;
    /** 图片 */
    private int      photo;
    /** 选中图片 */
    private int      photoSelected;
    /** 对应的处理类 */
    private Class<?> clas;

    private Navigation(int index, String name, int photo, int photoSelected, Class<?> clas) {
        this.index = index;
        this.name = name;
        this.photo = photo;
        this.photoSelected = photoSelected;
        this.clas = clas;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getPhotoSelected() {
        return photoSelected;
    }

    public void setPhotoSelected(int photoSelected) {
        this.photoSelected = photoSelected;
    }

    public Class<?> getClas() {
        return clas;
    }

    public void setClas(Class<?> clas) {
        this.clas = clas;
    }
    /**
     * 根据序号获取枚举
     * 
     * @param index 序号
     * @return
     */
    public static Navigation getEnum(int index) {
        for (Navigation ele : values()) {
            if (ele.getIndex() == index) {
                return ele;
            }
        }
        return null;
    }

}
