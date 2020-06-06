package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyTrackTextWatcher implements TextWatcher {
    private EventPainterContainer eventPainterContainer;
    private Context context;
    private List<Event> events;
    private EventViewModel eventViewModel;
    private LifecycleOwner lifecycleOwner;

    public MyTrackTextWatcher(EventPainterContainer eventTrackContainer, Context context, LifecycleOwner lifecycleOwner, EventAdapter adapter, EventViewModel eventViewModel) {
        this.eventPainterContainer = eventTrackContainer;
        this.context = context;
        this.events = adapter.getEvents();
        this.eventViewModel = eventViewModel;
        this.lifecycleOwner = lifecycleOwner;

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
        eventViewModel.getCertainEvents(s.toString()).observe(lifecycleOwner, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventPainterContainer.removeAllViews();
                eventPainterContainer.addView(new TrackPainter(context, events));
                System.out.println("changed");
            }
        });
    }
}
