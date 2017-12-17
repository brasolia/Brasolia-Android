package br.com.brasolia.Activities;

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

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.Connectivity.BSRequestService;
import br.com.brasolia.R;
import br.com.brasolia.adapters.BSCommentsAdapter;
import br.com.brasolia.adapters.BSItemHoursAdapter;
import br.com.brasolia.models.BSComment;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.models.BSItem;
import br.com.brasolia.models.BSVenue;
import br.com.brasolia.util.BSAlertUtil;
import br.com.brasolia.util.BSConnectionActivity;
import br.com.brasolia.util.BSFirebaseListenerRef;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cayke on 12/04/17.
 */

public class BSItemActivity extends BSConnectionActivity {

    private RecyclerView recyclerViewComments, recyclerViewHours;
    private SliderLayout sliderShow;
    private EditText editText;
    private Button btSendMessage;
    private CircleImageView ivUser;

    private TextView qtd_comments;
    private TextView showMore;

    private BSItem item;
    private List<BSComment> comments;

    BSFirebaseListenerRef mRef;

    @Override
    protected void onDestroy() {
        if (mRef != null)
            mRef.detach();

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_event);

        item = getIntent().getParcelableExtra("item");


        //region SCREEN ELEMENTS
        TextView tvDistance, tvPhone;
        ImageButton imageButtonComprarIngresso, imageButtonNomeLista;
        LinearLayout btShare, btCall, btHour;
        RelativeLayout btLike;

        showMore = (TextView) findViewById(R.id.activity_event_showMore);
        qtd_comments = (TextView) findViewById(R.id.activity_event_qtd_comments);
        qtd_comments.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_regular.ttf"));
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        recyclerViewComments = (RecyclerView) findViewById(R.id.activity_event_recycler_comments);
        recyclerViewHours = (RecyclerView) findViewById(R.id.activity_event_recycler_hours);

        btShare = (LinearLayout) findViewById(R.id.activity_event_share);
        btCall = (LinearLayout) findViewById(R.id.activity_event_call);
        btLike = (RelativeLayout) findViewById(R.id.buttonLikeEvent);
        tvPhone = (TextView) findViewById(R.id.activity_event_textview_phone);
        editText = (EditText) findViewById(R.id.userComment);
        btSendMessage = (Button) findViewById(R.id.sendComment);
        ivUser = (CircleImageView) findViewById(R.id.activity_event_user_image);

        TextView secondAddressTitle = (TextView) findViewById(R.id.secondAddressTitle);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventDescription.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_regular.ttf"));
        TextView eventDate = (TextView) findViewById(R.id.eventDate);
        eventDate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/josefinsans_semibold.ttf"));
        ImageView eventCover = (ImageView) findViewById(R.id.eventCover);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        eventName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BebasNeue_bold.ttf"));
        //endregion

        //region Set Values on Screen
        if (item != null) {
            BSImageStorage.setImageWithPathToImageViewDownloadingIfNecessary(item.getThumb(), eventCover, 0, 750, 500, null);

            //todo colocar o icone bonitinho
            String price_rate = "";
            for (int i =0; i < item.getPrice_tier(); i++) {
                price_rate += "$";
            }

            eventName.setText(item.getName());
            eventDescription.setText(item.getDescription());
            tvPhone.setText(item.getPhone());

            mountRecyclerImages();
            mountRecyclerHours();
            getComments();
        }
        //endregion


        final ImageView likeEvent = (ImageView) findViewById(R.id.likeEvent);

        if (item.isLiked()) {
            likeEvent.setImageResource(R.drawable.ic_love_filled);
        }

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

        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    BSAlertUtil.toLikeUserShouldLogin();
                } else {
                    item.setLiked(!item.isLiked());

                    BSRequestService.likeItem(item.isLiked(), item, user.getUid());

                    if (item.isLiked()) {
                        likeEvent.setImageResource(R.drawable.ic_love_filled);
                    }
                    else {
                        likeEvent.setImageResource(R.drawable.ic_love);
                    }
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

                if (phone != null && !phone.equals("?") && phone.length() >= 8 && !phone.equals("")) {
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

//        //region maps
//        RelativeLayout openMap = (RelativeLayout) findViewById(R.id.map_photo_layout);
//        openMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(BSItemActivity.this, EventMapActivity.class);
//                i.putExtra("eventMap", item);
//                startActivity(i);
//            }
//        });
//        //endregion

        LinearLayout closeEvent = (LinearLayout) findViewById(R.id.closeEvent);
        closeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void mountRecyclerImages() {
        if (item.getImages() != null && item.getImages().size() > 0) {
            for (String imageURL : item.getImages()) {
                DefaultSliderView sliderView = new DefaultSliderView(this);
                sliderView.image(imageURL);
                sliderShow.addSlider(sliderView);
            }
        } else {
            sliderShow.setVisibility(View.GONE);
        }
    }

    private void mountRecyclerHours() {
        if (item instanceof BSEvent) {
            BSEvent event = (BSEvent) item;
            recyclerViewHours.setAdapter(new BSItemHoursAdapter(event.getSchedule_hours(), null));
        }
        else {
            BSVenue venue = (BSVenue) item;
            recyclerViewHours.setAdapter(new BSItemHoursAdapter(null, venue.getOperating_hours()));
        }
        recyclerViewHours.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewHours.setLayoutManager(layoutManager);
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
        mRef = BSRequestService.getCommentsForItem(item, this);
    }

    private void makeComment(String comment) {
        Date today = new Date();
        BSRequestService.makeComment(comment, FirebaseAuth.getInstance().getCurrentUser(), item, today.getTime());
        editText.setText("");
        editText.setEnabled(true);
        btSendMessage.setEnabled(true);
    }

    private void sendMessage() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            BSAlertUtil.toLikeUserShouldLogin();
        } else {
            editText.setEnabled(false);
            btSendMessage.setEnabled(false);
            makeComment(editText.getText().toString());
        }
    }

    private void updateCommentsQuantityLabel() {
        if (comments == null || comments.size() == 0)
            qtd_comments.setText("Sem comentários.");
        else if (comments.size() == 1)
            qtd_comments.setText("1 comentário.");
        else
            qtd_comments.setText(String.format(Locale.getDefault(), "%d comentários.", comments.size()));
    }

    @Override
    public void commentsUpdated(boolean success, List<BSComment> comments) {
        if (success && comments != null) {

            Collections.reverse(comments);
            this.comments = comments;

                    updateCommentsQuantityLabel();
            mountRecyclerComments(false);

            if (comments.size() > 5) {
                showMore.setVisibility(View.VISIBLE);
            } else {
                showMore.setVisibility(View.GONE);
            }
        }
        else {
            updateCommentsQuantityLabel();
        }
    }
}