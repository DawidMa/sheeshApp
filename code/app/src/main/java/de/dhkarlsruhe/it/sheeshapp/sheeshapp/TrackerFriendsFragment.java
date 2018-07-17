package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;

/**
 * Created by d0272129 on 17.07.18.
 */

public class TrackerFriendsFragment extends Fragment {

    CheckboxClicked callback;
    private View rootView;
    private Context context;
    private Friend friend;
    private ListView listView;
    private TrackerFriendsAdapter adapter;
    private List<ChooseFriendObject> checked = new ArrayList<>();
    private List<ChooseFriendObject> unchecked = new ArrayList<>();
    private List<ChooseFriendObject> objects = new ArrayList<>();


    public interface CheckboxClicked {
        public void send(ChooseFriendObject object, boolean checked);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (CheckboxClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tracker_friends, container, false);
        context = this.getActivity();
        friend = new Friend(context);
        listView = rootView.findViewById(R.id.lvFragTrackerFriends);
        adapter = new TrackerFriendsAdapter(getActivity(),objects, checked.size());
        listView.setAdapter(adapter);
        return rootView;
    }

    public void getFriendLists(List<ChooseFriendObject> checked, List<ChooseFriendObject> unchecked) {
        this.checked = checked;
        this.unchecked = unchecked;
        fillList();
    }

    private void fillList() {
        objects.clear();
        objects.addAll(checked);
        objects.addAll(unchecked);
        adapter = new TrackerFriendsAdapter(getActivity(),objects, checked.size());
        listView.setAdapter(adapter);

    }

    public void sendInfo(ChooseFriendObject object, boolean checked) {
        callback.send(object,checked);
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    class TrackerFriendsAdapter extends ArrayAdapter<ChooseFriendObject> {

        Context context;
        int numOfChecked;

        TrackerFriendsAdapter(Context c, List<ChooseFriendObject> checkedFirst, int numOfChecked) {
            super(c, R.layout.row_choose_friend,R.id.liChooseFriendName,checkedFirst);
            this.context = c;
            this.numOfChecked = numOfChecked;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_choose_friend,parent,false);
            final ChooseFriendObject actualObject = getItem(position);
            TextView myTitle = (TextView)row.findViewById(R.id.liChooseFriendName);
            myTitle.setText(actualObject.getName());
            long tag = actualObject.getId();
            final CheckBox cb = (CheckBox)row.findViewById(R.id.liChooseCb);
            cb.setTag(tag);
            if (position<numOfChecked) {
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb.isChecked()) {
                        sendInfo(actualObject,false);
                    } else {
                        sendInfo(actualObject,true);
                    }
                }
            });
            return row;
        }
    }

}
