package com.renyu.commonlibrary.commonutils.notification;

import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import java.util.Stack;

/**
 * Created by Administrator on 2018/10/22.
 * 参考 https://github.com/cloudzyy/NotificationCompat
 */
public class NotificationCompatColor {

    private int finalContentTitleColor = Color.WHITE;
    private int finalContentTextColor = Color.WHITE;

    public void byAutomation() {
        RemoteViews remoteViews = buildFakeRemoteViews();
        if (!fetchNotificationTextColorByText(remoteViews)) {
            if (!fetchNotificationTextColorById(remoteViews)) {
                fetchNotificationTextColorBySdkVersion();
            }
        }
    }

    public int getFinalContentTitleColor() {
        return finalContentTitleColor;
    }

    public int getFinalContentTextColor() {
        return finalContentTextColor;
    }

    /**
     * 创建默认的Notification获取默认通知内RemoteViews
     * @return
     */
    private RemoteViews buildFakeRemoteViews() {
        Notification.Builder builder = new Notification.Builder(Utils.getApp());
        builder.setContentTitle("title").setContentText("text").setTicker("ticker");
        RemoteViews remoteViews;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            remoteViews = builder.createContentView();
        }
        else {
            Notification notification = builder.getNotification();
            remoteViews = notification.contentView;
        }
        return remoteViews;
    }

    private boolean fetchNotificationTextColorByText(RemoteViews remoteViews) {
        if (remoteViews != null) {
            TextView contentTitleTextView = null;
            TextView contentTextTextView = null;
            View notificationRootView = remoteViews.apply(Utils.getApp(), new FrameLayout(Utils.getApp()));
            Stack<View> stack = new Stack<>();
            stack.push(notificationRootView);
            while (!stack.isEmpty()) {
                View view = stack.pop();
                if (view instanceof TextView) {
                    TextView childTextView = (TextView) view;
                    CharSequence charSequence = childTextView.getText();
                    if (charSequence.equals("title")) {
                        contentTitleTextView = childTextView;
                    } else if (charSequence.equals("text")) {
                        contentTextTextView = childTextView;
                    }
                    if (contentTitleTextView != null && contentTextTextView != null) {
                        break;
                    }
                }
                else if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int i = 0 ; i < viewGroup.getChildCount() ; i++) {
                        stack.push(viewGroup.getChildAt(i));
                    }
                }
            }
            stack.clear();
            return checkAndGuessColor(contentTitleTextView, contentTextTextView);
        }
        return false;
    }

    private boolean fetchNotificationTextColorById(RemoteViews remoteViews) {
        TextView contentTitleTextView = null;
        TextView contentTextTextView = null;
        int layoutId = remoteViews.getLayoutId();
        ViewGroup viewGroup= (ViewGroup) LayoutInflater.from(Utils.getApp()).inflate(layoutId, null, false);
        if (getAndroidInternalResourceId("title") > 0) {
            contentTitleTextView = viewGroup.findViewById(getAndroidInternalResourceId("title"));
        }
        if (getAndroidInternalResourceId("text") > 0) {
            contentTextTextView = viewGroup.findViewById(getAndroidInternalResourceId("text"));
        }
        return checkAndGuessColor(contentTitleTextView, contentTextTextView);
    }

    /**
     * L及以上系统都是黑色文字
     */
    private void fetchNotificationTextColorBySdkVersion() {
        final boolean isLightColor = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
        if (isLightColor) {
            finalContentTitleColor = 0xffffffff;
            finalContentTextColor = 0xff999999;
        } else {
            finalContentTitleColor = 0xde000000;
            finalContentTextColor = 0x8a000000;
        }
    }

    /**
     * 获取"android"包名里的id, 即com.android.internal.R.id.resourceName
     * defType = "id"，还可以有"layout","drawable"之类的
     * @param resourceName
     * @return
     */
    public int getAndroidInternalResourceId(String resourceName) {
        final int id = Resources.getSystem().getIdentifier(resourceName, "id", "android");
        if (id > 0) {
            return id;
        }
        return 0;
    }

    private boolean checkAndGuessColor(TextView contentTitleTextView, TextView contentTextTextView) {
        if (contentTitleTextView != null) {
            finalContentTitleColor = contentTitleTextView.getTextColors().getDefaultColor();
        }
        if (contentTextTextView != null) {
            finalContentTextColor = contentTextTextView.getTextColors().getDefaultColor();
        }
        if (finalContentTitleColor != 0 && finalContentTextColor != 0) {
            return true;
        }
        if (finalContentTitleColor != 0) {
            if (isLightColor(finalContentTitleColor)) {
                finalContentTextColor = 0xff999999;
            }
            else {
                finalContentTextColor = 0xff666666;
            }
            return true;
        }
        if (finalContentTextColor != 0) {
            if (isLightColor(finalContentTextColor)) {
                finalContentTitleColor = 0xffffffff;
            }
            else {
                finalContentTitleColor = 0xff000000;
            }
            return true;
        }
        return false;
    }

    private static boolean isLightColor(int color) {
        return isLightAverageColor(toAverageColor(color));
    }

    private static boolean isLightAverageColor(int averageColor) {
        return averageColor >= 0x80;
    }

    //RGB的平均值
    private static int toAverageColor(int color) {
        return (int) ((Color.red(color) + Color.green(color) + Color.blue(color)) / 3f + 0.5f);
    }
}
