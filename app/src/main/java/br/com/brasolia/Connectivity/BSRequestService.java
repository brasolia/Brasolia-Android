package br.com.brasolia.Connectivity;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.brasolia.models.BSCategory;
import br.com.brasolia.models.BSDataUpdated;
import br.com.brasolia.models.BSItem;
import br.com.brasolia.models.BSVenue;
import br.com.brasolia.util.BSFirebaseListenerRef;

/**
 * Created by cayke on 24/10/17.
 */

public class BSRequestService {

    public static BSFirebaseListenerRef getCategories(final BSDataUpdated interestedObject) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    List<BSCategory> categories = new ArrayList<BSCategory>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        try {
                            BSCategory category = child.getValue(BSCategory.class);
                            category.setId(child.getKey());
                            categories.add(category);
                        }
                        catch (Exception e) {
                            Log.d("BSRequestService", e.toString());
                        }
                    }

                    if (interestedObject != null)
                        interestedObject.categoriesUpdated(true, categories);
                }
                else {
                    if (interestedObject != null)
                        interestedObject.categoriesUpdated(true, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (interestedObject != null)
                    interestedObject.categoriesUpdated(false, null);
            }
        };

        ref.addValueEventListener(listener);

        return new BSFirebaseListenerRef(ref, listener);
    }

    public static BSFirebaseListenerRef getItemsFromCategory(BSCategory category, final BSDataUpdated interestedObject) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        final Query query = ref.orderByChild("categories/" + category.getId()).equalTo(true);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    List<BSItem> items = new ArrayList<BSItem>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        try {
                            Map dict = (Map) child.getValue();
                            String id = child.getKey();
                            if (dict.get("type").equals("venue"))
                                items.add(new BSVenue(id, dict));
                            //todo
//                        else
//                            items.add(child.getValue(BSEvent.class));
                        }
                        catch (Exception e) {
                            Log.d("BSRequestService", e.toString());
                        }
                    }

                    if (interestedObject != null)
                        interestedObject.itemsUpdated(true, items);
                }
                else {
                    if (interestedObject != null)
                        interestedObject.itemsUpdated(true, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (interestedObject != null)
                    interestedObject.itemsUpdated(false, null);
            }
        };

        query.addValueEventListener(listener);

        return new BSFirebaseListenerRef(query, listener);
    }

    public static BSFirebaseListenerRef searchForItems(String search, final BSDataUpdated interestedObject) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        final Query query = ref.orderByChild("name").equalTo(search);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    List<BSItem> items = new ArrayList<BSItem>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        try {
                            Map dict = (Map) child.getValue();
                            String id = child.getKey();
                            if (dict.get("type").equals("venue"))
                                items.add(new BSVenue(id, dict));
                            //todo
//                        else
//                            items.add(child.getValue(BSEvent.class));
                        }
                        catch (Exception e) {
                            Log.d("BSRequestService", e.toString());
                        }
                    }

                    if (interestedObject != null)
                        interestedObject.itemsUpdated(true, items);
                }
                else {
                    if (interestedObject != null)
                        interestedObject.itemsUpdated(true, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (interestedObject != null)
                    interestedObject.itemsUpdated(false, null);
            }
        };

        query.addValueEventListener(listener);

        return new BSFirebaseListenerRef(query, listener);
    }
}