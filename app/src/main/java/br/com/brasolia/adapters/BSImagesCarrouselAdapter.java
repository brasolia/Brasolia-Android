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

/**
 * Created by cayke on 11/04/17.
 */

public class BSImagesCarrouselAdapter extends RecyclerView.Adapter<BSImagesCarrouselHolder> {
    private List<String> images;

    public BSImagesCarrouselAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public BSImagesCarrouselHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_carrousel, parent, false);
        return new BSImagesCarrouselHolder(view);
    }

    @Override
    public void onBindViewHolder(BSImagesCarrouselHolder holder, int position) {
        if (holder.context instanceof AppCompatActivity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((AppCompatActivity) holder.context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels/3;

            BSImageStorage.setEventImageNamed(images.get(position), holder.imageView, width, width, null);
        }
        else {
            BSImageStorage.setEventImageNamed(images.get(position), holder.imageView, 200, 200, null);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}

class BSImagesCarrouselHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    Context context;

    BSImagesCarrouselHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        imageView = (ImageView) itemView.findViewById(R.id.item_image_carrousel_imageview);
    }
}
