package eu.marcellofabbri.dailyroadmap.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateTimeFormatter {
    public SimpleDateFormat dateWithSlashes = new SimpleDateFormat("dd/MM/yy");
    public SimpleDateFormat timeWithOneHour = new SimpleDateFormat("h:mm a");
    public SimpleDateFormat timeWithTwoHours = new SimpleDateFormat("hh:mm a");
    public SimpleDateFormat fullDateTimeLong = new SimpleDateFormat("dd/MM/yy hh:mm a");
    public SimpleDateFormat fullDateTimeShort = new SimpleDateFormat("dd/MM/yy h:mm a");

    public SimpleDateFormat fullDateTime(String string) {
        if (string.length() == 16) {
            return fullDateTimeLong;
        } else {
            return fullDateTimeShort;
        }
    }

    private int hourInterpreter(String hour, String a) {
        if (hour == "12" && a == "PM") {
            return 12;
        } else if (hour == "12" && a == "AM") {
            return 0;
        } else if (hour != "12" && a == "PM") {
            return Integer.parseInt(hour + 12);
        } else {
            return Integer.parseInt(hour);
        }
    }

//    public Date stringToDate(String string) {
//        if (string.length() == 16) {
//            int day = Integer.parseInt(string.substring(0, 2));
//            int month = Integer.parseInt(string.substring(3, 5));
//            int year = Integer.parseInt(string.substring(6, 8));
//            int hour = hourInterpreter(string.substring(8, 10), string.substring(14));
//            int minutes = Integer.parseInt(string.substring(11, 13));
//            return new Date(day, month, year, hour, minutes);
//        } else {
//            int day = Integer.parseInt(string.substring(0, 2));
//            int month = Integer.parseInt(string.substring(3, 5));
//            int year = Integer.parseInt(string.substring(6, 8));
//            int hour = hourInterpreter(string.substring(8, 9), string.substring(13));
//            int minutes = Integer.parseInt(string.substring(10, 12));
//        }
//    }
}
