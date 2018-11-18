package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToDateString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToTimeString;

public class CreatingAdapter extends RecyclerView.Adapter<CreatingAdapter.ViewHolder> {


    public List<Event> mEvents;

    public CreatingAdapter(List<Event> events) {
        Collections.sort(events);
        mEvents = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_event, parent, false);
        CreatingAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Event event = mEvents.get(position);
        viewHolder.updateUI(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, date, time, system;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.row_event_title);
            date = itemView.findViewById(R.id.row_event_date);
            time = itemView.findViewById(R.id.row_event_time);
            system = itemView.findViewById(R.id.row_event_system);
            system.setSelected(true);
        }

        public void updateUI(Event event) {
            title.setText(event.getTitle());
            date.setText(dateToDateString(event.getDate()));
            time.setText(dateToTimeString(event.getDate()));
            system.setText(event.getSystem());
        }
    }
}
