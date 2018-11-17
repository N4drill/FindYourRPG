package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ParcableUserPosition;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.LOOKING_FOR_GAME_BUNDLE_GEOPOSITION;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.precisionStringFromDouble;

public class CreatingGameFormFragment extends Fragment {

    private static final String TAG = "CreatingGameFormFragmen";
    private static final int COMMA_PRECISION = 1;

    //Views
    TextView mTitle, mLat, mLong;
    EditText mDescription;
    FloatingActionButton mAcceptButton;
    Button mSetLocationButton;
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

    private void initializeSpinners() {
        Log.d(TAG, "initializeSpinners: Starting creating spinners");
        initializeSystemSpinner();
        initializeMinExpSpinner();
        initializeRecExpSpinner();
        initializePlayerNumberSpinner();
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


    private void setListeners() {
        mSetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Set map button selected");
                ToastMaker.shortToast(getActivity(), "Set Map button!");
            }
        });

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Accept form button");
                ToastMaker.shortToast(getActivity(), "ACCEPT");
            }
        });


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
        Log.d(TAG, "initializeComponents: done");
    }
}
