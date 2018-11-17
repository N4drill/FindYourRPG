package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.CreatingGameFormFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivityWithNav;

public class GameCreatingFormActivity extends SinglePageActivityWithNav {

    private static final String TAG = "GameCreatingFormActivit";

    @Override
    protected Fragment createFragment() {
        return new CreatingGameFormFragment();
    }

    @Override
    protected Bundle lookingForGameBundlePackage() {
        return null;
    }


    private void getCurrentUserLocation() {

    }

}
