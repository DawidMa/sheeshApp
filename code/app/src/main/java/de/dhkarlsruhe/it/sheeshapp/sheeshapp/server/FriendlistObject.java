package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

/**
 * Created by d0272129 on 07.05.18.
 */

public class FriendlistObject {

    private String name;
    private long relation_id;
    private long friend_id;
    private long last_session_id;
    private long total_sessions;

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

    public long getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(long friend_id) {
        this.friend_id = friend_id;
    }

    public long getLast_session_id() {
        return last_session_id;
    }

    public void setLast_session_id(long last_session_id) {
        this.last_session_id = last_session_id;
    }

    public long getTotal_sessions() {
        return total_sessions;
    }

    public void setTotal_sessions(long total_sessions) {
        this.total_sessions = total_sessions;
    }
}
