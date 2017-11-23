package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Informatik on 23.11.2017.
 */

public class LogInActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private String username, password, sharedUsername, sharedPassword;
    private SharedPreferences pref;
   // private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        etUsername = findViewById(R.id.etLogUsername);
        etPassword = findViewById(R.id.etLogPassword);
        btnLogin = findViewById(R.id.btnLogLogin);

        pref = getSharedPreferences("com.preferences.sheeshapp", Context.MODE_PRIVATE);
        //editor = pref.edit();

        sharedPassword = pref.getString("savedPassword","#####");
        sharedUsername = pref.getString("savedUsername","nouser");

        if (!sharedUsername.equals("#####") && !sharedUsername.equals("nouser")) {
            //etUsername.setText(sharedUsername);
            //etPassword.setText(sharedPassword);
        }
    }

    public void login(View view) {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.equals(sharedUsername) && password.equals(sharedPassword)) {
            Toast.makeText(this,"Login successful", Toast.LENGTH_LONG).show();
            this.finish();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Wrong login", Toast.LENGTH_LONG).show();

        }

    }
}
