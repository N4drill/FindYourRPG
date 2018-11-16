package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public abstract class BaseActivityCreator extends AppCompatActivity {

    private static final String TAG = "BaseActivityCreator";

    protected abstract int getActivityLayoutId();

    protected abstract void initializeComponents();

    protected abstract void setOnClickListeners();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        //updateUserClientIfNeeded();
        initializeComponents();
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUserClientIfNeeded();
    }

    private void updateUserClientIfNeeded() {
        Log.d(TAG, "updateUserClient: Checking UserClient");

        if (((UserClient) (getApplicationContext())).getUser() == null) {
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
                        ((UserClient) (getApplicationContext())).setUser(user);
                        Log.d(TAG, "onComplete: Successful update UserClient");
                    } else {
                        Log.d(TAG, "onComplete: FAILED to update UserClient");
                    }
                }
            });
        }
    }
}
