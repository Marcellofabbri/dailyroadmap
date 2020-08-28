package eu.marcellofabbri.dailyroadmap.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.scales.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.utils.CurrentHour;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.EventAdapter;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.EventPainterContainer;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.MainHeader;
import eu.marcellofabbri.dailyroadmap.view.notificationHandlers.ReminderBroadcast;
import eu.marcellofabbri.dailyroadmap.view.activityHelpers.VerticalTrackPainter;
import eu.marcellofabbri.dailyroadmap.viewModel.EventViewModel;
import eu.marcellofabbri.dailyroadmap.utils.MyMainTextWatcher;
import eu.marcellofabbri.dailyroadmap.utils.MyTrackTextWatcher;
import eu.marcellofabbri.dailyroadmap.R;
import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    public static final int ADD_EVENT_REQUEST_CODE = 1;
    public static final int UPDATE_EVENT_REQUEST_CODE = 2;
    private int[] backgroundColors = new int[] {R.color.daytimeBackground, R.color.lightGrey};
    private int selectedBackgroundColorPosition = 1;
    private EventViewModel eventViewModel;
    private Calendar myCalendar = Calendar.getInstance();
    private List<Event> displayedEvents;
    private EntityFieldConverter converter = new EntityFieldConverter();
    private AlarmManager alarmManager;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_activity).setBackgroundColor(ContextCompat.getColor(this, backgroundColors[selectedBackgroundColorPosition]));

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.toolbar_logo_6));
        actionBar.setTitle("");

        final MainHeader mainHeader = findViewById(R.id.header);
        mainHeader.identifyFields();
        mainHeader.bootElements();
        mainHeader.bootDateClick();

        ImageButton addImageButton = findViewById(R.id.image_add);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUpdateEventActivity.class);
                intent.putExtra(AddUpdateEventActivity.EXTRA_STARTTIME, mainHeader.getCurrentDate().getText().toString());
                startActivityForResult(intent, ADD_EVENT_REQUEST_CODE);
            }
        });

        ImageButton deleteTodayEventsImageButton = findViewById(R.id.image_trash);
        deleteTodayEventsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("DELETE ALL EVENTS FOR THE DAY?");
                builder.setIcon(R.drawable.ic_big_trash);
                AlertDialog dialog = builder.create();
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentDateString = mainHeader.getCurrentDate().getText().toString();
                        OffsetDateTime currentDate = new EntityFieldConverter().convertDayStringToOffsetDateTime(currentDateString);

                        eventViewModel.getCertainEvents(currentDate).observe(MainActivity.this, new Observer<List<Event>>() {
                            @Override
                            public void onChanged(List<Event> events) {
                                for (Event event : events) {
                                    deleteNotification(event);
                                }
                            }
                        });

                        eventViewModel.deleteTodayEvents(currentDate);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
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
        TextView noEvents = findViewById(R.id.no_events);

        eventViewModel.getCertainEvents(displayedDate).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
                if (events.size() == 0) {
                    noEvents.setText("NO EVENTS\nTO DISPLAY");
                    noEvents.setTextColor(mainHeader.getDefaultColors()[1]);
                } else {
                    noEvents.setText("");
                }
            }
        });

        int hourOfDay = new CurrentHour(myCalendar).hourOfDay();
        final EventPainterContainer eventPainterContainer = findViewById(R.id.eventPainterContainer);
        final ScrollView scrollview = (ScrollView) findViewById(R.id.scrollpaint);
        ViewTreeObserver vto = scrollview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                scrollview.scrollTo(0, 3800*hourOfDay/24);
            }
        });


        eventViewModel.getCertainEvents(displayedDate).observe(MainActivity.this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventPainterContainer.removeAllViews();
                boolean isToday = displayedDate.toLocalDate().equals(LocalDate.now());
                VerticalTrackPainter verticalTrackPainter = new VerticalTrackPainter(MainActivity.this, events, isToday);
                eventPainterContainer.addView(verticalTrackPainter);


            }
        });



        TextWatcher dateTextWatcher = new MyMainTextWatcher(eventViewModel, adapter, MainActivity.this, noEvents);
        mainHeader.getCurrentDate().addTextChangedListener(dateTextWatcher);
        TextWatcher myTrackTextWatcher = new MyTrackTextWatcher(eventPainterContainer, this, this, adapter, eventViewModel);
        mainHeader.getCurrentDate().addTextChangedListener(myTrackTextWatcher);

        adapter.setOnButtonClickListener(new EventAdapter.OnButtonClickListener() {
            @Override
            public void onDeleteButtonClick(Event event) {
                eventViewModel.delete(event);
                deleteNotification(event);
            }

            @Override
            public void onUpdateButtonClick(Event event) {
                Intent intent = new Intent(MainActivity.this, AddUpdateEventActivity.class);
                intent.putExtra(AddUpdateEventActivity.EXTRA_ID, event.getId());
                intent.putExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION, event.getDescription());
                intent.putExtra(AddUpdateEventActivity.EXTRA_FINISHTIME, converter.extractDate(event.getFinishTime()) + converter.extractTime(event.getFinishTime()));
                intent.putExtra(AddUpdateEventActivity.EXTRA_STARTTIME, converter.extractDate(event.getStartTime()) + converter.extractTime(event.getStartTime()));
                intent.putExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID, event.getIcon());
                intent.putExtra(AddUpdateEventActivity.EXTRA_ORIGINAL_UNIX, String.valueOf(event.getStartUnix()));
                startActivityForResult(intent, UPDATE_EVENT_REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {

            // EXTRAPOLATE ALL NECESSARY INFO TO DESCRIBE THE FIELDS OF EVENT
            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            OffsetDateTime startTime = converter.convertMashedDateToString(startTimeString);
            String finishTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);
            String icon = data.getStringExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID);
            OffsetDateTime finishTime = converter.convertMashedDateToString(finishTimeString);
            long unixStart = startTime.toEpochSecond();
            int notice = Integer.parseInt(data.getStringExtra(AddUpdateEventActivity.EXTRA_NOTICE));

            // CREATE THE EVENT AND INSERT IT IN THE DATABASE
            Event newEvent = new Event(description, startTime, finishTime, unixStart, icon);
            eventViewModel.insert(newEvent);

            // CREATE AN ASSOCIATED NOTIFICATION FOR THE EVENT

            createNotification(newEvent, notice);

            // CONFIRMATORY TOAST
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
            String icon = data.getStringExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID);
            String stringOriginalUnix = data.getStringExtra(AddUpdateEventActivity.EXTRA_ORIGINAL_UNIX);
            long originalUnix = Long.parseLong(stringOriginalUnix);
            long unixStart = startTime.toEpochSecond();
            String stringNotice = data.getStringExtra(AddUpdateEventActivity.EXTRA_NOTICE);
            int notice = Integer.parseInt(stringNotice);

            Event event = new Event(description, startTime, finishTime, unixStart, icon);
            event.setId(id);
            eventViewModel.update(event);

            updateNotification(originalUnix, event, notice);

            Toast.makeText(this, "Event updated", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotification(Event event, int notice) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);
        intent.putExtra("title", event.getDescription());
        intent.putExtra("startTime", converter.extractTime(event.getStartTime()));
        intent.putExtra("finishTime", converter.extractTime(event.getFinishTime()));
        intent.putExtra("iconId", Integer.parseInt(event.getIcon()));
        long triggerAtMillis = event.getStartUnix()*1000;
        long noticeInMillis = notice*60*1000;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) event.getStartUnix(), intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis - noticeInMillis, pendingIntent);
    }

    private void deleteNotification(Event event) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) event.getStartUnix(), intent, 0);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    private void updateNotification(long oldRequestCode, Event newEvent, int notice) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) oldRequestCode, intent, 0);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);

        createNotification(newEvent, notice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.rectangular:
                return true;
            case R.id.vertical:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
