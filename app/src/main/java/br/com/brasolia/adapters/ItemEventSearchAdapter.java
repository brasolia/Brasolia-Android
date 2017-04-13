package br.com.brasolia.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;


/**
 * Created by eduardo on 10/10/16.
 */

public class ItemEventSearchAdapter extends BaseAdapter {
    private Activity activity;
    private Context mContext;
    private JsonArray data;

    public  ItemEventSearchAdapter(Activity activity, Context context, JsonArray data){
        this.activity = activity;
        this.mContext = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_event_search, null, true);

        JsonObject item = data.get(position).getAsJsonObject();

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        ImageView imgEvent = (ImageView) view.findViewById(R.id.imgEventSearch);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitleEventSearch);
        TextView tvPlace= (TextView) view.findViewById(R.id.tvPlaceEventSearch);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDateEventSearch);


        if(!item.get("cover").getAsString().equals("")) {
            BSImageStorage.setEventImageNamed(item.get("cover").getAsString(), imgEvent, 200,200);

        } else imgEvent.setImageResource(R.drawable.brasolia_logo);

        tvTitle.setText(item.get("name").getAsString());
        tvPlace.setText(item.get("locality").getAsJsonObject().get("address").getAsString());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.get("dates").getAsJsonArray().get(0).getAsJsonObject().get("datehour_init").getAsJsonObject().get("$date").getAsLong());
        tvDate.setText(formatter.format(calendar.getTime()));

        return view;
    }
}
