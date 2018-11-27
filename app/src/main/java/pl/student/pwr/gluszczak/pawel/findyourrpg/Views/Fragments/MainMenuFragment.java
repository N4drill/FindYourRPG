package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

public class MainMenuFragment extends BaseFragmentCreator {

    private static final String TAG = "MainMenuFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //updateUserIfNeeded();
        return view;
    }

    private void updateUserIfNeeded() {
        if (((UserClient) (getActivity().getApplicationContext())).getUser() == null) {
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            DocumentReference userReference = mDatabase
                    .collection(getString(R.string.collection_users))
                    .document(currentUser.getUid());

            userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        ((UserClient) (getActivity().getApplicationContext())).setUser(user);
                        Log.d(TAG, "onComplete: Updated user");
                    } else {
                        Log.d(TAG, "onComplete: failed");
                    }
                }
            });
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_mainmenu;
    }

    @Override
    protected void initializeComponents(View view) {
        //Add references to layout components..
        //For example
        //mButton = view.findViewById(R.id.id);
    }

    @Override
    protected void setOnClickListeners() {
        //Add listeners for each component that need it
    }
}