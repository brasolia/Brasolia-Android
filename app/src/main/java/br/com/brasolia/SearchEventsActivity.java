package br.com.brasolia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.brasolia.adapters.ItemEventSearchAdapter;

public class SearchEventsActivity extends Activity {

    private ListView listEvents;
    private Button btSearch;
    private EditText etSearch;
    private JsonArray searchEvents;
    private ItemEventSearchAdapter adapter;
    private TextView tvStatus, tvClose, searchTipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_events);

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        listEvents = (ListView) findViewById(R.id.listEventsSearch);
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
                searchEvents = searchEvents(s.toString());

                adapter = new ItemEventSearchAdapter(SearchEventsActivity.this, SearchEventsActivity.this, searchEvents);
                listEvents.setAdapter(adapter);

                if (s.length() != 0) {

                    if (searchEvents.size() != 0) {
                        tvStatus.setVisibility(View.GONE);
                        listEvents.setVisibility(View.VISIBLE);
                    } else {
                        tvStatus.setVisibility(View.VISIBLE);
                    }

                } else {

                    if (searchEvents.size() != 0) {
                        listEvents.setVisibility(View.VISIBLE);
                        tvStatus.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JsonObject event = searchEvents.get(position).getAsJsonObject();

                Intent i = new Intent(SearchEventsActivity.this, BSEventActivity.class);
                i.putExtra("event", event.toString());
                startActivity(i);
            }
        });

        searchTipTextView.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.GONE);
    }

    public JsonArray searchEvents(String word) {

        //todo implementar
        return null;
    }
}
