package eu.marcellofabbri.dailyroadmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {

            @Override
            public void onChanged(List<Event> events) {
                //RecyclerView
            }
        });
    }
}
