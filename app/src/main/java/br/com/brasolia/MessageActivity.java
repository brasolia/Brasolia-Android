package br.com.brasolia;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    CircleImageView btcloseSuggestions;
    EditText etComment;
    Button btsuggestionSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // screen elements ------------------------------------------------------
        btcloseSuggestions = (CircleImageView) findViewById(R.id.closeSuggestion);
        etComment = (EditText) findViewById(R.id.edit_texto);
        btsuggestionSend = (Button) findViewById(R.id.sendSuggestion);
        final View line = (View) findViewById(R.id.lineSuggestion);
        // ----------------------------------------------------------------------

        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });


        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    btsuggestionSend.callOnClick();
                    handled = true;
                }
                return handled;
            }
        });

        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    etComment.setHint("");
                    line.setBackgroundColor(ContextCompat.getColor(MessageActivity.this, R.color.defaultAccent));
                } else {
                    etComment.setHint("Escreva o que gostaria de nos dizer...");
                    line.setBackgroundColor(ContextCompat.getColor(MessageActivity.this, R.color.black));
                }
            }
        });

        btsuggestionSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etComment.getText().toString();

                if(message.trim().isEmpty())
                    Toast.makeText(MessageActivity.this, "Escreva algo", Toast.LENGTH_LONG).show();

                else{
                    BSRequests requests = BSConnection.createService(BSRequests.class);
                    Call<JsonObject> call = requests.sendSugestion(message);

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                BSResponse bsResponse = new BSResponse(response.body());
                                if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {

                                    Toast.makeText(MessageActivity.this, "Obrigado pela participação", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(MessageActivity.this, "Falha ao enviar sugestão, tente novamente.", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(MessageActivity.this, "Falha ao enviar sugestão, tente novamente.", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(MessageActivity.this, "Falha ao enviar sugestão, tente novamente.", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
        });

        btcloseSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
