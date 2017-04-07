package br.com.brasolia;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventMapActivity extends FragmentActivity  {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Intent in= getIntent();
                Bundle b = in.getExtras();
                String extra = getIntent().getStringExtra("eventMap");
                JsonParser jsonParser = new JsonParser();
                final JsonObject event = (JsonObject) jsonParser.parse(extra);
                LatLng eventMarker = new LatLng(event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble(), event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble());
                googleMap.addMarker(new MarkerOptions().position(eventMarker).title(event.get("name").getAsString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.brasolia_marker)));
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

        Intent in= getIntent();
        Bundle b = in.getExtras();
        String extra = getIntent().getStringExtra("eventMap");
        JsonParser jsonParser = new JsonParser();
        final JsonObject event = (JsonObject) jsonParser.parse(extra);

        if(b!=null) {
            RelativeLayout openGoogleMaps = (RelativeLayout) findViewById(R.id.open_maps);
            openGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1) + "," +  event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0)));
                startActivity(intent);
                }
            });
        }

    }

}
