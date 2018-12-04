package com.renyu.androidcommonlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class IncomingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {

            } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {

            }
        }
    }
}
