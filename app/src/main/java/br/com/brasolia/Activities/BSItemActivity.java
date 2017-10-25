package br.com.brasolia.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;
import br.com.brasolia.adapters.BSCommentsAdapter;
import br.com.brasolia.adapters.BSImagesCarrouselAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.BSComment;
import br.com.brasolia.models.BSItem;
import br.com.brasolia.util.AlertUtil;
import br.com.brasolia.util.BSConnectionActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cayke on 12/04/17.
 */

public class BSItemActivity extends BSConnectionActivity {

    private TextView tvDistance, tvPhone;
    private ImageButton imageButtonComprarIngresso, imageButtonNomeLista;
    private LinearLayout btShare, btCall, btHour, btLike;
    private RecyclerView recyclerViewImages, recyclerViewComments;
    private EditText editText;
    private Button btSendMessage;
    private CircleImageView ivUser;

    private TextView qtd_comments;
    private TextView showMore;

    private boolean liked;

    private BSItem item;
    private List<BSComment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_event);

        item = getIntent().getParcelableExtra("item");


        //region SCREEN ELEMENTS
        showMore = (TextView) findViewById(R.id.activity_event_showMore);
        qtd_comments = (TextView) findViewById(R.id.activity_event_qtd_comments);
        qtd_comments.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_regular.ttf"));
        recyclerViewImages = (RecyclerView) findViewById(R.id.activity_event_photos_recycler);
        recyclerViewComments = (RecyclerView) findViewById(R.id.activity_event_recycler_comments);
        btShare = (LinearLayout) findViewById(R.id.activity_event_share);
        btCall = (LinearLayout) findViewById(R.id.activity_event_call);
        btHour = (LinearLayout) findViewById(R.id.activity_event_hour);
        btLike = (LinearLayout) findViewById(R.id.buttonLikeEvent);
        tvPhone = (TextView) findViewById(R.id.activity_event_textview_phone);
        imageButtonComprarIngresso = (ImageButton) findViewById(R.id.imageButtonComprarIngresso);
        imageButtonNomeLista = (ImageButton) findViewById(R.id.imageButtonNomeLista);
        editText = (EditText) findViewById(R.id.userComment);
        btSendMessage = (Button) findViewById(R.id.sendComment);
        ivUser = (CircleImageView) findViewById(R.id.activity_event_user_image);

        tvDistance = (TextView) findViewById(R.id.tvEventDistance);
        tvDistance.setText(String.format(Locale.getDefault(), "%.2fkm", item.getDistance()));
        TextView secondAddressTitle = (TextView) findViewById(R.id.secondAddressTitle);
        TextView eventFullAddress = (TextView) findViewById(R.id.eventFullAddress);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventDescription.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_regular.ttf"));
        TextView eventDate = (TextView) findViewById(R.id.eventDate);
        eventDate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_regular.ttf"));
        ImageView staticMapImage = (ImageView) findViewById(R.id.map_photo);
        TextView tvEventPrice = (TextView) findViewById(R.id.tvMaleTicketPrice);
        ImageView eventCover = (ImageView) findViewById(R.id.eventCover);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        eventName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_semibold.ttf"));
        CircleImageView brasolia = (CircleImageView) findViewById(R.id.profile_picture_event);


        imageButtonComprarIngresso.setVisibility(View.GONE);
        imageButtonNomeLista.setVisibility(View.GONE);
        //endregion

        //region Set Values on Screen
        if (item != null) {
            BSImageStorage.setImageWithPathToImageViewDownloadingIfNecessary(item.getThumb(), eventCover, 0, 750, 500, null);

            //todo colocar o icone bonitinho
            String price_rate = "";
            for (int i =0; i < item.getPrice_tier(); i++) {
                price_rate += "$";
            }
            tvEventPrice.setText(price_rate);

            eventName.setText(item.getName());
//            secondAddressTitle.setText(item.getLocality());
            eventFullAddress.setText(item.getAddress());
            eventDescription.setText(item.getDescription());
            tvPhone.setText(item.getPhone());

            //todo
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
//            eventDate.setText(formatter.format(event.getStartHour()));

            final int zoom = 11; // Requisitado para remover zoom.
            String staticMapUrl = "http://maps.google.com/maps/api/staticmap?center=" + item.getLatitude() + "," + item.getLongitude() + "&markers=icon:https://s3-us-west-2.amazonaws.com/s.cdpn.io/766702/tag_brasolia_2.png|" + item.getLatitude() + "," + item.getLongitude() + "&zoom=" + zoom + "&size=480x200&sensor=false";
            Picasso picasso = Picasso.with(BrasoliaApplication.getAppContext());
            picasso.setIndicatorsEnabled(false);
            picasso.load(staticMapUrl).resize(480, 200).into(staticMapImage);

//            if (!event.getTicketLink().isEmpty()) {
//                imageButtonComprarIngresso.setVisibility(View.VISIBLE);
//                imageButtonComprarIngresso.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getTicketLink()));
//                        startActivity(browserIntent);
//                    }
//                });
//            } else if (!event.getListLink().isEmpty()) {
//                imageButtonNomeLista.setVisibility(View.VISIBLE);
//                imageButtonNomeLista.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getListLink()));
//                        startActivity(browserIntent);
//                    }
//                });
//            }

            mountRecyclerImages();
            getComments();
        }
        //endregion


        final ImageView likeEvent = (ImageView) findViewById(R.id.likeEvent);

        //region get if user liked event
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            BSRequests requests = BSConnection.createService(BSRequests.class);
//            Call<JsonObject> call = requests.getLikeEvent(event.getId());
//
//            call.enqueue(new Callback<JsonObject>() {
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    if (response.isSuccessful()) {
//                        BSResponse bsResponse = new BSResponse(response.body());
//                        if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
//                            liked = (boolean) bsResponse.getData();
//                            if (liked)
//                                likeEvent.setImageResource(R.drawable.ic_love_filled);
//                        } else {
//                            liked = false;
//                        }
//                    } else {
//                        liked = false;
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//        }
        //endregion

        //region Comments area handle
        btSendMessage.setVisibility(View.GONE);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Picasso.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).resize(120, 120).into(ivUser);
        }
        else {
            Picasso.with(this).load(R.drawable.profile).resize(120, 120).into(ivUser);
        }
        //endregion


        //region buttons listeners

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comments != null) {
                    mountRecyclerComments(true);
                    showMore.setVisibility(View.GONE);
                }
            }
        });

//        btLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//                    toLikeUserShouldLogin();
//                } else {
//                    liked = !liked;
//                    if (liked)
//                        likeEvent.setImageResource(R.drawable.event_heart_pressed);
//                    else
//                        likeEvent.setImageResource(R.drawable.event_heart);
//
//                    BSRequests requests = BSConnection.createService(BSRequests.class);
//                    Call<JsonObject> call = requests.likeEvent(event.getId(), liked);
//
//                    call.enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                            if (response.isSuccessful()) {
//                                BSResponse bsResponse = new BSResponse(response.body());
//                                if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
//                                    Log.d("likeEvent", "success");
//                                } else {
//                                    Log.d("likeEvent", "server error");
//                                }
//                            } else {
//                                Log.d("likeEvent", "conection failure");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Log.d("likeEvent", "conection failure");
//                        }
//                    });
//                }
//            }
//        });

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView eventDate = (TextView) findViewById(R.id.eventDate);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "" +
                        "Venha conferir o evento *" + item.getName() + "* no " +
                        "*" + item.getAddress() + "* no dia " +
                        "*" + eventDate.getText().toString().replace(" ", " ás ") +
                        "*.\n\nConfira outros eventos no *app Brasólia!*");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = item.getPhone();

                if (phone != null && !phone.equals("?") && phone.length() >= 8 && phone != "") {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else
                    Toast.makeText(BSItemActivity.this, "Número de telefone desconhecido.", Toast.LENGTH_LONG).show();
            }
        });

        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        //endregion

        //region Comment' edit text listener
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i1 == 0) {
                    ivUser.setVisibility(View.GONE);
                    btSendMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText.getText().toString().equals("")) {
                    ivUser.setVisibility(View.VISIBLE);
                    btSendMessage.setVisibility(View.GONE);
                }
            }
        });

        //region maps
        RelativeLayout openMap = (RelativeLayout) findViewById(R.id.map_photo_layout);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BSItemActivity.this, EventMapActivity.class);
                i.putExtra("eventMap", item);
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
                                Intent i = new Intent(BSItemActivity.this, BSLoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("cameFromApp", true);
                                startActivity(i);
                                break;
                        }
                    }
                });
    }

    private void mountRecyclerImages() {
        if (item.getImages() != null && item.getImages().size() > 0) {
            recyclerViewImages.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewImages.setLayoutManager(layoutManager);
            recyclerViewImages.setAdapter(new BSImagesCarrouselAdapter(item.getImages()));
        } else {
            recyclerViewImages.setVisibility(View.GONE);
        }
    }

    private void mountRecyclerComments(boolean shouldShowMore) {
        if (comments != null && comments.size() > 0) {
            recyclerViewComments.setVisibility(View.VISIBLE);
            recyclerViewComments.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewComments.setLayoutManager(layoutManager);
            recyclerViewComments.setAdapter(new BSCommentsAdapter(comments, shouldShowMore));
        } else {
            recyclerViewComments.setVisibility(View.GONE);
        }
    }

    private void getComments() {
//        BSRequests requests = BSConnection.createService(BSRequests.class);
//        Call<JsonObject> call = requests.getComments(event.getId(), 1, 1000);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    BSResponse bsResponse = new BSResponse(response.body());
//                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
//                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();
//
//                        comments = new ArrayList<BSComment>();
//                        for (Map<String, Object> dictionary : data) {
//                            comments.add(new BSComment(dictionary));
//                        }
//
//                        updateCommentsQuantityLabel();
//                        if (comments.size() > 5) {
//                            showMore.setVisibility(View.VISIBLE);
//                            mountRecyclerComments(false);
//                        } else
//                            mountRecyclerComments(false);
//                    } else {
//                        Log.d("getComments", "server error");
//                    }
//                } else {
//                    Log.d("getComments", "conection failure");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.d("getComments", "conection failure");
//            }
//        });
    }

    private void makeComment(String comment) {
//        BSRequests requests = BSConnection.createService(BSRequests.class);
//        Call<JsonObject> call = requests.makeComment(event.getId(), comment);
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    BSResponse bsResponse = new BSResponse(response.body());
//                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
//
//                        getComments();
//                        editText.setText("");
//                        editText.setEnabled(true);
//                    } else {
//                        Toast.makeText(BSItemActivity.this, "Erro. Nao foi possivel enviar comentário", Toast.LENGTH_LONG).show();
//                        editText.setEnabled(true);
//                    }
//                } else {
//                    Toast.makeText(BSItemActivity.this, "Erro. Nao foi possivel enviar comentário", Toast.LENGTH_LONG).show();
//                    editText.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(BSItemActivity.this, "Erro. Nao foi possivel enviar comentário", Toast.LENGTH_LONG).show();
//                editText.setEnabled(true);
//            }
//        });
    }

    private void sendMessage() {
//        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//            toLikeUserShouldLogin();
//        } else {
//            editText.setEnabled(false);
//            makeComment(editText.getText().toString());
//        }
    }

    private void updateCommentsQuantityLabel() {
        if (comments.size() == 0)
            qtd_comments.setText("Sem comentários.");
        else if (comments.size() == 1)
            qtd_comments.setText("1 comentário.");
        else
            qtd_comments.setText(String.format(Locale.getDefault(), "%d comentários.", comments.size()));
    }
}