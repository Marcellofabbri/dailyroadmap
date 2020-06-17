package eu.marcellofabbri.dailyroadmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.charts.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        //SWIPE GESTURES TO BE IMPLEMENTED

//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            int downX, upX;
//            int YESTERDAY = -1;
//            int TOMORROW = 1;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    downX = (int) event.getX();
//                    Log.i("event.getX()", " downX " + downX);
//                    return true;
//                }
//
//                else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    upX = (int) event.getX();
//                    Log.i("event.getX()", " upX " + upX);
//                    if (upX - downX > 350) {
//
//                        mainHeader.getMyCalendar().add(Calendar.DAY_OF_MONTH, YESTERDAY);
//                        String chosenDate = mainHeader.getSlashesFormat().format(myCalendar.getTime());
//                        mainHeader.getCurrentDate().setText(chosenDate);
//                        String weekDay = mainHeader.getWeekDayFormat().format(myCalendar.getTime());
//                        mainHeader.getDayOfTheWeek().setText(weekDay);
//                    }
//
//                    else if (downX - upX > 350) {
//
//                        mainHeader.getMyCalendar().add(Calendar.DAY_OF_MONTH, TOMORROW);
//                        String chosenDate = mainHeader.getSlashesFormat().format(myCalendar.getTime());
//                        mainHeader.getCurrentDate().setText(chosenDate);
//                        String weekDay = mainHeader.getWeekDayFormat().format(myCalendar.getTime());
//                        mainHeader.getDayOfTheWeek().setText(weekDay);
//                    }
//                    return true;
//
//                }
//                return false;
//            }
//        });


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
                } else {
                    noEvents.setText("");
                }
            }
        });

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
            String description = data.getStringExtra(AddUpdateEventActivity.EXTRA_DESCRIPTION);
            String startTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_STARTTIME);
            OffsetDateTime startTime = converter.convertMashedDateToString(startTimeString);
            String finishTimeString = data.getStringExtra(AddUpdateEventActivity.EXTRA_FINISHTIME);
            String icon = data.getStringExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID);
            OffsetDateTime finishTime = converter.convertMashedDateToString(finishTimeString);
            long unixStart = 0;

            Event newEvent = new Event(description, startTime, finishTime, unixStart, icon);

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
            String icon = data.getStringExtra(AddUpdateEventActivity.EXTRA_ICON_RESOURCEID);

            Event event = new Event(description, startTime, finishTime, 0, icon);
            event.setId(id);
            eventViewModel.update(event);

            Toast.makeText(this, "Event updated", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
