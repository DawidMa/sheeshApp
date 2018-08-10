package de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities;

import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;

/**
 * Created by d0272129 on 10.08.18.
 */

public class MyUtilities {

    public static String getChooseFriendsAsString(List<ChooseFriendObject> list) {
        String ok = "";
        for(int i = 0; i < list.size(); i++) {
            if(i==list.size()-1) {
                ok+=(list.get(i).getName()+".");
            } else {
                ok+=(list.get(i).getName()+", ");
            }
        }
        return ok;
    }
}
