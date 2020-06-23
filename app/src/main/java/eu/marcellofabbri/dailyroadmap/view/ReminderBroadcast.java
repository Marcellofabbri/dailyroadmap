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
        String title = "NOTIFICATION TITLE";
        String message = "notification message";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TheApplication.CHANNEL_1_ID)
                .setSmallIcon(R.id.chosen_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}

