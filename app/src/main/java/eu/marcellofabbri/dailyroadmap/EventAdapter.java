package eu.marcellofabbri.dailyroadmap;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter {

    class EventHolder extends RecyclerView.ViewHolder{
        private TextView TextViewDescription;
        private TextView TextViewStartTime;
        private TextView TextViewFinishTime;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
