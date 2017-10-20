package br.com.brasolia.Connectivity;

import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.brasolia.application.BrasoliaApplication;

/**
 * Created by cayke on 07/04/17.
 */

public class BSImageStorage {
    private static String getAmazonUrl1() {
        return "https://s3-sa-east-1.amazonaws.com/";
    }

    private static String getAmazonUrl2() {
        return "https://s3-us-west-2.amazonaws.com/";
    }

    private static String getCoverURL ()
    {
        return getAmazonUrl2() + "bs.cover/";
    }

    private static String getThumbURL ()
    {
        return getAmazonUrl2() +  "bs.thumb/";
    }

    public static void setCategoryImageNamed(String imageKey, ImageView imageView, int width, int height, Callback callback) {
        String serverUrl = getThumbURL();

        serverUrl = serverUrl + imageKey;

        setImageWithPathToImageViewDownloadingIfNecessary(serverUrl, imageView, 0, width, height, callback);
    }

    public static void setEventImageNamed(String imageKey, ImageView imageView, int width, int height, Callback callback) {
        String serverUrl = getCoverURL();

        serverUrl = serverUrl + imageKey;

        setImageWithPathToImageViewDownloadingIfNecessary(serverUrl, imageView, 0, width, height, callback);
    }

    public static void setImage(String imageKey, ImageView imageView, int width, int height, Callback callback) {
        setImageWithPathToImageViewDownloadingIfNecessary(imageKey, imageView, 0, width, height, callback);
    }

    private static void setImageWithPathToImageViewDownloadingIfNecessary(String imageURL, ImageView imageView, int placeholder, int width, int height, Callback callback) {
        Picasso picasso = Picasso.with(BrasoliaApplication.getAppContext());
        picasso.setIndicatorsEnabled(false);
        if (placeholder == 0)
            picasso.load(imageURL).resize(width, height).into(imageView, callback);
        else
            picasso.load(imageURL).placeholder(placeholder).resize(width, height).into(imageView, callback);
    }
}
