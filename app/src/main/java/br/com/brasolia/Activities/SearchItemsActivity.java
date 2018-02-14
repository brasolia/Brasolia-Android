package br.com.brasolia.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.brasolia.R;
import br.com.brasolia.adapters.BSSearchAdapter;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.models.BSItem;
import br.com.brasolia.models.BSVenue;
import br.com.brasolia.util.BSFirebaseListenerRef;
import br.com.brasolia.util.ItemClickSupport;

public class SearchItemsActivity extends Activity {

    private RecyclerView recyclerView;
    private Button btSearch;
    private EditText etSearch;
    private TextView tvStatus, tvClose, searchTipTextView;

    List<BSItem> items;
    BSFirebaseListenerRef mRef;

    @Override
    protected void onDestroy() {
        if (mRef != null)
            mRef.detach();

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_search_events);

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.activity_search_recycler);
        btSearch = (Button) findViewById(R.id.searchBtn);
        etSearch = (EditText) findViewById(R.id.etSearchCategory);
        tvStatus = (TextView) findViewById(R.id.tvStatusSearchEvents);
        tvClose = (TextView) findViewById(R.id.tvCloseSearchEvents);
        searchTipTextView = (TextView) findViewById(R.id.searchTipTextView);
        // -----------------------------------------------------------------------------------------

        etSearch.requestFocus();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    //to hide it, call the method again
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    etSearch.clearFocus();
                    return true;
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    searchEvents(s.toString());

                    searchTipTextView.setVisibility(View.GONE);
                }
                else {
                    searchTipTextView.setVisibility(View.VISIBLE);
                    tvStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvStatus.setVisibility(View.GONE);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                return false;
            }
        });
    }

    public void searchEvents(final String search) {
        if (mRef != null)
            mRef.detach();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        Query query = ref.orderByChild("name").startAt(search).endAt(search+"\uf8ff");

        final SearchItemsActivity self = this;

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
                            else if (dict.get("type").equals("event"))
                                items.add(new BSEvent(id, dict));
                        }
                        catch (Exception e) {
                            Log.d("BSRequestService", e.toString());
                        }
                    }

                    self.items = items;
                    self.mountRecycler();
                }
                else {
                    self.items = null;
                    self.mountRecycler();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                self.items = null;
                self.mountRecycler();
            }
        };

        query.addValueEventListener(listener);

        mRef = new BSFirebaseListenerRef(query, listener);
    }

    private void mountRecycler() {
        if (items != null && items.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BSSearchAdapter(items));

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent i = new Intent(SearchItemsActivity.this, BSItemActivity.class);
                    i.putExtra("item", items.get(position));
                    startActivity(i);
                }
            });
        }
        else {
            recyclerView.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
        }
    }
}