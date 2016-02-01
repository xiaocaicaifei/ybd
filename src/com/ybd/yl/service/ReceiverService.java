package com.ybd.yl.service;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.L;
import com.ybd.common.MainApplication;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;
import com.ybd.yl.xx.XxIndexActivity;

/**
 * 接收和发送容联云发送过来的信息
 * 
 * @author cyf
 * @version $Id: ReceiverService.java, v 0.1 2016-1-14 下午4:31:31 cyf Exp $
 */
public class ReceiverService extends Service {
    private static String      loginZh = "";
    //    private static boolean connectSuccess=false;

    //声明通知（消息）管理器   
    NotificationManager        m_NotificationManager;
    Intent                     m_Intent;
    PendingIntent              m_PendingIntent;
    //声明Notification对象   
    Notification               m_Notification;

    private SoundPool          sp;                   //声明一个SoundPool
    private int                music;                //定义一个整型用load（）；来设置suondID
    public static RongIMClient mRongIMClient;
    public Context             context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        loginZh = intent.getExtras().getString("loginZh");
        initNotify();
        connect(loginZh);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化状态栏
     */
    private void initNotify() {
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.music, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        //构造Notification对象   
        m_Notification = new Notification();
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(MainApplication
            .getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            mRongIMClient = RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Toast.makeText(ReceiverService.this, "连接成功" + userid, Toast.LENGTH_LONG).show();
                    receiverMsg();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    //                    Log.d("LoginActivity", "--onError" + errorCode);
                    Log.v("dddd", "连接失败" + errorCode);
                }
            });
        }
    }

    @SuppressWarnings("static-access")
    public void receiverMsg() {
        mRongIMClient.setOnReceiveMessageListener(new OnReceiveMessageListener() {

            @Override
            public boolean onReceived(Message arg0, int arg1) {
                if (arg0.getConversationType() == ConversationType.GROUP) {//如果接受到的是群组的信息

                } else if (arg0.getConversationType() == ConversationType.PRIVATE) {//如果接收到是信息时私聊的信息
                    if (isBackgroundRunning()) {
                        //状态栏显示信息
                        showNotification("您有新的艺论消息");
                    } else {
                        //提示音
                        sp.play(music, 1, 1, 0, 0, 1);
                    }
                    if (arg0.getContent() instanceof TextMessage) {//如果接受到的是文字消息
                        TextMessage textMessage = (TextMessage) arg0.getContent();
                        L.v(textMessage.getExtra());
                        @SuppressWarnings("unchecked")
                        Map<String, Object> map = (Map<String, Object>) PaseJson
                            .paseJsonToObject(textMessage.getExtra());
                        //                    if(PaseJson.getMapMsg(map, "type").equals("1")){//说明是聊天消息
                        map.put("sender_type", "0");
                        map.put("send_content", textMessage.getContent());
                        BroadcaseUtil.sendBroadcase(BroadcaseUtil.XX_LT, context,
                            (Serializable) map);
                        //                    }
                    }
                    return false;
                }
                return false;
            }
        });
    }

    /**
     * 判断应用是否在后台运行
     * @return
     */
    private boolean isBackgroundRunning() {
        String processName = "com.ybd.yl";

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (activityManager == null)
            return false;
        // get running application processes
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager
            .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(processName)) {
                boolean isBackground = process.importance != IMPORTANCE_FOREGROUND
                                       && process.importance != IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState)
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    //显示状态栏的通知
    @SuppressWarnings("deprecation")
    private void showNotification(String title) {
        //初始化NotificationManager对象   
        m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //点击通知时转移内容   
        m_Intent = new Intent(ReceiverService.this, XxIndexActivity.class);
        m_Intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//如果这个Activity已经被打开了，那就不重新加载
        //主要是设置点击通知时显示内容的类   
        m_PendingIntent = PendingIntent.getActivity(ReceiverService.this, 0, m_Intent, 0); //如果轉移內容則用m_Intent();   
        //        //构造Notification对象   
        m_Notification = new Notification();
        //设置通知在状态栏显示的图标   
        m_Notification.icon = R.drawable.ic_launcher;
        //当我们点击通知时显示的内容   
        m_Notification.tickerText = "艺论消息通知";
        //通知时发出默认的声音   
        m_Notification.defaults = Notification.DEFAULT_SOUND;
        //设置通知显示的参数   
        m_Notification.setLatestEventInfo(ReceiverService.this, "艺论", title, m_PendingIntent);
        m_Notification.flags = Notification.FLAG_AUTO_CANCEL;

        //可以理解为执行这个通知   
        m_NotificationManager.notify(0, m_Notification);
        //        startForeground(0x111, m_Notification);
    }

    /**
     * 发送消息，默认发送的是私聊的消息
     * @param activity
     * @param contentMsg
     * @param extraMsg
     * @param userid
     */
    public static void sendMessage(Activity activity, String contentMsg, String extraMsg,
                                   String userid) {
        if (mRongIMClient != null) {
            TextMessage textMessage = TextMessage.obtain(contentMsg);
            textMessage.setExtra(extraMsg);
            //            textMessage.setExtra("文字消息Extra");
            mRongIMClient.sendMessage(Conversation.ConversationType.PRIVATE, userid, textMessage,
                null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        Log.d("sendMessage",
                            "----发发发发发--发送消息失败----ErrorCode----" + errorCode.getValue());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        L.v("发送成功");
                    }
                });
        } else {
            Toast.makeText(activity, "请先连接。。。", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 发送消息，消息类型用户自定义
     * @param activity
     * @param contentMsg
     * @param extraMsg
     * @param userid
     */
    public static void sendMessage(Activity activity, String contentMsg, String extraMsg,
                                   String userid, ConversationType conversationType) {
        if (mRongIMClient != null) {
            TextMessage textMessage = TextMessage.obtain(contentMsg);
            textMessage.setExtra(extraMsg);
            //            textMessage.setExtra("文字消息Extra");
            mRongIMClient.sendMessage(conversationType, userid, textMessage, null, null,
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        Log.d("sendMessage",
                            "----发发发发发--发送消息失败----ErrorCode----" + errorCode.getValue());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        L.v("发送成功");
                    }
                });
        } else {
            Toast.makeText(activity, "请先连接。。。", Toast.LENGTH_LONG).show();
        }
    }
}
