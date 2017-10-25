package br.com.brasolia.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;
import br.com.brasolia.models.BSItem;

/**
 * Created by cayke on 13/04/17.
 */

public class BSSearchAdapter extends RecyclerView.Adapter<BSSearchViewHolder> {
    private List<BSItem> items;

    public BSSearchAdapter(List<BSItem> items) {
        this.items = items;
    }

    @Override
    public BSSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_search, parent, false);
        return new BSSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BSSearchViewHolder holder, int position) {
        holder.bindEvent(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
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

    void bindEvent(BSItem item) {
        BSImageStorage.setImageWithPathToImageViewDownloadingIfNecessary(item.getThumb(), imgEvent, 0, 200,200, null);

        tvTitle.setText(item.getName());
//        tvPlace.setText(item.getLocality());
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(item.getStartHour());
//        tvDate.setText(formatter.format(calendar.getTime()));
    }
}
