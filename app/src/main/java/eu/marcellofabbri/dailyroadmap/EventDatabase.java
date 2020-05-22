package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDateTime;
import java.time.Month;

@Database(entities = Event.class, version = 1)
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
            new PopulateDbAsyncTask(instance).execute();
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
            eventDao.insert(new Event("event description", LocalDateTime.of(2015, Month.JANUARY, 01, 8, 00, 00).toString(), LocalDateTime.of(2015, Month.JANUARY, 01, 9, 00, 00).toString()));
            eventDao.insert(new Event("event description", LocalDateTime.of(2015, Month.JANUARY, 01, 10, 00, 00).toString(), LocalDateTime.of(2015, Month.JANUARY, 01, 11, 00, 00).toString()));
            return null;
        }
    }
}
