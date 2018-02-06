package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by d0272129 on 14.04.17.
 */

public class TrackerSetupFragment extends Fragment{

    private TextView tvSeconds, tvMinutes, tvQuestionTime, tvQuestionFriends;
   // private Typeface slabo;
    private static NumberPicker pickSeconds, pickMinutes;
    private Button btStartMeeting;
    static SharedPreferences pref;
    private SharedPreferences settingsPref;
    static SharedPreferences.Editor editor;
    private boolean prefCheckbox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // slabo = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Slabo.ttf");
        pref = this.getActivity().getSharedPreferences("com.preferences.sheeshapp",0);
        settingsPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefCheckbox = settingsPref.getBoolean("pref_ownProfilePicture",false);
        editor = pref.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracker_setup, container, false);
        init(rootView);
        btStartMeeting = (Button) rootView.findViewById(R.id.seBtChooseFriends);
        btStartMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChooseFriendActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void init(View v) {
        initPicker(v);
        initTextViews(v);
    }
    private void initPicker(View v) {
        pickSeconds = (NumberPicker)v.findViewById(R.id.sePickSeconds);
        pickMinutes = (NumberPicker)v.findViewById(R.id.sePickMinutes);
        pickSeconds.setMaxValue(59);
        pickSeconds.setMinValue(0);
        pickMinutes.setMaxValue(5);
        pickMinutes.setMinValue(0);

        if (prefCheckbox) {
            Toast.makeText(getActivity(),"TRUE",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(),"FALSE",Toast.LENGTH_SHORT).show();
        }
    }
    private void initTextViews(View v) {
       // tvTitle = (TextView)v.findViewById(R.id.seTvTitle);
        tvSeconds = (TextView)v.findViewById(R.id.seTvSeconds);
        tvMinutes = (TextView)v.findViewById(R.id.seTvMinutes);
        tvQuestionFriends = (TextView)v.findViewById(R.id.seTvQuestionForFriends);
        tvQuestionTime = (TextView)v.findViewById(R.id.seTvQuestionForTime);
/*
        tvTitle.setTypeface(slabo);
        tvSeconds.setTypeface(slabo);
        tvMinutes.setTypeface(slabo);
        tvQuestionFriends.setTypeface(slabo);
        tvQuestionTime.setTypeface(slabo); */
    }

    public static boolean runShisha(Context c) {
        int seconds = pickSeconds.getValue();
        int minutes = pickMinutes.getValue();
        boolean pass=false;
        seconds+=minutes*60;
        editor.putInt("TIME_IN_SECONDS",seconds);
        editor.commit();
        if(pref.getInt("TIME_IN_SECONDS",0)>0) {
            pass = checkChoosenFriends();
            if(!pass) {
                Toast.makeText(c,"Wähle mindestens 2 Freunde!",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(c,"Du hast 0 Sekunden gewählt!",Toast.LENGTH_SHORT).show();
        }
        return pass;
    }

    private static boolean checkChoosenFriends() {
        boolean check = false;
        int numberFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        int sumChoosen=0;
        for(int i=1; i<=numberFriends; i++) {
            boolean choosen =pref.getBoolean("FRIEND_CHOOSEN_"+i,false);
            if(choosen) {
                sumChoosen++;
                int numShishas = pref.getInt("FRIENDS_NUM_SHISHAS_"+i,0);
                numShishas++;
                editor.putInt("FRIENDS_NUM_SHISHAS_"+i,numShishas);
            }
        }
        if(sumChoosen>=2) {
            check = true;
            editor.commit();
        }
        return check;
    }
}