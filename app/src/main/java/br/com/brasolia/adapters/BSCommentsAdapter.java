package br.com.brasolia.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.brasolia.R;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.BSComment;
import br.com.brasolia.util.DateUtil;

/**
 * Created by cayke on 12/04/17.
 */

public class BSCommentsAdapter extends RecyclerView.Adapter<BSCommentsViewHolder> {

    List<BSComment> comments;
    boolean shouldShowMore;

    public BSCommentsAdapter(List<BSComment> comments, boolean shouldShowMore) {
        this.comments = comments;
        this.shouldShowMore = shouldShowMore;
    }

    @Override
    public BSCommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new BSCommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BSCommentsViewHolder holder, int position) {
        holder.bindComment(comments.get(position));
    }

    @Override
    public int getItemCount() {
        if (comments.size() > 5) {
            if (shouldShowMore)
                return comments.size();
            else
                return 5;
        } else
            return comments.size();

    }
}

class BSCommentsViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView name, date, tvComment;

    public BSCommentsViewHolder(View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.item_comment_user_image);
        name = (TextView) itemView.findViewById(R.id.item_comment_name);
        date = (TextView) itemView.findViewById(R.id.item_comment_date);
        tvComment = (TextView) itemView.findViewById(R.id.item_comment_text);
    }

    public void bindComment(BSComment comment) {
        name.setText(comment.getOwner().getName());

        date.setText(DateUtil.getTimeAgo(comment.getCreateAt().getTime()));

        tvComment.setText(comment.getMessage());

        if (!comment.getOwner().getImageKey().equals("")) {
            Picasso picasso = Picasso.with(BrasoliaApplication.getAppContext());
            picasso.setIndicatorsEnabled(false);
            picasso.load(comment.getOwner().getImageKey()).resize(100, 100).into(imageView);
        }
    }

}