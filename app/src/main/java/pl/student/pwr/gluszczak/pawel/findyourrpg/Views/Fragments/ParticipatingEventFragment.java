package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.ParticipantsAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemImagesMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.EventUtils.prepareUpdateDatabaseList;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateUserAverageAsGM;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateUserGames;

public class ParticipatingEventFragment extends Fragment {

    private static final String TAG = "ParticipatingEventFragm";

    private static final String ARG_EVENT = "event";
    private static final String ARG_EVENT_DATE = "eventDate";

    //Views
    private TextView mTitle, mDate, mMasterName, mMasterGames;
    private ImageView mSysImage;
    private CircleImageView mMasterImage;
    private RatingBar mMasterRating;
    private RecyclerView mRecyclerView;
    private Button mButton;

    //Vars
    private Event event;
    private SystemImagesMap mSystemImagesMap;


    public static ParticipatingEventFragment newInstance(Event event, long dateTime) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        args.putLong(ARG_EVENT_DATE, dateTime);

        ParticipatingEventFragment fragment = new ParticipatingEventFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_summary, container, false);
        event = getArguments().getParcelable(ARG_EVENT);
        event.setDate(new Date(getArguments().getLong(ARG_EVENT_DATE)));

        initializeComponents(view);
        initializeValues();
        initializeListeners();
        return view;
    }

    private void initializeComponents(View view) {
        mTitle = view.findViewById(R.id.game_summary_title);
        mDate = view.findViewById(R.id.game_summary_date);
        mMasterName = view.findViewById(R.id.row_participator_name);
        mMasterGames = view.findViewById(R.id.row_participator_games_played);
        mSysImage = view.findViewById(R.id.game_summary_sys_image);
        mMasterImage = view.findViewById(R.id.row_participator_image);
        mMasterRating = view.findViewById(R.id.row_participator_rating);
        mRecyclerView = view.findViewById(R.id.game_summary_recyclerview);
        mButton = view.findViewById(R.id.game_summary_button);
        mSystemImagesMap = SystemImagesMap.newInstance(getActivity());
        mButton.setText(getString(R.string.event_detail_participator));
        mButton.setVisibility(View.VISIBLE);
    }

    private void initializeValues() {
        mTitle.setText(event.getTitle());
        mDate.setText(event.getDate().toString());
        mMasterName.setText(event.getGame_maser().getUsername());
        mMasterGames.setText(String.valueOf(calculateUserGames(event.getGame_maser())));
        mSysImage.setImageResource(mSystemImagesMap.getImageForSystem(event.getSystem()));
        mSysImage.setImageResource(event.getGame_maser().getAvatarUrl());
        mMasterRating.setRating(calculateUserAverageAsGM(event.getGame_maser()));

        initRecyclerView(event);

    }


    private void initRecyclerView(Event event) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ParticipantsAdapter adapter = new ParticipantsAdapter(event.getParticipants(), event.getGame_maser(), getActivity());
        mRecyclerView.setAdapter(adapter);
    }

    private void initializeListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(event.getTitle())
                        .setMessage("Are you sure to quit this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeUserFormEventDatabase();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    private void removeUserFormEventDatabase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        DocumentReference eventReference = database
                .collection(getString(R.string.collection_events))
                .document(event.getId());

        ArrayList<User> newParticipants = new ArrayList<>();
        User currentUser = ((UserClient) (getActivity().getApplicationContext())).getUser();
        if (!currentUser.getId().equals(event.getGame_maser().getId())) {
            for (User user : event.getParticipants()) {
                if (!user.getId().equals(currentUser.getId())) {
                    newParticipants.add(user);
                }
            }
        }

        //event.setParticipants(newParticipants);

        List<Map<String, Object>> list = prepareUpdateDatabaseList(newParticipants);

        eventReference.update(getString(R.string.document_participants), list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Successfully removed user from list");
                getActivity().finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

    }
}
