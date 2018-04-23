package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Informatik on 28.11.2017.
 */

public class TimeTrackerFragment extends android.support.v4.app.Fragment {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Random rnd;
    private ConstraintLayout tiLayoutMain;
    private Vibrator vib;
    private MyTimer timerTotal, timerSingle;

    private TextView tiTvChoosenFriends,tiTvTotal,tiTvSingle, tiTvInfo, tiTvTopTitle;

    private FloatingActionButton btStart, btPause, btEnd;

    private String[] friends;

    private List<String> sequence = new ArrayList<>();

    static String firstFriend;
    private String friendsAsString="", dateStart ="", dateEnd="",totalTime;


    private Thread threadTotal, threadSingle, threadTimeToChange, threadVibrator;

    private boolean threadsRunning=false;
    private boolean firstStart = true;
    private boolean timeToChange=false;
    private boolean darkBackground = true;

    private int actualFriend=0;
    private int numOfSwitchedCoal=0;
    private int times;

    private Calendar calendar = Calendar.getInstance();

    //Notification Setup
    private NotificationCompat.Builder notification;
    private static final int uniqueID = 123456;
    private boolean showNotification = true;
    private boolean notificationIsActive = false;
    private NotificationManager manager;
    private Thread threadNotification;

    private View v;

    private History history;

    public TimeTrackerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_tracker, container, false);
        v= rootView;
        init();
        sequence = getChoosenFriendsAsList();
        printFriendsList(sequence);
        Collections.shuffle(sequence);
        //printFriends(friends);
        //sequence = setNewSequence();
        firstFriend = sequence.get(0);
        return rootView;
    }

    private List<String> setNewSequence() {
        String[] newOrder = new String[friends.length];
        List<String> listOrder = new ArrayList<>();
        for (int i = 0; i < newOrder.length; i++) {
            newOrder[i] = friends[i];
        }
        Collections.shuffle(Arrays.asList(newOrder));
        for (int i=0; i<newOrder.length; i++) {
            listOrder.add(newOrder[i]);
        }
        return listOrder;

    }

    private void init() {
        pref = this.getActivity().getSharedPreferences("com.preferences.sheeshapp", 0);
        editor = pref.edit();
        history = new History(getContext());
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
        rnd = new Random();
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
        tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend) + " dran.");
    }

    public static String getFirstFriend() {
        return firstFriend;
    }

    public void fragmentPressedPause(View view) {
        btPause.setEnabled(false);
        btStart.setEnabled(true);
        threadsRunning=false;
       // deleteOneFriend();
        randomColeChanger();
        numOfSwitchedCoal++;
    }

    private void deleteOneFriend() {
        if (sequence.size()>1) {
            int randomInt = rnd.nextInt(sequence.size());
            System.out.println(sequence.get(randomInt));
            sequence.remove(randomInt);
            if (sequence.size()<actualFriend) {
                actualFriend = 0;
            }
            printFriendsList(sequence);
        } else {
            Toast.makeText(getActivity(),"Not enough friends", Toast.LENGTH_SHORT).show();
        }

    }

    private void printFriendsList(List<String> sequence) {
        tiTvChoosenFriends.setText("");
        for(int i = 0; i < sequence.size(); i++) {
            if(i==sequence.size()-1) {
                tiTvChoosenFriends.append(sequence.get(i)+".");
            } else {
                tiTvChoosenFriends.append(sequence.get(i)+", ");
            }
        }
    }

    private void randomColeChanger() {
        int numFriends = sequence.size();
        tiTvInfo.setText(sequence.get(rnd.nextInt(numFriends)) + " muss die Kohle drehen!");
    }

    public void fragmentPressedEnd(View view) {
        btPause.setEnabled(false);
        btStart.setEnabled(true);
        threadsRunning=false;
        getActivity().showDialog(2);
        totalTime=timerTotal.getTotalTimeAsString();
    }
    private void printFriends(String[] str) {
        for(int i = 0; i < str.length; i++) {
            if(i==str.length-1) {
                tiTvChoosenFriends.append(str[i]+".");
                friendsAsString+=(str[i]+".");
            } else {
                tiTvChoosenFriends.append(str[i]+", ");
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

    private List<String> getChoosenFriendsAsList() {
        int numberFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        System.out.println("Friends:"+numberFriends);
        List<String> choosenFriends = new ArrayList<>();
        for (int i=1 ;i<=numberFriends; i++) {
            boolean choosen = pref.getBoolean("FRIEND_CHOOSEN_"+i,false);
            if(choosen) {
                choosenFriends.add(pref.getString("FRIEND_"+i,"FEHLER"));
            }
        }
        choosenFriends.add(pref.getString("savedUsername","noUser"));
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
                                    if(actualFriend>=sequence.size()) {
                                        actualFriend=0;
                                    }
                                    tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend) + " dran.");
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
                        if (getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (darkBackground) {
                                        tiLayoutMain.setBackgroundColor(Color.rgb(0, 1, 99));
                                        darkBackground = false;
                                    } else {
                                        tiLayoutMain.setBackgroundColor(Color.rgb(0, 1, 56));
                                        darkBackground = true;
                                    }
                                }
                            });
                        }
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
                        if (getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    vibrate(setTtime);
                                    times--;
                                }
                            });
                        }
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
        tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend) + " dran.");
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
                        tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend) + " dran.");
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
        int numOfSavedEntries = history.getNumOfSavedEntries();
        numOfSavedEntries++;
        editor.putInt("NUM_OF_SAVED_ENTRIES",numOfSavedEntries);
        String savedString="";
        //Statistic: Datum, Freunde, Gesamtzeit, Anzahl Kohledrehung
        savedString+=dateStart +"#";
        savedString+="Friends"+friendsAsString+"#";
        savedString+="Total time: "+totalTime+"#";
        if (numOfSwitchedCoal>0) {
            savedString+="Coal: "+numOfSwitchedCoal+"#";
        }
        savedString+="End: " +dateEnd+"#";
        savedString+="END";
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
        notification.setTicker(sequence.get(actualFriend) + " ist dran.");
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
                                notification.setTicker(sequence.get(actualFriend) + " ist dran.");
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
