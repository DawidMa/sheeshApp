package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

import java.util.Calendar;

/**
 * Created by d0272129 on 10.07.18.
 */

public class FriendRequestObject {

    private long unverified_relation_id;
    private long friend_id;
    private String friend_name;
    private boolean verified;
    private Calendar add_date;

    public long getUnverified_relation_id() {
        return unverified_relation_id;
    }

    public void setUnverified_relation_id(long unverified_relation_id) {
        this.unverified_relation_id = unverified_relation_id;
    }

    public long getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(long friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Calendar getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Calendar add_date) {
        this.add_date = add_date;
    }
}
