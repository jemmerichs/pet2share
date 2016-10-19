package io.pet2share.pet2share.data.offer;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.FirebaseLoader;
import io.pet2share.pet2share.common.Pet2ShareApplication;
import io.pet2share.pet2share.interfaces.loader.OfferLoadingInterface;
import io.pet2share.pet2share.model.offer.Offer;


/**
 * Created by Bausch on 17.10.2016.
 */

public class OfferLoader extends FirebaseLoader {
    private static OfferLoader instance = new OfferLoader();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Pet2ShareApplication.get().getString(R.string.timestring));

    public void loadOffersByUID(String uid, final OfferLoadingInterface finishingInterface) {
        getFirebaseDatabase().getReference(String.format("offers/%s/",uid)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Offer> offerList = new ArrayList<>();
                System.out.println(dataSnapshot.getChildrenCount());
                for(DataSnapshot offer : dataSnapshot.getChildren()) {
                    Offer loadedOffer = offer.getValue(Offer.class);
                    loadedOffer.setKey(offer.getKey());
                    ArrayList<Long> timeSlots = new ArrayList<>();
                    for(DataSnapshot timeSlot : offer.child("timeslots").getChildren()) {
                            timeSlots.add(Long.parseLong(String.valueOf(timeSlot.getValue())));
                    }
                    loadedOffer.setTimeSlots(timeSlots);
                    offerList.add(loadedOffer);
                }
                finishingInterface.loadOffers(offerList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadAllOffers(String uid, final OfferLoadingInterface finishingInterface, final Location locationForCalculation) {
        getFirebaseDatabase().getReference(String.format("offers")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Offer> offerList = new ArrayList<>();
                for(DataSnapshot offerListOfUser:dataSnapshot.getChildren()) {
                    for (DataSnapshot offer : offerListOfUser.getChildren()) {
                        Offer loadedOffer = offer.getValue(Offer.class);
                        loadedOffer.setKey(offer.getKey());
                        ArrayList<Long> timeSlots = new ArrayList<>();
                        for (DataSnapshot timeSlot : offer.child("timeslots").getChildren()) {
                            timeSlots.add(Long.parseLong(String.valueOf(timeSlot.getValue())));
                        }
                        loadedOffer.setTimeSlots(timeSlots);
                        offerList.add(loadedOffer);
                    }
                }
                if(locationForCalculation!= null) {
                    Collections.sort(offerList,(o1,o2)->(int)(o1.calculateDistanceToOfferinMeters(locationForCalculation.getLatitude(),locationForCalculation.getLongitude())-o2.calculateDistanceToOfferinMeters(locationForCalculation.getLatitude(),locationForCalculation.getLongitude())));
                }
                finishingInterface.loadOffers(offerList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void createOffer(String uid, Offer offer) {
        DatabaseReference databaseReference = getFirebaseDatabase().getReference();
        HashMap<String,Object> offerMap = offer.toMap();
        String key = databaseReference.child("offers").child(uid).push().getKey();
        Map<String, Object> update = new HashMap<String, Object>();
        update.put(String.format("offers/%s/%s",uid,key),offerMap);
        databaseReference.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void uploadPictureForOffer(Offer offer, String filePath) throws FileNotFoundException {
        uploadPictureForOffer(offer, new FileInputStream(new File(filePath)));
    }

    public void uploadPictureForOffer(Offer offer, Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        uploadPictureForOffer(offer,bs);
    }

    private void uploadPictureForOffer(Offer offer, InputStream pictureStream) {


        String URI = String.format("offers/%s/%s",offer.getKey(),String.valueOf(new Date().getTime())+".jpg");
        StorageReference picReference = getFirebaseStorage().getReference().child(URI);
        UploadTask uploadTask = picReference.putStream(pictureStream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                offer.getPictureURIs().add(URI);
                savePictureURIs(offer);
            }
        });
    }

    private void savePictureURIs(Offer offer) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = offer.getKey();
        databaseReference.child("offers").child(uid).child(key).child("pictureURIs").setValue(offer.getPictureURIs());

    }

    public static OfferLoader getInstance() {
        return instance;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
