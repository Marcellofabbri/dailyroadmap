package eu.marcellofabbri.dailyroadmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_EVENT_REQUEST_CODE = 1;
    public static final int UPDATE_EVENT_REQUEST_CODE = 2;
    private EventViewModel eventViewModel;
    private MyVisualizer myVisualizer;
    private Calendar myCalendar = Calendar.getInstance();
    private List<Event> displayedEvents;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainHeader mainHeader = findViewById(R.id.header);
        mainHeader.identifyFields();
        mainHeader.bootElements();

        FloatingActionButton addEventButton = findViewById(R.id.button_add_event);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUpdateEventActivity.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST_CODE);
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final EventAdapter adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        final String displayedDate = mainHeader.getCurrentDate().getText().toString();
        eventViewModel.getCertainEvents(displayedDate).observe(this, new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
            }
        });

        eventViewModel.getCertainEvents(displayedDate).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {

                RelativeLayout eventPainterContainer = findViewById(R.id.eventPainterContainer);
                TrackPainter trackPainter = new TrackPainter(MainActivity.this, events);
                System.out.println(trackPainter.map);
                eventPainterContainer.addView(trackPainter);
            }
        });

        TextWatcher textWatcher = new MyMainTextWatcher(eventViewModel, adapter, MainActivity.this);
        mainHeader.getCurrentDate().addTextChangedListener(textWatcher);

        MyButtonsCardView myButtonsCardView = findViewById(R.id.my_buttons_cardview);
        FloatingActionButton calendarButton = findViewById(R.id.calendar_button);
        myButtonsCardView.setCalendarButton(calendarButton);
        myButtonsCardView.setMainHeader(mainHeader);
        myButtonsCardView.bootCalendarButton();

        adapter.setOnButtonClickListener(new EventAdapter.OnButtonClickListener() {
            @Override
            public void onDeleteButtonClick(Event event) {
                eventViewModel.delete(event);
            }

            @Override
            public void onUpdateButtonClick(Event event) {
                Intent intent = new Intent(MainActivity.this, AddUpdateEventActivity.class);
                intent.putExtra(AddUpdateEventActivity.EXTRA_ID, event.getId());
                intent.putExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION, event.getDescription());
                intent.putExtra(AddUpdateEventActivity.EXTRA_FINISHTIME, event.getFinishTime());
                intent.putExtra(AddUpdateEventActivity.EXTRA_STARTTIME, event.getStartTime());
                startActivityForResult(intent, UPDATE_EVENT_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTime = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            String finishTime = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);

            Event newEvent = new Event(description, startTime, finishTime);
            eventViewModel.insert(newEvent);

            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == UPDATE_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddUpdateEventActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Event can't be updated", Toast.LENGTH_LONG).show();
                return;
            }

            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTime = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            String finishTime = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);

            Event event = new Event(description, startTime, finishTime);
            event.setId(id);
            eventViewModel.update(event);

            Toast.makeText(this, "Event updated", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
