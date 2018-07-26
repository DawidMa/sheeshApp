package de.dhkarlsruhe.it.sheeshapp.sheeshapp.timer;

public class FloTimer {

    private boolean paused;
    private boolean autoReload;
    private boolean isRunning;
    private boolean countMode;
    private float time;
    private float startTime;
    private float resolution;
    private TimerCallback callback;
    private TimerCallback finished;
    private Thread thread;

    public FloTimer(float time){
        this.time = startTime = time;
        this.resolution = 1;
        this.countMode = true;
        this.autoReload = true;
    }

    public void start(){
        isRunning = true;

        thread = new Thread(new Runnable() {
            public void run() {
                while (isRunning) {
                    if(paused) continue;

                    if(callback != null){
                        callback.action();
                    } else {
                        isRunning = false;
                        System.err.println("Timer has no callback!");
                    }

                    if(countMode){
                        time -= resolution;
                        if(time <= 0) {
                            if(autoReload){
                                time = startTime;
                            } else {
                                isRunning = false;
                            }
                            if(finished!=null)finished.action();
                        }
                    } else {
                        time += resolution;
                    }
                    try {
                        Thread.sleep((long) (resolution*1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void stop(){
        isRunning = false;
        time = startTime;
    }

    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean getCountMode() {
        return countMode;
    }

    public void setCountMode(boolean countMode) {
        this.countMode = countMode;
    }

    public float getTime() {
        return time;
    }

    public long getTimeAsLong() {
        return (long)time*1000;
    }

    public void setTime(float time) {
        this.time = startTime = time;
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public TimerCallback getCallback() {
        return callback;
    }

    public void setCallback(TimerCallback callback) {
        this.callback = callback;
    }

    public void interrupt() {
        thread.interrupt();
    }

    public boolean isPaused() {
        return paused;
    }

    public abstract static class TimerCallback{
        public abstract void action();
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public void setFinishCallback(TimerCallback finished) {
        this.finished = finished;
    }

    public String getTimeAsString(){
        //int millis  = (int) (time - Math.floor(time));
        int seconds = (int) (time % 60);
        int minutes = (int) ((time / 60) % 60);
        int hours   = (int) (time / 3600);

        return String.format("%02d:%02d:%02d", hours,minutes,seconds);
    }
}
