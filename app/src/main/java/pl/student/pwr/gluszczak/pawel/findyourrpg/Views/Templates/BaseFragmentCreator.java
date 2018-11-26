package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public abstract class BaseFragmentCreator extends Fragment {

    private static final String TAG = "BaseFragmentCreator";

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
        //updateUserClientIfNeeded();
        initializeComponents(view);
        setOnClickListeners();
        return view;
    }

    private void updateUserClientIfNeeded() {
        Log.d(TAG, "updateUserClient: Checking UserClient");

        if (((UserClient) (getActivity().getApplicationContext())).getUser() == null) {
            Log.d(TAG, "updateUserClientIfNeeded: Need to update UserClient, doing so");
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
                        Log.d(TAG, "onComplete: Successful update UserClient");
                    } else {
                        Log.d(TAG, "onComplete: FAILED to update UserClient");
                    }
                }
            });
        }
    }
}
