package eu.marcellofabbri.dailyroadmap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.view.activityHelpers.EventPainterContainer;
import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.EventAdapter;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.TrackPainter;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.VerticalTrackPainter;
import eu.marcellofabbri.dailyroadmap.viewModel.EventViewModel;

public class MyTrackTextWatcher implements TextWatcher {
    private EventPainterContainer eventPainterContainer;
    private Context context;
    private List<Event> events;
    private EventViewModel eventViewModel;
    private LifecycleOwner lifecycleOwner;
    private String currentView;

    public MyTrackTextWatcher(EventPainterContainer eventTrackContainer, Context context, LifecycleOwner lifecycleOwner, EventAdapter adapter, EventViewModel eventViewModel, String currentView) {
        this.eventPainterContainer = eventTrackContainer;
        this.context = context;
        this.events = adapter.getEvents();
        this.eventViewModel = eventViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.currentView = currentView;
    }

    public void setCurrentView(String string) {
        this.currentView = string;
    }

    public void setEventPainterContainer(EventPainterContainer eventPainterContainer) {
        this.eventPainterContainer = eventPainterContainer;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void afterTextChanged(Editable s) {
        String newString = s.toString();
        OffsetDateTime newDateTime = new EntityFieldConverter().convertDayStringToOffsetDateTime(newString);
        eventViewModel.getCertainEvents(newDateTime).observe(lifecycleOwner, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventPainterContainer.removeAllViews();
                boolean isToday = newDateTime.toLocalDate().equals(LocalDate.now());
                if (currentView.equals("rectangular")) {
                    eventPainterContainer.addView(new TrackPainter(context, events, isToday));
                } else if (currentView.equals("vertical")) {
                    eventPainterContainer.addView(new VerticalTrackPainter(context, events, isToday));
                }
            }
        });
    }
}
