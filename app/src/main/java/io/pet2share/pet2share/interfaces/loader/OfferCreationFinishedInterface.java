package io.pet2share.pet2share.interfaces.loader;

import io.pet2share.pet2share.model.offer.Offer;

/**
 * Created by Bausch on 22.10.2016.
 */

public interface OfferCreationFinishedInterface {
    abstract void receiveOffer(Offer offer);
}
