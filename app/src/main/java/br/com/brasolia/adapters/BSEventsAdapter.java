package br.com.brasolia.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;
import br.com.brasolia.models.BSEvent;

/**
 * Created by cayke on 11/04/17.
 */

public class BSEventsAdapter extends RecyclerView.Adapter<BSEventViewHolder> {
    private List<BSEvent> events;

    public BSEventsAdapter(List<BSEvent> events) {
        this.events = events;
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
        holder.bindEvent(events.get(position));

    }
}

class BSEventViewHolder extends RecyclerView.ViewHolder {

    private ImageView cover;
    private TextView title, place, date, price;
    private Context context;

    public BSEventViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        price = (TextView) itemView.findViewById(R.id.item_event_price);
        title = (TextView) itemView.findViewById(R.id.item_event_title);
        place = (TextView) itemView.findViewById(R.id.item_event_place);
        date = (TextView) itemView.findViewById(R.id.item_event_date);

        cover = (ImageView) itemView.findViewById(R.id.item_event_cover);
    }

    public void bindEvent(BSEvent event) {
        if (context instanceof AppCompatActivity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = (int) (width*0.67);

            price.setText(String.format(Locale.getDefault(), "R$%.2f", event.getPrices().get(0).getPrice()));
            title.setText(event.getName());
            place.setText(event.getLocality());

            if(event.getStartHour().getDate() == event.getEndHour().getDate() &&
                    event.getStartHour().getMonth() == event.getEndHour().getMonth())
                date.setText(String.format(Locale.getDefault(), "%s/%s",
                        getDate(event.getStartHour().getDate()),
                        getDate(event.getStartHour().getMonth())));
            else
                date.setText(String.format(Locale.getDefault(), "%s/%s a %s/%s",
                        getDate(event.getStartHour().getDate()),
                        getDate(event.getStartHour().getMonth()),
                        getDate(event.getEndHour().getDate()),
                        getDate(event.getEndHour().getMonth())));


            /*if(event.getStartHour().compareTo(event.getEndHour()) == 0)
                date.setText(String.format(Locale.getDefault(), "%s/%s",
                        getDate(event.getStartHour().getDate()),
                        getDate(event.getStartHour().getMonth())));
            else
                date.setText(String.format(Locale.getDefault(), "%s/%s a %s/%s",
                        getDate(event.getStartHour().getDate()),
                        getDate(event.getStartHour().getMonth()),
                        getDate(event.getEndHour().getDate()),
                        getDate(event.getEndHour().getMonth())));*/


            BSImageStorage.setEventImageNamed(event.getCoverImageKey(), cover, width, height);

        } else {

            title.setText(event.getName());
            place.setText(event.getLocality());
            date.setText(event.getStartHour() + " - " + event.getEndHour());

            BSImageStorage.setEventImageNamed(event.getCoverImageKey(), cover, 600, 400);
        }
    }

    public String getDate(int date){

        if(date < 10)
            return "0" + date;
        else
            return Integer.toString(date);
    }
}