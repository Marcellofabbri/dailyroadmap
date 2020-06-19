package eu.marcellofabbri.dailyroadmap.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Database(entities = Event.class, version = 3)
public abstract class EventDatabase extends RoomDatabase {

    private static EventDatabase instance;

    public abstract EventDao eventDao();

    //synchronized: there can only be one thread accessing the database
    public static synchronized EventDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), EventDatabase.class, "Event_database")
                    .addCallback(roomCallback) //to call the onCreate method
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao eventDao;

        private PopulateDbAsyncTask(EventDatabase eventDatabase) {
            eventDao = eventDatabase.eventDao();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void...voids) {
            eventDao.insert(new Event("event description", OffsetDateTime.of(LocalDateTime.of(2015, Month.JANUARY, 01, 8, 00, 00), ZoneOffset.ofHours(0)), OffsetDateTime.of(LocalDateTime.of(2015, Month.JANUARY, 01, 9, 00, 00), ZoneOffset.ofHours(0)), 0, "\uD83C\uDFF3"));
            return null;
        }
    }
}
