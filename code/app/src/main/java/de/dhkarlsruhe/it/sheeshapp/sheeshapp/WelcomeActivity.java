package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


/**
 * Created by Informatik on 17.11.2017.
 */

public class WelcomeActivity extends AppCompatActivity {

    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        image = findViewById(R.id.imgWelLogo);
        if (image != null) {
            Glide.with(this).load(R.drawable.sheeshopa).into(image);
            sendViewToBack(image);
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
    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public void openGuest(View view) {
        Snackbar.make(view, R.string.coming_soon_text,Snackbar.LENGTH_SHORT).show();
    }
}
