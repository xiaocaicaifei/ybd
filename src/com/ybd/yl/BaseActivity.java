package com.ybd.yl;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ybd.common.App;
import com.ybd.common.C;
import com.ybd.common.SystemBarTintManager;

/**
 * 基础的Activity,所有的Activity必须继承
 * 
 * @author cyf
 * @version $Id: BaseActivity.java, v 0.1 2011-6-3 上午06:18:56 cyf Exp $
 * 
 *          2014-04-26 zn add 带请求编码的跳转 toActivity(Intent intent, Class<? extends
 *          Activity> end, int requestCode)
 */
public abstract class BaseActivity extends FragmentActivity implements App {

    /** 触摸缩放的处理 */
    //    protected ZoomTouch zoom;

    /** 所有已存在的Activity */
    public static ConcurrentLinkedQueue<Activity> allActivity        = new ConcurrentLinkedQueue<Activity>();

    //    /** 同时有效的界面数量 */
    protected static final int                    validActivityCount = 15;
    //
    //    /** 用户名 */
    //    protected static final String NAME = "name";
    /**
     * 当前页数
     */
    public int                                    page               = 1;
    public Activity                               activity;

    //    private DatePickerDialog dateDialog = null;
    //    private TimePickerDialog timeDialog = null;

    //    /** 初始化组件（子类重写此方法） */
    protected abstract void initComponentBase();
    
    private TextView titleTextView;
    private RelativeLayout leftRelativeLayout;//统一标题头左边按钮的背景层
    private RelativeLayout rightRelativeLayout;//统一标题头右边按钮的背景层
    private ImageView leftImageView;//统一标题头左边按钮
    private ImageView rightImageView;//统一标题头右边的按钮

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {

        //	this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // Activity打开后并不直接显示软键盘窗口，直到用户自己touch文本框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (allActivity.size() >= validActivityCount) {
            Activity act = allActivity.poll();
            act.finish();// 结束
        }
        allActivity.add(this);
        dumpActivity();
        activity = this;
        initComponentBase();
    }
    /**
     * 初始化公用的控件
     * @param titleName 标题头显示的文字
     */
    public void initPublicView(String titleName){
        leftRelativeLayout=(RelativeLayout) findViewById(R.id.left_rl);
        if(leftRelativeLayout!=null){
            leftRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
       titleTextView=(TextView) findViewById(R.id.title_name_tv);
       if(titleTextView!=null){
           titleTextView.setText(titleName);
       }
    }
    /**
     * 初始化公用的控件标题头的部分
     * @param titleName 标题头显示的文字
     */
     public void initPublicView(String titleName,int leftId,int rightId,OnClickListener leftClickListener,OnClickListener rightClickListener) {
         leftRelativeLayout = (RelativeLayout) activity.findViewById(R.id.left_rl);
         rightRelativeLayout = (RelativeLayout) activity.findViewById(R.id.right_rl);
         leftImageView=(ImageView) activity.findViewById(R.id.left_iv);
         rightImageView=(ImageView) activity.findViewById(R.id.right_iv);
         if (leftId==R.drawable.login_fh) {//如果是特殊情况，是返回按钮
             leftRelativeLayout.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     activity.finish();
                 }
             });
         }
         if(leftId==0){//表示左边没有按钮
             leftImageView.setVisibility(View.GONE);
         }else{
             leftImageView.setVisibility(View.VISIBLE);
             leftImageView.setBackgroundResource(leftId);
             leftRelativeLayout.setOnClickListener(leftClickListener);
         }
         if(rightId==0){//表示右边没有按钮
             rightImageView.setVisibility(View.GONE);
         }else{
             rightImageView.setVisibility(View.VISIBLE);
             rightImageView.setBackgroundResource(leftId);
             rightRelativeLayout.setOnClickListener(rightClickListener);
         }
         titleTextView = (TextView) activity.findViewById(R.id.title_name_tv);
         if (titleTextView != null) {
             titleTextView.setText(titleName);
         }
         
     }
    /**
     * 控制台上打印 {@link BaseActivity#allActivity}
     */
    private void dumpActivity() {
        if (C.isDebug) {
            for (Activity a : allActivity) {
                Log.d("BaseActivity", a.getClass().getName());
            }
        }
    }

    public static void finishAll() {
        // 结束Activity
        try {
            for (Activity act : allActivity) {
                allActivity.remove(act);
                act.finish();
            }
        } catch (Exception e) {
            Log.e("BaseActivity", e.getMessage(), e);
        }
    }

    /**
     * 打印异常
     * 
     * @param e
     *            要打印的异常栈
     */
    protected void error(Exception e) {
        Log.e(this.getClass().getName(), e.getMessage(), e);
    }

    /**
     * 设置状态的颜色
     */
    public void setStateColor(int index) {
        // 创建状态栏的管理实例  
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // 激活状态栏设置  
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置  
        tintManager.setNavigationBarTintEnabled(true);

        switch (index) {
            case 1://登录注册快捷入口的颜色
                   // 设置一个颜色给系统栏  
                tintManager.setTintColor(activity.getResources().getColor(R.color.login_bg));
                break;
            case 2://所有的红色的状态栏的头的颜色
                   // 设置一个颜色给系统栏  
                tintManager.setTintColor(activity.getResources().getColor(
                    R.color.unitform_title_red));
                break;
            default:
                break;
        }
        //透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // 设置一个样式背景给导航栏  
        //        tintManager.setNavigationBarTintResource(R.drawable.my_tint);  
        //        // 设置一个状态栏资源  
        //        tintManager.setStatusBarTintDrawable(MyDrawable);  
    }

    /**
     * 根据信息弹出一个提示框
     * 
     * @param msg
     *            要提示的信息
     */
    public void toastShow(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View toastView = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.LLToast));
        TextView toastTV = (TextView) toastView.findViewById(R.id.tv_toast);
        toastTV.setText(msg);
        Toast tost = new Toast(this);
        tost.setView(toastView);
        tost.setDuration(Toast.LENGTH_SHORT);
        tost.setGravity(Gravity.CENTER, 0, 0);
        tost.show();
        // DialogUtil.toast(this, msg);
    }

    /**
     * 根据信息弹出一个提示框
     * 
     * @param msg
     *            要提示的信息的句柄
     */
    public void toastShow(int msg) {
        toastShow(getString(msg));
    }

    /**
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当用户点击返回时，结束掉当前Activity
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @see android.app.Activity#finish()
     */
    public void finish() {
        try {
            super.finish();
            // 从Activity集合中清理出已结束的Activity
            if (allActivity != null && allActivity.size() > 0 && allActivity.contains(this)) {
                allActivity.remove(this);
            }
            dumpActivity();
        } catch (Exception e) {
            error(e);
        }
    }

    /**
     * 取得版本号
     * 
     * @return
     */
    public int getVersionCode() {
        if (null != getPackageInfo()) {
            return getPackageInfo().versionCode;
        } else {
            return 1;
        }
    }

    /**
     * 取得版本名称
     * 
     * @return
     */
    public String getVersionName() {
        if (null != getPackageInfo()) {
            return getPackageInfo().versionName;
        } else {
            return "";
        }
    }

    private PackageInfo getPackageInfo() {
        PackageManager manager = this.getPackageManager();
        try {
            return manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.d("BaseActivity", e.getMessage());
        }
        return null;
    }

    /**
     * XListview下拉刷新的监听的事件
     * 
     * @param listView
     * @param serverSubmit
     */
    //    public void setXListViewListener(final XListView listView,
    //	    final INetWork serverSubmit) {
    //	listView.setXListViewListener(new IXListViewListener() {// 实现下拉刷新和加载更多的接口
    //	    @Override
    //	    public void onRefresh() {
    //		new Handler().postDelayed(new Runnable() {// 实现延迟2秒加载刷新
    //			    @Override
    //			    public void run() {
    //				// 不实现顶部刷新
    //				try {
    //				    page = 1;
    //				    NetWork.submit(activity, serverSubmit);
    //				} catch (Exception e) {
    //				    e.printStackTrace();
    //				}
    //			    }
    //			}, 2000);
    //	    }
    //
    //	    @Override
    //	    public void onLoadMore() {
    //		new Handler().postDelayed(new Runnable() {// 实现底部延迟2秒加载更多的功能
    //			    @SuppressWarnings("deprecation")
    //			    @Override
    //			    public void run() {
    //				try {
    //				    page++;
    //				    NetWork.submit(activity, serverSubmit);
    //				    listView.stopRefresh();
    //				    listView.stopLoadMore();
    //				    listView.setRefreshTime(new Date()
    //					    .toLocaleString());
    //				} catch (Exception e) {
    //				    e.printStackTrace();
    //				}
    //			    }
    //			}, 2000);
    //	    }
    //	});
    //    }

    public void showFileChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        // intent.setTypeAndNormalize("*/*");
        intent.putExtra("return-data", true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择要上传的文件"), requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

}
