package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.Event;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.ParcableUserPosition;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.LOOKING_FOR_GAME_BUNDLE_GEOPOSITION;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.MAPVIEW_BUNDLE_KEY;

public class LookingForGameFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "LookingForGameFragment";
    private static final double BOUNDARY_MARGIN = .1;

    //views
    private MapView mMapView;

    //Vars
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBounds;
    private ParcableUserPosition mUserPosition;
    private ArrayList<Event> mEvents = new ArrayList<>();

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
        Log.d(TAG, "placeMarkersOnMap: Current array state:");
        for (Event e : mEvents) {
            Log.d(TAG, "placeMarkersOnMap: " + e.getTitle());
        }

        for (Event event : mEvents) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .title(event.getTitle())
                            .position(new LatLng(event.getLocalization().getLatitude(), event.getLocalization().getLongitude()))
            );
        }
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
}
