package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class TimePickerFragment extends AppCompatDialogFragment {

    private static final String TAG = "TimePickerFragment";
    private static final String ARG_TIME = "time";

    public static final String EXTRA_HOUR = "pl.student.pwr.gluszczak.pawel.findyourrpg.hour";
    public static final String EXTRA_MIN = "pl.student.pwr.gluszczak.pawel.findyourrpg.min";

    //Views
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date) {
        Log.d(TAG, "newInstance: Creating instance of TimePickerFragment");
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity()).
                inflate(R.layout.dialog_time_picker, null);

        mTimePicker = view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setMinute(min);
        mTimePicker.setHour(hour);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose time")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getHour();
                        int min = mTimePicker.getMinute();
                        sendResult(Activity.RESULT_OK, hour, min);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, int hour, int min) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hour);
        intent.putExtra(EXTRA_MIN, min);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
