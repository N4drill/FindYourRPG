package pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class SystemImagesMap {

    private static SystemImagesMap sSystemImagesMap;

    private HashMap<String, Integer> mImagesMap;

    private SystemImagesMap(Context context) {
        initMap(context);
    }

    public static SystemImagesMap newInstance(Context context) {
        if (sSystemImagesMap == null) {
            sSystemImagesMap = new SystemImagesMap(context);
        }
        return sSystemImagesMap;
    }

    private void initMap(Context context) {
        mImagesMap = new LinkedHashMap<>();
        mImagesMap.put(context.getString(R.string.system_dnd), R.drawable.dnd);
        mImagesMap.put(context.getString(R.string.system_cyberpunk), R.drawable.cyberpunk);
        mImagesMap.put(context.getString(R.string.system_darkeye), R.drawable.darkeye);
        mImagesMap.put(context.getString(R.string.system_hiroshima), R.drawable.system_placeholder);
        mImagesMap.put(context.getString(R.string.system_monastyr), R.drawable.system_placeholder);
        mImagesMap.put(context.getString(R.string.system_vampir), R.drawable.vampir);
        mImagesMap.put(context.getString(R.string.system_warhammer), R.drawable.warhammer);
        mImagesMap.put(context.getString(R.string.system_pathfinder), R.drawable.pathfinder);
    }

    public HashMap<String, Integer> getImagesMap() {
        return mImagesMap;
    }

    public Integer getImageForSystem(String system) {
        return mImagesMap.get(system);
    }
}
