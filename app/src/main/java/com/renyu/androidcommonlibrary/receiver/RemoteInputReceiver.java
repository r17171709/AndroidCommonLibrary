package com.renyu.androidcommonlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.commonutils.notification.NotificationCenterManager;

/**
 * Created by Administrator on 2018/1/29.
 */

public class RemoteInputReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = RemoteInput.getResultsFromIntent(intent);
        if (bundle != null) {
            String reply = bundle.getCharSequence(NotificationCenterManager.KEY_TEXT_REPLY) == null ? "" : bundle.getCharSequence(NotificationCenterManager.KEY_TEXT_REPLY).toString();
            NotificationCenterManager.getManager().createNormalNotification("ticker", "channel1", reply, Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, null, new Intent(), 105, "channel_default3");
        }
    }
}
