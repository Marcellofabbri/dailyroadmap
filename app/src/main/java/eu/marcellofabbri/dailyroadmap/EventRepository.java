package eu.marcellofabbri.dailyroadmap;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.time.OffsetDateTime;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    public EventRepository(Application application) {
        EventDatabase eventDatabase = EventDatabase.getInstance(application);
        eventDao = eventDatabase.eventDao(); //abstract method implemented by Room automatically
        allEvents = eventDao.getAllEvents();
    }

    //database operations

    public void insert(Event event) {
        System.out.println("INSERTING");
        System.out.println(event.getStartTime());
        new InsertEventAsyncTask(eventDao).execute(event);
    }

    public void update(Event event) {
        new UpdateEventAsyncTask(eventDao).execute(event);
    }

    public void delete(Event event) {
        new DeleteEventAsyncTask(eventDao).execute(event);
    }

    public void deleteAllEvents() {
        new DeleteAllEventsAsyncTask(eventDao).execute();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public LiveData<List<Event>> getCertainEvents(OffsetDateTime selectedDate) {
        String selectedDateString = new EntityFieldConverter().convertToString(selectedDate).substring(0, 10);
        return eventDao.getCertainEvents(selectedDateString);
    }

    public void deleteTodayEvents(OffsetDateTime startTime) {
        String startTimeString = new EntityFieldConverter().convertToString(startTime);
        new DeleteTodayEventsAsyncTask(eventDao).execute(startTimeString);
    };

    private static class InsertEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        InsertEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }
        @Override
        protected Void doInBackground(Event... events) {
            eventDao.insert(events[0]);
            return null;
        }
    }

    private static class UpdateEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        UpdateEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }
        @Override
        protected Void doInBackground(Event... events) {
            eventDao.update(events[0]);
            return null;
        }
    }

    private static class DeleteEventAsyncTask extends AsyncTask<Event, Void, Void> {
        private EventDao eventDao;

        DeleteEventAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }
        @Override
        protected Void doInBackground(Event... events) {
            eventDao.delete(events[0]);
            return null;
        }
    }

    private static class DeleteAllEventsAsyncTask extends AsyncTask<Void, Void, Void> {
        private EventDao eventDao;

        DeleteAllEventsAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            eventDao.deleteAllEvents();
            return null;
        }
    }

    private static class DeleteTodayEventsAsyncTask extends AsyncTask<String, Void, Void> {
        private EventDao eventDao;

        DeleteTodayEventsAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            eventDao.deleteTodayEvents(strings[0]);
            return null;
        }
    }
}
