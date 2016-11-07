package io.pet2share.pet2share.data.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import io.pet2share.pet2share.common.FirebaseLoader;
import io.pet2share.pet2share.interfaces.loader.ProfileInformationLoadingInterface;
import io.pet2share.pet2share.interfaces.loader.ProfilePictureLoadingInterface;
import io.pet2share.pet2share.model.user.Profile;

/**
 * Created by Bausch on 09.10.2016.
 */

public class ProfileLoader extends FirebaseLoader {

    public static ProfileLoader instance = new ProfileLoader();

    public void loadProfileInformation(String uid, final ProfileInformationLoadingInterface finishingInterface) {
        getFirebaseDatabase().getReference(String.format("users/%s/", uid)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                System.out.println(profile.getFirstname());
                finishingInterface.applyInformation(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadProfilePicture(String uid, final ProfilePictureLoadingInterface finishingInterface) {
        StorageReference picture = getFirebaseStorage().getReference().child(String.format("profile/picture/%s.jpg",uid));
        picture.getMetadata().addOnSuccessListener(storageMetadata -> finishingInterface.applyInformation(storageMetadata.getDownloadUrl().toString()));
    }

    public void saveProfileInformation( String firstname, String lastname, String gender, final ProfileInformationLoadingInterface finishingInterface){
        DatabaseReference databaseReference = getFirebaseDatabase().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String,String> userMap = new HashMap<>();
        userMap.put("firstname",firstname);
        userMap.put("lastname",lastname);
        userMap.put("gender",gender);
        userMap.put("dateofbirth","1990-01-01");
        Map<String, Object> update = new HashMap<String, Object>();
        update.put(String.format("users/%s/",uid),userMap);
        databaseReference.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finishingInterface.applyInformation(null);
            }
        });
    }

    public static ProfileLoader getInstance() {
        return instance;
    }
}
