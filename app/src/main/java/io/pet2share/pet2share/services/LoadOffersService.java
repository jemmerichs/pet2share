package io.pet2share.pet2share.services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.pet2share.pet2share.R;
import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.model.offer.Offer;

/**
 * Created by Muki-Zenbook on 20.10.2016.
 */

public class LoadOffersService extends IntentService {

    private ResultReceiver resultReceiver;

    public final static  int    successResultCode = 1;
    public final static  int    failureResultCode = 2;
    public final static  String offersKey         = "offers";
    private final static String resultReceiverKey = "resultReceiver";


    public LoadOffersService() {
        super("LoadOffersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.resultReceiver = intent.getParcelableExtra(resultReceiverKey);
        OfferLoader.getInstance().loadAllOffers(offers -> {

            if (offers != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(offersKey, Parcels.wrap(offers));
                resultReceiver.send(successResultCode, bundle);
            }
        }, null);

    }

    private void delete(List<Offer> offerList) {
        for (Offer offer : offerList) {
            OfferLoader.getInstance().deleteOffer(offer);
        }
    }

    public static void startLoadRestaurantService(Context context, ProgressDialog dialog, ResultReceiver resultReceiver) {
        dialog.show();
        Intent intent = new Intent(context, LoadOffersService.class);
        intent.putExtra(resultReceiverKey, resultReceiver);
        context.startService(intent);

    }

    private void createExample() {
        Date soon = new Date();
        soon.setTime(new Date().getTime() + 1000000);
        Date sooner = new Date();
        sooner.setTime(new Date().getTime() + 10000);
        ArrayList<Long> times = new ArrayList<>();
        times.add(sooner.getTime());
        times.add(soon.getTime());
        Offer offer = new Offer("Spitz sucht einen temporären Hüter", new Date().getTime(), soon.getTime(), 49.4844656, 8.4678411, times,
                                "Hallo, sein Name ist Spitz und er ist süß.");
        OfferLoader.getInstance().createOffer(FirebaseAuth.getInstance().getCurrentUser().getUid(), offer);
        OfferLoader.getInstance().uploadPictureForOffer(offer, BitmapFactory.decodeResource(this.getResources(), R.drawable.dog));


        Offer offer2 = new Offer("Tigali sucht einen ebenfalls eine befristete Unterkunft", new Date().getTime(), soon.getTime(),
                                 49.4844656, 8.4678411, times, "Achtung SÜSS!");

        OfferLoader.getInstance().createOffer(FirebaseAuth.getInstance().getCurrentUser().getUid(), offer2);
        OfferLoader.getInstance().uploadPictureForOffer(offer2, BitmapFactory.decodeResource(this.getResources(), R.drawable.cat));

    }

}
