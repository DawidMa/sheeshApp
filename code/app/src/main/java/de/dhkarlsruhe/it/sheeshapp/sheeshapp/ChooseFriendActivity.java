package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by d0272129 on 18.04.17.
 */

public class ChooseFriendActivity extends AppCompatActivity {

    private ListView list;
    String names[];
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    FloatingActionButton chFabAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Freunde wählen");

        pref = getSharedPreferences("com.preferences.sheeshapp",0);
        editor = pref.edit();
        chFabAccept = (FloatingActionButton)findViewById(R.id.chFabAccept);
        chFabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        names = getFriends();
        list = (ListView)findViewById(R.id.liChooseList);
        MyAdapter2 adapter = new MyAdapter2(ChooseFriendActivity.this,names);
        list.setAdapter(adapter);
    }

    private String[] getFriends() {
        String[] friends = new String[pref.getInt("NUMBER_OF_FRIENDS",0)];
        for(int i=1; i<=friends.length; i++) {
            friends[i-1] = pref.getString("FRIEND_"+i,"Fehler");
        }
        return friends;
    }
    class MyAdapter2 extends ArrayAdapter<String> {

        Context context;
        String myNames[];

        MyAdapter2(Context c, String[] titles) {
            super(c, R.layout.row_choose_friend,R.id.liChooseFriendName,titles);
            this.context = c;
            this.myNames = titles;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_choose_friend,parent,false);
            //View rootView = inflater.inflate(R.layout.fragment_friends, parent, false);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            myTitle.setText(names[position]);

            String tag;
            tag = ""+(position+1);
            CheckBox cb = (CheckBox)row.findViewById(R.id.liChooseCb);
            cb.setTag(tag);

            final boolean checked = pref.getBoolean("FRIEND_CHOOSEN_"+tag,false);
            cb.setChecked(checked);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String)v.getTag();
                    if(!checked) {
                        editor.putBoolean("FRIEND_CHOOSEN_"+tag,true);
                    } else {
                        editor.putBoolean("FRIEND_CHOOSEN_"+tag,false);
                    }
                    editor.commit();
                }
            });
            return row;
        }
    }
}
