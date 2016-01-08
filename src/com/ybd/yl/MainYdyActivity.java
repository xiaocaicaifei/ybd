package com.ybd.yl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybd.common.L;
import com.ybd.common.SlidePageAdapter;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.login.LoginActivity;

/**
 * 引导页(第一次加载的引导页)
 * 
 * @author cyf
 * @version $Id: MainActivity.java, v 0.1 2015-10-23 下午3:52:41 cyf Exp $
 */
public class MainYdyActivity extends BaseActivity {
    ViewPager                 viewPager;
    RelativeLayout            layout;
    //    ImageView                 fhImageView;
    TextView                  titleTextView;
    LinearLayout              linearLayout;
    List<View>                dotViewList = new ArrayList<View>();
    List<Map<String, Object>> imgList=new ArrayList<Map<String,Object>>();
    LayoutInflater            inflater;
    int                       index       = 0;

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.activity_main_ydy);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        for (int i = 0; i < 4; i++) {
            Map<String, Object> m=new HashMap<String, Object>();
            switch (i) {
                case 0:
                    m.put("path", "file:///android_asset/index_ydy_one.jpg");
                    break;
                case 1:
                    m.put("path", "file:///android_asset/index_ydy_two.jpg");
                    break;
                case 2:
                    m.put("path", "file:///android_asset/index_ydy_three.jpg");
                    break;
                case 3:
                    m.put("path", "file:///android_asset/index_ydy_three.jpg");
                    break;
                default:
                    break;
            }
            imgList.add(m);
        }
//        imgList = ((List<Map<String, Object>>) this.getIntent().getSerializableExtra("object"));
        linearLayout = (LinearLayout) findViewById(R.id.dot_layout);
        inflater = this.getLayoutInflater();
        initViewPager();

    }

    /**
     * 初始化图片的滑动ViewPager
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initViewPager() {
        final List<View> viewPage = new ArrayList<View>();
        int i = 0;
        for (Map<String, Object> am : imgList) {
            View view = new View(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            //          params.height = LayoutParams.WRAP_CONTENT;
            //          params.width = LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
            view.setPadding(100, 0, 100, 0);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.dot_select);
            } else {
                params.setMargins(10, 0, 0, 0);
                view.setBackgroundResource(R.drawable.dot_unselect);
            }
            if(i<3){
                dotViewList.add(view);
                linearLayout.addView(view);
            }
            View layoutWebView = inflater.inflate(R.layout.common_previewimg_webview, null);
            //          final ProgressBar bar=(ProgressBar) layoutWebView.findViewById(R.id.pb);
            //          bar.setMax(100); 
            //            final ImageView progressImageView = (ImageView) layoutWebView
            //                .findViewById(R.id.progressview);
            //          final WebView webView=new WebView(this);
            //            progressImageView.setVisibility(View.VISIBLE);
            final WebView webView = (WebView) layoutWebView.findViewById(R.id.webview);
            String addrStr = "";
            if (PaseJson.getMapMsg(am, "path").equals("")) {
                addrStr = PaseJson.getMapMsg(am, "pic_url");
            } else {
                addrStr = PaseJson.getMapMsg(am, "path");
            }
            //            String addrStr = am.get("path").toString();
            //            String addrStr = C.IP+"/download.mo?uuid=b1639941-a37e-4f0a-a22d-145dcfb62bf0&id=40288acf4c6d6f74014c6d7a50380006";

            WebSettings webSettings = webView.getSettings(); // webView: 类WebView的实例
            //          webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);  //
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setJavaScriptEnabled(true);
            //            webView.addJavascriptInterface(new JavascriptInterface(), "imagelistener");
            //          webView.setWebViewClient(new MyWebViewClient());
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
            //          webView.requestFocus();
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    //                   handler.cancel(); // Android默认的处理方式
                    handler.proceed(); // 接受所有网站的证书
                    //handleMessage(Message msg); // 进行其他处理
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description,
                                            String failingUrl) {
                    // TODO Auto-generated method stub
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    //                    if (progress == 100) {
                    //                        progressImageView.setVisibility(View.GONE);
                    //                    }
                    super.onProgressChanged(view, progress);
                }
            });
            String data = "<html onclick='window.imagelistener.openImage();'><div style='height:100%;width:100%;background:url("
                          + addrStr
                          + ") no-repeat center center;background-size:100% auto;' /><html>";
            webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            //          if(i>0){
            //              webView.stopLoading();
            //          }
            webView.setOnGenericMotionListener(new OnGenericMotionListener() {
                @Override
                public boolean onGenericMotion(View v, MotionEvent event) {
                    return false;
                }
            });
            webView.setBackgroundColor(Color.parseColor("#000000"));
            //          webView.setBackgroundResource(R.drawable.no_pic);
            //          webView.setAlpha(50);
            // 监听   
            viewPage.add(layoutWebView);
            i++;
        }

        SlidePageAdapter myAdapter = new SlidePageAdapter(viewPage, this);
        viewPager.setAdapter(myAdapter);
        //下面的点图
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < dotViewList.size(); i++) {
                    View v = dotViewList.get(i);
                    if (i == arg0) {
                        v.setBackgroundResource(R.drawable.dot_select);
                    } else {
                        v.setBackgroundResource(R.drawable.dot_unselect);
                    }
                }
                
                if(arg0==3){
                    Intent intent=new Intent();
                    intent.setClass(activity, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //              titleTextView.setText("查看图片（第"+(arg0+1)+"/"+imgList.size()+"）");
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                
            }
        });
        //      int index=this.getIntent().getExtras().getInt("index");
        //      viewPager.setCurrentItem(index);
        //        fhImageView.setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                finish();
        //            }
        //        });
    }
}
