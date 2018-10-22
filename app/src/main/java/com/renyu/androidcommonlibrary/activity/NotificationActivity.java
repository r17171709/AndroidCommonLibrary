package com.renyu.androidcommonlibrary.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.Person;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.receiver.RemoteInputReceiver;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.notification.NotificationUtils;

import java.util.ArrayList;

/**
 * Created by renyu on 2018/1/29.
 */

public class NotificationActivity extends BaseActivity {

    TextView tv_normal;
    TextView tv_channel1;
    TextView tv_channel2;
    TextView tv_send_messagestyle;
    TextView tv_update_messagestyle;
    TextView tv_send_messagestyle8;
    TextView tv_send_remoteinput;

    NotificationManager manager=null;

    NotificationCompat.MessagingStyle style;

    @Override
    public void initParams() {
        tv_normal = findViewById(R.id.tv_normal);
        tv_channel1 = findViewById(R.id.tv_channel1);
        tv_channel2 = findViewById(R.id.tv_channel2);
        tv_send_messagestyle = findViewById(R.id.tv_send_messagestyle);
        tv_update_messagestyle = findViewById(R.id.tv_update_messagestyle);
        tv_send_messagestyle8 = findViewById(R.id.tv_send_messagestyle8);
        tv_send_remoteinput = findViewById(R.id.tv_send_remoteinput);

        // 询问用户开启通知权限
        if (!NotificationUtils.isNotificationEnabled()) {
            NotificationUtils.openNotification();
        }

        manager=(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            NotificationChannel channel = new NotificationChannel("channel3", "channelName3", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(ContextCompat.getColor(this, com.renyu.commonlibrary.R.color.colorPrimary));
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            channel.setBypassDnd(true);
            manager.createNotificationChannel(channel);
        }

        tv_normal.setOnClickListener(v -> {
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                {
                    NotificationCompat.Builder builder = NotificationUtils.getNotificationCenter().getSimpleBuilder("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, "channel3", new Intent());
                    NotificationUtils.getNotificationCenter().getNotificationManager().notify(100, builder.build());
                }

                {
                    NotificationCompat.Builder builder = NotificationUtils.getNotificationCenter().getSimpleBuilderWithTimeout("ticker", "channel2", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, "channel3", 10, new Intent());
                    NotificationUtils.getNotificationCenter().getNotificationManager().notify(101, builder.build());
                }
            }
            else {
                NotificationCompat.Builder builder = NotificationUtils.getNotificationCenter().getSimpleBuilder("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, NotificationUtils.channelDefaultId, new Intent());
                builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
                Uri sound=Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring_user_message_high);
                builder.setSound(sound);
                NotificationUtils.getNotificationCenter().getNotificationManager().notify(100, builder.build());
            }
        });

        tv_channel1.setOnClickListener(v -> {
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                NotificationUtils.getNotificationCenter().deleteNotificationChannel("Channel1");
            }
        });

        tv_channel2.setOnClickListener(v -> {
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                NotificationUtils.getNotificationCenter().deleteNotificationChannel("Channel2");
            }
        });

        tv_send_messagestyle.setOnClickListener(v -> {
            ArrayList<NotificationCompat.MessagingStyle.Message> messages = new ArrayList<>();
            for (int i = 0 ; i < 5 ; i++) {
                Person.Builder personBuilder = new Person.Builder();
                personBuilder.setName("i:"+i);
                NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(i+" "+System.currentTimeMillis(), System.currentTimeMillis(), personBuilder.build());
                messages.add(message);
            }
            style = NotificationUtils.getNotificationCenter().createMessagingStyleNotification("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, "demo", "2 new messages wtih ", messages, new Intent(), NotificationUtils.channelDefaultId, 104);
        });

        tv_update_messagestyle.setOnClickListener(v -> {
            ArrayList<NotificationCompat.MessagingStyle.Message> messages = new ArrayList<>();
            Person.Builder personBuilder = new Person.Builder();
            personBuilder.setName("66");
            NotificationCompat.MessagingStyle.Message message6 = new NotificationCompat.MessagingStyle.Message("6 "+System.currentTimeMillis(), System.currentTimeMillis(), personBuilder.build());
            messages.add(message6);
            NotificationUtils.getNotificationCenter().updateMessagingStyleNotification("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, style, messages, new Intent(), NotificationUtils.channelDefaultId, 104);
        });

        tv_send_messagestyle8.setOnClickListener(v -> {
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                Notification.Builder builder = createNewNotificationMessagingStyle("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                builder.setNumber(10);
                manager.notify(102, builder.build());
            }
        });

        tv_send_remoteinput.setOnClickListener(v -> {
            NotificationUtils.getNotificationCenter().createRemoteInput("ticker", "channel1", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, "channel3", new Intent(), 105,
                    "快速回复", RemoteInputReceiver.class, R.mipmap.ic_launcher, "请输入回复的内容");
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
        // android o 在通知堆积的时候会重新启动MainActivity
        if (((ExampleApp) getApplication()).openClassNames.contains(getLocalClassName())) {
            NotificationUtils.getNotificationCenter().cancelAll();
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getSimpleBuilder(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId) {
        Notification.Builder builder=new Notification.Builder(this, channelId);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder createNewNotificationMessagingStyle(String ticker, String title, String content, int color, int smallIcon, int largeIcon) {
        android.app.Person.Builder personBuilder = new android.app.Person.Builder();
        personBuilder.setName("66");
        android.app.Person.Builder historicPersonBuilder = new android.app.Person.Builder();
        historicPersonBuilder.setName("77");
        Notification.MessagingStyle style = new Notification.MessagingStyle(personBuilder.build());
        style.setConversationTitle("How are you?");
        style.addMessage("Hello: "+System.currentTimeMillis(), 0, personBuilder.build());
        style.addMessage(new Notification.MessagingStyle.Message("image", 0, personBuilder.build())
                .setData("image/png", Uri.parse("http://example.com/image.png")));
        style.addHistoricMessage(new Notification.MessagingStyle.Message("Historic Message - not visible", 0, historicPersonBuilder.build()));
        Notification.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelDefaultId);
        builder.setStyle(style);
        return builder;
    }
}
