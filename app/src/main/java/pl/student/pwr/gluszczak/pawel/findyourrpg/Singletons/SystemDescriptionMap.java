package pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class SystemDescriptionMap {
    private static SystemDescriptionMap sSystemDescriptionMap;

    private HashMap<String, String> mDescMap;

    public static SystemDescriptionMap newInstance(Context context) {
        if (sSystemDescriptionMap == null) {
            sSystemDescriptionMap = new SystemDescriptionMap(context);
        }
        return sSystemDescriptionMap;
    }

    private SystemDescriptionMap(Context context) {
        initMap(context);
    }

    private void initMap(Context context) {
        mDescMap = new LinkedHashMap<>();
        mDescMap.put(context.getString(R.string.system_dnd), context.getString(R.string.lib_dnd));
        mDescMap.put(context.getString(R.string.system_cyberpunk), context.getString(R.string.lib_cyberpunk));
        mDescMap.put(context.getString(R.string.system_darkeye), context.getString(R.string.lib_darkeye));
        mDescMap.put(context.getString(R.string.system_hiroshima), context.getString(R.string.lorem_ipsum_long));
        mDescMap.put(context.getString(R.string.system_monastyr), context.getString(R.string.lib_monastyr));
        mDescMap.put(context.getString(R.string.system_vampir), context.getString(R.string.lib_vampir));
        mDescMap.put(context.getString(R.string.system_warhammer), context.getString(R.string.lib_warhammer));
        mDescMap.put(context.getString(R.string.system_pathfinder), context.getString(R.string.lib_pathfinder));

    }

    public String getDescriptionForSystem(String system) {
        return mDescMap.get(system);
    }

}
