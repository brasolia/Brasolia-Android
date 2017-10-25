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
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.R;
import br.com.brasolia.adapters.BSSearchAdapter;
import br.com.brasolia.models.BSEvent;
import br.com.brasolia.util.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchEventsActivity extends Activity {

    private RecyclerView recyclerView;
    private Button btSearch;
    private EditText etSearch;
    private TextView tvStatus, tvClose, searchTipTextView;
    private Call<JsonObject> call;
    private List<BSEvent> events;

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

    public void searchEvents(String search) {
        if (call != null)
            call.cancel(); //cancelar se tivesse uma chamada antiga

        BSRequests requests = BSConnection.createService(BSRequests.class);
        call = requests.searchEvent(search);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) bsResponse.getData();

                        events = new ArrayList<BSEvent>();
                        for (Map<String, Object> dictionary : data) {
                            events.add(new BSEvent(dictionary));
                        }

                        mountRecycler();
                    }
                    else {
                        Log.d("search", "server error");
                        events = null;
                        mountRecycler();
                    }
                }
                else {
                    Log.d("search", "conection failure");
                    events = null;
                    mountRecycler();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("search", "conection failure");
                events = null;
                mountRecycler();
            }
        });
    }

    private void mountRecycler() {
        if (events != null && events.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BSSearchAdapter(events));

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent i = new Intent(SearchEventsActivity.this, BSEventActivity.class);
                    i.putExtra("event", events.get(position));
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
