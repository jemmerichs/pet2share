package io.pet2share.pet2share.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Muki-Zenbook on 11.10.2016.
 */

public class Pet2ShareApplication extends Application {
    private static Context mContext;

    public static Context get() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}