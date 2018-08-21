package de.dhkarlsruhe.it.sheeshapp.sheeshapp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MainActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.login.Login;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by Informatik on 23.11.2017.
 */

public class LogInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private String email, password;
    private Login login;
    private CheckBox cbSaveLogin;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobileAds.initialize(this, MyUtilities.AD_APP_ID);
        adView = findViewById(R.id.adLogin);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        etEmail = findViewById(R.id.etLogEmail);
        etPassword = findViewById(R.id.etLogPassword);
        cbSaveLogin = findViewById(R.id.cbLogSaveLogin);

        login = new Login(this,adView);

        cbSaveLogin.setChecked(login.isSaved());

        checkAutomaticLogin();
    }

    private void checkAutomaticLogin() {
        if(cbSaveLogin.isChecked()) {
            email = login.getSavedEmail();
            password = login.getSavedPassword();
            if (!email.equals("empty") && !password.equals("empty")) {
                etEmail.setText(email);
                etPassword.setText(password);
            }
        }
    }

    public void login(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        login.startLogin(email,password,cbSaveLogin.isChecked());
    }

    public void cancelLogin(View view) {
        this.finish();
    }

}
