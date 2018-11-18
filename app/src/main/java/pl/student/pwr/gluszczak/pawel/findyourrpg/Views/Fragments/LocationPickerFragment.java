package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DEFAULT_LATITUDE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DEFAULT_LONGITUDE;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.DIALOG_MAPVIEW_BUNDLE_KEY;

public class LocationPickerFragment extends AppCompatDialogFragment implements OnMapReadyCallback {

    private static final String TAG = "LocationPickerFragment";

    private static final String ARG_LAT = "lat";
    private static final String ARG_LONG = "long";

    private static final double BOUNDARY_MARGIN = 0.001;

    public static final String EXTRA_LAT = "pl.student.pwr.gluszczak.pawel.findyourrpg.lat";
    public static final String EXTRA_LONG = "pl.student.pwr.gluszczak.pawel.findyourrpg.long";

    //Views
    private MapView mMapView;

    //Vars
    private double mLatitude;
    private double mLongtidute;
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBounds;

    private Marker mMapMarker;


    public static LocationPickerFragment newInstance(double latidute, double longtidute) {
        Log.d(TAG, "newInstance: Creating instance of LocationPicker");

        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, latidute);
        args.putDouble(ARG_LONG, longtidute);

        LocationPickerFragment fragment = new LocationPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mLatitude = getArguments().getDouble(ARG_LAT);
        mLongtidute = getArguments().getDouble(ARG_LONG);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_map, null);

        initGoogleMap(savedInstanceState, view);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose location")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //prepare result

                        //default
                        sendResult(Activity.RESULT_OK, mLatitude, mLongtidute);

                    }
                })
                .create();
    }

    private void sendResult(int resultCode, double latidute, double longtidute) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_LAT, latidute);
        intent.putExtra(EXTRA_LONG, longtidute);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void initGoogleMap(Bundle savedInstanceState, View view) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(DIALOG_MAPVIEW_BUNDLE_KEY);
        }
        mMapView = view.findViewById(R.id.dialog_map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(DIALOG_MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(DIALOG_MAPVIEW_BUNDLE_KEY, mapViewBundle);
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
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map ready to use");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        mGoogleMap = googleMap;
        setCameraView(mLatitude, mLongtidute);
        placeNewMarker(mLatitude, mLongtidute);

        mGoogleMap.setOnMapClickListener(onMapClickListener());
    }

    private GoogleMap.OnMapClickListener onMapClickListener() {
        return new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                removePreviousMarker();
                placeNewMarker(latLng.latitude, latLng.longitude);
                saveCurrentPosition(latLng.latitude, latLng.longitude);
                setCameraView(latLng.latitude, latLng.longitude);
            }
        };
    }

    private void removePreviousMarker() {
        if (mMapMarker != null) {
            mMapMarker.remove();
        }
    }

    private void saveCurrentPosition(double latitude, double longitude) {
        mLatitude = latitude;
        mLongtidute = longitude;
    }

    private void placeNewMarker(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMapMarker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title("Current position"));
    }

    private void setCameraView(double latitude, double longitude) {
        //to make rectangle boundary
        double bottomBoundary = latitude - BOUNDARY_MARGIN;
        double leftBoundary = longitude - BOUNDARY_MARGIN;
        double topBoundary = latitude + BOUNDARY_MARGIN;
        double rightBoundary = longitude + BOUNDARY_MARGIN;

        mMapBounds = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, 0));
        Log.d(TAG, "setCameraView: Centered map at: " + latitude + ", " + longitude);
    }
}
