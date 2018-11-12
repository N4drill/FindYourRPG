package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.support.v4.app.Fragment;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.RegisterFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;

public class RegisterActivity extends SinglePageActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
}
