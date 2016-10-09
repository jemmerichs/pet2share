package io.pet2share.pet2share.interfaces.loader;


import android.graphics.Bitmap;
import android.media.Image;

import io.pet2share.pet2share.model.user.Profile;

/**
 * Created by Bausch on 09.10.2016.
 */

public interface ProfilePictureLoadingInterface {
    public abstract void applyInformation(Bitmap profilePicture);
}
