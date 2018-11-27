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
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.EventUtils.prepareUpdateDatabaseList;

public class GameSummaryAdapter extends RecyclerView.Adapter<GameSummaryAdapter.ViewHolder> {

    private static final String TAG = "GameSummaryAdapter";

    private List<User> mParticipators;
    private Context mContext;
    private Event mEvent;
    private TextView mEmptyText;

    public GameSummaryAdapter(Context context, Event event, TextView emptyText) {
        mContext = context;
        mEmptyText = emptyText;
        //mParticipators = event.getParticipants();
        mParticipators = filterParticipants(event);
        mEvent = event;
    }

    private ArrayList<User> filterParticipants(Event event) {
        ArrayList<User> needed = new ArrayList<>(event.getParticipants());
        for (User user : event.getParticipants()) {
            if (user.getId().equals(event.getGame_maser().getId())) {
                needed.remove(user);
            } else {
                for (User alreadyVoted : event.getVotedUsers()) {
                    if (alreadyVoted.getId().equals(user.getId())) {
                        needed.remove(user);
                    }
                }
            }

        }

        return needed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_rank_player, parent, false);
        GameSummaryAdapter.ViewHolder viewHolder = new GameSummaryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        User user = mParticipators.get(position);
        viewHolder.updateUI(user);
    }

    @Override
    public int getItemCount() {
        return mParticipators.size();
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
                        updateDatabase(mParticipators.get(getAdapterPosition()), rating1, rating2, rating3);
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
            image.setImageResource(R.drawable.face_placeholder); // TODO: Change image
        }
    }

    private void updateDatabase(final User user, float rating1, float rating2, float rating3) {
        final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        DocumentReference mUserReference = mDatabase
                .collection(mContext.getString(R.string.collection_users))
                .document(user.getId());

        int gamesPlayed = user.getGamesAsPlayer();
        float newRank1, newRank2, newRank3;
        if (gamesPlayed == 0) {
            newRank1 = rating1;
            newRank2 = rating2;
            newRank3 = rating3;
        } else {
            newRank1 = (user.getPlayerCreativity() + rating1) / 2;
            newRank2 = (user.getPlayerBehaviour() + rating2) / 2;
            newRank3 = (user.getPlayerGameFeel() + rating3) / 2;
        }


        Map<String, Object> updates = new HashMap<>();
        updates.put("gamesAsPlayer", gamesPlayed + 1);
        updates.put("playerCreativity", newRank1);
        updates.put("playerBehaviour", newRank2);
        updates.put("playerGameFeel", newRank3);


        mUserReference.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: Updated User Info");
                updateEventInfo(mDatabase, user);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update");
                    }
                });

    }

    private void updateEventInfo(final FirebaseFirestore mDatabase, User user) {
        final ArrayList<User> newVotedParticipators = new ArrayList<>(mEvent.getVotedUsers());
        newVotedParticipators.add(user);

        List<Map<String, Object>> list = prepareUpdateDatabaseList(newVotedParticipators);

        DocumentReference mEventReference = mDatabase
                .collection(mContext.getString(R.string.collection_events))
                .document(mEvent.getId());

        mEventReference.update(mContext.getString(R.string.document_voted_users), list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Successfully updated voteUserList");
                //If that was last user to vote

                if (newVotedParticipators.size() == mEvent.getParticipants().size() - 1) {
                    updateGameMasterStats(mDatabase);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update Event");
                    }
                });


    }

    private void updateGameMasterStats(FirebaseFirestore mDatabase) {
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
    }


    private void removeAt(int position) {
        mParticipators.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, mParticipators.size());
    }


}
