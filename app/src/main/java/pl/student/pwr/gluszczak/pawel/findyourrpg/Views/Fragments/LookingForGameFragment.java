package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Date;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ClusterMarker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ParcableUserPosition;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.MyClusterManagerRenderer;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.LOOKING_FOR_GAME_BUNDLE_GEOPOSITION;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.MAPVIEW_BUNDLE_KEY;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.REQUEST_DATE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.REQUEST_EVENT;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.REQUEST_LOCATION;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToDateString;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.TextFormat.dateToTimeString;

public class LookingForGameFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "LookingForGameFragment";
    private static final double BOUNDARY_MARGIN = .01;
    private static final String DIALOG_EVENT = "DialogEvent";

    //views
    private MapView mMapView;

    //Vars
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBounds;
    private ParcableUserPosition mUserPosition;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();

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
        mUserPosition = getArguments().getParcelable(LOOKING_FOR_GAME_BUNDLE_GEOPOSITION);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_looking_for_game, container, false);
        mMapView = view.findViewById(R.id.lfg_map);
        initGoogleMap(savedInstanceState);

        return view;
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
        setCameraView();
        showEventsOnMap();

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
                                mEvents.add(document.toObject(Event.class));
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

        for (Event event : mEvents) {
            try {
                ClusterMarker newClusterMarker = new ClusterMarker(
                        new LatLng(event.getLocalization().getLatitude(), event.getLocalization().getLongitude()),
                        event.getTitle(),
                        dateToDateString(event.getDate()) + ", " + dateToTimeString(event.getDate()),
                        //TODO: Replace with user icon...
                        R.drawable.ic_placeholder,
                        event
                );

                mClusterManager.addItem(newClusterMarker);
                mClusterMarkers.add(newClusterMarker);

            } catch (NullPointerException ex) {
                Log.d(TAG, "placeMarkersOnMap: NullPointerException " + ex.getMessage());
            }
        }
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
        Log.d(TAG, "updateEventsArray: mEvents Array size before: " + mEvents.size());
        for (Event e : mEvents) {
            if (event.getId().equals(e.getId())) {
                Log.d(TAG, "updateEventsArray: Event" + e.getId() + "has users: " + e.getParticipants().size() + " before");
                e.setParticipants(event.getParticipants());
                Log.d(TAG, "updateEventsArray: Array updated");
                Log.d(TAG, "updateEventsArray: Event" + e.getId() + "has users: " + e.getParticipants().size() + " after");

            }
        }
        Log.d(TAG, "updateEventsArray: mEvents Array size after: " + mEvents.size());


    }
}
