package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LoginFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;

public class LoginActivity extends SinglePageActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}
