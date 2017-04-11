package br.com.brasolia.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

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
    public void onBindViewHolder(BSEventViewHolder holder, int position) {
        holder.bindEvent(events.get(position));
    }
}

class BSEventViewHolder extends RecyclerView.ViewHolder {
    ImageView cover;

    Context context;

    public BSEventViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        cover = (ImageView) itemView.findViewById(R.id.item_event_cover);
    }

    public void bindEvent(BSEvent event) {
        if (context instanceof AppCompatActivity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = (int) (width*0.67);

            BSImageStorage.setEventImageNamed(event.getCoverImageKey(), cover, width, height);
        }
        else {
            BSImageStorage.setEventImageNamed(event.getCoverImageKey(), cover, 600, 400);
        }
    }
}