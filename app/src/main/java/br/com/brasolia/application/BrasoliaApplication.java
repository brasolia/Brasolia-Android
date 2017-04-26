package br.com.brasolia.application;

import android.app.Application;
import android.content.Context;

import br.com.brasolia.models.BSUser;

/**
 * Created by Matheus on 19/07/2016.
 */

public class BrasoliaApplication extends Application {
    private static double latitude, longitude;
    private static Context context;
    private static BSUser user;

    @Override
    public void onCreate() {
        super.onCreate();

        BrasoliaApplication.context =  getApplicationContext();

        user = BSUser.loadUserFromDevice();
    }

    public static Context getAppContext() {
        return BrasoliaApplication.context;
    }

    public static BSUser getUser() {
        return user;
    }

    public static void setUser(BSUser user) {
        BrasoliaApplication.user = user;
    }
}
