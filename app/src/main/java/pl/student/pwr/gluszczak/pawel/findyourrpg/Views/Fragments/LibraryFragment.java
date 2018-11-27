package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.LibraryAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.LibraryGameSystem;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemDescriptionMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.SystemImagesMap;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class LibraryFragment extends BaseFragmentCreator {

    private RecyclerView mRecyclerView;
    private LibraryAdapter mLibraryAdapter;
    private ArrayList<LibraryGameSystem> mLibrary;
    private SystemImagesMap mImagesMap;
    private SystemDescriptionMap mDescriptionMap;




    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_library;
    }

    @Override
    protected void initializeComponents(View view) {
        mRecyclerView = view.findViewById(R.id.library_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLibrary = new ArrayList<>();
        mImagesMap = SystemImagesMap.newInstance(getActivity());
        mDescriptionMap = SystemDescriptionMap.newInstance(getActivity());

        String[] gameSystems = getActivity().getResources().getStringArray(R.array.game_systems);
        for (String system : gameSystems) {
            if (!system.equals(getString(R.string.header_game_systems))) {
                mLibrary.add(new LibraryGameSystem(
                        system,
                        mImagesMap.getImageForSystem(system),
                        mDescriptionMap.getDescriptionForSystem(system)
                ));
            }
        }

        mLibraryAdapter = new LibraryAdapter(mLibrary);
        mRecyclerView.setAdapter(mLibraryAdapter);
    }

    @Override
    protected void setOnClickListeners() {

    }
}
