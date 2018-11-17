package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import java.util.ArrayList;
import java.util.Arrays;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ParcableUserPosition;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DEFAULT_LATITUDE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DEFAULT_LONGITUDE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToString;
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
    TextView mTitle, mLat, mLong;
    EditText mDescription;
    FloatingActionButton mAcceptButton;
    Button mSetLocationButton, mSetDateButton, mSetHourButton;
    Spinner mNeededPlayersSpinner, mSystemSpinner, mMinExpSpinner, mRecExpSpinner;

    //Vals
    ArrayAdapter<CharSequence> mSystemsAdapter, mMinExpAdapter, mRecExpAdapter, mPlayerNumberAdapter;
    ParcableUserPosition mUserPosition;
    FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creating_game_form, container, false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
                ToastMaker.shortToast(getActivity(), "ACCEPT");
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

        mSetHourButton.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            updateDateButtonText(date);
        }

        if (requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 12);
            int min = data.getIntExtra(TimePickerFragment.EXTRA_MIN, 00);

            updateTimeButtonText(hour, min);
        }
        if (requestCode == REQUEST_LOCATION) {
            double latidute = data.getDoubleExtra(LocationPickerFragment.EXTRA_LAT, DEFAULT_LATITUDE);
            double longitude = data.getDoubleExtra(LocationPickerFragment.EXTRA_LONG, DEFAULT_LONGITUDE);

            updateLocationText(latidute, longitude);
        }
    }

    private void updateTimeButtonText(int hour, int min) {
        mSetHourButton.setText(hourMinToString(hour, min));
    }

    private void updateDateButtonText(Date date) {
        mSetDateButton.setText(dateToString(date));
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
        ArrayList<CharSequence> options = new ArrayList<CharSequence>(Arrays.asList("Players needed"));
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
        mSetHourButton = view.findViewById(R.id.form_hour_button);
        Log.d(TAG, "initializeComponents: done");
    }
}
