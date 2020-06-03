package eu.marcellofabbri.dailyroadmap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainHeader extends LinearLayout {
    private EditText currentDate;
    private FloatingActionButton calendarButton;
    private Calendar myCalendar = Calendar.getInstance();
    public MutableLiveData<String> currentDateText = new MutableLiveData<>();

    public MainHeader(Context context) {
        super(context);
    }

    public MainHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditText getCurrentDate() {
        return currentDate;
    }

    public void identifyFields() {
        currentDate = findViewById(R.id.header_date);
        calendarButton = findViewById(R.id.calendar_button);
    }

    public void bootElements() {
        bootHeaderDate();
        bootCalendarButton();
    }

    private void bootHeaderDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String today = sdf.format(Calendar.getInstance().getTime());
        currentDate.setText(today);
    }

    private void bootCalendarButton() {
        calendarButton.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "dd/MM/yy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    String chosenDate = sdf.format(myCalendar.getTime());
                    currentDate.setText(chosenDate);
                    currentDateText.setValue(chosenDate);
                }
            };

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

}
