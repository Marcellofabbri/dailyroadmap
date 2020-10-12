package eu.marcellofabbri.dailyroadmap.utils;
import java.util.Calendar;
import java.util.Date;

public class CurrentHour {
    private Calendar calendarInstance;

    public CurrentHour(Calendar calendarInstance) {
        this.calendarInstance = calendarInstance;
    }

    public int hourOfDay() {
        Date date = calendarInstance.getTime();
        String hourString = date.toString().substring(11,13);
        int hourInteger = Integer.valueOf(hourString);
        return hourInteger;
    }
}
