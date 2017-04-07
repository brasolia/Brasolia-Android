package br.com.brasolia.util;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Yoshiharu Takuno on 5/9/16.
 */
public class PermissionUtil {

    public static boolean hasPermissionGPS(Context context){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.equals("")) {
            return false;
        }
        return true;
    }

}
