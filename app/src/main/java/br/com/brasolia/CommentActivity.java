package br.com.brasolia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import br.com.brasolia.adapters.CommentsListAdapter;
import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.User;
import br.com.brasolia.webserver.BrasoliaAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    String eventId;
    ListView list;
    int pageCount = 1;
    boolean mutex = false;
    BrasoliaAPI api;
    JsonArray messages = new JsonArray();
    CommentsListAdapter adapter;
    TextView tvStatus, tvStatusLogin;
    LinearLayout commentField, statusField, layoutCommentList;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        list =(ListView) findViewById(R.id.listMessage);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        commentField = (LinearLayout) findViewById(R.id.fieldTypeComment);
        tvStatusLogin = (TextView) findViewById(R.id.tvStatusLoginComments);
        statusField = (LinearLayout) findViewById(R.id.fieldStatusLogin);
        layoutCommentList = (LinearLayout) findViewById(R.id.llCommentList);
        // -----------------------------------------------------------------------------------------
        tvStatus.setVisibility(View.GONE);

        user = User.getUser(CommentActivity.this);

        statusField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null){
                    Intent it = new Intent(CommentActivity.this, LoginActivity.class);
                    it.putExtra("fromComments", true);
                    startActivity(it);
                }
            }
        });

        eventId = getIntent().getStringExtra("eventId");

        api = ((BrasoliaApplication) CommentActivity.this.getApplication()).getApi();

        Call<JsonObject> getComments = api.getComments(eventId, String.valueOf(pageCount), "15");

        getComments.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    messages.addAll(response.body().getAsJsonObject().get("data").getAsJsonArray());

                    if(messages.size() == 0) tvStatus.setVisibility(View.VISIBLE);

                    if(messages != null){
                        adapter = new CommentsListAdapter( CommentActivity.this ,messages);
                        list.setAdapter(adapter);
                    }

                    mutex = true;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });



        list.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView view, int scrollState) {
                 if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                         && (list.getLastVisiblePosition() - list.getHeaderViewsCount() -
                         list.getFooterViewsCount()) >= (adapter.getCount() - 1) && mutex) { // list end

                     pageCount++;
                     mutex = false;

                     Call<JsonObject> getComments = api.getComments(eventId, String.valueOf(pageCount), "15");

                     getComments.enqueue(new Callback<JsonObject>() {
                         @Override
                         public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                             if(response.isSuccessful()){
                                 messages.addAll(response.body().getAsJsonObject().get("data").getAsJsonArray());
                                 adapter.notifyDataSetChanged();
                                 mutex = true;
                             }
                         }

                         @Override
                         public void onFailure(Call<JsonObject> call, Throwable t) {
                             t.printStackTrace();
                         }
                     });
                 }
             }

             @Override
             public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

             }
        });

        CircleImageView closeSuggestions = (CircleImageView) findViewById(R.id.closeSuggestion);
        Button sendComment = (Button) findViewById(R.id.sendComment);


        closeSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText userComment = (EditText) findViewById(R.id.userComment);

                if(userComment.getText().toString().trim().length() < 1){
                    new AlertDialog.Builder(CommentActivity.this)
                            .setTitle("Ops...")
                            .setMessage("Escreva um comentário para prosseguir.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){

                                }
                            }).create().show();

                    return;
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CommentActivity.this);
                Call<JsonObject> makeComment = api.makeComment(preferences.getString("cookie", ""), eventId, userComment.getText().toString());

                makeComment.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){

                            List<JsonObject> jsons = new ArrayList<JsonObject>();
                            for (int i = 0; i < messages.size(); i++) {
                                jsons.add(messages.get(i).getAsJsonObject());
                            }

                            jsons.add(0, response.body().getAsJsonObject().get("data").getAsJsonObject());

                            Gson gson = new GsonBuilder().create();
                            messages = gson.toJsonTree(jsons).getAsJsonArray();

                            adapter.setData(messages);

                            adapter.notifyDataSetChanged();

                            tvStatus.setVisibility(View.GONE);


                            userComment.setText("");
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });


    }

    public void onResume(){
        super.onResume();

        user = User.getUser(CommentActivity.this);
        if(user == null){

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (50*scale + 0.5f);

            layoutCommentList.setPadding(0,0,0,dpAsPixels);
            commentField.setVisibility(View.GONE);
            tvStatusLogin.setText("Para comentar é necessário estar logado. Toque para realizar login.");

        }else{

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (110*scale + 0.5f);

            layoutCommentList.setPadding(0,0,0,dpAsPixels);
            commentField.setVisibility(View.VISIBLE);
            tvStatusLogin.setText("Deixe seu comentário abaixo");

        }

    }
}
