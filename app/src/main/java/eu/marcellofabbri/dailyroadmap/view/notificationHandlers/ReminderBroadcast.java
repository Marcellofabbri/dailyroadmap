package eu.marcellofabbri.dailyroadmap.view.notificationHandlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.hasExtra("title") ? intent.getStringExtra("title") : "TITLE";
        String startTime = intent.hasExtra("startTime") ? intent.getStringExtra("startTime") : "START";
        String finishTime = intent.hasExtra("finishTime") ? intent.getStringExtra("finishTime") : "FINISH";
        String body = startTime + " - " + finishTime;
        int iconId = intent.getIntExtra("iconId", 0);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notificationBuilder = notificationHelper.getChannel1Notification(title, body, iconId);
        notificationHelper.getManager().notify(1, notificationBuilder.build());
    }
}

