package eu.marcellofabbri.dailyroadmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "Event_table")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private String startTime;
    private String finishTime;

    public Event(String description, String startTime, String finishTime) {
        this.description = description;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public int getId() {
        return id;
    }
}
