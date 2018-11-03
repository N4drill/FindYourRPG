package pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons;

import android.content.Context;

import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Database.FakeDataProvider;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.LibraryGameSystem;

public class LibrarySingleton {
    private static LibrarySingleton sLibrarySingleton;

    private List<LibraryGameSystem> mLibrary;

    public static LibrarySingleton getInstance(Context context) {
        if (sLibrarySingleton == null) {
            sLibrarySingleton = new LibrarySingleton(context);
        }
        return sLibrarySingleton;
    }

    private LibrarySingleton(Context context) {
        mLibrary = FakeDataProvider.generateGameSystems(context);
    }

    public List<LibraryGameSystem> getGamesLibrary() {
        return mLibrary;
    }
}
