package br.com.brasolia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import br.com.brasolia.adapters.TicketsListAdapter;

public class TicketActivity extends AppCompatActivity {
    ImageView btClose;
    ListView listEvents;
    TicketsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);



        // screen elements --------------------------------------------------
        btClose = (ImageView) findViewById(R.id.imgCloseTicketsActivity);
        listEvents = (ListView) findViewById(R.id.listEventTickets);
        // ------------------------------------------------------------------

        // Hard Coded test ------------------------------------
        JsonArray array = new JsonArray();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "Tomorrow Land Brasilia");
        obj.addProperty("description", "Espalhe seu amor pelo mundo");
        obj.addProperty("date", "Sexta-feira - 23:00");
        array.add(obj);

        adapter = new TicketsListAdapter(TicketActivity.this, array);
        listEvents.setAdapter(adapter);
        // ----------------------------------------------------

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(TicketActivity.this, SummaryTicketActivity.class);
                startActivity(it);
            }
        });


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
