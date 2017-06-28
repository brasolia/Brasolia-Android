package br.com.brasolia.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.com.brasolia.BSLoginActivity;
import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.R;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.util.AlertUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 11/04/17.
 */

public class BSEventsAdapter extends RecyclerView.Adapter<BSEventViewHolder> {

    private List<BSEvent> events;
    private int choice;

    public BSEventsAdapter(List<BSEvent> events, int choice) {
        this.events = events;
        this.choice = choice;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public BSEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new BSEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BSEventViewHolder holder, int position) {
        holder.setEvent(events.get(position));
        holder.bindEvent(events.get(position), choice);
    }
}

class BSEventViewHolder extends RecyclerView.ViewHolder {

    private FrameLayout frameLayout;
    private ImageView heart_icon;
    private ImageView cover;
    private TextView title, place, date, price, distance;
    private Context context;
    private FrameLayout heart_icon_frameLayout;
    private boolean liked;
    private BSEvent event;

    public BSEventViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        heart_icon_frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayout_item_event_heart_icon);
        heart_icon = (ImageView) itemView.findViewById(R.id.item_event_heart_icon);
        distance = (TextView) itemView.findViewById(R.id.item_event_distance);
        price = (TextView) itemView.findViewById(R.id.item_event_price);
        title = (TextView) itemView.findViewById(R.id.item_event_title);
        title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/josefinsans_regular.ttf"));
        place = (TextView) itemView.findViewById(R.id.item_event_place);
        //place.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/josefinsans_thin.ttf"));
        date = (TextView) itemView.findViewById(R.id.item_event_date);
        //date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/josefinsans_thin.ttf"));

        frameLayout = (FrameLayout) itemView.findViewById(R.id.item_event_frameLayout);

        cover = (ImageView) itemView.findViewById(R.id.item_event_cover);

        heart_icon_frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BrasoliaApplication.getUser() == null) {
                    toLikeUserShouldLogin();
                }
                else {
                    liked = !liked;
                    if (liked)
                        heart_icon.setImageResource(R.drawable.ic_love_filled);
                    else
                        heart_icon.setImageResource(R.drawable.ic_love);

                    BSRequests requests = BSConnection.createService(BSRequests.class);
                    Call<JsonObject> call = requests.likeEvent(event.getId(), liked);

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                BSResponse bsResponse = new BSResponse(response.body());
                                if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                                    Log.d("likeEvent", "success");
                                } else {
                                    Log.d("likeEvent", "server error");
                                }
                            } else {
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
    }

    private void toLikeUserShouldLogin() {
        AlertUtil.confirm(context, "Entrar", context.getString(R.string.liked_logout), "Cancelar", "Conectar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent i = new Intent(context, BSLoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("cameFromApp", true);
                                context.startActivity(i);
                                break;
                        }
                    }
                });
    }

    public void setEvent(BSEvent event){
        this.event = event;
    }

    public BSEvent getEvent(){
        return event;
    }

    public void bindEvent(BSEvent event, int choice) {

        //region get if user liked event
        if (BrasoliaApplication.getUser() != null) {
            BSRequests requests = BSConnection.createService(BSRequests.class);
            Call<JsonObject> call = requests.getLikeEvent(getEvent().getId());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        BSResponse bsResponse = new BSResponse(response.body());
                        if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                            liked = (boolean) bsResponse.getData();
                            if (liked)
                                heart_icon.setImageResource(R.drawable.ic_love_filled);
                            else
                                heart_icon.setImageResource(R.drawable.ic_love);
                        } else {
                            liked = false;
                        }
                    } else {
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

        if (context instanceof AppCompatActivity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = (int) (width * 0.67);

            frameLayout.getLayoutParams().width = width;
            frameLayout.getLayoutParams().height = height;

            BSImageStorage.setEventImageNamed(event.getCoverImageKey(), cover, width, height, null);
        }

        switch (choice) {
            case 1:
                price.setVisibility(View.GONE);
                distance.setVisibility(View.GONE);
                break;
            case 2:
                price.setVisibility(View.GONE);
                if (event.getDistance() > 100)
                    distance.setVisibility(View.GONE);
                else
                    distance.setText(String.format(Locale.getDefault(), "%.2fkm", event.getDistance()));
                break;
            case 3:
                distance.setVisibility(View.GONE);
                break;
        }

        if (event.getPrices().get(0).getPrice() == 0)
            price.setText("Gratuito");
        else
            price.setText(String.format(Locale.getDefault(), "R$%.2f", event.getPrices().get(0).getPrice()));
        
        title.setText(event.getName());
        place.setText(event.getLocality());

        if (event.getStartHour().getDay() == event.getEndHour().getDay() &&
                event.getStartHour().getMonth() == event.getEndHour().getMonth()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
            date.setText(formatter.format(event.getStartHour()));
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
            date.setText(formatter.format(event.getStartHour()) + " a " + formatter.format(event.getEndHour()));
        }
    }
}