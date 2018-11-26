package pl.student.pwr.gluszczak.pawel.findyourrpg.Enums;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class ExpierienceLevelMap {

    private static ExpierienceLevelMap mExpierienceLevelMap;

    private HashMap<String, String> mLevelMap;
    private HashMap<String, Integer> mLevelValueMap;

    private ExpierienceLevelMap(Context context) {
        initMap(context);
        initValueMap(context);
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

    public HashMap<String, Integer> getLevelValuesMapping() {
        return mLevelValueMap;
    }

    public Integer getValueOfLevel(String level) {
        return mLevelValueMap.get(level);
    }

    public String getLevelMappedToShort(String level) {
        return mLevelMap.get(level);
    }

    private void initMap(Context context) {
        mLevelMap = new LinkedHashMap<>();
        mLevelMap.put(context.getString(R.string.exp_1), context.getString(R.string.exp_1_s));
        mLevelMap.put(context.getString(R.string.exp_2), context.getString(R.string.exp_2_s));
        mLevelMap.put(context.getString(R.string.exp_3), context.getString(R.string.exp_3_s));
        mLevelMap.put(context.getString(R.string.exp_4), context.getString(R.string.exp_4_s));
        mLevelMap.put(context.getString(R.string.exp_5), context.getString(R.string.exp_5_s));
    }

    private void initValueMap(Context context) {
        mLevelValueMap = new LinkedHashMap<>();
        mLevelValueMap.put(context.getString(R.string.exp_1_s), 1);
        mLevelValueMap.put(context.getString(R.string.exp_2_s), 2);
        mLevelValueMap.put(context.getString(R.string.exp_3_s), 3);
        mLevelValueMap.put(context.getString(R.string.exp_4_s), 4);
        mLevelValueMap.put(context.getString(R.string.exp_5_s), 5);
    }


}
