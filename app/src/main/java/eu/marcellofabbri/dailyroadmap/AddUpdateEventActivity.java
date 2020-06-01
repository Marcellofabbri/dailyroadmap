package eu.marcellofabbri.dailyroadmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddUpdateEventActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "eu.marcellofabbri.dailyroadmap.EXTRA_ID";
    public static final String EXTRA_DESCRIPTION = "eu.marcellofabbri.dailyroadmap.EXTRA_DESCRIPTION";
    public static final String EXTRA_STARTTIME = "eu.marcellofabbri.dailyroadmap.EXTRA_STARTTIME";
    public static final String EXTRA_FINISHTIME = "eu.marcellofabbri.dailyroadmap.EXTRA_FINISHTIME";

    public EditText etDescription;
    public EditText etStartDate;
    public EditText etFinishDate;
    public EditText etStartTime;
    public EditText etFinishTime;

    private void assignEditTextsToFields() {
        etDescription = (EditText) findViewById(R.id.edit_text_description);
        etStartDate = (EditText) findViewById(R.id.edit_text_startDate);
        etFinishDate = (EditText) findViewById(R.id.edit_text_finishDate);
        etStartTime = (EditText) findViewById(R.id.edit_text_startTime);
        etFinishTime = (EditText) findViewById(R.id.edit_text_finishTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        assignEditTextsToFields();

        new DatePickerPrompter(etStartDate).listenForClicks();
        new DatePickerPrompter(etFinishDate).listenForClicks();
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
        }
    }

    public void saveEvent() {
        String description = etDescription.getText().toString();
        String startTime = etStartDate.getText().toString() + etStartTime.getText().toString();
        String finishTime = etFinishDate.getText().toString() + etFinishTime.getText().toString();

        if (startTime.trim().isEmpty() || finishTime.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Insert a valid description or input times", Toast.LENGTH_SHORT).show();
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
}
