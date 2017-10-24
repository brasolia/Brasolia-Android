package br.com.brasolia.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cayke on 24/10/17.
 */

public class BSRequestService {

    public static void getCategories(final BSDataUpdated interestedObject) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    List<BSCategory> categories = new ArrayList<BSCategory>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        categories.add(child.getValue(BSCategory.class));
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
        });
    }
}