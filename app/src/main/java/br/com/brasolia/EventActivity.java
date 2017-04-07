package br.com.brasolia;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.eventbus.UpdateLike;
import br.com.brasolia.models.User;
import br.com.brasolia.util.AlertUtil;
import br.com.brasolia.webserver.BrasoliaAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    private TextView tvDistance;
    private ImageView btshareEvent;
    private ImageButton btleaveComment, btmakeCall, imageButtonComprarIngresso, imageButtonNomeLista;
    private SharedPreferences sp;
    private User user;
    private double latitude, longitude;
    private boolean liked;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        btshareEvent = (ImageView) findViewById(R.id.btshareEvent);
        btleaveComment = (ImageButton) findViewById(R.id.btleaveComment);
        btmakeCall = (ImageButton) findViewById(R.id.btCall);
        imageButtonComprarIngresso = (ImageButton) findViewById(R.id.imageButtonComprarIngresso);
        imageButtonNomeLista = (ImageButton) findViewById(R.id.imageButtonNomeLista);

        tvDistance = (TextView) findViewById(R.id.tvEventDistance);
        TextView secondAddressTitle = (TextView) findViewById(R.id.secondAddressTitle);
        TextView eventFullAddress = (TextView) findViewById(R.id.eventFullAddress);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        TextView eventDate = (TextView) findViewById(R.id.eventDate);
        ImageView staticMapImage = (ImageView) findViewById(R.id.map_photo);
        TextView tvEventPrice = (TextView) findViewById(R.id.tvMaleTicketPrice);
        ImageView eventCover = (ImageView) findViewById(R.id.eventCover);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        TextView addressTitle = (TextView) findViewById(R.id.addressTitle);


        imageButtonComprarIngresso.setVisibility(View.GONE);
        imageButtonNomeLista.setVisibility(View.GONE);

        sp = PreferenceManager.getDefaultSharedPreferences(EventActivity.this);
        latitude = Double.parseDouble(sp.getString("latitude", "-1.0"));
        longitude = Double.parseDouble(sp.getString("longitude", "-1.0"));

        loading = new ProgressDialog(this);
        loading.setTitle("Aguarde");
        loading.setMessage("Carregando comentários...");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        final BrasoliaAPI api = ((BrasoliaApplication) EventActivity.this.getApplication()).getApi();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EventActivity.this);

        Intent in = getIntent();
        Bundle b = in.getExtras();

        String extra = getIntent().getStringExtra("event");
        JsonParser jsonParser = new JsonParser();

        final JsonObject event = (JsonObject) jsonParser.parse(extra);

        if (b != null) {

            ImageLoader imageLoader = ImageLoader.getInstance();

            if (!event.get("cover").isJsonNull()) {
                String imageUrl = "https://s3-us-west-2.amazonaws.com/bs.cover/" + event.get("cover").getAsString();
                imageLoader.displayImage(imageUrl, eventCover);
            } else {
                eventCover.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                eventCover.setImageResource(R.drawable.brasolia_logo_preto);
            }

            if (event.get("prices").getAsJsonArray().size() > 0) {
                double _price = -1;
                int i = 0;

                while (i < event.get("prices").getAsJsonArray().size()) {
                    if (_price == -1 || _price > event.get("prices").getAsJsonArray().get(i).getAsJsonObject().get("value").getAsDouble())
                        _price = event.get("prices").getAsJsonArray().get(i).getAsJsonObject().get("value").getAsDouble();

                    i++;
                }

                if (i > 2)
                    tvEventPrice.setText("R$ " + String.format("%.2f", event.get("prices").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsDouble() + "+"));
                else
                    tvEventPrice.setText("R$ " + String.format("%.2f", event.get("prices").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsDouble()));
            } else
                tvEventPrice.setText("Free");

            eventName.setText(event.get("name").getAsString());

            addressTitle.setText(event.get("locality").getAsJsonObject().get("title").getAsString());

            secondAddressTitle.setText(event.get("locality").getAsJsonObject().get("title").getAsString());

            eventFullAddress.setText(event.get("locality").getAsJsonObject().get("address").getAsString());

            eventDescription.setText(event.get("description").getAsString());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(event.get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong());
            eventDate.setText(formatter.format(calendar.getTime()));
            final int zoom = 11; // Requisitado para remover zoom.

            String staticMapUrl = "http://maps.google.com/maps/api/staticmap?center=" + event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1) + "," + event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0) + "&markers=icon:https://s3-us-west-2.amazonaws.com/s.cdpn.io/766702/tag_brasolia_2.png|" + event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1) + "," + event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0) + "&zoom=" + zoom + "&size=480x200&sensor=false";
            imageLoader.displayImage(staticMapUrl, staticMapImage);

            if (!event.get("ticket_link").getAsString().isEmpty()) {
                imageButtonComprarIngresso.setVisibility(View.VISIBLE);
                imageButtonComprarIngresso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.get("ticket_link").getAsString()));
                        startActivity(browserIntent);
                    }
                });
            } else if (!event.get("list_link").getAsString().isEmpty()) {
                imageButtonNomeLista.setVisibility(View.VISIBLE);
                imageButtonNomeLista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.get("list_link").getAsString()));
                        startActivity(browserIntent);
                    }
                });
            }
        }

        CircleImageView profilePictureEvent = (CircleImageView) findViewById(R.id.profile_picture_event);

        user = User.getUser(this);

        if (user != null) {
            String imageUrl = user.getPhoto();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, profilePictureEvent);
        }

        final ImageView likeEvent = (ImageView) findViewById(R.id.likeEvent);

        Call<JsonObject> getLikeEvent = api.getLikeEvent(preferences.getString("cookie", ""), event.get("id").getAsJsonObject().get("$oid").getAsString());

        getLikeEvent.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body().getAsJsonObject().get("data").getAsString() == "true") {
                        liked = true;
                        likeEvent.setImageResource(R.drawable.event_heart_pressed);
                    } else liked = false;
                } else {
                    liked = false;

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loading.dismiss();
                t.printStackTrace();
            }
        });


        likeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("cookie", "").equals("")) {
                    likeLogout();
                    return;
                }

                Call<JsonObject> likeEventService = api.likeEventService(preferences.getString("cookie", ""), event.get("id").getAsJsonObject().get("$oid").getAsString(), String.valueOf(!liked));

                likeEventService.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {

                            liked = !liked;

                            if (liked) likeEvent.setImageResource(R.drawable.event_heart_pressed);
                            else likeEvent.setImageResource(R.drawable.event_heart);

                            EventBus.getDefault().post(new UpdateLike());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });


        btshareEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView eventDate = (TextView) findViewById(R.id.eventDate);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "" +
                        "Venha conferir o evento *" + event.get("name").getAsString() + "* no " +
                        "*" + event.get("locality").getAsJsonObject().get("title").getAsString() + "* no dia " +
                        "*" + eventDate.getText().toString().replace(" ", " ás ") +
                        "*.\n\nConfira outros eventos no *app Brasólia!*");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        RelativeLayout openMap = (RelativeLayout) findViewById(R.id.map_photo_layout);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventActivity.this, EventMapActivity.class);
                i.putExtra("eventMap", getIntent().getStringExtra("event"));
                startActivity(i);
            }
        });

        CircleImageView closeEvent = (CircleImageView) findViewById(R.id.closeEvent);
        closeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btmakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = event.get("phone").getAsString();

                if (phone != null && !phone.equals("?") && phone.length() >= 8 && phone != "") {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else
                    Toast.makeText(EventActivity.this, "Número de telefone desconhecido.", Toast.LENGTH_LONG).show();
            }
        });

        btleaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();

                Intent i = new Intent(EventActivity.this, CommentActivity.class);

                i.putExtra("eventId", event.get("id").getAsJsonObject().get("$oid").getAsString());
                startActivity(i);

                loading.dismiss();

            }
        });

        if (latitude != -1.0 && longitude != -1.0) {
            Location userLocation = new Location("pointA");

            userLocation.setLatitude(latitude);
            userLocation.setLongitude(longitude);

            Double eventLatitude = event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble();
            Double eventLongitude = event.get("locality").getAsJsonObject().get("point").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble();

            Location eventLocation = new Location("pointB");

            eventLocation.setLatitude(eventLatitude);
            eventLocation.setLongitude(eventLongitude);

            Float distance = userLocation.distanceTo(eventLocation);

            distance = distance / 1000; // in kilometers

            tvDistance.setText("(" + String.format("%.2f", distance) + " km) ");
        }
    }

    private void likeLogout() {
        AlertUtil.confirm(this, "Entrar", getString(R.string.liked_logout), "Cancelar", "Conectar",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent i = new Intent(EventActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        break;
                }
            }
        });
    }

}

