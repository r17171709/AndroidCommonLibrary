package com.renyu.commonlibrary.commonutils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class NotificationUtils {

	private volatile static NotificationUtils center=null;
	private static NotificationManager manager=null;

	private static Context context=null;

	private static HashMap<String, NotificationCompat.Builder> builders;

	private static HashMap<String, Integer> lastPercentMaps;

	public static final String channelId = "channel_default";
	public static final String channelName = "channel_name_default";

	/**
	 * 通知栏中心调度
	 * @param context
	 * @return
	 */
	public static NotificationUtils getNotificationCenter(Context context) {
		if(center==null) {
			synchronized (NotificationUtils.class) {
				if (center==null) {
					center=new NotificationUtils();
					manager=(NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
					if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
						createNotificationChannel();
					}
					builders=new HashMap<>();
					lastPercentMaps=new HashMap<>();
					NotificationUtils.context = context.getApplicationContext();
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

	@RequiresApi(api = Build.VERSION_CODES.O)
	private static void createNotificationChannel(){
		NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
		// 是否在桌面icon右上角展示小红点
		channel.enableLights(true);
		// 小红点颜色
		channel.setLightColor(Color.RED);
		// 是否在久按桌面图标时显示此渠道的通知
		channel.setShowBadge(true);
		manager.createNotificationChannel(channel);
	}

	@NonNull
	private NotificationCompat.Builder getSimpleBuilder(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId, Intent intent) {
		NotificationCompat.Builder builder=new NotificationCompat.Builder(context, channelId);
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setContentIntent(PendingIntent.getBroadcast(context, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
		builder.setColor(color);
		builder.setSmallIcon(smallIcon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);
		builder.setPriority(NotificationCompat.PRIORITY_MAX);
		builder.setDefaults(Notification.DEFAULT_ALL);
		// 保持通知不被移除
		builder.setOngoing(false);
		return builder;
	}

	/**
	 * 普通通知栏
	 * @param ticker
	 * @param title
	 * @param content
	 */
	public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Intent intent) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 创建有按钮的通知
	 * @param ticker
	 * @param title
	 * @param content
	 * @param color
	 * @param smallIcon
	 * @param largeIcon
	 * @param actionIcon1
	 * @param actionTitle1
	 * @param actionClass1
	 */
	public void createButtonNotification(String ticker, String title, String content,
										 int color, int smallIcon, int largeIcon,
										 int actionIcon1, String actionTitle1, Class actionClass1,
										 Intent intent) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.addAction(actionIcon1, actionTitle1, PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(), new Intent(context, actionClass1), PendingIntent.FLAG_UPDATE_CURRENT));
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	public void createButton2Notification(String ticker, String title, String content,
										  int color, int smallIcon, int largeIcon,
										  int actionIcon1, String actionTitle1, Class actionClass1,
										  int actionIcon2, String actionTitle2, Class actionClass2,
										  Intent intent) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.addAction(actionIcon1, actionTitle1, PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(), new Intent(context, actionClass1), PendingIntent.FLAG_UPDATE_CURRENT));
		builder.addAction(actionIcon2, actionTitle2, PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(), new Intent(context, actionClass2), PendingIntent.FLAG_UPDATE_CURRENT));
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 精确进度条通知
	 * @param ticker
	 * @param title
	 * @param content
	 * @param color
	 * @param smallIcon
	 * @param largeIcon
	 * @param max
	 * @param progress
	 */
	public void createProgressNotification(String ticker, String title, String content,
										   int color, int smallIcon, int largeIcon,
										   int max, int progress, Intent intent) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setProgress(max, progress, false);
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 模糊进度条通知
	 * @param ticker
	 * @param title
	 * @param content
	 * @param color
	 * @param smallIcon
	 * @param largeIcon
	 */
	public void createIndeterminateProgressNotification(String ticker, String title, String content,
														int color, int smallIcon, int largeIcon,
														Intent intent) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setProgress(0, 0, true);
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 长文本通知
	 * @param ticker
	 * @param title
	 * @param content
	 * @param color
	 * @param smallIcon
	 * @param largeIcon
	 * @param bigText
	 * @param bigContentTitle
	 * @param summaryText
	 */
	public void createBigTextNotification(String ticker, String title, String content,
										  int color, int smallIcon, int largeIcon,
										  String bigText, String bigContentTitle, String summaryText,
										  Intent intent) {
		NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle();
		style.bigText(bigText);
		style.setBigContentTitle(bigContentTitle);
		style.setSummaryText(summaryText);
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setStyle(style);
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 大图通知
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
	 */
	public void createBigImageNotification(String ticker, String title, String content,
										   int color, int smallIcon, int largeIcon,
										   int bigLargeIcon, int bigPicture, String bigContentTitle, String summaryText,
										   Intent intent) {
		NotificationCompat.BigPictureStyle style=new NotificationCompat.BigPictureStyle();
		style.bigLargeIcon(BitmapFactory.decodeResource(context.getResources(), bigLargeIcon));
		style.bigPicture(BitmapFactory.decodeResource(context.getResources(), bigPicture));
		style.setBigContentTitle(bigContentTitle);
		style.setSummaryText(summaryText);
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setStyle(style);
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 列表型通知
	 * @param ticker
	 * @param title
	 * @param content
	 * @param color
	 * @param smallIcon
	 * @param largeIcon
	 * @param linesString
	 * @param bigContentTitle
	 * @param summaryText
	 */
	public void createTextListNotification(String ticker, String title, String content,
										   int color, int smallIcon, int largeIcon,
										   ArrayList<String> linesString, String bigContentTitle, String summaryText,
										   Intent intent) {
		NotificationCompat.InboxStyle style=new NotificationCompat.InboxStyle();
		for (String s : linesString) {
			style.addLine(s);
		}
		style.setBigContentTitle(bigContentTitle);
		style.setSummaryText(summaryText);
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setStyle(style);
		manager.notify((int) (System.currentTimeMillis()/1000), builder.build());
	}

	/**
	 * 开启新下载通知
	 * @param id
	 */
	public void createDownloadNotification(int id, String title, int color, int smallIcon, int largeIcon) {
		if (checkContainId(id)) {
			return;
		}
		NotificationCompat.Builder builder=new NotificationCompat.Builder(context, NotificationUtils.channelId);
		builder.setSmallIcon(smallIcon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
		builder.setWhen(System.currentTimeMillis());
		builder.setPriority(NotificationCompat.PRIORITY_MAX);
		builder.setColor(color);
		builder.setOngoing(true);
		builder.setTicker(title);
		builder.setDefaults(Notification.DEFAULT_LIGHTS);
		builder.setContentTitle(title);
		builder.setProgress(100, 0,false);
		builder.setAutoCancel(false);
		builder.setShowWhen(false);
		builder.setContentIntent(PendingIntent.getBroadcast(context, (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
		manager.notify(id, builder.build());
		builders.put(""+id, builder);
		lastPercentMaps.put(""+id, 0);
	}

	/**
	 * 更新相应id的通知栏
	 * @param persent
	 * @param id
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
	 * 关闭通知
	 * @param id
	 */
	public void cancelNotification(int id) {
		manager.cancel(id);
		builders.remove(""+id);
		lastPercentMaps.remove(""+id);
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
	public void deleteChannel(String id) {
		manager.deleteNotificationChannel(id);
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
		builder.setContentIntent(PendingIntent.getBroadcast(context, (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
		manager.notify(id, builder.build());
		manager.cancel(id);
		builders.remove(""+id);
		lastPercentMaps.remove(""+id);
	}

	/**
	 * 8.0开启后台服务
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
				NotificationUtils.channelId,
				new Intent());
		builder.setAutoCancel(false);
		service.startForeground(id, builder.build());
	}

	/**
	 * 8.0关闭后台服务
	 * @param service
	 * @param id
	 */
	public void hideStartForeground(Service service, int id) {
		service.stopForeground(true);
		manager.cancel(id);
	}

	private static boolean isDarkNotificationTheme() {
		return !isSimilarColor(Color.BLACK, getNotificationColor());
	}

	/**
	 * 获取通知栏颜色
	 * @return
	 */
	private static int getNotificationColor() {
		NotificationCompat.Builder builder=new NotificationCompat.Builder(context, NotificationUtils.channelId);
		Notification notification=builder.build();
		// 7.0没有解决
		if (notification.contentView==null) {
			return Color.WHITE;
		}
		int layoutId=notification.contentView.getLayoutId();
		ViewGroup viewGroup= (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
		if (viewGroup.findViewById(android.R.id.title)!=null) {
			return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
		}
		return findColor(viewGroup);
	}

	private static int findColor(ViewGroup viewGroupSource) {
		int color=Color.TRANSPARENT;
		LinkedList<ViewGroup> viewGroups=new LinkedList<>();
		viewGroups.add(viewGroupSource);
		while (viewGroups.size()>0) {
			ViewGroup viewGroup1=viewGroups.getFirst();
			for (int i = 0; i < viewGroup1.getChildCount(); i++) {
				if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
					viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
				}
				else if (viewGroup1.getChildAt(i) instanceof TextView) {
					if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor()!=-1) {
						color=((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
					}
				}
			}
			viewGroups.remove(viewGroup1);
		}
		return color;
	}

	private static boolean isSimilarColor(int baseColor, int color) {
		int simpleBaseColor=baseColor|0xff000000;
		int simpleColor=color|0xff000000;
		int baseRed=Color.red(simpleBaseColor)-Color.red(simpleColor);
		int baseGreen=Color.green(simpleBaseColor)-Color.green(simpleColor);
		int baseBlue=Color.blue(simpleBaseColor)-Color.blue(simpleColor);
		double value=Math.sqrt(baseRed*baseRed+baseGreen*baseGreen+baseBlue*baseBlue);
		if (value<180.0) {
			return true;
		}
		return false;
	}
}