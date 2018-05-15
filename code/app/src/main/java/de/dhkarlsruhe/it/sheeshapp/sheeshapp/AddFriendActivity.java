package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.DelayAutoCompleteTextView;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.FriendAutoCompleteAdapter;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.UserSearchObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by User on 23.11.2017.
 */
public class AddFriendActivity extends AppCompatActivity {

    private TextView tvTitle;
    private FloatingActionButton fabAdd;
    private DelayAutoCompleteTextView autoCompleteTextView;
    private Friend friend;
    private String emailOfFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        friend = new Friend(this);
        setTitle("Add Friend");
        init();
    }

    private void init() {
        tvTitle = findViewById(R.id.addTvTitle);
        autoCompleteTextView = findViewById(R.id.autoAddName);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(new FriendAutoCompleteAdapter(this)); // 'this' is Activity instance
        autoCompleteTextView.setLoadingIndicator((android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserSearchObject user = (UserSearchObject) adapterView.getItemAtPosition(position);
                autoCompleteTextView.setText(user.getName());
                emailOfFriend = user.getEmail();
            }
        });
        fabAdd = findViewById(R.id.addFabAdd);
        fabAdd.setImageResource(R.mipmap.icon_plus_white);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        friend.addFriend(emailOfFriend);
                        finish();
                }
        });

    }
}
