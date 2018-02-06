package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by Informatik on 23.11.2017.
 */

public class FriendsFragment extends android.support.v4.app.Fragment {
//new branch. Working on ListView
    private ListView list;
    private String names[];
    private String valueShishas[];
    int value;
    TextView frTvNoFriends;
    private Friend friend;
    ImageView friendImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        friend = new Friend(this.getActivity());
        frTvNoFriends = (TextView) rootView.findViewById(R.id.tvFragFriInfo);
        friendImage = rootView.findViewById(R.id.liFriendImage);
        int numberOfFriends = friend.getNumberOfFriends();
        if (numberOfFriends > 0) {
            frTvNoFriends.setVisibility(View.GONE);
        }
        names = friend.getFriends();
        valueShishas = getNumOfShishas();
        list = (ListView) rootView.findViewById(R.id.lvFragFriList);
        MyAdapter adapter = new MyAdapter(getContext(), names, valueShishas, friendImage);
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
        ImageView roundImage;
        private int LOAD_IMAGE_RESULTS =1;


        MyAdapter(Context c, String[] titles, String[] descriptions, ImageView image) {
            super(c, R.layout.row_friends,R.id.liChooseFriendName,titles);
            this.context = c;
            this.myNames = titles;
            this.myValueShishas = descriptions;
            this.roundImage = image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_friends,parent,false);
            //View rootView = inflater.inflate(R.layout.fragment_friends, parent, false);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            TextView myDescriptions = (TextView)row.findViewById(R.id.liFriendNumOfShishas);
            final ImageView myImage = row.findViewById(R.id.liFriendImage);
            myTitle.setText(names[position]);
            myDescriptions.setText(valueShishas[position]);
            Glide.with(context).load(R.drawable.user_avatar).asBitmap().centerCrop().into(new BitmapImageViewTarget(myImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    myImage.setImageDrawable(circularBitmapDrawable);
                }
            });
           // Glide.with(context).load(R.drawable.user_avatar).apply(RequestOptions.circleCropTransform()).into(roundImage);
            String tag;
            tag = ""+(position+1);
            myImage.setTag(tag);
            myImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String)v.getTag();
                    value = Integer.parseInt(tag);
                    Toast.makeText(getContext(),tag,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,LOAD_IMAGE_RESULTS);
                }
            });

            Button button = (Button)row.findViewById(R.id.liDeleteFriend);
            button.setTag(tag);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String)v.getTag();
                        value = Integer.parseInt(tag);
                        Toast.makeText(getContext(),tag,Toast.LENGTH_SHORT).show();
                        friend.deleteFriend(value);
                        reloadListView();

                        if(friend.getNumberOfFriends()==0) {
                            frTvNoFriends.setVisibility(View.VISIBLE);
                        }
                    }

                });


            return row;
        }
    }
    public void reloadListView() {
        names = friend.getFriends();
        valueShishas = getNumOfShishas();
        MyAdapter adapter = new MyAdapter(getContext(), names, valueShishas,friendImage);
        list.setAdapter(adapter);
    }

    private String[] getNumOfShishas() {
        String[] numOfShishas = new String[friend.getNumberOfFriends()];
        for(int i=1; i<=numOfShishas.length; i++) {
            numOfShishas[i-1] = "Bisherige Treffen: "+friend.getNumberOfShishasWithFriend(i);
        }
        return numOfShishas;
    }



    @Override
    public void onResume() {
        super.onResume();
        reloadListView();
        if(friend.getNumberOfFriends()==0) {
            frTvNoFriends.setVisibility(View.VISIBLE);
        } else {
            frTvNoFriends.setVisibility(View.GONE);

        }
    }
}

