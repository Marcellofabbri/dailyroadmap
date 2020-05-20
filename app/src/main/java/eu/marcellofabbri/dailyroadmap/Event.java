package eu.marcellofabbri.dailyroadmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "Event_table")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    public Event(String description, LocalDateTime startTime, LocalDateTime finishTime) {
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }
}
