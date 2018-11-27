package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.CreatingAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.ParticipatingAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class GamesUserParticipateFragment extends BaseFragmentCreator {

    private static final String TAG = "GamesUserParticipateFra";

    //Views
    private Button mNextButton;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


    //Vars
    private ParticipatingAdapter mParticipatingAdapter;
    private ArrayList<Event> mEvents = new ArrayList<>();


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_creating_game;
    }

    @Override
    protected void initializeComponents(View view) {
        mNextButton = view.findViewById(R.id.createF_next_button);
        mNextButton.setVisibility(View.INVISIBLE);
        mRecyclerView = view.findViewById(R.id.createF_recycler);
        mProgressBar = view.findViewById(R.id.createF_progressBar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        updateRecycler();
        return view;
    }

    private void updateRecycler() {
        showProgressBar();
        Log.d(TAG, "getEventsFromDB: ------------------------------");
        Log.d(TAG, "getEventsFromDB: Trying to receive events from DB");
        Log.d(TAG, "getEventsFromDB: ------------------------------");
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        mDatabase.collection(getString(R.string.collection_events))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mEvents = new ArrayList<>();
                            long currentDateTime = new Date().getTime();
                            User currentUser = ((UserClient) (getActivity().getApplicationContext())).getUser();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                if (currentDateTime < event.getDate().getTime()) {
                                    if (doesUserParticipate(currentUser, event)) {
                                        Log.d(TAG, "onComplete: =>Document " + document.getId() + "received, adding");
                                        mEvents.add(document.toObject(Event.class));
                                    }
                                }
                            }
                            hideProgressBar();
                            updateAdapter();
                        } else {
                            Log.d(TAG, "onComplete: failed to obtain objects");
                        }
                    }
                });


    }

    private boolean doesUserParticipate(User currentUser, Event event) {
        for (User user : event.getParticipants()) {
            if (currentUser.getId().equals(user.getId()) && !currentUser.getId().equals(event.getGame_maser().getId()))
                return true;
        }
        return false;
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void updateAdapter() {
        hideProgressBar();
        Log.d(TAG, "updateAdapter: updating Adapter");
        mParticipatingAdapter = new ParticipatingAdapter(mEvents, getActivity());
        mRecyclerView.setAdapter(mParticipatingAdapter);
    }

    @Override
    protected void setOnClickListeners() {
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecycler();
    }
}
