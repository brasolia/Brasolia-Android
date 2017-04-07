package br.com.brasolia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        if (sp != null)
            cookie = sp.getString("cookie", ""); // if there is a valid cookie, go to the main actvity, else go to the login activity

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);

        //creating timer
        Timer timer_interact = new Timer();
        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 0);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 300);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 600);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 900);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 1200);

    }

    final Handler handler_interact = new Handler();
    private int splash = 0;

    private void UpdateGUI() {
        if (splash == 4) {
            if (!cookie.equals("")) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
            finish();
        } else {
            handler_interact.post(runnable_interact);
        }
    }


    final Runnable runnable_interact = new Runnable() {
        public void run() {
            int randomNum = (int) (Math.random() * 31);

            Resources resources = getResources();
            final int resourceId = resources.getIdentifier("splash" + randomNum, "drawable", getPackageName());

            getWindow().setBackgroundDrawableResource(resourceId);

            splash++;
        }
    };

}
