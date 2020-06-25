package eu.marcellofabbri.dailyroadmap.view.activityHelpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eu.marcellofabbri.dailyroadmap.R;

public class MainHeader extends ConstraintLayout {
    private EditText currentDate;
    private EditText dayOfTheWeek;
    private Button leftButton;
    private Button rightButton;
    private FloatingActionButton todayButton;
    private TextView dayNumberTextView;
    private FloatingActionButton addButton;
    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat slashesFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    private SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
    private int[] defaultColors = new int[]{R.color.white, R.color.daytimeBackgroundDarker};
    private int selectedColorPosition = 1;

    public MainHeader(Context context) {
        super(context);
    }

    public MainHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    public int[] getDefaultColors() {
        return defaultColors;
    }

    public void identifyFields() {
        currentDate = findViewById(R.id.header_date);
        dayOfTheWeek = findViewById(R.id.day_of_the_week);
        todayButton = findViewById(R.id.today_button);
        addButton = findViewById(R.id.button_add_event);
        dayNumberTextView = findViewById(R.id.day_number);
    }

    public void bootElements() {
        bootHeaderDate();
        bootDayOfTheWeek();
        bootRightLeftButtons();
        writeDayNumberTextView();
        bootTodayButton();
    }

    private void bootDayOfTheWeek() {
        String weekDay = weekDayFormat.format(myCalendar.getTime());
        dayOfTheWeek.setText(weekDay);
        dayOfTheWeek.setTextColor(ContextCompat.getColor(getContext(), defaultColors[selectedColorPosition]));
    }

    private void bootHeaderDate() {
        String today = slashesFormat.format(myCalendar.getTime());
        currentDate.setText(today);
        currentDate.setShadowLayer(2, 0, 0, Color.GRAY);
        currentDate.setTextColor(ContextCompat.getColor(getContext(), defaultColors[selectedColorPosition]));
    }

    public void bootRightLeftButtons() {
        rightButton = findViewById(R.id.right_button);
        leftButton = findViewById(R.id.left_button);

        for (int i = 0; i < 2; i++) {
            final Integer amount;
            Button button;
            Drawable drawable;
            if (i == 0) {
                button = rightButton;
                amount = 1;
                drawable = button.getCompoundDrawables()[0];
            } else {
                button = leftButton;
                amount = -1;
                drawable = button.getCompoundDrawables()[2];
            }


            drawable.setTint(ContextCompat.getColor(getContext(), defaultColors[selectedColorPosition]));

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

    public void bootDateClick() {
        currentDate.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar myCalendar = getMyCalendar();
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String chosenDate = getSlashesFormat().format(myCalendar.getTime());
                    getCurrentDate().setText(chosenDate);
                    String weekDay = getWeekDayFormat().format(myCalendar.getTime());
                    getDayOfTheWeek().setText(weekDay);
                }
            };

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Calendar myCalendar = getMyCalendar();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void writeDayNumberTextView() {
        SimpleDateFormat dayNumberFormat = new SimpleDateFormat("dd");
        Date currentDate = new Date();
        String dayNumberString = dayNumberFormat.format(currentDate);
        dayNumberTextView.setText(dayNumberString);
    }

    private void bootTodayButton() {
        todayButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Date today = new Date();
                String todayDate = getSlashesFormat().format(today);
                getCurrentDate().setText(todayDate);
                String weekDay = getWeekDayFormat().format(today);
                getDayOfTheWeek().setText(weekDay);
                getMyCalendar().setTime(today);
            }
        });
    }


}
