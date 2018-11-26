package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class GameSummaryAdapter extends RecyclerView.Adapter<GameSummaryAdapter.ViewHolder> {

    private List<User> mParticipators;
    private Context mContext;

    public GameSummaryAdapter(Context context, List<User> participators) {
        mContext = context;
        mParticipators = participators;
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
                        //Update na ocenę usera
                        //Usun z adaptera tego usera
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


}
