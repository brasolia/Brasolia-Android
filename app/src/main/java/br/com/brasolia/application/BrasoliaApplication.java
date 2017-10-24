package br.com.brasolia.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Matheus on 19/07/2016.
 */

public class BrasoliaApplication extends Application {
    private static double latitude, longitude;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        BrasoliaApplication.context =  getApplicationContext();

    }

    public static Context getAppContext() {
        return BrasoliaApplication.context;
    }
}
