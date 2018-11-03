package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class FragmentHelper {
    private static final String LOG_TAG = "FragmentHelper Log";

    public static Fragment generateFragmentBasedOnClassName(Class classFile) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) classFile.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Failed while generating Fragment based on classFile");
        }

        return fragment;
    }
}
