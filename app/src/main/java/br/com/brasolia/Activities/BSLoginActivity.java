package br.com.brasolia.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import br.com.brasolia.R;

/**
 * Created by cayke on 12/04/17.
 */

public class BSLoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog loading;
    private boolean alreadyCameFromApp;
    private ImageView login_background;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics.getInstance(this);
        FacebookSdk.sdkInitialize(this);
        mAuth = FirebaseAuth.getInstance();

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
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                showLoginError();
            }

            @Override
            public void onError(FacebookException exception) {
                showLoginError();
            }
        });

        //endregion

        if (alreadyCameFromApp)
            btNoLogin.setVisibility(View.INVISIBLE);

        btNoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BSLoginActivity.this, AppActivity.class));
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

    private void goToMainApp() {
        if (alreadyCameFromApp)
            finish();
        else {
            Intent i = new Intent(this, AppActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainApp();
                        } else {
                            Toast.makeText(BSLoginActivity.this, "Autenticação falhou. Erro:" + task.getException(),
                                    Toast.LENGTH_LONG).show();

                            LoginManager.getInstance().logOut();

                            if (loading != null)
                                loading.dismiss();
                        }
                    }
                });
    }
}