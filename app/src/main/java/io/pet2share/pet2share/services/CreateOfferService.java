package io.pet2share.pet2share.services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.model.offer.Offer;

/**
 * Created by Muki-Zenbook on 20.10.2016.
 */

public class CreateOfferService extends IntentService {

    public final static int CREATE_OFFER_SERVICE_CODE = 2;
    public final static String SERVICE_RESULT_KEY = "RESULT";
    public final static int SUCCESS_CODE = 1;
    public final static int FAILURE_CODE = 2;
    public final static String OFFER_KEY = "OFFER";
    public final static String BITMAPS_URI_KEY = "BITMAP";
    private final static String RESULT_RECEIVER_KEY = "RECEIVER";

    private ResultReceiver resultReceiver;
    private Offer offer;
    private Bitmap bitmap;

    public CreateOfferService() {
        super("CreateOfferService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        unpackIntent(intent);
        OfferLoader.getInstance().createOffer(FirebaseAuth.getInstance().getCurrentUser().getUid(), offer, offerCreated -> {
            if (bitmap != null)
                OfferLoader.getInstance().uploadPictureForOffer(offerCreated, bitmap);
        });
        Bundle bundle = new Bundle();
        bundle.putInt(SERVICE_RESULT_KEY, SUCCESS_CODE);
        resultReceiver.send(CREATE_OFFER_SERVICE_CODE, bundle);

    }

    public static void startCreateOfferService(Context context, ProgressDialog dialog, ResultReceiver resultReceiver, Offer offer,
                                               Bitmap bitmap) {
        dialog.setTitle("Creating Offer...");
        dialog.setCancelable(false);
        dialog.show();
        Intent intent = new Intent(context, CreateOfferService.class);
        intent.putExtra(RESULT_RECEIVER_KEY, resultReceiver);
        intent.putExtra(OFFER_KEY, Parcels.wrap(offer));
        intent.putExtra(BITMAPS_URI_KEY, bitmap);
        context.startService(intent);

    }

    private void unpackIntent(Intent intent) {
        offer = Parcels.unwrap(intent.getParcelableExtra(OFFER_KEY));
        bitmap = intent.getParcelableExtra(BITMAPS_URI_KEY);
        resultReceiver = intent.getParcelableExtra(RESULT_RECEIVER_KEY);
    }
}
