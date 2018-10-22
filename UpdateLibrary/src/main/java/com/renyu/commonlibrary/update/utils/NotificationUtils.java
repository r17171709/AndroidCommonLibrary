package com.renyu.commonlibrary.update.utils;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.renyu.commonlibrary.update.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotificationUtils {
	private volatile static NotificationUtils center=null;
	private static NotificationManager manager=null;

	private static HashMap<String, NotificationCompat.Builder> builders;

	private static HashMap<String, Integer> lastPercentMaps;

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
	 * @return
	 */
	public static NotificationUtils getNotificationCenter() {
		if(center==null) {
			synchronized (NotificationUtils.class) {
				if (center==null) {
					center=new NotificationUtils();
					manager=(NotificationManager) com.blankj.utilcode.util.Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
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
						channel.setLightColor(ContextCompat.getColor(com.blankj.utilcode.util.Utils.getApp(), R.color.colorPrimary));
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
					builders=new HashMap<>();
					lastPercentMaps=new HashMap<>();
				}
			}
		}
		return center;
	}

	private boolean checkContainId(int id) {
		Iterator iterator=builders.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry= (Map.Entry) iterator.next();
			if (id==Integer.parseInt(entry.getKey().toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 创建group分组
	 * @param groups
	 */
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void createNotificationGroups(ArrayList<NotificationChannelGroup> groups) {
		manager.createNotificationChannelGroups(groups);
	}

	/**
	 * 创建渠道
	 * @param channel
	 */
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void createNotificationChannel(NotificationChannel channel) {
		manager.createNotificationChannel(channel);
	}

	/**
	 * 基本通知
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
		NotificationCompat.Builder builder=new NotificationCompat.Builder(com.blankj.utilcode.util.Utils.getApp(), channelId);
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setContentIntent(PendingIntent.getBroadcast(com.blankj.utilcode.util.Utils.getApp(), (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
		builder.setColor(color);
		// 设置和启用通知的背景颜色（只能在用户必须一眼就能看到的持续任务的通知中使用此功能）
		builder.setColorized(true);
		builder.setSmallIcon(smallIcon);
		builder.setLargeIcon(BitmapFactory.decodeResource(com.blankj.utilcode.util.Utils.getApp().getResources(), largeIcon));
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);
		builder.setPriority(NotificationCompat.PRIORITY_MAX);
		builder.setDefaults(NotificationCompat.DEFAULT_ALL);
		// 保持通知不被移除
		builder.setOngoing(false);
		return builder;
	}

	/**
	 * 开启新下载通知
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
		NotificationCompat.Builder builder=new NotificationCompat.Builder(com.blankj.utilcode.util.Utils.getApp(), NotificationUtils.channelDownloadId);
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
		builder.setProgress(100, 0,false);
		builder.setAutoCancel(false);
		builder.setShowWhen(false);
		builder.setContentIntent(PendingIntent.getBroadcast(com.blankj.utilcode.util.Utils.getApp(), (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
		manager.notify(id, builder.build());
		builders.put(""+id, builder);
		lastPercentMaps.put(""+id, 0);
	}

	/**
	 * 更新相应id的通知栏
	 * @param id
	 * @param persent
	 * @param title
	 */
	public void updateDownloadNotification(int id, int persent, String title) {
		if (!checkContainId(id)) {
			return;
		}
		if (lastPercentMaps.containsKey(""+id)) {
			if (persent-10>lastPercentMaps.get(""+id).intValue()) {
				lastPercentMaps.put(""+id, persent);
			}
			else {
				return;
			}
		}
		else {
			return;
		}
		NotificationCompat.Builder builder=builders.get(""+id);
		builder.setContentTitle(title);
		builder.setContentText("已下载"+persent+"%");
		builder.setProgress(100, persent,false);
		manager.notify(id, builder.build());
	}

	/**
	 * 最后提示
	 * @param id
	 */
	public void showEndNotification(int id) {
		if (!checkContainId(id)) {
			return;
		}
		NotificationCompat.Builder builder=builders.get(""+id);
		builder.setContentText("下载完成");
		builder.setProgress(100, 100, false);
		manager.notify(id, builder.build());
		new Handler().postDelayed(() -> cancelNotification(id), 1000);
	}

	/**
	 * 关闭通知
	 * @param id
	 */
	public void cancelNotification(int id) {
		manager.cancel(id);
		builders.remove(""+id);
		lastPercentMaps.remove(""+id);
	}

	/**
	 * 8.0开启前台服务
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
}