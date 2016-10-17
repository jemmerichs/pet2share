package io.pet2share.pet2share.interfaces.loader;

import java.util.ArrayList;

import io.pet2share.pet2share.model.offer.Offer;

/**
 * Created by Bausch on 17.10.2016.
 */

public interface OfferLoadingInterface {
    public abstract void loadOffers(ArrayList<Offer> offers);
}
