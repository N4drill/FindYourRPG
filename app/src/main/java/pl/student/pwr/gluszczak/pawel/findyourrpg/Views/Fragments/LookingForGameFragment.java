package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Enums.ExpierienceLevelMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ClusterMarker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ParcableUserPosition;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemImagesMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.MyClusterManagerRenderer;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ViewWeightAnimationWrapper;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.compareDates;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isToday;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.LOOKING_FOR_GAME_BUNDLE_GEOPOSITION;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.MAPVIEW_BUNDLE_KEY;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.MAP_LAYOUT_STATE_CONTRACTED;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.MAP_LAYOUT_STATE_EXPANDED;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.REQUEST_DATE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.REQUEST_EVENT;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.LatLngParser.calculateDistance;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToDateString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToTimeString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.getDistanceString;

public class LookingForGameFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    private static final String TAG = "LookingForGameFragment";
    private static final double BOUNDARY_MARGIN = .01;
    private static final String DIALOG_EVENT = "DialogEvent";
    private static final String DIALOG_DATE = "DialogDate";

    //views
    private MapView mMapView;
    private RelativeLayout mMapContainer, mFormContainer;
    private ImageButton mExpandButton;

    private Spinner mSystemSpinner, mExpSpinner;
    private Button mFilterButton, mDistanceLeftButton, mDistanceRightButton, mResetButton;
    private TextView mDistanceText, mDateLowText, mDateUpText, mCounter;
    private CardView mDistanceLeftCard, mDistanceRightCard, mDateLow, mDateUp;
    private Switch mSystemSwitch, mExpSwitch, mDistanceSwitch, mDateSwitch;
    private ProgressBar mProgressBar, mProgressBarMap;


    //Vars
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBounds;
    private ParcableUserPosition mUserPosition;
    private ArrayList<Event> mAllEvents = new ArrayList<>();
    private ArrayList<Event> mFilteredEvents = new ArrayList<>();
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private ExpierienceLevelMap mExpLevelMap;
    private SystemImagesMap mImagesMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private ArrayAdapter<CharSequence> mSystemAdapter, mExpAdapter;

    //Flags
    private int mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
    private boolean mIsFiltered = false;
    private boolean mUpDateClicked = false;
    private boolean mLowDateClicked = false;

    //Init Filter Values
    private double mCurrentDistance = 0.0;
    private String mCurrentGame;
    private Date mCurrentLowDate;
    private Date mCurrentUpDate;


    private void initGoogleMap(Bundle savedInstanceState) {

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_looking_for_game, container, false);
        mMapView = view.findViewById(R.id.lfg_map);
        mMapContainer = view.findViewById(R.id.lfg_map_container);
        mFormContainer = view.findViewById(R.id.lfg_form_container);
        mExpandButton = view.findViewById(R.id.lfg_exp_button);
        mExpandButton.setOnClickListener(this);
        mExpLevelMap = ExpierienceLevelMap.newInstance(getActivity());
        mImagesMap = SystemImagesMap.newInstance(getActivity());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        initDefaultValues();
        initForm(view);
        initListeners();

        initGoogleMap(savedInstanceState);

        return view;
    }

    private void initDefaultValues() {
        mCurrentLowDate = new Date();
        mCurrentUpDate = new Date();
        mCurrentGame = getString(R.string.system_default);
    }

    private void initForm(View view) {
        mSystemSpinner = view.findViewById(R.id.filter_system);
        mExpSpinner = view.findViewById(R.id.filter_exp);
        mFilterButton = view.findViewById(R.id.filter_button);
        mDistanceText = view.findViewById(R.id.filter_distance_text);
        mDistanceLeftCard = view.findViewById(R.id.filter_distance_left_card);
        mDistanceRightCard = view.findViewById(R.id.filter_distance_right_card);
        mDateLowText = view.findViewById(R.id.filter_date_low_text);
        mDateUpText = view.findViewById(R.id.filter_date_up_text);
        mDateLow = view.findViewById(R.id.filter_date_low);
        mDateUp = view.findViewById(R.id.filter_date_up);
        mDateSwitch = view.findViewById(R.id.filter_switch_date);
        mExpSwitch = view.findViewById(R.id.filter_switch_exp);
        mSystemSwitch = view.findViewById(R.id.filter_switch_systems);
        mDistanceSwitch = view.findViewById(R.id.filter_switch_distance);
        mProgressBar = view.findViewById(R.id.filter_progressbar);
        mProgressBarMap = view.findViewById(R.id.lfg_progressbar);
        mDistanceLeftButton = view.findViewById(R.id.filter_distance_left_button);
        mDistanceRightButton = view.findViewById(R.id.filter_distance_right_button);
        mResetButton = view.findViewById(R.id.filter_reset_button);
        mCounter = view.findViewById(R.id.lfg_counter);


        initSpinners();

        initFormWithDefaultData();
    }

    private void initSpinners() {
        initSystemSpinner();
        initExpSpinner();
    }

    private void initSystemSpinner() {
        mSystemAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.game_systems, android.R.layout.simple_spinner_item);
        mSystemAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.game_systems, android.R.layout.simple_spinner_item);
        mSystemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSystemSpinner.setAdapter(mSystemAdapter);

    }

    private void initExpSpinner() {
        mExpAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.exp_levels_short, android.R.layout.simple_spinner_item);
        mExpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExpSpinner.setAdapter(mExpAdapter);
    }

    private void initFormWithDefaultData() {
        updateDateLowText(mCurrentLowDate);
        updateDateUpText(mCurrentUpDate);
    }

    private void updateDateLowText(Date date) {
        mDateLowText.setText(dateToDateString(date));
    }

    private void updateDateUpText(Date date) {
        mDateUpText.setText(dateToDateString(date));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
        mGoogleMap.setOnInfoWindowClickListener(this);
        setUserPosition();
        showEventsOnMap();

    }

    private void setUserPosition() {
        Log.d(TAG, "getLastKnownLocation: called.");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Attached location to bundle");
                    Location location = task.getResult();
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    mUserPosition = new ParcableUserPosition();
                    mUserPosition.setGeoPoint(geoPoint);
                    Log.d(TAG, "onComplete: ----------------------------------------");
                    Log.d(TAG, "onComplete: Done attaching location to bundle with geoPoint: " + mUserPosition.getGeoPoint().getLatitude() + ", " + mUserPosition.getGeoPoint().getLongitude());
                    Log.d(TAG, "onComplete: ----------------------------------------");

                    setCameraView();
                }
            }
        });
    }

    private void setCameraView() {
        //to make rectangle boundary

        double bottomBoundary = mUserPosition.getGeoPoint().getLatitude() - BOUNDARY_MARGIN;
        double leftBoundary = mUserPosition.getGeoPoint().getLongitude() - BOUNDARY_MARGIN;
        double topBoundary = mUserPosition.getGeoPoint().getLatitude() + BOUNDARY_MARGIN;
        double rightBoundary = mUserPosition.getGeoPoint().getLongitude() + BOUNDARY_MARGIN;

        mMapBounds = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, 0));
        Log.d(TAG, "setCameraView: Centered map at: " + mUserPosition.getGeoPoint().getLatitude() + ", " + mUserPosition.getGeoPoint().getLongitude());
    }

    private void showEventsOnMap() {
        Log.d(TAG, "getEventsFromDB: ------------------------------");
        Log.d(TAG, "getEventsFromDB: Trying to receive events from DB");
        Log.d(TAG, "getEventsFromDB: ------------------------------");
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        mDatabase.collection(getString(R.string.collection_events))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            long currentDateTime = new Date().getTime();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                //TODO: Dodac jako warunek wyswietlania tylko tych w przyszlosci...
                                //if (currentDateTime > event.getDate().getTime()) {
                                Log.d(TAG, "onComplete: =>Document " + document.getId() + "received, adding");
                                mAllEvents.add(document.toObject(Event.class));
                                //}
                            }
                            placeMarkersOnMap();
                        } else {
                            Log.d(TAG, "onComplete: failed to obtain objects");
                        }
                    }
                });

    }

    private void placeMarkersOnMap() {
        if (mGoogleMap != null) {
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mGoogleMap);
            }

            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        getActivity(),
                        mGoogleMap,
                        mClusterManager
                );

                mClusterManager.setRenderer(mClusterManagerRenderer);
            }
        }
        Log.d(TAG, "placeMarkersOnMap: End of Clusters preparation");

        for (Event event : mAllEvents) {
            try {
                ClusterMarker newClusterMarker = createCluster(event);

                mClusterManager.addItem(newClusterMarker);
                mClusterMarkers.add(newClusterMarker);

            } catch (NullPointerException ex) {
                Log.d(TAG, "placeMarkersOnMap: NullPointerException " + ex.getMessage());
            }
        }
        mCounter.setText(String.valueOf(mClusterMarkers.size()));
        mClusterManager.cluster();
    }

    private ClusterMarker createCluster(Event event) {
        return new ClusterMarker(
                new LatLng(event.getLocalization().getLatitude(), event.getLocalization().getLongitude()),
                event.getTitle(),
                dateToDateString(event.getDate()) + ", " + dateToTimeString(event.getDate()),
                mImagesMap.getImageForSystem(event.getSystem()),
                event
        );
    }

    private void updateClusters(List<Event> events) {
        for (ClusterMarker clusterMarker : mClusterMarkers) {
            mClusterManager.removeItem(clusterMarker);
        }
        mClusterMarkers.clear();

        for (Event event : events) {
            try {
                ClusterMarker newClusterMarker = createCluster(event);
                mClusterManager.addItem(newClusterMarker);
                mClusterMarkers.add(newClusterMarker);

            } catch (NullPointerException ex) {
                Log.d(TAG, "placeMarkersOnMap: NullPointerException " + ex.getMessage());
            }
        }
        mCounter.setText(String.valueOf(mClusterMarkers.size()));
        mClusterManager.cluster();
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Event event = null;
        for (ClusterMarker cm : mClusterMarkers) {
            if (cm.getEvent().getTitle().equals(marker.getTitle())) {
                event = cm.getEvent();
            }
        }
        if (event != null) {
            Log.d(TAG, "onInfoWindowClick: clicked on marker");
            FragmentManager manager = getFragmentManager();
            EventDetailsDialogFragment dialog = EventDetailsDialogFragment.newInstance(event);
            dialog.setTargetFragment(LookingForGameFragment.this, REQUEST_EVENT);
            //TestDialog dialog = TestDialog.newInstance(event);
            dialog.show(manager, DIALOG_EVENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EVENT) {
            Event event = data.getParcelableExtra(EventDetailsDialogFragment.EXTRA_EVENT);
            updateDatabase(event);
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            if (mUpDateClicked) {
                mCurrentUpDate = date;
                mUpDateClicked = false;
                updateDateUpText(date);
            }
            if (mLowDateClicked) {
                mCurrentLowDate = date;
                mLowDateClicked = false;
                updateDateLowText(date);
            }

        }
    }

    private void updateDatabase(final Event event) {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        DocumentReference eventRef = mDatabase
                .collection(getString(R.string.collection_events))
                .document(event.getId());

        eventRef.set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Updated database event =>" + event.getId());
                    updateEventsArray(event);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Failed to update array");
            }
        });

    }

    private void updateEventsArray(Event event) {
        Log.d(TAG, "updateEventsArray: Trying to update array");
        Log.d(TAG, "updateEventsArray: mAllEvents Array size before: " + mAllEvents.size());
        for (Event e : mAllEvents) {
            if (event.getId().equals(e.getId())) {
                Log.d(TAG, "updateEventsArray: Event" + e.getId() + "has users: " + e.getParticipants().size() + " before");
                e.setParticipants(event.getParticipants());
                Log.d(TAG, "updateEventsArray: Array updated");
                Log.d(TAG, "updateEventsArray: Event" + e.getId() + "has users: " + e.getParticipants().size() + " after");

            }
        }
        Log.d(TAG, "updateEventsArray: mAllEvents Array size after: " + mAllEvents.size());
    }

    private void expandMapAnimation() {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                50,
                100);
        mapAnimation.setDuration(800);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(mFormContainer);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                50,
                0);
        recyclerAnimation.setDuration(800);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    private void contractMapAnimation() {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                100,
                50);
        mapAnimation.setDuration(800);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(mFormContainer);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                0,
                50);
        recyclerAnimation.setDuration(800);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    private void showProgressBar() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if (mProgressBarMap.getVisibility() == View.INVISIBLE) {
            mProgressBarMap.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBarMap.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lfg_exp_button: {
                if (mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
                    mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                    expandMapAnimation();
                } else if (mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
                    mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                    contractMapAnimation();
                }
                break;
            }
        }
    }

    private void initListeners() {
        mDistanceRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentDistance < 50) {
                    mCurrentDistance++;
                    updateDistanceText(mCurrentDistance);
                }
            }
        });

        mDistanceLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentDistance > 0) {
                    mCurrentDistance--;
                    updateDistanceText(mCurrentDistance);
                }

            }
        });

        mDateLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLowDateClicked = true;
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(LookingForGameFragment.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        mDateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpDateClicked = true;
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(LookingForGameFragment.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        mFilterButton.setOnClickListener(onFilterClickListener());

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFiltered = false;
                mFilteredEvents.clear();
                updateClusters(mAllEvents);
                resetFiltersSwitches();
            }
        });
    }

    private void resetFiltersSwitches() {
        mSystemSwitch.setChecked(false);
        mDistanceSwitch.setChecked(false);
        mExpSwitch.setChecked(false);
        mDateSwitch.setChecked(false);
    }

    private View.OnClickListener onFilterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                Log.d(TAG, "onClick: FILTERING!");
                boolean system = mSystemSwitch.isChecked();
                boolean exp = mExpSwitch.isChecked();
                boolean distance = mDistanceSwitch.isChecked();
                boolean date = mDateSwitch.isChecked();
                boolean validation = false;


                if (!system && !exp && !distance && !date) {
                    ToastMaker.shortToast(getActivity(), "You need to choose at least one filter");
                } else {
                    validation = checkValidation(system, exp, distance, date);
                }

                //Validation completed
                if (validation) {
                    Log.d(TAG, "onClick: Validation success");
                    mFilteredEvents = filterEvents(mAllEvents, system, exp, distance, date);
                    mIsFiltered = true;

                    updateClusters(mFilteredEvents);


                    //Checking
                    Log.d(TAG, "onClick: Filtered list");
                    Log.d(TAG, "onClick: -----BEFORE------");
                    for (Event e : mAllEvents) {
                        Log.d(TAG, "onClick: Event: => " + e.getId());
                    }
                    Log.d(TAG, "onClick: -----AFTER------");
                    for (Event e : mFilteredEvents) {
                        Log.d(TAG, "onClick: Event: => " + e.getId());
                    }
                    //-------------------------
                    //Na koniec zmienic logikę na zbieranie iformacji z filtrowanych
                } else {
                    Log.d(TAG, "onClick: validation unsuccess");
                }

                hideProgressBar();
            }
        };
    }

    private boolean checkValidation(boolean system, boolean exp, boolean distance, boolean date) {
        if (system) {
            if (!checkSystemSpinnerOK()) {
                ToastMaker.shortToast(getActivity(), "Choose system to filter");
                return false;
            }
        }
        if (exp) {
            if (!checkExpSpinnerOK()) {
                ToastMaker.shortToast(getActivity(), "Choose min exp to filter");
                return false;
            }
        }
        if (distance) {
            if (!checkDistanceOK()) {
                ToastMaker.shortToast(getActivity(), "Choose distance to filter");
                return false;
            }
        }
        if (date) {
            if (!isDateOK()) {
                Toast.makeText(getActivity(), "Give real and correct time bounds", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        Log.d(TAG, "checkValidation: Filter form validation success");
        return true;
    }

    private boolean isDateOK() {
        Date date = new Date();

        if (mCurrentLowDate.getTime() < date.getTime() || mCurrentUpDate.getTime() < date.getTime())
            if (!isToday(mCurrentLowDate) || !isToday(mCurrentUpDate)) {
                return false;
            }
        return mCurrentUpDate.getTime() >= mCurrentLowDate.getTime();
    }

    private boolean checkDistanceOK() {
        return mCurrentDistance > 0 && mCurrentDistance < 50;
    }

    private boolean checkExpSpinnerOK() {
        return !mExpSpinner.getSelectedItem().toString().equals(getString(R.string.header_exp_levels));
    }

    private ArrayList<Event> filterEvents(List<Event> events, boolean bySystem, boolean byExp, boolean byDistance, boolean byDate) {
        Log.d(TAG, "filterEvents: Events size before filter: " + events.size());
        ArrayList<Event> newEventList = new ArrayList<>(events);
        ArrayList<Event> eventsToRemove = new ArrayList<>();

        if (bySystem) {
            String system = mSystemSpinner.getSelectedItem().toString();
            for (Event event : events) {
                if (!event.getSystem().equals(system)) {
                    eventsToRemove.add(event);
                }
            }
        }

        if (byExp) {
            Log.d(TAG, "filterEvents: Filtering by exp");
            int current = mExpLevelMap.getValueOfLevel(mExpSpinner.getSelectedItem().toString());
            for (Event event : events) {
                // If level of event is below min level, remove
                int value = mExpLevelMap.getValueOfLevel(mExpLevelMap.getLevelMappedToShort(event.getMin_exp()));
                if (value < current) {
                    eventsToRemove.add(event);
                }
            }
        }

        //TODO: Naprawic filtrowanie
//        if (byDistance) {
//            //sqrt((x2-x1)^2 + (y2-y1)^2)
//            //lat deg = 110.574 km
//            //long deg = 111.320*cos(lat) km
//            double chosenDistance = mCurrentDistance;
//            for (Event event : events) {
//                double distance = calculateDistance(event.getLocalization().getLatitude(),
//                        mUserPosition.getGeoPoint().getLatitude(),
//                        event.getLocalization().getLongitude(),
//                        mUserPosition.getGeoPoint().getLongitude()
//                );
//
//                if (distance < chosenDistance) {
//                    eventsToRemove.add(event);
//                }
//            }
//        }
        if (byDate) {
            for (Event event : newEventList) {
                if (!(event.getDate().getTime() > mCurrentLowDate.getTime() && event.getDate().getTime() < mCurrentUpDate.getTime())) {
                    //if (compareDates(mCurrentLowDate, event.getDate()) >= 0 && compareDates(mCurrentUpDate, event.getDate()) <= 0) {  //TODO: Method to repair
                    eventsToRemove.add(event);
                }
            }
        }

        Log.d(TAG, "filterEvents: Events size after filter: " + newEventList.size());
        newEventList.removeAll(eventsToRemove);
        return newEventList;
    }

    private boolean checkSystemSpinnerOK() {
        return !mSystemSpinner.getSelectedItem().toString().equals(getString(R.string.header_game_systems));
    }

    private void updateDistanceText(double distance) {
        mDistanceText.setText(getDistanceString(getActivity(), distance));
    }


}
