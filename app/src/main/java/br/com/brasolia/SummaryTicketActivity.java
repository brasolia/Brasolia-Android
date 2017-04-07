package br.com.brasolia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SummaryTicketActivity extends AppCompatActivity {
    ListView listSummary;
    CircleImageView btClose;

    // HARD CODED TEST -----------------------------------------


    // ---------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_ticket);

        // SCREEN ELEMENTS ---------------------------------------
        btClose = (CircleImageView) findViewById(R.id.imgCloseSummaryTicketActivity);
        // -------------------------------------------------------


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
