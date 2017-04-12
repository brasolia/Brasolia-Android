package br.com.brasolia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import br.com.brasolia.application.BrasoliaApplication;
import br.com.brasolia.models.User;
import br.com.brasolia.util.LoadingView;
import br.com.brasolia.webserver.BrasoliaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private boolean showFirstSteps;
    private SharedPreferences preferences;
    private CallbackManager callbackManager;
    private BrasoliaAPI api;
    boolean fromComments = false;
    private LoadingView loadingView;

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("hue2", "handleSignInResult:" + i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_login_2);

        loadingView = new LoadingView(this);
        fromComments = getIntent().getBooleanExtra("fromComments", false);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        showFirstSteps = preferences.getBoolean("ShowFirstSteps", true);

        callbackManager = CallbackManager.Factory.create();

        LinearLayout fb = (LinearLayout) findViewById(R.id.face_login);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        api = ((BrasoliaApplication) this.getApplication()).getApi();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        /*
        ImageButton signInButton = (ImageButton) findViewById(R.id.sign_in_button);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        */

        TextView noLogin = (TextView) findViewById(R.id.no_login_text);
        noLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                sp.edit().remove("cookie").commit();
                SharedPreferences sp2 = getSharedPreferences("brasolia", MODE_PRIVATE);
                sp2.edit().clear().commit();
                User.setUser(LoginActivity.this, null);
                LoginManager.getInstance().logOut();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                finish();
            }
        });


        loginButton.setReadPermissions(Arrays.asList("email, public_profile"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");

                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {

                                        try {
                                            String id = object.getString("id");

                                            try {
                                                URL profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");

                                                User u = new User();
                                                Log.e("resposta", " :" + response.toString());

                                                String name = object.getString("name");
                                                String lname = object.getString("last_name");

                                                String email = null;

                                                try {
                                                    email = object.getString("email");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    email = id + "@brasolia.com.br";
                                                }


                                                if (name != null) u.setName(name);
                                                if (email != null) u.setEmail(email);
                                                u.setPhoto(profile_pic.toString());
                                                User.setUser(LoginActivity.this, u);


                                                serverRegister(email, id, name, name, true); // Login on server, if success open next activity, else show a toast message


                                            } catch (MalformedURLException e) {
                                                Log.e("FacebookLogin", "MalformedURLException");
                                                e.printStackTrace();
                                            }

                                        } catch (JSONException e) {
                                            Log.e("FacebookLogin", "JSONException");
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday, last_name");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Falha no login, tente novamente 1",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Falha no login, tente novamente 2",
                                Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    }
                });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            loadingView.show(null, "Carregando...");

            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

                    Person person = null;
                    try {
                        person = loadPeopleResult.getPersonBuffer().get(0);
                    } catch (Exception e) {

                    }

                    User u = new User();

                    if (person != null && acct != null) {

                        if (person.getDisplayName() != null) u.setName(person.getDisplayName());

                        if (person.getImage() != null) {
                            String userImage = person.getImage().getUrl();

                            userImage = userImage.substring(0, userImage.length() - 2);
                            userImage = userImage + "200";

                            u.setPhoto(userImage);
                        }

                        if (acct.getEmail() != null) u.setEmail(acct.getEmail().toString());

                        User.setUser(LoginActivity.this, u);

                        serverRegister(u.getEmail(), acct.getId(), person.getName().getGivenName(), person.getName().getFamilyName(), false);
                    } else {
                        loadingView.hide();
                        Toast.makeText(LoginActivity.this, "Erro no login do Google, tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
            loadingView.hide();
            Log.e("LoginGoogle", "Login não concluido com sucesso.");
            Log.d("LoginGoogle", result.getStatus().toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        } else if (requestCode == 2) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) handleSignInResult(result);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 2);
    }


    public void serverRegister(final String email, final String id, String fname, String lname, final boolean facebook) {

        Call<JsonObject> register;

        if (facebook) {
            register = api.register(email, id, fname, lname);
        } else {
            register = api.registerGoogle(email, id, fname, lname);
        }

        register.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    serverLogin(email, id, facebook);
                } else {
                    loadingView.hide();

                    User.setUser(LoginActivity.this, null);
                    LoginManager.getInstance().logOut();

                    Toast.makeText(LoginActivity.this, "Falha no login, tente novamente. 3", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loadingView.hide();

                User.setUser(LoginActivity.this, null);
                LoginManager.getInstance().logOut();

                Toast.makeText(LoginActivity.this, "Falha no login, tente novamente. 4", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void serverLogin(String email, String id, boolean facebook) {
        Call<JsonObject> login;

        if (facebook) {
            login = api.login(email, id, "on");
        } else {
            login = api.loginGoogle(email, id, "on");
        }

        login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    loadingView.hide();

                    // get header value
                    String cookie = response.headers().get("Set-Cookie");
                    Log.i("OnResponse", response.message());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cookie", cookie.split(";")[0]);
                    editor.apply();

                    // success on login, go to the next activity
//                    if (!fromComments) { // test to know if user is only login to comment or not
                        Intent i;
                        if (showFirstSteps)
                            i = new Intent(LoginActivity.this, FirstStepsActivity.class);
                        else
                            i = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(i);
//                    }
                    finish();


                } else {
                    loadingView.hide();

                    User.setUser(LoginActivity.this, null);
                    LoginManager.getInstance().logOut();

                    Toast.makeText(LoginActivity.this, "Falha no login, tente novamente 5",
                            Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loadingView.hide();

                User.setUser(LoginActivity.this, null);
                LoginManager.getInstance().logOut();

                Toast.makeText(LoginActivity.this, "Falha no login, tente novamente 6",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

    }

}
