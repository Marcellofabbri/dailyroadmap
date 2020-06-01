package eu.marcellofabbri.dailyroadmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private List<Event> events = new ArrayList<>();
    private boolean isRotate = false;
    private OnDeleteButtonClickListener listener;

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventHolder holder, int position) {
        Event currentEvent = events.get(position);
        holder.textViewDescription.setText(currentEvent.getDescription());
        holder.textViewStartTime.setText(currentEvent.getStartTime().substring(8));
        holder.textViewFinishTime.setText(currentEvent.getFinishTime().substring(8));
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimator.rotateFab(v, !isRotate);
                if(isRotate){
                    ViewAnimator.showIn(holder.updateButton);
                    ViewAnimator.showIn(holder.deleteButton);
                }else{
                    ViewAnimator.showOut(holder.updateButton);
                    ViewAnimator.showOut(holder.deleteButton);
                }
            }
        });
        ViewAnimator.init(holder.updateButton);
        ViewAnimator.init(holder.deleteButton);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public Event getEventAt(int position) {
        return events.get(position);
    }

    class EventHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewStartTime;
        private TextView textViewFinishTime;
        private FloatingActionButton editButton;
        private FloatingActionButton updateButton;
        private FloatingActionButton deleteButton;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewStartTime = itemView.findViewById(R.id.text_view_start_time);
            textViewFinishTime = itemView.findViewById(R.id.text_view_finish_time);
            editButton = itemView.findViewById(R.id.edit_button);
            updateButton = itemView.findViewById(R.id.update_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //while executing, if click happens before the update the position might be -1
                    if (listener != null && position != RecyclerView.NO_POSITION)
                    listener.onDeleteButtonClick(events.get(position));
                }
            });
        }
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(Event event);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.listener = listener;
    }
}
