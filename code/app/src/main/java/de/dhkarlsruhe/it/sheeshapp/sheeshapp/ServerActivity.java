package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by d0272129 on 24.04.18.
 */

public class ServerActivity extends AppCompatActivity {
    TextView id;
    TextView name;
    String url = "http://sheeshapp.it.dh-karlsruhe.de:8080/server-0.0.1-SNAPSHOT/getUser";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        id = findViewById(R.id.tvServId);
        name = findViewById(R.id.tvServName);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(ServerActivity.this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            id.setText(jsonString);
            /*
            JSONArray fruitsArray = object.getJSONArray("fruits");
            ArrayList al = new ArrayList();

            for(int i = 0; i < fruitsArray.length(); ++i) {
                al.add(fruitsArray.getString(i));
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
            fruitsList.setAdapter(adapter);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
}
