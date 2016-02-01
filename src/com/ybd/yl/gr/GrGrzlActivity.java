/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.ybd.yl.gr;

import java.util.Map;

import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.MainApplication;
import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.ImageUtil;
import com.ybd.common.tools.KeyboardOperate;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.common.SelectPhotoActivity;

/**
 * 个人-个人资料
 * 
 * @author cyf
 * @version $Id: HomeFragment.java, v 0.1 2015年1月16日 上午11:16:50cyf  Exp $
 */
public class GrGrzlActivity extends BaseActivity implements OnClickListener {
    private LinearLayout        txLinearLayout;                         //头像点击层
    private LinearLayout        ncLinearLayout;                         //昵称点击层
    private LinearLayout        xbLinearLayout;                         //性别点击层
    private LinearLayout        qmLinearLayout;                         //签名点击层
    private LinearLayout        sjhLinearLayout;                        //手机号点击层
    private LinearLayout        yxLinearLayout;                         //邮箱点击层
    private ImageView           txImageView;                            //头像
    private TextView            ncTextView;                             //昵称
    private TextView            xbTextView;                             //性别
    private TextView            qmTextView;                             //签名
    private TextView            sjhTextView;                            //手机号
    private TextView            yxTextView;                             //邮箱
    private Map<String, Object> map;
    ImageLoader                 imageLoader = ImageLoader.getInstance();
    private String              tempPhotoUrl;                           //上次图片给出的临时图片路径

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.gr_grzl);
        initPublicView("个人资料", R.drawable.login_fh, 0, null, null);
        init();
        NetWork.submit(activity, jbzlINetWork);
    }

    /**
     * 初始化控件
     */
    private void init() {
        txLinearLayout = (LinearLayout) findViewById(R.id.tx_ll);
        txLinearLayout.setOnClickListener(this);
        txImageView = (ImageView) findViewById(R.id.tx_iv);
        ncLinearLayout = (LinearLayout) findViewById(R.id.nc_ll);
        ncLinearLayout.setOnClickListener(this);
        ncTextView = (TextView) findViewById(R.id.nc_tv);
        xbLinearLayout = (LinearLayout) findViewById(R.id.xb_ll);
        xbLinearLayout.setOnClickListener(this);
        xbTextView = (TextView) findViewById(R.id.xb_tv);
        qmLinearLayout = (LinearLayout) findViewById(R.id.qm_ll);
        qmLinearLayout.setOnClickListener(this);
        qmTextView = (TextView) findViewById(R.id.qm_tv);
        yxLinearLayout = (LinearLayout) findViewById(R.id.yx_ll);
        yxLinearLayout.setOnClickListener(this);
        yxTextView = (TextView) findViewById(R.id.yx_tv);
        sjhLinearLayout = (LinearLayout) findViewById(R.id.sjh_ll);
        sjhLinearLayout.setOnClickListener(this);
        sjhTextView = (TextView) findViewById(R.id.sjh_tv);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tx_ll://头像层
                intent.setClass(activity, SelectPhotoActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.nc_ll://昵称层
                intent.setClass(activity, GrGrzlNcActivity.class);
                intent.putExtra("nr",PaseJson.getMapMsg(map, "nick_name") );
                startActivityForResult(intent,1);
                break;
            case R.id.xb_ll://性别层

                break;
            case R.id.qm_ll://签名层
                intent.setClass(activity, GrGrzlQmActivity.class);
                intent.putExtra("nr",PaseJson.getMapMsg(map, "new_sign") );
                startActivityForResult(intent,3);
                break;
            case R.id.sjh_ll://手机号

                break;
            case R.id.yx_ll://邮箱
                intent.setClass(activity, GrGrzlYxActivity.class);
                intent.putExtra("nr",PaseJson.getMapMsg(map, "email") );
                startActivityForResult(intent,5);
                break;
            default:
                break;
        }
    }

    /**
     * 查询用户的基本资料
     */
    INetWork jbzlINetWork = new INetWork() {

                              @Override
                              public boolean validate() {
                                  return true;
                              }

                              @Override
                              public Data getSubmitData() throws Exception {
                                  Data data = new Data("auser/selectUserById.json");
                                  data.addData("user_id",
                                      PropertiesUtil.read(activity, PropertiesUtil.USERID));
                                  return data;
                              }

                              @SuppressWarnings("unchecked")
                              @Override
                              public void result(String result) throws Exception {
                                  map = (Map<String, Object>) ((Map<String, Object>) PaseJson
                                      .paseJsonToObject(result)).get("data");
                                  imageLoader.displayImage(
                                      C.IP + PaseJson.getMapMsg(map, "icon_url"), txImageView,
                                      MainApplication.getRoundOffOptions());
                                  L.v(C.IP + PaseJson.getMapMsg(map, "icon_url"));
                                  ncTextView.setText(PaseJson.getMapMsg(map, "nick_name"));
                                  xbTextView
                                      .setText(PaseJson.getMapMsg(map, "sex").equals("1") ? "男"
                                          : "女");
                                  qmTextView.setText(PaseJson.getMapMsg(map, "new_sign"));
                                  sjhTextView.setText(PaseJson.getMapMsg(map, "mobile"));
                                  yxTextView.setText(PaseJson.getMapMsg(map, "email"));
                              }

                          };

    /**
     * 修改基本资料
     */
    class XggrzlINetWork implements INetWork {
        private String iconUrl  = "";
        private String nickName = "";
        private String sex      = "";
        private String sign     = "";
        private String email    = "";

        public XggrzlINetWork(String iconUrl, String nickName, String sex, String sign, String email) {
            this.iconUrl = iconUrl;
            this.nickName = nickName;
            this.sex = sex;
            this.sign = sign;
            this.email = email;
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data data = new Data("register/updateUserDetail.json");
            data.addData("user_id", PropertiesUtil.read(activity, PropertiesUtil.USERID));
            data.addData("flag", "0");
            if (!iconUrl.equals("")) {
                data.addData("icon_url", iconUrl);
                L.v("iconUrl:::::::"+iconUrl);
            }
            if (!nickName.equals("")) {
                data.addData("nick_name", nickName);
                L.v("nick_name:::::::"+nickName);
            }
            if (!sex.equals("")) {
                data.addData("sex", sex);
                L.v("nick_name:::::::"+sex);
            }
            if (!sign.equals("")) {
                data.addData("new_sign", sign);
                L.v("nick_name:::::::"+sign);
            }
            if (!email.equals("")) {
                data.addData("email", email);
                L.v("nick_name:::::::"+email);
            }
            return data;
        }

        @Override
        public void result(String result) throws Exception {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) PaseJson.paseJsonToObject(result);
            if (map.get("code").equals("0")) {
                toastShow("修改成功！");
                NetWork.submit(activity, jbzlINetWork);
            } else {
                toastShow("修改失败！");
            }
        }

    };

    /**
     * 上传附件
     * @author Administrator
     *
     */
    private class UploadFile implements INetWork {
        private String picPath;

        public UploadFile(String picPath) {
            this.picPath = picPath;
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public Data getSubmitData() throws Exception {
            Data d = new Data("fs/uploadFile.json");
            d.addData("temp", "");
            d.addData("is_zip", "false");
            //                                  d.addData("path", picPath);
            d.addPath(picPath);
            return d;
        }

        @Override
        public void result(String result) throws Exception {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) PaseJson.paseJsonToObject(result);
            if (map.get("code").equals("0")) {
                String[] str = (String[]) map.get("fileList");
                tempPhotoUrl = str[0].split(",")[0];
                NetWork.submit(activity, new XggrzlINetWork(tempPhotoUrl, "", "", "", ""));//修改用户头像
            } else {
                toastShow("上传图片失败！");
            }
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == 0) {
                String path = arg2.getExtras().getString("path");
                NetWork.submit(activity, new UploadFile(path));
                //                txImageView
                //                    .setImageBitmap(ImageUtil.toRoundBitmap2(ImageUtil.getLoacalBitmap(path)));
                imageLoader.displayImage("file://" + path, txImageView,
                    MainApplication.getRoundOffOptions());
                NetWork.submit(activity, new UploadFile(path));
            }else if(arg0==1){
                String nr=arg2.getExtras().getString("nr");
                NetWork.submit(activity, new XggrzlINetWork("", nr, "", "", ""));
            }else if(arg0==3){
                String nr=arg2.getExtras().getString("nr");
                NetWork.submit(activity, new XggrzlINetWork("", "", "", nr, ""));
            }else if(arg0==5){
                String nr=arg2.getExtras().getString("nr");
                NetWork.submit(activity, new XggrzlINetWork("", "", "", "",nr));
            }
        }
    }
}
