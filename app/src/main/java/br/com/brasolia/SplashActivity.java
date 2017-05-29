package br.com.brasolia;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import br.com.brasolia.application.BrasoliaApplication;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //creating timer
        Timer timer_interact = new Timer();
        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 100);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 800);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 1500);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 2200);

        timer_interact.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 3000);

    }

    final Handler handler_interact = new Handler();
    private int splash = 0;

    private void UpdateGUI() {
        if (splash >= 4) {
            if (BrasoliaApplication.getUser() != null) {
                Intent i = new Intent(this, AppActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, BSLoginActivity.class);
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
