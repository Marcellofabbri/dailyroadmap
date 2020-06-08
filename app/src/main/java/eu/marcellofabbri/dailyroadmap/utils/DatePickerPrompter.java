package eu.marcellofabbri.dailyroadmap.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerPrompter {

    private EditText editText;
    private EditText secondaryEditText;
    private Calendar myCalendar;
    private long minimumDate;

    public DatePickerPrompter(EditText editText) {
        this.editText = editText;
        this.myCalendar = calendarInstance();
    }

    public DatePickerPrompter(EditText editText, EditText secondaryEditText) {
        this.editText = editText;
        this.secondaryEditText = secondaryEditText;
        this.myCalendar = calendarInstance();
    }

    public EditText getEditText() {
        return editText;
    }

    private Calendar calendarInstance() {
        return Calendar.getInstance();
    }

    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editText.setText(sdf.format(myCalendar.getTime()));
        secondaryEditText.setText(sdf.format(myCalendar.getTime()));
    }

    public void listenForClicks() {

        editText.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(editText);
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
