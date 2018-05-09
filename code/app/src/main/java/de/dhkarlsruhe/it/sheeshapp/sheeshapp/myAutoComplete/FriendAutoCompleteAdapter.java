package de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myvolley.VolleyCallback;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;

/**
 * Created by d0272129 on 09.05.18.
 */

public class FriendAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static Context context;
    private List<UserSearchObject> resultList = new ArrayList<>();
    private Gson json = new Gson();
    private static FriendAutoCompleteAdapter instance;
    private RequestQueue mRequestQueue;


    public FriendAutoCompleteAdapter(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized FriendAutoCompleteAdapter getInstance() {
        if (instance == null) {
            instance = new FriendAutoCompleteAdapter(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.dropdownTitle)).setText(getItem(position).getName());
        ((TextView) convertView.findViewById(R.id.dropdownText)).setText(getItem(position).getEmail());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            FilterResults filterResults = new FilterResults();
            Type type = new TypeToken<List<UserSearchObject>>(){}.getType();
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    getResponse(ServerConstants.URL + "user/name?searchFor=" + constraint.toString(), new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            List<UserSearchObject> users;
                            users = json.fromJson(result,type);
                            filterResults.values = users;
                            filterResults.count = users.size();
                        }
                    });
                }
                return filterResults;
            }



            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<UserSearchObject>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    public void getResponse(String url, final VolleyCallback callback) {
        mRequestQueue = getInstance().getRequestQueue();
        StringRequest request = new StringRequest(url, new Response.Listener < String > () {
            @Override
            public void onResponse(String Response) {
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
