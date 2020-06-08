package eu.marcellofabbri.dailyroadmap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainHeader extends ConstraintLayout {
    private EditText currentDate;
    private FloatingActionButton calendarButton;
    private EditText dayOfTheWeek;
    private Button leftButton;
    private Button rightButton;
    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat slashesFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());

    public MainHeader(Context context) {
        super(context);
    }

    public MainHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLeftButton(Button leftButton) {
        this.leftButton = leftButton;
    }

    public void setRightButton(Button rightButton) {
        this.rightButton = rightButton;
    }

    public EditText getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public SimpleDateFormat getSlashesFormat() {
        return slashesFormat;
    }

    public SimpleDateFormat getWeekDayFormat() {
        return weekDayFormat;
    }

    public Calendar getMyCalendar() {
        return myCalendar;
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
        bootDayOfTheWeek();
        bootRightLeftButtons();
    }

    private void bootDayOfTheWeek() {
        String weekDay = weekDayFormat.format(myCalendar.getTime());
        dayOfTheWeek.setText(weekDay);
    }

    private void bootHeaderDate() {
        String today = slashesFormat.format(myCalendar.getTime());
        currentDate.setText(today);
        currentDate.setShadowLayer(2, 0, 0, Color.GRAY);
    }

    public void bootRightLeftButtons() {
        rightButton = findViewById(R.id.right_button);
        leftButton = findViewById(R.id.left_button);

        for (int i = 0; i < 2; i++) {
            final Integer amount;
            Button button;
            if (i == 0) {
                button = rightButton;
                amount = 1;
            } else {
                button = leftButton;
                amount = -1;
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myCalendar.add(Calendar.DAY_OF_MONTH, amount);
                    String chosenDate = getSlashesFormat().format(myCalendar.getTime());
                    getCurrentDate().setText(chosenDate);
                    String weekDay = getWeekDayFormat().format(myCalendar.getTime());
                    getDayOfTheWeek().setText(weekDay);
                }
            });
        }
    }


}
