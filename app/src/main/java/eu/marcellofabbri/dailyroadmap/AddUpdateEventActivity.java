package eu.marcellofabbri.dailyroadmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import eu.marcellofabbri.dailyroadmap.utils.DatePickerPrompter;
import eu.marcellofabbri.dailyroadmap.utils.MyIconsAlertFacilitator;
import eu.marcellofabbri.dailyroadmap.utils.TimePickerPrompter;

public class AddUpdateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_ID = "eu.marcellofabbri.dailyroadmap.EXTRA_ID";
    public static final String EXTRA_DESCRIPTION = "eu.marcellofabbri.dailyroadmap.EXTRA_DESCRIPTION";
    public static final String EXTRA_STARTTIME = "eu.marcellofabbri.dailyroadmap.EXTRA_STARTTIME";
    public static final String EXTRA_FINISHTIME = "eu.marcellofabbri.dailyroadmap.EXTRA_FINISHTIME";

    public EditText etDescription;
    public EditText etStartDate;
    public EditText etFinishDate;
    public EditText etStartTime;
    public EditText etFinishTime;
    public EditText etIcon;

    private void assignEditTextsToFields() {
        etDescription = findViewById(R.id.edit_text_description);
        etStartDate = findViewById(R.id.edit_text_startDate);
        etFinishDate = findViewById(R.id.edit_text_finishDate);
        etStartTime = findViewById(R.id.edit_text_startTime);
        etFinishTime = findViewById(R.id.edit_text_finishTime);
        etIcon = findViewById(R.id.edit_text_icon);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        assignEditTextsToFields();

        new DatePickerPrompter(etStartDate, etFinishDate).listenForClicks();
        //new DatePickerPrompter(etFinishDate).listenForClicks();
        new TimePickerPrompter(etStartTime).listenForClicks();
        new TimePickerPrompter(etFinishTime).listenForClicks();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit task");
            etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            etStartDate.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(0, 8));
            etFinishDate.setText(intent.getStringExtra(EXTRA_FINISHTIME).substring(0, 8));
            etStartTime.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(8));
            etFinishTime.setText(intent.getStringExtra(EXTRA_FINISHTIME).substring(8));
        } else {
            setTitle("Add task");
            etStartDate.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(0, 8));
            etFinishDate.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(0, 8));
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.icons_array, R.layout.individual_icon_textview);
        MyIconsAlertFacilitator facilitator = new MyIconsAlertFacilitator(adapter, this);
        GridView gridView = facilitator.getGridView();
        AlertDialog.Builder builder = facilitator.getBuilder();
        AlertDialog alertDialog = builder.create();

        etIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                etIcon.setText(text);
                alertDialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEvent() {
        DateTimeFormatter dtfLong = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dtfShort = DateTimeFormatter.ofPattern("h:mm a");
        String description = etDescription.getText().toString();
        String startTime = etStartDate.getText().toString() + etStartTime.getText().toString();
        LocalTime start = startTime.length() == 16 ? LocalTime.parse(startTime.substring(8), dtfLong) : LocalTime.parse(startTime.substring(8), dtfShort);
        String finishTime = etFinishDate.getText().toString() + etFinishTime.getText().toString();
        LocalTime finish = finishTime.length() == 16 ? LocalTime.parse(finishTime.substring(8), dtfLong).minusMinutes(1) : LocalTime.parse(finishTime.substring(8), dtfShort).minusMinutes(1);

        if (startTime.trim().isEmpty() || finishTime.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Insert a valid description or input times", Toast.LENGTH_SHORT).show();
            return;
        }
        if (start.isAfter(finish)) {
            Toast.makeText(this, "Finish time must be after start time", Toast.LENGTH_SHORT).show();
            return;
        }

        //to send data back to the activity that started this activity
        Intent data = new Intent();
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_STARTTIME, startTime);
        data.putExtra(EXTRA_FINISHTIME, finishTime);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);
        return true;
    }

    //to handle clicks on the Menu
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_event:
                saveEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
