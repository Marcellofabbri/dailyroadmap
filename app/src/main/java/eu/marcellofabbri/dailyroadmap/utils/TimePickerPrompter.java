package eu.marcellofabbri.dailyroadmap.utils;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class TimePickerPrompter {

    private EditText editText;
    private Calendar myCalendar;

    public TimePickerPrompter(EditText editText) {
        this.editText = editText;
        this.myCalendar = calendarInstance();
    }

    public EditText getEditText() {
        return editText;
    }

    private Calendar calendarInstance() {
        return Calendar.getInstance();
    }

    private void updateLabel(EditText editText) {
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void listenForClicks() {

        editText.setOnClickListener(new View.OnClickListener() {
            TimePickerDialog.OnTimeSetListener startTime = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    updateLabel(editText);
                }

            };

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                new TimePickerDialog(context, startTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });
    }
}

