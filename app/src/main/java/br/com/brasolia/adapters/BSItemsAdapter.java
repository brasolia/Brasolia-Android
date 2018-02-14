package br.com.brasolia.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.Connectivity.BSRequestService;
import br.com.brasolia.R;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.models.BSItem;
import br.com.brasolia.util.BSAlertUtil;
import br.com.brasolia.util.BSFirebaseListenerRef;
import br.com.brasolia.util.BSRecyclerViewHolderDataObserver;

/**
 * Created by cayke on 11/04/17.
 */

public class BSItemsAdapter extends RecyclerView.Adapter<BSEventViewHolder> {

    private List<BSItem> items;
    private int choice;

    public BSItemsAdapter(List<BSItem> items, int choice) {
        this.items = items;

        this.choice = choice;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public BSEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new BSEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BSEventViewHolder holder, int position) {
        holder.setItem(items.get(position));
        holder.bindItem(items.get(position), choice);
    }
}

class BSEventViewHolder extends BSRecyclerViewHolderDataObserver {

    private ImageView heart_icon, date_icon;
    private ImageView cover;
    private TextView title, place, date, price, distance;
    private Context context;
    private LinearLayout heart_icon_frameLayout;
    private boolean liked;
    private BSItem item;

    public BSEventViewHolder(final View itemView) {
        super(itemView);

        context = itemView.getContext();
        heart_icon_frameLayout = (LinearLayout) itemView.findViewById(R.id.frameLayout_item_event_heart_icon);
        heart_icon = (ImageView) itemView.findViewById(R.id.item_event_heart_icon);
        distance = (TextView) itemView.findViewById(R.id.item_event_distance);
        price = (TextView) itemView.findViewById(R.id.item_event_price);
        title = (TextView) itemView.findViewById(R.id.item_event_title);
        title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/BebasNeue_bold.ttf"));
        place = (TextView) itemView.findViewById(R.id.item_event_place);
        place.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/josefinsans_semibold.ttf"));
        date = (TextView) itemView.findViewById(R.id.item_event_date);
        date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/BebasNeue_regular.ttf"));
        date_icon = (ImageView) itemView.findViewById(R.id.item_event_date_icon);

        cover = (ImageView) itemView.findViewById(R.id.item_event_cover);

        heart_icon_frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    BSAlertUtil.toLikeUserShouldLogin();
                }
                else {
                    liked = !liked;
                    if (liked)
                        heart_icon.setImageResource(R.drawable.ic_love_filled);
                    else
                        heart_icon.setImageResource(R.drawable.ic_love);

                    try {
                        BSRequestService.likeItem(liked, item, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                    catch (Exception e) {
                        Log.d("BSItemsAdapter", e.toString());
                    }
                }
            }
        });
    }

    public void setItem(BSItem item){
        this.item = item;
    }

    public BSItem getItem(){
        return item;
    }

    public void bindItem(BSItem item, int choice) {
        if (mRef!= null && mRef.get() != null)
            mRef.get().detach();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mRef = new WeakReference<BSFirebaseListenerRef>(BSRequestService.isItemLikedByUser(item, user.getUid(), this));
        }

        if (context instanceof AppCompatActivity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = (int) (width * 0.67);

            BSImageStorage.setImageWithPathToImageViewDownloadingIfNecessary(item.getThumb(), cover,0, width, height, null);
        }

        switch (choice) {
            case 1:
                price.setVisibility(View.GONE);
                distance.setVisibility(View.GONE);
                break;
            case 2:
                price.setVisibility(View.GONE);
                if (item.getDistance() > 100)
                    distance.setVisibility(View.GONE);
                else
                    distance.setText(String.format(Locale.getDefault(), "%.2fkm", item.getDistance()));
                break;
            case 3:
                distance.setVisibility(View.GONE);
                break;
        }
        
        title.setText(item.getName());
        place.setText(item.getAddress());

        if (item instanceof BSEvent) {
            BSEvent event = (BSEvent) item;
            date.setVisibility(View.VISIBLE);
            date.setText(event.getCustomDate());
            date_icon.setVisibility(View.VISIBLE);
        }
        else {
            date_icon.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
        }

    }

    @Override
    public void itemLikeUpdated(boolean success, boolean liked) {
        item.setLiked(liked);

        if (liked)
            heart_icon.setImageResource(R.drawable.ic_love_filled);
        else
            heart_icon.setImageResource(R.drawable.ic_love);
    }
}