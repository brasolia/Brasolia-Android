package br.com.brasolia.util;

/**
 * Created by cayke on 14/02/2018.
 */

public class BSUtil {

    public static String getURLForFBProfilePicture(String ID) {
        return "https://graph.facebook.com/" + ID + "/picture?height=500";
    }

}
