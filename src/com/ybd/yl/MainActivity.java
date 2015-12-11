package com.ybd.yl;

import com.ybd.yl.login.LoginActivity;

import android.content.Intent;
import android.os.Handler;

/**
 * 引导页
 * 
 * @author cyf
 * @version $Id: MainActivity.java, v 0.1 2015-10-23 下午3:52:41 cyf Exp $
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.activity_main);
//        NetWork.submit(activity, new Login());
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.setClass(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

//    private class Login implements INetWork {
//        @Override
//        public boolean validate() {
//            return true;
//        }
//
//        @Override
//        public Data getSubmitData() throws Exception {
//            Data data = new Data("searchUser.json");
//            data.addData("limit", "20");
//            data.addData("page", "1");
//            return data;
//        }
//
//        @Override
//        public void result(String result) throws Exception {
//            Log.v("dddd", result);
//        }
//    }
}
