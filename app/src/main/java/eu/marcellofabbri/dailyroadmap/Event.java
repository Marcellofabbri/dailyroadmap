package eu.marcellofabbri.dailyroadmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.TypeConverters;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

@Entity(tableName = "Event_table")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    @TypeConverters(EntityFieldConverter.class)
    private OffsetDateTime startTime;
    @TypeConverters(EntityFieldConverter.class)
    private OffsetDateTime finishTime;
    private long startUnix;
    private String icon;

    public Event(String description, OffsetDateTime startTime, OffsetDateTime finishTime, long startUnix, String icon) {
        this.description = description;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.startUnix = startUnix;
        this.icon = icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public OffsetDateTime getFinishTime() {
        return finishTime;
    }

    public int getId() {
        return id;
    }

    public long getStartUnix() { return startUnix; }

    public void setStartUnix(Integer unix) { this.startUnix = unix; }

    public String getIcon() { return icon; }

}
