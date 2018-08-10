package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;

/**
 * Created by Informatik on 16.11.2017.
 */

public class FirstActivity extends AppCompatActivity{

    public ImageView logo;
    public Animation fadeInAnimation;
    public TextView tvSplash;
    private ConstraintLayout layout;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logo = findViewById(R.id.logoSplash);
        tvSplash = findViewById(R.id.tvSplash);
        layout = findViewById(R.id.layoutFirst);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        activateAnimation();
        runnable = new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        };
        handler.postDelayed(runnable,4000);
        setListener();

    }

    private void setListener() {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                startNextActivity();
            }
        });
    }

    private void startNextActivity() {
        Intent intent = new Intent(this,WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void activateAnimation()  {

        //AnimationSet animation = new AnimationSet(false); //change to false
        //animation.addAnimation(fadeInAnimation);
        //animation.addAnimation(fadeOutAnimation);
        logo.setAnimation(fadeInAnimation);
        tvSplash.setAnimation(fadeInAnimation);

    }


}
