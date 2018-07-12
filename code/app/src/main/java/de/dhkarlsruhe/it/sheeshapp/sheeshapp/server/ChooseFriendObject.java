package de.dhkarlsruhe.it.sheeshapp.sheeshapp.server;

/**
 * Created by d0272129 on 11.07.18.
 */

public class ChooseFriendObject {

    private String name;
    private long id;

    public ChooseFriendObject(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
