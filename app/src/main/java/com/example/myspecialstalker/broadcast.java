package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;


public class broadcast extends BroadcastReceiver {

    public static final String CHANNEL = "Channel";
    public static final String CHANNEL_ID = "1";
    NotificationCompat.Builder builder;


    @Override
    public void onReceive(final Context context, Intent intent) {

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()))
        {
            String calledNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (MainActivity.emptyPhone && MainActivity.emptyMsg)
            {
                String messageToSend = MainActivity.getMsg + calledNumber;
                handleNotifications("sending message...", context);
                PendingIntent sentPI = getPendingIntent(context, "SMS_SENT");
                PendingIntent deliveredPI = getPendingIntent(context, "SMS_DELIVERED");
                BroadcastReceiver sendSMS = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        if(getResultCode() == Activity.RESULT_OK)
                        {
                            handleNotifications("message sent successfully!", context);
                        }
                    }
                };
                BroadcastReceiver deliverSMS = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        if(getResultCode() == Activity.RESULT_OK)
                        {
                            handleNotifications("message received successfully!", context);
                        }
                    }
                };
                context.getApplicationContext().registerReceiver(sendSMS, new IntentFilter("SMS_SENT"));
                context.getApplicationContext().registerReceiver(deliverSMS, new IntentFilter("SMS_DELIVERED"));
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(MainActivity.getPhoneNum, null, messageToSend, sentPI, deliveredPI);
            }
        }
    }

    private void handleNotifications(String title, Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL,
                            NotificationManager.IMPORTANCE_DEFAULT);
            Notification notification =
                    new Notification.Builder(context.getApplicationContext())
                            .setSmallIcon(R.drawable.images)
                            .setContentTitle("Stalk Stalk")
                            .setContentText(title)
                            .setChannelId(CHANNEL_ID).build();
            NotificationManager notificationManager = context.getApplicationContext().
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, notification);
        }
        else
        {
            builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.images)
                    .setContentTitle("Stalk Stalk")
                    .setContentText(title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.
                    from(context.getApplicationContext());
            notificationManager.notify(1, builder.build());
        }
    }
        public PendingIntent getPendingIntent(Context context, String state)
    {
        return PendingIntent.getBroadcast(context, 0, new Intent(state), 0);
    }

}
