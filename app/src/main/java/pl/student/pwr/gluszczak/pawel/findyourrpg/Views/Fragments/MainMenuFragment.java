package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.view.View;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class MainMenuFragment extends BaseFragmentCreator {

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_mainmenu;
    }

    @Override
    protected void initializeComponents(View view) {
        //Add references to layout components..
        //For example
        //mButton = view.findViewById(R.id.id);
    }

    @Override
    protected void setOnClickListeners() {
        //Add listeners for each component that need it
    }
}