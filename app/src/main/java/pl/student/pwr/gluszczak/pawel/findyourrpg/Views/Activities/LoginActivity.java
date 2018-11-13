package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Singletons.UserClient;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.LoginFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseActivityCreator;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isEmpty;

public class LoginActivity extends BaseActivityCreator {

    private static final String TAG = "LoginFragment";

    //Views
    private Button mLoginButton, mRegisterButton;
    private EditText mLoginInput, mPasswordInput;
    private ProgressBar mProgressBar;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: Setup started");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed as: " + user.getUid());
                    ToastMaker.shortToast(LoginActivity.this, "Logged as: " + user.getEmail());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                            .setTimestampsInSnapshotsEnabled(true)
                            .build();

                    db.setFirestoreSettings(settings);

                    DocumentReference userRef = db.collection(getString(R.string.collection_users))
                            .document(user.getUid());

                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: successfully set the user client.");
                                User user = task.getResult().toObject(User.class);
                                ((UserClient) (getApplicationContext())).setUser(user);
                            }
                        }
                    });

                    Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out??");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initializeComponents() {
        //Init firebase

        setupFirebaseAuth();

        mLoginButton = findViewById(R.id.login_button);
        mLoginInput = findViewById(R.id.login_login_input);
        mPasswordInput = findViewById(R.id.login_password_input);
        mProgressBar = findViewById(R.id.login_progressBar);
        mRegisterButton = findViewById(R.id.login_register_button);
    }

    @Override
    protected void setOnClickListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                singIn();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Trying to redirect to register");
                redirectToRegister();
            }
        });
    }

    private void singIn() {
        //Fields with inputs are not empty
        if (!isEmpty(mLoginInput.getText().toString()) && !isEmpty(mPasswordInput.getText().toString())) {
            Log.d(TAG, "singIn: Trying to login...");

            showProgressBar();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(mLoginInput.getText().toString(), mPasswordInput.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgressBar();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ToastMaker.shortToast(LoginActivity.this, "Authentication failed");
                            hideProgressBar();
                        }
                    });
            //Fields with inputs ARE empty
        } else {
            ToastMaker.shortToast(LoginActivity.this, "Fill all fields!");
            hideProgressBar();
        }
    }

    private void redirectToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        Log.d(TAG, "redirectToRegister: Redirected");
        startActivity(intent);
    }


    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
