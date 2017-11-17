package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Informatik on 17.11.2017.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 4000);
*/
    }

    public void openTestMode(View view) {
        this.finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void openSignUp(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
