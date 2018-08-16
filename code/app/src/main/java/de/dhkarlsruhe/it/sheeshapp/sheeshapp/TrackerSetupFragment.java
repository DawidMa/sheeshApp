package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by d0272129 on 14.04.17.
 */

public class TrackerSetupFragment extends Fragment{

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static Friend friend;
    private CustomTimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private ConstraintLayout layoutTime, layoutFriends;
    private int minutes, seconds;
    private TextView tvTime, tvFriends, tvLocationLength, tvCommentLength;
    private static EditText etLocation, etComment;
    private static final int MAX_CHARS = 20;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // slabo = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Slabo.ttf");
        friend = new Friend(getContext());
        pref = getActivity().getSharedPreferences(SharedPrefConstants.SETTINGS,0);
        editor = pref.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracker_setup, container, false);

        tvTime = rootView.findViewById(R.id.tvSeTimeInfo);
        tvFriends = rootView.findViewById(R.id.tvSeFriendsInfo);
        tvLocationLength = rootView.findViewById(R.id.tvSeLocationLength);
        tvCommentLength = rootView.findViewById(R.id.tvSeCommentLength);

        etLocation = rootView.findViewById(R.id.etSeLocation);
        etComment = rootView.findViewById(R.id.etSeComment);

        MyUtilities.configureEtMax(etLocation,tvLocationLength,MAX_CHARS,true);
        MyUtilities.configureEtMax(etComment,tvCommentLength,MAX_CHARS,true);
        updateTvTime();

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editor.putInt(SharedPrefConstants.SECONDS,minute);
                editor.putInt(SharedPrefConstants.MINUTES,hourOfDay);
                editor.commit();
                seconds = minute;
                minutes = hourOfDay;
                updateTvTime();
            }
        };
        timePickerDialog = new CustomTimePickerDialog(getContext(), onTimeSetListener,minutes,seconds,true);

        layoutTime = rootView.findViewById(R.id.layoutSeTime);
        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        layoutFriends = rootView.findViewById(R.id.layoutSeFriends);
        layoutFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseFriendActivity.class);
                startActivityForResult(intent,11);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            updateTvFriend();
        } else {
            tvFriends.setText(getString(R.string.no_friends_selected_click_to_select));
        }
    }

    private void updateTvFriend() {
        List<ChooseFriendObject> objects = friend.getAllCheckedFriends();
        if (objects.isEmpty()) {
            tvFriends.setText(getString(R.string.no_friends_selected_click_to_select));
        } else {
            tvFriends.setText(MyUtilities.getChooseFriendsAsString(objects));
        }
    }

    private void updateTvTime() {
        minutes = pref.getInt(SharedPrefConstants.MINUTES,0);
        seconds = pref.getInt(SharedPrefConstants.SECONDS,0);

        if (minutes==0 && seconds == 0) {
            tvTime.setText(getString(R.string.no_time_selected_click_to_select));
        } else {
            String text = getString(R.string.your_time_text);
            if (minutes<=9) {
                text+="0";
            }
            text+=minutes+":";
            if (seconds<=9) {
                text+="0";
            }
            text+=seconds;
            tvTime.setText(text);
        }
    }

    public static boolean runShisha(Context c) {

        int sec = pref.getInt(SharedPrefConstants.SECONDS,0);
        int min = pref.getInt(SharedPrefConstants.MINUTES,0);

        boolean pass=false;
        sec+=min*60;

        if(sec>=5) {
            pass = checkChoosenFriends();
            if(!pass) {
                Toast.makeText(c, R.string.choose_at_least_one_friend,Toast.LENGTH_SHORT).show();
            } else {
                editor.putInt(SharedPrefConstants.TIME_IN_SECONDS,sec);
                editor.putString(SharedPrefConstants.COMMENT,etComment.getText().toString());
                editor.putString(SharedPrefConstants.LOCATION,etLocation.getText().toString());
                editor.commit();
            }
        } else {
            Toast.makeText(c, R.string.choose_more_than_5_seconds,Toast.LENGTH_SHORT).show();
        }
        return pass;
    }

    private static boolean checkChoosenFriends() {
        int numberFriends = friend.getAllCheckedFriends().size();

        if(numberFriends==0) {
            List<ChooseFriendObject> objects = new ArrayList<>();
            objects = MyUtilities.getOfflineFriends(friend);
            Gson json = new Gson();
            friend.setAllUncheckedFriends(json.toJson(objects));
        }
        return true;
    }
}