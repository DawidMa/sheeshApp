package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MyAlert;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendRequestObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;

/**
 * Created by d0272129 on 08.05.18.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvNumberShishas, tvTobacco, tvSince, tvNumberFriends;
    private String name, numberShishas, tobacco, since, numberFriends;
    private UserProfileObject userProfileObject;
    private ImageView profileImage;
    private Window window;
    private long friendid;

    private Profile profile;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        onlineLoader();
    }

    private void init() {
        profile = new Profile(this);
        friend = new Friend(this);
        tvName = findViewById(R.id.tvProName);
        tvNumberShishas = findViewById(R.id.tvProShishas);
        tvTobacco = findViewById(R.id.tvProTobacco);
        tvSince = findViewById(R.id.tvProSince);
        tvNumberFriends = findViewById(R.id.tvProFriends);
        profileImage = findViewById(R.id.imgProImage);
        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("PROFILE_IMAGE");
        friendid = extras.getLong("FRIEND_ID");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        profileImage.setImageBitmap(bmp);

        name = extras.getString("FRIEND_NAME");
        tvName.setText(name);
    }

    public void onlineLoader() {

        long user_id = profile.getId();
        final StringRequest request = new StringRequest(ServerConstants.URL+"user/info?user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseData(string);
                reloadProfile();
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

    private void parseData(String string) {
        Gson json = new Gson();
        try {
            userProfileObject = json.fromJson(string,UserProfileObject.class);
        } catch (Exception e) {
            userProfileObject.setNumberFriends("#111");
            userProfileObject.setNumberShishas("555");
            userProfileObject.setSince("12.12.2012");
            userProfileObject.setTobacco("Dawid's Tabak");
        }
    }

    private void errorResponse() {
        Toast.makeText(this,"Error loading profile", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void reloadProfile() {
        tvTobacco.setText(userProfileObject.getTobacco());
        tvSince.setText(userProfileObject.getSince());
        tvNumberFriends.setText(userProfileObject.getNumberFriends());
        tvNumberShishas.setText(userProfileObject.getNumberShishas());
    }

    public void deleteFriend(View view) {
        MyAlert alert = new MyAlert(this, "Löschen", "Bist du sicher dass du " + tvName.getText().toString() + " löschen willst?");
        alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    friend.deleteFriendOnline(friendid);
                } catch (Exception e) {

                }
            }
        });
        alert.show();
    }


}
