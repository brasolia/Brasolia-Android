package br.com.brasolia.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Yoshiharu Takuno on 4/19/16.
 */
public class LoadingView {

    private ProgressDialog progress;

    public LoadingView(Context context) {
        progress = new ProgressDialog(context);
    }

    public void show(String title, String message) {
        progress.setTitle(title);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    public void hide() {
        try {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        } catch (Exception e) {

        }
    }

}
