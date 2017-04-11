package br.com.brasolia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import br.com.brasolia.homeTabs.BSCategoryFragment;
import br.com.brasolia.homeTabs.EventsFragment;
import br.com.brasolia.homeTabs.ProfileFragment;
import br.com.brasolia.models.BSTest;
import br.com.brasolia.util.AlertUtil;
import br.com.brasolia.util.LocationUtil;
import br.com.brasolia.util.PermissionUtil;

public class MainActivity extends AppCompatActivity implements LocationUtil.LocationListenerUtil {

    private DrawerLayout drawerLayout;
    private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SharedPreferences sp;
    private LocationUtil locationUtil;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //todo comentar antes de upar
        BSTest.init();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Screen Elements ---------------------------------------------------------
        mViewPager = (ViewPager) findViewById(R.id.container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);
        // ------------------------------------------------------------------------

        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final FloatingActionButton[] fab = {(FloatingActionButton) findViewById(R.id.fab_btn)};
        fab[0].hide();
        fab[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0, true);
            }
        });

        locationUtil = new LocationUtil(this, this);
        checkPermissionGPS();


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fab[0] = (FloatingActionButton) findViewById(R.id.fab_btn);
                if (position == 0 && positionOffsetPixels == 0) {
//                    fab[0].startAnimation(AnimationUtils.makeOutAnimation(getBaseContext(), true));
                    fab[0].hide();
                }
                if (position == 0 && positionOffsetPixels != 0) {
                    fab[0].show();
//                    fab[0].startAnimation(AnimationUtils.makeInAnimation(getBaseContext(), false));
                }

                Log.i("ScrollStateChanged", "            position:" + position);
                Log.i("ScrollStateChanged", "      positionOffset:" + positionOffset);
                Log.i("ScrollStateChanged", "positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) drawerLayout.setActivated(true);
                else drawerLayout.setActivated(false);
                Log.i("OnPageChangeListener", "Pagina Selecionada");


            }

            @Override
            public void onPageScrollStateChanged(int state) {

                Log.i("ScrollStateChanged", "" + state);
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);


        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_opened,
                R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BSCategoryFragment();
                case 1:
                    return EventsFragment.newInstance();
                case 2:
                    return ProfileFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CATEGORIAS";
                case 1:
                    return "EVENTOS";
                case 2:
                    return "PERFIL";
            }
            return null;
        }
    }


}