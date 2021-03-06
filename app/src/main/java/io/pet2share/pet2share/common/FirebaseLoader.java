package io.pet2share.pet2share.common;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Bausch on 09.10.2016.
 */

public class FirebaseLoader {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }
    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
