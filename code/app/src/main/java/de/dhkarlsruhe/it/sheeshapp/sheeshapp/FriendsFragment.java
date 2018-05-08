package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.ProfileActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by Informatik on 23.11.2017.
 */

public class FriendsFragment extends android.support.v4.app.Fragment {

    private ListView list;
    private List<String> names = new ArrayList<>();
    private List<String> valueShishas = new ArrayList<>();
    int value;
    private TextView frTvNoFriends;
    private Friend friend;
    ImageView friendImage;
    MyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<FriendlistObject> friendlistObject;
    private Gson json = new Gson();
    private UserSessionObject session;
    private int numOfFriends = 0;
    private Context c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        friend = new Friend(this.getActivity());
        c = this.getActivity();
        session = new UserSessionObject(getContext());
        frTvNoFriends = (TextView) rootView.findViewById(R.id.tvFragFriInfo);
        friendImage = rootView.findViewById(R.id.liFriendImage);
        list = (ListView) rootView.findViewById(R.id.lvFragFriList);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeFragFri);
        swipeRefreshLayout.setColorSchemeColors(Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFriendlist();
            }
        });
        getResponseOnline();

        /** Show text if no friends available */
        checkAmountFriends();

        return rootView;
    }

    private void checkAmountFriends() {
        if (friendlistObject!=null) {
            numOfFriends = friendlistObject.size();
        } else
            numOfFriends = 0;
        if (numOfFriends > 0) {
            frTvNoFriends.setVisibility(View.GONE);
        } else {
            frTvNoFriends.setVisibility(View.VISIBLE);
        }
    }

    public void reloadListView() {
        names.clear();
        valueShishas.clear();
        if (friendlistObject!=null) {
            for (FriendlistObject rels: friendlistObject) {
                names.add(rels.getName()+"");
                valueShishas.add(rels.getRelation_id()+"");
            }
            adapter = new MyAdapter(getContext(), names, valueShishas,friendImage);
            list.setAdapter(adapter);
        } else {
            numOfFriends = 0;
        }
        checkAmountFriends();
    }

    private String[] getNumOfShishas() {
        String[] numOfShishas = new String[friend.getNumberOfFriends()];
        for(int i=1; i<=numOfShishas.length; i++) {
            numOfShishas[i-1] = "Bisherige Treffen: "+friend.getNumberOfShishasWithFriend(i);
        }
        return numOfShishas;
    }

    public void updateFriendlist() {
        getResponseOnline();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getResponseOnline() {
        swipeRefreshLayout.setRefreshing(true);
        long id = session.getUser_id();
        StringRequest request =  new StringRequest(ServerConstants.URL_RELATIONS+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                positiveResponse(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                negativeResponse(volleyError.getMessage());
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    private void negativeResponse(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void positiveResponse(String string) {
        Type listType = new TypeToken<List<FriendlistObject>>(){}.getType();
        friendlistObject = json.fromJson(string,listType);
        numOfFriends = friendlistObject.size();
        reloadListView();
        swipeRefreshLayout.setRefreshing(false);
    }

    /** START OF MyAdapter */
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> names;
        List<String> descriptions;
        ImageView image;

        MyAdapter(Context c, List<String> names, List<String> descriptions, ImageView image) {
            //Deleted R.id.lichoosefriend in super()
            super(c, R.layout.row_friends,R.id.liFriendName,names);
            this.context = c;
            this.names = names;
            this.descriptions = descriptions;
            this.image = image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_friends,parent,false);
            TextView tvTitle = (TextView)row.findViewById(R.id.liFriendName);
            TextView tvDescription = (TextView)row.findViewById(R.id.liFriendNumOfShishas);
            final ImageView imgFriends = row.findViewById(R.id.liFriendImage);

            tvTitle.setText(names.get(position));
            tvDescription.setText(valueShishas.get(position));
            loadRoundedImage(imgFriends);

            String tag;
            tag = ""+(position+1);
            Button button = (Button)row.findViewById(R.id.liDeleteFriend);
            button.setTag(tag);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String)v.getTag();
                        value = Integer.parseInt(tag);
                        removeFriend(value-1);
                    }

                });
            row.setTag(tag);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String)v.getTag();
                        value = Integer.parseInt(tag);
                        openFriendsProfile(value-1);
                    }
                });
            return row;
        }

        private void loadRoundedImage(ImageView view) {
            /** Loading Random drawable as round icon */
            Resources res = context.getResources();
            int resID = res.getIdentifier(friend.getRandomDrawable(), "drawable", context.getPackageName());
            Glide.with(context).load(resID).asBitmap().centerCrop().into(new BitmapImageViewTarget(view) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    view.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    private void openFriendsProfile(int i) {
        friend.setProfile(friendlistObject.get(i));
        Intent intent = new Intent(c, ProfileActivity.class);
        startActivity(intent);
    }

    private void removeFriend(int i) {
        final int id = i;
        MyAlert alert = new MyAlert(c,"Löschen","Bist du sicher dass du " +friendlistObject.get(id).getName()+ " löschen willst?");
        alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                friendlistObject.remove(id);
                reloadListView();
            }
        });alert.show();
    }

    /** END OF ADAPTER */


    @Override
    public void onResume() {
        super.onResume();
        reloadListView();
    }
}

