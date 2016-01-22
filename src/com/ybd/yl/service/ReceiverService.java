package com.ybd.yl.service;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ybd.common.BroadcaseUtil;
import com.ybd.common.C;
import com.ybd.common.L;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.R;
import com.ybd.yl.xx.XxIndexActivity;
import com.ybd.yl.xx.dao.XxLtDao;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPCallManager.CallType;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.OnMeetingListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;
import com.yuntongxun.ecsdk.meeting.intercom.ECInterPhoneMeetingMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMsg;

/**
 * 接收和发送容联云发送过来的信息
 * 
 * @author cyf
 * @version $Id: ReceiverService.java, v 0.1 2016-1-14 下午4:31:31 cyf Exp $
 */
public class ReceiverService extends Service {
    private static String loginZh = "";
    //    private static boolean connectSuccess=false;

    //声明通知（消息）管理器   
    NotificationManager   m_NotificationManager;
    Intent                m_Intent;
    PendingIntent         m_PendingIntent;
    //声明Notification对象   
    Notification          m_Notification;

    private SoundPool     sp;                    //声明一个SoundPool
    private int           music;                 //定义一个整型用load（）；来设置suondID

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loginZh = intent.getExtras().getString("loginZh");
        initNotify();
        // 判断SDK是否已经初始化，如果已经初始化则可以直接调用登陆接口
        // 没有初始化则先进行初始化SDK，然后调用登录接口注册SDK
        if (!ECDevice.isInitialized()) {
            ECDevice.initial(ReceiverService.this, new ECDevice.InitListener() {
                @Override
                public void onInitialized() {
                    // SDK已经初始化成功
                    ytxLogin();//登录容联云
                }

                @Override
                public void onError(Exception exception) {
                    // SDK 初始化失败,可能有如下原因造成
                    // 1、可能SDK已经处于初始化状态
                    // 2、SDK所声明必要的权限未在清单文件（AndroidManifest.xml）里配置、
                    //    或者未配置服务属性android:exported="false";
                    // 3、当前手机设备系统版本低于ECSDK所支持的最低版本（当前ECSDK支持
                    //    Android Build.VERSION.SDK_INT 以及以上版本）
                    Toast
                        .makeText(ReceiverService.this, "初始化容联云通讯失败！请检测网络是否可用！", Toast.LENGTH_LONG)
                        .show();
                }
            });
        } else {

        }
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
     * sdk初始化成功之后
     * 登录的容联云通讯
     */
    private void ytxLogin() {
        //        第二步：设置注册参数、设置通知回调监听
        // 构建注册所需要的参数信息
        //5.0.3的SDK初始参数的方法：ECInitParams params = new ECInitParams();5.1.*以上版本如下：
        ECInitParams params = ECInitParams.createParams();
        //自定义登录方式：
        //测试阶段Userid可以填写手机
        params.setUserid(loginZh);
        L.v(loginZh);
        //        params.setPwd(mm);
        params.setAppKey(C.YTX_APPKEY);
        params.setToken(C.YTX_TOKEN);
        // 设置登陆验证模式（是否验证密码）NORMAL_AUTH-自定义方式
        params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        //        //voip账号+voip密码方式：
        //        params.setUserid("voip账号");
        //        params.setPwd("voip密码");
        //        params.setAppKey("应用ID");
        //        params.setToken("应用Token");
        //        // 设置登陆验证模式（是否验证密码）PASSWORD_AUTH-密码登录方式
        //        params.setAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
        //        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        //        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        //        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        //        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

        // 设置登陆状态回调
        params.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
            public void onConnect() {
                // 兼容4.0，5.0可不必处理
            }

            @Override
            public void onDisconnect(ECError error) {
                // 兼容4.0，5.0可不必处理
            }

            @Override
            public void onConnectState(ECDevice.ECConnectState state, ECError error) {
                if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
                    if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                        //账号异地登陆
                    } else {
                        //连接状态失败
                        Log.v("dddd", ":3");
                    }
                    return;
                } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
                    Toast.makeText(ReceiverService.this, "登录成功", Toast.LENGTH_LONG).show();
                    //                    sendMsg();
                    //                    connectSuccess=true;
                }
            }
        });

        // 设置SDK接收消息回调
        params.setOnChatReceiveListener(new OnChatReceiveListener() {
            @Override
            public void OnReceivedMessage(ECMessage msg) {
                // 收到新消息
                Log.v("dddd", ":4");
                getMsg(msg);
            }

            @Override
            public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
                // 收到群组通知消息（有人加入、退出...）
                // 可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
                Log.v("dddd", ":5");
            }

            @Override
            public void onOfflineMessageCount(int count) {
                // 登陆成功之后SDK回调该接口通知账号离线消息数
                //                Log.v("dddd", ":6");
            }

            @Override
            public void onReceiveOfflineMessageCompletion() {
                // SDK通知应用离线消息拉取完成
                //                Log.v("dddd", ":7");
            }

            @Override
            public void onServicePersonVersion(int version) {
                // SDK通知应用当前账号的个人信息版本号
                //                Log.v("dddd", ":8");
            }

            @Override
            public int onGetOfflineMessage() {
                return 0;
            }

            @Override
            public void onReceiveDeskMessage(ECMessage arg0) {
                Log.v("dddd", ":9");
            }

            @Override
            public void onReceiveOfflineMessage(List<ECMessage> arg0) {
                Log.v("dddd", ":10");
            }

            @Override
            public void onSoftVersion(String arg0, int arg1) {
                Log.v("dddd", ":11");
            }
        });

        // 获得SDKVoIP呼叫接口
        // 注册VoIP呼叫事件回调监听
        ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();
        if (callInterface != null) {
            callInterface.setOnVoIPCallListener(new ECVoIPCallManager.OnVoIPListener() {
                @Override
                public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
                    // 处理呼叫事件回调
                    if (voipCall == null) {
                        Log.e("SDKCoreHelper", "handle call event error , voipCall null");
                        return;
                    }
                    // 根据不同的事件通知类型来处理不同的业务
                    ECVoIPCallManager.ECCallState callState = voipCall.callState;
                    switch (callState) {
                        case ECCALL_PROCEEDING:
                            // 正在连接服务器处理呼叫请求
                            break;
                        case ECCALL_ALERTING:
                            // 呼叫到达对方客户端，对方正在振铃
                            break;
                        case ECCALL_ANSWERED:
                            // 对方接听本次呼叫
                            break;
                        case ECCALL_FAILED:
                            // 本次呼叫失败，根据失败原因播放提示音
                            break;
                        case ECCALL_RELEASED:
                            // 通话释放[完成一次呼叫]
                            break;
                        default:
                            Log.e("SDKCoreHelper", "handle call event error , callState "
                                                   + callState);
                            break;
                    }
                }

                @Override
                public void onSwitchCallMediaTypeRequest(String arg0, CallType arg1) {
                }

                @Override
                public void onSwitchCallMediaTypeResponse(String arg0, CallType arg1) {
                }

                @Override
                public void onVideoRatioChanged(VideoRatio arg0) {
                }

                @Override
                public void onDtmfReceived(String arg0, char arg1) {
                }
            });
        }

        // 注册会议消息处理监听 
        if (ECDevice.getECMeetingManager() != null) {
            ECDevice.getECMeetingManager().setOnMeetingListener(new OnMeetingListener() {
                @Override
                public void onReceiveInterPhoneMeetingMsg(ECInterPhoneMeetingMsg msg) {
                    // 处理实时对讲消息Push
                }

                @Override
                public void onReceiveVoiceMeetingMsg(ECVoiceMeetingMsg msg) {
                    // 处理语音会议消息push
                }

                @Override
                public void onReceiveVideoMeetingMsg(ECVideoMeetingMsg msg) {
                    // 处理视频会议消息Push（暂未提供）
                }

                @Override
                public void onVideoRatioChanged(VideoRatio arg0) {
                }
            });
        }

        //第三步：验证参数是否正确，注册SDK
        if (params.validate()) {
            // 判断注册参数是否正确
            ECDevice.login(params);
        }
    }

    /**
     * 发送消息
     */
    public static void sendMsg(String jszZh, String nr, String path, final Activity activity) {
        //        if(connectSuccess){
        //           L.v("用户没有登录，不能直接发送信息了");
        //           return;
        //        }
        try {
            // 或者创建一个图片消息体 并且设置附件包体（其实图片也是相当于附件）
            // 比如我们发送SD卡里面的一张Tony_2015.jpg图片
            L.v(path + ":receiver");
            ECMessage msg;
            final ProgressDialog dialog = new ProgressDialog(activity);
            if (path.equals("")) {
                // 组建一个待发送的ECMessage
                msg = ECMessage.createECMessage(ECMessage.Type.TXT);
                //设置消息的属性：发出者，接受者，发送时间等
                msg.setForm(loginZh);
                msg.setMsgTime(System.currentTimeMillis());
                L.v("发送者：" + loginZh + " 接受者：" + jszZh);
                // 设置消息接收者
                msg.setTo(jszZh);
                msg.setSessionId(jszZh);
                // 设置消息发送类型（发送或者接收）
                msg.setDirection(ECMessage.Direction.SEND);
                // 创建一个文本消息体，并添加到消息对象中
                ECTextMessageBody msgBody = new ECTextMessageBody(nr);
                msg.setBody(msgBody);
            } else {
                // 组建一个待发送的ECMessage
                msg = ECMessage.createECMessage(ECMessage.Type.IMAGE);
                //设置消息的属性：发出者，接受者，发送时间等
                msg.setForm(loginZh);
                msg.setMsgTime(System.currentTimeMillis());
                L.v("发送者：" + loginZh + " 接受者：" + jszZh);
                // 设置消息接收者
                msg.setTo(jszZh);
                msg.setSessionId(jszZh);
                // 设置消息发送类型（发送或者接收）
                msg.setDirection(ECMessage.Direction.SEND);
                ECImageMessageBody imgBody = new ECImageMessageBody();
                // 设置附件名
                imgBody.setFileName(path);
                String ext = path.substring(path.lastIndexOf(".") + 1, path.length());
                // 设置附件扩展名
                imgBody.setFileExt(ext);
                // 设置附件本地路径
                imgBody.setLocalUrl(path);
                msg.setBody(imgBody);
                msg.setUserData(nr);
                //                msg.setUserData(arg0);

                //设置进度条风格，风格为圆形，旋转的 
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //设置ProgressDialog 标题 
                dialog.setTitle("图片上传进度框");
                //设置ProgressDialog 提示信息 
                dialog.setMessage("上传进度");
                //设置ProgressDialog 标题图标 
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                //设置ProgressDialog的最大进度 
                dialog.setMax(100);
                //设置ProgressDialog 是否可以按退回按键取消 
                dialog.setCancelable(true);
            }

            // 或者创建一个创建附件消息体
            // 比如我们发送SD卡里面的一个Tony_2015.zip文件
            //            ECFileMessageBody msgBody  = new ECFileMessageBody();
            //            // 设置附件名
            //            msgBody.setFileName("Tony_2015.zip");
            //            // 设置附件扩展名
            //            msgBody.setFileExt(zip);
            //            // 设置附件本地路径
            //            msgBody.setLocalUrl("../Tony_2015.zip");
            //            // 设置附件长度
            //            msgBody.setLength("$Tony_2015.zip文件大小");

            // 将消息体存放到ECMessage中
            // 调用SDK发送接口发送消息到服务器
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
                @Override
                public void onSendMessageComplete(ECError error, ECMessage message) {
                    // 处理消息发送结果
                    if (message == null) {
                        return;
                    }
                    // 将发送的消息更新到本地数据库并刷新UI
                }

                @Override
                public void onProgress(String msgId, int totalByte, int progressByte) {
                    // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                    L.v(progressByte + ":" + totalByte + ":" + msgId);

                    if (totalByte == progressByte) {
                        dialog.dismiss();
                    } else {
                        dialog.show();
                    }
                    //显示 
                    dialog.setProgress(progressByte/totalByte*100);
                    //设置ProgressDialog的当前进度 
                }

            });
        } catch (Exception e) {
            // 处理发送异常
            Log.e("ECSDK_Demo", "send message fail , e=" + e.getMessage());
            Log.v("dddd", "发送异常");
            e.printStackTrace();
        }
    }

    /**
     * 接收消息
     * 
     * @param msg
     */
    public void getMsg(ECMessage msg) {
        if (msg == null) {
            return;
        }
        if (isBackgroundRunning()) {
            //状态栏显示信息
            showNotification("您有新的艺论消息");
            //          MyApplication.getInstance().exit();
        } else {
            //提示音
            sp.play(music, 1, 1, 0, 0, 1);
        }
        // 接收到的IM消息，根据IM消息类型做不同的处理(IM消息类型：ECMessage.Type)
        ECMessage.Type type = msg.getType();
        if (type == ECMessage.Type.TXT) {
            // 在这里处理文本消息
            ECTextMessageBody textMessageBody = (ECTextMessageBody) msg.getBody();

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) PaseJson
                .paseJsonToObject(textMessageBody.getMessage());
            String XxType = PaseJson.getMapMsg(map, "type");
            if (XxType.equals("1")) {//代表是聊天消息
                BroadcaseUtil.sendBroadcase(BroadcaseUtil.XX_LT, this.getApplicationContext(),
                    textMessageBody.getMessage());
                XxLtDao ltDao = new XxLtDao(ReceiverService.this);
                ltDao.add(map);//保存信息到数据库中
            }
            L.v(textMessageBody.getMessage() + ":::" + msg.getForm() + ":" + msg.getMsgStatus());
            Toast.makeText(ReceiverService.this,
                textMessageBody.getMessage() + ":::" + msg.getForm() + ":" + msg.getMsgStatus(),
                Toast.LENGTH_LONG).show();
        } else {

            String thumbnailFileUrl = null;
            String remoteUrl = null;
            if (type == ECMessage.Type.FILE) {
                // 在这里处理附件消息
                ECFileMessageBody fileMsgBody = (ECFileMessageBody) msg.getBody();
                // 获得下载地址
                remoteUrl = fileMsgBody.getRemoteUrl();
            } else if (type == ECMessage.Type.IMAGE) {
                // 在这里处理图片消息
                ECImageMessageBody imageMsgBody = (ECImageMessageBody) msg.getBody();
                // 获得缩略图地址
                thumbnailFileUrl = imageMsgBody.getThumbnailFileUrl();
                // 获得原图地址
                remoteUrl = imageMsgBody.getRemoteUrl();
                L.v(remoteUrl + ";:::");
                JSONObject object;
                try {
                    object = new JSONObject(msg.getUserData());
                    L.v(msg.getUserData());
                    object.put("send_content", thumbnailFileUrl);
                    BroadcaseUtil.sendBroadcase(BroadcaseUtil.XX_LT, this.getApplicationContext(),
                        object.toString());
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) PaseJson
                        .paseJsonToObject(object.toString());
                    XxLtDao ltDao = new XxLtDao(ReceiverService.this);
                    ltDao.add(map);//保存信息到数据库中
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (type == ECMessage.Type.VOICE) {
                // 在这里处理语音消息
                ECVoiceMessageBody voiceMsgBody = (ECVoiceMessageBody) msg.getBody();
                // 获得下载地址
                remoteUrl = voiceMsgBody.getRemoteUrl();
            } else {
                Log.e("ECSDK_Demo", "Can't handle msgType=" + type.name() + " , then ignore.");
                // 后续还会支持（地址位置、视频以及自定义等消息类型）
            }

            if (TextUtils.isEmpty(remoteUrl)) {
                return;
            }
            if (!TextUtils.isEmpty(thumbnailFileUrl)) {
                // 先下载缩略图
            } else {
                // 下载附件
            }
        }
        // 根据不同类型处理完消息之后，将消息序列化到本地存储（sqlite）
        // 通知UI有新消息到达
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
}
