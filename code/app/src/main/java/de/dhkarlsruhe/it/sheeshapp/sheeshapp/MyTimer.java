package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

/**
 * Created by Informatik on 28.11.2017.
 */

public class MyTimer {
    int time, hours=0,minutes=0, seconds=0;
    boolean forward;

    public MyTimer(int time, boolean forward) {
        this.time = time;
        this.forward = forward;
        if(!forward) {
            seconds=time%60;
            minutes=time/60;
            hours=minutes/60;
        }
    }

    public String getTotalTimeAsString() {
        String timeAsString;
        timeAsString = String.format("%s:%s:%s",hours<10?"0"+hours:""+hours,minutes<10?"0"+minutes:""+minutes,seconds<10?"0"+seconds:""+seconds);
        return timeAsString;
    }
    public String getSingleTimeAsString() {
        String timeAsString;
        timeAsString = String.format("%s:%s",minutes<10?"0"+minutes:""+minutes,seconds<10?"0"+seconds:""+seconds);
        return timeAsString;
    }

    public int getSeconds() {
        return seconds;
    }
    public int getMinutes() {
        return minutes;
    }

    public void incSeconds() {
        seconds++;
        if(seconds == 60) {
            seconds = 0;
            minutes++;
        }
        if(minutes == 60) {
            minutes = 0;
            hours++;
        }
    }
    public void decSeconds() {
        seconds--;
        if(seconds<0) {
            minutes--;
            seconds = 59;
            if(minutes<0) {
                minutes=time/60;
                seconds=time%60;
            }
        }
    }
}

