package pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Fragments.FacePickerFragment;
import pl.student.pwr.gluszczak.pawel.findyourrpg.Views.Templates.BaseActivityCreator;

import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.doesStringMatch;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isEmpty;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isIntInBounds;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isStringEmail;
import static pl.student.pwr.gluszczak.pawel.findyourrpg.Tools.CheckingTool.isStringNumber;

public class RegisterActivity extends BaseActivityCreator implements FacePickerFragment.onCompleteListener {

    private static final String TAG = "RegisterActivity";
    private static final String DIALOG_FACE = "DialogFace";

    //Views
    EditText mEmailInput, mNickInput, mAgeInput, mPasswordInput, mSecondPasswordInput;
    ProgressBar mProgressBar;
    Button mButton;
    ImageButton mFaceButton;
    EditText mBio, mTelephone;

    //Vars
    int mAvatarResource = R.drawable.face_placeholder;

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
        mAgeInput = findViewById(R.id.register_age_input);
        mFaceButton = findViewById(R.id.register_face_button);
        mBio = findViewById(R.id.register_bio);
        mTelephone = findViewById(R.id.register_telephone);

        mDatabase = FirebaseFirestore.getInstance();
    }

    @Override
    public void sendInput(int resource) {
        Log.d(TAG, "sendInput: Took face resource => " + resource);
        mAvatarResource = resource;
        changeImageButtonFace(resource);
    }

    private void changeImageButtonFace(int resource) {
        mFaceButton.setBackground(getDrawable(resource));
    }

    @Override
    protected void setOnClickListeners() {
        mFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacePickerFragment dialog = new FacePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_FACE);
            }
        });



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (areFieldsNotEmpty()) {
                    Log.d(TAG, "onClick: Fields are not empty");
                    if (isStringNumber(mAgeInput.getText().toString())) {
                        Log.d(TAG, "onClick: Age is number");
                        int age = Integer.parseInt(mAgeInput.getText().toString());
                        if (isIntInBounds(0, 100, age)) {
                            Log.d(TAG, "onClick: Age is in bounds");
                            if (isStringEmail(mEmailInput.getText().toString())) {
                                Log.d(TAG, "onClick: EmailInput is EmailType");
                                if (doesPasswordsMatch()) {
                                    Log.d(TAG, "onClick: Passwords match");
                                    if (isPhoneOK()) {
                                        registerNewEmail(
                                                mEmailInput.getText().toString(),
                                                mPasswordInput.getText().toString(),
                                                mNickInput.getText().toString(),
                                                mAgeInput.getText().toString(),
                                                mTelephone.getText().toString(),
                                                mBio.getText().toString()
                                        );
                                    } else {
                                        Log.d(TAG, "onClick: Phone entered but not 9 numbers");
                                        ToastMaker.shortToast(RegisterActivity.this, "Phone must have 9 numbers");
                                    }
                                } else {
                                    Log.d(TAG, "onClick: Passwords don't match");
                                    ToastMaker.shortToast(RegisterActivity.this, "Passwords does not match");
                                    mSecondPasswordInput.setText("");
                                }
                            }
                        } else {
                            ToastMaker.shortToast(RegisterActivity.this, "Age must be between 0 and 100");
                        }
                    }
                } else {
                    Log.d(TAG, "onClick: There are some empty fields");
                    ToastMaker.shortToast(RegisterActivity.this, "You must fill all inputs to register account");
                }
            }
        });
    }

    private boolean isPhoneOK() {
        String number = mTelephone.getText().toString();
        if (!isEmpty(number)) {
            if (isStringNumber(number)) {
                //String is number and is not empty so need to have 9 numbers
                return number.length() == 9;
            }
            //Number not empty but is not number
            else {
                return false;
            }
        }
        //Number empty
        else {
            return true;
        }
    }

    private boolean doesPasswordsMatch() {
        return doesStringMatch(mPasswordInput.getText().toString(), mSecondPasswordInput.getText().toString());
    }

    private boolean areFieldsNotEmpty() {
        return !isEmpty(mEmailInput.getText().toString())
                && !isEmpty(mNickInput.getText().toString())
                && !isEmpty(mPasswordInput.getText().toString())
                && !isEmpty(mSecondPasswordInput.getText().toString())
                && !isEmpty(mAgeInput.getText().toString());
    }

    public void registerNewEmail(final String email, String password, final String nickname, final String age, final String phone, final String bio) {
        showProgressBar();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //Insert default user data;
                            User user = createNewUser(nickname, email, FirebaseAuth.getInstance().getUid(), age, phone, bio);

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
                                        successfulRegistration();
                                    } else {
                                        View parentLayout = RegisterActivity.this.findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Ups, something went wrong", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "onComplete: failure" + task.getException());
                            ToastMaker.shortToast(RegisterActivity.this, task.getException().getMessage());
                            hideProgressBar();
                        }
                    }
                });
    }


    private User createNewUser(String nickname, String email, String uid, String age, String phone, String bio) {
        int ageint = Integer.parseInt(age);

        User user = new User();
        user.setUsername(nickname);
        user.setEmail(email);
        user.setId(uid);
        user.setAge(ageint);
        if (!isEmpty(phone)) {
            user.setPhone(phone);
        }
        if (!isEmpty(bio)) {
            user.setBio(bio);
        }
        user.setAvatarUrl(mAvatarResource);
        //user.setFavouriteSystem(getString(R.string.system_default));
        return user;
    }


    private void successfulRegistration() {
        Log.d(TAG, "successfulRegistration: Redirecting to LoginActivity");

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
