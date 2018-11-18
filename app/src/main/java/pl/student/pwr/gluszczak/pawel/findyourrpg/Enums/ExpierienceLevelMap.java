package pl.student.pwr.gluszczak.pawel.findyourrpg.Enums;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class ExpierienceLevelMap {

    private static ExpierienceLevelMap mExpierienceLevelMap;

    private HashMap<String, String> mLevelMap;

    private ExpierienceLevelMap(Context context) {


    }

    private void initMap(Context context) {
        mLevelMap = new LinkedHashMap<>();
        mLevelMap.put(context.getString(R.string.exp_1), "Beginner");
        mLevelMap.put(context.getString(R.string.exp_2), "Beginner");
        mLevelMap.put(context.getString(R.string.exp_3), "Beginner");
        mLevelMap.put(context.getString(R.string.exp_4), "Beginner");
        mLevelMap.put(context.getString(R.string.exp_5), "Beginner");
    }
}
