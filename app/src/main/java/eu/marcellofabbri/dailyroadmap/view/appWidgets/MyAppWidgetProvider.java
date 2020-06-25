package eu.marcellofabbri.dailyroadmap.view.appWidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.OffsetDateTime;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.R;
import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.view.activities.MainActivity;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.EventAdapter;
import eu.marcellofabbri.dailyroadmap.viewModel.EventViewModel;

public class MyAppWidgetProvider extends android.appwidget.AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_one);
            views.setOnClickPendingIntent(R.layout.app_widget_one, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
