package io.pet2share.pet2share.services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

import io.pet2share.pet2share.data.offer.OfferLoader;
import io.pet2share.pet2share.interfaces.loader.OfferLoadingInterface;
import io.pet2share.pet2share.model.offer.Offer;

/**
 * Created by Muki-Zenbook on 20.10.2016.
 */

public class DeleteAllOffersService extends IntentService {

    public final static  int    DELETE_SERVICE_CODE= 1;
    public final static  String SERVICE_RESULT_KEY = "RESULT";
    public final static  int    SUCCESS_CODE       = 1;
    public final static  int    FAILURE_CODE       = 2;
    private final static String resultReceiverKey  = "resultReceiver";
    private ResultReceiver resultReceiver;

    public DeleteAllOffersService() {
        super("DeleteAllOffersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        unpackData(intent);
        OfferLoader.getInstance().loadAllOffers(new ArrayList<>(),offers -> {

            if (offers != null) {
                delete(offers);
            }
        }, null);

        OfferLoader.getInstance().loadAllOffers(new ArrayList<>(),new OfferLoadingInterface() {
            @Override
            public void loadOffers(ArrayList<Offer> offers) {
                Bundle bundle = new Bundle();
                if (offers.size() == 0) {
                    bundle.putInt(SERVICE_RESULT_KEY, SUCCESS_CODE);
                } else {
                    bundle.putInt(SERVICE_RESULT_KEY, FAILURE_CODE);
                }
                resultReceiver.send(DELETE_SERVICE_CODE, bundle);
            }
        }, null);
    }

    public static void startDeleteOffersService(Context context, ProgressDialog dialog, ResultReceiver resultReceiver) {
        dialog.setTitle("Deleting all current offers");
        dialog.show();
        Intent intent = new Intent(context, DeleteAllOffersService.class);
        intent.putExtra(resultReceiverKey, resultReceiver);
        context.startService(intent);

    }

    private void unpackData(Intent intent) {
        this.resultReceiver = intent.getParcelableExtra(resultReceiverKey);
    }

    private void delete(List<Offer> offerList) {
        for (Offer offer : offerList) {
            OfferLoader.getInstance().deleteOffer(offer);
        }
    }
}
