package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.PastCreatedFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.PastParticipatedFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT_DATE;

public class PastParticipatedActivity extends SinglePageActivity {
    @Override
    protected Fragment createFragment() {
        Intent fromIntent = getIntent();
        Event event = fromIntent.getParcelableExtra(PASS_EVENT);
        long time = fromIntent.getLongExtra(PASS_EVENT_DATE, 0);
        User user = ((UserClient) (getApplicationContext())).getUser();
        PastParticipatedFragment fragment = PastParticipatedFragment.newInstance(event, time, user);
        return fragment;
    }

}
