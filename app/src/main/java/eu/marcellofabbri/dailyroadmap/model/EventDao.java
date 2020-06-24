package eu.marcellofabbri.dailyroadmap.model;

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

    @Query("SELECT * FROM Event_table WHERE instr(startTime, :selectedDate) > 0 OR instr(finishTime, :selectedDate) ORDER BY startTime ASC")
    LiveData<List<Event>> getCertainEvents(String selectedDate);

    @Query("DELETE FROM Event_table WHERE instr(startTime, :startTime) > 0")
    void deleteTodayEvents(String startTime);

    @Query("SELECT * FROM Event_table WHERE id = :id")
    Event getEventById(int id);

    @Query("SELECT * FROM Event_table ORDER BY id DESC")
    LiveData<List<Event>> getEventsOrderedById();
}
