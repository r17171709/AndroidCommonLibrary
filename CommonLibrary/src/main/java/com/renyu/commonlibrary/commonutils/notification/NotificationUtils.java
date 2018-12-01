package com.renyu.commonlibrary.commonutils.notification;

import android.app.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import com.blankj.utilcode.util.Utils;
import com.renyu.commonlibrary.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class NotificationUtils {
    private volatile static NotificationUtils center = null;
    private static NotificationManager manager = null;

    private static HashMap<String, NotificationCompat.Builder> builders;

    private static HashMap<String, Integer> lastPercentMaps;

    public static final String KEY_TEXT_REPLY = "key_text_reply";

    public static final String groupId = "channel_group_default";
    public static final String groupName = "channel_group_name_default";
    public static final String groupDownloadId = "channel_group_download";
    public static final String groupDownloadName = "channel_group_name_download";

    public static final String channelDefaultId = "channel_default";
    public static final String channelDefaultName = "channel_name_default";
    public static final String channelDownloadId = "channel_download";
    public static final String channelDownloadName = "channel_name_download";

    /**
     * 通知栏中心调度
     *
     * @return
     */
    public static NotificationUtils getNotificationCenter() {
        if (center == null) {
            synchronized (NotificationUtils.class) {
                if (center == null) {
                    center = new NotificationUtils();
                    manager = (NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                        ArrayList<NotificationChannelGroup> groups = new ArrayList<>();
                        NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
                        groups.add(group);
                        NotificationChannelGroup group_download = new NotificationChannelGroup(groupDownloadId, groupDownloadName);
                        groups.add(group_download);
                        createNotificationGroups(groups);

                        NotificationChannel channel = new NotificationChannel(channelDefaultId, channelDefaultName, NotificationManager.IMPORTANCE_HIGH);
                        // 开启指示灯，如果设备有的话
                        channel.enableLights(true);
                        // 设置指示灯颜色
                        channel.setLightColor(ContextCompat.getColor(Utils.getApp(), R.color.colorPrimary));
                        // 是否在久按桌面图标时显示此渠道的通知
                        channel.setShowBadge(true);
                        // 设置是否应在锁定屏幕上显示此频道的通知
                        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
                        // 设置绕过免打扰模式
                        channel.setBypassDnd(true);
                        channel.setGroup(groupId);
                        createNotificationChannel(channel);

                        NotificationChannel channel_download = new NotificationChannel(channelDownloadId, channelDownloadName, NotificationManager.IMPORTANCE_LOW);
                        channel_download.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
                        channel_download.setGroup(groupDownloadId);
                        createNotificationChannel(channel_download);
                    }
                    builders = new HashMap<>();
                    lastPercentMaps = new HashMap<>();
                }
            }
        }
        return center;
    }

    private boolean checkContainId(int id) {
        Iterator iterator = builders.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (id == Integer.parseInt(entry.getKey().toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建group分组
     *
     * @param groups
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationGroups(ArrayList<NotificationChannelGroup> groups) {
        manager.createNotificationChannelGroups(groups);
    }

    /**
     * 创建渠道
     *
     * @param channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(NotificationChannel channel) {
        manager.createNotificationChannel(channel);
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
     * @param channelId
     * @param intent
     * @return
     */
    public NotificationCompat.Builder getSimpleBuilder(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId, Intent intent) {
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
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        // 保持通知不被移除
        builder.setOngoing(false);
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
     * @param channelId
     * @param timeout
     * @param intent
     * @return
     */
    public NotificationCompat.Builder getSimpleBuilderWithTimeout(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId, long timeout, Intent intent) {
        return getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent).setTimeoutAfter(timeout * 1000);
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
     * @param intent
     */
    public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Intent intent, String channelId) {
        createNormalNotification(ticker, title, content, color, smallIcon, largeIcon, intent, channelId, (int) (System.currentTimeMillis() / 1000));
    }

    public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Intent intent, String channelId, int notifyId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
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
     * @param intent
     */
    public void createButtonNotification(String ticker, String title, String content,
                                         int color, int smallIcon, int largeIcon,
                                         int actionIcon1, String actionTitle1, Class actionClass1,
                                         Intent intent, String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
        builder.addAction(actionIcon1, actionTitle1, PendingIntent.getActivity(Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(Utils.getApp(), actionClass1), PendingIntent.FLAG_UPDATE_CURRENT));
        manager.notify((int) (System.currentTimeMillis() / 1000), builder.build());
    }

    public void createButton2Notification(String ticker, String title, String content,
                                          int color, int smallIcon, int largeIcon,
                                          int actionIcon1, String actionTitle1, Class actionClass1,
                                          int actionIcon2, String actionTitle2, Class actionClass2,
                                          Intent intent, String channelId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
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
     * @param intent
     */
    public void createProgressNotification(String ticker, String title, String content,
                                           int color, int smallIcon, int largeIcon,
                                           int max, int progress,
                                           Intent intent, String channelId, int notifyId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
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
     * @param intent
     */
    public void createIndeterminateProgressNotification(String ticker, String title, String content,
                                                        int color, int smallIcon, int largeIcon,
                                                        Intent intent, String channelId, int notifyId) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
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
     * @param intent
     */
    public void createBigTextNotification(String ticker, String title, String content,
                                          int color, int smallIcon, int largeIcon,
                                          String bigText, String bigContentTitle, String summaryText,
                                          Intent intent, String channelId) {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(bigText);
        style.setBigContentTitle(bigContentTitle);
        style.setSummaryText(summaryText);
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
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
     * @param intent
     */
    public void createBigImageNotification(String ticker, String title, String content,
                                           int color, int smallIcon, int largeIcon,
                                           int bigLargeIcon, int bigPicture, String bigContentTitle, String summaryText,
                                           Intent intent, String channelId) {
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigLargeIcon(BitmapFactory.decodeResource(Utils.getApp().getResources(), bigLargeIcon));
        style.bigPicture(BitmapFactory.decodeResource(Utils.getApp().getResources(), bigPicture));
        style.setBigContentTitle(bigContentTitle);
        style.setSummaryText(summaryText);
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
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
     * @param intent
     */
    public void createTextListNotification(String ticker, String title, String content,
                                           int color, int smallIcon, int largeIcon,
                                           ArrayList<String> linesString, String bigContentTitle, String summaryText,
                                           Intent intent, String channelId, int notifyId) {
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (String s : linesString) {
            style.addLine(s);
        }
        style.setBigContentTitle(bigContentTitle);
        style.setSummaryText(summaryText);
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
        builder.setStyle(style);
        manager.notify(notifyId, builder.build());
    }

    public NotificationCompat.MessagingStyle createMessagingStyleNotification(String ticker, String title, String content,
                                                                              int color, int smallIcon, int largeIcon,
                                                                              String userDisplayName, String conversationTitle,
                                                                              ArrayList<NotificationCompat.MessagingStyle.Message> messages,
                                                                              Intent intent, String channelId, int notifyId) {
        NotificationCompat.MessagingStyle style = new NotificationCompat
                .MessagingStyle(userDisplayName)
                .setConversationTitle(conversationTitle);
        for (NotificationCompat.MessagingStyle.Message message : messages) {
            style.addMessage(message);
        }
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
        builder.setStyle(style);
        manager.notify(notifyId, builder.build());
        return style;
    }

    public void updateMessagingStyleNotification(String ticker, String title, String content,
                                                 int color, int smallIcon, int largeIcon,
                                                 NotificationCompat.MessagingStyle style,
                                                 ArrayList<NotificationCompat.MessagingStyle.Message> messages,
                                                 Intent intent, String channelId, int notifyId) {
        for (NotificationCompat.MessagingStyle.Message message : messages) {
            style.addMessage(message);
        }
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
        builder.setStyle(style);
        manager.notify(notifyId, builder.build());
    }

    public void createRemoteInput(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId, Intent intent, int notifyId,
                                  String replyLabel, Class receiverClass, int replyIcon, CharSequence replyTitle) {
        NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel(replyLabel).build();
        Intent intent1 = new Intent(Utils.getApp(), receiverClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Utils.getApp(), 0, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(replyIcon, replyTitle, pendingIntent).addRemoteInput(remoteInput).build();
        builder.addAction(replyAction);
        manager.notify(notifyId, builder.build());
    }

    /**
     * 开启新下载通知
     *
     * @param id
     * @param title
     * @param color
     * @param smallIcon
     * @param largeIcon
     */
    public void createDownloadNotification(int id, String ticker, String title, int color, int smallIcon, int largeIcon) {
        if (checkContainId(id)) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), NotificationUtils.channelDownloadId);
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(Utils.getApp().getResources(), largeIcon));
        builder.setWhen(System.currentTimeMillis());
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setColor(color);
        builder.setColorized(true);
        builder.setOngoing(true);
        builder.setTicker(ticker);
        builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        builder.setContentTitle(title);
        builder.setProgress(100, 0, false);
        builder.setAutoCancel(false);
        builder.setShowWhen(false);
        builder.setContentIntent(PendingIntent.getBroadcast(Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        manager.notify(id, builder.build());
        builders.put("" + id, builder);
        lastPercentMaps.put("" + id, 0);
    }

    /**
     * 更新相应id的通知栏
     *
     * @param id
     * @param persent
     * @param title
     */
    public void updateDownloadNotification(int id, int persent, String title) {
        if (!checkContainId(id)) {
            return;
        }
        if (lastPercentMaps.containsKey("" + id)) {
            if (persent - 10 > lastPercentMaps.get("" + id).intValue()) {
                lastPercentMaps.put("" + id, persent);
            } else {
                return;
            }
        } else {
            return;
        }
        NotificationCompat.Builder builder = builders.get("" + id);
        builder.setContentTitle(title);
        builder.setContentText("已下载" + persent + "%");
        builder.setProgress(100, persent, false);
        manager.notify(id, builder.build());
    }

    /**
     * 最后提示
     *
     * @param id
     */
    public void showEndNotification(int id) {
        if (!checkContainId(id)) {
            return;
        }
        NotificationCompat.Builder builder = builders.get("" + id);
        builder.setContentText("下载完成");
        builder.setProgress(100, 100, false);
        manager.notify(id, builder.build());
        new Handler().postDelayed(() -> cancelNotification(id), 1000);
    }

    /**
     * 关闭通知
     *
     * @param id
     */
    public void cancelNotification(int id) {
        manager.cancel(id);
        builders.remove("" + id);
        lastPercentMaps.remove("" + id);
    }

    /**
     * 取消全部通知
     */
    public void cancelAll() {
        manager.cancelAll();
        builders.clear();
        lastPercentMaps.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChannel(String channelId) {
        manager.deleteNotificationChannel(channelId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationGroup(String groupId) {
        manager.deleteNotificationChannelGroup(groupId);
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
     */
    public void showStartForeground(Service service, String ticker, String title, String content, int color, int smallIcon, int largeIcon, int id) {
        NotificationCompat.Builder builder = getSimpleBuilder(
                ticker,
                title,
                content,
                color,
                smallIcon,
                largeIcon,
                NotificationUtils.channelDownloadId,
                new Intent());
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
    }

    /**
     * 跳转通知设置界面
     */
    public static void openNotification() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", Utils.getApp().getPackageName(), null));
        Utils.getApp().startActivity(localIntent);
    }

    /**
     * 打开通知监听设置页面
     */
    public static void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            Utils.getApp().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测通知监听服务是否被授权
     * @param context
     * @return
     */
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(Utils.getApp());
        return packageNames.contains(context.getPackageName());
    }

    /**
     * 把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作
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
}