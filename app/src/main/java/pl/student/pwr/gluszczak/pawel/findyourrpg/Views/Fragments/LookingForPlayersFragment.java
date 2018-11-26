package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.support.v7.widget.CardView;
import android.view.View;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class LookingForPlayersFragment extends BaseFragmentCreator {

    private CardView fragmentContainer;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_looking_for_player;
    }

    @Override
    protected void initializeComponents(View view) {
        fragmentContainer = view.findViewById(R.id.find_player_fragment_container);
    }

    @Override
    protected void setOnClickListeners() {

    }
}
