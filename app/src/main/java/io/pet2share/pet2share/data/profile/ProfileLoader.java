package io.pet2share.pet2share.data.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

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
        final long ONE_MEGABYTE = 1024*1024;
        picture.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                finishingInterface.applyInformation(bmp);
            }
        });

    }

    public static ProfileLoader getInstance() {
        return instance;
    }
}
