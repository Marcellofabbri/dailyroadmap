package eu.marcellofabbri.dailyroadmap;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_EVENT_REQUEST_CODE = 1;
    private EventViewModel eventViewModel;
    float dpHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    float dpWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private MyVisualizer myVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVisualizer = (MyVisualizer) findViewById(R.id.myVisualizer);

        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_event);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final EventAdapter adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                adapter.setEvents(events);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            String description = data.getStringExtra(AddEventActivity.EXTRA_DESCRIPTION);
            String startTime = data.getStringExtra(AddEventActivity.EXTRA_STARTTIME);
            String finishTime = data.getStringExtra(AddEventActivity.EXTRA_FINISHTIME);

            Event newEvent = new Event(description, startTime, finishTime);
            eventViewModel.insert(newEvent);

            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Event not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
