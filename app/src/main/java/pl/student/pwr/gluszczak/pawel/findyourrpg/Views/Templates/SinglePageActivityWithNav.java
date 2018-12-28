package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities.LoginActivity;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.CreatingGameFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.GamesUserParticipateFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LibraryFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LookingForGameFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LookingForPlayersFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.MainMenuFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.PastGamesUserCreatedFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.PastGamesUserParticipatedFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.ProfileFragment;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.Constants.PASS_USER;

public abstract class SinglePageActivityWithNav extends AppCompatActivity {

    protected abstract Fragment createFragment();

    private static final String TAG = "SinglePageActivityWithN";

    //Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mNavName;
    private CircleImageView mNavImage;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        initializeNavigationHeader();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_fragment_container);

        initializeNavigation();

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, fragment)
                    .commit();
        }

        new AsyncUpdateUser().execute();
    }

    private void initializeNavigationHeader() {
        mNavName = findViewById(R.id.nav_name);
        mNavImage = findViewById(R.id.nav_image);
    }


    protected void initializeNavigation() {
        mDrawerLayout = findViewById(R.id.navigation_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationView = findViewById(R.id.navigation_navigation);

        setNavigationListener(mNavigationView);
    }

    private void setNavigationListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        replaceFragmentWithOptionSelect(menuItem);
                        //updateFragmentBasedOnOptionSelect(menuItem);
                        return true;
                    }
                }
        );
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void updateNavigation(MenuItem menuItem) {
        //menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();
        }
    }

    private void replaceFragmentWithOptionSelect(MenuItem menuItem) {
        Fragment fragment;
        Class className;
        //Bundle bundle = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.nav_create:
                className = CreatingGameFragment.class;
                break;
            case R.id.nav_looking:
                className = LookingForGameFragment.class;
                //Bundle temp = lookingForGameBundlePackage();
                //if (temp != null) {
                //    bundle = temp;
                //}
                break;
            case R.id.nav_library:
                className = LibraryFragment.class;
                break;
            case R.id.nav_profile:
                className = ProfileFragment.class;
                break;
            case R.id.nav_participate:
                className = GamesUserParticipateFragment.class;
                break;
            case R.id.nav_past_participated:
                className = PastGamesUserParticipatedFragment.class;
                break;
            case R.id.nav_past_created:
                className = PastGamesUserCreatedFragment.class;
                break;
            case R.id.nav_logout:
                signOut();
            default:
                className = MainMenuFragment.class;
        }

        fragment = FragmentHelper.generateFragmentBasedOnClassName(className);
        if (fragment != null) {

            //fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();
        }

        updateNavigation(menuItem);
    }

    protected abstract Bundle lookingForGameBundlePackage();


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUserClientIfNeeded() {
        if (((UserClient) (getApplicationContext())).getUser() == null) {
            Log.d(TAG, "updateUserClientIfNeeded: ----------------");
            Log.d(TAG, "updateUserClientIfNeeded: UserClient is empty, trying to update.");
            Log.d(TAG, "updateUserClientIfNeeded: ----------------");
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
                        Log.d(TAG, "onComplete: Successful update UserClient");
                    } else {
                        Log.d(TAG, "onComplete: FAILED to update UserClient");
                    }
                }
            });
        }
    }

    private class AsyncUpdateUser extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            return ((UserClient) (getApplicationContext())).getUser();
        }

        @Override
        protected void onPostExecute(User user) {
            Log.d(TAG, "onPostExecute: Trying to update nav header");
            if (user != null) {
                TextView navName = findViewById(R.id.nav_name);
                CircleImageView navImage = findViewById(R.id.nav_image);
                navName.setText(user.getUsername());
                navImage.setImageResource(user.getAvatarUrl());
                Log.d(TAG, "onPostExecute: Success");
            } else {
                Log.d(TAG, "onPostExecute: UserClient is null");
            }
        }
    }
}
