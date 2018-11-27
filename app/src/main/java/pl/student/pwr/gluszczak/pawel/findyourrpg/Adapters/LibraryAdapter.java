package pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.LibraryGameSystem;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private List<LibraryGameSystem> mGamesLibrary;

    public LibraryAdapter(List<LibraryGameSystem> gameSystems) {
        mGamesLibrary = gameSystems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_library, parent, false);
        LibraryAdapter.ViewHolder viewHolder = new LibraryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        LibraryGameSystem gameSystem = mGamesLibrary.get(position);
        viewHolder.bindView(gameSystem);
    }

    @Override
    public int getItemCount() {
        return mGamesLibrary.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView systemName;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.row_library_image);
            systemName = itemView.findViewById(R.id.row_library_systemName);
            systemName.setEnabled(true);
            description = itemView.findViewById(R.id.row_library_description);
        }

        public void bindView(LibraryGameSystem gameSystem) {
            image.setImageResource(gameSystem.getImageResource());
            systemName.setText("" + gameSystem.getName());
            description.setText("" + gameSystem.getDescription());
        }


    }
}
