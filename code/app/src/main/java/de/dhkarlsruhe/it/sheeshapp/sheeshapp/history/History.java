package de.dhkarlsruhe.it.sheeshapp.sheeshapp.history;

import java.util.Calendar;

/**
 * Created by d0272129 on 24.07.18.
 */

public class History {

    private long historyId;
    private long userId;
    private String sessionName;
    private String location;
    private String date;
    private long duration;
    private String participants;
    private long totalShishas;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public void setTotalShishas(long totalShishas) {
        this.totalShishas = totalShishas;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public long getDuration() {
        return duration;
    }

    public String getParticipants() {
        return participants;
    }

    public long getTotalShishas() {
        return totalShishas;
    }

    public long getHistoryId() {
        return historyId;
    }
}
