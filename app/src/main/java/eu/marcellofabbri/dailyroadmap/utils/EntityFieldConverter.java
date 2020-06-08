package eu.marcellofabbri.dailyroadmap.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.anychart.scales.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class EntityFieldConverter {
    @TypeConverter
    public String convertToString(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toString();
    }

    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public OffsetDateTime convertToOffsetDateTime(String string) {
        return OffsetDateTime.parse(string);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public OffsetDateTime convertDayStringToOffsetDateTime(String string) {
        int day = Integer.parseInt(string.substring(0, 2));
        int month = Integer.parseInt(string.substring(3, 5));
        int year = Integer.parseInt(string.substring(6, 8));

        return OffsetDateTime.of(LocalDateTime.of((2000 + year), month, day, 0, 0, 0), ZoneOffset.ofHours(0));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String extractTime(OffsetDateTime offsetDateTime) {
        LocalTime lt = offsetDateTime.toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return lt.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String extractDate(OffsetDateTime offsetDateTime) {
        Instant instant = offsetDateTime.toInstant();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        return formatter.format(Date.from(instant));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public OffsetDateTime convertMashedDateToString(String string) {
        //dd/MM/yy h:mm a
        if (string.length() == 16) {
            int day = Integer.parseInt(string.substring(0, 2));
            int month = Integer.parseInt(string.substring(3, 5));
            int year = Integer.parseInt(string.substring(6, 8));
            int hour = (string.substring(14).equals("AM")) ? (string.substring(8, 10).equals("12") ? Integer.parseInt(string.substring(8, 10)) - 12 : Integer.parseInt(string.substring(8, 10))) :
                    (string.substring(8, 10).equals("12") ? Integer.parseInt(string.substring(8, 10)) : Integer.parseInt(string.substring(8, 10)) + 12);
            int minutes = Integer.parseInt(string.substring(11, 13));

            return OffsetDateTime.of(LocalDateTime.of((2000 + year), month, day, hour, minutes, 0), ZoneOffset.ofHours(0));
        } else {
            int day = Integer.parseInt(string.substring(0, 2));
            int month = Integer.parseInt(string.substring(3, 5));
            int year = Integer.parseInt(string.substring(6, 8));
            int hour = (string.substring(13).equals("AM")) ? Integer.parseInt(string.substring(8, 9)) : Integer.parseInt(string.substring(8, 9)) + 12;
            int minutes = Integer.parseInt(string.substring(10, 12));

            return OffsetDateTime.of(LocalDateTime.of((2000 + year), month, day, hour, minutes, 0), ZoneOffset.ofHours(0));
        }
    }
}
