package com.renyu.commonlibrary.commonutils;

import android.app.Notification;
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
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.commonlibrary.R;

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

	public static final String KEY_TEXT_REPLY = "key_text_reply";

	public static final String groupId = "channel_group_default";
	public static final String groupName = "channel_group_name_default";
	public static final String groupDownloadId = "channel_group_download";
	public static final String groupDownloadName = "channel_group_name_download";

	public static final String channelId = "channel_default";
	public static final String channelName = "channel_name_default";
	public static final String channelDownloadId = "channel_download";
	public static final String channelDownloadName = "channel_name_download";

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
						ArrayList<NotificationChannelGroup> groups = new ArrayList<>();
						NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
						groups.add(group);
						NotificationChannelGroup group_download = new NotificationChannelGroup(groupDownloadId, groupDownloadName);
						groups.add(group_download);
						createNotificationGroups(groups);

						NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
						// 开启指示灯，如果设备有的话
						channel.enableLights(true);
						// 设置指示灯颜色
						channel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
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
		NotificationCompat.Builder builder=new NotificationCompat.Builder(context, channelId);
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setContentIntent(PendingIntent.getBroadcast(context, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
		builder.setColor(color);
		// 设置和启用通知的背景颜色（只能在用户必须一眼就能看到的持续任务的通知中使用此功能）
		builder.setColorized(true);
		builder.setSmallIcon(smallIcon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
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
		return getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent).setTimeoutAfter(timeout*1000);
	}

	/**
	 * 普通通知栏
	 * @param ticker
	 * @param title
	 * @param content
	 * @param color
	 * @param smallIcon
	 * @param largeIcon
	 * @param intent
	 */
	public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Intent intent) {
		createNormalNotification(ticker, title, content, color, smallIcon, largeIcon, intent, (int) (System.currentTimeMillis()/1000));
	}

	public void createNormalNotification(String ticker, String title, String content, int color, int smallIcon, int largeIcon, Intent intent, int notifyId) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		manager.notify(notifyId, builder.build());
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
	 * @param intent
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
	 * @param intent
	 */
	public void createProgressNotification(String ticker, String title, String content,
										   int color, int smallIcon, int largeIcon,
										   int max, int progress,
										   Intent intent, int notifyId) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setProgress(max, progress, false);
		manager.notify(notifyId, builder.build());
	}

	/**
	 * 模糊进度条通知
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
														Intent intent, int notifyId) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setProgress(0, 0, true);
		manager.notify(notifyId, builder.build());
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
	 * @param intent
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
	 * @param intent
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
	 * @param intent
	 */
	public void createTextListNotification(String ticker, String title, String content,
										   int color, int smallIcon, int largeIcon,
										   ArrayList<String> linesString, String bigContentTitle, String summaryText,
										   Intent intent, int notifyId) {
		NotificationCompat.InboxStyle style=new NotificationCompat.InboxStyle();
		for (String s : linesString) {
			style.addLine(s);
		}
		style.setBigContentTitle(bigContentTitle);
		style.setSummaryText(summaryText);
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setStyle(style);
		manager.notify(notifyId, builder.build());
	}

	public NotificationCompat.MessagingStyle createMessagingStyleNotification(String ticker, String title, String content,
												 int color, int smallIcon, int largeIcon,
												 String userDisplayName, String conversationTitle,
												 ArrayList<NotificationCompat.MessagingStyle.Message> messages,
												 Intent intent, int notifyId) {
		NotificationCompat.MessagingStyle style = new NotificationCompat
				.MessagingStyle(userDisplayName)
				.setConversationTitle(conversationTitle);
		for (NotificationCompat.MessagingStyle.Message message : messages) {
			style.addMessage(message);
		}
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setStyle(style);
		manager.notify(notifyId, builder.build());
		return style;
	}

	public void updateMessagingStyleNotification(String ticker, String title, String content,
												 int color, int smallIcon, int largeIcon,
												 NotificationCompat.MessagingStyle style,
												 ArrayList<NotificationCompat.MessagingStyle.Message> messages,
												 Intent intent, int notifyId) {
		for (NotificationCompat.MessagingStyle.Message message : messages) {
			style.addMessage(message);
		}
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, NotificationUtils.channelId, intent);
		builder.setStyle(style);
		manager.notify(notifyId, builder.build());
	}

	public void createRemoteInput(String ticker, String title, String content, int color, int smallIcon, int largeIcon, String channelId, Intent intent, int notifyId,
								  String replyLabel, Class receiverClass, int replyIcon, CharSequence replyTitle) {
		NotificationCompat.Builder builder = getSimpleBuilder(ticker, title, content, color, smallIcon, largeIcon, channelId, intent);
		RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel(replyLabel).build();
		Intent intent1 = new Intent(context, receiverClass);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
		NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(replyIcon, replyTitle, pendingIntent).addRemoteInput(remoteInput).build();
		builder.addAction(replyAction);
		manager.notify(notifyId, builder.build());
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
		NotificationCompat.Builder builder=new NotificationCompat.Builder(context, NotificationUtils.channelDownloadId);
		builder.setSmallIcon(smallIcon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
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
		builder.setContentIntent(PendingIntent.getBroadcast(context, (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
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
				NotificationUtils.channelDownloadId,
				new Intent());
		builder.setOngoing(true);
		builder.setAutoCancel(false);
		builder.setColor(Color.WHITE);
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

	public NotificationManager getNotificationManager() {
		return manager;
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