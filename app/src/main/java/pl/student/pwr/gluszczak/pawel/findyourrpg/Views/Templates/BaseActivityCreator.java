package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivityCreator extends AppCompatActivity {

    protected abstract int getActivityLayoutId();

    protected abstract void initializeComponents();

    protected abstract void setOnClickListeners();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        initializeComponents();
        setOnClickListeners();
    }
}
