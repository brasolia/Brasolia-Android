package br.com.brasolia.util;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by cayke on 25/10/17.
 */

public class BSFirebaseListenerRef {

    private ValueEventListener listener;
    private Query query;

    public BSFirebaseListenerRef(Query query, ValueEventListener listener) {
        this.query = query;
        this.listener = listener;
    }

    public void detach() {
        if (query != null && listener != null)
            query.removeEventListener(listener);
    }
}
