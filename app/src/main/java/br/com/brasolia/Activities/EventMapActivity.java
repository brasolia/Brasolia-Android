package br.com.brasolia.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.brasolia.R;
import br.com.brasolia.models.BSItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class EventMapActivity extends FragmentActivity  {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_event_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        final BSItem item = getIntent().getParcelableExtra("eventMap");

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng eventMarker = new LatLng(item.getLatitude(), item.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(eventMarker).title(item.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.brasolia_marker)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventMarker, 15));
            }
        });

        CircleImageView closeMap = (CircleImageView) findViewById(R.id.close_map);
        closeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(item != null) {
            RelativeLayout openGoogleMaps = (RelativeLayout) findViewById(R.id.open_maps);
            openGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + item.getLatitude() + "," +  item.getLongitude()));
                startActivity(intent);
                }
            });
        }

    }

}
