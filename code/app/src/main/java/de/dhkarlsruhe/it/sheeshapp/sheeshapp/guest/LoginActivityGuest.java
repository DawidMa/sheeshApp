package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;

public class LoginActivityGuest extends AppCompatActivity implements RewardedVideoAdListener {

    private EditText et;
    private Guest guest;
    private final static String AD_APP_ID = "ca-app-pub-4355529827581242~4147435635";
    private final static String AD_REWARD_ID = "ca-app-pub-4355529827581242/8469199305";
    private final static String AD_REWARD_TEST = "ca-app-pub-3940256099942544/5224354917";
    private RewardedVideoAd mRewardedVideoAd;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_activity_login);
        et = findViewById(R.id.etGuestLogin);
        guest = new Guest(this);
        MobileAds.initialize(this,AD_APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {

        mRewardedVideoAd.loadAd(AD_REWARD_TEST, new AdRequest.Builder().build());
    }


    public void cancelLogin(View view) {
        this.finish();
    }


    public void loginGuest(View view) {
        String name = et.getText().toString().trim();
        if (name.matches("")) {
            et.setText("");
            Snackbar.make(view,getString(R.string.check_your_inout),Snackbar.LENGTH_LONG).show();
        } else {
            username = name;
            startAd();
        }
    }

    private void startAd() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            guest.deleteAll();
            guest.setName(username);
            guest.setEmail(username);
            Intent intent = new Intent(this, MainActivityGuest.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        guest.deleteAll();
        guest.setName(username);
        guest.setEmail(username);
        Intent intent = new Intent(this, MainActivityGuest.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
