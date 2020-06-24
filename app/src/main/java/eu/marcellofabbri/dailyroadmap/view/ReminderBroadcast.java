package eu.marcellofabbri.dailyroadmap.view;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import eu.marcellofabbri.dailyroadmap.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notificationBuilder = notificationHelper.getChannel1Notification("TITLE", "BODY");
        notificationHelper.getManager().notify(1, notificationBuilder.build());
        System.out.println("ONRECEIVE TRIGGERED");
    }
}

