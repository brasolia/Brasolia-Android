package br.com.brasolia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import br.com.brasolia.application.BrasoliaApplication;

/**
 * Created by cayke on 26/06/17.
 */

public class BSSplashActivity extends AppCompatActivity {
    ImageView imageView;
    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        imageView = (ImageView) findViewById(R.id.activity_splash_image_view);
        updateImageViewWithPlaceholder();
    }

    private void updateImageViewWithPlaceholder() {
        count++;
        if (count < 6) {
            int randomNum = new Random().nextInt(30);
            final int resourceId = getResources().getIdentifier("splash" + randomNum, "drawable", getPackageName());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resourceId);
                    //Picasso.with(BSSplashActivity.this).load(resourceId).into(imageView);
                }
            });

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    updateImageViewWithPlaceholder();
                }
            }, 700);
        }
        else {
            goToMainApp();
        }
    }

    private void goToMainApp() {
        if (BrasoliaApplication.getUser() != null) {
            Intent i = new Intent(this, AppActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, BSLoginActivity.class);
            startActivity(i);
        }
        finish();
    }
}
