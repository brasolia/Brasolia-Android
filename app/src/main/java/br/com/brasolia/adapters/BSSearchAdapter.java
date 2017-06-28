package br.com.brasolia.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;
import br.com.brasolia.models.BSEvent;

/**
 * Created by cayke on 13/04/17.
 */

public class BSSearchAdapter extends RecyclerView.Adapter<BSSearchViewHolder> {
    private List<BSEvent> events;

    public BSSearchAdapter(List<BSEvent> events) {
        this.events = events;
    }

    @Override
    public BSSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_search, parent, false);
        return new BSSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BSSearchViewHolder holder, int position) {
        holder.bindEvent(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

class BSSearchViewHolder extends RecyclerView.ViewHolder {
    private  ImageView imgEvent;
    private TextView tvTitle , tvPlace, tvDate;

    BSSearchViewHolder(View itemView) {
        super(itemView);

        imgEvent = (ImageView) itemView.findViewById(R.id.imgEventSearch);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitleEventSearch);
        tvPlace= (TextView) itemView.findViewById(R.id.tvPlaceEventSearch);
        tvDate = (TextView) itemView.findViewById(R.id.tvDateEventSearch);
    }

    void bindEvent(BSEvent event) {
        BSImageStorage.setEventImageNamed(event.getCoverImageKey(), imgEvent, 200,200, null);

        tvTitle.setText(event.getName());
        tvPlace.setText(event.getLocality());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartHour());
        tvDate.setText(formatter.format(calendar.getTime()));
    }
}
