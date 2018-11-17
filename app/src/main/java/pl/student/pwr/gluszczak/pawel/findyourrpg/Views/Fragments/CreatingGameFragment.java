package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities.GameCreatingFormActivity;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class CreatingGameFragment extends BaseFragmentCreator {

    private static final String TAG = "CreatingGameFragment";

    //Views
    private Button mNextButton;


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_creating_game;
    }

    @Override
    protected void initializeComponents(View view) {
        mNextButton = view.findViewById(R.id.createF_next_button);
    }

    @Override
    protected void setOnClickListeners() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Running Form Activity");
                Intent intent = new Intent(getActivity(), GameCreatingFormActivity.class);
                startActivity(intent);
            }
        });
    }
}
