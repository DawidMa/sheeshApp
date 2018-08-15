package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by d0272129 on 18.04.17.
 */

public class ChooseFriendActivityGuest extends AppCompatActivity {

    private ListView list;
    private FloatingActionButton chFabAccept;
    private NamesAdapter adapter;
    private List<ChooseFriendObject> objects = new ArrayList<>();
    private TextView tvFriends;
    private Guest guest;
    private Button btnAdd;
    private EditText etLocalName;
    private int addedLocals = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        guest = new Guest(this);
        tvFriends = findViewById(R.id.tvChooseTitle);
        Window window = getWindow();
        window.setStatusBarColor(Color.rgb(0,0,0));
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(getDrawable(R.drawable.toolbar_colors));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setElevation(0);
        setTitle(getString(R.string.choose_friend_title));
        btnAdd = findViewById(R.id.btnChooseAddLocal);
        etLocalName = findViewById(R.id.etChooseLocalFriend);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etLocalName.getText().toString();
                if (name.length()>0) {
                    addedLocals++;
                    long id = addedLocals*(-1);
                    objects.add(new ChooseFriendObject(name,id));
                    adapter.notifyDataSetChanged();
                    etLocalName.setText("");
                }
            }
        });
        chFabAccept = (FloatingActionButton)findViewById(R.id.chFabAccept);
        chFabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = (ListView)findViewById(R.id.liChooseList);
        fillList();
    }

    private void fillList() {
        adapter = new NamesAdapter(ChooseFriendActivityGuest.this,objects);
        list.setAdapter(adapter);
    }

    class NamesAdapter extends ArrayAdapter<ChooseFriendObject> {

        Context context;

        NamesAdapter(Context c, List<ChooseFriendObject> data) {
            super(c, R.layout.row_choose_friend,R.id.liChooseFriendName,data);
            this.context = c;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_choose_friend,parent,false);
            final ChooseFriendObject actualObject = getItem(position);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            myTitle.setText(actualObject.getName());
            Switch cb = (Switch) row.findViewById(R.id.switchFriends);
            cb.setChecked(true);
            cb.setClickable(false);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objects.remove(actualObject);
                    if (objects.isEmpty()) {
                        tvFriends.setText(getString(R.string.no_selected_friends_text));
                    } else {
                        tvFriends.setText(MyUtilities.getChooseFriendsAsString(objects));
                    }
                }
            });
            return row;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        guest.setFriends(objects);
        setResult(RESULT_CANCELED);
    }
}
