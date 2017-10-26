package br.com.brasolia.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.brasolia.models.BSCategory;
import br.com.brasolia.models.BSComment;
import br.com.brasolia.models.BSDataUpdated;
import br.com.brasolia.models.BSItem;

/**
 * Created by cayke on 26/10/17.
 */

public class BSRecyclerViewHolderDataObserver extends RecyclerView.ViewHolder implements BSDataUpdated {
    public WeakReference<BSFirebaseListenerRef> mRef;



    public BSRecyclerViewHolderDataObserver(View itemView) {
        super(itemView);
    }

    @Override
    public void categoriesUpdated(boolean success, List<BSCategory> categories) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void itemsUpdated(boolean success, List<BSItem> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void commentsUpdated(boolean success, List<BSComment> comments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void itemLikeUpdated(boolean success, boolean liked) {
        throw new UnsupportedOperationException();
    }
}
