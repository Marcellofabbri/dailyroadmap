package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

public class MyMainTextWatcher implements TextWatcher {
    private EventViewModel eventViewModel;
    private EventAdapter adapter;
    private LifecycleOwner lifecycleOwner;

    //pass as arguments eventViewModel, adapter, MainActivity.this
    public MyMainTextWatcher(EventViewModel eventViewModel, EventAdapter adapter, LifecycleOwner lifecycleOwner) {
        this.eventViewModel = eventViewModel;
        this.adapter = adapter;
        this.lifecycleOwner = lifecycleOwner;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
                adapter.notifyDataSetChanged();
            }
        });
    }

}
