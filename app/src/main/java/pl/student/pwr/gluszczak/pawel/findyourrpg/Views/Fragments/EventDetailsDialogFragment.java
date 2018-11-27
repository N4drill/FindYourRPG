package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.ParticipantsAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemImagesMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ExpierienceParser.expierienceNameParserLongToShort;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.playersNeededLeft;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateAverage;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateUserAverageAsGM;

public class EventDetailsDialogFragment extends AppCompatDialogFragment {


    private static final String TAG = "EventDetailsDialogFragm";
    private static final String ARG_EVENT = "event";

    public static final String EXTRA_EVENT = "pl.student.pwr.gluszczak.pawel.findyourrpg.event";


    //Views
    TextView mDescription, mDate, mSystem, mMinExp, mRecExp, mPlayers, mGMName, mGMGamesPlayed;
    CircleImageView mGMImage;
    ImageView mSystemImage;
    RatingBar mGMRating;
    RecyclerView mRecyclerView;
    ParticipantsAdapter mParticipantsAdapter;
    SystemImagesMap mSystemImagesMap;


    //Vars
    private Event mEvent;

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
        mSystemImagesMap = SystemImagesMap.newInstance(getActivity());
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_event_details, null);

        initializeComponents(view, event);
        updateUI(event);
        mEvent = event;

        AlertDialog alert = buildDialog(event, view);
        return alert;
    }

    private AlertDialog buildDialog(final Event event, View view) {
        //Dialog if user is game master
        if (isUserGameMaster(event.getGame_maser())) {

            return new AlertDialog.Builder(getActivity())
                    .setTitle(event.getTitle())
                    .setView(view)
                    .setNegativeButton(R.string.event_dialog_negative_fullList, null)
                    .create();
        }

        //Dialog with user on list
        if (isUserOnList(event.getParticipants())) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(event.getTitle())
                    .setView(view)
                    .setPositiveButton(R.string.event_dialog_possitive_onList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = ((UserClient) (getActivity().getApplicationContext())).getUser();
                            ArrayList<User> newUserList = new ArrayList<>();
                            for (User u : event.getParticipants()) {
                                if (!user.getId().equals(u.getId())) {
                                    newUserList.add(u);
                                }
                            }
                            mEvent.setParticipants(newUserList);
                            sendJoinResult(Activity.RESULT_OK, mEvent);
                        }
                    })
                    .setNegativeButton(R.string.event_dialog_negative_onList, null)
                    .create();

        }

        //User is not on the list but its full
        if (event.getParticipants().size() == event.getNeeded_players()) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(event.getTitle())
                    .setView(view)
                    .setNegativeButton(R.string.event_dialog_negative_fullList, null)
                    .create();
        }


        //Normal dialog if user is not on the list and it's not full
        return new AlertDialog.Builder(getActivity())
                .setTitle(event.getTitle())
                .setView(view)
                .setPositiveButton(R.string.event_dialog_possitive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: Joining");

                        User user = ((UserClient) (getActivity().getApplicationContext())).getUser();
                        if (user != null) {
                            ArrayList<User> newUserList = mEvent.getParticipants();
                            newUserList.add(user);
                            mEvent.setParticipants(newUserList);
                            sendJoinResult(Activity.RESULT_OK, mEvent);
                        }
                    }
                })
                .setNegativeButton(R.string.event_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastMaker.shortToast(getActivity(), "Clicked no");
                    }
                })
                .create();

    }

    private boolean isUserGameMaster(User user) {
        User currentUser = ((UserClient) (getActivity().getApplicationContext())).getUser();
        return currentUser.getId().equals(user.getId());
    }


    private boolean isUserOnList(ArrayList<User> participants) {
        User currentUser = ((UserClient) (getActivity().getApplicationContext())).getUser();
        for (User u : participants) {
            if (u.getId().equals(currentUser.getId())) return true;
        }

        return false;
    }

    private void sendJoinResult(int resultCode, Event event) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_EVENT, event);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void updateUI(Event event) {
        mDescription.setText(event.getDescription());
        mSystemImage.setImageResource(mSystemImagesMap.getImageForSystem(event.getSystem()));
        mDate.setText(event.getDate().toString());
        mSystem.setText(event.getSystem());
        mMinExp.setText(expierienceNameParserLongToShort(getActivity(), event.getMin_exp()));
        mRecExp.setText(expierienceNameParserLongToShort(getActivity(), event.getRec_exp()));
        mPlayers.setText(playersNeededLeft(event.getNeeded_players(), event.getParticipants().size()));
        mGMImage.setImageResource(event.getGame_maser().getAvatarUrl());
        mGMName.setText(event.getGame_maser().getUsername());
        mGMRating.setRating(calculateUserAverageAsGM(event.getGame_maser()));
        mGMGamesPlayed.setText(String.valueOf(event.getGame_maser().getGamesAsMaster() + event.getGame_maser().getGamesAsPlayer()));
        //Init recyclerView
        initRecyclerView(event);
        updatePlayersColor(event);
    }

    private void updatePlayersColor(Event event) {
        int left = event.getNeeded_players() - event.getParticipants().size();
        float result = (float) left / (float) event.getParticipants().size();

        if (result < 0.3f) {
            colorPlayerText(R.color.players_full);
        } else if (result < 1) {
            colorPlayerText(R.color.players_medium);
        } else {
            colorPlayerText(R.color.players_low);
        }
    }

    private void colorPlayerText(int colorResource) {
        mPlayers.setTextColor(getActivity().getColor(colorResource));
    }

    private void initRecyclerView(Event event) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mParticipantsAdapter = new ParticipantsAdapter(event.getParticipants(), event.getGame_maser(), getActivity());
        mRecyclerView.setAdapter(mParticipantsAdapter);
        mRecyclerView.setHasFixedSize(true);
    }


    private void initializeComponents(View view, Event event) {
        mDescription = view.findViewById(R.id.event_detail_desc);
        mDate = view.findViewById(R.id.event_detail_date);
        mSystem = view.findViewById(R.id.event_detail_system);
        mMinExp = view.findViewById(R.id.event_detail_min_exp);
        mRecExp = view.findViewById(R.id.event_detail_rec_exp);
        mPlayers = view.findViewById(R.id.event_detail_players_needed);
        mGMName = view.findViewById(R.id.row_participator_name);
        mGMGamesPlayed = view.findViewById(R.id.row_participator_games_played);
        mGMImage = view.findViewById(R.id.row_participator_image);
        mSystemImage = view.findViewById(R.id.event_detail_image);
        mGMRating = view.findViewById(R.id.row_participator_rating);
        mRecyclerView = view.findViewById(R.id.event_detail_recycler_participants);
        mSystem.setEnabled(true);
        mMinExp.setEnabled(true);
        mRecExp.setEnabled(true);
        mDate.setEnabled(true);

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
