package pl.student.pwr.gluszczak.pawel.findyourrpg.Enums;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class ExpierienceLevelMap {

    private static ExpierienceLevelMap mExpierienceLevelMap;

    private HashMap<String, String> mLevelMap;

    private ExpierienceLevelMap(Context context) {
        initMap(context);
    }

    public static ExpierienceLevelMap newInstance(Context context) {
        if (mExpierienceLevelMap == null) {
            mExpierienceLevelMap = new ExpierienceLevelMap(context);
        }
        return mExpierienceLevelMap;
    }

    public HashMap<String, String> getLevelMapping() {
        return mLevelMap;
    }

    private void initMap(Context context) {
        mLevelMap = new LinkedHashMap<>();
        mLevelMap.put(context.getString(R.string.exp_1), context.getString(R.string.exp_1_s));
        mLevelMap.put(context.getString(R.string.exp_2), context.getString(R.string.exp_2_s));
        mLevelMap.put(context.getString(R.string.exp_3), context.getString(R.string.exp_3_s));
        mLevelMap.put(context.getString(R.string.exp_4), context.getString(R.string.exp_4_s));
        mLevelMap.put(context.getString(R.string.exp_5), context.getString(R.string.exp_5_s));
    }
}
