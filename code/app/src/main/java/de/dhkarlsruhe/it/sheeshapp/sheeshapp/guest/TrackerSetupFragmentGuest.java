package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.ChooseFriendActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.CustomTimePickerDialog;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by d0272129 on 14.04.17.
 */

public class TrackerSetupFragmentGuest extends Fragment{


    private static Friend friend;
    private CustomTimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private ConstraintLayout layoutTime, layoutFriends;
    private int minutes, seconds;
    private TextView tvTime, tvFriends, tvLocationLength, tvCommentLength;
    private static Guest guest;
    private View rootView;
    private EditText etComment, etLocation;
    private static final int MAX_CHARS = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friend = new Friend(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tracker_setup, container, false);
        guest = new Guest(getActivity());
        tvTime = rootView.findViewById(R.id.tvSeTimeInfo);
        tvFriends = rootView.findViewById(R.id.tvSeFriendsInfo);
        tvLocationLength = rootView.findViewById(R.id.tvSeLocationLength);
        tvCommentLength = rootView.findViewById(R.id.tvSeCommentLength);

        etComment = rootView.findViewById(R.id.etSeComment);
        etLocation = rootView.findViewById(R.id.etSeLocation);
        etComment.setKeyListener(null);
        etLocation.setKeyListener(null);

        MyUtilities.configureEtMax(etLocation,tvLocationLength,MAX_CHARS,false);
        MyUtilities.configureEtMax(etComment,tvCommentLength,MAX_CHARS,false);

        updateTvTime();

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                guest.setTime(minute,hourOfDay);
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
                Intent intent = new Intent(getContext(), ChooseFriendActivityGuest.class);
                startActivityForResult(intent,12345);
            }
        });
        myResume();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(resultCode+"XXXXXXXXXXXXXX");
        if (requestCode == 12345 && resultCode == RESULT_OK) {
            myResume();
        }
    }

    private void updateTvTime() {
        minutes = guest.getMinutes();
        seconds = guest.getSeconds();

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

        int sec = guest.getSeconds();
        int min = guest.getMinutes();

        boolean pass=false;
        sec+=min*60;

        if(sec>=5) {
            pass = checkChoosenFriends();
            if(!pass) {
                Toast.makeText(c, R.string.choose_at_least_one_friend,Toast.LENGTH_SHORT).show();
            } else {
                guest.setTimeInSeconds(sec);
            }
        } else {
            Toast.makeText(c, R.string.choose_more_than_5_seconds,Toast.LENGTH_SHORT).show();
        }
        return pass;
    }

    private static boolean checkChoosenFriends() {
        boolean check = false;
        int numberFriends = guest.getFriends().size();
        if(numberFriends==0) {
            check = true;
        }
        return true;
    }

    public void myResume() {
        System.out.println("nullnullnullnull");
        //tvFriends = rootView.findViewById(R.id.tvSeFriendsInfo);
        if (tvFriends!=null) {
            List<ChooseFriendObject> friends = guest.getFriends();
            if (!friends.isEmpty()) {
                tvFriends.setText(MyUtilities.getChooseFriendsAsString(friends));
            } else {
                tvFriends.setText(getString(R.string.no_friends_selected_click_to_select));
            }
        }
    }


}