package de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MainActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myvolley.VolleyCallback;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by d0272129 on 09.05.18.
 */

public class FriendAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static Activity context;
    private List<UserSearchObject> resultList = new ArrayList<>();
    private Gson json = new Gson();
    private static FriendAutoCompleteAdapter instance;
    private RequestQueue mRequestQueue;
    private Friend friend;
    private String myResult;
    private static AlertDialog dialog;
    private UserSessionObject session;
    List<UserSearchObject> users;
    Type type;

    public FriendAutoCompleteAdapter(Activity context, AlertDialog dialog) {
        this.context = context;
        mRequestQueue = getRequestQueue();
        friend = new Friend(this.context);
        session = new UserSessionObject(context);
        this.dialog = dialog;
    }

    public static synchronized FriendAutoCompleteAdapter getInstance() {
        if (instance == null) {
            instance = new FriendAutoCompleteAdapter(context, dialog);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public UserSearchObject getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.dropdownTitle)).setText(getItem(position).getName());
        ((TextView) convertView.findViewById(R.id.dropdownText)).setText(getItem(position).getEmail());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(position).getEmail().equals(session.getEmail())) {
                    Snackbar.make(dialog.getCurrentFocus(), R.string.cant_be_your_own_friend,Snackbar.LENGTH_LONG).show();
                } else {
                    friend.addFriend(getItem(position).getEmail(),getItem(position).getName(),dialog);
                }
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            FilterResults filterResults = new FilterResults();
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint!=null) {
                    String string = constraint.toString();
                    if (!string.contains(" ")) {
                        type = new TypeToken<List<UserSearchObject>>() {}.getType();
                        getResponse(ServerConstants.URL + "user/name?searchFor=" + constraint.toString(), new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                myResult = result;
                                users = json.fromJson(myResult, type);
                                filterResults.values = users;
                                filterResults.count = users.size();
                                resultList = users;
                                if (filterResults != null && filterResults.count > 0) {
                                    notifyDataSetChanged();
                                } else {
                                    notifyDataSetInvalidated();
                                }                            }
                        });
                    }
                }
                return filterResults;
            }
                @Override
                protected void publishResults (CharSequence constraint, FilterResults results){

                }
            };
    }

    public void getResponse(String url, final VolleyCallback callback) {
        mRequestQueue = getInstance().getRequestQueue();
        StringRequest request = new StringRequest(url, new Response.Listener < String > () {
            @Override
            public void onResponse(String Response) {
                //Toast.makeText(context,Response,Toast.LENGTH_LONG).show();
                callback.onSuccessResponse(Response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show();
            }
        });
        getInstance().addToRequestQueue(request);
    }
}
