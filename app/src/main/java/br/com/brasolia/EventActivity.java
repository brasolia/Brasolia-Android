package br.com.brasolia;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.adapters.BSImagesCarrouselAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.models.BSEventPrice;
import br.com.brasolia.models.BSUser;
import br.com.brasolia.util.AlertUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    private TextView tvDistance, tvPhone;
    private ImageButton imageButtonComprarIngresso, imageButtonNomeLista;
    private LinearLayout btShare, btCall, btHour;
    private RecyclerView recyclerView;

    private boolean liked;
    private ProgressDialog loading;

    private BSEvent event;
    private BSUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        event = getIntent().getParcelableExtra("event");


        //region SCREEN ELEMENTS
        recyclerView = (RecyclerView) findViewById(R.id.activity_event_photos_recycler);
        btShare = (LinearLayout) findViewById(R.id.activity_event_share);
        btCall = (LinearLayout) findViewById(R.id.activity_event_call);
        btHour = (LinearLayout) findViewById(R.id.activity_event_hour);
        tvPhone = (TextView) findViewById(R.id.activity_event_textview_phone);
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
        TextView rating = (TextView) findViewById(R.id.activity_event_rating);


        imageButtonComprarIngresso.setVisibility(View.GONE);
        imageButtonNomeLista.setVisibility(View.GONE);
        //endregion

        loading = new ProgressDialog(this);
        loading.setTitle("Aguarde");
        loading.setMessage("Carregando comentários...");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        //region Set Values on Screen
        if (event != null) {
            BSImageStorage.setEventImageNamed(event.getCoverImageKey(), eventCover, 750, 500);

            if (event.getPrices().size() > 0) {
                BSEventPrice price = event.getPrices().get(0);
                tvEventPrice.setText(String.format(Locale.getDefault(), "R$%.2f", price.getPrice()));

            }
            else
                tvEventPrice.setText("Free");

            if (event.getRating() > 0)
                rating.setText("8.5/10");
            else
                rating.setVisibility(View.GONE);

            eventName.setText(event.getName());
            addressTitle.setText(event.getLocality());
            secondAddressTitle.setText(event.getLocality());
            eventFullAddress.setText(event.getAddress());
            eventDescription.setText(event.getDescription());
            tvPhone.setText(event.getPhone());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getStartHour());
            eventDate.setText(formatter.format(calendar.getTime()));
            final int zoom = 11; // Requisitado para remover zoom.

            String staticMapUrl = "http://maps.google.com/maps/api/staticmap?center=" + event.getLatitute() + "," + event.getLongitude() + "&markers=icon:https://s3-us-west-2.amazonaws.com/s.cdpn.io/766702/tag_brasolia_2.png|" + event.getLatitute() + "," + event.getLongitude() + "&zoom=" + zoom + "&size=480x200&sensor=false";
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(staticMapUrl, staticMapImage);

            if (!event.getTicketLink().isEmpty()) {
                imageButtonComprarIngresso.setVisibility(View.VISIBLE);
                imageButtonComprarIngresso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getTicketLink()));
                        startActivity(browserIntent);
                    }
                });
            } else if (!event.getListLink().isEmpty()) {
                imageButtonNomeLista.setVisibility(View.VISIBLE);
                imageButtonNomeLista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getListLink()));
                        startActivity(browserIntent);
                    }
                });
            }

            mountRecycler();
        }
        //endregion

        user = BrasoliaApplication.getUser();

        final ImageView likeEvent = (ImageView) findViewById(R.id.likeEvent);

        //region get if User liked event
        if (user != null) {
            BSRequests requests = BSConnection.createService(BSRequests.class);
            Call<JsonObject> call = requests.getLikeEvent(event.getId());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        BSResponse bsResponse = new BSResponse(response.body());
                        if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                            //todo testar isso
                            liked = (boolean) bsResponse.getData();
                            if (liked)
                                likeEvent.setImageResource(R.drawable.event_heart_pressed);
                        }
                        else {
                            liked = false;
                        }
                    }
                    else {
                        liked = false;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        //endregion

        //region buttons listeners
        likeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    toLikeUserShouldLogin();
                }
                else {
                    liked = !liked;
                    if (liked)
                        likeEvent.setImageResource(R.drawable.event_heart_pressed);
                    else
                        likeEvent.setImageResource(R.drawable.event_heart);

                    BSRequests requests = BSConnection.createService(BSRequests.class);
                    Call<JsonObject> call = requests.likeEvent(event.getId(), liked);

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                BSResponse bsResponse = new BSResponse(response.body());
                                if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                                    Log.d("likeEvent", "success");
                                }
                                else {
                                    Log.d("likeEvent", "server error");
                                }
                            }
                            else {
                                Log.d("likeEvent", "conection failure");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("likeEvent", "conection failure");
                        }
                    });
                }
            }
        });

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView eventDate = (TextView) findViewById(R.id.eventDate);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "" +
                        "Venha conferir o evento *" + event.getName() + "* no " +
                        "*" + event.getLocality() + "* no dia " +
                        "*" + eventDate.getText().toString().replace(" ", " ás ") +
                        "*.\n\nConfira outros eventos no *app Brasólia!*");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = event.getPhone();

                if (phone != null && !phone.equals("?") && phone.length() >= 8 && phone != "") {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else
                    Toast.makeText(EventActivity.this, "Número de telefone desconhecido.", Toast.LENGTH_LONG).show();
            }
        });

        //todo mudar
        btHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();

                Intent i = new Intent(EventActivity.this, CommentActivity.class);

                i.putExtra("eventId", event.getId());
                startActivity(i);

                loading.dismiss();

            }
        });
        //endregion

        //region maps
        RelativeLayout openMap = (RelativeLayout) findViewById(R.id.map_photo_layout);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventActivity.this, EventMapActivity.class);
                i.putExtra("eventMap", event);
                startActivity(i);
            }
        });
        //endregion

        CircleImageView closeEvent = (CircleImageView) findViewById(R.id.closeEvent);
        closeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void toLikeUserShouldLogin() {
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

    private void mountRecycler() {
        if (event.getImages() != null && event.getImages().size() > 0) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BSImagesCarrouselAdapter(event.getImages()));
        }
        else {
            recyclerView.setVisibility(View.GONE);
        }
    }
}

