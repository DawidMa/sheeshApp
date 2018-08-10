package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;

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
    private TextView tvTime, tvFriends;

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

        init(rootView);
        tvTime = rootView.findViewById(R.id.tvSeTimeInfo);
        tvFriends = rootView.findViewById(R.id.tvSeFriendsInfo);
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

    private void updateTvTime() {
        minutes = pref.getInt(SharedPrefConstants.MINUTES,0);
        seconds = pref.getInt(SharedPrefConstants.SECONDS,0);

        if (minutes==0 && seconds == 0) {
            tvTime.setText("No time selected! Click to select.");
        } else {
            String text = "Your Time: ";
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

    private void init(View v) {
        initTextViews(v);
    }

    private void initTextViews(View v) {
       // tvTitle = (TextView)v.findViewById(R.id.seTvTitle);

/*
        tvTitle.setTypeface(slabo);
        tvSeconds.setTypeface(slabo);
        tvMinutes.setTypeface(slabo);
        tvQuestionFriends.setTypeface(slabo);
        tvQuestionTime.setTypeface(slabo); */
    }

    public static boolean runShisha(Context c) {

        int sec = pref.getInt(SharedPrefConstants.SECONDS,0);
        int min = pref.getInt(SharedPrefConstants.MINUTES,0);

        boolean pass=false;
        sec+=min*60;

        if(sec>4) {
            pass = checkChoosenFriends();
            if(!pass) {
                Toast.makeText(c,"Wähle mindestens 2 Freunde!",Toast.LENGTH_SHORT).show();
            } else {
                editor.putInt(SharedPrefConstants.TIME_IN_SECONDS,sec);
                editor.commit();
            }
        } else {
            Toast.makeText(c,"Du hast weniger als 4 Sekunden gewählt!",Toast.LENGTH_SHORT).show();
        }
        return pass;
    }

    private static boolean checkChoosenFriends() {
        boolean check = false;
        int numberFriends = friend.getNumberOfAllCheckedFriends();
       /* int sumChoosen=0;
        for(int i=1; i<=numberFriends; i++) {
            boolean choosen =friend.getChecked(i+"");
            if(choosen) {
                sumChoosen++;
                int numShishas = pref.getInt("FRIENDS_NUM_SHISHAS_"+i,0);
                numShishas++;
                editor.putInt("FRIENDS_NUM_SHISHAS_"+i,numShishas);
            }
        }*/
        if(numberFriends>=2) {
            check = true;
            editor.commit();
        }
        return check;
    }
}