package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.Login;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.Session;

/**
 * Created by Informatik on 23.11.2017.
 */

public class LogInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private String email, password;
    private SharedPreferences pref;
    private ProgressDialog dialog;
   // private Animation anim;
   // private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // anim = AnimationUtils.loadAnimation(this,R.anim.anim_move_from_left);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        etEmail = findViewById(R.id.etLogEmail);
        etPassword = findViewById(R.id.etLogPassword);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        /*
        boolean savedSettings = pref.getBoolean("saveLogin",false);
        sharedPassword = pref.getString("savedPassword","#####");
        sharedUsername = pref.getString("savedUsername","nouser");

            if (savedSettings && !sharedUsername.equals("#####") && !sharedUsername.equals("nouser")) {
                etEmail.setText(sharedUsername);
                etPassword.setText(sharedPassword);
            }*/

    }

    public void login(View view) {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if (checkInput(email, password)) {
            handleLogin();
        } else {
            Toast.makeText(this,"Check input", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkInput(String email, String password) {
        if (email.equals(""))
            return false;
        if (password.equals(""))
            return false;
        return true;
    }

    private void handleLogin() {
        Login login = new Login(email, password,this);
        StringRequest request =  new StringRequest(login.getUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                handleResponse(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleResponse(volleyError.getMessage());
            }
        });
        login.startLogin(request);

    }

    private void handleResponse(String string) {
        Toast.makeText(this, string,Toast.LENGTH_LONG).show();
        dialog.dismiss();
        if (string.equals("OK")) {
            this.finish();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, string, Toast.LENGTH_LONG).show();
        }
    }

}
