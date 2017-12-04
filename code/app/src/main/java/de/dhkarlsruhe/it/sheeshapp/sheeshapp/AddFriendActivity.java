package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 23.11.2017.
 */
public class AddFriendActivity extends AppCompatActivity {

    TextView tvTitle;
    EditText etName;
    FloatingActionButton fabAdd;
    private Friend friend;


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
        tvTitle = (TextView)findViewById(R.id.addTvTitle);
        etName = (EditText)findViewById(R.id.addEtName);
        fabAdd = (FloatingActionButton)findViewById(R.id.addFabAdd);
        fabAdd.setImageResource(R.mipmap.icon_plus_white);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSymbol('#')) {
                    if (friend.checkFriend(etName.getText().toString())) {
                        friend.addFriend(etName.getText().toString());
                        finish();
                    } else {
                        etName.setText("");
                        Toast.makeText(getApplicationContext(), "Name bereits vorhanden!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etName.setText("");
                    Toast.makeText(getApplicationContext(), "Unerlaubtes Zeichen '#'", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkSymbol(char c) {
        if(etName.getText().toString().indexOf(c)>=0) {
            return false;
        }
        return true;
    }
}
