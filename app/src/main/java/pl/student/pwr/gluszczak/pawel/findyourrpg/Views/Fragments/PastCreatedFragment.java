package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.GameSummaryAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.ParticipantsAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemImagesMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateUserAverageAsGM;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateUserGames;

public class PastCreatedFragment extends Fragment {

    private static final String ARG_EVENT = "event";
    private static final String ARG_EVENT_DATE = "eventDate";

    //Views
    private TextView mTitle, mDate, mMasterName, mMasterGames;
    private ImageView mSysImage;
    private CircleImageView mMasterImage;
    private RatingBar mMasterRating;
    private RecyclerView mRecyclerView;
    private Button mRemoveButton, mEditButton;
    private TextView mEmptyText;

    //Vars
    private Event event;
    private SystemImagesMap mSystemImagesMap;


    public static PastCreatedFragment newInstance(Event event, long dateTime) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        args.putLong(ARG_EVENT_DATE, dateTime);

        PastCreatedFragment fragment = new PastCreatedFragment();
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
        mRemoveButton = view.findViewById(R.id.game_summary_button);
        mEditButton = view.findViewById(R.id.game_summary_edit);
        mEmptyText = view.findViewById(R.id.game_summary_empty_text);
        mSystemImagesMap = SystemImagesMap.newInstance(getActivity());
        // mRemoveButton.setVisibility(View.VISIBLE);
        // mEditButton.setVisibility(View.VISIBLE);
    }

    private void initializeValues() {
        mTitle.setText(event.getTitle());
        mDate.setText(event.getDate().toString());
        mMasterName.setText(event.getGame_maser().getUsername());
        mMasterGames.setText(String.valueOf(calculateUserGames(event.getGame_maser())));
        mSysImage.setImageResource(mSystemImagesMap.getImageForSystem(event.getSystem()));
        mMasterImage.setImageResource(event.getGame_maser().getAvatarUrl());
        mMasterRating.setRating(calculateUserAverageAsGM(event.getGame_maser()));

        if (isEventVotedByGameMaster(event)) {
            mEmptyText.setVisibility(View.VISIBLE);
        }

        initRecyclerView(event);

    }


    private void initRecyclerView(Event event) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (isEventVotedByGameMaster(event)) {
            ParticipantsAdapter adapter = new ParticipantsAdapter(event.getParticipants(), event.getGame_maser(), getActivity());
            mRecyclerView.setAdapter(adapter);
        } else {
            GameSummaryAdapter adapter = new GameSummaryAdapter(getActivity(), event, mEmptyText);
            mRecyclerView.setAdapter(adapter);
        }


    }

    private boolean isEventVotedByGameMaster(Event event) {
        return event.getParticipants().size() - 1 == event.getVotedUsers().size();
    }

    private void initializeListeners() {


    }
}
