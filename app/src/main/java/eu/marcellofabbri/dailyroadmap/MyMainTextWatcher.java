package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.List;

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

    @Override
    public void afterTextChanged(Editable s) {
        String newString = s.toString();
        eventViewModel.getCertainEvents(newString).observe(lifecycleOwner, new Observer<List<Event>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
