package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.circle.CircleAnimation;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.circle.MyCircle;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.history.History;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.timer.FloTimer;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by Informatik on 28.11.2017.
 */

public class TimeTrackerFragmentGuest extends android.support.v4.app.Fragment {

    private Random rnd;
    private ConstraintLayout tiLayoutMain;
    private Vibrator vib;
    private FloTimer timerTotal1, timerSingle1, timerNextPlayer, timerFlash;

    private TextView tiTvChoosenFriends,tiTvTotal,tiTvSingle, tiTvInfo, tiTvTopTitle;

    private FloatingActionButton btStart, btPause, btEnd;

    private List<ChooseFriendObject> sequence = new ArrayList<>();

    public static String firstFriend;

    private Guest guest;

    private Thread threadVibrator;

    private boolean firstStart = true;
    private boolean timeToChange=false;
    private boolean darkBackground = true;

    private int actualFriend=0;
    private int numOfSwitchedCoal=0;
    private int times;

    private Calendar calendar = Calendar.getInstance();

    //Notification Setup
    private NotificationCompat.Builder notification;
    private static final int uniqueID = 111;
    private boolean showNotification = true;
    private boolean notificationIsActive = false;
    private NotificationManagerCompat manager;
    private View v;
    private List<History> histories = new ArrayList<>();
    private CircleAnimation circleAnimation;
    private MyCircle circle;
    private MyCircle circleGray;
    private String myChannelId = "DawidsChannel";
    private Window window;
    private Gson gson = new Gson();

    private MediaPlayer soundTimeUp;
    private AdView adView;
    private Button btnChange;



    public TimeTrackerFragmentGuest() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_tracker, container, false);
        v= rootView;
        btnChange = v.findViewById(R.id.btnTrackChange);
        btnChange.setVisibility(View.GONE);
        guest = new Guest(getActivity());
        init();
        MobileAds.initialize(getContext(), MyUtilities.AD_APP_ID);
        adView = v.findViewById(R.id.adTracker);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        sequence = guest.getFriends();
        sequence.add(new ChooseFriendObject(guest.getName(),-sequence.size()));
        Collections.shuffle(sequence);
        printFriendsList(sequence);
        firstFriend = sequence.get(0).getName();
        circle = rootView.findViewById(R.id.animatedCircle);
        circle.setColor("#00ff00");
        circleGray = rootView.findViewById(R.id.grayCircle);
        circleGray.setAngle(360);
        circleGray.setColor("#383838");
        circleAnimation = new CircleAnimation(circle, 360);
        circleAnimation.setDuration(1000);
        circleAnimation.setRepeatMode(Animation.INFINITE);
        circle.startAnimation(circleAnimation);
        return rootView;
    }

    private void init() {
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
        timerSingle1 = new FloTimer(guest.getTimeInSeconds());
        timerSingle1.setResolution(0.01f);
        timerTotal1 = new FloTimer(0);
        timerTotal1.setCountMode(false);
        timerNextPlayer = new FloTimer(5);
        timerNextPlayer.setResolution(0.01f);
        timerFlash = new FloTimer(3);
        timerFlash.setResolution(0.5f);
        tiTvTotal.setText("Gesamtzeit: "+timerTotal1.getTimeAsString());
        tiTvSingle.setText(timerSingle1.getTimeAsString());
        rnd = new Random();
        //Notification
        createNotificationChannel();
        notification = new NotificationCompat.Builder(this.getActivity(),myChannelId);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.icon_setup_white);
        notification.setOngoing(true);
        notification.setContentIntent(((TimeTrackerActivityGuest)getActivity()).myPendingIntent());
        notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        manager = NotificationManagerCompat.from(getActivity());
        window = getActivity().getWindow();
        soundTimeUp = MediaPlayer.create(getContext(), R.raw.time_up_sound);
        soundTimeUp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying(mp);
                soundTimeUp = MediaPlayer.create(getContext(), R.raw.time_up_sound);
            }
        });
    }

    public void fragmentPressedStart() {
        btPause.setEnabled(true);
        btStart.setEnabled(false);
        if(firstStart) {
            firstStart = false;
            getActivity().showDialog(1);
        } else {
            timerSingle1.resume();
            if (timerFlash.isPaused()) {
                timerFlash.resume();
            }
        }
        tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend).getName() + " dran.");
    }

    public void fragmentPressedPause() {
        btPause.setEnabled(false);
        btStart.setEnabled(true);
        timerSingle1.pause();
        if (timerFlash.isRunning()) {
            timerFlash.pause();
        }
        randomColeChanger();
        numOfSwitchedCoal++;
    }

    public void fragmentPressedEnd() {
        getActivity().showDialog(2);
    }

    private void printFriendsList(List<ChooseFriendObject> sequence) {
        tiTvChoosenFriends.setText("");
        for(int i = 0; i < sequence.size(); i++) {
            if(i==sequence.size()-1) {
                tiTvChoosenFriends.append(sequence.get(i).getName()+".");
            } else {
                tiTvChoosenFriends.append(sequence.get(i).getName()+", ");
            }
        }
    }

    private void randomColeChanger() {
        int numFriends = sequence.size();
        tiTvInfo.setText(sequence.get(rnd.nextInt(numFriends)).getName() + " muss die Kohle drehen!");
    }

    @Override
    public void onDetach() {
        try {
            timerTotal1.interrupt();
            timerSingle1.interrupt();
            timerNextPlayer.interrupt();
        } catch (Exception e) {

        }

        super.onDetach();
    }

    private void runTimeTotal() {
        timerTotal1.setCallback(new FloTimer.TimerCallback() {
            public void action() {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tiTvTotal.setText("Gesamtzeit: " + timerTotal1.getTimeAsString());
                        }
                    });
                }
            }
        });
        timerTotal1.start();
    }

    private void runTimeSingle() {
        final int standardTime = (int)guest.getTimeInSeconds();
        circleAnimation.setDuration(100);
        timerSingle1.setCallback(new FloTimer.TimerCallback() {
            @Override
            public void action() {
                if(!timeToChange&&timerSingle1.getTime()<=3) {
                    timeToChange = true;
                    soundTimeUp.start();
                    if (timerFlash.isPaused()) {
                        timerFlash.resume();
                    } else {
                        runTimeFlash();
                    }
                }
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tiTvSingle.setText(timerSingle1.getTimeAsString());
                            float angle = (timerSingle1.getTime() / standardTime) * 360f;
                            if (notificationIsActive) {
                                startNotificationThread();
                            }
                            circle.setColor(calculateProgressColor(angle));
                            circle.setAngle(angle);
                            circle.invalidate();
                        }
                    });
                }

            }
        });
        timerSingle1.setFinishCallback(new FloTimer.TimerCallback() {
            @Override
            public void action() {
                timerSingle1.pause();
                actualFriend++;
                timeToChange=false;
                vibrateXTimes(3,350);
                if(actualFriend>=sequence.size()) {
                    actualFriend=0;
                }
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tiTvInfo.setText("Übergabe an " + sequence.get(actualFriend).getName());
                            btPause.setEnabled(false);
                        }
                    });
                }
                if (timerNextPlayer.isPaused()) {
                    timerNextPlayer.resume();
                } else {
                    runTimeNextplayer();
                }
            }
        });
        timerSingle1.start();
    }

    private void runTimeFlash() {
        timerFlash.setCallback(new FloTimer.TimerCallback() {
            @Override
            public void action() {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (darkBackground) {
                                tiLayoutMain.setBackgroundColor(Color.rgb(0, 1, 99));
                                window.setStatusBarColor(Color.rgb(0, 1, 99));
                                darkBackground = false;
                            } else {
                                tiLayoutMain.setBackgroundColor(Color.rgb(0, 1, 56));
                                window.setStatusBarColor(Color.rgb(0, 1, 56));
                                darkBackground = true;
                            }
                        }
                    });
                }
            }
        });
        timerFlash.setFinishCallback(new FloTimer.TimerCallback() {
            @Override
            public void action() {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tiLayoutMain.setBackgroundColor(Color.rgb(0, 0, 0));
                            window.setStatusBarColor(Color.rgb(0, 0, 0));
                        }
                    });
                }
                timerFlash.pause();
            }
        });
        timerFlash.start();
    }

    private void runTimeNextplayer() {
        timerNextPlayer.setCallback(new FloTimer.TimerCallback() {
            @Override
            public void action() {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tiTvSingle.setText(timerNextPlayer.getTimeAsString());
                            float angle = ((5-timerNextPlayer.getTime()) / 5) * 360f;
                            circle.setColor(calculateProgressColor(angle));
                            circle.setAngle(angle);
                            circle.invalidate();
                            if (notificationIsActive) {
                                startNotificationThread();
                            }
                        }
                    });
                }
            }
        });
        timerNextPlayer.setFinishCallback(new FloTimer.TimerCallback() {
            @Override
            public void action() {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tiLayoutMain.setBackgroundColor(Color.rgb(0, 1, 56));
                            window.setStatusBarColor(Color.rgb(0, 1, 56));
                            tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend).getName() + " dran.");
                            btPause.setEnabled(true);
                        }
                    });
                }
                timerNextPlayer.pause();
                timerSingle1.resume();
            }
        });
        timerNextPlayer.start();
    }

    private String calculateProgressColor(float angle) {
        String color = "";
        if (angle>=180) {
            int value = (int)(((360f-angle)/180f)*255);
            color = String.format("#%02x%02x%02x", value, 255, 0);
        } else if (angle>=0) {
            int value = (int)(((angle)/180f)*255);
            color = String.format("#%02x%02x%02x", 255, value, 0);
        }
        return color;
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
        runTimeTotal();
        runTimeSingle();
        //tiTvInfo.setText("Momentan ist " + sequence.get(actualFriend).getName() + " dran.");
    }

    public void endPositiv() {
        timerNextPlayer.pause();
        timerTotal1.pause();
        timerFlash.pause();
        showNotification=false;
        try {
            stopPlaying(soundTimeUp);
        } catch (Exception e) {}
        getActivity().finish();
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "CHANNEL_DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(myChannelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void startNotificationThread() {
        notification.setWhen(System.currentTimeMillis());
        notification.setTicker(tiTvInfo.getText());
        notification.setContentTitle(tiTvInfo.getText());
        notification.setContentText(tiTvSingle.getText());
        manager.notify(uniqueID,notification.build());
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
            notificationIsActive = true;
        }
    }

    public void endNeutral() {
        showNotification=false;
        stopPlaying(soundTimeUp);
        getActivity().finish();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception

    }

    private void stopPlaying(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}
