package br.com.brasolia.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.util.List;

import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;
import br.com.brasolia.models.BSCategory;

/**
 * Created by cayke on 11/04/17.
 */

public class BSCategoriesAdapter extends RecyclerView.Adapter{
    private final static int VIEW_TYPE_RECTANGULAR = 0;
    private final static int VIEW_TYPE_SQUARE = 1;

    private List<BSCategory> categories;

    public BSCategoriesAdapter(List<BSCategory> categories) {
        this.categories = categories;
    }

    public void setLayoutManager(GridLayoutManager layoutManager, final int spanCount) {
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == 1)
                    return spanCount;
                else
                    return spanCount/2;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 1)
            return VIEW_TYPE_RECTANGULAR;
        else
            return VIEW_TYPE_SQUARE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RECTANGULAR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_rectangular, parent, false);
            return new BSCategoryRectangularViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_square, parent, false);
            return new BSCategorySquareViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BSCategoryRectangularViewHolder) {
            BSCategoryRectangularViewHolder viewHolder = (BSCategoryRectangularViewHolder) holder;
            viewHolder.bindCategory(categories.get(position));
        }
        else {
            BSCategorySquareViewHolder viewHolder = (BSCategorySquareViewHolder) holder;
            viewHolder.bindCategory(categories.get(position));
        }
    }

    private class BSCategorySquareViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        private Context context;

        BSCategorySquareViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.item_category_imageview);
            textView = (TextView) itemView.findViewById(R.id.item_category_textview);
            textView.setText("");

            context = itemView.getContext();
        }

        void bindCategory(final BSCategory category) {
            final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/josefinsans_semibold.ttf");

            if (context instanceof AppCompatActivity) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels/2;

                BSImageStorage.setCategoryImageNamed(category.getImage(), imageView, width, width, new Callback() {
                    @Override
                    public void onSuccess() {
                        textView.setTypeface(type);
                        textView.setText(category.getName());
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            else {
                BSImageStorage.setCategoryImageNamed(category.getImage(), imageView, 300, 300, new Callback() {
                    @Override
                    public void onSuccess() {
                        textView.setTypeface(type);
                        textView.setText(category.getName());
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }
    }

    private class BSCategoryRectangularViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        private Context context;

        BSCategoryRectangularViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.item_category_imageview);
            textView = (TextView) itemView.findViewById(R.id.item_category_textview);
            textView.setText("");

            context = itemView.getContext();
        }

        void bindCategory(final BSCategory category) {
            final Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/josefinsans_semibold.ttf");

            if (context instanceof AppCompatActivity) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels;
                int height = (int) (width*0.49);

                BSImageStorage.setCategoryImageNamed(category.getImage(), imageView, width, height, new Callback() {
                    @Override
                    public void onSuccess() {
                        textView.setTypeface(type);
                        textView.setText(category.getName());
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            else {
                BSImageStorage.setCategoryImageNamed(category.getImage(), imageView, 600, 300, new Callback() {
                    @Override
                    public void onSuccess() {
                        textView.setTypeface(type);
                        textView.setText(category.getName());
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }
    }
}