package br.com.brasolia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import br.com.brasolia.homeTabs.BSCategoryFragment;
import br.com.brasolia.models.BSTest;
import br.com.brasolia.models.BSTestEndpoint;
import br.com.brasolia.models.BSUser;
import br.com.brasolia.util.AlertUtil;
import br.com.brasolia.util.LocationUtil;
import br.com.brasolia.util.PermissionUtil;

/**
 * Created by cayke on 07/05/17.
 */

public class AppActivity extends AppCompatActivity implements LocationUtil.LocationListenerUtil{
    private static LocationUtil locationUtil;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //todo comentar antes de upar
        BSTestEndpoint.test();
        //BSUser.removeUserFromDevice();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_container);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new BSCategoryFragment()).commit();

        sp = PreferenceManager.getDefaultSharedPreferences(AppActivity.this);
        locationUtil = new LocationUtil(this, this);
        checkPermissionGPS();
    }

    public void pushFragment(final Fragment fragment, final String tag) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager mFragmentManager = getSupportFragmentManager();

                List fragments = mFragmentManager.getFragments();
                Fragment actualFragment = (Fragment) fragments.get(getLastValidObjectIndex(fragments));
                if (fragment.getClass() != actualFragment.getClass())
                    mFragmentManager.beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(null).commit();
            }
        });
    }

    public void popFragment() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mFragmentManager.popBackStack();
            }
        });
    }

    private int getLastValidObjectIndex(List<Object> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Object object = list.get(i);
            if (object == null && i != 0)
                return i - 1;
        }
        return size - 1;
    }

    private boolean checkPermissionGPS() {
        if (!PermissionUtil.hasPermissionGPS(this)) {
            AlertUtil.confirm(this, getString(R.string.attention), getString(R.string.location_disable),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    enableGPS();
                                    break;
                            }
                        }
                    });
            return false;
        }
        return true;
    }

    private void enableGPS() {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private boolean hasPermission() {
        if (!LocationUtil.hasPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            return false;
        }
        return true;
    }

    @Override
    public void availableLocation() {
        if (hasPermission() && locationUtil.availableLocation()) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("latitude", String.valueOf(locationUtil.getLatitude()));
            editor.putString("longitude", String.valueOf(locationUtil.getLongitude()));
            editor.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationUtil.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        availableLocation();
        locationUtil.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationUtil.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationUtil.pause();
    }

    public static LocationUtil getLocationUtil(){
        return locationUtil;
    }
}