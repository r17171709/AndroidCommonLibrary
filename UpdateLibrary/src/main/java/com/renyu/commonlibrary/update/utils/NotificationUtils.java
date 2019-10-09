package com.renyu.commonlibrary.update.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotificationUtils {
    private volatile static NotificationUtils center = null;
    private static NotificationManager manager = null;

    private static HashMap<String, NotificationCompat.Builder> builders;

    private static HashMap<String, Integer> lastPercentMaps;

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
                }
            }
        }
        return center;
    }

    private NotificationUtils() {
        manager = (NotificationManager) com.blankj.utilcode.util.Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            NotificationChannel channel_download = new NotificationChannel("update", "升级", NotificationManager.IMPORTANCE_LOW);
            channel_download.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            createNotificationChannel(channel_download);
        }
        builders = new HashMap<>();
        lastPercentMaps = new HashMap<>();
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
     * 创建渠道
     *
     * @param channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(NotificationChannel channel) {
        manager.createNotificationChannel(channel);
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(com.blankj.utilcode.util.Utils.getApp(), "update");
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(com.blankj.utilcode.util.Utils.getApp().getResources(), largeIcon));
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
        builder.setContentIntent(PendingIntent.getBroadcast(com.blankj.utilcode.util.Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(com.blankj.utilcode.util.Utils.getApp(), "update");
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(PendingIntent.getBroadcast(com.blankj.utilcode.util.Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        builder.setColor(Color.WHITE);
        // 设置和启用通知的背景颜色（只能在用户必须一眼就能看到的持续任务的通知中使用此功能）
        builder.setColorized(true);
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(Utils.getApp().getResources(), largeIcon));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        // 保持通知不被移除
        builder.setOngoing(true);
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