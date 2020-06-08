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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;
import eu.marcellofabbri.dailyroadmap.utils.MyDateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    public static final int ADD_EVENT_REQUEST_CODE = 1;
    public static final int UPDATE_EVENT_REQUEST_CODE = 2;
    private EventViewModel eventViewModel;
    private Calendar myCalendar = Calendar.getInstance();
    private List<Event> displayedEvents;
    private EntityFieldConverter converter = new EntityFieldConverter();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainHeader mainHeader = findViewById(R.id.header);
        mainHeader.identifyFields();
        mainHeader.bootElements();

        FloatingActionButton addEventButton = findViewById(R.id.button_add_event);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUpdateEventActivity.class);
                SimpleDateFormat dateFormat = mainHeader.getSlashesFormat();
                intent.putExtra(AddUpdateEventActivity.EXTRA_STARTTIME, dateFormat.format(myCalendar.getTime()));
                startActivityForResult(intent, ADD_EVENT_REQUEST_CODE);
            }
        });

        FloatingActionButton deleteTodayEventsButton = findViewById(R.id.button_delete_today_events);
        deleteTodayEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateString = mainHeader.getCurrentDate().getText().toString();
                OffsetDateTime currentDate = new EntityFieldConverter().convertDayStringToOffsetDateTime(currentDateString);
                eventViewModel.deleteTodayEvents(currentDate);
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final EventAdapter adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        final String displayedDateString = mainHeader.getCurrentDate().getText().toString();
        final OffsetDateTime displayedDate = converter.convertDayStringToOffsetDateTime(displayedDateString);

        eventViewModel.getCertainEvents(displayedDate).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
            }
        });

        final EventPainterContainer eventPainterContainer = findViewById(R.id.eventPainterContainer);

        eventViewModel.getCertainEvents(displayedDate).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventPainterContainer.removeAllViews();
                eventPainterContainer.addView(new TrackPainter(MainActivity.this, events));
            }
        });

        TextWatcher dateTextWatcher = new MyMainTextWatcher(eventViewModel, adapter, MainActivity.this);
        mainHeader.getCurrentDate().addTextChangedListener(dateTextWatcher);
        TextWatcher myTrackTextWatcher = new MyTrackTextWatcher(eventPainterContainer, this, this, adapter, eventViewModel);
        mainHeader.getCurrentDate().addTextChangedListener(myTrackTextWatcher);

        MyButtonsCardView myButtonsCardView = findViewById(R.id.my_buttons_cardview);
        FloatingActionButton calendarButton = findViewById(R.id.calendar_button);
        TextView dayNumberTextView = findViewById(R.id.day_number);
        FloatingActionButton todayButton = findViewById(R.id.today_button);
        myButtonsCardView.setCalendarButton(calendarButton);
        myButtonsCardView.setMainHeader(mainHeader);
        myButtonsCardView.bootCalendarButton();
        myButtonsCardView.setDayNumberTextView(dayNumberTextView);
        myButtonsCardView.writeDayNumberTextView();
        myButtonsCardView.setTodayButton(todayButton);
        myButtonsCardView.bootTodayButton();

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
                intent.putExtra(AddUpdateEventActivity.EXTRA_FINISHTIME, converter.extractDate(event.getFinishTime())+converter.extractTime(event.getFinishTime()));
                intent.putExtra(AddUpdateEventActivity.EXTRA_STARTTIME, converter.extractDate(event.getStartTime())+converter.extractTime(event.getStartTime()));
                startActivityForResult(intent, UPDATE_EVENT_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String start = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);


        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            OffsetDateTime startTime = converter.convertMashedDateToString(startTimeString);
            String finishTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);
            OffsetDateTime finishTime = converter.convertMashedDateToString(finishTimeString);
            long unixStart = 0;


            Event newEvent = new Event(description, startTime, finishTime, unixStart);
            eventViewModel.insert(newEvent);

            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == UPDATE_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddUpdateEventActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Event can't be updated", Toast.LENGTH_LONG).show();
                return;
            }

            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            OffsetDateTime startTime = converter.convertMashedDateToString(startTimeString);
            String finishTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);
            OffsetDateTime finishTime = converter.convertMashedDateToString(finishTimeString);

            Event event = new Event(description, startTime, finishTime, 0);
            event.setId(id);
            eventViewModel.update(event);

            Toast.makeText(this, "Event updated", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
