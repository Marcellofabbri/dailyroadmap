package eu.marcellofabbri.dailyroadmap;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM Event_table")
    void deleteAllEvents();

    @Query("SELECT * FROM Event_table ORDER BY startTime DESC")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM Event_table WHERE instr(startTime, :selectedDate) > 0 OR instr(finishTime, :selectedDate)")
    LiveData<List<Event>> getCertainEvents(String selectedDate);
}
