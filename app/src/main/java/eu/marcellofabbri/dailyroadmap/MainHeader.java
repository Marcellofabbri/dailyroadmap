package eu.marcellofabbri.dailyroadmap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainHeader extends RelativeLayout {
    private EditText currentDate;
    private FloatingActionButton calendarButton;
    private EditText dayOfTheWeek;
    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat slashesFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

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
        dayOfTheWeek = findViewById(R.id.day_of_the_week);
    }

    public void bootElements() {
        bootHeaderDate();
        bootCalendarButton();
        bootDayOfTheWeek();
    }

    private void bootDayOfTheWeek() {
        String weekDay = weekDayFormat.format(myCalendar.getTime());
        dayOfTheWeek.setText(weekDay);
    }

    private void bootHeaderDate() {
        String today = slashesFormat.format(myCalendar.getTime());
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
                    String chosenDate = slashesFormat.format(myCalendar.getTime());
                    currentDate.setText(chosenDate);
                    String weekDay = weekDayFormat.format(myCalendar.getTime());
                    dayOfTheWeek.setText(weekDay);
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
