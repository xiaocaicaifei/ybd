package com.ybd.yl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ybd.common.net.INetWork;
import com.ybd.common.net.NetWork;
import com.ybd.common.xListView.XListView;
import com.ybd.common.xListView.XListView.IXListViewListener;

/**
 * 该类是所有Fragment的父类，所有在主要内容区域显示的页面都继承该页面
 * 
 * @author caiyanfei
 * @version $Id: BaseFragment.java, v 0.1 2015-3-23 下午2:11:52 caiyanfei Exp $
 */
public abstract class BaseFragment extends Fragment {

    public LayoutInflater  inflater;
    public View            view;
    public Activity        activity;
    public int             page = 1;
    //	private DatePickerDialog dateDialog = null;
    private RelativeLayout leftRelativeLayout;//统一标题头左边按钮的背景层
    private RelativeLayout rightRelativeLayout;//统一标题头右边按钮的背景层
    private ImageView leftImageView;//统一标题头左边按钮
    private ImageView rightImageView;//统一标题头右边的按钮
    private TextView       titleTextView;

    //  /** 初始化组件（子类重写此方法） */
    protected abstract void initComponentBase();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //		this.activity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        activity = getActivity();
        initComponentBase();
        return view;
    }

    /**
     * XListview下拉刷新的监听的事件
     * 
     * @param listView
     * @param serverSubmit
     */
    public void setXListViewListener(final XListView listView, final INetWork serverSubmit,final List<Map<String, Object>> list) {
        listView.setXListViewListener(new IXListViewListener() {// 实现下拉刷新和加载更多的接口
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {// 实现延迟2秒加载刷新
                            @Override
                            public void run() {
                                // 不实现顶部刷新
                                try {
                                    page = 1;
                                    list.clear();
                                    NetWork.submit(activity, serverSubmit);
                                    listView.stopRefresh();
                                    listView.stopLoadMore();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 2000);
                }

                @Override
                public void onLoadMore() {
                    new Handler().postDelayed(new Runnable() {// 实现底部延迟2秒加载更多的功能
                            @SuppressWarnings("deprecation")
                            @Override
                            public void run() {
                                try {
                                    page++;
                                    NetWork.submit(activity, serverSubmit);
                                    listView.stopRefresh();
                                    listView.stopLoadMore();
                                    listView.setRefreshTime(new Date().toLocaleString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 2000);
                }
            });
    }

    /**
     * 主内容区域跳转页面
     */
    public void toFragmentContent(BaseFragment baseFragment, int id, Bundle bundle) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (bundle != null) {
            baseFragment.setArguments(bundle);
        }
        ft.replace(id, baseFragment);
        ft.addToBackStack("tag");
        ft.commit();
    }

    /**
    * 初始化公用的控件(默认左边是返回按钮)
    * @param titleName 标题头显示的文字
    */
    public void initPublicView(String titleName) {
        leftRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_rl);
        if (leftRelativeLayout != null) {
            leftRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
        titleTextView = (TextView) activity.findViewById(R.id.title_name_tv);
        if (titleTextView != null) {
            titleTextView.setText(titleName);
        }
    }
    /**
     * 初始化公用的控件标题头的部分
     * @param titleName 标题头显示的文字
     */
     public void initPublicView(String titleName,int leftId,int rightId,OnClickListener leftClickListener,OnClickListener rightClickListener) {
         leftRelativeLayout = (RelativeLayout) view.findViewById(R.id.left_rl);
         rightRelativeLayout = (RelativeLayout) view.findViewById(R.id.right_rl);
         leftImageView=(ImageView) view.findViewById(R.id.left_iv);
         rightImageView=(ImageView) view.findViewById(R.id.right_iv);
         titleTextView = (TextView) view.findViewById(R.id.title_name_tv);
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
             rightImageView.setBackgroundResource(rightId);
             rightRelativeLayout.setOnClickListener(rightClickListener);
         }
         if (titleTextView != null) {
             titleTextView.setText(titleName);
         }
         
     }

    /**
     * 根据信息弹出一个提示框
     * 
     * @param msg
     *            要提示的信息
     */
    public void toastShow(String msg) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View toastView = inflater.inflate(R.layout.toast,
            (ViewGroup) activity.findViewById(R.id.LLToast));
        TextView toastTV = (TextView) toastView.findViewById(R.id.tv_toast);
        toastTV.setText(msg);
        Toast tost = new Toast(activity);
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
     * 所有页面的返回按钮的事件
     */
    //	public void addFh(View v) {
    //		TextView fhTextView = (TextView) v.findViewById(R.id.fh);
    //		fhTextView.setOnClickListener(new OnClickListener() {
    //			@Override
    //			public void onClick(View arg0) {
    //				activity.finish();
    //			}
    //		});
    //	}

}