package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemImagesMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities.EventDetailsActivity;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities.ParticipatingDetailsActivity;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities.PastCreatedActivity;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT_DATE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToDateString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToTimeString;

public class PastCreatedAdapter extends RecyclerView.Adapter<PastCreatedAdapter.ViewHolder> {

    private static final String TAG = "PastCreatedAdapter";

    private List<Event> mEvents;
    private Context mContext;

    public PastCreatedAdapter(List<Event> events, Context context) {
        Log.d(TAG, "CreatingAdapter: ");
        Collections.sort(events);
        mEvents = events;
        mContext = context;
    }

    @NonNull
    @Override
    public PastCreatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_event, parent, false);
        PastCreatedAdapter.ViewHolder viewHolder = new PastCreatedAdapter.ViewHolder(view, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PastCreatedAdapter.ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Event event = mEvents.get(position);
        viewHolder.updateUI(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, date, time, system;
        private ImageView image;
        private SystemImagesMap systemMap;
        public CardView card;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            Log.d(TAG, "ViewHolder: ");
            image = itemView.findViewById(R.id.row_event_image);
            title = itemView.findViewById(R.id.row_event_title);
            date = itemView.findViewById(R.id.row_event_date);
            time = itemView.findViewById(R.id.row_event_time);
            system = itemView.findViewById(R.id.row_event_system);
            card = itemView.findViewById(R.id.row_event_card);
            title.setSelected(true);

            systemMap = SystemImagesMap.newInstance(context);
            system.setSelected(true);
        }

        public void updateUI(final Event event) {
            Log.d(TAG, "updateUI: ");
            image.setImageResource(systemMap.getImageForSystem(event.getSystem()));
            title.setText(event.getTitle());
            date.setText(dateToDateString(event.getDate()));
            time.setText(dateToTimeString(event.getDate()));
            system.setText(event.getSystem());
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), PastCreatedActivity.class);
                    intent.putExtra(PASS_EVENT, event);
                    intent.putExtra(PASS_EVENT_DATE, event.getDate().getTime());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
