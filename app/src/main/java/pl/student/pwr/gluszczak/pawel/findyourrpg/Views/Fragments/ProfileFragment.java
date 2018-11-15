package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateAverage;

public class ProfileFragment extends BaseFragmentCreator {

    private static final String TAG = "ProfileFragment";

    //Vars
    private User user;

    //Views
    private CircleImageView mImage;
    private TextView mName, mAge, mGamesPlayed, mFavSystem;
    private TextView mP1Rank, mP2Rank, mP3Rank;
    private TextView mM1Rank, mM2Rank, mM3Rank;
    private RatingBar mPRatingBar, mMRatingBar, mOverallRating;
    private FloatingActionButton mEditButton;

    //flags
    private boolean mNickNameType = true;

    @Override
    public void onResume() {
        super.onResume();
        updateProfileInformation();
    }

    private void updateProfileInformation() {
        //Get the user from UserClient
        user = ((UserClient) (getActivity().getApplicationContext())).getUser();

        if (user != null) {
            Log.d(TAG, "updateProfileInformation: Successful get user from UserClient");

            try {
                //Set avatar
                if (user.getAvatarUrl() != null) {
                    mImage.setImageResource(setUserImage(user.getAvatarUrl()));
                    Log.d(TAG, "updateProfileInformation: avatar updated");
                }

                //Set name
                setNameTypeDisplay();
                Log.d(TAG, "updateProfileInformation: name set");

                //Set age
                mAge.setText(String.valueOf(user.getAge()));
                Log.d(TAG, "updateProfileInformation: age set");

                //Set games played
                mGamesPlayed.setText(String.valueOf(user.getGamesAsMaster() + user.getGamesAsPlayer()));
                Log.d(TAG, "updateProfileInformation: games played set");

                //Set favourite system
                mFavSystem.setText(user.getFavouriteSystem());
                Log.d(TAG, "updateProfileInformation: fav system set");

                //--Player ranks--
                //Set player rank 1
                mP1Rank.setText(Float.toString(user.getPlayerCreativity()));
                Log.d(TAG, "updateProfileInformation: player rank 1 set ");

                //Set player rank 2
                mP2Rank.setText(Float.toString(user.getPlayerBehaviour()));
                Log.d(TAG, "updateProfileInformation: player rank 2 set ");

                //Set player rank 3
                mP3Rank.setText(Float.toString(user.getPlayerGameFeel()));
                Log.d(TAG, "updateProfileInformation: player rank 3 set ");

                //Set master rank 1
                mM1Rank.setText(Float.toString(user.getMasterCreativity()));
                Log.d(TAG, "updateProfileInformation: master rank 1 set ");

                //Set master rank 2
                mM2Rank.setText(Float.toString(user.getMasterBehaviour()));
                Log.d(TAG, "updateProfileInformation: master rank 2 set ");

                //Set master rank 3
                mM3Rank.setText(Float.toString(user.getMasterGameFeel()));
                Log.d(TAG, "updateProfileInformation: master rank 3 set ");


                float pAverage = calculateAverage(user.getPlayerCreativity(), user.getPlayerBehaviour(), user.getPlayerGameFeel());
                float mAverage = calculateAverage(user.getMasterCreativity(), user.getMasterBehaviour(), user.getMasterGameFeel());

                //Set player average
                mPRatingBar.setRating(pAverage);
                Log.d(TAG, "updateProfileInformation: player rating bar set");

                //Set master average
                mMRatingBar.setRating(mAverage);
                Log.d(TAG, "updateProfileInformation: master rating bar set");
                //Set overall rank
                mOverallRating.setRating((pAverage + mAverage) / 2);
                Log.d(TAG, "updateProfileInformation: overall rating bar set");


            } catch (NullPointerException ne) {
                Log.d(TAG, "updateProfileInformation, NullPointer: One of UserClient values is null");
            }


        } else {
            Log.d(TAG, "updateProfileInformation: Could't get UserClient");
        }


    }

    private int setUserImage(String avatarUrl) {
        // Change this method
        return R.drawable.face_placeholder;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initializeComponents(View view) {
        mImage = view.findViewById(R.id.profile_image);
        mName = view.findViewById(R.id.profile_name);
        mAge = view.findViewById(R.id.profile_player_age);
        mGamesPlayed = view.findViewById(R.id.profile_games_played_value);
        mFavSystem = view.findViewById(R.id.profile_fav_system_value);
        mFavSystem.setSelected(true);

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

        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNameTypeDisplay();
            }
        });
    }

    private void setNameTypeDisplay() {
        //Check if you have user information
        if (user != null) {
            if (mNickNameType) {
                mNickNameType = false;
                mName.setText(user.getUsername());
            } else {
                mNickNameType = true;
                mName.setText(user.getEmail());
            }
        }
    }
}
