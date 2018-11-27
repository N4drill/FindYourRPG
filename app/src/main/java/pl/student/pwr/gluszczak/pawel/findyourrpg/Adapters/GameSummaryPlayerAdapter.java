package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.EventUtils.prepareUpdateDatabaseList;

public class GameSummaryPlayerAdapter extends RecyclerView.Adapter<GameSummaryPlayerAdapter.ViewHolder> {

    private static final String TAG = "GameSummaryPlayer";

    private List<User> mGameMasterList = new ArrayList<>();
    private Context mContext;
    private Event mEvent;
    private TextView mEmptyText;
    private User mCurrentUser;

    public GameSummaryPlayerAdapter(Context context, Event event, TextView emptyText, User currentUser) {
        mContext = context;
        mEmptyText = emptyText;
        //mGameMasterList = event.getParticipants();
        mGameMasterList.add(event.getGame_maser());
        mEvent = event;
        mCurrentUser = currentUser;
    }

    @NonNull
    @Override
    public GameSummaryPlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_rank_player, parent, false);
        GameSummaryPlayerAdapter.ViewHolder viewHolder = new GameSummaryPlayerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameSummaryPlayerAdapter.ViewHolder viewHolder, int position) {
        User user = mGameMasterList.get(position);
        viewHolder.updateUI(user);
    }

    @Override
    public int getItemCount() {
        return mGameMasterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private CircleImageView image;
        private RatingBar rank1, rank2, rank3;
        private Button button;

        public float rating1 = 0f;
        public float rating2 = 0f;
        public float rating3 = 0f;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.row_rank_name);
            image = itemView.findViewById(R.id.row_rank_image);
            rank1 = itemView.findViewById(R.id.row_rank_rank1);
            rank2 = itemView.findViewById(R.id.row_rank_rank2);
            rank3 = itemView.findViewById(R.id.row_rank_rank3);
            button = itemView.findViewById(R.id.row_rank_vote_button);


            rank1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        rating1 = rating;
                    }
                }
            });

            rank2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        rating2 = rating;
                    }
                }
            });

            rank3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        rating3 = rating;
                    }
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkValidation(rating1, rating2, rating3)) {
                        updateDatabase(mEvent.getGame_maser(), rating1, rating2, rating3);
                        removeAt(getAdapterPosition());
                    }
                }
            });

        }

        private boolean checkValidation(float rating1, float rating2, float rating3) {
            return rating1 != 0f && rating2 != 0f && rating3 != 0f;
        }

        public void updateUI(User user) {
            name.setText(user.getUsername());
            image.setImageResource(user.getAvatarUrl());
        }
    }

    private void updateDatabase(final User gameMaster, float rating1, float rating2, float rating3) {
        final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        DocumentReference mGameMasterReference = mDatabase
                .collection(mContext.getString(R.string.collection_users))
                .document(gameMaster.getId());

        int gamesPlayed = gameMaster.getGamesAsMaster();
        float newRank1, newRank2, newRank3;
        if (gamesPlayed == 0) {
            newRank1 = rating1;
            newRank2 = rating2;
            newRank3 = rating3;
        } else {
            newRank1 = (gameMaster.getMasterCreativity() + rating1) / 2;
            newRank2 = (gameMaster.getMasterBehaviour() + rating2) / 2;
            newRank3 = (gameMaster.getMasterGameFeel() + rating3) / 2;
        }


        Map<String, Object> updates = new HashMap<>();
        updates.put("masterCreativity", newRank1);
        updates.put("masterBehaviour", newRank2);
        updates.put("masterGameFeel", newRank3);


        mGameMasterReference.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: Updated Game master Info");
                updateEventInfo(mDatabase);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update");
                    }
                });

    }

    private void updateEventInfo(final FirebaseFirestore mDatabase) {

        final ArrayList<User> newVotedOnMaster = new ArrayList<>(mEvent.getVotedOnMaster());
        newVotedOnMaster.add(mCurrentUser);

        List<Map<String, Object>> list = prepareUpdateDatabaseList(newVotedOnMaster);

        DocumentReference mEventReference = mDatabase
                .collection(mContext.getString(R.string.collection_events))
                .document(mEvent.getId());

        mEventReference.update("votedOnMaster", list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Successfully updated voteUserList");
                //If that was last user to vote

                mEmptyText.setText(mContext.getString(R.string.voted_on_master));
                mEmptyText.setVisibility(View.VISIBLE);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update Event");
                    }
                });


    }

/*    private void updateGameMasterStats(FirebaseFirestore mDatabase) {
        DocumentReference mGameMasterRef = mDatabase
                .collection(mContext.getString(R.string.collection_users))
                .document(mEvent.getGame_maser().getId());

        int gamesAsMasterPlayer = mEvent.getGame_maser().getGamesAsMaster();

        Map<String, Object> update = new HashMap<>();
        update.put("gamesAsMaster", gamesAsMasterPlayer + 1);

        mGameMasterRef.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: This was final user, updated  Master Games");
                mEmptyText.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Couldnt update game master games");
            }
        });
    }*/


    private void removeAt(int position) {
        mGameMasterList.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, mGameMasterList.size());
    }

}
