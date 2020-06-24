package eu.marcellofabbri.dailyroadmap.view;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

import eu.marcellofabbri.dailyroadmap.model.Event;
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
    public static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences sharedPreferences;
    NotificationManagerCompat notificationManagerCompat;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_activity).setBackgroundColor(ContextCompat.getColor(this, backgroundColors[selectedBackgroundColorPosition]));

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

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

        // SET UP AND MAINTAIN NOTIFICATIONS
//        eventViewModel.getCertainEvents(displayedDate).observe(this, new Observer<List<Event>>() {
//            @Override
//            public void onChanged(List<Event> events) {
//
//                // CREATE ALARMS FOR EACH OF TODAY'S EVENTS
//                for (Event event : events) {
//                    Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, event.getId(), intent, 0);
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, event.getStartTime().toInstant().toEpochMilli(), pendingIntent);
//                }
//            }
//        });


        final EventPainterContainer eventPainterContainer = findViewById(R.id.eventPainterContainer);

        eventViewModel.getCertainEvents(displayedDate).observe(MainActivity.this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventPainterContainer.removeAllViews();
                eventPainterContainer.addView(new TrackPainter(MainActivity.this, events));
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
            }

            @Override
            public void onUpdateButtonClick(Event event) {
                Intent intent = new Intent(MainActivity.this, AddUpdateEventActivity.class);
                intent.putExtra(AddUpdateEventActivity.EXTRA_ID, event.getId());
                intent.putExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION, event.getDescription());
                intent.putExtra(AddUpdateEventActivity.EXTRA_FINISHTIME, converter.extractDate(event.getFinishTime()) + converter.extractTime(event.getFinishTime()));
                intent.putExtra(AddUpdateEventActivity.EXTRA_STARTTIME, converter.extractDate(event.getStartTime()) + converter.extractTime(event.getStartTime()));
                intent.putExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID, event.getIcon());
                startActivityForResult(intent, UPDATE_EVENT_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //String start = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);

        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {

            // EXTRAPOLATE ALL NECESSARY INFO TO DESCRIBE THE FIELDS OF EVENT
            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            OffsetDateTime startTime = converter.convertMashedDateToString(startTimeString);
            String finishTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);
            String icon = data.getStringExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID);
            OffsetDateTime finishTime = converter.convertMashedDateToString(finishTimeString);
            long unixStart = startTime.toEpochSecond();

            // CREATE THE EVENT AND INSERT IT IN THE DATABASE
            Event newEvent = new Event(description, startTime, finishTime, unixStart, icon);
            eventViewModel.insert(newEvent);

            // CREATE AN ASSOCIATED NOTIFICATION FOR THE EVENT
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, ReminderBroadcast.class);
            intent.putExtra("title", description);
            intent.putExtra("startTime", startTimeString.substring(8));
            intent.putExtra("finishTime", finishTimeString.substring(8));
            intent.putExtra("iconId", Integer.parseInt(icon));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, unixStart*1000, pendingIntent);

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

            Event event = new Event(description, startTime, finishTime, 0, icon);
            event.setId(id);
            eventViewModel.update(event);

            Toast.makeText(this, "Event updated", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpEventNotification(Event event) {
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(MainActivity.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, event.getStartTime().toInstant().toEpochMilli(), pendingIntent);
    }

    private void recordStartTime(Event event) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String index = String.valueOf(event.getId());
        editor.putLong(index, event.getStartTime().toInstant().toEpochMilli());
        editor.commit();
    }

    private long retrieveStartTime(Event event) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String index = String.valueOf(event.getId());
        return sharedPreferences.getLong(index, 0);
    }

    public void createNotifications(List<Event> events, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.getAll().keySet().forEach(key -> {
            Intent intent = new Intent(context, ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) Integer.parseInt(key), intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, sharedPreferences.getLong(key, 0), pendingIntent);
        });
    }

    public void deleteNotification() {

    }


}
