package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.RegisterFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseActivityCreator;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.SinglePageActivity;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.doesStringMatch;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isEmpty;

public class RegisterActivity extends BaseActivityCreator {

    private static final String TAG = "RegisterFragment";

    //Views
    EditText mEmailInput, mNickInput, mPasswordInput, mSecondPasswordInput;
    ProgressBar mProgressBar;
    Button mButton;

    //Firebase
    private FirebaseFirestore mDatabase;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initializeComponents() {
        mEmailInput = findViewById(R.id.register_email_input);
        mPasswordInput = findViewById(R.id.register_password_input);
        mSecondPasswordInput = findViewById(R.id.register_second_password_input);
        mNickInput = findViewById(R.id.register_nick_input);
        mButton = findViewById(R.id.register_button);
        mProgressBar = findViewById(R.id.register_progressBar);

        mDatabase = FirebaseFirestore.getInstance();
    }

    @Override
    protected void setOnClickListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (areFieldsNotEmpty()) {
                    if (doesPasswordsMatch()) {

                        registerNewEmail(mEmailInput.getText().toString(), mPasswordInput.getText().toString(), mNickInput.getText().toString());

                    } else {
                        ToastMaker.shortToast(RegisterActivity.this, "Passwords does not match");
                        mSecondPasswordInput.setText("");
                    }

                } else {
                    ToastMaker.shortToast(RegisterActivity.this, "You must fill all inputs to register account");
                }
            }
        });
    }

    private boolean doesPasswordsMatch() {
        return doesStringMatch(mPasswordInput.getText().toString(), mSecondPasswordInput.getText().toString());
    }

    private boolean areFieldsNotEmpty() {
        return !isEmpty(mEmailInput.getText().toString())
                && !isEmpty(mNickInput.getText().toString())
                && !isEmpty(mPasswordInput.getText().toString())
                && !isEmpty(mSecondPasswordInput.getText().toString());
    }

    public void registerNewEmail(final String email, String password, final String nickname) {
        showProgressBar();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //Insert default user data;
                            User user = new User();
                            user.setUsername(nickname);
                            user.setEmail(email);
                            user.setId(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestoreSettings setting = new FirebaseFirestoreSettings.Builder()
                                    .setTimestampsInSnapshotsEnabled(true)
                                    .build();
                            mDatabase.setFirestoreSettings(setting);

                            DocumentReference newUserReference = mDatabase
                                    .collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                            newUserReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    hideProgressBar();

                                    if (task.isSuccessful()) {
                                        succesfullRegistration();
                                    } else {
                                        View parentLayout = RegisterActivity.this.findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Ups, something went wrong", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void succesfullRegistration() {
        Log.d(TAG, "succesfullRegistration: Redirecting to LoginActivity");

        ToastMaker.shortToast(RegisterActivity.this, "Succesfully registered!");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
