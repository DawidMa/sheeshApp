package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendRequestObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by d0272129 on 18.04.17.
 */

public class ChooseFriendActivity extends AppCompatActivity {

    private ListView list;
    private FloatingActionButton chFabAccept;
    private Friend friend;
    private NamesAdapter adapter;
    private List<ChooseFriendObject> objects = new ArrayList<>();
    private Gson json = new Gson();
    private TextView tvFriends;
    private List<ChooseFriendObject> checkedObjects= new ArrayList<>();
    private List<ChooseFriendObject> uncheckedObjects = new ArrayList<>();
    private Button btnAdd;
    private EditText etLocalName;
    private int addedLocals = 0;
    private boolean changedData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        friend = new Friend(this);
        friend.dropAllFriends();
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
                    ChooseFriendObject newObject = new ChooseFriendObject(name,id);
                    checkedObjects.add(newObject);
                    objects.add(newObject);
                    adapter.notifyDataSetChanged();
                    etLocalName.setText("");
                    updateTv();
                }
            }
        });
        chFabAccept = (FloatingActionButton)findViewById(R.id.chFabAccept);
        chFabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson json = new Gson();
                friend.setAllCheckedFriends(json.toJson(checkedObjects));
                friend.setAllUncheckedFriends(json.toJson(uncheckedObjects));
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        list = (ListView)findViewById(R.id.liChooseList);
        getOfflineData();
    }

    private void getOfflineData() {

        objects = MyUtilities.getOfflineFriends(friend);
        uncheckedObjects.addAll(objects);
        fillList();
    }

    private void fillList() {
        adapter = new NamesAdapter(ChooseFriendActivity.this,objects);
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
            final ChooseFriendObject actualObject = objects.get(position);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            myTitle.setText(actualObject.getName());
            final Switch cb = (Switch) row.findViewById(R.id.switchFriends);
            cb.setClickable(false);
            final boolean checked;
            if (checkedObjects.contains(objects.get(position))) {
                checked = true;
            } else {
                checked = false;
            }
            cb.setChecked(checked);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!cb.isChecked()) {
                        checkedObjects.add(objects.get(position));
                        uncheckedObjects.remove(objects.get(position));
                        cb.setChecked(true);
                    } else {
                        checkedObjects.remove(objects.get(position));
                        uncheckedObjects.add(objects.get(position));
                        cb.setChecked(false);
                    }
                    updateTv();
                }
            });
            return row;
        }
    }

    private void updateTv(){
        if (checkedObjects.isEmpty()) {
            tvFriends.setText(getString(R.string.no_selected_friends_text));
        } else {
            tvFriends.setText(MyUtilities.getChooseFriendsAsString(checkedObjects));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
