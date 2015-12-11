package com.ybd.yl.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ybd.common.C;
import com.ybd.yl.R;


/**
 * 微信的回调类
 * 微信返回的所有信息都会在这里面
 * @author cyf
 * @version $Id: WXEntryActivity.java, v 0.1 2015-11-4 下午3:07:11 cyf Exp $
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static IWXAPI api;           //注册的微信的api
    
    @Override
    public void onReq(BaseReq arg0) {
    }

    /**
     * 所有从微信返回的信息都将在这里面展现出来
     * @see com.tencent.mm.sdk.openapi.IWXAPIEventHandler#onResp(com.tencent.mm.sdk.modelbase.BaseResp)
     */
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        Log.v("dddd", "Android result:"+resp.errCode+"");
        switch (resp.errCode) {
        case BaseResp.ErrCode.ERR_OK:
            result = R.string.errcode_success;
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            result = R.string.errcode_cancel;
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            result = R.string.errcode_deny;
            break;
        default:
            result = R.string.errcode_unknown;
            break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        Bundle bundle=new Bundle();
        resp.toBundle(bundle);
        String token=((SendAuth.Resp) resp).token;
        Log.v("dddd", "Android result:"+token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, C.APP_ID, false);//创建微信通讯的openapi接口
        api.registerApp(C.APP_ID);
//        api.handleIntent(getIntent(), this);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    
    /**
     * 在该页面暂停的时候关闭该不透明的页面
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
