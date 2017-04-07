package br.com.brasolia.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.brasolia.R;

/**
 * Created by Eduardo on 22/08/2016.
 */
public class MenuAdapter extends BaseAdapter {
    int imagens[];
    Activity context;

    public MenuAdapter(int imagens[], Activity context){
        this.imagens = imagens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagens.length;
    }

    @Override
    public Object getItem(int position) {
        return imagens[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
        View view = inflater.inflate(R.layout.sliding_menu_item, null, true);

        // screen elements ------------------------------------------------
        ImageView btMenu = (ImageView) view.findViewById(R.id.imgMenu);
        // ----------------------------------------------------------------

        if(position == 5){ // Heart Image
            int dimensionInDp = (int) (30 * Resources.getSystem().getDisplayMetrics().density); // in dp
            int mLeft = (int) (35 * Resources.getSystem().getDisplayMetrics().density); // in dp
            int mRight = (int) (35 * Resources.getSystem().getDisplayMetrics().density); // in dp
            int mTop = (int) (60 * Resources.getSystem().getDisplayMetrics().density); // in dp
            int mBottom = (int) (15 * Resources.getSystem().getDisplayMetrics().density); // in dp
            btMenu.getLayoutParams().width = dimensionInDp; // Setting new image width
            btMenu.getLayoutParams().height = dimensionInDp;
            btMenu.requestLayout(); // updating the image layout

            ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(btMenu.getLayoutParams());
            marginParams.setMargins(mLeft, mTop, mRight, mBottom );
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
            btMenu.setLayoutParams(layoutParams);
        }

        btMenu.setImageResource(imagens[position]); // Set image resource for the ImageButton

        return view;
    }
}