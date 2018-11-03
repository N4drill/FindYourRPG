package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Adapters.LibraryAdapter;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.LibrarySingleton;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class LibraryFragment extends BaseFragmentCreator {

    private LibrarySingleton mLibrarySingleton;

    private RecyclerView mRecyclerView;
    private LibraryAdapter mLibraryAdapter;



    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_library;
    }

    @Override
    protected void initializeComponents(View view) {
        mRecyclerView = view.findViewById(R.id.library_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLibrarySingleton = LibrarySingleton.getInstance(getActivity());

        mLibraryAdapter = new LibraryAdapter(mLibrarySingleton.getGamesLibrary());
        mRecyclerView.setAdapter(mLibraryAdapter);
    }

    @Override
    protected void setOnClickListeners() {

    }
}
