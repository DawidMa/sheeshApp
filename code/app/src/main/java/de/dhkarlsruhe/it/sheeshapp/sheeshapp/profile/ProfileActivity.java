package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;

/**
 * Created by d0272129 on 08.05.18.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvName = findViewById(R.id.tvProName);
        friend = new Friend(this);
        tvName.setText(friend.getProfileName());
    }
}
