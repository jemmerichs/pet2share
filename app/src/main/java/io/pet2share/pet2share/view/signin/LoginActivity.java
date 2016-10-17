package io.pet2share.pet2share.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.pet2share.pet2share.MainActivity;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.data.profile.ProfileLoader;
import io.pet2share.pet2share.interfaces.loader.ProfileInformationLoadingInterface;
import io.pet2share.pet2share.model.user.Profile;
import io.pet2share.pet2share.view.home.OverviewActivity;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LOGIN";

    @BindView(R.id.genderGroup)
    RadioGroup genderRadioGroup;

    @BindView(R.id.radio_male)
    RadioButton genderMale;

    @BindView(R.id.radio_female)
    RadioButton genderFemale;

    @BindView(R.id.send)
    Button sendButton;

    @BindView(R.id.email)
    EditText emailEditText;

    @BindView(R.id.password)
    EditText passwordEditText;

    @BindView(R.id.passwordRepeat)
    EditText passwordRepeatEditText;


    @BindView(R.id.firstname)
    EditText firstnameEditText;


    @BindView(R.id.lastname)
    EditText lastnameEditText;

    @BindView(R.id.switchType)
    TextView switchTypeTextView;

    GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Type currentType = Type.Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("960322293887-hq6ukh26n5fl6mvbvnqeg25sgav0lfmf.apps.googleusercontent.com").requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        /*findViewById(R.id.anonym_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInAnonymously()
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInAnonymously", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    LoginActivity.this.finish();
                                }

                            }
                        });
            }
        });*/

    }

    @OnClick(R.id.send)
    public void send() {

        switch(currentType) {
            case Login:
                login();
                break;
            case Register:
                register();
                break;
        }

    }
    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            handleSingleInSuccessful();
                        }


                    }
                });
    }

    private String getGender() {
        switch(genderRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_male:
               return  "male";
            case R.id.radio_female:
                return  "female";
        }
        return "male";
    }

    private void register() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordRepeat = passwordRepeatEditText.getText().toString();
        final String firstname = firstnameEditText.getText().toString();
        final String lastname = lastnameEditText.getText().toString();
        boolean cont = true;
        final String gender = getGender();

        if(email.equals("")) {
            cont = false;
            emailEditText.setError("No e-mail provided");
        }
        if(firstname.equals("")){
            cont = false;
            firstnameEditText.setError("No firstname provided");
        }
        if(lastname.equals("")){
            cont = false;
            lastnameEditText.setError("No lastname provided");
        }
        if(password.equals("")){
            cont = false;
            passwordEditText.setError("No password provided");
        }
        if(!password.equals(passwordRepeat)){
            cont = false;
            passwordRepeatEditText.setError("Passwords do not match");
        }

        if(cont) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.getResult());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ProfileLoader.getInstance().saveProfileInformation(firstname, lastname, gender, new ProfileInformationLoadingInterface() {
                                    @Override
                                    public void applyInformation(Profile profile) {
                                        handleSingleInSuccessful();
                                    }
                                });
                            }
                        }
                    });
        }

    }

    @OnClick(R.id.switchType)
    public void switchType() {
        int viewMode = 1;
        switch(currentType) {
            case Login:
                switchTypeTextView.setText(getResources().getText(R.string.login_here));
                sendButton.setText(getResources().getText(R.string.register));
                viewMode = View.VISIBLE;
                currentType = Type.Register;
                break;
            case Register:
                switchTypeTextView.setText(getResources().getText(R.string.register_here));
                sendButton.setText(getResources().getText(R.string.login));
                viewMode = View.GONE;
                currentType = Type.Login;
                break;
        }
        passwordRepeatEditText.setVisibility(viewMode);
        firstnameEditText.setVisibility(viewMode);
        lastnameEditText.setVisibility(viewMode);
        genderRadioGroup.setVisibility(viewMode);
    }

    private void handleSingleInSuccessful() {
        Intent i = new Intent(LoginActivity.this, OverviewActivity.class);
        startActivity(i);
        this.finish();
    }

    /*
        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }

        }

        private void handleSignInResult(GoogleSignInResult result) {
            Log.d(TAG, "handleSignInResult:" + result.getStatus());
            Toast.makeText(this,result.getStatus()+" resultStatus",Toast.LENGTH_LONG).show();
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    /*
        private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
            Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                LoginActivity.this.finish();
                            }
                        }
                    });
        }*/
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private enum Type{
        Login,Register;
    }

}
