package pl.student.pwr.gluszczak.pawel.findyourrpg.Database;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.LibraryGameSystem;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public class FakeDataProvider {

    public static List<LibraryGameSystem> generateGameSystems(Context context) {
        List<LibraryGameSystem> gameSystems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            gameSystems.add(new LibraryGameSystem("Game system", R.drawable.ic_placeholder, context.getString(R.string.lorem_ipsum_medium)));
        }

        return gameSystems;
    }
}
