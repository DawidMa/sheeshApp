package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Informatik on 20.11.2017.
 */

public class SignUpActivity extends AppCompatActivity{

    private EditText edTEmail, edTPasswort, edTPasswordRepeat, edTUsername;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String userName, userPassword, userPasswordRepeat, userEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edTUsername = findViewById(R.id.edTUsername);
        edTEmail = findViewById(R.id.edTEmail);
        edTPasswort = findViewById(R.id.edTPassword);
        edTPasswordRepeat = findViewById(R.id.edTPasswordRepeat );

        pref = getSharedPreferences("com.preferences.sheeshapp", Context.MODE_PRIVATE);
        editor = pref.edit();




    }

    public void closeSignUp(View view) {
        this.finish();
    }

    public void submitSignUp(View view) {

       userName = edTUsername.getText().toString();
       userEmail = edTEmail.getText().toString();
       userPassword = edTPasswort.getText().toString();
       userPasswordRepeat = edTPasswordRepeat.getText().toString();


       editor.putString("savedUsername", userName);
       editor.putString("savedPassword", userPassword);
       editor.commit();
       this.finish();
    }
}
