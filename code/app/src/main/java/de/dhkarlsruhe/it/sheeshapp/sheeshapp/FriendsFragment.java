package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.Profile;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.ProfileActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Informatik on 23.11.2017.
 */

public class FriendsFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private ListView list;
    private List<String> names = new ArrayList<>();
    private List<String> valueShishas = new ArrayList<>();
    private TextView frTvNoFriends;
    private Friend friend;
    private ImageView friendImage;
    private MyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<FriendlistObject> friendlistObject;
    private Gson json = new Gson();
    private UserSessionObject session;
    private int numOfFriends = 0;
    private Context context;
    private Profile profile;
    private ImageHelper imageHelper;
    private Handler handler;
    private ProgressDialog progressDialog;
    private boolean permissionGranted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        initElementts();
        return rootView;
    }

    private void initElementts() {
        context = this.getActivity();
        friend = new Friend(context);
        profile = new Profile(context);
        session = new UserSessionObject(context);
        imageHelper = new ImageHelper(context);

        frTvNoFriends = rootView.findViewById(R.id.tvFragFriInfo);
        friendImage = rootView.findViewById(R.id.liFriendImage);
        list = rootView.findViewById(R.id.lvFragFriList);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeFragFri);

        swipeRefreshLayout.setColorSchemeColors(Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFriendlist();
            }
        });
        checkAmountFriends();
        loadFriendInformation();
        checkPermission();

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
        } else {
            checkPermission();
        }
    }

    private void checkAmountFriends() {
        if (friendlistObject != null) {
            numOfFriends = friendlistObject.size();
        } else
            numOfFriends = 0;
        if (numOfFriends > 0) {
            frTvNoFriends.setVisibility(View.GONE);
        } else {
            frTvNoFriends.setVisibility(View.VISIBLE);
        }
    }

    public void reloadListView() {
        names.clear();
        valueShishas.clear();
        if (friendlistObject != null) {
            for (FriendlistObject rels : friendlistObject) {
                names.add(rels.getName() + "");
                valueShishas.add(rels.getFriend_id() + "");
            }
            prepareProgressDialog();
            adapter = new MyAdapter(context, names, valueShishas, friendImage);
            list.setAdapter(adapter);
        } else {
            numOfFriends = 0;
        }
        checkAmountFriends();
    }

    private void prepareProgressDialog() {
        handler = new Handler();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    public void updateFriendlist() {
        loadFriendInformation();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadFriendInformation() {
        swipeRefreshLayout.setRefreshing(true);
        long id = session.getUser_id();
        StringRequest request = new StringRequest(ServerConstants.URL_RELATIONS + id, new Response.Listener<String>() {
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
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    private void negativeResponse(String message) {
        String offlineData = friend.getOfflineData();
        if (!offlineData.equals("empty") && friend.actualInformationAvailable()) {
            Type listType = new TypeToken<List<FriendlistObject>>() {}.getType();
            friendlistObject = json.fromJson(friend.getOfflineData(),listType);
            numOfFriends = friendlistObject.size();
        } else {
            friendlistObject.clear();
        }
        reloadListView();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void positiveResponse(String string) {
        //parse downloaded data
        Type listType = new TypeToken<List<FriendlistObject>>() {}.getType();
        friendlistObject = json.fromJson(string,listType);
        numOfFriends = friendlistObject.size();
        reloadListView();
        //Save for offline use
        friend.saveOfflineInformation(string);
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        //reloadListView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * START OF MyAdapter
     */
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> names;
        List<String> descriptions;
        ImageView image;

        MyAdapter(Context c, List<String> names, List<String> descriptions, ImageView image) {
            //Deleted R.id.lichoosefriend in super()
            super(c, R.layout.row_friends, R.id.liFriendName, names);
            this.context = c;
            this.names = names;
            this.descriptions = descriptions;
            this.image = image;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_friends, parent, false);
            TextView tvTitle = (TextView) row.findViewById(R.id.liFriendName);
            TextView tvDescription = (TextView) row.findViewById(R.id.liFriendNumOfShishas);
            final ImageView imgFriends = row.findViewById(R.id.liFriendImage);
            tvTitle.setText(names.get(position));
            tvDescription.setText(valueShishas.get(position));
            loadRoundedImage(imgFriends, position);
            imgFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFileFromServer(friendlistObject.get(position).getFriend_id() + "", imgFriends);
                }
            });

            Button button = (Button) row.findViewById(R.id.liDeleteFriend);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFriend(position);
                }

            });
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFriendsProfile(position);
                }
            });
            return row;
        }

        private void loadRoundedImage(ImageView view, int id) {
            final String user_id = friendlistObject.get(id).getFriend_id() + "";
            Bitmap bitmap = imageHelper.loadImageFromStorage(user_id);
            if (1 == 2) {
                Bitmap thumbnail = imageHelper.getThumbnailOfBitmap(bitmap, 200, 200);
                Glide.with(context).load(thumbnail).apply(RequestOptions.circleCropTransform()).into(view);
            } else {
                if (view != null) {
                    Glide.with(context).load(R.drawable.sheeshopa).apply(RequestOptions.circleCropTransform()).into(view);
                }
            }
        }
    }

    private void openFriendsProfile(int i) {
        Intent intent = new Intent(context, ProfileActivity.class);
        profile.setProfile(friendlistObject.get(i));
        startActivity(intent);
    }

    private void loadFileFromServer(final String userid, final ImageView imageView) {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(ServerConstants.URL_DOWNLOAD + userid + ".png").build();
                okhttp3.Response response = null;
                try {
                    response = client.newCall(request).execute();
                    //String fileName = response.body().toString();
                    float fileSize = response.body().contentLength();
                    BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());

                    OutputStream outputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Download/" + userid + ".png");
                    byte[] data = new byte[8192];
                    float total = 0;
                    int readedBytes = 0;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });

                    while ((readedBytes = inputStream.read(data)) != -1) {
                        total = total + readedBytes;
                        outputStream.write(data, 0, readedBytes);
                        progressDialog.setProgress((int) ((total / fileSize) * 100));
                    }
                    progressDialog.dismiss();
                    outputStream.flush();
                    outputStream.close();
                    response.body().close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showLoadedFile(imageView, Environment.getExternalStorageDirectory() + "/Download/" + userid + ".png");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }

    private void showLoadedFile(ImageView imageView, String s) {
        File file = new File(s);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Glide.with(context).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
    }

    private void removeFriend(int i) {
        final int id = i;
        MyAlert alert = new MyAlert(context, "Löschen", "Bist du sicher dass du " + friendlistObject.get(id).getName() + " löschen willst?");
        alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    friend.deleteFriendOnline(friendlistObject.get(id).getFriend_id());
                    friendlistObject.remove(id);
                    reloadListView();
                } catch (Exception e) {

                }
            }
        });
        alert.show();
    }
}

