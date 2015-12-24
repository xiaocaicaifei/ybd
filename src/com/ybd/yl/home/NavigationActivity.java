/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.ybd.yl.home;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.w.song.widget.navigation.RollNavigationBar;
import cn.w.song.widget.navigation.adapter.RollNavigationBarAdapter;

import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
/**
 * 导航栏
 * 
 * @author cyf
 * @version $Id: NavigationActivity.java, v 0.1 2014-4-10 下午4:39:05 cyf Exp $
 */
public class NavigationActivity extends BaseActivity {
    public static int[]                nums        = new int[] { 0, 0, 0, 0, 0 };
    private static final String    TAG        = "NavigationActivity";
    /**下次再说间隔的时间 1000 * 60 * 60 * 24*/
//    private static final long      TIME       = 1000 * 60 * 60 * 24;
    /** 双击退出函数 */
    private static Boolean         isExit     = false;
    private RollNavigationBar      rnb;
    private LayoutInflater         mInflater;
    private Map<Integer, Fragment> fs         = new HashMap<Integer, Fragment>(5);

    private MyNavigationBarAdapter navAdapter = null;

//    private TZReceiver             receiver   = null;

//    private DownloadManager        downloadManager;
//
//    private long                   myDownloadReference;
//
//    private BroadcastReceiver      downReceiver;

    /**
     * @see com.common.BaseActivity#initComponentBase()
     */
    protected void initComponentBase() {
        setContentView(R.layout.home_navigation);
        mInflater = LayoutInflater.from(this.getApplicationContext());
        rnb = (RollNavigationBar) findViewById(R.id.navigationbartest_ui_RollNavigationBar);
        /* 设置滑动条的滑动时间，时间范围在0.1~1s，不在范围则默认0.1s */
        rnb.setSelecterMoveContinueTime(0.1f);
        /* 设置滑动条样式（图片） */
//        rnb.setSelecterDrawableSource(R.drawable.home_dh);// 必须
        /* 设置导航栏的被选位置 */

//        rnb.setSelectedChildPosition(Navigation.QZ.getIndex());// 可以不设置（设置选中第几个）
        selectedTab(Navigation.YL.getIndex());//进来设置一个默认的选择项

        /* 导航栏扩展 */
        navAdapter = new MyNavigationBarAdapter();
        rnb.setAdapter(navAdapter);// 必须
        rnb.setNavigationBarListener(new RollNavigationBar.NavigationBarListener() {
            @Override
            public void onNavigationBarClick(int position, View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    selectedTab(position);
                }
            }
        });
    }

    /**
     * 导航栏扩展
     * 
     * @author wum
     * @version $Id: NavigationActivity.java, v 0.1 2014年5月15日 下午3:46:08 wum Exp
     *          $
     */
    private final class MyNavigationBarAdapter extends RollNavigationBarAdapter {

        @Override
        public int getCount() {
            return Navigation.values().length;
        }

        /**
         * 获取每个组件
         * 
         * @param position
         *            组件的位置
         * @param contextView
         *            组件
         * @param parent
         *            上层组件
         */
        @Override
        public View getView(int position, View contextView, ViewGroup parent) {
            mInflater.inflate(R.layout.home_tabs_item, (ViewGroup) contextView);
            Navigation nav = Navigation.getEnum(position);
            ImageView img = (ImageView) contextView.findViewById(R.id.image_view);
            TextView tv = (TextView) contextView.findViewById(R.id.tv);
            tv.setText(nav.getName());
            // 区分选择与被选择图片
            if (position == rnb.getSelectedChildPosition()) {// 被选择
                img.setImageResource(nav.getPhotoSelected());
                tv.setTextColor(getResources().getColor(R.color.nav_txt_sel));
            } else {// 没被选择
                img.setImageResource(nav.getPhoto());
                tv.setTextColor(getResources().getColor(R.color.nav_txt));
            }
            
            //导航栏上显示数字（暂时不用）
//            TextView tvn = (TextView) contextView.findViewById(R.id.new_tv);
//            int num = nums[nav.getIndex()];
//            if (num > 0) {
//                tvn.setText(String.valueOf(num));
//                tvn.setVisibility(View.VISIBLE);
//            } else {
//                tvn.setVisibility(View.GONE);
//            }
            return contextView;
        }

    }

    /**
     * @see com.common.BaseActivity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Timer tExit = null;
                if (isExit == false) {
                    isExit = true; // 准备退出  
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    tExit = new Timer();
                    tExit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isExit = false; // 取消退出  
                        }
                    }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  

                } else {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    this.startActivity(intent);
                    finishAll();
                    return Boolean.TRUE;
                }

        }
        return Boolean.FALSE;
    }

    /**
     * 选中导航栏
     * 
     * @param position
     *            tab的position
     */
    private void selectedTab(int position) {
//        if (position == Navigation.GG.getIndex()) {
//            PropertiesUtil.write(NavigationActivity.this, "isShowDel", "false");
//        } else if (position == Navigation.WD.getIndex()) {
//            PropertiesUtil.write(NavigationActivity.this, "isShowDel", "true");
//        }
//        if (position == Navigation.WD.getIndex()) {
//            Intent intent = new Intent(NICK_CHANGE_NOTIFACTION);
//            sendBroadcast(intent);
//            LocalBroadcastManager.getInstance(NavigationActivity.this).sendBroadcast(intent);
//        }
        Navigation nav = Navigation.getEnum(position);
        Log.d(TAG, nav.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (fs.containsKey(nav.getIndex())) {
            for (Map.Entry<Integer, Fragment> ele : fs.entrySet()) {
                if (ele.getKey() == nav.getIndex()) {
                    if (nums[position] > 0 || ele.getValue().isVisible()) {
                        ft.remove(ele.getValue());
                        Fragment details = Fragment.instantiate(NavigationActivity.this, nav
                            .getClas().getName());
                        details.setArguments(getIntent().getExtras());
                        fs.put(nav.getIndex(), details);
                        ft.add(R.id.yl_data, details);
                        ft.addToBackStack(null);
                        ft.commitAllowingStateLoss();
                        nums[position] = 0;
                        return;
                    } else {
                        ft.show(ele.getValue());
                    }
                } else {
                    ft.hide(ele.getValue());
                }
            }
            ft.commitAllowingStateLoss();
        } else {
            Fragment details = Fragment.instantiate(NavigationActivity.this, nav.getClas()
                .getName());
            details.setArguments(getIntent().getExtras());
            fs.put(nav.getIndex(), details);
            ft.add(R.id.yl_data, details);
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
            nums[position] = 0;
        }
        rnb.setSelectedChildPosition(position);
    }

    /**
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        // 不要删除掉，否则Fragment会重叠
    }

    /**
     * 刷新导航栏的数字
     * 
     * @author wum
     * @version $Id: NavigationActivity.java, v 0.1 2014年9月3日 上午9:40:32 wum Exp
     *          $
     */
//    public class TZReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null) {
//                if (LOGINOUT.equals(intent.getAction())) {
//                    String cur = PropertiesUtil.getSqbm(getApplicationContext());
//                    String curName = PropertiesUtil.read(getApplicationContext(),
//                        PropertiesUtil.tagName);
//                    if (StringUtil.isBlank(cur) || StringUtil.isBlank(curName)) {
//                        toActivity(AppSelectAreaActivity.class);
//                    } else {
//                        //                        rnb.setSelectedChildPosition(Navigation.GG.getIndex());// 可以不设置
//                        //                        selectedTab(Navigation.GG.getIndex());
//                        //                        rnb.refreshView(navAdapter);
//                    }
//                } else if (CHANGEXQ.equals(intent.getAction())) {
//                    fs.remove(Navigation.GG.getIndex());
//                }
//            }
//        }
//    }

    /**
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    protected void onDestroy() {
        super.onDestroy();
//        if (receiver != null) {
//            this.unregisterReceiver(receiver);
//        }
//        if (null != downReceiver) {
//            this.unregisterReceiver(downReceiver);
//        }
    }

    /**
     * 更新导航栏的数字
     * 
     * @param intent
     *            {@link Intent}
     */
//    private void ref(Intent intent) {
//        rnb.refreshView(navAdapter);
//    }

    /**
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        ref(intent);
//    }

    /**
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
//    protected void onResume() {
//        super.onResume();
//        String last = PropertiesUtil.read(getApplicationContext(), PropertiesUtil.LAST_UPDATE);
//        if (StringUtil.isBlank(last) || System.currentTimeMillis() - Long.parseLong(last) > TIME) {
//            NetWork.submitNoDialog(getApplicationContext(), new GetClintNetWork());
//        }
//    }

    /**
     * 更新客户端
     * 
     * @author wum
     * @version $Id: WdszActivity.java, v 0.1 2014年12月8日 上午11:29:29 wum Exp $
     */
//    private class GetClintNetWork implements INetWork {
//
//        @Override
//        public boolean validate() {
//            return true;
//        }
//
//        @Override
//        public Data getSubmitData() {
//            Data d = new Data("getNewVersion", WsEnum.ClientService);
//            d.addData("version", getVersionCode());
//            return d;
//        }
//
//        @Override
//        public void result(String result) throws Exception {
//            JSONObject o = new JSONObject(result);
//            //boolean isForced = o.getBoolean("isForced");
//            final String url = o.getString("url");
//            String desc = o.getString("desc");
//            if (StringUtil.isBlank(url)) {
//                //toastShow(desc);
//                return;
//            }
//
//            LayoutInflater inflater = LayoutInflater.from(NavigationActivity.this);
//            FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.wd_client, null);
//
//            // 新建对话框对象
//            final AlertDialog dialog = new AlertDialog.Builder(NavigationActivity.this).create();
//            dialog.setCancelable(true);
//            dialog.show();
//            dialog.setContentView(layout);
//
//            // 消息内容
//            TextView dialog_title = (TextView) layout.findViewById(R.id.dialog_title);
//            dialog_title.setText("发现新版本：");
//
//            // 消息内容
//            TextView dialog_msg = (TextView) layout.findViewById(R.id.dialog_msg);
//            dialog_msg.setText(desc);
//
//            // 确定按钮
//            Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
//            btnOK.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    downLoad(url);
//                    dialog.dismiss();
//                }
//            });
//
//            // 取消按钮
//            Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
//            btnCancel.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    PropertiesUtil.write(getApplicationContext(), PropertiesUtil.LAST_UPDATE,
//                        String.valueOf(System.currentTimeMillis()));
//                }
//            });
//
//        }
//
//        private void downLoad(String url) {
//            //下载文件
//            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//            Uri uri = Uri.parse(url);
//            DownloadManager.Request request = new Request(uri);
//            String f = FileUtil.getRFileSaveDir(DirType.CLIENT, "csp.apk");
//            request.setDestinationUri(Uri.fromFile(new File(f)));
//            myDownloadReference = downloadManager.enqueue(request);
//        }
//    }

}
