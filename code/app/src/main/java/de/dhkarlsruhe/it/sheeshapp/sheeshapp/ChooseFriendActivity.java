package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendRequestObject;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        friend = new Friend(this);
        session = new UserSessionObject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Choose Friends");
        chFabAccept = (FloatingActionButton)findViewById(R.id.chFabAccept);
        chFabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = (ListView)findViewById(R.id.liChooseList);
        getOnlineData();
    }

    private void getOnlineData() {
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
        rQueue.add(request);
    }

    private void fillData(String string) {
        Type listType = new TypeToken<List<ChooseFriendObject>>() {}.getType();
        objects = json.fromJson(string, listType);
        friend.dropAllFriends();
        for (int i=0; i<objects.size(); i++) {
            friend.setFriendName(objects.get(i).getId(),objects.get(i).getName());
            friend.setChecked(objects.get(i).getId(),false);
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
            CheckBox cb = (CheckBox)row.findViewById(R.id.liChooseCb);
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
                    } else {
                        friend.setChecked(tag,false);
                        checkedFriendIds.remove(tag);
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
    }
}
