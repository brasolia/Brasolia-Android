package br.com.brasolia.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import br.com.brasolia.LoginActivity;
import br.com.brasolia.R;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.util.AlertUtil;
import br.com.brasolia.webserver.BrasoliaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private JsonArray data;
    private int positionFilter;
    private boolean liked;

    public CustomListAdapter(Activity context, JsonArray data) {
        super(context, R.layout.list_event);

        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_event, null, false);

        TextView eventTitle = (TextView) rowView.findViewById(R.id.textList);
        final RoundedImageView roundedImageView = (RoundedImageView) rowView.findViewById(R.id.eventImage);
        TextView addressTitle = (TextView) rowView.findViewById(R.id.textViewList);
        TextView eventDate = (TextView) rowView.findViewById(R.id.eventDate);
        final ImageView likeEvent = (ImageView) rowView.findViewById(R.id.likeEvent);
        final ProgressBar progress = (ProgressBar) rowView.findViewById(R.id.progressImageEvent);

        final JsonObject item = data.get(position).getAsJsonObject();

        TextView infoText = (TextView) rowView.findViewById(R.id.infoTV);
        infoText.setVisibility(View.GONE);
        likeEvent.setVisibility(View.GONE);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (positionFilter == 0) {
            likeEvent.setVisibility(View.VISIBLE);

            final BrasoliaAPI api = ((BrasoliaApplication) getContext().getApplication()).getApi();

            Call<JsonObject> getLikeEvent = api.getLikeEvent(preferences.getString("cookie", ""), item.get("id").getAsString());

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

                    Call<JsonObject> likeEventService = api.likeEventService(preferences.getString("cookie", ""), item.get("id").getAsString(), String.valueOf(!liked));

                    likeEventService.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {

                                liked = !liked;

                                if (liked)
                                    likeEvent.setImageResource(R.drawable.event_heart_pressed);
                                else likeEvent.setImageResource(R.drawable.event_heart);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });
        } else if (positionFilter == 1) {
            infoText.setVisibility(View.VISIBLE);

            double latitude = Double.parseDouble(preferences.getString("latitude", "-1.0"));
            double longitude = Double.parseDouble(preferences.getString("longitude", "-1.0"));

            if (latitude != -1.0 && longitude != -1.0) {
                Location userLocation = new Location("pointA");

                userLocation.setLatitude(latitude);
                userLocation.setLongitude(longitude);

                Double eventLatitude = item.get("latitude").getAsDouble();
                Double eventLongitude = item.get("longitude").getAsDouble();

                Location eventLocation = new Location("pointB");

                eventLocation.setLatitude(eventLatitude);
                eventLocation.setLongitude(eventLongitude);

                Float distance = userLocation.distanceTo(eventLocation);

                distance = distance / 1000; // in kilometers

                infoText.setText(String.format("%.1f", distance) + " km");
            }
        } else if (positionFilter == 2) {
            infoText.setVisibility(View.VISIBLE);
            if (item.get("price").getAsDouble() == 0) {
                infoText.setText("Free");
            } else {
                infoText.setText("R$ " + String.format("%.2f", item.get("price").getAsDouble()));
            }
        }

        eventTitle.setText(item.get("title").getAsString());
        String imageUrl = null;

        if (item.get("cover") != null) {

            imageUrl = item.get("cover").getAsString();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, roundedImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    roundedImageView.setImageResource(R.drawable.brasolia_logo);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progress.setVisibility(View.GONE);
                    roundedImageView.setImageResource(R.drawable.brasolia_logo);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    roundedImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progress.setVisibility(View.GONE);
                    roundedImageView.setImageResource(R.drawable.brasolia_logo);

                }
            });
        } else {
            progress.setVisibility(View.GONE);
            roundedImageView.setImageResource(R.drawable.brasolia_logo);
        }

        addressTitle.setText(item.get("addressTitle").getAsString());

        eventDate.setText(item.get("eventDate").getAsString() + "h");

        return rowView;
    }

    private void likeLogout() {
        AlertUtil.confirm(getContext(), "Entrar", getContext().getString(R.string.liked_logout), "Cancelar",
                "Conectar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(i);
                        getContext().finish();
                        break;
                }
            }
        });
    }

    @Override
    public Activity getContext() {
        return context;
    }

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }

    public void setPositionFilter(int positionFilter) {
        this.positionFilter = positionFilter;
    }
}