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
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.Profile;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile.ProfileActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendRequestObject;
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
    private View header;
    private ListView listRequests;
    private int numOfRequests = 0;
    private TextView frTvNoFriends;
    private Friend friend;
    private ImageView friendImage;
    private MyAdapter adapter;
    private MyAdapter2 adapter2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<FriendlistObject> friendlistObject;
    private ListView list;
    private List<FriendRequestObject> friendRequestObjects;
    private Gson json = new Gson();
    private UserSessionObject session;
    private int numOfFriends = 0;
    private Context context;
    private Profile profile;
    private ImageHelper imageHelper;
    private boolean permissionGranted;

    private PopupWindow popupWindow;

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
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.orangeAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFriendlist();
            }
        });
        header = getLayoutInflater().inflate(R.layout.header_friends, null);
        list.addHeaderView(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOfRequests >0) {
                    showPopup(v);
                }
            }
        });
        checkAmountFriends();
        loadFriendInformation();
        loadRequestInformation();
        checkPermission();

    }

    private void loadRequestInformation() {
        long id = session.getUser_id();
        StringRequest request = new StringRequest(ServerConstants.URL_FRIEND_REQUESTS + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                positiveResponseRequests(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //negativeResponse(volleyError.getMessage());
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    private void positiveResponseRequests(String string) {
        Type listType = new TypeToken<List<FriendRequestObject>>() {}.getType();
        friendRequestObjects = json.fromJson(string, listType);
        numOfRequests = friendRequestObjects.size();
        setHeaderInfo();
    }

    private void setHeaderInfo() {
        TextView tvTitle = header.findViewById(R.id.tvHeaderTitle);
        TextView tvInfo = header.findViewById(R.id.tvHeaderInfo);
        ImageView imgNotification = header.findViewById(R.id.imgHeaderNotification);
        ImageView imgHeader = header.findViewById(R.id.imgHeader);
        String formattedTitle = String.format(getString(R.string.friend_requests_formatted_text), numOfRequests);
        if (numOfRequests>0) {
            tvTitle.setText(Html.fromHtml(formattedTitle));
            tvInfo.setText(R.string.click_to_open);
            header.setBackgroundColor(Color.WHITE);
            imgNotification.setVisibility(View.VISIBLE);
            imgHeader.setColorFilter(Color.BLACK);
            } else {
            tvTitle.setText(getString(R.string.no_friend_requests_text));
            tvInfo.setText("---");
            header.setBackgroundColor(Color.LTGRAY);
            imgNotification.setVisibility(View.GONE);
            imgHeader.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }

    }

    public void showPopup(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.popup_friends, null);

        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.setAnimationStyle(R.style.popupAnimation);

        listRequests = popupView.findViewById(R.id.lv_friend_requests);
        adapter2 = new MyAdapter2(friendRequestObjects,context);
        listRequests.setAdapter(adapter2);
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);
        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        if (friendRequestObjects.size()>5) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            popupWindow.setHeight(size.y/2);
        }
        int location[] = new int[2];
        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);
        // Using location, the PopupWindow will be displayed right under anchorView
        final TextView tvInfo = header.findViewById(R.id.tvHeaderInfo);

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1] + anchorView.getHeight());


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
        if (friendlistObject != null && adapter!=null) {
            //prepareProgressDialog();
            loadRequestInformation();
            adapter.notifyDataSetChanged();
        } else {
            numOfFriends = 0;
        }
        checkAmountFriends();
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
            Type listType = new TypeToken<List<FriendlistObject>>() {
            }.getType();
            friendlistObject = json.fromJson(offlineData, listType);
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
        friendlistObject = json.fromJson(string, listType);

        Collections.sort(friendlistObject, new Comparator<FriendlistObject>() {
            @Override
            public int compare(final FriendlistObject object1, final FriendlistObject object2) {
                int result = 0;

                String name1 = object1.getName().trim().toLowerCase();
                String name2 = object2.getName().trim().toLowerCase();
                for(int i = 0; i < name1.length(); i++){
                    char char1 = name1.charAt(i);
                    if(i <= name2.length()-1){
                        char char2 = name2.charAt(i);

                        if((char1 == char2)){
                            result = 0;
                        } else {
                            result = (char1 > char2) ? 1 : -1;
                            break;
                        }
                    } else {
                        result = 1;
                        break;
                    }
                }
                return result;
            }
        });

        numOfFriends = friendlistObject.size();
        reloadListView();
        //Save for offline use
        String sortedFriends = json.toJson(friendlistObject);
        friend.saveOfflineInformation(sortedFriends);
        swipeRefreshLayout.setRefreshing(false);
        adapter = new MyAdapter(context, friendlistObject);
        list.setAdapter(adapter);
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
    public class MyAdapter extends ArrayAdapter<FriendlistObject> {

        private List<FriendlistObject> dataSet;
        Context context;

        MyAdapter(Context c, List<FriendlistObject> friendlistObjects) {

            super(c, R.layout.row_friends, R.id.liFriendName, friendlistObjects);
            this.context = c;
            this.dataSet = friendlistObjects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            FriendlistObject actualObject = getItem(position);
            View row = inflater.inflate(R.layout.row_friends, parent, false);
            final TextView tvTitle = (TextView) row.findViewById(R.id.liFriendName);
            TextView tvDescription = (TextView) row.findViewById(R.id.liFriendNumOfShishas);
            final Button button = (Button) row.findViewById(R.id.liDeleteFriend);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFriend(position);
                }

            });
            final ImageView imgFriends = row.findViewById(R.id.liFriendImage);
            imgFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFriendsImagePopUp(imgFriends,position,tvTitle,button);
                }
            });
            long friendid = actualObject.getFriend_id();
            tvTitle.setText(actualObject.getName());
            tvDescription.setText(friendid+"");
            decideProfileImage(imgFriends, actualObject);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFriendsProfile(position,imgFriends,tvTitle,button);
                }
            });
            return row;
        }

    }

    private void decideProfileImage(ImageView imgFriends, FriendlistObject object) {
        long friendid = object.getFriend_id();
        String imageId = object.getLast_changed_icon_id();
        if (object.isHas_icon()) {
            //prepareProgressDialog();
                if (imageHelper.getIconId(friendid).equals(imageId)) {
                    Bitmap bitmap = imageHelper.loadImageFromStorage(friendid+"");
                    if (bitmap!=null) {
                        imageHelper.setRoundImageWithBitmap(imgFriends,bitmap);
                    } else {
                        imageHelper.loadFileFromServer(friendid,imgFriends,imageId);
                    }
                } else {
                    imageHelper.loadFileFromServer(friendid,imgFriends,imageId);
                }
        } else {
            imageHelper.setRoundImageDefault(imgFriends);
        }
    }

    private void openFriendsImagePopUp(final ImageView imgFriends, final int position, final TextView friendName, final Button deleteButton) {

        final View popupView = getLayoutInflater().inflate(R.layout.popup_profileimage, null);
        final ImageView imgProfile = popupView.findViewById(R.id.imgPopupProfileimage);
        final TextView tvName = popupView.findViewById(R.id.tvPopupProfileName);
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendsProfile(position,imgProfile,tvName, deleteButton);
            }
        });
        popupView.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(30);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        imageHelper.setRectImage(friendlistObject.get(position).getFriend_id()+"",imgProfile);
        tvName.setText(friendlistObject.get(position).getName());
        // Get the View's(the one that was clicked in the Fragment) location
        // Using location, the PopupWindow will be displayed right under anchorView
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        popupWindow.showAtLocation(imgFriends, Gravity.CENTER, 0,0);
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void openFriendsProfile(int i, ImageView friendImage, TextView friendName, Button friendDelete) {
        //Hier wird das Profil von einem Freund geöffnet
        Intent intent = new Intent(context, ProfileActivity.class);
        profile.setProfile(friendlistObject.get(i));
        Pair<View, String> p1 = Pair.create((View)friendImage, "profileimage");
        Pair<View, String> p2 = Pair.create((View)friendName, "friendname");
        Pair<View, String> p3 = Pair.create((View)friendDelete, "deletebutton");

        intent.putExtra("FRIEND_NAME",friendName.getText().toString());
        intent.putExtra("FRIEND_ID",friendlistObject.get(i).getFriend_id());

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), p1, p2,p3);
        startActivity(intent,options.toBundle());
    }


    private void showLoadedFile(ImageView imageView, String s) {
        if (s==null) {
            Glide.with(context).load(R.drawable.sheeshopa).apply(RequestOptions.circleCropTransform()).into(imageView);
        } else {
            File file = new File(s);
            if (file.length() < 300) {
                Glide.with(context).load(R.drawable.sheeshopa).apply(RequestOptions.circleCropTransform()).into(imageView);
                file.delete();
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                Glide.with(context).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
            }
        }
    }

    private void removeFriend(int i) {
        final int id = i;
        MyAlert alert = new MyAlert(context, getString(R.string.delete_text), String.format(getString(R.string.friend_delete_formatted_text), friendlistObject.get(id).getName()));
        alert.setNegativeButton(getString(R.string.no_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {
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

    class MyAdapter2 extends ArrayAdapter<FriendRequestObject> {

        private List<FriendRequestObject> dataSet;
        Context context;

        public MyAdapter2(List<FriendRequestObject> dataSet,Context c) {
            super(c, R.layout.row_friend_request, R.id.lv_friend_requests,dataSet);
            this.context = c;
            this.dataSet = dataSet;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_friend_request, parent, false);
            final TextView tvName = (TextView) row.findViewById(R.id.tv_request_name);
            TextView tvDate = (TextView) row.findViewById(R.id.tv_request_date);
            tvName.setText(getItem(position).getFriend_name());

            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyy hh:mm");
            Calendar calendar = getItem(position).getAdd_date();
            String formatted = format1.format(calendar.getTime());
            tvDate.setText(formatted);

            Button btnAccept = (Button) row.findViewById(R.id.btn_accept_friend);
            Button btnDenie = row.findViewById(R.id.btn_denie_friend);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptFriend(position,true);
                    Toast.makeText(context, R.string.accepted_text,Toast.LENGTH_SHORT).show();
                }

            });
            btnDenie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptFriend(position,false);
                    Toast.makeText(context, R.string.denied_text,Toast.LENGTH_SHORT).show();
                }
            });
            return row;
        }

    }

    private void acceptFriend(final int pos, boolean accept) {
        if (friendRequestObjects.size()>0) {
            final FriendRequestObject object = friendRequestObjects.get(pos);
            long rel_id = object.getUnverified_relation_id();
            StringRequest request = new StringRequest(ServerConstants.URL_FRIEND_ACCEPT + rel_id + "&accept="+accept, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {
                    if (string.equals("OK") || string.equals("deleted")) {
                        friendRequestObjects.remove(object);
                        adapter2.notifyDataSetChanged();
                        if (numOfRequests==1) {
                            popupWindow.dismiss();
                        }
                        loadRequestInformation();
                        loadFriendInformation();
                    } else {
                        Toast.makeText(context, string, Toast.LENGTH_SHORT);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, R.string.some_error_occured_text,Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(getContext());
            rQueue.add(request);
        }
    }
}

