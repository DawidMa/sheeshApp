package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;

/**
 * Created by d0272129 on 08.05.18.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvSessions;
    private TextView tvTobacco;
    private Profile profile;

    private String name;
    private long sessions;
    private String tobacco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        onlineLoader();
    }

    private void init() {
        profile = new Profile(this);
        tvName = findViewById(R.id.tvProName);
        tvSessions = findViewById(R.id.tvProSessions);
        tvTobacco = findViewById(R.id.tvProTobacco);

        name = profile.getProfileName();
        sessions = profile.getTotalSessions();
    }

    public void onlineLoader() {

        long user_id = profile.getId();
        final StringRequest request = new StringRequest(ServerConstants.URL+"user/info?user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                tobacco = string;
                makeProfile();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                errorResponse();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);
    }

    private void errorResponse() {
        Toast.makeText(this,"Error loading profile", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void makeProfile() {
        tvName.setText(name);
        tvSessions.setText(sessions+"");
        tvTobacco.setText(tobacco);
    }
}
