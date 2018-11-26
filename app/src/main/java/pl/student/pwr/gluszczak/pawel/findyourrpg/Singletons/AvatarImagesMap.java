package pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class AvatarImagesMap {

    private static AvatarImagesMap sAvatarImagesMap;

    private HashMap<Integer, Integer> mAvatarsMap;

    public static AvatarImagesMap newInstance(Context context) {
        if (sAvatarImagesMap == null) {
            sAvatarImagesMap = new AvatarImagesMap(context);
        }
        return sAvatarImagesMap;
    }

    private AvatarImagesMap(Context context) {
        initMap(context);
    }

    private void initMap(Context context) {
        mAvatarsMap = new LinkedHashMap<>();
        mAvatarsMap.put(1, R.drawable.system_placeholder);
        mAvatarsMap.put(2, R.drawable.system_placeholder);
        mAvatarsMap.put(3, R.drawable.system_placeholder);
        mAvatarsMap.put(4, R.drawable.system_placeholder);
        mAvatarsMap.put(5, R.drawable.system_placeholder);
        mAvatarsMap.put(6, R.drawable.system_placeholder);
        mAvatarsMap.put(7, R.drawable.system_placeholder);
        mAvatarsMap.put(8, R.drawable.system_placeholder);
    }
}
