package de.dhkarlsruhe.it.sheeshapp.sheeshapp.session;

/**
 * Created by d0272129 on 05.05.18.
 */

public class Session {

    private String user_id;
    private String name;
    private String email;

    public Session(String user_id, String name, String email) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
    }
}
