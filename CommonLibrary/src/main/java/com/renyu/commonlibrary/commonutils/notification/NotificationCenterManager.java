package com.renyu.commonlibrary.commonutils.notification;

import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.blankj.utilcode.util.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class NotificationCenterManager {
    public static final String KEY_TEXT_REPLY = "key_text_reply";

    private static volatile NotificationCenterManager notificationCenterManager;
    private NotificationManager manager;

    public static NotificationCenterManager getManager() {
        if (notificationCenterManager == null) {
            synchronized (NotificationCenterManager.class) {
                if (notificationCenterManager == null) {
                    notificationCenterManager = new NotificationCenterManager();
                }
            }
        }
        return notificationCenterManager;
    }

    private NotificationCenterManager() {
        manager = (NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 基本通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param soundUri
     * @param intent
     * @param channelId
     * @return
     */
    public NotificationCompat.Builder getSimpleBuilder(
            String ticker,
            String title,
            String content,
            int color,
            int smallIcon,
            int largeIcon,
            Uri soundUri,
            Intent intent,
            String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), channelId);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(PendingIntent.getBroadcast(Utils.getApp(), (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        builder.setColor(color);
        // 设置和启用通知的背景颜色（只能在用户必须一眼就能看到的持续任务的通知中使用此功能）
        builder.setColorized(true);
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(Utils.getApp().getResources(), largeIcon));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (soundUri != null) {
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
            builder.setSound(soundUri);
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        }
        // 显示消息时间
//        builder.setShowWhen(true);
        // 保持通知不被移除
        builder.setOngoing(false);
        // 浮动通知
//        builder.setFullScreenIntent(PendingIntent.getBroadcast(Utils.getApp(), (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT), true);
        // 锁屏通知
//        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        // 自定义RemoteViews
//        builder.setCustomBigContentView()
//        builder.setCustomContentView()
        return builder;
    }

    /**
     * 设置带有超时销毁的通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param soundUri
     * @param intent
     * @param timeout
     * @param channelId
     * @return
     */
    public NotificationCompat.Builder getSimpleBuilderWithTimeout(
            String ticker,
            String title,
            String content,
            int color,
            int smallIcon,
            int largeIcon,
            Uri soundUri,
            Intent intent,
            long timeout,
            String channelId) {
        return getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId).setTimeoutAfter(timeout * 1000);
    }

    /**
     * 普通通知栏
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param channelId
     * @param soundUri
     * @param intent
     */
    public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Uri soundUri, Intent intent, String channelId) {
        createNormalNotification(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, (int) (System.currentTimeMillis() / 1000), channelId);
    }

    public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Uri soundUri, Intent intent, int notifyId, String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        manager.notify(notifyId, builder.build());
    }

    /**
     * 创建有按钮的通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param actionIcon1
     * @param actionTitle1
     * @param actionClass1
     * @param soundUri
     * @param intent
     * @param channelId
     */
    public void createButtonNotification(String ticker,
                                         String title,
                                         String content,
                                         int color,
                                         int smallIcon,
                                         int largeIcon,
                                         int actionIcon1,
                                         String actionTitle1,
                                         Class actionClass1,
                                         Uri soundUri,
                                         Intent intent,
                                         String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.addAction(actionIcon1, actionTitle1, PendingIntent.getActivity(Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(Utils.getApp(), actionClass1), PendingIntent.FLAG_UPDATE_CURRENT));
        manager.notify((int) (System.currentTimeMillis() / 1000), builder.build());
    }

    public void createButton2Notification(String ticker,
                                          String title,
                                          String content,
                                          int color,
                                          int smallIcon,
                                          int largeIcon,
                                          int actionIcon1,
                                          String actionTitle1,
                                          Class actionClass1,
                                          int actionIcon2,
                                          String actionTitle2,
                                          Class actionClass2,
                                          Uri soundUri,
                                          Intent intent,
                                          String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.addAction(actionIcon1, actionTitle1, PendingIntent.getActivity(Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(Utils.getApp(), actionClass1), PendingIntent.FLAG_UPDATE_CURRENT));
        builder.addAction(actionIcon2, actionTitle2, PendingIntent.getActivity(Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(Utils.getApp(), actionClass2), PendingIntent.FLAG_UPDATE_CURRENT));
        manager.notify((int) (System.currentTimeMillis() / 1000), builder.build());
    }

    /**
     * 精确进度条通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param max
     * @param progress
     * @param soundUri
     * @param intent
     * @param notifyId
     * @param channelId
     */
    public void createProgressNotification(String ticker,
                                           String title,
                                           String content,
                                           int color,
                                           int smallIcon,
                                           int largeIcon,
                                           int max,
                                           int progress,
                                           Uri soundUri,
                                           Intent intent,
                                           int notifyId,
                                           String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setProgress(max, progress, false);
        manager.notify(notifyId, builder.build());
    }

    /**
     * 模糊进度条通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param soundUri
     * @param intent
     * @param notifyId
     * @param channelId
     */
    public void createIndeterminateProgressNotification(String ticker,
                                                        String title,
                                                        String content,
                                                        int color,
                                                        int smallIcon,
                                                        int largeIcon,
                                                        Uri soundUri,
                                                        Intent intent,
                                                        int notifyId,
                                                        String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setProgress(0, 0, true);
        manager.notify(notifyId, builder.build());
    }

    /**
     * 长文本通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param bigText
     * @param bigContentTitle
     * @param summaryText
     * @param soundUri
     * @param intent
     * @param channelId
     */
    public void createBigTextNotification(String ticker,
                                          String title,
                                          String content,
                                          int color,
                                          int smallIcon,
                                          int largeIcon,
                                          String bigText,
                                          String bigContentTitle,
                                          String summaryText,
                                          Uri soundUri,
                                          Intent intent,
                                          String channelId) {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(bigText);
        style.setBigContentTitle(bigContentTitle);
        style.setSummaryText(summaryText);
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setStyle(style);
        manager.notify((int) (System.currentTimeMillis() / 1000), builder.build());
    }

    /**
     * 大图通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param bigLargeIcon
     * @param bigPicture
     * @param bigContentTitle
     * @param summaryText
     * @param soundUri
     * @param intent
     * @param channelId
     */
    public void createBigImageNotification(String ticker,
                                           String title,
                                           String content,
                                           int color,
                                           int smallIcon,
                                           int largeIcon,
                                           int bigLargeIcon,
                                           int bigPicture,
                                           String bigContentTitle,
                                           String summaryText,
                                           Uri soundUri,
                                           Intent intent,
                                           String channelId) {
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigLargeIcon(BitmapFactory.decodeResource(Utils.getApp().getResources(), bigLargeIcon));
        style.bigPicture(BitmapFactory.decodeResource(Utils.getApp().getResources(), bigPicture));
        style.setBigContentTitle(bigContentTitle);
        style.setSummaryText(summaryText);
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setStyle(style);
        manager.notify((int) (System.currentTimeMillis() / 1000), builder.build());
    }

    /**
     * 列表型通知
     *
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param linesString
     * @param bigContentTitle
     * @param summaryText
     * @param soundUri
     * @param intent
     * @param notifyId
     * @param channelId
     */
    public void createTextListNotification(String ticker,
                                           String title,
                                           String content,
                                           int color,
                                           int smallIcon,
                                           int largeIcon,
                                           ArrayList<String> linesString,
                                           String bigContentTitle,
                                           String summaryText,
                                           Uri soundUri,
                                           Intent intent,
                                           int notifyId,
                                           String channelId) {
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (String s : linesString) {
            style.addLine(s);
        }
        style.setBigContentTitle(bigContentTitle);
        style.setSummaryText(summaryText);
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setStyle(style);
        manager.notify(notifyId, builder.build());
    }

    public NotificationCompat.MessagingStyle createMessagingStyleNotification(
            String ticker,
            String title,
            String content,
            int color,
            int smallIcon,
            int largeIcon,
            String userDisplayName,
            String conversationTitle,
            ArrayList<NotificationCompat.MessagingStyle.Message> messages,
            int notifyId,
            Uri soundUri,
            Intent intent,
            String channelId) {
        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(userDisplayName).setConversationTitle(conversationTitle);
        for (NotificationCompat.MessagingStyle.Message message : messages) {
            style.addMessage(message);
        }
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setStyle(style);
        manager.notify(notifyId, builder.build());
        return style;
    }

    public void updateMessagingStyleNotification(
            String ticker,
            String title,
            String content,
            int color,
            int smallIcon,
            int largeIcon,
            NotificationCompat.MessagingStyle style,
            ArrayList<NotificationCompat.MessagingStyle.Message> messages,
            int notifyId,
            Uri soundUri,
            Intent intent,
            String channelId) {
        for (NotificationCompat.MessagingStyle.Message message : messages) {
            style.addMessage(message);
        }
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, soundUri, intent, channelId);
        builder.setStyle(style);
        manager.notify(notifyId, builder.build());
    }

    public void createRemoteInput(
            String ticker,
            String title,
            String content,
            int color,
            int smallIcon,
            int largeIcon,
            int notifyId,
            Uri soundUri,
            Intent intent,
            String replyLabel,
            Class receiverClass,
            int replyIcon,
            CharSequence replyTitle,
            String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(
                ticker,
                title,
                content,
                color,
                smallIcon,
                largeIcon,
                soundUri,
                intent,
                channelId);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel(replyLabel).build();
        Intent intent1 = new Intent(Utils.getApp(), receiverClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Utils.getApp(), 0, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(replyIcon, replyTitle, pendingIntent).addRemoteInput(remoteInput).build();
        builder.addAction(replyAction);
        manager.notify(notifyId, builder.build());
    }

    /**
     * 创建组
     *
     * @param groups
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationGroups(ArrayList<NotificationChannelGroup> groups) {
        manager.createNotificationChannelGroups(groups);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationGroup(NotificationChannelGroup group) {
        manager.createNotificationChannelGroup(group);
    }

    /**
     * 创建渠道
     *
     * @param channels
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(ArrayList<NotificationChannel> channels) {
        manager.createNotificationChannels(channels);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(NotificationChannel channel) {
        manager.createNotificationChannel(channel);
    }

    /**
     * 删除渠道
     *
     * @param channelId
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChannel(String channelId) {
        manager.deleteNotificationChannel(channelId);
    }

    /**
     * 删除组
     *
     * @param groupId
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationGroup(String groupId) {
        manager.deleteNotificationChannelGroup(groupId);
    }

    /**
     * 取消全部通知
     */
    public void cancelAll() {
        manager.cancelAll();
    }

    /**
     * 关闭通知
     *
     * @param id
     */
    public void cancelNotification(int id) {
        manager.cancel(id);
    }

    public NotificationManager getNotificationManager() {
        return manager;
    }

    /**
     * 判断是否开启通知权限
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            String CHECK_OP_NO_THROW = "checkOpNoThrow";
            String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

            AppOpsManager mAppOps = (AppOpsManager) Utils.getApp().getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = Utils.getApp().getApplicationInfo();
            String pkg = Utils.getApp().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass;
            /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return NotificationManagerCompat.from(Utils.getApp()).areNotificationsEnabled();
        }
    }

    /**
     * 跳转通知设置界面
     */
    public static void openNotification() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, Utils.getApp().getPackageName());
                Utils.getApp().startActivity(localIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                localIntent.putExtra("app_package", Utils.getApp().getPackageName());
                localIntent.putExtra("app_uid", Utils.getApp().getApplicationInfo().uid);
                Utils.getApp().startActivity(localIntent);
            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                localIntent.setData(Uri.fromParts("package", Utils.getApp().getPackageName(), null));
                Utils.getApp().startActivity(localIntent);
            }
        } catch (Exception e) {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", Utils.getApp().getPackageName(), null));
            Utils.getApp().startActivity(localIntent);
        }
    }

    /**
     * 打开通知监听设置页面
     */
    public static void openNotificationListenSettings(Context context) {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测通知监听服务是否被授权
     *
     * @param context
     * @return
     */
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(Utils.getApp());
        return packageNames.contains(context.getPackageName());
    }

    /**
     * 把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作
     *
     * @param serviceClass
     */
    public static void toggleNotificationListenerService(Class serviceClass) {
        PackageManager pm = Utils.getApp().getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(Utils.getApp(), serviceClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(Utils.getApp(), serviceClass),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 8.0开启前台服务
     *
     * @param service
     * @param ticker
     * @param title
     * @param content
     * @param color
     * @param smallIcon
     * @param largeIcon
     * @param id
     * @param channelId
     */
    public void showStartForeground(Service service, String ticker, String title, String content, int color, int smallIcon, int largeIcon, int id, String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(
                ticker,
                title,
                content,
                color,
                smallIcon,
                largeIcon,
                null,
                new Intent(),
                channelId);
        builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setColor(Color.WHITE);
        service.startForeground(id, builder.build());
    }

    /**
     * 8.0关闭前台服务
     *
     * @param service
     * @param id
     */
    public void hideStartForeground(Service service, int id) {
        service.stopForeground(true);
        manager.cancel(id);
    }
}
