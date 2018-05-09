package de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete;

/**
 * Created by d0272129 on 09.05.18.
 */

public class UserSearchObject {

    private String name;
    private String email;

    public UserSearchObject(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
