package io.pet2share.pet2share.data.offer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.pet2share.pet2share.R;
import io.pet2share.pet2share.common.FirebaseLoader;
import io.pet2share.pet2share.common.Pet2ShareApplication;
import io.pet2share.pet2share.data.profile.ProfileLoader;
import io.pet2share.pet2share.interfaces.loader.OfferLoadingInterface;
import io.pet2share.pet2share.interfaces.loader.ProfileInformationLoadingInterface;
import io.pet2share.pet2share.model.offer.Offer;
import io.pet2share.pet2share.model.user.Profile;

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

    public static OfferLoader getInstance() {
        return instance;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
