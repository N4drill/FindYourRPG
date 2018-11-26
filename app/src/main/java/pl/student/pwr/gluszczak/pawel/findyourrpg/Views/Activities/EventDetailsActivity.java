package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.EventDetailsFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivityWithNav;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_EVENT_DATE;

public class EventDetailsActivity extends SinglePageActivity {
    @Override
    protected Fragment createFragment() {
        Intent fromIntent = getIntent();
        Event event = fromIntent.getParcelableExtra(PASS_EVENT);
        long time = fromIntent.getLongExtra(PASS_EVENT_DATE, 0);
        EventDetailsFragment fragment = EventDetailsFragment.newInstance(event, time);
        return fragment;
    }

}
