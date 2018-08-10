package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by d0272129 on 18.04.17.
 */

public class ChooseFriendActivity extends AppCompatActivity {

    private ListView list;
    private FloatingActionButton chFabAccept;
    private Friend friend;
    private UserSessionObject session;
    private NamesAdapter adapter;
    private List<ChooseFriendObject> objects = new ArrayList<>();
    private Gson json = new Gson();
    private List<Long> checkedFriendIds = new ArrayList<>();
    private List<Long> uncheckedFriendIds = new ArrayList<>();

    private Button btnAdd;
    private EditText etLocalName;
    private int addedLocals = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        friend = new Friend(this);
        session = new UserSessionObject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Choose Friends");
        btnAdd = findViewById(R.id.btnChooseAddLocal);
        etLocalName = findViewById(R.id.etChooseLocalFriend);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etLocalName.getText().toString();
                if (name.length()>0) {
                    addedLocals++;
                    long id = addedLocals*(-1);
                    friend.setFriendName(id,name);
                    friend.setChecked(id,false);
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


        getOfflineData();
    }

    private void getOfflineData() {
        if (friend.actualInformationAvailable()) {
            String offlineData = friend.getOfflineData();
            Type listType = new TypeToken<List<FriendlistObject>>() {}.getType();
            List<FriendlistObject> friendlistObject = json.fromJson(offlineData, listType);
            for (FriendlistObject o: friendlistObject) {
                objects.add(new ChooseFriendObject(o.getName(),o.getFriend_id()));
            }
            fillList();
        }
    }

    private void fillList() {
        friend.dropAllFriends();
        for (int i=0; i<objects.size(); i++) {
            friend.setFriendName(objects.get(i).getId(),objects.get(i).getName());
            friend.setChecked(objects.get(i).getId(),false);
        }
        for (ChooseFriendObject i:objects) {
            uncheckedFriendIds.add(i.getId());
        }
        adapter = new NamesAdapter(ChooseFriendActivity.this,objects);
        list.setAdapter(adapter);
    }

    private void getOnlineData() {
        /*
        long id = session.getUser_id();
        StringRequest request =  new StringRequest(ServerConstants.URL_FRIEND_NAMES+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                fillData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"An Error occured",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(request);*/

    }

    private void fillData(String string) {
        Type listType = new TypeToken<List<ChooseFriendObject>>() {}.getType();
        objects = json.fromJson(string, listType);
        friend.dropAllFriends();
        for (int i=0; i<objects.size(); i++) {
            friend.setFriendName(objects.get(i).getId(),objects.get(i).getName());
            friend.setChecked(objects.get(i).getId(),false);
        }
        for (ChooseFriendObject i:objects) {
            uncheckedFriendIds.add(i.getId());
        }
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
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_choose_friend,parent,false);
            ChooseFriendObject actualObject = getItem(position);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            myTitle.setText(actualObject.getName());
            long tag = actualObject.getId();
            Switch cb = (Switch) row.findViewById(R.id.switchFriends);
            cb.setTag(tag);
            boolean checked = friend.getChecked(tag);
            cb.setChecked(checked);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long tag = (long)v.getTag();
                    boolean checked = friend.getChecked(tag);
                    if(!checked) {
                       friend.setChecked(tag,true);
                       checkedFriendIds.add(tag);
                       uncheckedFriendIds.remove(tag);
                    } else {
                        friend.setChecked(tag,false);
                        checkedFriendIds.remove(tag);
                        uncheckedFriendIds.add(tag);
                    }
                }
            });
            return row;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        friend.setAllCheckedFriends(checkedFriendIds);
        friend.setAllUnchekedFriends(uncheckedFriendIds);
    }

    private String printFriendsList() {
        String ok = "";
        for(int i = 0; i < checkedFriendIds.size(); i++) {
            if(i==checkedFriendIds.size()-1) {
                ok+=(objects.get(i).getName()+".");
            } else {
                ok+=(objects.get(i).getName()+", ");
            }
        }
        return ok;
    }
}
