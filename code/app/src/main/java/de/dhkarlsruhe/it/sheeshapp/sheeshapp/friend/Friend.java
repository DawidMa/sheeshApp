package de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.Random;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by Informatik on 01.12.2017.
 */

public class Friend  {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Random rnd = new Random();
    private boolean sorted;
    private Context c;
    private UserSessionObject session;

    public Friend(Context context) {
        pref = context.getSharedPreferences("com.preferences.sheeshapp",0);
        editor = pref.edit();
        sorted = pref.getBoolean("SORTED_FRIEND_LIST",false);
        this.c = context;
        session = new UserSessionObject(c);
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
        /*
        int numOfFriends = pref.getInt("NUMBER_OF_FRIENDS",0);
        numOfFriends++;
        editor.putInt("NUMBER_OF_FRIENDS",numOfFriends);
        editor.putString("FRIEND_"+numOfFriends,newFriend);
        editor.putInt("FRIENDS_NUM_SHISHAS_"+numOfFriends,0);
        editor.commit();
        */
        long id = session.getUser_id();
        StringRequest request =  new StringRequest(ServerConstants.URL_ADD_FRIEND+id+"&friendmail="+newFriend, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                positiveResponse(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                negativeResponse(volleyError.getMessage());
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
    }

    private void negativeResponse(String message) {
        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
    }

    private void positiveResponse(String string) {
        Toast.makeText(c, string,Toast.LENGTH_SHORT).show();
    }

    public String[] getFriends() {
        String[] friends = new String[pref.getInt("NUMBER_OF_FRIENDS",0)];
        for(int i=1; i<=friends.length; i++) {
            friends[i-1] = pref.getString("FRIEND_"+i,"Fehler");
        }
        if (sorted) {
            Arrays.sort(friends);
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

    public void deleteFriendOnline(long friendid) {
        long id = session.getUser_id();
        StringRequest request =  new StringRequest(ServerConstants.URL_DELETE_FRIEND+id+"&friendid="+friendid, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                String response;
                if (string.equals("OK")) {
                    response = "Friend deleted";
                } else {
                    response = string;
                }
                Toast.makeText(c,response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                negativeResponse(volleyError.getMessage());
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
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

    public void switchSorted() {
        if (!sorted) {
            sorted = true;
        } else {
            sorted = false;
        }
        editor.putBoolean("SORTED_FRIEND_LIST",sorted);
        editor.commit();
    }
}
