package io.pet2share.pet2share.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class Pet2ShareApplication extends Application {
    private static Context mContext;

    public static Context get() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}