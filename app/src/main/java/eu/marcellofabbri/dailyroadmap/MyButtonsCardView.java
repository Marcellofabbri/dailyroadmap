package eu.marcellofabbri.dailyroadmap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MyButtonsCardView extends CardView {
    private FloatingActionButton calendarButton;
    private MainHeader mainHeader;

    public void setCalendarButton(FloatingActionButton calendarButton) {
        this.calendarButton = calendarButton;
    }

    public void setMainHeader(MainHeader mainHeader) {
        this.mainHeader = mainHeader;
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


}
