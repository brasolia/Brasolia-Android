package br.com.brasolia.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.brasolia.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eduardo on 08/09/2016.
 */
public class FriendsEventLikesAdapter extends RecyclerView.Adapter<FriendsEventLikesAdapter.Holder>{
    Activity context;
    ArrayList<String> friends;

    public FriendsEventLikesAdapter(Activity context, ArrayList<String> friends){
        this.context = context;
        this.friends = friends;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_friends_like_event_list, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.imgFriend.setImageResource(R.drawable.profile);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CircleImageView imgFriend;

        public Holder(View itemView) {
            super(itemView);

            imgFriend = (CircleImageView) itemView.findViewById(R.id.friend_photo);

        }
    }
}
