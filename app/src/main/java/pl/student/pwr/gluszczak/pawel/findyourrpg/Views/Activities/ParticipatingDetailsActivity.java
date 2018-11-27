package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.support.v4.app.Fragment;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.ParticipatingEventFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT_DATE;

public class ParticipatingDetailsActivity extends SinglePageActivity {
    @Override
    protected Fragment createFragment() {
        Intent fromIntent = getIntent();
        Event event = fromIntent.getParcelableExtra(PASS_EVENT);
        long time = fromIntent.getLongExtra(PASS_EVENT_DATE, 0);
        ParticipatingEventFragment fragment = ParticipatingEventFragment.newInstance(event, time);
        return fragment;
    }
}
