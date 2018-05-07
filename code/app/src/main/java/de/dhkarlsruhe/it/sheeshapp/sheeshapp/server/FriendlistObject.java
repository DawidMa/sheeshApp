package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

/**
 * Created by d0272129 on 07.05.18.
 */

public class FriendlistObject {

    private long relation_id;
    private long friendid;
    private long last_session_id;
    private int total_sessions;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(long relation_id) {
        this.relation_id = relation_id;
    }

    public long getFriendid() {
        return friendid;
    }

    public void setFriendid(long friendid) {
        this.friendid = friendid;
    }

    public long getLast_session_id() {
        return last_session_id;
    }

    public void setLast_session_id(long last_session_id) {
        this.last_session_id = last_session_id;
    }

    public int getTotal_sessions() {
        return total_sessions;
    }

    public void setTotal_sessions(int total_sessions) {
        this.total_sessions = total_sessions;
    }
}
