package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class GameSummaryFragment extends Fragment {

    //Views
    private TextView mTitle, mDate, mMasterName, mMasterGames;
    private ImageView mSysImage;
    private CircleImageView mMasterImage;
    private RatingBar mMasterRating;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_summary, container, false);
        initializeComponents(view);
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
    }

    private void initializeListeners() {
    }
}
