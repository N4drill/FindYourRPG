package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;
import java.util.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ParcableUserPosition;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.areFirstExpLowerOrSameAsSecondExp;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.doesStringMatch;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isEmpty;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isHourAndMinBeforeDate;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isToday;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DEFAULT_LATITUDE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DEFAULT_LONGITUDE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToDateString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.hourMinToString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.precisionStringFromDouble;

public class CreatingGameFormFragment extends Fragment {

    private static final String TAG = "CreatingGameFormFragmen";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_LOCATION = "DialogLocation";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_LOCATION = 2;
    private static final int COMMA_PRECISION = 1;


    //Views
    private TextView mTitle, mLat, mLong;
    private EditText mDescription;
    private FloatingActionButton mAcceptButton;
    private Button mSetLocationButton, mSetDateButton, mSetTimeButton;
    private Spinner mNeededPlayersSpinner, mSystemSpinner, mMinExpSpinner, mRecExpSpinner;
    private ProgressBar mProgressBar;


    //Vals
    private ArrayAdapter<CharSequence> mSystemsAdapter, mMinExpAdapter, mRecExpAdapter, mPlayerNumberAdapter;
    private ParcableUserPosition mUserPosition;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //Flags
    private boolean mIsTimeSet = false;
    private boolean mIsDateSet = false;
    private Date mCurrentDate;
    private int mCurrentHour;
    private int mCurrentMin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creating_game_form, container, false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        new AsyncSetTime().execute(view);
        setCurrentUserLocation();
        initializeComponents(view);
        initializeSpinners();

        setListeners();

        return view;
    }

    private void setListeners() {
        mSetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Set location button");
                FragmentManager manager = getFragmentManager();

                if (mUserPosition == null) {
                    setCurrentUserLocation();
                }

                LocationPickerFragment dialog = LocationPickerFragment.newInstance(mUserPosition.getGeoPoint().getLatitude(), mUserPosition.getGeoPoint().getLongitude());
                dialog.setTargetFragment(CreatingGameFormFragment.this, REQUEST_LOCATION);

                dialog.show(manager, DIALOG_LOCATION);
            }
        });

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Accept form button");

                if (checkAllInputs()) {
                    Log.d(TAG, "onClick: Form Validation passed");
                    Event eventToAdd = createNewEvent();
                    putNewEventToDatabase(eventToAdd);

                } else {
                    Log.d(TAG, "onClick: Form Validation failed");
                }

            }
        });

        mSetDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Set date button");
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(CreatingGameFormFragment.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Set hour button");
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(CreatingGameFormFragment.this, REQUEST_TIME);

                dialog.show(manager, DIALOG_TIME);
            }
        });


    }

    private void putNewEventToDatabase(Event eventToAdd) {
        Log.d(TAG, "putNewEventToDatabase: Start trying to put event");
        showProgressBar();
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();


        DocumentReference newEventRef = mDatabase
                .collection(getString(R.string.collection_events))
                .document();

        newEventRef.set(eventToAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBar();
                Log.d(TAG, "onComplete: Trying to save event");
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: ----------------------------");
                    Log.d(TAG, "onComplete: Successfull operation");
                    Log.d(TAG, "onComplete: ----------------------------");
                    ToastMaker.shortToast(getActivity(), "SAVED EVENT!");


                } else {
                    Log.d(TAG, "onComplete: Couldn't save event");
                }
            }
        });
    }

    private Event createNewEvent() {
        Event event = new Event();
        ArrayList<User> participants = new ArrayList<>();
        User user = ((UserClient) (getActivity().getApplicationContext())).getUser();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCurrentDate);
        participants.add(user);

        event.setTitle(mTitle.getText().toString());
        event.setMin_exp(mMinExpSpinner.getSelectedItem().toString());
        event.setRec_exp(mRecExpSpinner.getSelectedItem().toString());
        event.setNeeded_players(Integer.valueOf(mNeededPlayersSpinner.getSelectedItem().toString()));
        event.setLocalization(mUserPosition.getGeoPoint());
        event.setDescription(mDescription.getText().toString());
        event.setParticipants(participants);
        event.setDate(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mCurrentHour, mCurrentMin).getTime());
        event.setGame_maser(user);
        event.setSystem(mSystemSpinner.getSelectedItem().toString());

        return event;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCurrentDate = date;
            mIsDateSet = true;
            updateDateButtonText(date);
        }

        if (requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 12);
            mCurrentHour = hour;
            int min = data.getIntExtra(TimePickerFragment.EXTRA_MIN, 00);
            mCurrentMin = min;
            mIsTimeSet = true;
            updateTimeButtonText(hour, min);
        }
        if (requestCode == REQUEST_LOCATION) {
            double latidute = data.getDoubleExtra(LocationPickerFragment.EXTRA_LAT, DEFAULT_LATITUDE);
            double longitude = data.getDoubleExtra(LocationPickerFragment.EXTRA_LONG, DEFAULT_LONGITUDE);

            updateLocationText(latidute, longitude);
        }
    }

    private void updateTimeButtonText(int hour, int min) {
        mSetTimeButton.setText(hourMinToString(hour, min));
    }

    private void updateDateButtonText(Date date) {
        mSetDateButton.setText(dateToDateString(date));
    }

    private void setCurrentUserLocation() {
        Log.d(TAG, "getLastKnownLocation: called.");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "setCurrentUserLocation: Permission denied");
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    mUserPosition = new ParcableUserPosition();
                    mUserPosition.setGeoPoint(geoPoint);
                    Log.d(TAG, "onComplete: Successfully get user Location: " + mUserPosition.getGeoPoint().getLatitude() + ", " + mUserPosition.getGeoPoint().getLongitude());

                    updateLocationText(mUserPosition.getGeoPoint());
                }
            }
        });
    }

    private void updateLocationText(GeoPoint geoPoint) {
        if (geoPoint != null) {
            mLat.setText(precisionStringFromDouble(geoPoint.getLatitude(), COMMA_PRECISION));
            mLong.setText(precisionStringFromDouble(geoPoint.getLongitude(), COMMA_PRECISION));
            Log.d(TAG, "updateLocationText: Updated location parameters to:" + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());

        } else {
            Log.d(TAG, "updateLocationText: got null geopoint...");
        }
    }

    private void updateLocationText(double latidute, double longtidute) {
        mLat.setText(precisionStringFromDouble(latidute, COMMA_PRECISION));
        mLong.setText(precisionStringFromDouble(longtidute, COMMA_PRECISION));
        Log.d(TAG, "updateLocationText: Updated location parameters to:" + latidute + ", " + longtidute);
    }

    private void initializeSpinners() {
        Log.d(TAG, "initializeSpinners: Starting creating spinners");
        initializeSystemSpinner();
        initializeMinExpSpinner();
        initializeRecExpSpinner();
        initializePlayerNumberSpinner();
        disableHeaderSelections();
    }

    private void disableHeaderSelections() {
        mSystemSpinner.setSelection(0, false);
    }

    private void initializeSystemSpinner() {
        mSystemsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.game_systems, android.R.layout.simple_spinner_item);
        mSystemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSystemSpinner.setAdapter(mSystemsAdapter);
        Log.d(TAG, "initializeSystemSpinner: done");
    }

    private void initializeMinExpSpinner() {
        mMinExpAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.exp_levels_min, android.R.layout.simple_spinner_item);
        mMinExpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMinExpSpinner.setAdapter(mMinExpAdapter);
        Log.d(TAG, "initializeMinExpSpinner: done");
    }

    private void initializeRecExpSpinner() {
        mRecExpAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.exp_levels_rec, android.R.layout.simple_spinner_item);
        mRecExpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRecExpSpinner.setAdapter(mRecExpAdapter);
        Log.d(TAG, "initializeRecExpSpinner: done");
    }

    private void initializePlayerNumberSpinner() {
        ArrayList<CharSequence> options = new ArrayList<CharSequence>(Arrays.asList(getString(R.string.header_players_needed)));
        for (int i = 0; i < 10; i++) {
            options.add(String.valueOf(i));
        }
        mPlayerNumberAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, options);
        mPlayerNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNeededPlayersSpinner.setAdapter(mPlayerNumberAdapter);
        Log.d(TAG, "initializePlayerNumberSpinner: done");
    }

    private void initializeComponents(View view) {
        mTitle = view.findViewById(R.id.form_title);
        mLat = view.findViewById(R.id.form_lat);
        mLong = view.findViewById(R.id.form_long);
        mDescription = view.findViewById(R.id.form_description);
        mAcceptButton = view.findViewById(R.id.form_accept_button);
        mSetLocationButton = view.findViewById(R.id.form_map_button);
        mNeededPlayersSpinner = view.findViewById(R.id.form_players_needed);
        mSystemSpinner = view.findViewById(R.id.form_system);
        mMinExpSpinner = view.findViewById(R.id.form_minExp);
        mRecExpSpinner = view.findViewById(R.id.form_recommendedExp);
        mSetDateButton = view.findViewById(R.id.form_date_button);
        mSetTimeButton = view.findViewById(R.id.form_time_button);
        mProgressBar = view.findViewById(R.id.form_progressbar);
        Log.d(TAG, "initializeComponents: done");
    }

    private boolean checkAllInputs() {
        Log.d(TAG, "checkAllInputs: ");
        //Maybe error code ?
        if (!isTitleSet()) {
            ToastMaker.shortToast(getActivity(), "You need to put title!");
            return false;
        }
        if (!isSystemSet()) {
            ToastMaker.shortToast(getActivity(), "You need to choose game system!");
            return false;
        }
        if (!isMinExpSet()) {
            ToastMaker.shortToast(getActivity(), "You need to choose minimum experience level!");
            return false;
        }
        if (!isRecExpSet()) {
            ToastMaker.shortToast(getActivity(), "You need to choose recommended experience level!");
            return false;
        }
        if (!isPlayersNeededSet()) {
            ToastMaker.shortToast(getActivity(), "You need to specify how many players do you need!");
            return false;
        }
        if (!isLocationSet()) {
            ToastMaker.shortToast(getActivity(), "You need to specify location of event!");
            return false;
        }
        if (!isDescriptionSet()) {
            ToastMaker.shortToast(getActivity(), "You need to put description!");
            return false;
        }
        if (!isTimeSet()) {
            ToastMaker.shortToast(getActivity(), "You need to set time!");
            return false;
        }
        if (!isDateSet()) {
            ToastMaker.shortToast(getActivity(), "You need to set date!");
            return false;
        }

        if (!isTimeOK()) {
            ToastMaker.shortToast(getActivity(), "You cannot make event in the past");
            return false;
        }

        if (!isLevelsOK()) {
            ToastMaker.shortToast(getActivity(), "Min exp level must be below Recommended!");
            return false;
        }


        //If it passes everything
        return true;
    }

    private boolean isLevelsOK() {
        Log.d(TAG, "isLevelsOK: ");
        return areFirstExpLowerOrSameAsSecondExp(getActivity(), mMinExpSpinner.getSelectedItem().toString(), mRecExpSpinner.getSelectedItem().toString());
    }


    private boolean isTimeOK() {
        Log.d(TAG, "isTimeOK: ");
        if (isToday(mCurrentDate)) {
            //Check if current hour is before chosen hour
            return !isHourAndMinBeforeDate(mCurrentHour, mCurrentMin);
        }
        return true;
    }


    private boolean isDateSet() {
        Log.d(TAG, "isDateSet: ");
        return mIsDateSet && !doesStringMatch(mSetTimeButton.getText().toString(), getString(R.string.form_set_date));
    }

    private boolean isTimeSet() {
        Log.d(TAG, "isTimeSet: ");
        return mIsTimeSet && !doesStringMatch(mSetTimeButton.getText().toString(), getString(R.string.form_set_time));

    }

    private boolean isDescriptionSet() {
        Log.d(TAG, "isDescriptionSet: ");
        return !isEmpty(mDescription.getText().toString());
    }

    private boolean isLocationSet() {
        Log.d(TAG, "isLocationSet: ");
        return (!isEmpty(mLat.getText().toString()) && !isEmpty(mLong.getText().toString()));
    }

    private boolean isPlayersNeededSet() {
        Log.d(TAG, "isPlayersNeededSet: ");
        return !doesStringMatch(mNeededPlayersSpinner.getSelectedItem().toString(), getString(R.string.header_players_needed));
    }

    private boolean isRecExpSet() {
        Log.d(TAG, "isRecExpSet: ");
        return !doesStringMatch(mRecExpSpinner.getSelectedItem().toString(), getString(R.string.header_exp_levels_recommended));
    }

    private boolean isMinExpSet() {
        Log.d(TAG, "isMinExpSet: ");
        return !doesStringMatch(mMinExpSpinner.getSelectedItem().toString(), getString(R.string.header_exp_levels_min));
    }

    private boolean isSystemSet() {
        Log.d(TAG, "isSystemSet: ");
        return !doesStringMatch(mSystemSpinner.getSelectedItem().toString(), getString(R.string.header_game_systems));
    }

    private boolean isTitleSet() {
        Log.d(TAG, "isTitleSet: ");
        return !isEmpty(mTitle.getText().toString());
    }


    private class AsyncSetTime extends AsyncTask<View, Void, Calendar> {
        Button timeButton, dateButton;

        @Override
        protected Calendar doInBackground(View... views) {
            Log.d(TAG, "doInBackground: Starting async task");
            timeButton = views[0].findViewById(R.id.form_time_button);
            dateButton = views[0].findViewById(R.id.form_date_button);
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mCurrentDate = date;
            mCurrentHour = calendar.get(Calendar.HOUR) + 1;
            mCurrentMin = calendar.get(Calendar.MINUTE);
            return calendar;
        }

        @Override
        protected void onPostExecute(Calendar calendar) {
            updateTimeButtonText(calendar.get(Calendar.HOUR) + 1, calendar.get(Calendar.MINUTE));
            updateDateButtonText(
                    new GregorianCalendar(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH))
                            .getTime());

            mIsTimeSet = true;
            mIsDateSet = true;

            Log.d(TAG, "onPostExecute: Finished async");
        }
    }

    private void showProgressBar() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
