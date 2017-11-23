package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Informatik on 23.11.2017.
 */

public class FriendsFragment extends android.support.v4.app.Fragment {

    private ListView list;
    String names[];
    String valueShishas[];
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int value;
    TextView frTvNoFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        pref = this.getActivity().getSharedPreferences("com.preferences.sheeshapp", Context.MODE_PRIVATE);
        editor = pref.edit();
        frTvNoFriends = (TextView) rootView.findViewById(R.id.tvFragFriInfo);
        if (pref.getInt("NUMBER_OF_FRIENDS", 0) > 0) {
            frTvNoFriends.setVisibility(View.GONE);
        }
        names = getFriends();
        valueShishas = getNumOfShishas();
        list = (ListView) rootView.findViewById(R.id.lvFragFriList);
        MyAdapter adapter = new MyAdapter(getContext(), names, valueShishas);
        list.setAdapter(adapter);
      /*  fab = (FloatingActionButton)rootView.findViewById(R.id.frFabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);
            }
        });*/
        return rootView;
    }
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String myNames[];
        String myValueShishas[];

        MyAdapter(Context c, String[] titles, String[] descriptions) {
            super(c, R.layout.row_friends,R.id.liChooseFriendName,titles);
            this.context = c;
            this.myNames = titles;
            this.myValueShishas = descriptions;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_friends,parent,false);
            //View rootView = inflater.inflate(R.layout.fragment_friends, parent, false);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            TextView myDescriptions = (TextView)row.findViewById(R.id.liFriendNumOfShishas);
            myTitle.setText(names[position]);
            myDescriptions.setText(valueShishas[position]);

            String tag;
            tag = ""+(position+1);
            Button button = (Button)row.findViewById(R.id.liDeleteFriend);
            button.setTag(tag);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String)v.getTag();
                        value = Integer.parseInt(tag);
                        Toast.makeText(getContext(),tag,Toast.LENGTH_SHORT).show();
                        deleteFriend(value);
                        reloadListView();

                        if(pref.getInt("NUMBER_OF_FRIENDS",0)==0) {
                            frTvNoFriends.setVisibility(View.VISIBLE);
                        }
                    }

                });


            return row;
        }
    }
    public void reloadListView() {
        names = getFriends();
        valueShishas = getNumOfShishas();
        MyAdapter adapter = new MyAdapter(getContext(), names, valueShishas);
        list.setAdapter(adapter);
    }
    private void deleteFriend(int v) {
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        int numberToMove = numOfFriends - v;
        if(numberToMove>0) {
            for(int i=0; i<numberToMove;i++) {

                int moveFrom = v+i+1;
                int moveTo = v+i;

                if(moveFrom<=numOfFriends) {
                    editor.putString("FRIEND_" + moveTo, pref.getString("FRIEND_" + moveFrom, "Fehler bei überschreiben"));
                    editor.putBoolean("FRIEND_CHOOSEN_"+moveTo, pref.getBoolean("FRIEND_CHOOSEN_"+moveFrom,false));
                    editor.putInt("FRIENDS_NUM_SHISHAS_"+moveTo, pref.getInt("FRIENDS_NUM_SHISHAS_"+moveFrom,0));
                    editor.commit();
                }
            }
        }
        editor.putInt("NUMBER_OF_FRIENDS", numOfFriends - 1);
        editor.commit();
        editor.remove("FRIEND_" + numOfFriends);
        editor.remove("FRIEND_CHOOSEN_"+numOfFriends);
        editor.remove("FRIENDS_NUM_SHISHAS_"+numOfFriends);
        editor.commit();
    }

    private String[] getFriends() {
        String[] friends = new String[pref.getInt("NUMBER_OF_FRIENDS",0)];
        for(int i=1; i<=friends.length; i++) {
            friends[i-1] = pref.getString("FRIEND_"+i,"Fehler");
        }
        return friends;
    }
    private String[] getNumOfShishas() {
        String[] numOfShishas = new String[pref.getInt("NUMBER_OF_FRIENDS",0)];
        for(int i=1; i<=numOfShishas.length; i++) {
            numOfShishas[i-1] = "Bisherige Treffen: "+pref.getInt("FRIENDS_NUM_SHISHAS_"+i,2);
        }
        return numOfShishas;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadListView();
        if(pref.getInt("NUMBER_OF_FRIENDS",0)==0) {
            frTvNoFriends.setVisibility(View.VISIBLE);
        } else {
            frTvNoFriends.setVisibility(View.GONE);

        }
    }
}

