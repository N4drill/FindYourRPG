package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public abstract class BaseFragmentCreator extends Fragment {

    /**
     * Points layout resource id
     *
     * @return Id of needed layout file
     */
    protected abstract int getFragmentLayoutId();

    protected abstract void initializeComponents(View view);

    protected abstract void setOnClickListeners();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayoutId(), container, false);
        initializeComponents(view);
        setOnClickListeners();
        return view;
    }
}
