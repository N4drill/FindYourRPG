package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.FragmentHelper;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.CreatingGameFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LibraryFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LookingForGameFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LookingForPlayersFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.MainMenuFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.ProfileFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivityWithNav;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.ERROR_DIALOG_REQUEST;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainScreenActivity extends SinglePageActivityWithNav {

    private static final String TAG = "MainScreenActivity";

    //vals
    private boolean mLocationPermissionGranted = false;

    @Override
    protected Fragment createFragment() {
        return new MainMenuFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachUserClient();
    }


    private void attachUserClient() {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference userReference = mDatabase
                .collection(getString(R.string.collection_users))
                .document(currentUser.getUid());

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    ((UserClient) (getApplicationContext())).setUser(user);
                    Log.d(TAG, "onComplete: Successfully updated UserClient:");
                    Log.d(TAG, "onComplete: UserClient, username:" + user.getUsername());
                    Log.d(TAG, "onComplete: UserClient, email:" + user.getEmail());
                    Log.d(TAG, "onComplete: UserClient, uid:" + user.getId());
                    updateNavHeader();
                } else {
                    Log.d(TAG, "onComplete: failed");
                }
            }
        });


    }

    private void updateNavHeader() {
        CircleImageView mNavImage = findViewById(R.id.nav_image);
        TextView mNavName = findViewById(R.id.nav_name);

        User user;
        user = ((UserClient) (getApplicationContext())).getUser();
        if (user != null) {
            //Setup nav image

            //Setup nav name
            mNavName.setText(user.getUsername());
            Log.d(TAG, "updateNavHeader: Successfully updated navHead");
        } else {
            Log.d(TAG, "updateNavHeader: Something went wrong with fetching UserClient");
        }

    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            return isMapsEnabled();
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainScreenActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainScreenActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            ToastMaker.shortToast(this, "You can't make map requests");
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {

                } else {
                    getLocationPermission();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                Log.d(TAG, "onResume: Done everything");
            }
            {
                getLocationPermission();
            }
        }
    }
}
