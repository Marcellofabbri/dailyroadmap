package eu.marcellofabbri.dailyroadmap.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.utils.CustomColors;
import eu.marcellofabbri.dailyroadmap.R;
import eu.marcellofabbri.dailyroadmap.utils.ViewAnimator;
import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private List<Event> events = new ArrayList<>();
    private boolean isRotate = false;
    private OnButtonClickListener listener;
    private EntityFieldConverter converter = new EntityFieldConverter();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final EventHolder holder, int position) {
        Event currentEvent = events.get(position);
        ColorStateList assignedColor = ColorStateList.valueOf(Color.parseColor(holder.colors[position > 5 ? position - 6 : position]));
        //holder.editButton.setBackgroundTintList(assignedColor);
        holder.editButton.setRippleColor(assignedColor);
        holder.updateButton.setBackgroundTintList(assignedColor);
        holder.deleteButton.setBackgroundTintList(assignedColor);
        holder.textViewDescription.setText(currentEvent.getDescription());
        holder.textViewDescription.setTextColor(assignedColor);
        holder.textViewStartTime.setText(converter.extractTime(currentEvent.getStartTime()));
        holder.textViewStartTime.setTextColor(Color.DKGRAY);
        holder.textViewFinishTime.setText(converter.extractTime(currentEvent.getFinishTime()));
        holder.textViewFinishTime.setTextColor(Color.DKGRAY);
        holder.editButton.setImageResource(Integer.parseInt(currentEvent.getIcon()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimator.rotateFab(v, !isRotate);
                if(isRotate){
                    ViewAnimator.showIn(holder.updateButton);
                    ViewAnimator.showIn(holder.deleteButton);
                    holder.textViewStartTime.setTextColor(Color.GRAY);
                    holder.textViewFinishTime.setTextColor(Color.GRAY);
                    holder.textViewDescription.setTextColor(Color.GRAY);
                }else{
                    ViewAnimator.showOut(holder.updateButton);
                    ViewAnimator.showOut(holder.deleteButton);
                    holder.textViewStartTime.setTextColor(Color.DKGRAY);
                    holder.textViewFinishTime.setTextColor(Color.DKGRAY);
                    holder.textViewDescription.setTextColor(assignedColor);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public Event getEventAt(int position) {
        return events.get(position);
    }

    public List<Event> getEvents() {
        return events;
    }

    class EventHolder extends RecyclerView.ViewHolder{
        private View iv;
        private TextView textViewDescription;
        private TextView textViewStartTime;
        private TextView textViewFinishTime;
        private FloatingActionButton editButton;
        private FloatingActionButton updateButton;
        private FloatingActionButton deleteButton;
        private CustomColors myColors = new CustomColors();
        String[] colors = new String[] { "#DD1515", "#FFA928", "#2E42B5", "#128E1D", "#FF6600", "#000000"};
        String[] fadedColors = new String[] { "#A8DD1515", "#A8FFD128", "#A82E42B5", "#A8128E1D", "#B1FF6600", "#A8000000"};

        @RequiresApi(api = Build.VERSION_CODES.P)
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewStartTime = itemView.findViewById(R.id.text_view_start_time);
            textViewFinishTime = itemView.findViewById(R.id.text_view_finish_time);
            editButton = itemView.findViewById(R.id.edit_button);
            updateButton = itemView.findViewById(R.id.update_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            iv = itemView;

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //while executing, if click happens before the update the position might be -1
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onDeleteButtonClick(events.get(position));
                    }
                }
            });

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onUpdateButtonClick(events.get(position));
                    }
                }
            });
        }
    }

    public interface OnButtonClickListener {
        void onDeleteButtonClick(Event event);
        void onUpdateButtonClick(Event event);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

}
