package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class EventDetailsDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "EventDetailsDialogFragm";
    private static final String ARG_EVENT = "event";

    public static final String EXTRA_EVENT = "pl.student.pwr.gluszczak.pawel.findyourrpg.event";


    //Views
    TextView mTitle, mDate, mSystem, mMinExp, mRecExp, mPlayers, mGMName, mGMGamesPlayed, mDescription;
    CircleImageView mGMImage, mSystemImage;
    RatingBar mGMRating;
    RecyclerView mRecyclerView;

    public static EventDetailsDialogFragment newInstance(Event event) {
        Log.d(TAG, "newInstance: Creating new instance of EventDialog");
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);

        EventDetailsDialogFragment fragment = new EventDetailsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Event event = getArguments().getParcelable(ARG_EVENT);
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_event_details, null);

        initializeComponents(view);
        updateUI(event);

        return null;
    }

    private void updateUI(Event event) {
        mTitle.setText(event.getTitle());
        //image for system
        mDate.setText(event.getDate().toString());
        mSystem.setText(event.getSystem());
        mMinExp.setText(event.getMin_exp());


    }


    private void initializeComponents(View view) {
        mTitle = view.findViewById(R.id.event_detail_title);
        mDate = view.findViewById(R.id.event_detail_date);
        mSystem = view.findViewById(R.id.event_detail_system);
        mMinExp = view.findViewById(R.id.event_detail_min_exp);
        mRecExp = view.findViewById(R.id.event_detail_rec_exp);
        mPlayers = view.findViewById(R.id.event_detail_players_needed);
        mGMName = view.findViewById(R.id.row_participator_name);
        mGMGamesPlayed = view.findViewById(R.id.row_participator_games_played);
        mDescription = view.findViewById(R.id.event_detail_description);
        mGMImage = view.findViewById(R.id.row_participator_image);
        mSystemImage = view.findViewById(R.id.event_detail_image);
        mGMRating = view.findViewById(R.id.row_participator_rating);
        mRecyclerView = view.findViewById(R.id.event_detail_recycler_participants);
        mSystem.setEnabled(true);

    }


    //Try to update UI async
//    private class AsyncUploadUI extends AsyncTask<View, Void, Event>{
//        @Override
//        protected void onPostExecute(Event event) {
//            super.onPostExecute(event);
//        }
//
//        @Override
//        protected Event doInBackground(View... views) {
//            return null;
//        }
//    }
}
