package com.ybd.yl.login;

import java.util.Map;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ybd.common.PropertiesUtil;
import com.ybd.common.net.Data;
import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;

/**
 * 用户登录-填写验证码
 * @author cyf
 * @version $Id: LoginActivity.java, v 0.1 2015-10-27 下午5:30:45 cyf Exp $
 */
public class LoginTxyzmActivity extends BaseActivity implements OnClickListener {

    private Button xybButton; //下一步按钮
    private Button   fsyzmButton; //发送验证码
    private EditText yzmEditText; //验证码
    private int      i   = 60;    //倒计时
    private String   yzm = "";    //活的到的用户的验证码

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.login_txyzm);
        initPublicView("填写验证码");
        initView();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        xybButton = (Button) findViewById(R.id.login_xyb_b);
        xybButton.setOnClickListener(this);
//        xybButton.setClickable(false);
        yzmEditText = (EditText) findViewById(R.id.regist_yzm_et);
        fsyzmButton = (Button) findViewById(R.id.regist_fsyzm_b);
        fsyzmButton.setOnClickListener(this);
        fsyzmButton.setClickable(true);
        fsyzmButton.setText("点击获取");
        yzmEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    xybButton.setClickable(true);
                    xybButton.setBackgroundResource(R.drawable.login_share_button);
                    xybButton.setTextColor(activity.getResources().getColor(R.color.white));
                } else {
                    xybButton.setClickable(false);
                    xybButton.setBackgroundResource(R.drawable.login_share_button2);
                    xybButton.setTextColor(activity.getResources()
                        .getColor(R.color.regist_yzm_color));
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {

        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.login_xyb_b:
                if(!yzmEditText.getText().toString().equals(yzm)){
                    toastShow("验证码填写错误！");
                    return;
                }else{
                    intent.putExtra("yzm",yzm);
                    intent.putExtra("sjh", activity.getIntent().getExtras().getString("sjh"));
                    intent.setClass(activity, LoginCzmmActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.regist_fsyzm_b:
                NetWork.submit(activity, hqyzm);
                fsyzmButton.setClickable(false);
                fsyzmButton.setBackgroundResource(R.drawable.login_share_yzm2);
                fsyzmButton
                    .setTextColor(activity.getResources().getColor(R.color.regist_yzm_color));
                timer();
                break;
        }
    }

    /**
     * 获取验证码的匿名内部类
     */
    INetWork hqyzm = new INetWork() {

                       @Override
                       public boolean validate() {
                           return true;
                       }

                       @Override
                       public Data getSubmitData() throws Exception {
                           Data data = new Data("tools/sendSMS.json");
                           data.addData("mobile", activity.getIntent().getExtras().getString("sjh"));
                           data.addData("seconds", "60");
                           return data;
                       }

                       @SuppressWarnings("unchecked")
                       @Override
                       public void result(String result) throws Exception {
                           Map<String, Object> map = (Map<String, Object>) PaseJson
                               .paseJsonToObject(result);
                           Map<String, Object> map2 = (Map<String, Object>) map.get("data");
                           yzm = map2.get("randNum").toString();
                       }
                   };

//    /**
//     * 提交手机号信息
//     */
//    INetWork tjsjh = new INetWork() {
//
//                       @Override
//                       public boolean validate() {
//                           if (!yzmEditText.getText().toString().equals(yzm)) {
//                               toastShow("验证码输入不正确，请重新输入！");
//                               return false;
//                           }
//                           return true;
//                       }
//
//                       @Override
//                       public Data getSubmitData() throws Exception {
//                           Data data = new Data("register/checkMobile.json");
//                           //            data.addData("user_id", "");
//                           data.addData("wbopen_id", "");
//                           data.addData("wxopen_id", "");
//                           data.addData("mobile", activity.getIntent().getExtras().getString("sjh"));
//                           return data;
//                       }
//
//                       @SuppressWarnings("unchecked")
//                       @Override
//                       public void result(String result) throws Exception {
//                           Map<String, Object> map = (Map<String, Object>) PaseJson
//                               .paseJsonToObject(result);
//                           String loginType = PropertiesUtil.read(activity,PropertiesUtil.LOGINTYPE);
//                           if (map.get("code").toString().equals("1")) {//1手机号未注册
//                               Intent intent = new Intent();
//                               intent.putExtra("phone", sjhEditText.getText().toString());
//                               if(loginType.equals("1")){//手机号注册（没有用第三方登录）
//                                   
//                               }else if(loginType.equals("2")){
//                                   intent.putExtra("wbid", "wb123456789");
//                               }else if(loginType.equals("3")){
//                                   intent.putExtra("wxid", "wx123456789");
//                               }
//                               intent.putExtra("yzm", yzmEditText.getText().toString());
//                               intent.setClass(activity, RegistSzmmActivity.class);
//                               startActivity(intent);
//                           } else if (map.get("code").toString().equals("2")) {//已注册信息不完整,跳过设置密码，完善个人资料
//                           //                Intent intent=new Intent();
//                           //                intent.setClass(activity, RegistSzmmActivity.class);
//                           //                startActivity(intent);
//                               if(loginType.equals("2")){
//                                   Intent intent = new Intent();
//                                   intent.setClass(activity, RegistCjgrzlActivity.class);
//                                   startActivity(intent);
//                               }else if (loginType.equals("3")) {
//                                   Intent intent = new Intent();
//                                   intent.setClass(activity, RegistCjgrzlActivity.class);
//                                   startActivity(intent);
//                               }
//                               toastShow("此手机号已注册，请直接登录！");
//                           } else if (map.get("code").toString().equals("3")) {
//                               //                Intent intent=new Intent();
//                               //                intent.setClass(activity, RegistSzmmActivity.class);
//                               //                startActivity(intent);
//                               toastShow("此手机号已注册，请直接登录！");
//                           } else if (map.get("code").toString().equals("-1")) {//出现错误信息
//                               toastShow(map.get("msg").toString());
//                           }
////                           Map<String, Object> map2 = (Map<String, Object>) map.get("data");
////                           if (map2 != null) {
////                               PropertiesUtil.write(activity, PropertiesUtil.USERID,
////                                   PaseJson.getMapMsg(map2, "id"));
////                               PropertiesUtil.write(activity, PropertiesUtil.NICKNAME,
////                                   PaseJson.getMapMsg(map2, "nickname"));
////                               PropertiesUtil.write(activity, PropertiesUtil.HEADIMGURL,
////                                   PaseJson.getMapMsg(map2, "headimgurl"));
////                           }
//                       }
//                   };

    /**
     * 60秒计时器
     */
    private void timer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fsyzmButton.setText(i + "秒后重发");
                i--;
                if (i == 1) {
                    fsyzmButton.setClickable(true);
                    fsyzmButton.setBackgroundResource(R.drawable.login_share_yzm);
                    fsyzmButton.setTextColor(activity.getResources().getColor(R.color.white));
                    fsyzmButton.setText("重新获取");
                    i = 60;
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
