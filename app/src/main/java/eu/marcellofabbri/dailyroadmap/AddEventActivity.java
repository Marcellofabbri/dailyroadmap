package eu.marcellofabbri.dailyroadmap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    public EditText etStartDate;
    public EditText etFinishDate;
    public EditText etStartTime;
    public EditText etFinishTime;

    private void assignEditTextsToFields() {
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
    }
}
