package pl.student.pwr.gluszczak.pawel.findyourrpg.Enums;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class ExpierienceLevel {

    private static ExpierienceLevel mExpierienceLevel;

    private List<String> expLevels;

    private ExpierienceLevel(Context context) {
        expLevels = Arrays.asList(context.getResources().getStringArray(R.array.exp_levels_min));
    }

    public static ExpierienceLevel getInstance(Context context) {
        if (mExpierienceLevel == null) {
            mExpierienceLevel = new ExpierienceLevel(context);
        }
        return mExpierienceLevel;
    }

    public List<String> getExpLevels() {
        return expLevels;
    }
}

