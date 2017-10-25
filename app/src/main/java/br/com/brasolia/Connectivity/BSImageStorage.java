package br.com.brasolia.Connectivity;

import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.brasolia.application.BrasoliaApplication;

/**
 * Created by cayke on 07/04/17.
 */

public class BSImageStorage {
    public static void setImageWithPathToImageViewDownloadingIfNecessary(String imageURL, ImageView imageView, int placeholder, int width, int height, Callback callback) {
        Picasso picasso = Picasso.with(BrasoliaApplication.getAppContext());
        picasso.setIndicatorsEnabled(false);
        if (placeholder == 0)
            picasso.load(imageURL).resize(width, height).into(imageView, callback);
        else
            picasso.load(imageURL).placeholder(placeholder).resize(width, height).into(imageView, callback);
    }
}
