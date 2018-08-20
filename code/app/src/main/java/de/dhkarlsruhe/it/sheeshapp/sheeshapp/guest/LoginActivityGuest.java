package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

public class LoginActivityGuest extends AppCompatActivity implements RewardedVideoAdListener {

    private EditText et;
    private Guest guest;
    private final static String AD_APP_ID = "ca-app-pub-4355529827581242~4147435635";
    private final static String AD_REWARD_ID = "ca-app-pub-4355529827581242/8469199305";
    private final static String AD_REWARD_ID_TEST = "ca-app-pub-3940256099942544/5224354917";
    private RewardedVideoAd mRewardedVideoAd;
    private String username;
    private boolean rewared = false;
    private Button btnLogin;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_activity_login);
        et = findViewById(R.id.etGuestLogin);
        guest = new Guest(this);
        btnLogin = findViewById(R.id.btnGuestLogin);
        tvInfo = findViewById(R.id.tvGuestLoginInfo);
        MobileAds.initialize(this,AD_APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(AD_REWARD_ID_TEST, new AdRequest.Builder().build());
        tvInfo.setText(getString(R.string.loading_ad_text));
    }


    public void cancelLogin(View view) {
        this.finish();
    }


    public void loginGuest(View view) {
        if (!MyUtilities.etOK(et)) {
            et.setText("");
            Snackbar.make(view,getString(R.string.check_your_inout),Snackbar.LENGTH_LONG).show();
        } else {
            btnLogin.setEnabled(false);
            username = et.getText().toString().trim();
            startAd(view);
        }
    }

    private void startAd(View view) {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            /*
            guest.deleteAll();
            guest.setName(username);
            guest.setEmail(username);
            Intent intent = new Intent(this, MainActivityGuest.class);
            startActivity(intent);
            this.finish();*/
            Snackbar.make(view,getString(R.string.error_text),Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        btnLogin.setEnabled(true);
        tvInfo.setText(getString(R.string.guest_login_info_text));
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
        if (rewared) {
            guest.deleteAll();
            guest.setName(username);
            guest.setEmail(username);
            Intent intent = new Intent(this, MainActivityGuest.class);
            startActivity(intent);
            this.finish();
        } else {
            Snackbar.make(btnLogin, R.string.clsoed_ad_text, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        rewared = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Snackbar.make(btnLogin, getString(R.string.failed_to_load_ad), Snackbar.LENGTH_LONG).show();
        tvInfo.setText(getString(R.string.failed_to_load_ad));

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
