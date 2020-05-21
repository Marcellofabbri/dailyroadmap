package eu.marcellofabbri.dailyroadmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private List<Event> events = new ArrayList<>();

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        Event currentEvent = events.get(position);
        holder.textViewDescription.setText(currentEvent.getDescription());
        holder.textViewStartTime.setText(currentEvent.getStartTime().toString());
        holder.textViewFinishTime.setText(currentEvent.getFinishTime().toString());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    class EventHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewStartTime;
        private TextView textViewFinishTime;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewStartTime = itemView.findViewById(R.id.text_view_start_time);
            textViewFinishTime = itemView.findViewById(R.id.text_view_finish_time);
        }
    }
}
