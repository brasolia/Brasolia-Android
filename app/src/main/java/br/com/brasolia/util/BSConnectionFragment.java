package br.com.brasolia.util;

import android.support.v4.app.Fragment;

import java.util.List;

import br.com.brasolia.models.BSCategory;
import br.com.brasolia.models.BSComment;
import br.com.brasolia.models.BSDataUpdated;
import br.com.brasolia.models.BSItem;

/**
 * Created by cayke on 25/10/17.
 */

public class BSConnectionFragment extends Fragment implements BSDataUpdated {
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
