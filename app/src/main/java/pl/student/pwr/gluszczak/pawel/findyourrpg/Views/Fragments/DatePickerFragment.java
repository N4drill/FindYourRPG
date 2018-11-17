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
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class DatePickerFragment extends AppCompatDialogFragment {

    private static final String TAG = "DatePickerFragment";
    private static final String ARG_DATE = "date";

    public static final String EXTRA_DATE = "pl.student.pwr.gluszczak.pawel.findyourrpg.date"; //????


    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {
        Log.d(TAG, "newInstance: Creating instance of DatePickerFragment");
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date_picker, null);

        mDatePicker = view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        mDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose date")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth() + 1;
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
