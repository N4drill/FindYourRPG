package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.CreatingGameFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LibraryFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LookingForGameFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LookingForPlayersFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.MainMenuFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivityWithNav;

public class MainScreenActivity extends SinglePageActivityWithNav {


    private static final String LOG_TAG_FRAGMENT_SWAP = "MainScreenActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected Fragment createFragment() {
        return new MainMenuFragment();
    }

    @Override
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
                        updateFragmentBasedOnOptionSelect(menuItem);
                        return true;
                    }
                }
        );
    }

    private void updateFragmentBasedOnOptionSelect(MenuItem menuItem) {
        Fragment chosenFragment = chooseFragment(menuItem);
        replaceFragment(chosenFragment);
        updateNavigation(menuItem);
    }

    private void updateNavigation(MenuItem menuItem) {
        menuItem.setChecked(true);
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

    private Fragment chooseFragment(MenuItem menuItem) {
        Class fragmentClassFile;
        switch (menuItem.getItemId()) {
            case R.id.nav_create:
                fragmentClassFile = CreatingGameFragment.class;
                ToastMaker.shortToast(this, "Create");
                break;
            case R.id.nav_looking:
                fragmentClassFile = LookingForGameFragment.class;
                ToastMaker.shortToast(this, "LFG");
                break;
            case R.id.nav_players:
                fragmentClassFile = LookingForPlayersFragment.class;
                ToastMaker.shortToast(this, "Players");
                break;
            case R.id.nav_library:
                fragmentClassFile = LibraryFragment.class;
                ToastMaker.shortToast(this, "Library");
                break;
            case R.id.nav_profile:
                fragmentClassFile = LibraryFragment.class;
                ToastMaker.shortToast(this, "Profile");
                break;
            case R.id.nav_logout:
                fragmentClassFile = MainMenuFragment.class;
                ToastMaker.shortToast(this, "Logout");
                break;
            default:
                fragmentClassFile = MainMenuFragment.class;
        }

        //Logout Option selected
        if (fragmentClassFile == null) {
            return null;
        }

        try {
            return (Fragment) fragmentClassFile.newInstance();
        } catch (Exception e) {
            Log.d(LOG_TAG_FRAGMENT_SWAP, "Failed to fetch Fragment");
            e.printStackTrace();
        }

        return null;
    }


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


}
