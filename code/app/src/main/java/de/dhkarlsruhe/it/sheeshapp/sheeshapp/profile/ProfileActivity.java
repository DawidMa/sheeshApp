package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;

/**
 * Created by d0272129 on 08.05.18.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvSessions;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile = new Profile(this);
        tvName = findViewById(R.id.tvProName);
        tvSessions = findViewById(R.id.tvProSessions);
        tvName.setText(profile.getProfileName());
        tvSessions.setText(profile.getTotalSessions()+"");
    }
}
