package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest.LoginActivityGuest;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.login.LogInActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.signup.SignUpActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        image = findViewById(R.id.imgWelSheesh);
        if (image != null) {
            Glide.with(this).load(R.drawable.sheeshopa).into(image);
        }
    }

    public void closeWelcome(View view) {
        this.finishAndRemoveTask();
    }

    public void openSignUp(View view) {
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    public void openLogIn(View view) {
        Intent intent = new Intent(this,LogInActivity.class);
        startActivity(intent);
    }

    public void openGuest(View view) {
        Intent intent = new Intent(this,LoginActivityGuest.class);
        startActivity(intent);
    }
}
