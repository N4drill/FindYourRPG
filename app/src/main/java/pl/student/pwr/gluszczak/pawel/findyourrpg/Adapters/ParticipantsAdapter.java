package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isEmpty;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.UserUtils.calculateUserAverageAsPlayer;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

    private static final String TAG = "ParticipantsAdapter";

    private List<User> mParticipants = new ArrayList<>();
    private Context mContext;

    public ParticipantsAdapter(List<User> participants, User gameMaster, Context context) {
        mContext = context;
        //Eliminate game master form recycler list
        for (User user : participants) {
            if (!user.getUsername().equals(gameMaster.getUsername())) {
                mParticipants.add(user);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_event_player, parent, false);
        ParticipantsAdapter.ViewHolder viewHolder = new ParticipantsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        User user = mParticipants.get(position);
        viewHolder.updateUI(user);
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";

        User currentUser = ((UserClient) (mContext.getApplicationContext())).getUser();

        CardView card;
        CircleImageView image;
        TextView username, games, empty;
        RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.row_participator_card);
            image = itemView.findViewById(R.id.row_participator_image);
            username = itemView.findViewById(R.id.row_participator_name);
            games = itemView.findViewById(R.id.row_participator_games_played);
            rating = itemView.findViewById(R.id.row_participator_rating);
            empty = itemView.findViewById(R.id.row_participator_empty_text);

            Log.d(TAG, "ViewHolder: ");
        }

        public void updateUI(User user) {
            //set image
            username.setText(user.getUsername());
            games.setText(String.valueOf(user.getGamesAsPlayer() + user.getGamesAsMaster()));
            rating.setRating(calculateUserAverageAsPlayer(user));
            if (user.getId() == currentUser.getId()) {
                card.setCardBackgroundColor(mContext.getColor(R.color.row_participatror_current_user));
            }
        }
    }
}
