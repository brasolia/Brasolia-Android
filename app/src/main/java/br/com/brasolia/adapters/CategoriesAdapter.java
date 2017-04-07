package br.com.brasolia.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import br.com.brasolia.MainActivity;
import br.com.brasolia.R;
import br.com.brasolia.models.Category;
import br.com.brasolia.models.ItemAdapterCategory;

/**
 * Created by Eduardo on 24/08/2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Holder> {
    LayoutInflater inflater;
    private List<ItemAdapterCategory> itens;
    Activity context;
    int auxPosition;


    public CategoriesAdapter(Activity context, List<ItemAdapterCategory> itens) {
        this.context = context;
        this.itens = itens;


    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_categories, parent, false);
        Holder holder = new Holder(view);


        return holder;
    }


    public void onBindViewHolder(Holder holder, final int position) {
        ItemAdapterCategory item = itens.get(position);
        if (item != null) {
            holder.tvNameLeft.setText(item.getCategoryLeft().getName());
//            holder.imgLeft.setImageResource(item.getCategoryLeft().getImage());

            String imageUrl = item.getCategoryLeft().getImage();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, holder.imgLeft);

            holder.leftLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).loadEventsCategory(2 * position);
                }
            });


            if (item.getCategoryRight() != null) {
                holder.tvNameRight.setText(item.getCategoryRight().getName());

                String imageUrlRight = item.getCategoryRight().getImage();
                imageLoader.displayImage(imageUrlRight, holder.imgRight);

                holder.rightLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auxPosition = position;

                        if (auxPosition > 0) auxPosition++;

                        ((MainActivity) context).loadEventsCategory((2 * position) + 1);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView tvNameLeft, tvNameRight;
        RoundedImageView imgLeft, imgRight;
        LinearLayout leftLayout, rightLayout;

        public Holder(View itemView) {
            super(itemView);

            leftLayout = (LinearLayout) itemView.findViewById(R.id.layoutLeftItemCategory);
            rightLayout = (LinearLayout) itemView.findViewById(R.id.layoutRightItemCategory);
            tvNameLeft = (TextView) itemView.findViewById(R.id.tvLeftItemCategory);
            tvNameRight = (TextView) itemView.findViewById(R.id.tvRightItemCategory);
            imgLeft = (RoundedImageView) itemView.findViewById(R.id.imgLeftItemCategory);
            imgRight = (RoundedImageView) itemView.findViewById(R.id.imgRightItemCategory);

        }
    }

    public List<ItemAdapterCategory> getItens() {
        return itens;
    }

    public void setItens(List<ItemAdapterCategory> itens) {
        this.itens = itens;
    }
}

