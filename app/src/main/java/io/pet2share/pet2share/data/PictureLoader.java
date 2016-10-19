package io.pet2share.pet2share.data;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import io.pet2share.pet2share.common.FirebaseLoader;
import io.pet2share.pet2share.interfaces.loader.PictureLoadingInterface;

/**
 * Created by Bausch on 19.10.2016.
 */

public class PictureLoader extends FirebaseLoader{
    private static PictureLoader instance = new PictureLoader();

    public void loadPictureDownloadURL(String URI, PictureLoadingInterface finishingInterface) {
        StorageReference picture = getFirebaseStorage().getReference().child(URI);

        picture.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                finishingInterface.getURL(storageMetadata.getDownloadUrl().toString());
            }
        });
    }

    public static PictureLoader getInstance() {
        return instance;
    }
}
