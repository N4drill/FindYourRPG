package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments;

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
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Model.User;
import pl.student.pwr.gluszczak.pawel.findyourrpg.R;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.ToastMaker;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities.LoginActivity;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseFragmentCreator;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.doesStringMatch;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isEmpty;

public class RegisterFragment extends BaseFragmentCreator {

    private static final String TAG = "RegisterFragment";

    //Views
    EditText mEmailInput, mNickInput, mPasswordInput, mSecondPasswordInput;
    ProgressBar mProgressBar;
    Button mButton;

    //Firebase
    private FirebaseFirestore mDatabase;


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initializeComponents(View view) {
        mEmailInput = view.findViewById(R.id.register_email_input);
        mPasswordInput = view.findViewById(R.id.register_password_input);
        mSecondPasswordInput = view.findViewById(R.id.register_second_password_input);
        mNickInput = view.findViewById(R.id.register_nick_input);
        mButton = view.findViewById(R.id.register_button);
        mProgressBar = view.findViewById(R.id.register_progressBar);

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
                        ToastMaker.shortToast(getActivity(), "Passwords does not match");
                        mSecondPasswordInput.setText("");
                    }

                } else {
                    ToastMaker.shortToast(getActivity(), "You must fill all inputs to register account");
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
                                        View parentLayout = getActivity().findViewById(android.R.id.content);
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

        ToastMaker.shortToast(getActivity(), "Succesfully registered!");

        Intent intent = new Intent(getActivity(), LoginActivity.class);
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
