package eu.marcellofabbri.dailyroadmap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyButtonsCardView extends CardView {
    private FloatingActionButton calendarButton;
    private MainHeader mainHeader;
    private FloatingActionButton deleteButton;
    private FloatingActionButton todayButton;
    private TextView dayNumberTextView;

    public void setCalendarButton(FloatingActionButton calendarButton) {
        this.calendarButton = calendarButton;
    }

    public void setDayNumberTextView(TextView textView) {
        this.dayNumberTextView = textView;
    }

    public void setTodayButton(FloatingActionButton todayButton) {
        this.todayButton = todayButton;
    }

    public void setMainHeader(MainHeader mainHeader) {
        this.mainHeader = mainHeader;
    }

    public void setDeleteButton(FloatingActionButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public MyButtonsCardView(@NonNull Context context) {
        super(context);
    }

    public MyButtonsCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButtonsCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bootCalendarButton() {
        calendarButton.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar myCalendar = mainHeader.getMyCalendar();
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String chosenDate = mainHeader.getSlashesFormat().format(myCalendar.getTime());
                    mainHeader.getCurrentDate().setText(chosenDate);
                    String weekDay = mainHeader.getWeekDayFormat().format(myCalendar.getTime());
                    mainHeader.getDayOfTheWeek().setText(weekDay);
                }
            };

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Calendar myCalendar = mainHeader.getMyCalendar();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    public void writeDayNumberTextView() {
        SimpleDateFormat dayNumberFormat = new SimpleDateFormat("dd");
        Date currentDate = new Date();
        String dayNumberString = dayNumberFormat.format(currentDate);
        dayNumberTextView.setText(dayNumberString);
    }

    public void bootTodayButton() {
        todayButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Date today = new Date();
                String todayDate = mainHeader.getSlashesFormat().format(today);
                mainHeader.getCurrentDate().setText(todayDate);
                String weekDay = mainHeader.getWeekDayFormat().format(today);
                mainHeader.getDayOfTheWeek().setText(weekDay);
                mainHeader.getMyCalendar().setTime(today);
            }
        });
    }

}
