package io.pet2share.pet2share.data.offer;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.FirebaseLoader;
import io.pet2share.pet2share.common.Pet2ShareApplication;
import io.pet2share.pet2share.interfaces.loader.OfferCreationFinishedInterface;
import io.pet2share.pet2share.interfaces.loader.OfferLoadingInterface;
import io.pet2share.pet2share.model.offer.Offer;


/**
 * Created by Bausch on 17.10.2016.
 */

public class OfferLoader extends FirebaseLoader {
    private static OfferLoader instance = new OfferLoader();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Pet2ShareApplication.get().getString(R.string.timestring));

    private ArrayList<Offer> offersForDiscovery = new ArrayList<>();

    public void loadOffersByUID(String uid, ArrayList<Comparator> comparators, final OfferLoadingInterface finishingInterface) {
        getFirebaseDatabase().getReference(String.format("offers/%s/", uid)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Offer> offerList = new ArrayList<>();
                System.out.println(dataSnapshot.getChildrenCount());
                for (DataSnapshot offer : dataSnapshot.getChildren()) {
                    Offer loadedOffer = offer.getValue(Offer.class);
                    loadedOffer.setUserId(uid);
                    loadedOffer.setKey(offer.getKey());
                    /*if(loadedOffer.getPictureURIs()==null) {
                        loadedOffer.setPictureURIs(new ArrayList<>());
                    }*/
                    offerList.add(loadedOffer);
                }
                for(Comparator comparator : comparators) {

                }
                finishingInterface.loadOffers(offerList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadSingleOffer(String uid, String key, final OfferLoadingInterface finishingInterface) {
        getFirebaseDatabase().getReference(String.format("offers/%s/%s",uid,key)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Offer offer = dataSnapshot.getValue(Offer.class);
                offer.setKey(dataSnapshot.getKey());
                offer.setUserId(uid);
                ArrayList<Offer> offers = new ArrayList<Offer>();
                offers.add(offer);
                finishingInterface.loadOffers(offers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadAllOffers(ArrayList<Predicate> comparators, final OfferLoadingInterface finishingInterface, final Location locationForCalculation) {
        getFirebaseDatabase().getReference(String.format("offers")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Offer> offerList = new ArrayList<>();
                for (DataSnapshot offerListOfUser : dataSnapshot.getChildren()) {
                    for (DataSnapshot offer : offerListOfUser.getChildren()) {
                        Offer loadedOffer = offer.getValue(Offer.class);
                        loadedOffer.setUserId(offerListOfUser.getKey());
                        loadedOffer.setKey(offer.getKey());
                       /* if(loadedOffer.getPictureURIs()==null) {
                            loadedOffer.setPictureURIs(new ArrayList<>());
                        }*/
                        offerList.add(loadedOffer);
                    }
                }
                if (locationForCalculation != null) {
                    Collections.sort(offerList, (o1, o2) -> (int) (o1.calculateDistanceToOfferinMeters(locationForCalculation.getLatitude(), locationForCalculation.getLongitude()) - o2.calculateDistanceToOfferinMeters(locationForCalculation.getLatitude(), locationForCalculation.getLongitude())));
                }
                for(Predicate predicate : comparators) {
                    offerList = (ArrayList<Offer>)Stream.of(offerList).filter(predicate).collect(Collectors.toCollection(ArrayList<Offer>::new));
                }
                finishingInterface.loadOffers(offerList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void createOffer(String uid, Offer offer, OfferCreationFinishedInterface finishedInterface) {
        DatabaseReference databaseReference = getFirebaseDatabase().getReference();
        HashMap<String, Object> offerMap = offer.toMap();
        String key = databaseReference.child("offers").child(uid).push().getKey();
        Map<String, Object> update = new HashMap<String, Object>();
        update.put(String.format("offers/%s/%s", uid, key), offerMap);

        offer.setKey(key);
        databaseReference.updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finishedInterface.receiveOffer(offer);
            }
        });
    }

    public void deleteOffer(Offer offer) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = offer.getKey();
        if(offer.getPictureURIs()!=null) {
            for (String uri : offer.getPictureURIs()) {
                StorageReference uriReference = getFirebaseStorage().getReference().child(uri);
                uriReference.delete();
            }
        }
        getFirebaseDatabase().getReference().child("offers").child(uid).child(key).setValue(null);
    }

    public void uploadPictureForOffer(Offer offer, String filePath) throws FileNotFoundException {
        uploadPictureForOffer(offer, new FileInputStream(new File(filePath)));
    }

    public void uploadPictureForOffer(Offer offer, Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        uploadPictureForOffer(offer, bs);
    }

    private void uploadPictureForOffer(Offer offer, InputStream pictureStream) {


        String URI = String.format("offers/%s/%s", offer.getKey(), String.valueOf(new Date().getTime()) + ".jpg");
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
                if(offer.getPictureURIs() == null) {
                    offer.setPictureURIs(new ArrayList<>());
                }
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

    public ArrayList<Offer> getOffersForDiscovery() {
        return offersForDiscovery;
    }

    public void setOffersForDiscovery(ArrayList<Offer> offersForDiscovery) {
        this.offersForDiscovery = offersForDiscovery;
    }
}
