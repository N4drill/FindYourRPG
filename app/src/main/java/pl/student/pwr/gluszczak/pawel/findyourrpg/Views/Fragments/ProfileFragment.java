package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class ProfileFragment extends BaseFragmentCreator {

    //Views
    CircleImageView mImage;
    TextView mName, mAge, mGamesPlayed, mFavSystem;
    TextView mP1Rank, mP2Rank, mP3Rank;
    TextView mM1Rank, mM2Rank, mM3Rank;
    RatingBar mPRatingBar, mMRatingBar, mOverallRating;
    FloatingActionButton mEditButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);


        return view;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initializeComponents(View view) {
        mImage = view.findViewById(R.id.profile_image);
        mName = view.findViewById(R.id.profile_name);
        mAge = view.findViewById(R.id.profile_age);
        mGamesPlayed = view.findViewById(R.id.profile_games_played_value);
        mFavSystem = view.findViewById(R.id.profile_fav_system_value);

        mP1Rank = view.findViewById(R.id.profile_rank_1_value);
        mP2Rank = view.findViewById(R.id.profile_rank_2_value);
        mP3Rank = view.findViewById(R.id.profile_rank_3_value);

        mM1Rank = view.findViewById(R.id.profile_rank_1_value_master);
        mM2Rank = view.findViewById(R.id.profile_rank_2_value_master);
        mM3Rank = view.findViewById(R.id.profile_rank_3_value_master);

        mPRatingBar = view.findViewById(R.id.profile_rank_rating);
        mMRatingBar = view.findViewById(R.id.profile_rank_rating_master);
        mOverallRating = view.findViewById(R.id.profile_overall_rating);

        mEditButton = view.findViewById(R.id.profile_edit_button);
    }

    @Override
    protected void setOnClickListeners() {
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastMaker.shortToast(getActivity(), "EDIT!");
            }
        });
    }
}
