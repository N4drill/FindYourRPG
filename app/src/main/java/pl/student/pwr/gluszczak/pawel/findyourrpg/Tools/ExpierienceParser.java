package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import android.content.Context;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Enums.ExpierienceLevelMap;

public abstract class ExpierienceParser {


    /**
     * Returns String with shorter representation of exp level
     *
     * @param context
     * @param level
     * @return
     */
    public static String expierienceNameParser(Context context, String level) {
        ExpierienceLevelMap expierienceLevelMap = ExpierienceLevelMap.newInstance(context);

        return expierienceLevelMap.getLevelMapping().get(level);
    }

}
