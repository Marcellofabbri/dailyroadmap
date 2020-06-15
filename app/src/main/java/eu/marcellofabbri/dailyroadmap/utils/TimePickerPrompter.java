package eu.marcellofabbri.dailyroadmap.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.SimpleTimeZone;

public class TimePickerPrompter {

    private EditText editText;
    private Calendar myCalendar;
    private Optional<EditText> optionalEditText;

    public TimePickerPrompter(EditText editText, Optional<EditText> optionalEditText) {
        this.editText = editText;
        this.myCalendar = calendarInstance();
        this.optionalEditText = optionalEditText;
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

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    updateLabel(editText);
                    if (optionalEditText.isPresent()) {
                        pegAnotherEditText(optionalEditText.get());
                    }
                }

            };

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String timeString = editText.getText().toString();
                if (timeString.isEmpty()) {
                    new TimePickerDialog(context, startTime, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
                } else {
                    try {
                        Date time = new SimpleDateFormat("h:mm a").parse(timeString);
                        myCalendar.setTime(time);
                        new TimePickerDialog(context, startTime, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void pegAnotherEditText(EditText anotherEditText) {
        if (anotherEditText.getText().toString().isEmpty() && !editText.getText().toString().isEmpty()) {
            anotherEditText.setText(editText.getText().toString());
        }
    }
}

