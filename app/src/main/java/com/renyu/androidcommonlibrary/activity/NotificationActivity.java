package com.renyu.androidcommonlibrary.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.Utils;
import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.receiver.RemoteInputReceiver;
import com.renyu.androidcommonlibrary.service.NotificationCollectorService;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.notification.NotificationCenterManager;

import java.util.ArrayList;

/**
 * Created by renyu on 2018/1/29.
 */

public class NotificationActivity extends BaseActivity {
    Uri uri1 = null;
    Uri uri2 = null;

    NotificationCompat.MessagingStyle style;

    /**
     * 创建渠道
     */
    private void createChannels() {
        // 音频资源
        uri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.msg);
        uri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring_user_message_high);

        createChannel("channel_default1", "channel_default1", R.color.colorPrimary, uri1);
        createChannel("channel_default2", "channel_default2", R.color.colorPrimary, uri2);
        createChannel("channel_default3", "channel_default3", R.color.colorPrimary, null);
    }

    private void createChannel(String id, String name, int lightColor, Uri soundUri) {
        // 设置是在第一次创建通道时设置的，除非通过手动执行全新安装或清除数据，否则不会进行修改
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            NotificationCenterManager notificationCenterManager = NotificationCenterManager.getManager();
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // 开启指示灯，如果设备有的话
            channel.enableLights(true);
            // 设置指示灯颜色
            channel.setLightColor(ContextCompat.getColor(Utils.getApp(), lightColor));
            // 是否在久按桌面图标时显示此渠道的通知
            channel.setShowBadge(true);
            // 设置是否应在锁定屏幕上显示此频道的通知
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            // 设置绕过免打扰模式
            channel.setBypassDnd(true);
            if (soundUri != null) {
                channel.setSound(soundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            }
            notificationCenterManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void initParams() {
        TextView tv_normal = findViewById(R.id.tv_normal);
        TextView tv_channel1 = findViewById(R.id.tv_channel1);
        TextView tv_channel2 = findViewById(R.id.tv_channel2);
        TextView tv_channel1_delete = findViewById(R.id.tv_channel1_delete);
        TextView tv_channel2_delete = findViewById(R.id.tv_channel2_delete);
        TextView tv_send_messagestyle = findViewById(R.id.tv_send_messagestyle);
        TextView tv_update_messagestyle = findViewById(R.id.tv_update_messagestyle);
        TextView tv_send_messagestyle8 = findViewById(R.id.tv_send_messagestyle8);
        TextView tv_send_remoteinput = findViewById(R.id.tv_send_remoteinput);
        TextView tv_notificationlistener = findViewById(R.id.tv_notificationlistener);

        // 询问用户开启通知权限
        if (!NotificationCenterManager.isNotificationEnabled()) {
            NotificationCenterManager.openNotification();
        }

        if (NotificationCenterManager.isNotificationListenerEnabled(this)) {
            NotificationCenterManager.toggleNotificationListenerService(NotificationCollectorService.class);
        }

        // 创建渠道
        createChannels();

        tv_normal.setOnClickListener(v -> {
            NotificationCenterManager notificationCenterManager = NotificationCenterManager.getManager();
            NotificationCompat.Builder builder = notificationCenterManager.getSimpleBuilder(
                    "ticker",
                    "title3",
                    "content3",
                    Color.RED,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    null,
                    new Intent(),
                    "channel_default3");
            notificationCenterManager.getNotificationManager().notify(103, builder.build());
        });

        tv_channel1.setOnClickListener(v -> {
            NotificationCenterManager notificationCenterManager = NotificationCenterManager.getManager();
            NotificationCompat.Builder builder = notificationCenterManager.getSimpleBuilder(
                    "ticker",
                    "title1",
                    "content1",
                    Color.RED,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    uri1,
                    new Intent(),
                    "channel_default1");
            notificationCenterManager.getNotificationManager().notify(100, builder.build());
        });

        tv_channel2.setOnClickListener(v -> {
            NotificationCenterManager notificationCenterManager = NotificationCenterManager.getManager();
            NotificationCompat.Builder builder = notificationCenterManager.getSimpleBuilder(
                    "ticker",
                    "title2",
                    "content2",
                    Color.RED,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    uri2,
                    new Intent(),
                    "channel_default2");
            notificationCenterManager.getNotificationManager().notify(101, builder.build());
        });

        tv_channel1_delete.setOnClickListener((view) -> {
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                // 整个渠道甚至所有渠道都被删除，不能再次使用
                NotificationCenterManager.getManager().deleteNotificationChannel("channel_default1");
            }
        });

        tv_channel2_delete.setOnClickListener((view) -> {
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                // 整个渠道甚至所有渠道都被删除，不能再次使用
                NotificationCenterManager.getManager().deleteNotificationChannel("channel_default2");
            }
        });

        tv_send_messagestyle.setOnClickListener(v -> {
            ArrayList<NotificationCompat.MessagingStyle.Message> messages = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Person.Builder personBuilder = new Person.Builder();
                personBuilder.setName("i:" + i);
                NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(i + " " + System.currentTimeMillis(), System.currentTimeMillis(), personBuilder.build());
                messages.add(message);
            }
            style = NotificationCenterManager.getManager().createMessagingStyleNotification(
                    "ticker",
                    "channel1",
                    "content",
                    Color.RED,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    "demo",
                    "2 new messages wtih ",
                    messages,
                    104,
                    null,
                    new Intent(),
                    "channel_default3");
        });

        tv_update_messagestyle.setOnClickListener(v -> {
            ArrayList<NotificationCompat.MessagingStyle.Message> messages = new ArrayList<>();
            Person.Builder personBuilder = new Person.Builder();
            personBuilder.setName("66");
            NotificationCompat.MessagingStyle.Message message6 = new NotificationCompat.MessagingStyle.Message("6 " + System.currentTimeMillis(), System.currentTimeMillis(), personBuilder.build());
            messages.add(message6);
            NotificationCenterManager.getManager().updateMessagingStyleNotification(
                    "ticker",
                    "channel1",
                    "content",
                    Color.RED,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    style,
                    messages,
                    104,
                    null,
                    new Intent(),
                    "channel_default3");
        });

        tv_send_messagestyle8.setOnClickListener(v -> {
            if (Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
                Notification.Builder builder = createNewNotificationMessagingStyle("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                builder.setNumber(10);
                NotificationCenterManager.getManager().getNotificationManager().notify(104, builder.build());
            }
        });

        tv_send_remoteinput.setOnClickListener(v -> {
            NotificationCenterManager.getManager().createRemoteInput(
                    "ticker",
                    "channel1",
                    "content",
                    Color.RED,
                    R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,
                    105,
                    null,
                    new Intent(),
                    "快速回复",
                    RemoteInputReceiver.class,
                    R.mipmap.ic_launcher,
                    "请输入回复的内容",
                    "channel_default3");
        });

        tv_notificationlistener.setOnClickListener((view) -> {
            if (!NotificationCenterManager.isNotificationListenerEnabled(this)) {
                NotificationCenterManager.openNotificationListenSettings(this);
            }
            NotificationCenterManager.toggleNotificationListenerService(NotificationCollectorService.class);
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_notification;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在通知堆积的时候可能会重新启动MainActivity
        if (((ExampleApp) getApplication()).openClassNames.contains(getLocalClassName())) {
            NotificationCenterManager.getManager().cancelAll();
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getSimpleBuilder(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId) {
        Notification.Builder builder = new Notification.Builder(this, channelId);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        Intent intent = new Intent(NotificationActivity.this, RemoteInputReceiver.class);
        builder.setContentIntent(PendingIntent.getBroadcast(this, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        builder.setColor(color);
        // 设置和启用通知的背景颜色（只能在用户必须一眼就能看到的持续任务的通知中使用此功能）
        builder.setColorized(true);
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), largeIcon));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setDefaults(Notification.DEFAULT_ALL);
        // 保持通知不被移除
        builder.setOngoing(false);
        return builder;
    }

    @TargetApi(Build.VERSION_CODES.P)
    public Notification.Builder createNewNotificationMessagingStyle(String ticker, String title, String content, int color, int smallIcon, int largeIcon) {
        android.app.Person.Builder personBuilder = new android.app.Person.Builder();
        personBuilder.setName("66");
        android.app.Person.Builder historicPersonBuilder = new android.app.Person.Builder();
        historicPersonBuilder.setName("77");
        Notification.MessagingStyle style = new Notification.MessagingStyle(personBuilder.build());
        style.setConversationTitle("How are you?");
        style.addMessage("Hello: " + System.currentTimeMillis(), 0L, personBuilder.build());
        style.addMessage(new Notification.MessagingStyle.Message("image", 0, personBuilder.build())
                .setData("image/png", Uri.parse("http://example.com/image.png")));
        style.addHistoricMessage(new Notification.MessagingStyle.Message("Historic Message - not visible", 0, historicPersonBuilder.build()));
        Notification.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, "channel_default3");
        builder.setStyle(style);
        return builder;
    }
}
