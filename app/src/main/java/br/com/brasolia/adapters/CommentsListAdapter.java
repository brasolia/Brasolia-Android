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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.brasolia.R;

public class CommentsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private JsonArray data;

    public CommentsListAdapter(Activity context, JsonArray data){
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
        View rowViewComments = inflater.inflate(R.layout.list_comments, null,false);

        TextView messageTitle = (TextView) rowViewComments.findViewById(R.id.textList);
        TextView messageContent = (TextView) rowViewComments.findViewById(R.id.messageContent);
        TextView messageDate = (TextView) rowViewComments.findViewById(R.id.messageDate);
        final ImageView imgProfile = (ImageView) rowViewComments.findViewById(R.id.imgProfileComment);



        JsonObject item = data.get(position).getAsJsonObject();

        messageTitle.setText(item.get("user").getAsJsonObject().get("fName").getAsString());
        messageContent.setText(item.get("message").getAsString());



        final ImageLoader imageLoader = ImageLoader.getInstance();

        //String image = item.get("user").getAsJsonObject().get("image").getAsString();
        //Log.e("url imagem", ""+image);

        //if(image != null && !image.isEmpty()){

           // String imageUrl = "https://s3-us-west-2.amazonaws.com/bs.thumb/"+image;
           /* imageLoader.displayImage(imageUrl, imgProfile, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    imgProfile.setImageResource(R.drawable.profile);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            */
        //}


        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.get("created_at").getAsJsonObject().get("$date").getAsLong());
        messageDate.setText(formatter.format(calendar.getTime()));

        return rowViewComments;

    };

    public void setData(JsonArray data){
        this.data = data;
    }

    public JsonArray getData() {
        return data;
    }
}