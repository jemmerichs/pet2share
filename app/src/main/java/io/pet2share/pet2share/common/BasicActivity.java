package io.pet2share.pet2share.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import io.pet2share.pet2share.signin.LoginActivity;


/**
 * Created by bausch on 05.10.16.
 */

public class BasicActivity extends AppCompatActivity {


    @Override
    protected void onResume() {
        super.onResume();

        checkLogin();
    }

    protected void checkLogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            System.out.println(auth.getCurrentUser().getEmail());
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            this.finish();
        }
    }
public FirebaseDatabase getFirebaseDatase() {return FirebaseDatabase.getInstance();}

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public String getCurrentUid() {
        return getFirebaseAuth().getCurrentUser().getUid();
    }
}
