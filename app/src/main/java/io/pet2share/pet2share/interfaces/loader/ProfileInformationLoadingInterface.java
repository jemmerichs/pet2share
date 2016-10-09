package io.pet2share.pet2share.interfaces.loader;

import android.widget.ImageView;
import android.widget.TextView;

import io.pet2share.pet2share.model.user.Profile;

/**
 * Created by Bausch on 09.10.2016.
 */

public interface ProfileInformationLoadingInterface {
    public abstract void applyInformation(Profile profile);
}
