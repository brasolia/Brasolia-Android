package br.com.brasolia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Arrays;

import br.com.brasolia.Connectivity.BSConnection;
import br.com.brasolia.Connectivity.BSRequests;
import br.com.brasolia.Connectivity.BSResponse;
import br.com.brasolia.models.BSUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cayke on 12/04/17.
 */

public class BSLoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog loading;
    private boolean alreadyCameFromApp;
    private ImageView login_background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_login);

        alreadyCameFromApp = getIntent().getBooleanExtra("cameFromApp", false);

        login_background = (ImageView) findViewById(R.id.login_background);
        TextView btNoLogin = (TextView) findViewById(R.id.no_login_text);
        LinearLayout btFacebook = (LinearLayout) findViewById(R.id.face_login);
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);

        //region fbbutton methods
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        Picasso.with(this).load(R.drawable.login_background).resize(781,1390).into(login_background);

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                BSUser user = new BSUser();
                                String fbID = null;
                                try {
                                    fbID = object.getString("id");
                                    user.setEmail(object.getString("email"));
                                    user.setfName(object.getString("first_name"));
                                    user.setlName(object.getString("last_name"));
                                    user.setGender(object.getString("gender"));
                                    user.setImageKey(object.getJSONObject("picture").getJSONObject("data").getString("url"));

                                    logUser(user, fbID);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showLoginError();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name,last_name,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                loading.dismiss();
            }

            @Override
            public void onError(FacebookException exception) {
                loading.dismiss();
            }
        });

        //endregion

        if (alreadyCameFromApp)
            btNoLogin.setVisibility(View.INVISIBLE);

        btNoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BSLoginActivity.this, MainActivity.class));

                finish();
            }
        });

        btFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Carregando...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void showLoginError() {
        loading.dismiss();
        Toast.makeText(this, "Erro ao fazer login, tente novamente mais tarde.", Toast.LENGTH_LONG).show();
    }

    private void showLoginInternetFailure() {
        loading.dismiss();
        Toast.makeText(this, "Falha de conexao. Conecte-se a internet e  tente novamente.", Toast.LENGTH_LONG).show();
    }

    private void logUser(final BSUser user, final String fbID) {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.logUser(user.getEmail(), fbID, null, "on");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                         user.saveUser();

                        //todo enviar imagem do user

                        goToMainApp();
                    }
                    else {
                        registerUser(user, fbID);
                    }
                }
                else {
                    Log.d("logUser", "conection failure");
                    showLoginInternetFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("logUser", "conection failure");
                showLoginInternetFailure();
            }
        });
    }

    private void registerUser(final BSUser user, String fbID) {
        BSRequests requests = BSConnection.createService(BSRequests.class);
        Call<JsonObject> call = requests.register(user.getEmail(), fbID, null, user.getfName(), user.getlName());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    BSResponse bsResponse = new BSResponse(response.body());
                    if (bsResponse.getStatus() == BSResponse.ResponseStatus.BSResponseSuccess) {
                        user.saveUser();

                        //todo enviar imagem do user

                        goToMainApp();
                    }
                    else {
                        showLoginError();
                        Log.d("registerUser", "server error");
                    }
                }
                else {
                    showLoginInternetFailure();
                    Log.d("registerUser", "conection failure");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showLoginInternetFailure();
                Log.d("registerUser", "conection failure");
            }
        });
    }

    private void goToMainApp() {
        if (alreadyCameFromApp)
            finish();
        else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}