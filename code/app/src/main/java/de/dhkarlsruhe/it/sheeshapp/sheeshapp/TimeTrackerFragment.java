package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Informatik on 28.11.2017.
 */

public class TimeTrackerFragment extends android.support.v4.app.Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    MyTimer timerTotal, timerSingle;
    TextView tiTvChoosenFriends,tiTvTotal,tiTvSingle, tiTvInfo, tiTvTopTitle;
    String[] friends, sequence;
    static String firstFriend;
    FloatingActionButton btStart, btPause, btEnd;
    Thread threadTotal, threadSingle, threadTimeToChange, threadVibrator;
    boolean threadsRunning=false;
    boolean firstStart = true;
    int actualFriend=0;
    Random rnd;
    boolean timeToChange=false;
    boolean darkBackground = true;
    ConstraintLayout tiLayoutMain;
    Vibrator vib;
    int times;
    Calendar calendar = Calendar.getInstance();
    int numOfSavedEntries, numOfSwitchedCoal=0;
    String friendsAsString="", dateStart ="", dateEnd="",totalTime;

    //Notification Setup
    NotificationCompat.Builder notification;
    private static final int uniqueID = 123456;
    private boolean showNotification = true;
    private boolean notificationIsActive = false;
    NotificationManager manager;
    Thread threadNotification;

    View v;
    public TimeTrackerFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_tracker, container, false);
        v= rootView;
        init();
        friends = getChoosenFriends();
        printFriends(friends, tiTvChoosenFriends);
        rnd = new Random();
        sequence = setNewSequence();
        firstFriend = sequence[0];
        return rootView;
    }

    private String[] setNewSequence() {
        String[] newOrder = new String[friends.length];
        for (int i = 0; i < newOrder.length; i++) {
            newOrder[i] = friends[i];
        }
        Collections.shuffle(Arrays.asList(newOrder));
        return newOrder;

    }

    private void init() {
        pref = this.getActivity().getSharedPreferences("com.preferences.sheeshapp", 0);
        editor = pref.edit();
        tiTvChoosenFriends = (TextView) v.findViewById(R.id.tiTvChoosenFriends);
        tiTvTopTitle = (TextView)v.findViewById(R.id.tiTvTopTitle);
        tiTvTotal = (TextView) v.findViewById(R.id.tiTvTotal);
        tiTvSingle = (TextView) v.findViewById(R.id.tiTvSingle);
        tiTvInfo = (TextView)v.findViewById(R.id.tiTvInfo);
        btStart = (FloatingActionButton)v.findViewById(R.id.tiBtStart);
        btPause = (FloatingActionButton)v.findViewById(R.id.tiBtPause);
        btPause.setEnabled(false);
        btEnd = (FloatingActionButton)v.findViewById(R.id.tiBtEnd);
        tiLayoutMain = (ConstraintLayout)v.findViewById(R.id.tiLayoutMain);
        vib = (Vibrator) this.getContext().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        timerTotal = new MyTimer(0,true);
        timerSingle = new MyTimer(pref.getInt("TIME_IN_SECONDS",0),false);
        tiTvTotal.setText("Gesamtzeit: "+timerTotal.getTotalTimeAsString());
        tiTvSingle.setText("Restliche Zugzeit: "+timerSingle.getSingleTimeAsString());

        numOfSavedEntries = pref.getInt("NUM_OF_SAVED_ENTRIES",0);
        dateStart = setDate();
        //Notification
        notification = new NotificationCompat.Builder(this.getActivity());
        notification.setAutoCancel(true);

    }
    public void fragmentPressedStart(View view) {
        btPause.setEnabled(true);
        btStart.setEnabled(false);
        if(firstStart) {
            firstStart = false;
            getActivity().showDialog(1);
        } else {
            threadsRunning=true;
            runTimeSingle();
        }
        tiTvInfo.setText("Momentan ist " + sequence[actualFriend] + " dran.");
    }

    public static String getFirstFriend() {
        return firstFriend;
    }

    public void fragmentPressedPause(View view) {
        btPause.setEnabled(false);
        btStart.setEnabled(true);
        threadsRunning=false;
        randomColeChanger();
        numOfSwitchedCoal++;
    }

    private void randomColeChanger() {
        int numFriends = friends.length;
        tiTvInfo.setText(friends[rnd.nextInt(numFriends)] + " muss die Kohle drehen!");
    }

    public void fragmentPressedEnd(View view) {
        btPause.setEnabled(false);
        btStart.setEnabled(true);
        threadsRunning=false;
        getActivity().showDialog(2);
        totalTime=timerTotal.getTotalTimeAsString();
    }
    private void printFriends(String [] str, TextView tv) {
        for(int i = 0; i < str.length; i++) {
            if(i==str.length-1) {
                tv.append(str[i]+".");
                friendsAsString+=(str[i]+".");
            } else {
                tv.append(str[i]+", ");
                friendsAsString+=(str[i]+", ");
            }
        }
    }

    @Override
    public void onDetach() {
        try {
            threadTotal.interrupt();
            threadSingle.interrupt();
        } catch (Exception e) {

        }

        super.onDetach();
    }

    private String[] getChoosenFriends() {
        int numberFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        int numberChoosenFriends=0;
        for(int i=1; i<=numberFriends; i++) {
            boolean choosen = pref.getBoolean("FRIEND_CHOOSEN_"+i,false);
            if(choosen) {
                numberChoosenFriends++;
            }
        }
        String[] choosenFriends = new String[numberChoosenFriends];
        int friendNumber=0;
        for(int i=1; i<=numberFriends; i++) {
            boolean choosen = pref.getBoolean("FRIEND_CHOOSEN_"+i,false);
            if(choosen) {
                choosenFriends[friendNumber] = pref.getString("FRIEND_"+i,"FEHLER");
                friendNumber++;
            }
        }
        return choosenFriends;
    }

    private void runTimeTotal() {
        threadTotal = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timerTotal.incSeconds();
                                tiTvTotal.setText("Gesamtzeit: " +timerTotal.getTotalTimeAsString());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        threadTotal.start();
    }

    private void runTimeSingle() {
        threadSingle = new Thread() {
            @Override
            public void run() {
                try {
                    while (threadsRunning) {
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timerSingle.decSeconds();
                                if(!timeToChange&&timerSingle.getSeconds()<=5 && timerSingle.getMinutes()==0) {
                                    timeToChange = true;
                                    showThatTimeToChange();
                                }
                                if(timerSingle.getSeconds()==0 && timerSingle.getMinutes()==0) {
                                    actualFriend++;
                                    timeToChange=false;
                                    vibrateXTimes(3,350);
                                    if(actualFriend>=friends.length) {
                                        actualFriend=0;
                                    }
                                    tiTvInfo.setText("Momentan ist " + sequence[actualFriend] + " dran.");
                                }
                                tiTvSingle.setText("Restliche Zugzeit: "+timerSingle.getSingleTimeAsString());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        threadSingle.start();
    }
    private void showThatTimeToChange() {
        threadTimeToChange = new Thread() {

            @Override
            public void run() {
                try {
                    while (timeToChange) {
                        Thread.sleep(500);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(darkBackground) {
                                    tiLayoutMain.setBackgroundColor(Color.rgb(0,1,99));
                                    darkBackground = false;
                                } else {
                                    tiLayoutMain.setBackgroundColor(Color.rgb(0,1,56));
                                    darkBackground = true;
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        threadTimeToChange.start();
    }

    private void vibrateXTimes(int often, final int setTtime) {
        times = often;
        threadVibrator = new Thread() {

            @Override
            public void run() {
                try {
                    while (times>1) {
                        Thread.sleep(400);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vibrate(setTtime);
                                times--;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        threadVibrator.start();
    }

    public void playPositive() {
        threadsRunning=true;
        runTimeTotal();
        runTimeSingle();
        tiTvInfo.setText("Momentan ist " + sequence[actualFriend] + " dran.");
    }

    public void endPositiv() {
        dateEnd = setDate();
        saveToStatistics();
        showNotification=false;
        threadsRunning = false;
//        threadSingle.interrupt();
       // threadTotal.interrupt();
        //threadNotification.interrupt();
        getActivity().finish();
    }

    public void endNegative() {
        if(!firstStart) {
            btPause.setEnabled(true);
            btStart.setEnabled(false);
            threadsRunning=true;
            runTimeSingle();
        }
    }
    /*@Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Heute beginnt " + sequence[0] + "!");
                builder.setCancelable(false);
                builder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        threadsRunning=true;
                        runTimeTotal();
                        runTimeSingle();
                        tiTvInfo.setText("Momentan ist " + sequence[actualFriend] + " dran.");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Willst du wirklich gehen?");
                builder2.setCancelable(true);
                builder2.setPositiveButton("Ja und speichern!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateEnd = setDate();
                        saveToStatistics();
                        showNotification=false;
                        getActivity().finish();
                    }
                });
                builder2.setNegativeButton("Nein!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btPause.setEnabled(true);
                        btStart.setEnabled(false);
                        threadsRunning=true;
                        runTimeSingle();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
        }
        return super.onCreateDialog(id);
    }
*/
    private void saveToStatistics() {
        numOfSavedEntries++;
        editor.putInt("NUM_OF_SAVED_ENTRIES",numOfSavedEntries);
        String savedString="";
        //Statistic: Datum, Freunde, Gesamtzeit, Anzahl Kohledrehung
        savedString+= dateStart +"#";
        savedString+=friendsAsString+"#";
        savedString+=totalTime+"#";
        savedString+=numOfSwitchedCoal+"#";
        savedString+=dateEnd;
        editor.putString("STATISTIC_ENTRY_"+numOfSavedEntries,savedString);
        editor.commit();
    }

    public String setDate() {
        String date="";
        calendar.setTimeInMillis(System.currentTimeMillis());
        date += calendar.get(Calendar.DAY_OF_MONTH)+".";
        date += (calendar.get(Calendar.MONTH)+1)+".";
        date += calendar.get(Calendar.YEAR)+"  ";
        date += calendar.get(Calendar.HOUR_OF_DAY)+":";
        date += calendar.get(Calendar.MINUTE);
        return date;
    }

    // Vibrate for 500 milliseconds
    public void vibrate(int i) {
        vib.vibrate(i);
    }

    private void startNotificationThread() {
        notification.setSmallIcon(R.mipmap.button_shisha);
        notification.setWhen(System.currentTimeMillis());
        notification.setTicker(sequence[actualFriend] + " ist dran.");
        notification.setContentTitle(tiTvInfo.getText());
        notification.setContentText(timerSingle.getSingleTimeAsString() + " verbleibend");
        notification.setOngoing(true);
        notification.setContentIntent(((TimeTrackerActivity)getActivity()).myPendingIntent());

        manager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(uniqueID,notification.build());

        threadNotification = new Thread() {
            @Override
            public void run() {
                try {
                    while (notificationIsActive) {
                        Thread.sleep(500);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notification.setTicker(sequence[actualFriend] + " ist dran.");
                                notification.setContentTitle(tiTvInfo.getText());
                                notification.setContentText(timerSingle.getSingleTimeAsString() + " verbleibend");
                                manager.notify(uniqueID,notification.build());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        threadNotification.start();
    }

    public void myResume() {
        notification.setOngoing(false);
        if(notificationIsActive) {
            manager.cancelAll();
        }
        notificationIsActive=false;
    }

    public void myStop() {
        if(showNotification) {
            startNotificationThread();
            notificationIsActive = true;
        }
    }
}
