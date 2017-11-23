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
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("com.preferences.sheeshapp",0);
        editor = pref.edit();
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
                    if (checkFriend()) {
                        addFriend();
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

    private boolean checkFriend() {
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        boolean accepted = true;
        if(numOfFriends>0) {
            for(int i=1; i<=numOfFriends; i++) {
                String name = pref.getString("FRIEND_"+i,"FEHLER");
                if(name.equals(etName.getText().toString())) {
                    return false;
                }
            }
        }
        return accepted;
    }

    private void addFriend() {
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        numOfFriends++;
        editor.putInt("NUMBER_OF_FRIENDS",numOfFriends);
        editor.commit();
        editor.putString("FRIEND_"+numOfFriends,etName.getText().toString());
        editor.putInt("FRIENDS_NUM_SHISHAS_"+numOfFriends,0);
        editor.commit();
        this.finish();
    }
}
