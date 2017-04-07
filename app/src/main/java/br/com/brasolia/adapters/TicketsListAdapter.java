package br.com.brasolia.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import br.com.brasolia.R;

public class TicketsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final JsonArray data;

    public TicketsListAdapter(Activity context, JsonArray data){
        super(context, R.layout.list_event);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.data = data;
    }

    @Override
    public int getCount(){
        return data!=null ? data.size() : 0;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowViewComments = inflater.inflate(R.layout.list_tickets, null,false);

        // screen elements  ------------------------------------------------------------
        TextView etEventTitle = (TextView) rowViewComments.findViewById(R.id.tvEventTitle);
        TextView etEventDescription = (TextView) rowViewComments.findViewById(R.id.tvEventDescription);
        TextView etEventDate = (TextView) rowViewComments.findViewById(R.id.tvEventDate);
        ImageView imgEvent = (ImageView) rowViewComments.findViewById(R.id.imgEvent);
        // -----------------------------------------------------------------------------

        JsonObject item = data.get(position).getAsJsonObject();
        etEventTitle.setText(item.get("name").getAsString());
        etEventDescription.setText(item.get("description").getAsString());
        etEventDate.setText(item.get("date").getAsString());



        return rowViewComments;

    };
}