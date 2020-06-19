package eu.marcellofabbri.dailyroadmap.utils;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.time.OffsetDateTime;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;
import eu.marcellofabbri.dailyroadmap.view.EventAdapter;
import eu.marcellofabbri.dailyroadmap.viewModel.EventViewModel;

public class MyMainTextWatcher implements TextWatcher {
    private EventViewModel eventViewModel;
    private EventAdapter adapter;
    private LifecycleOwner lifecycleOwner;
    private TextView noEvents;

    //pass as arguments eventViewModel, adapter, MainActivity.this
    public MyMainTextWatcher(EventViewModel eventViewModel, EventAdapter adapter, LifecycleOwner lifecycleOwner, TextView noEvents) {
        this.eventViewModel = eventViewModel;
        this.adapter = adapter;
        this.lifecycleOwner = lifecycleOwner;
        this.noEvents = noEvents;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void afterTextChanged(Editable s) {
        String newString = s.toString();
        OffsetDateTime newDateTime = new EntityFieldConverter().convertDayStringToOffsetDateTime(newString);
        eventViewModel.getCertainEvents(newDateTime).observe(lifecycleOwner, new Observer<List<Event>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
                if (events.size() == 0) {
                    noEvents.setText("NO EVENTS\nTO DISPLAY");
                } else {
                    noEvents.setText("");
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

}
