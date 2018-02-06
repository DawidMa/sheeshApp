package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Informatik on 01.12.2017.
 */

public class Friend  {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Random rnd = new Random();

    public Friend(Context context) {
        pref = context.getSharedPreferences("com.preferences.sheeshapp",0);
        editor = pref.edit();
    }

    public boolean checkFriend(String newFriend) {
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        boolean accepted = true;
        if(numOfFriends>0) {
            for(int i=1; i<=numOfFriends; i++) {
                String name = pref.getString("FRIEND_"+i,"FEHLER");
                if(name.equals(newFriend)) {
                    return false;
                }
            }
        }
        if (pref.getString("savedUsername","errorUser").equals(newFriend)) {
            return false;
        }
        return accepted;
    }

    public void addFriend(String newFriend) {
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        numOfFriends++;
        editor.putInt("NUMBER_OF_FRIENDS",numOfFriends);
        editor.putString("FRIEND_"+numOfFriends,newFriend);
        editor.putInt("FRIENDS_NUM_SHISHAS_"+numOfFriends,0);
        editor.commit();
    }

    public String[] getFriends() {
        String[] friends = new String[pref.getInt("NUMBER_OF_FRIENDS",0)];
        for(int i=1; i<=friends.length; i++) {
            friends[i-1] = pref.getString("FRIEND_"+i,"Fehler");
        }
        return friends;
    }

    public void deleteFriend(int v) {
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        int numberToMove = numOfFriends - v;
        if(numberToMove>0) {
            for(int i=0; i<numberToMove;i++) {

                int moveFrom = v+i+1;
                int moveTo = v+i;

                if(moveFrom<=numOfFriends) {
                    editor.putString("FRIEND_" + moveTo, pref.getString("FRIEND_" + moveFrom, "Fehler bei überschreiben"));
                    editor.putBoolean("FRIEND_CHOOSEN_"+moveTo, pref.getBoolean("FRIEND_CHOOSEN_"+moveFrom,false));
                    editor.putInt("FRIENDS_NUM_SHISHAS_"+moveTo, pref.getInt("FRIENDS_NUM_SHISHAS_"+moveFrom,0));
                    editor.commit();
                }
            }
        }
        editor.putInt("NUMBER_OF_FRIENDS", numOfFriends - 1);
        editor.commit();
        editor.remove("FRIEND_" + numOfFriends);
        editor.remove("FRIEND_CHOOSEN_"+numOfFriends);
        editor.remove("FRIENDS_NUM_SHISHAS_"+numOfFriends);
        editor.commit();
    }

    public boolean getChecked(String tag) {
        return pref.getBoolean("FRIEND_CHOOSEN_"+tag,false);
    }
    public void setChecked(String tag, boolean b) {
        editor.putBoolean("FRIEND_CHOOSEN_"+tag,b);
        editor.commit();
    }

    public int getNumberOfFriends() {
        return pref.getInt("NUMBER_OF_FRIENDS", 0);
    }
    public int getNumberOfShishasWithFriend(int i) {
        return pref.getInt("FRIENDS_NUM_SHISHAS_"+i,2);
    }

    public String getRandomDrawable() {

        String imageName;
        int randomInt = rnd.nextInt(3);
        if (randomInt ==0 ) {
            imageName = "user_avatar";
        } else if (randomInt == 1){
            imageName = "logo_splash";
        } else {
            imageName = "ic_info";
        }
        return imageName;
    }

    public String[] getAlphabeticalFriends() {
        String[] sorted = getFriends();
        Arrays.sort(sorted);
        return sorted;
    }
}
